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
    public Integer id;
    public Integer theatre_id;
    String title;
    public Integer price;
    public Integer seats_available;
    public static final List<ShowRegistry.Booking> bookings = new ArrayList<>();

    public final static record GetShow(Integer show_id, ActorRef<Show> replyTo) implements Command {
    }

    public final static record GetShowOfTheatre(Integer show_id, Integer theatre_id, ActorRef<Show> replyTo)
            implements Command {
    }

    public final static record Show(Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
    }

    public final static record Booking(Integer id, Integer user_id, Integer show_id, Integer seats_booked) {
    }

    public final static record Response(String description) {
    }

    public final static record AddBooking(Booking booking, ActorRef<ShowRegistry.Booking> replyTo) implements Command {
    }

    public final static record DeleteUserBooking(Integer user_id, Integer show_id, ActorRef<Response> replyTo)
            implements Command {
    }

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
                .onMessage(GetShowOfTheatre.class, this::onGetShowOfTheatre)
                .onMessage(AddBooking.class, this::onAddBooking)
                .onMessage(DeleteUserBooking.class, this::onDeleteUserBooking)
                .build();
    }

    private Behavior<Command> onGetShow(GetShow command) {
        if (this.id.equals(command.show_id())) {
            command.replyTo().tell(new Show(this.id, this.theatre_id, this.title, this.price, this.seats_available));

        }
        return this;
    }

    private Behavior<Command> onGetShowOfTheatre(GetShowOfTheatre command) {
        if (this.theatre_id.equals(command.theatre_id())) {
            command.replyTo().tell(new Show(this.id, this.theatre_id, this.title, this.price, this.seats_available));
        } else {
            command.replyTo().tell(new Show(null, this.theatre_id, this.title, this.price, this.seats_available));
        }
        return this;
    }

    private Behavior<Command> onAddBooking(AddBooking command) {
        Integer id = command.booking.id;
        Integer user_id = command.booking.user_id;
        Integer show_id = command.booking.show_id;
        Integer seats_booked = command.booking.seats_booked;
        this.seats_available = this.seats_available - seats_booked;
        Booking newBooking = new Booking(id, user_id, show_id, seats_booked);
        bookings.add(newBooking);
        command.replyTo().tell(newBooking);
        return this;
    }

    private Behavior<Command> onDeleteUserBooking(DeleteUserBooking command) {
        ListIterator<Booking> iter = bookings.listIterator();
        while (iter.hasNext()) {
            Booking currentBooking = iter.next();
            if (Objects.equals(currentBooking.user_id, command.user_id)) {
                seats_available += currentBooking.seats_booked;
                iter.remove();
            }
        }
        command.replyTo().tell(new Response("Done"));
        return this;
    }

}