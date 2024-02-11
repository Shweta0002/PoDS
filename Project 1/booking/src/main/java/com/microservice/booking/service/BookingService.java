package com.microservice.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Booking;

@Service
public interface BookingService {

	boolean getBookingById(Long bookingId);

	Booking addBooking(Booking booking);

	void deleteAllBooking();

	List<Booking> getAllBookingsByUserId(Long user_id);

}
