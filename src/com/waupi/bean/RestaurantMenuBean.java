package com.waupi.bean;

public class RestaurantMenuBean {

	public String id,name,rating,desc,imagepath;

	public RestaurantMenuBean(String id, String name, String rating,
			String desc, String imagepath) {
		super();
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.desc = desc;
		this.imagepath = imagepath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
}
