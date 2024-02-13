package com.microservice.booking.VO;

public class RequestWalletTransactionTemplate {
	private String action;
	private Integer amount;

	public RequestWalletTransactionTemplate(String action, Integer amount) {
		super();
		this.action = action;
		this.amount = amount;
	}

	public String getAction() {
		return this.action;
	}

	public Integer getAmount() {
		return this.amount;
	}
}
