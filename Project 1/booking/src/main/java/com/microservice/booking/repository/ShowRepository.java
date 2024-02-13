package com.microservice.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.booking.entities.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

	@Query(value = "SELECT * FROM shows s WHERE s.theatre_id = :theatre_id", nativeQuery = true)
	List<Show> findShowsByTheatreId(@Param("theatre_id") Integer theatre_id);

	@Query(value = "SELECT * FROM shows s WHERE s.show_id = :show_id", nativeQuery = true)
	Show getbyShowId(@Param("show_id") Integer show_id);

}
