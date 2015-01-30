package com.waupi.bean;

public class ReviewsBean {

	String review_id,review_name,reviewer_image,reviewer_text;

	public ReviewsBean(String review_id, String review_name,
			String reviewer_image, String reviewer_text) {
		super();
		this.review_id = review_id;
		this.review_name = review_name;
		this.reviewer_image = reviewer_image;
		this.reviewer_text = reviewer_text;
	}

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}

	public String getReview_name() {
		return review_name;
	}

	public void setReview_name(String review_name) {
		this.review_name = review_name;
	}

	public String getReviewer_image() {
		return reviewer_image;
	}

	public void setReviewer_image(String reviewer_image) {
		this.reviewer_image = reviewer_image;
	}

	public String getReviewer_text() {
		return reviewer_text;
	}

	public void setReviewer_text(String reviewer_text) {
		this.reviewer_text = reviewer_text;
	}
}
