package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.booking.VO.RequestWalletTranscationTemplate;
import com.microservice.booking.VO.Users;
import com.microservice.booking.VO.Wallet;
import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.BookingRepository;
import com.microservice.booking.repository.ShowRepository;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private ShowRepository showRepo;

	@Autowired
	private RestTemplate restTemplate;

	public BookingServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getBookingById(Long bookingId) {

		return bookingRepo.existsById(bookingId);
	}

	@Override
	public ResponseEntity<?> addBooking(Booking booking) {
		Long show_id = booking.getShow_id();
		Integer User_id = booking.getUser_id();
		Show show = showRepo.getbyShowId(show_id);

		if (show == null) {
			return ResponseEntity.badRequest().body("Show not found");
		}
		System.out.println("erro1");
		ResponseEntity<Users> userResponse = restTemplate.getForEntity("http://localhost:8080/users/" + User_id,
				Users.class);
		ResponseEntity<Wallet> wallet = restTemplate.getForEntity("http://localhost:8082/wallets/" + User_id,
				Wallet.class);
		System.out.println("erro2");
		if (userResponse.getStatusCode().is2xxSuccessful() && wallet.getStatusCode().is2xxSuccessful()) {
			if (show.getSeats_available() < booking.getSeats_booked()) {
				return ResponseEntity.badRequest().body("Seats not available");
			}
			System.out.println("erro3");
			// Calculate the total cost based on the price of the show and the number of
			// seats booked
			Integer totalCost = (int) (show.getPrice() * booking.getSeats_booked());
			if (wallet.getBody().getBalance() < totalCost) {
				return ResponseEntity.badRequest().body("Insufficient Balance");
			}

			// Deduct the amount from the user's wallet
			System.out.println("erro4");
			HttpHeaders headers = new HttpHeaders();
			// set all headers
			headers.setContentType(MediaType.APPLICATION_JSON);
			RequestWalletTranscationTemplate data = new RequestWalletTranscationTemplate("debit", totalCost);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
			ResponseEntity<Wallet> response = restTemplate.exchange(
					"http://localhost:8082/wallets/" + User_id, HttpMethod.PUT, requestEntity, Wallet.class);
			System.out.println("erro5");
			Booking booking1 = new Booking();
			booking1.setUser_id(User_id);
			booking1.setShow_id(show_id);
			booking1.setSeats_booked(booking.getSeats_booked());

			// Update the number of seats available for the show
			show.setSeats_available(show.getSeats_available() - booking.getSeats_booked());

			// Save the changes
			showRepo.save(show);
			bookingRepo.save(booking1);
			System.out.println("error8");

			return ResponseEntity.ok("Booking created successfully");
		} else {
			// User not found, return a 400 Bad Request response
			return ResponseEntity.badRequest().body("User not found");
		}

	}

	@Override
	public void deleteAllBooking() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Booking> getAllBookingsByUserId(Integer user_id) {
		// TODO Auto-generated method stub
		return bookingRepo.findAllbyUserId(user_id);
	}

	@Override
	public List<Booking> getAllBookings() {
		// TODO Auto-generated method stub
		return bookingRepo.findAll();
	}

	@Override
	public void deleteBookingofUserByShowId(Integer user_id, Long show_id) {
		List<Booking> alluserBookings = getAllBookingsByUserId(user_id);

	}

	@Override
	public ResponseEntity<?> deleteAllUserBookings(Integer user_id) {
		List<Booking> userBookings = getAllBookingsByUserId(user_id);

		if (userBookings == null || userBookings.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// 1. seat to be returned to available pool (show_id - add seats_booked)
		for (Booking b : userBookings) {
			Show show = showRepo.getbyShowId(b.getShow_id());
			show.setSeats_available(show.getSeats_available() + b.getSeats_booked());
			showRepo.save(show);

			// 2. The amounts that were taken to make these userBookings are returned to the
			// user’s wallet.
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Integer totalCost = (int) (b.getSeats_booked() * show.getPrice());
			RequestWalletTranscationTemplate data = new RequestWalletTranscationTemplate("credit", totalCost);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
			restTemplate.exchange(
					"http://localhost:8082/wallets/" + user_id, HttpMethod.PUT, requestEntity, Wallet.class);

			bookingRepo.delete(b);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
