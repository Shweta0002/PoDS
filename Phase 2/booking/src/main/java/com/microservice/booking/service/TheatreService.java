package com.microservice.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Theatre;

@Service
public interface TheatreService {

	// Returns list of all available theatres
	List<Theatre> getAllTheatres();

	// Saves a new theatre
	Theatre addTheatre(Theatre theatre);

}
