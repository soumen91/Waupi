package com.waupi.bean;

public class AtmBean {

	String atm_id,atm_name,atm_address,atm_lat,atm_lng;

	public AtmBean(String atm_id, String atm_name, String atm_address,
			String atm_lat, String atm_lng) {
		super();
		this.atm_id = atm_id;
		this.atm_name = atm_name;
		this.atm_address = atm_address;
		this.atm_lat = atm_lat;
		this.atm_lng = atm_lng;
	}

	public String getAtm_id() {
		return atm_id;
	}

	public void setAtm_id(String atm_id) {
		this.atm_id = atm_id;
	}

	public String getAtm_name() {
		return atm_name;
	}

	public void setAtm_name(String atm_name) {
		this.atm_name = atm_name;
	}

	public String getAtm_address() {
		return atm_address;
	}

	public void setAtm_address(String atm_address) {
		this.atm_address = atm_address;
	}

	public String getAtm_lat() {
		return atm_lat;
	}

	public void setAtm_lat(String atm_lat) {
		this.atm_lat = atm_lat;
	}

	public String getAtm_lng() {
		return atm_lng;
	}

	public void setAtm_lng(String atm_lng) {
		this.atm_lng = atm_lng;
	}
	
	
}
