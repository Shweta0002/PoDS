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

//#booking-registry-actor
public class BookingRegistry extends AbstractBehavior<BookingRegistry.Command> {

  // actor protocol
  sealed interface Command {
  }

  public final static record GetBookings(ActorRef<Bookings> replyTo) implements Command {
  }

  public final static record CreateBooking(Booking booking, ActorRef<ActionPerformed> replyTo) implements Command {
  }

  public final static record GetBookingResponse(Optional<Booking> maybeBooking) {
  }

  public final static record GetBooking(String name, ActorRef<GetBookingResponse> replyTo) implements Command {
  }

  public final static record DeleteBooking(String name, ActorRef<ActionPerformed> replyTo) implements Command {
  }

  public final static record ActionPerformed(String description) implements Command {
  }

  // #booking-case-classes
  public final static record Booking(String name, int age, String countryOfResidence) {
  }

  public final static record Bookings(List<Booking> bookings) {
  }
  // #booking-case-classes

  private final List<Booking> bookings = new ArrayList<>();

  private BookingRegistry(ActorContext<Command> context) {
    super(context);
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(BookingRegistry::new);
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(GetBookings.class, this::onGetBookings)
        .onMessage(CreateBooking.class, this::onCreateBooking)
        .onMessage(GetBooking.class, this::onGetBooking)
        .onMessage(DeleteBooking.class, this::onDeleteBooking)
        .build();
  }

  private Behavior<Command> onGetBookings(GetBookings command) {
    // We must be careful not to send out bookings since it is mutable
    // so for this response we need to make a defensive copy
    command.replyTo().tell(new Bookings(Collections.unmodifiableList(new ArrayList<>(bookings))));
    return this;
  }

  private Behavior<Command> onCreateBooking(CreateBooking command) {
    bookings.add(command.booking());
    command.replyTo().tell(new ActionPerformed(String.format("Booking %s created.", command.booking().name())));
    return this;
  }

  private Behavior<Command> onGetBooking(GetBooking command) {
    Optional<Booking> maybeBooking = bookings.stream()
        .filter(booking -> booking.name().equals(command.name()))
        .findFirst();
    command.replyTo().tell(new GetBookingResponse(maybeBooking));
    return this;
  }

  private Behavior<Command> onDeleteBooking(DeleteBooking command) {
    bookings.removeIf(booking -> booking.name().equals(command.name()));
    command.replyTo().tell(new ActionPerformed(String.format("Booking %s deleted.", command.name)));
    return this;
  }

}
// #booking-registry-actor