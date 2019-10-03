package com.hibernate.helloworld.entity;

import java.sql.Blob;
import java.sql.Clob;

public class LargeText {

	private Integer id; 
	//大文本
	private Clob content;
	//图片, 二进制数据
	private Blob image;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Clob getContent() {
		return content;
	}
	public void setContent(Clob content) {
		this.content = content;
	}
	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	
}
