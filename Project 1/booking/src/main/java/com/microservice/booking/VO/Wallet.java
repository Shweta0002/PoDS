package com.microservice.booking.VO;

public class Wallet {

	private Integer user_id;
	private Integer balance;

	public Wallet() {
	}

	public Wallet(Integer user_id, Integer balance) {
		this.user_id = user_id;
		this.balance = balance;
	}

	public Integer getUserId() {
		return this.user_id;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public void setUserId(Integer user_id) {
		this.user_id = user_id;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

}
