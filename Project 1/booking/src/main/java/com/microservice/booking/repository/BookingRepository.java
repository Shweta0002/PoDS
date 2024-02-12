package com.microservice.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{

	@Query(value = "SELECT * FROM booking b WHERE b.user_id =:user_id",nativeQuery = true)
	List<Booking> findAllbyUserId(@Param("user_id")Integer user_id);
	

}
