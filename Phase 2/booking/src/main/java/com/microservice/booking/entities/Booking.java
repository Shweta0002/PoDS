package com.microservice.booking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer show_id;
	private Integer user_id;
	private Integer seats_booked;

	public Booking() {
		super();
	}

	public Booking(Integer id, Integer show_id, Integer user_id, Integer seats_booked) {
		super();
		this.id = id;
		this.show_id = show_id;
		this.user_id = user_id;
		this.seats_booked = seats_booked;
	}

	public Integer getId() {
		return this.id;
	}

	public void setBookingId(Integer id) {
		this.id = id;
	}

	public Integer getShow_id() {
		return this.show_id;
	}

	public void setShow_id(Integer show_id) {
		this.show_id = show_id;
	}

	public Integer getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getSeats_booked() {
		return this.seats_booked;
	}

	public void setSeats_booked(Integer seats_booked) {
		this.seats_booked = seats_booked;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", show_id=" + show_id + ", user_id=" + user_id + ", seats_booked="
				+ seats_booked + "]";
	}

}
