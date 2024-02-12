package com.microservice.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.booking.entities.Theatre;


@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

	@Query(value = "SELECT * FROM theatre t WHERE t.theatre_id =:theatre_id",nativeQuery = true)
	Theatre getTheatrebyId(Long theatre_id);

}
