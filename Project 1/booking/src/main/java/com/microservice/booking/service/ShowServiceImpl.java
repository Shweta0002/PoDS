package com.microservice.booking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.ShowRepository;

@Service
public class ShowServiceImpl implements ShowService {

	@Autowired
	private ShowRepository showRepo;
	
	public ShowServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Show> getAllShows() {
		// TODO Auto-generated method stub
		return showRepo.findAll();
	}

	@Override
	public Show addShow(Show show) {
		// TODO Auto-generated method stub
		return showRepo.save(show);
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
