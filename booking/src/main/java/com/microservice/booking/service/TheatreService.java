package com.microservice.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.booking.entities.Theatre;

@Service
public interface TheatreService {

	List<Theatre> getAllTheatres();

	Theatre addTheatre(Theatre theatre);

}
