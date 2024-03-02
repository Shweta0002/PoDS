package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;

import com.microservice.booking.VO.RequestWalletTransactionTemplate;
import com.microservice.booking.VO.Users;
import com.microservice.booking.VO.Wallet;
import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.BookingRepository;
import com.microservice.booking.repository.ShowRepository;

@Transactional
@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private ShowRepository showRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean getBookingById(Integer bookingId) {
		return bookingRepo.existsById(bookingId);
	}

	@Override
	public ResponseEntity<?> addBooking(Booking booking) {
		Integer show_id = booking.getShow_id();
		Integer User_id = booking.getUser_id();
		Show show = showRepo.getbyShowId(show_id);

		// Ensure the show exists where booking is to be added
		if (show == null) {
			return ResponseEntity.badRequest().body("Show not found");
		}

		ResponseEntity<Wallet> wallet = null;

		// Ensure the user exists for whom booking is to be added
		try {
			restTemplate.getForEntity("http://users-microservice:8080/users/" + User_id,
					Users.class);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("User not found");
		}
		try {
			wallet = restTemplate.getForEntity("http://wallet-microservice:8082/wallets/" + User_id,
					Wallet.class);
		} catch (Exception e) {
			// Initializing new user wallet with balance 0
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			RequestWalletTransactionTemplate data = new RequestWalletTransactionTemplate("credit", 0);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
			restTemplate.exchange(
					"http://wallet-microservice:8082/wallets/" + User_id, HttpMethod.PUT, requestEntity, Wallet.class);

			return ResponseEntity.badRequest().body("Insufficient Balance");

		}

		// Ensure the available seats for the show is greater or equal to number of
		// seats to be booked
		if (show.getSeats_available() < booking.getSeats_booked()) {
			return ResponseEntity.badRequest().body("Seats not available");
		}

		Integer totalCost = (int) (show.getPrice() * booking.getSeats_booked());
		// Ensure user has sufficient balance for completing booking
		if (wallet.getBody().getBalance() < totalCost) {
			return ResponseEntity.badRequest().body("Insufficient Balance");
		}

		// Deduct the amount from the users wallet
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestWalletTransactionTemplate data = new RequestWalletTransactionTemplate("debit", totalCost);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
		restTemplate.exchange(
				"http://wallet-microservice:8082/wallets/" + User_id, HttpMethod.PUT, requestEntity, Wallet.class);
		Booking bookingOfUserWithSameShow_id = bookingRepo.findByUserIdShowId(User_id, show_id);
		Booking newBooking;
		if (bookingOfUserWithSameShow_id != null) {
			bookingOfUserWithSameShow_id
					.setSeats_booked(booking.getSeats_booked() + bookingOfUserWithSameShow_id.getSeats_booked());
			newBooking = bookingRepo.save(bookingOfUserWithSameShow_id);
		} else {
			Booking booking1 = new Booking();
			booking1.setUser_id(User_id);
			booking1.setShow_id(show_id);
			booking1.setSeats_booked(booking.getSeats_booked());
			newBooking = bookingRepo.save(booking1);
		}

		// Update the number of seats available for the show
		show.setSeats_available(show.getSeats_available() - booking.getSeats_booked());

		// Save the changes
		showRepo.save(show);
		return new ResponseEntity<Booking>(newBooking, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteAllBooking() {
		List<Booking> bookings = getAllBookings();

		// If no bookings exist
		if (bookings == null || bookings.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// seat to be returned to available pool
		for (Booking b : bookings) {
			Show show = showRepo.getbyShowId(b.getShow_id());
			show.setSeats_available(show.getSeats_available() + b.getSeats_booked());
			showRepo.save(show);

			// The amounts to make these bookings are returned to the user’s wallet.
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Integer totalCost = (int) (b.getSeats_booked() * show.getPrice());
			RequestWalletTransactionTemplate data = new RequestWalletTransactionTemplate("credit", totalCost);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
			restTemplate.exchange(
					"http://wallet-microservice:8082/wallets/" + b.getUser_id(), HttpMethod.PUT, requestEntity,
					Wallet.class);

			bookingRepo.delete(b);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public List<Booking> getAllBookingsByUserId(Integer user_id) {
		return bookingRepo.findAllbyUserId(user_id);
	}

	@Override
	public List<Booking> getAllBookings() {
		return bookingRepo.findAll();
	}

	@Override
	public ResponseEntity<?> deleteBookingofUserByShowId(Integer user_id, Integer show_id) {
		List<Booking> alluserBookings = getAllBookingsByUserId(user_id);
		for (Booking b : alluserBookings) {
			if (b.getShow_id() == show_id) {
				Show show = showRepo.getbyShowId(show_id);
				Integer totalCost = (int) (show.getPrice() * b.getSeats_booked());

				// creating response entity to update wallet
				HttpHeaders headers = new HttpHeaders();
				// set all headers
				headers.setContentType(MediaType.APPLICATION_JSON);
				RequestWalletTransactionTemplate data = new RequestWalletTransactionTemplate("credit", totalCost);
				HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
				restTemplate.exchange(
						"http://wallet-microservice:8082/wallets/" + user_id, HttpMethod.PUT, requestEntity,
						Wallet.class);
				show.setSeats_available(show.getSeats_available() + b.getSeats_booked());
				bookingRepo.delete(b);
				showRepo.save(show);
				return ResponseEntity.ok("Booking deleted successfully");
			}
		}
		return ResponseEntity.badRequest().body("Not Found");
	}

	@Override
	public ResponseEntity<?> deleteAllUserBookings(Integer user_id) {
		List<Booking> userBookings = getAllBookingsByUserId(user_id);

		if (userBookings == null || userBookings.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// seat to be returned to available pool
		for (Booking b : userBookings) {
			Show show = showRepo.getbyShowId(b.getShow_id());
			show.setSeats_available(show.getSeats_available() + b.getSeats_booked());
			showRepo.save(show);

			// The amounts to make these userBookings are returned to the user’s wallet.
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Integer totalCost = (int) (b.getSeats_booked() * show.getPrice());
			RequestWalletTransactionTemplate data = new RequestWalletTransactionTemplate("credit", totalCost);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(data, headers);
			restTemplate.exchange(
					"http://wallet-microservice:8082/wallets/" + user_id, HttpMethod.PUT, requestEntity, Wallet.class);

			bookingRepo.delete(b);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
