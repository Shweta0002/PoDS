package com.microservice.booking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Show;

@Service
public interface ShowService {

	List<Show> getAllShows();

	ResponseEntity<?> addShow(Show show);

	List<Show> getAllShowByTheatreId(Integer theatre_id);

	Show getShowById(Integer show_id);

}
