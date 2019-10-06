package com.hibernate.one_to_one.primary.entity;

public class Citizen {

	private Integer id;
	private String citizenName;
	private IdentityCard identityCard;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCitizenName() {
		return citizenName;
	}
	public void setCitizenName(String citizenName) {
		this.citizenName = citizenName;
	}
	public IdentityCard getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(IdentityCard identityCard) {
		this.identityCard = identityCard;
	}
	
}
