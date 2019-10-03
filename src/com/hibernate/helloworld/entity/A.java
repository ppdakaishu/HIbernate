package com.hibernate.helloworld.entity;

public class A {

	private Integer id;
	private String aOneField;
	private B b;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getaOneField() {
		return aOneField;
	}
	public void setaOneField(String aOneField) {
		this.aOneField = aOneField;
	}
	public B getB() {
		return b;
	}
	public void setB(B b) {
		this.b = b;
	}
	
}
