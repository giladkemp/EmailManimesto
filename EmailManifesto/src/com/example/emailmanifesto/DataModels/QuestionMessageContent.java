package com.example.emailmanifesto.DataModels;

import java.util.List;

public class QuestionMessageContent implements InterfaceMessageContent{
	//Fields
	private boolean actionNeeded;
	private List<Question> questions;
	
	//Constructors
	public QuestionMessageContent(boolean actionNeeded, List<Question> questions) {
		super();
		this.actionNeeded = actionNeeded;
		this.questions = questions;
	}
	
	//Getters and setters
	public boolean isActionNeeded() {
		return actionNeeded;
	}
	public void setActionNeeded(boolean actionNeeded) {
		this.actionNeeded = actionNeeded;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (actionNeeded ? 1231 : 1237);
		result = prime * result
				+ ((questions == null) ? 0 : questions.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionMessageContent other = (QuestionMessageContent) obj;
		if (actionNeeded != other.actionNeeded)
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		return true;
	}
	
	
}
