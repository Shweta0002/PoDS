package com.microservice.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.booking.entities.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

	@Query(value = "SELECT * FROM shows s WHERE s.theatre_id =:theatre_id",nativeQuery = true)
	List<Show> findShowsByTheatreId(@Param("theatre_id") Long theatre_id);
//	@Query("SELECT s FROM Show s JOIN s.theatre_id t WHERE t.id = :theatreId")
//    List<Show> findShowsByTheatreId(@Param("theatre_id") Long theatre_id);
//	
	
	@Query(value = "SELECT * FROM shows s WHERE s.show_id =:show_id",nativeQuery = true)
	Show getbyShowId(@Param("show_id") Long show_id);
	
	
//	
//	Show findByShowId(Long show_id);
//
//
//
//	Show getShowById(Long show_id);
	
}
