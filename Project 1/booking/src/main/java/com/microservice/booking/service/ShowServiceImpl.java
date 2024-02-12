package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.ShowRepository;
import com.microservice.booking.repository.TheatreRepository;

@Service
public class ShowServiceImpl implements ShowService {

	@Autowired
	private ShowRepository showRepo;
	
	@Autowired
	private TheatreRepository theatreRepo;
	
	public ShowServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Show> getAllShows() {
		// TODO Auto-generated method stub
		return showRepo.findAll();
	}

	@Override
	public ResponseEntity<?> addShow(Show show) {
		// TODO Auto-generated method stub
		if(theatreRepo.getTheatrebyId(show.getTheatre_id())!=null)
		{
			showRepo.save(show);
			return new ResponseEntity<Show>(show, HttpStatus.OK); 
		}
		else {
			return ResponseEntity.badRequest().body("Theatre_id not found");
		}
		 
	}

	@Override
	public List<Show> getAllShowByTheatreId(Long theatre_id) {
		// TODO Auto-generated method stub
		return showRepo.findShowsByTheatreId(theatre_id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Show getShowById(Long show_id) {
		// TODO Auto-generated method stub
		
		return showRepo.getbyShowId(show_id);
	}

}
