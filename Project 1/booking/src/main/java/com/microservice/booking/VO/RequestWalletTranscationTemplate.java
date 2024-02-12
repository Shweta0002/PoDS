package com.microservice.booking.VO;

public class RequestWalletTranscationTemplate {
	 	String action;
	    public RequestWalletTranscationTemplate(String action, Integer amount) {
			super();
			this.action = action;
			this.amount = amount;
		}

		Integer amount;

	    public String getAction() {
	        return this.action;
	    }

	    public Integer getAmount() {
	        return this.amount;
	    }
}
