package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.io.*;

public class ShowRegistry extends AbstractBehavior<ShowRegistry.Command> {

    // actor protocol
    sealed interface Command {
    }

    // Instance variables
    private final Integer id;
    private final Integer theatre_id;
    private final String title;
    private final Integer price;
    private final Integer seats_available;

    public final static record GetShow(Integer show_id, ActorRef<Show> replyTo) implements Command {
    }

    // public final static record GetShowsAtTheatre(Integer theatre_id,
    // ActorRef<Show> replyTo) implements Command {
    // }

    public final static record ActionPerformed(String description) implements Command {
    }

    public final static record Show(Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
    }

    // public final static record ShowsAtTheatre(List<Show> showsAtTheatre) {
    // }

    // private final List<Show> showsAtTheatre = new ArrayList<>();

    private ShowRegistry(ActorContext<Command> context, Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
        super(context);
        this.id = id;
        this.theatre_id = theatre_id;
        this.title = title;
        this.price = price;
        this.seats_available = seats_available;
    }

    public static Behavior<Command> create(Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
        return Behaviors.setup(context -> new ShowRegistry(context, id, theatre_id, title, price, seats_available));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetShow.class, this::onGetShow)
                .build();
    }

    private Behavior<Command> onGetShow(GetShow command) {
        if (this.id.equals(command.show_id())) {
            command.replyTo().tell(new Show(this.id, this.theatre_id, this.title, this.price, this.seats_available));

        }
        return this;
    }

}