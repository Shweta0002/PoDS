package com.example;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.example.BookingRegistry.Booking;
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

/**
 * Routes can be defined in separated classes like shown in here
 */
// #booking-routes-class
public class BookingRoutes {
  // #booking-routes-class
  private final static Logger log = LoggerFactory.getLogger(BookingRoutes.class);
  private final ActorRef<BookingRegistry.Command> bookingRegistryActor;
  private final Duration askTimeout;
  private final Scheduler scheduler;

  public BookingRoutes(ActorSystem<?> system, ActorRef<BookingRegistry.Command> bookingRegistryActor) {
    this.bookingRegistryActor = bookingRegistryActor;
    scheduler = system.scheduler();
    askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
  }

  private CompletionStage<BookingRegistry.GetBookingResponse> getBooking(String name) {
    return AskPattern.ask(bookingRegistryActor, ref -> new BookingRegistry.GetBooking(name, ref), askTimeout,
        scheduler);
  }

  private CompletionStage<BookingRegistry.ActionPerformed> deleteBooking(String name) {
    return AskPattern.ask(bookingRegistryActor, ref -> new BookingRegistry.DeleteBooking(name, ref), askTimeout,
        scheduler);
  }

  private CompletionStage<BookingRegistry.Bookings> getBookings() {
    return AskPattern.ask(bookingRegistryActor, BookingRegistry.GetBookings::new, askTimeout, scheduler);
  }

  private CompletionStage<BookingRegistry.ActionPerformed> createBooking(Booking booking) {
    return AskPattern.ask(bookingRegistryActor, ref -> new BookingRegistry.CreateBooking(booking, ref), askTimeout,
        scheduler);
  }

  /**
   * This method creates one route (of possibly many more that will be part of
   * your Web App)
   */
  // #all-routes
  public Route bookingRoutes() {
    return pathPrefix("bookings", () -> concat(
        // #bookings-get-delete
        pathEnd(() -> concat(
            get(() -> onSuccess(getBookings(),
                bookings -> complete(StatusCodes.OK, bookings, Jackson.marshaller()))),
            post(() -> entity(
                Jackson.unmarshaller(Booking.class),
                booking -> onSuccess(createBooking(booking), performed -> {
                  log.info("Create result: {}", performed.description());
                  return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                }))))),
        // #bookings-get-delete
        // #bookings-get-post
        path(PathMatchers.segment(), (String name) -> concat(
            get(() ->
            // #retrieve-booking-info
            rejectEmptyResponse(() -> onSuccess(getBooking(name),
                performed -> complete(StatusCodes.OK, performed.maybeBooking(), Jackson.marshaller())))
            // #retrieve-booking-info
            ),
            delete(() ->
            // #bookings-delete-logic
            onSuccess(deleteBooking(name), performed -> {
              log.info("Delete result: {}", performed.description());
              return complete(StatusCodes.OK, performed, Jackson.marshaller());
            })
            // #bookings-delete-logic
            )))
    // #bookings-get-post
    ));
  }
  // #all-routes

}
