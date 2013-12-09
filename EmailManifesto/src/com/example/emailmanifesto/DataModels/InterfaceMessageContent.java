package com.example.emailmanifesto.DataModels;

import org.json.JSONObject;

public interface InterfaceMessageContent {
	public boolean equals(Object obj);
	public int hashCode();
	
	// toJSON and fromJSON
	public JSONObject toJson();
	
	public InterfaceMessageContent fromJson(JSONObject json);
}
