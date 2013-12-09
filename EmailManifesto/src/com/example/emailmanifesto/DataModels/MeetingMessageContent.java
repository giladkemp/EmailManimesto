package com.example.emailmanifesto.DataModels;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MeetingMessageContent implements InterfaceMessageContent {
	private String title;
	private String location;
	private String description;
	private Duration duration;
	private List<Interval> availableIntervals;
	
	
	private static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

	// constructors
	public MeetingMessageContent(String title, String location,
			String description, Duration duration,
			List<Interval> availableIntervals) {
		super();
		this.title = title;
		this.location = location;
		this.description = description;
		this.duration = duration;
		this.availableIntervals = availableIntervals;

	}





	// getters and setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public List<Interval> getAvailableIntervals() {
		return availableIntervals;
	}

	public void setAvailableIntervals(List<Interval> availableIntervals) {
		this.availableIntervals = availableIntervals;

	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((availableIntervals == null) ? 0 : availableIntervals
						.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		MeetingMessageContent other = (MeetingMessageContent) obj;
		if (availableIntervals == null) {
			if (other.availableIntervals != null)
				return false;
		} else if (!availableIntervals.equals(other.availableIntervals))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}





	@Override
	public JSONObject toJson() {
		
		JSONObject main = new JSONObject();
		try {
			main.put("title", this.getTitle());
			main.put("location", this.getLocation());
			main.put("description", this.getDescription());
			
			// convert duration into minutes
			main.put("duration", this.getDuration().getStandardMinutes());
			
			// for each interval, get startTime and endTime
			// place into array as [ [sTime1, eTime1],  [sTime2, eTime2]  etc ]
			JSONArray timeArr = new JSONArray();
			for (Interval i : this.getAvailableIntervals()) {
				String sTime = fmt.print(i.getStart());
				String eTime = fmt.print(i.getEnd());
				JSONArray intervalArr = new JSONArray();
				intervalArr.put(sTime);
				intervalArr.put(eTime);
				timeArr.put(intervalArr);
			}
			
			main.put("availableIntervals", timeArr);
//			main.put("intervalCount", this.getAvailableIntervals().size());
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return main;
	}





	@Override
	public InterfaceMessageContent fromJson(JSONObject json) {
		try {
			this.title = json.getString("title");
			this.location = json.getString("location");
			this.description = json.getString("description");
			
			long durationMins = json.getLong("duration");
			this.duration = new Duration(durationMins);
			
			// for each interval in json, need to create interval var
			JSONArray timeArr = json.getJSONArray("availableIntervals");
			this.availableIntervals.clear();
			for (int i = 0; i < timeArr.length(); i++) {
				JSONArray intervalArr = timeArr.getJSONArray(i);
				// get sTime and eTime
				DateTime sDT = fmt.parseDateTime(intervalArr.getString(0)); // sTime
				DateTime eDT = fmt.parseDateTime(intervalArr.getString(1)); // eTime
				Interval interval = new Interval(sDT, eDT);
				this.availableIntervals.add(interval);
				
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		
		return this;
	}

}
