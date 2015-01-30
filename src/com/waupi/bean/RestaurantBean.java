package com.waupi.bean;

import java.util.ArrayList;

public class RestaurantBean {

	String restaurant_id,restaurant_name,restaurant_address,restaurant_rating,restaurant_iamge,foursquare_id;
	public ArrayList<RestaurantMenuBean> menuBean = new ArrayList<RestaurantMenuBean>();
	public ArrayList<DealsBean> dealsbean = new ArrayList<DealsBean>();
	public RestaurantBean(String restaurant_id, String restaurant_name,
			String restaurant_address, String restaurant_rating,
			String restaurant_iamge, String foursquare_id,
			ArrayList<RestaurantMenuBean> menuBean,
			ArrayList<DealsBean> dealsbean) {
		super();
		this.restaurant_id = restaurant_id;
		this.restaurant_name = restaurant_name;
		this.restaurant_address = restaurant_address;
		this.restaurant_rating = restaurant_rating;
		this.restaurant_iamge = restaurant_iamge;
		this.foursquare_id = foursquare_id;
		this.menuBean = menuBean;
		this.dealsbean = dealsbean;
	}
	public String getRestaurant_id() {
		return restaurant_id;
	}
	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	public String getRestaurant_name() {
		return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}
	public String getRestaurant_address() {
		return restaurant_address;
	}
	public void setRestaurant_address(String restaurant_address) {
		this.restaurant_address = restaurant_address;
	}
	public String getRestaurant_rating() {
		return restaurant_rating;
	}
	public void setRestaurant_rating(String restaurant_rating) {
		this.restaurant_rating = restaurant_rating;
	}
	public String getRestaurant_iamge() {
		return restaurant_iamge;
	}
	public void setRestaurant_iamge(String restaurant_iamge) {
		this.restaurant_iamge = restaurant_iamge;
	}
	public String getFoursquare_id() {
		return foursquare_id;
	}
	public void setFoursquare_id(String foursquare_id) {
		this.foursquare_id = foursquare_id;
	}
	public ArrayList<RestaurantMenuBean> getMenuBean() {
		return menuBean;
	}
	public void setMenuBean(ArrayList<RestaurantMenuBean> menuBean) {
		this.menuBean = menuBean;
	}
	public ArrayList<DealsBean> getDealsbean() {
		return dealsbean;
	}
	public void setDealsbean(ArrayList<DealsBean> dealsbean) {
		this.dealsbean = dealsbean;
	}
}
