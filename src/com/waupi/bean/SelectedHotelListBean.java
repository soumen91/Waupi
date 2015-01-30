package com.waupi.bean;

public class SelectedHotelListBean {

	public String hotel_id;
	public String hotel_name;
	public String hotel_address;
	public String hotel_image,hotel_rating;
	public String hotel_city,hotel_country;
	
	
	public SelectedHotelListBean(String hotel_id, String hotel_name,
			String hotel_address, String hotel_image, String hotel_rating,
			String hotel_city, String hotel_country) {
		super();
		this.hotel_id = hotel_id;
		this.hotel_name = hotel_name;
		this.hotel_address = hotel_address;
		this.hotel_image = hotel_image;
		this.hotel_rating = hotel_rating;
		this.hotel_city = hotel_city;
		this.hotel_country = hotel_country;
	}

	public String getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}

	public String getHotel_address() {
		return hotel_address;
	}

	public void setHotel_address(String hotel_address) {
		this.hotel_address = hotel_address;
	}

	public String getHotel_image() {
		return hotel_image;
	}

	public void setHotel_image(String hotel_image) {
		this.hotel_image = hotel_image;
	}

	public String getHotel_rating() {
		return hotel_rating;
	}

	public void setHotel_rating(String hotel_rating) {
		this.hotel_rating = hotel_rating;
	}

	public String getHotel_city() {
		return hotel_city;
	}

	public void setHotel_city(String hotel_city) {
		this.hotel_city = hotel_city;
	}

	public String getHotel_country() {
		return hotel_country;
	}

	public void setHotel_country(String hotel_country) {
		this.hotel_country = hotel_country;
	}
	
}
