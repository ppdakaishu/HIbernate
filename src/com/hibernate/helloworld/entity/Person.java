package com.hibernate.helloworld.entity;

import java.util.Date;

public class Person {

	private Integer id;
	private String name;
	private String company;
	private Integer age;
	private Date registerTime;
	
	public Person() {
		super();
	}

	public Person(String name, String company, Integer age, Date registerTime) {
		super();
		this.name = name;
		this.company = company;
		this.age = age;
		this.registerTime = registerTime;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", company=" + company + ", age=" + age + ", registerTime="
				+ registerTime + "]";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
}
