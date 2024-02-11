package com.microservice.booking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="theatre")
public class Theatre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long theatre_id;
	
	
	private String name;
	private String location;
	public Long getTheatre_id() {
		return theatre_id;
	}
	public void setTheatre_id(Long theatre_id) {
		this.theatre_id = theatre_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Theatre(Long theatre_id, String name, String location) {
		super();
		this.theatre_id = theatre_id;
		this.name = name;
		this.location = location;
	}
	public Theatre() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
