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
	private Long bookingId;

	private Long show_id;
	private Integer user_id;
	private Long seats_booked;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getShow_id() {
		return show_id;
	}

	public void setShow_id(Long show_id) {
		this.show_id = show_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Long getSeats_booked() {
		return seats_booked;
	}

	public void setSeats_booked(Long seats_booked) {
		this.seats_booked = seats_booked;
	}

	public Booking(Long bookingId, Long show_id, Integer user_id, Long seats_booked) {
		super();
		this.bookingId = bookingId;
		this.show_id = show_id;
		this.user_id = user_id;
		this.seats_booked = seats_booked;
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", show_id=" + show_id + ", user_id=" + user_id + ", seats_booked="
				+ seats_booked + "]";
	}

	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}
}
