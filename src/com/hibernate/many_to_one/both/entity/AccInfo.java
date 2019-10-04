package com.hibernate.many_to_one.both.entity;

import java.util.HashSet;
import java.util.Set;

public class AccInfo {

	private Integer accId;
	private String accName;
	//1. 声明集合类型时, 需要使用接口类型, 因为 hibernate 在获取
	//	  集合类型时, 返回的时 hibernate 内置的集合类型, 而不是 JavaSE 
	//	  标准的集合实现. 
	//2. 需要把集合初始化, 可以防止发生空指针异常. 
	//3. HashSet<SealCard>(0) 中的 0 代表初始容量为 0. 
	private Set<SealCard> sealCards = new HashSet<SealCard>(0);
	
	public Integer getAccId() {
		return accId;
	}
	public void setAccId(Integer accId) {
		this.accId = accId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Set<SealCard> getSealCards() {
		return sealCards;
	}
	public void setSealCards(Set<SealCard> sealCards) {
		this.sealCards = sealCards;
	}
	
}
