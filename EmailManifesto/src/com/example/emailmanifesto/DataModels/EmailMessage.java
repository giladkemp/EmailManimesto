package com.example.emailmanifesto.DataModels;

import java.util.List;

import org.joda.time.DateTime;

public class EmailMessage {
	private String subject;
	private String from;
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	//lowest priority is 1 (blue), highest is 5 (red)
	private int priority;
	private DateTime sentTime;
	private InterfaceMessageContent messageContent;
	
	public EmailMessage(String subject, String from, List<String> to,
			List<String> cc, List<String> bcc, int priority, DateTime sentTime,
			InterfaceMessageContent messageContent) {
		super();
		this.subject = subject;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.priority = priority;
		this.sentTime = sentTime;
		this.messageContent = messageContent;
	}
	//Getters and setters
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public List<String> getBcc() {
		return bcc;
	}
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public DateTime getSentTime() {
		return sentTime;
	}
	public void setSentTime(DateTime sentTime) {
		this.sentTime = sentTime;
	}
	public InterfaceMessageContent getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(InterfaceMessageContent messageContent) {
		this.messageContent = messageContent;
	}
	//hashCode and equals methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bcc == null) ? 0 : bcc.hashCode());
		result = prime * result + ((cc == null) ? 0 : cc.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((messageContent == null) ? 0 : messageContent.hashCode());
		result = prime * result + priority;
		result = prime * result
				+ ((sentTime == null) ? 0 : sentTime.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		EmailMessage other = (EmailMessage) obj;
		if (bcc == null) {
			if (other.bcc != null)
				return false;
		} else if (!bcc.equals(other.bcc))
			return false;
		if (cc == null) {
			if (other.cc != null)
				return false;
		} else if (!cc.equals(other.cc))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (messageContent == null) {
			if (other.messageContent != null)
				return false;
		} else if (!messageContent.equals(other.messageContent))
			return false;
		if (priority != other.priority)
			return false;
		if (sentTime == null) {
			if (other.sentTime != null)
				return false;
		} else if (!sentTime.equals(other.sentTime))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
	//TODO: add toJson and fromJson functionality
	
}
