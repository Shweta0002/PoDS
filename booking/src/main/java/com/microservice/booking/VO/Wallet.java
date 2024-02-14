package com.microservice.booking.VO;

// Class for response recieved from wallet microservice
public class Wallet {

	private Integer user_id;
	private Integer balance;

	public Wallet() {
	}

	public Wallet(Integer user_id, Integer balance) {
		this.user_id = user_id;
		this.balance = balance;
	}

	public Integer getUser_id() {
		return this.user_id;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

}
