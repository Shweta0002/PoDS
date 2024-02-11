package com.microservice.booking.VO;

import jakarta.persistence.Column;

public class ResponseTemplateObject {
	
	Integer userId;

    String name;

    @Column(unique = true)
    String email;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

}
