package com.hibernate.many_to_one.one.entity;

public class SealCard {

	private Integer sealCardId;
	private String cardNo;
	private AccInfo accInfo;
	
	
	public Integer getSealCardId() {
		return sealCardId;
	}
	public void setSealCardId(Integer sealCardId) {
		this.sealCardId = sealCardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public AccInfo getAccInfo() {
		return accInfo;
	}
	public void setAccInfo(AccInfo accInfo) {
		this.accInfo = accInfo;
	}
	
}
