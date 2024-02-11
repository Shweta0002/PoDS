package com.microservice.booking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Shows")
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long show_id;
//	
//	@ManyToOne
//    @JoinColumn(name = "theatre_id", nullable = false)
//    private Theatre theatre; 
//
	
	private Long theatre_id;
	
	
	private String title;
	private Long price;
	private long seats_available;
	public Long getShow_id() {
		return show_id;
	}
	public void setShow_id(Long show_id) {
		this.show_id = show_id;
	}	
	
	public Long getTheatre_id() {
		return theatre_id;
	}
	public void setTheatre_id(Long theatre_id) {
		this.theatre_id = theatre_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public long getSeats_available() {
		return seats_available;
	}
	public void setSeats_available(long seats_available) {
		this.seats_available = seats_available;
	}
	public Show(Long show_id, Long theatre_id, String title, Long price, long seats_available) {
		super();
		this.show_id = show_id;
		this.theatre_id = theatre_id;
		this.title = title;
		this.price = price;
		this.seats_available = seats_available;
	}
	public Show() {
		super();
		// TODO Auto-generated constructor stub
	}
}
