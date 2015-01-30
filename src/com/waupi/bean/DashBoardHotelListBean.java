package com.waupi.bean;

public class DashBoardHotelListBean {

	public String hotel_id;
	public String hotel_name;
	public String hotel_address,reviews_count;
	public String hotel_image,hotel_rating,valet_no,donot_disturb_no;
	public String hotel_city,hotel_country,frontdesk_no,laundry_pickup_no;
	
	
	public DashBoardHotelListBean(String hotel_id, String hotel_name,
			String hotel_address, String hotel_image, String hotel_rating,
			String hotel_city, String hotel_country,String reviews_count,
			String frontdesk_no,String laundry_pickup_no,String valet_no,
			String donot_disturb_no) {
		super();
		this.hotel_id = hotel_id;
		this.hotel_name = hotel_name;
		this.hotel_address = hotel_address;
		this.hotel_image = hotel_image;
		this.hotel_rating = hotel_rating;
		this.hotel_city = hotel_city;
		this.hotel_country = hotel_country;
		this.reviews_count = reviews_count;
		this.frontdesk_no = frontdesk_no;
		this.laundry_pickup_no = laundry_pickup_no;
		this.valet_no = valet_no;
		this.donot_disturb_no = donot_disturb_no;
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

	public String getReviews_count() {
		return reviews_count;
	}

	public void setReviews_count(String reviews_count) {
		this.reviews_count = reviews_count;
	}

	public String getValet_no() {
		return valet_no;
	}

	public void setValet_no(String valet_no) {
		this.valet_no = valet_no;
	}

	public String getDonot_disturb_no() {
		return donot_disturb_no;
	}

	public void setDonot_disturb_no(String donot_disturb_no) {
		this.donot_disturb_no = donot_disturb_no;
	}

	public String getFrontdesk_no() {
		return frontdesk_no;
	}

	public void setFrontdesk_no(String frontdesk_no) {
		this.frontdesk_no = frontdesk_no;
	}

	public String getLaundry_pickup_no() {
		return laundry_pickup_no;
	}

	public void setLaundry_pickup_no(String laundry_pickup_no) {
		this.laundry_pickup_no = laundry_pickup_no;
	}
	
}
