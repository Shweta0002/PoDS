package com.example;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.*;

import com.example.BookingRegistry;
import com.example.ShowRegistry;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// booking-routes-class
public class BookingRoutes {
  private final static Logger log = LoggerFactory.getLogger(BookingRoutes.class);
  private final ActorRef<BookingRegistry.Command> bookingRegistryActor;
  private final Duration askTimeout;
  private final Scheduler scheduler;

  public BookingRoutes(ActorSystem<?> system, ActorRef<BookingRegistry.Command> bookingRegistryActor) {
    this.bookingRegistryActor = bookingRegistryActor;
    scheduler = system.scheduler();
    askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
  }

  private CompletionStage<BookingRegistry.TheatresReply> getTheatres() {
    return AskPattern.ask(bookingRegistryActor, BookingRegistry.GetTheatres::new,
        askTimeout, scheduler);
  }

  private CompletionStage<ShowRegistry.Show> getShow(Integer show_id) {
    ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
    return AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.GetShow(show_id, ref), askTimeout, scheduler);
  }

  private CompletionStage<List<ShowRegistry.Show>> getShowsOfTheatre(Integer theatre_id) {
    List<CompletionStage<ShowRegistry.Show>> futures = new ArrayList<>();

    for (int show_id : BookingRegistry.showActors.keySet()) {
      ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
      futures.add(
          AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.GetShowOfTheatre(show_id, theatre_id, ref),
              askTimeout,
              scheduler));

    }

    CompletableFuture<ShowRegistry.Show>[] futuresArray = futures.toArray(new CompletableFuture[0]);
    CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futuresArray);

    return allOfFuture.thenApply(v -> {
      return futures.stream()
          .map(CompletionStage::toCompletableFuture)
          .map(CompletableFuture::join)
          .filter(show -> show != null && show.id() != null) // Filter out null shows and shows with null IDs
          .collect(Collectors.toList()); // Collect non-null shows into a list
    });
  }

  public Route bookingRoutes() {
    return concat(
        pathPrefix("theatres",
            () -> pathEnd(() -> get(
                () -> onSuccess(getTheatres(), theatres -> complete(StatusCodes.OK, theatres,
                    Jackson.marshaller()))))),
        pathPrefix("shows", () -> path(PathMatchers.segment(), (String show_id) -> get(() -> {
          return onSuccess(getShow(Integer.parseInt(show_id)), showDetails -> {
            if (showDetails != null) {
              return complete(StatusCodes.OK, showDetails, Jackson.marshaller());
            } else {
              return complete(StatusCodes.NOT_FOUND, "Show not found");
            }
          });
        }))),
        pathPrefix("shows/theatres", () -> path(PathMatchers.segment(), (String theatre_id) -> get(() -> {
          return onSuccess(getShowsOfTheatre(Integer.parseInt(theatre_id)), showDetails -> {
            if (showDetails != null) {
              return complete(StatusCodes.OK, showDetails, Jackson.marshaller());
            } else {
              return complete(StatusCodes.NOT_FOUND, "Shows not found for the particular theatre");
            }
          });
        }))));
  }
}
