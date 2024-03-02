package com.microservice.booking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Show;

@Service
public interface ShowService {

	// Returns list of all shows
	List<Show> getAllShows();

	// Adds new show
	ResponseEntity<?> addShow(Show show);

	// Returns shows in a given theatre ID
	List<Show> getAllShowByTheatreId(Integer theatre_id);

	// Returns show with given show ID
	Show getShowById(Integer show_id);

}
