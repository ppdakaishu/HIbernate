package com.hibernate.many_to_many.one.entity;

import java.util.HashSet;
import java.util.Set;

public class Categoty {

	private Integer id;
	private String categotyName;
	private Set<Item> items = new HashSet<Item>();
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategotyName() {
		return categotyName;
	}
	public void setCategotyName(String categotyName) {
		this.categotyName = categotyName;
	}
	public Set<Item> getItems() {
		return items;
	}
	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
}
