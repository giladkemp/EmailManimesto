package com.example.emailmanifesto.Managers;

import java.util.List;

import android.content.Context;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.DataModels.SQLiteEmail;

public interface InterfaceEmailManager {
	/**
	 * 
	 * @param subject
	 * 		subject of the email
	 * @param body
	 * 		body of the email
	 * @param recipients
	 * 		Comma separated list of recipients
	 * @param callback
	 * 		Callback used to process results of email sending operation
	 */
	public void sendEmailAsync(String subject, String body, String recipients, SentEmailCallback callback);

	/**
	 * 
	 * @param subject
	 * 		subject of the email
	 * @param body
	 * 		body of the email
	 * @param recipients
	 * 		comma separated list of recipients
	 * @param jsonAttachment
	 * 		JSON to be attached to the email
	 * @param callback
	 * 		callback used to process results of the email sending operation
	 */
	public void sendEmailWithJsonAttachmentAsync(String subject, String body, String recipients, String jsonAttachment, SentEmailCallback callback);
	
	/**
	 * 
	 * @param minUID
	 * 		minimum UID to be returned
	 * @return
	 * 		list of emails ready to be placed into database
	 */
	public List<SQLiteEmail> getEmails(long minUID, Context context);
}