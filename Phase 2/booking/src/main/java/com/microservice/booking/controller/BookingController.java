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

	// Initialization of "theatre" and "show" tables
	@PostConstruct
	public void init() {
		try {
			String theatres[] = { "1,Helen Hayes Theater,240 W 44th St.",
					"2,Cherry Lane Theatre,38 Commerce Street",
					"3,New World Stages,340 West 50th Street",
					"4,The Zipper Theater,100 E 17th St",
					"5,Queens Theatre,Meadows Corona Park",
					"6,The Public Theater,425 Lafayette St",
					"7,Manhattan Ensemble Theatre,55 Mercer St.",
					"8,Metropolitan Playhouse,220 E 4th St.",
					"9,Acorn Theater,410 West 42nd Street",
					"10,Apollo Theater,253 West 125th Street" };
			for (String line : theatres) {
				String[] str = line.split(","); // use comma as separator
				Theatre t = new Theatre();
				t.setName(str[1]);
				t.setLocation(str[2]);
				theatreService.addTheatre(t);

			}
		} catch (Exception e) {
			// Log the exception for debugging
			System.out.println("Error occurred during theatre initialization:" + e);
		}
		try {
			String shows[] = { "1,1,Youth in Revolt,50,40",
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
				Show s = new Show();
				s.setTheatre_id(Integer.parseInt(str[1]));
				s.setTitle(str[2]);
				s.setPrice(Integer.parseInt(str[3]));
				s.setSeats_available(Integer.parseInt(str[4]));
				showService.addShow(s);

			}

		} catch (Exception e) {
			// Log the exception for debugging
			System.out.println("Error occurred during show initialization:" + e);
		}
	}

	@DeleteMapping("/bookings/users/{user_id}/shows/{show_id}")
	private ResponseEntity<?> deleteBookingofUserByShowId(@PathVariable Integer user_id,
			@PathVariable Integer show_id) {
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
			return bookingService.addBooking(booking);
		} catch (Exception e) {
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
	private ResponseEntity<?> getShow(@PathVariable Integer show_id) {
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
	private ResponseEntity<List<Show>> getAllShowByTheatreId(@PathVariable Integer theatre_id) {
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
