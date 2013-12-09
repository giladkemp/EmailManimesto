package com.example.emailmanifesto.DataModels;

public class SQLiteEmail {
	private long UID;
	private String jsonAttachment;

	public SQLiteEmail(long uID, String jsonAttachment) {
		super();
		UID = uID;
		this.jsonAttachment = jsonAttachment;
	}

	public long getUID() {
		return UID;
	}

	public void setUID(long uID) {
		UID = uID;
	}

	public String getJsonAttachment() {
		return jsonAttachment;
	}

	public void setJsonAttachment(String jsonAttachment) {
		this.jsonAttachment = jsonAttachment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (UID ^ (UID >>> 32));
		result = prime * result
				+ ((jsonAttachment == null) ? 0 : jsonAttachment.hashCode());
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
		SQLiteEmail other = (SQLiteEmail) obj;
		if (UID != other.UID)
			return false;
		if (jsonAttachment == null) {
			if (other.jsonAttachment != null)
				return false;
		} else if (!jsonAttachment.equals(other.jsonAttachment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SQLiteEmail [UID=" + UID + ", jsonAttachment=" + jsonAttachment
				+ "]";
	}

}
