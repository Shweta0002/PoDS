package com.microservice.booking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Shows")
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer theatre_id;
	private String title;
	private Integer price;
	private Integer seats_available;

	public Show() {
		super();
	}

	public Show(Integer id, Integer theatre_id, String title, Integer price, Integer seats_available) {
		super();
		this.id = id;
		this.theatre_id = theatre_id;
		this.title = title;
		this.price = price;
		this.seats_available = seats_available;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTheatre_id() {
		return this.theatre_id;
	}

	public void setTheatre_id(Integer theatre_id) {
		this.theatre_id = theatre_id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getSeats_available() {
		return this.seats_available;
	}

	public void setSeats_available(Integer seats_available) {
		this.seats_available = seats_available;
	}

}
