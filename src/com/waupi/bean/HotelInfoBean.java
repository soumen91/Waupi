package com.waupi.bean;

public class HotelInfoBean {

	public String image,name,location,rating,lat,lng;
	
	
	public HotelInfoBean(String image, String name, String location,
			String rating, String lat, String lng) {
		super();
		this.image = image;
		this.name = name;
		this.location = location;
		this.rating = rating;
		this.lat = lat;
		this.lng = lng;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
}
