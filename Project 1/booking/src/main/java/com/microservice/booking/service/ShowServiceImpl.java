package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.ShowRepository;
import com.microservice.booking.repository.TheatreRepository;

@Service
public class ShowServiceImpl implements ShowService {

	@Autowired
	private ShowRepository showRepo;

	@Autowired
	private TheatreRepository theatreRepo;

	@Override
	public List<Show> getAllShows() {
		return showRepo.findAll();
	}

	@Override
	public ResponseEntity<?> addShow(Show show) {
		if (theatreRepo.getTheatreById(show.getTheatreId()) != null) {
			showRepo.save(show);
			return new ResponseEntity<Show>(show, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("Theatre_id not found");
		}
	}

	@Override
	public List<Show> getAllShowByTheatreId(Integer theatre_id) {
		return showRepo.findShowsByTheatreId(theatre_id);
	}

	@Override
	public Show getShowById(Integer show_id) {
		return showRepo.getbyShowId(show_id);
	}

}
