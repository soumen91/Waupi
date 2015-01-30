package com.waupi.bean;

public class CouponBean {

	public String id,code,descripton,image,validiy,status;

	public CouponBean(String id, String code, String descripton, String image,
			String validiy, String status) {
		super();
		this.id = id;
		this.code = code;
		this.descripton = descripton;
		this.image = image;
		this.validiy = validiy;
		this.status = status;
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

	public String getValidiy() {
		return validiy;
	}

	public void setValidiy(String validiy) {
		this.validiy = validiy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
