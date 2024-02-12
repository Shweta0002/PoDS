package com.microservice.booking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Booking;

@Service
public interface BookingService {

	boolean getBookingById(Long bookingId);

	ResponseEntity<?> addBooking(Booking booking);

	void deleteAllBooking();

	List<Booking> getAllBookingsByUserId(Integer user_id);

	List<Booking> getAllBookings();

	ResponseEntity<?> deleteBookingofUserByShowId(Integer user_id, Long show_id);
	ResponseEntity<?> deleteAllUserBookings(Integer user_id);


}
