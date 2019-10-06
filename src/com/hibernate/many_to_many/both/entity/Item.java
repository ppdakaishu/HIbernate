package com.hibernate.many_to_many.both.entity;

import java.util.HashSet;
import java.util.Set;

public class Item {

	private Integer id;
	private String itemName;
	private Set<Categoty> categotys = new HashSet<Categoty>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Set<Categoty> getCategotys() {
		return categotys;
	}
	public void setCategotys(Set<Categoty> categotys) {
		this.categotys = categotys;
	}
	
}
