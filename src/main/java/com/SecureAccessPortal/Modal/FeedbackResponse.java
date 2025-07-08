package com.SecureAccessPortal.Modal;

import java.util.List;

public class FeedbackResponse {
	private List<Question> questions;
	private Ratings ratings;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Ratings getRatings() {
		return ratings;
	}

	public void setRatings(Ratings ratings) {
		this.ratings = ratings;
	}

}
