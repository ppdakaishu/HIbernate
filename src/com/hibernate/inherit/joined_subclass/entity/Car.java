package com.hibernate.inherit.joined_subclass.entity;

public class Car extends Person {

	private Integer carNumber;

	public Integer getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(Integer carNumber) {
		this.carNumber = carNumber;
	}
	
}
