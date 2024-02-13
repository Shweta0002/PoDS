package com.microservice.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.booking.entities.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Integer> {

	@Query(value = "SELECT * FROM theatre t WHERE t.id = :theatre_id", nativeQuery = true)
	Theatre getTheatreById(Integer theatre_id);

}
