package com.waupi.bean;

public class MenuBean {

	String menu_id,menu_name,menu_desc,menu_image,menu_rating;

	public MenuBean(String amenity_id, String amenity_name,
			 String amenity_desc,String amenity_image,String amenities_rating) {
		super();
		this.menu_id = amenity_id;
		this.menu_name = amenity_name;
		this.menu_desc = amenity_desc;
		this.menu_image = amenity_image;
		this.menu_rating = amenities_rating;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_desc() {
		return menu_desc;
	}

	public void setMenu_desc(String menu_desc) {
		this.menu_desc = menu_desc;
	}

	public String getMenu_image() {
		return menu_image;
	}

	public void setMenu_image(String menu_image) {
		this.menu_image = menu_image;
	}

	public String getMenu_rating() {
		return menu_rating;
	}

	public void setMenu_rating(String menu_rating) {
		this.menu_rating = menu_rating;
	}
}
