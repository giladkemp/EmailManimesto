package com.example.emailmanifesto.DataModels;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject main = new JSONObject();
		try {
			main.put("actionNeeded", this.isActionNeeded());
			
			// each ques has name and list of responses 
			/*
			 
			  
			  {
				"questions": [
				{ "questionName":"John" , "responses": ["r1", "r2"]}, 
				{ "questionName":"Anna" , "responses": ["r1", "r2"] }, 
				{ "questionName":"Jim" , "responses": ["r1", "r2"] }
				]
				}
			 * 
			 * 
			 * 
			 */
			
			
			JSONArray qArr = new JSONArray();
			for (Question q : this.getQuestions()) {
				JSONObject questionJSON = new JSONObject();
				questionJSON.put("questionName", q.getQuestion());
				// put responses in array
				questionJSON.put("responses", new JSONArray(q.getResponses()));
				
				// put in main json
				qArr.put(questionJSON);
				
			}
			main.put("questions", qArr);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return main;
	}

	@Override
	public InterfaceMessageContent fromJson(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			this.actionNeeded = json.getBoolean("actionNeeded");
			
			//for e/ ques, get question
			JSONArray qArr = json.getJSONArray("questions");
			this.questions.clear();
			// inside qArr is json obj
			for (int i=0; i < qArr.length(); i++) {
				JSONObject q = qArr.getJSONObject(i);
				String questionName = q.getString("questionName");
				
				// for e response, put in list
				JSONArray respArr = q.getJSONArray("responses");
				ArrayList<String> newResponses = new ArrayList<String>();
				for (int j = 0; j < respArr.length(); j++) {
					String resp = (String) respArr.get(j);
					newResponses.add(resp);
				}
				Question newQues = new Question(questionName, newResponses);
				this.questions.add(newQues);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return this;
	}
	
	
}
