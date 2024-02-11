package com.microservice.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.booking.entities.Theatre;


@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

}
