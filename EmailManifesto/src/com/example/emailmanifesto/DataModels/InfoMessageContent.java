package com.example.emailmanifesto.DataModels;

import java.util.List;

public class InfoMessageContent implements InterfaceMessageContent {
	private boolean responseRequested;
	private List<Object> attachedContent;
	private String type;
	
	//constructors
	public InfoMessageContent(boolean responseRequested,
			List<Object> attachedContent, String type) {
		super();
		this.responseRequested = responseRequested;
		this.attachedContent = attachedContent;
		this.type = type;
	}
	
	
	//getters and setters
	public boolean isResponseRequested() {
		return responseRequested;
	}
	public void setResponseRequested(boolean responseRequested) {
		this.responseRequested = responseRequested;
	}
	public List<Object> getAttachedContent() {
		return attachedContent;
	}
	public void setAttachedContent(List<Object> attachedContent) {
		this.attachedContent = attachedContent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attachedContent == null) ? 0 : attachedContent.hashCode());
		result = prime * result + (responseRequested ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		InfoMessageContent other = (InfoMessageContent) obj;
		if (attachedContent == null) {
			if (other.attachedContent != null)
				return false;
		} else if (!attachedContent.equals(other.attachedContent))
			return false;
		if (responseRequested != other.responseRequested)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
