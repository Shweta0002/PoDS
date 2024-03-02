package com.microservice.booking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Booking;

@Service
public interface BookingService {

	// Returns true if a booking ID exists
	boolean getBookingById(Integer bookingId);

	// Adds new booking
	ResponseEntity<?> addBooking(Booking booking);

	// Deletes all bookings
	ResponseEntity<?> deleteAllBooking();

	// Returns all bookings of a user ID
	List<Booking> getAllBookingsByUserId(Integer user_id);

	// Return all bookings
	List<Booking> getAllBookings();

	// Deletes all bookings of a user ID and show ID
	ResponseEntity<?> deleteBookingofUserByShowId(Integer user_id, Integer show_id);

	// Deletes all bookings of a user
	ResponseEntity<?> deleteAllUserBookings(Integer user_id);

}
