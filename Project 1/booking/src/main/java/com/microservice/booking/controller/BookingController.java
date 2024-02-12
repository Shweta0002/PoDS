package com.microservice.booking.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;
import com.microservice.booking.entities.Theatre;
import com.microservice.booking.exceptions.DataNotFoundException;
import com.microservice.booking.service.BookingService;
import com.microservice.booking.service.ShowService;
import com.microservice.booking.service.TheatreService;
import jakarta.annotation.PostConstruct;

@Controller
public class BookingController {

	@Autowired
	private ShowService showService;

	@Autowired
	private TheatreService theatreService;

	@Autowired
	private BookingService bookingService;

	@PostConstruct
	public void init() {
		try {
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(
					"Project 1\\booking\\src\\main\\java\\com\\microservice\\booking\\data\\theatres.csv"));
			System.out.println();
			int i = 0;
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				if (i != 0) {
					String[] str = line.split(","); // use comma as separator
					Theatre t = new Theatre();
					t.setName(str[1]);
					t.setLocation(str[2]);
					theatreService.addTheatre(t);
				}
				i++;
			}
			br.close();

			br = new BufferedReader(
					new FileReader("Project 1\\booking\\src\\main\\java\\com\\microservice\\booking\\data\\shows.csv"));
			System.out.println();
			i = 0;
			while ((line = br.readLine()) != null) {
				if (i != 0) {
					String[] str = line.split(",");
					Show s = new Show();
					s.setTheatre_id(Long.parseLong(str[1]));
					s.setTitle(str[2]);
					s.setPrice(Long.parseLong(str[3]));
					s.setSeats_available(Long.parseLong(str[4]));
					showService.addShow(s);
				}
				i++;
			}
			br.close();

		} catch (Exception e) {

		}
	}

	@DeleteMapping("/bookings/users/{user_id}/shows/{show_id}")
	private ResponseEntity<?> deleteBookingofUserByShowId(@PathVariable Integer user_id, @PathVariable Long show_id) {
		try {

			return bookingService.deleteBookingofUserByShowId(user_id, show_id);
		} catch (DataNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/bookings/users/{user_id}")
	private ResponseEntity<?> deleteUserBookings(@PathVariable Integer user_id) {
		try {
			return bookingService.deleteAllUserBookings(user_id);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/bookings/users/{user_id}")
	private ResponseEntity<List<Booking>> getAllBookingsByUserId(@PathVariable Integer user_id) {
		try {
			List<Booking> bookings = bookingService.getAllBookingsByUserId(user_id);
			if (bookings.isEmpty())
				return new ResponseEntity<List<Booking>>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<List<Booking>>(bookings, HttpStatus.OK);

		} catch (DataNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/bookings/users/")
	private ResponseEntity<List<Booking>> getAllBookings() {
		try {
			List<Booking> bookings = bookingService.getAllBookings();
			if (bookings.isEmpty())
				return new ResponseEntity<List<Booking>>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<List<Booking>>(bookings, HttpStatus.OK);

		} catch (DataNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/bookings")
	private ResponseEntity<?> addBooking(@RequestBody Booking booking) {
		try {
			System.out.println("hello");
			return bookingService.addBooking(booking);

		} catch (Exception e) {
			System.out.println("Error here");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/bookings")
	private ResponseEntity<?> deleteAllBooking() {
		try {
			return bookingService.deleteAllBooking();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/shows/{show_id}")
	private ResponseEntity<?> getShow(@PathVariable Long show_id) {
		try {
			Show show = showService.getShowById(show_id);
			return new ResponseEntity<Show>(show, HttpStatus.OK);

		} catch (Exception e) {
			if (e.getMessage().contains("No show found with Id :"))
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("shows/theatres/{theatre_id}")
	private ResponseEntity<List<Show>> getAllShowByTheatreId(@PathVariable Long theatre_id) {
		try {
			List<Show> shows = showService.getAllShowByTheatreId(theatre_id);
			if (shows.isEmpty())
				return new ResponseEntity<List<Show>>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<List<Show>>(shows, HttpStatus.OK);

		} catch (DataNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/shows")
	private ResponseEntity<List<Show>> getAllShows() {
		try {
			List<Show> shows = showService.getAllShows();
			if (shows.isEmpty())
				return new ResponseEntity<List<Show>>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<List<Show>>(shows, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add/show")
	private ResponseEntity<?> addShow(@RequestBody Show show) {
		try {
			return showService.addShow(show);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/theatres")
	private ResponseEntity<List<Theatre>> getAllTheatres() {
		try {
			List<Theatre> theatres = theatreService.getAllTheatres();
			if (theatres.isEmpty())
				return new ResponseEntity<List<Theatre>>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<List<Theatre>>(theatres, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add/theatre")
	private ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatre) {
		try {
			Theatre theatre1 = theatreService.addTheatre(theatre);
			return new ResponseEntity<Theatre>(theatre1, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
