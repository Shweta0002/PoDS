package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import java.util.*;

import com.CborSerializable;

public class WorkerActor extends AbstractBehavior<WorkerActor.Command>  {
    //public Map<Integer,ActorRef<ShowActor.Command>> showActors;
    public Integer user_id;
    public ActorRef<BookingRegistry.Bookings> replyTo;
    public ActorRef<ShowActor.Response> replyToDelete;
    public ActorRef<WorkerActor.Command> selfRef;

    public ActorRef<WorkerActor.DeleteResponse> replyToDeleteAll;
    public Integer numProcessed;
    public boolean flag;
    List<BookingRegistry.Booking> userBookings = new ArrayList<>();
    List<ShowActor.Show> showOfTheatre =new ArrayList<>();

    sealed interface Command extends CborSerializable{}
    public final ClusterSharding sharding;
    public final record GetUserBookings(ActorRef<BookingRegistry.Bookings> replyTo, Integer user_id, ClusterSharding sharding) implements Command{}
    public final record DeleteAllUserBookings(ActorRef<ShowActor.Response> replyTo, Integer user_id, ClusterSharding sharding) implements Command{}

    public final record DeleteAllBookings(ActorRef<WorkerActor.DeleteResponse> replyTo, ClusterSharding sharding) implements Command{}

    public final record GetShowBookings(List<ShowActor.Booking> bookings) implements Command{}

    public final record Booking(Integer id,Integer user_id,Integer show_id , Integer seats_booked)implements CborSerializable{}
    public final record Bookings(List<ShowActor.Booking> bookings)implements CborSerializable{}
    public final record Showlist(List<ShowActor.Show> showlist)implements CborSerializable{}

    public final record Response(boolean flag) implements Command{}

    public final record DeleteResponse(boolean flag) implements Command{}



    public final record ShowByTheatre(Integer theatre_id,ActorRef<Showlist> replyTo, ClusterSharding sharding) implements Command{}
    public final record TheatreOfShow(Boolean status,ShowActor.Show showDetails,Integer i,ActorRef<Showlist> replyTo) implements Command{}

    

    //private WorkerActor(ActorContext<Command> context, Map<Integer,ActorRef<ShowActor.Command>> showActors) {
    private WorkerActor(ActorContext<Command> context,ClusterSharding sharding) {
        super(context);
        this.numProcessed = 0;
        this.sharding = sharding;
        this.selfRef = getContext().getSelf(); 
    }

    public static Behavior<Command> create(ClusterSharding sharding) {
        return Behaviors.setup(context->new WorkerActor(context,sharding));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetShowBookings.class, this::onGetShowBookings)
                .onMessage(GetUserBookings.class, this::onGetUserBookings)
                .onMessage(ShowByTheatre.class,this::onShowByTheatre)
                .onMessage(TheatreOfShow.class,this::onTheatreOfShow)
                .onMessage(Response.class,this::onGetResponse)
                .onMessage(DeleteAllUserBookings.class, this::onDeleteAllUserBookings)
                .onMessage(DeleteAllBookings.class, this::onDeleteAllBookings)
                .onMessage(DeleteResponse.class, this::onGetDeleteResponse)
                .build();
    }

    private Behavior<Command> onGetShowBookings(GetShowBookings message) {
        this.numProcessed += 1;
        for (ShowActor.Booking b : message.bookings) {
            this.userBookings.add(new BookingRegistry.Booking(b.id(), b.user_id(), b.show_id(), b.seats_booked()));
        }
        if(this.numProcessed == 20){
            List<BookingRegistry.Booking> result = new ArrayList<>();
            for (BookingRegistry.Booking b : this.userBookings){
                if(b.user_id() == this.user_id)
                    result.add(b);
            }
            this.replyTo.tell(new BookingRegistry.Bookings(Collections.unmodifiableList(new ArrayList<>(result))));
            return Behaviors.stopped();
        }
        return this;
    }

    private Behavior<Command> onGetUserBookings(GetUserBookings message) {
        ClusterSharding shard = this.sharding;
        this.replyTo = message.replyTo;
        this.user_id = message.user_id;
        for (int i=1;i<=20;i++) {
            EntityRef<ShowActor.Command> ref = sharding.entityRefFor(ShowActor.TypeKey, Integer.toString(i));
            ref.tell(new ShowActor.GetBookings(this.selfRef));
        }
        return this;
    }


    private Behavior<Command> onTheatreOfShow(TheatreOfShow message) {
        if(message.status)
            this.showOfTheatre.add(message.showDetails);
        if(message.i == 0){
            message.replyTo().tell(new Showlist(showOfTheatre));
            return Behaviors.stopped();
        }
        return this;
    }

    private Behavior<Command> onShowByTheatre(ShowByTheatre message) {
        ClusterSharding shard = this.sharding;
        int i=20;
        for (int k=1;k<=20;k++) {
            EntityRef<ShowActor.Command> ref = shard.entityRefFor(ShowActor.TypeKey, Integer.toString(i));
           ref.tell(new ShowActor.GetTheatre(this.selfRef, --i, message.theatre_id,message.replyTo));
        }
        return this;
    }


    private Behavior<Command> onGetResponse(Response message) {
        this.numProcessed += 1;
        if(message.flag())
            this.flag = true;

        if(this.numProcessed == 20){
            this.replyToDelete.tell(new ShowActor.Response(this.flag));
            return Behaviors.stopped();
        }
        return this;
    }

    private Behavior<Command> onDeleteAllUserBookings(DeleteAllUserBookings message) {
        ClusterSharding shard = this.sharding;
        this.replyToDelete = message.replyTo;
            EntityRef<ShowActor.Command> ref = shard.entityRefFor(ShowActor.TypeKey, Integer.toString(1));
            ref.tell(new ShowActor.DeleteUserBooking(message.user_id, 1, null, this.selfRef));
        return this;
    }


    private Behavior<Command> onGetDeleteResponse(DeleteResponse message) {
        this.numProcessed += 1;
        if(message.flag()){
            this.flag = true;
        }
        

        if(this.numProcessed == 21){
            this.replyToDeleteAll.tell(new DeleteResponse(this.flag));
            return this;
        }
        else {
        EntityRef<ShowActor.Command> ref = sharding.entityRefFor(ShowActor.TypeKey, Integer.toString(numProcessed));
        ref.tell(new ShowActor.DeleteAllBookings(this.selfRef));
        }
        return this;
    }

    private Behavior<Command> onDeleteAllBookings(DeleteAllBookings message) {
        ClusterSharding shard = this.sharding;
        this.replyToDeleteAll = message.replyTo;
        getContext().getLog().info("Show id: "+user_id);
        EntityRef<ShowActor.Command> ref = shard.entityRefFor(ShowActor.TypeKey, Integer.toString(1));
        ref.tell(new ShowActor.DeleteAllBookings(this.selfRef));

        return this;
    }

}