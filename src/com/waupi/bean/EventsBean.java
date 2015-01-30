package com.waupi.bean;

public class EventsBean {

	String event_id;
	String event_name;
	String event_date;
	String event_rating;
	String event_image;
	
	public EventsBean(String event_id, String event_name, String event_date,
			String event_rating,String event_image) {
		super();
		this.event_id = event_id;
		this.event_name = event_name;
		this.event_date = event_date;
		this.event_rating = event_rating;
		this.event_image = event_image;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_date() {
		return event_date;
	}

	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}

	public String getEvent_rating() {
		return event_rating;
	}

	public void setEvent_rating(String event_rating) {
		this.event_rating = event_rating;
	}

	public String getEvent_image() {
		return event_image;
	}

	public void setEvent_image(String event_image) {
		this.event_image = event_image;
	}
	
}
