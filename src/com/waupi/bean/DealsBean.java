package com.waupi.bean;

public class DealsBean {

	public String id,code,descripton,image,restaurant_id,validiy;

	public DealsBean(String id, String code, String descripton, String image,
			String restaurant_id, String validiy) {
		super();
		this.id = id;
		this.code = code;
		this.descripton = descripton;
		this.image = image;
		this.restaurant_id = restaurant_id;
		this.validiy = validiy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescripton() {
		return descripton;
	}

	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public String getValidiy() {
		return validiy;
	}

	public void setValidiy(String validiy) {
		this.validiy = validiy;
	}
}
