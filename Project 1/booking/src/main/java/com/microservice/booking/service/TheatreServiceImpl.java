package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Theatre;
import com.microservice.booking.repository.TheatreRepository;

@Service
public class TheatreServiceImpl implements TheatreService {
	
	@Autowired
	private TheatreRepository theatreRepo;

	public TheatreServiceImpl() {
		// TODO Auto-generated constructor stub
	} 

	
	@Override
	public List<Theatre> getAllTheatres() {
		// TODO Auto-generated method stub
		return theatreRepo.findAll();
	}

	@Override
	public Theatre addTheatre(Theatre theatre) {
		// TODO Auto-generated method stub
		return theatreRepo.save(theatre);
	}

}
