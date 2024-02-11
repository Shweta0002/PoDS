package com.microservice.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.booking.VO.Users;
import com.microservice.booking.VO.Wallet;
import com.microservice.booking.entities.Booking;
import com.microservice.booking.entities.Show;
import com.microservice.booking.repository.BookingRepository;
import com.microservice.booking.repository.ShowRepository;


@Service
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ShowRepository showRepo;
	
	@Autowired
	private RestTemplate restTemplate; 

	public BookingServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getBookingById(Long bookingId) {
		
		return bookingRepo.existsById(bookingId);
	}
	
	
	
	@Override
	public Booking addBooking(Booking booking) {
		Long show_id=booking.getShow_id();
		Integer User_id=booking.getUser_id();
		System.out.print(User_id);
		ResponseEntity<Users> userExists= restTemplate.getForEntity("http://localhost:8080/users/"+User_id, 
				Users.class);
		ResponseEntity<Wallet> wallet= restTemplate.getForEntity("http://localhost:8082/wallets/"+User_id, 
				Wallet.class);
		Show show = showRepo.getbyShowId(show_id);
//		if(wallet.get
//		}
//		
		if(showRepo.existsById(show_id) ) {
			System.out.println("Yes show exists");
		}
		System.out.print(userExists.toString());
		
		return booking;
	}

	@Override
	public void deleteAllBooking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Booking> getAllBookingsByUserId(Long user_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
