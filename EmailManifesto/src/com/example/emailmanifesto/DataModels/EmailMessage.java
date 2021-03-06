package com.example.emailmanifesto.DataModels;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailMessage {
	public static final String ATTACHMENT_FILENAME = "manifesto_manifest.json"; 
	
	private String subject;
	private String from;
	private List<String> to = new ArrayList<String>();
	private List<String> cc = new ArrayList<String>();
	private List<String> bcc = new ArrayList<String>();
	//lowest priority is 1 (blue), highest is 5 (red)
	private int priority;
	private DateTime sentTime;
	private InterfaceMessageContent messageContent;
	
	private static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
	
	public EmailMessage(){
		
	}
	
	public EmailMessage(String subject, String from, List<String> to,
			List<String> cc, List<String> bcc, int priority, DateTime sentTime,
			InterfaceMessageContent messageContent) {
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
	
	
	// create doodle for meeting
//		public String createDoodle() {
//			MeetingMessageContent m = null;
//			if (this.messageContent.getClass() == MeetingMessageContent.class) {
//				 m = (MeetingMessageContent)this.messageContent;
//			} else {
//				return "false";
//			}
//			MeetingDoodle.startDoodleFactory();
//			
//			ArrayList<String> dateTimes = new ArrayList<String>();
//			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
//			for (Interval interval : m.getAvailableIntervals()) {
//				dateTimes.add(fmt.print(interval.getStart()));
//			}
//			
//			// add participants
//			ArrayList<String> participants = new ArrayList<String>(this.getTo());
//			participants.addAll(getCc());
//			
//			
//			String id = MeetingDoodle.newPoll(m.getTitle(), m.getDescription(), 
//					(String[])dateTimes.toArray(), (String[])participants.toArray());
//			return "doodle.com/" + id;
//		}
//	
//	
	
	
	public JSONObject toJson() {
	    if (this.messageContent == null) {
	    	return new JSONObject();
	    }
		JSONObject main = new JSONObject();
		// first, create json for each smaller items
		try {
			main.put("subject", this.getSubject());
			main.put("from", this.getFrom());
			
			// JSON arrays of strings
			main.put("to", new JSONArray(this.getTo()));
			main.put("cc", new JSONArray(this.getCc()));
			main.put("bcc", new JSONArray(this.getBcc()));
			
			// int
			main.put("priority", this.getPriority());
			
			// date times
			main.put("sentTime", fmt.print((this.getSentTime())));
			
			
			if (this.getMessageContent().getClass().equals(InfoMessageContent.class)) {
				main.put("messageType", "Info");
			} else if (this.getMessageContent().getClass().equals(QuestionMessageContent.class)) {
				main.put("messageType", "Question");
			} else {
				main.put("messageType", "Meeting");
			}
			// message content
			main.put("messageContent", this.getMessageContent().toJson());
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		return main;
	}
	
	/**
	 * Before using this method, two conditions must be met:
	 * 1) Attached JSON must be already be parsed into a JSONObject
	 * 2) An EmailMessage object with corresponding MessageContent instantiated with 
	 *    proper parameters, including list arrays. 
	 *  
	 * @param json
	 * @return EmailMessage
	 */
	public static EmailMessage fromJson(JSONObject json) {
		EmailMessage ret = new EmailMessage();
		// set current object fields to values in json
		try {
			ret.setSubject(json.getString("subject"));
			ret.setFrom(json.getString("from"));

			// get all to values
			JSONArray toArr = json.getJSONArray("to");
			ArrayList<String> to = new ArrayList<String>();
			
			for (int i = 0; i < toArr.length(); i++) {
				String toStr = toArr.getString(i);
				to.add(toStr);
			}
			ret.setTo(to);

			// get all cc values
			JSONArray ccArr = json.getJSONArray("cc");
			ArrayList<String> cc = new ArrayList<String>();
			for (int i = 0; i < ccArr.length(); i++) {
				String ccStr = ccArr.getString(i);
				cc.add(ccStr);
			}
			ret.setCc(cc);
			
			// get all bcc values
			JSONArray bccArr = json.getJSONArray("bcc");
			ArrayList<String> bcc = new ArrayList<String>();

			for (int i = 0; i < bccArr.length(); i++) {
				String bccStr = bccArr.getString(i);
				bcc.add(bccStr);
			}
			ret.setBcc(bcc);
			ret.setPriority( json.getInt("priority"));

			ret.setSentTime(fmt.parseDateTime(json.getString("sentTime")));

			String type = json.getString("messageType");
			if (type.equalsIgnoreCase("Info")) {
				InfoMessageContent info = new InfoMessageContent(false, 
						new ArrayList<Object>(), "");
				ret.setMessageContent(info);
			} else if (type.equalsIgnoreCase("Question")) {
				QuestionMessageContent ques = new QuestionMessageContent(
						false, new ArrayList<Question>());
				ret.setMessageContent(ques);
			} else {
				MeetingMessageContent meet = new MeetingMessageContent
						("", "", "", null, new ArrayList<Interval>());
				ret.setMessageContent(meet);
			}
			ret.messageContent.fromJson(
					json.getJSONObject("messageContent"));
			


		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return ret;

	}

}
