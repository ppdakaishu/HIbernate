package com.hibernate.session.entity;

import java.util.Date;

public class Time {

	private Integer id;
	private Date date;
	private Date time;
	private Date timestamp;
	
	public Time() {
		
	}
	
	public Time(Date date, Date time, Date timestamp) {
		super();
		this.date = date;
		this.time = time;
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "Time [id=" + id + ", date=" + date + ", time=" + time + ", timestamp=" + timestamp + "]";
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
