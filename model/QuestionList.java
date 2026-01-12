/* Copyright (C) HEDNO - GR, Inc - All Rights Reserved
 * Unauthorized copying of this code without legitimate license is prohibited
 * Application: Registration Portal Services
 * Version 1.0
 * Online registration portal for accessing HEDNO web applications
 * Written by Kleopatra Konstanteli <k.konstanteli@deddie.gr>
 * May 2019
 */
package gr.deddie.pfr.model;

import java.util.List; 

public class QuestionList {

	private List<Question> questions;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

}
