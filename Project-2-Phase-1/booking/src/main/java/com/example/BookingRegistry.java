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

// booking-registry-actor
public class BookingRegistry extends AbstractBehavior<BookingRegistry.Command> {

  // Stores show actors for each showId
  public static final Map<Integer, ActorRef<ShowRegistry.Command>> showActors = new HashMap<>();

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

  // booking-case-classes
  public final static record Booking(String name, int age, String countryOfResidence) {
  }

  public final static record Bookings(List<Booking> bookings) {
  }
  // booking-case-classes

  private final List<Booking> bookings = new ArrayList<>();

  private BookingRegistry(ActorContext<Command> context) {
    super(context);
    String[] shows = { "1,1,Youth in Revolt,50,40",
        "2,1,Leap Year,55,30",
        "3,1,Remember Me,60,55",
        "4,2,Fireproof,65,65",
        "5,2,Beginners,55,50",
        "6,3,Music and Lyrics,75,40",
        "7,3,The Back-up Plan,65,60",
        "8,4,WALL-E,45,55",
        "9,4,Water For Elephants,50,45",
        "10,5,What Happens in Vegas,65,65",
        "11,6,Tangled,55,40",
        "12,6,The Curious Case of Benjamin Button,65,50",
        "13,7,Rachel Getting Married,40,60",
        "14,7,New Year's Eve,35,45",
        "15,7,The Proposal,45,55",
        "16,8,The Time Traveler's Wife,75,65",
        "17,8,The Invention of Lying,50,40",
        "18,9,The Heartbreak Kid,60,50",
        "19,10,The Duchess,70,60",
        "20,10,Mamma Mia!,40,45" };

    for (String line : shows) {
      String[] str = line.split(",");
      int show_id = Integer.parseInt(str[0]);
      int theatre_id = Integer.parseInt(str[1]);
      String title = str[2];
      int price = Integer.parseInt(str[3]);
      int seats_available = Integer.parseInt(str[4]);

      showActors.put(show_id,
          context.spawn(ShowRegistry.create(show_id, theatre_id, title, price, seats_available), "Show" + show_id));
    }
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(BookingRegistry::new);
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        // .onMessage(GetBookings.class, this::onGetBookings)
        // .onMessage(CreateBooking.class, this::onCreateBooking)
        // .onMessage(GetBooking.class, this::onGetBooking)
        // .onMessage(DeleteBooking.class, this::onDeleteBooking)
        .build();
  }

  // private Behavior<Command> onGetBookings(GetBookings command) {
  // // We must be careful not to send out bookings since it is mutable
  // // so for this response we need to make a defensive copy
  // command.replyTo().tell(new Bookings(Collections.unmodifiableList(new
  // ArrayList<>(bookings))));
  // return this;
  // }

  // private Behavior<Command> onCreateBooking(CreateBooking command) {
  // bookings.add(command.booking());
  // command.replyTo().tell(new ActionPerformed(String.format("Booking %s
  // created.", command.booking().name())));
  // return this;
  // }

  // private Behavior<Command> onGetBooking(GetBooking command) {
  // Optional<Booking> maybeBooking = bookings.stream()
  // .filter(booking -> booking.name().equals(command.name()))
  // .findFirst();
  // command.replyTo().tell(new GetBookingResponse(maybeBooking));
  // return this;
  // }

  // private Behavior<Command> onDeleteBooking(DeleteBooking command) {
  // bookings.removeIf(booking -> booking.name().equals(command.name()));
  // command.replyTo().tell(new ActionPerformed(String.format("Booking %s
  // deleted.", command.name)));
  // return this;
  // }

}
// booking-registry-actor