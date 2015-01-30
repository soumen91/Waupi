package com.waupi.bean;

public class AmenitiesBean {

	String amenity_id,amenity_name,amenity_date,amenity_desc,amenity_image,amenities_rating;

	public AmenitiesBean(String amenity_id, String amenity_name,
			String amenity_date, String amenity_desc,String amenity_image,String amenities_rating) {
		super();
		this.amenity_id = amenity_id;
		this.amenity_name = amenity_name;
		this.amenity_date = amenity_date;
		this.amenity_desc = amenity_desc;
		this.amenity_image = amenity_image;
		this.amenities_rating = amenities_rating;
	}

	public String getAmenity_id() {
		return amenity_id;
	}

	public void setAmenity_id(String amenity_id) {
		this.amenity_id = amenity_id;
	}

	public String getAmenity_name() {
		return amenity_name;
	}

	public void setAmenity_name(String amenity_name) {
		this.amenity_name = amenity_name;
	}

	public String getAmenity_date() {
		return amenity_date;
	}

	public void setAmenity_date(String amenity_date) {
		this.amenity_date = amenity_date;
	}

	public String getAmenity_desc() {
		return amenity_desc;
	}

	public void setAmenity_desc(String amenity_desc) {
		this.amenity_desc = amenity_desc;
	}

	public String getAmenity_image() {
		return amenity_image;
	}

	public void setAmenity_image(String amenity_image) {
		this.amenity_image = amenity_image;
	}

	public String getAmenities_rating() {
		return amenities_rating;
	}

	public void setAmenities_rating(String amenities_rating) {
		this.amenities_rating = amenities_rating;
	}
}
