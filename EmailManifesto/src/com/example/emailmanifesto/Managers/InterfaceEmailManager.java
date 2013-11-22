package com.example.emailmanifesto.Managers;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public interface InterfaceEmailManager {
	/**
	 * Returns all device emails as accounts on the phone
	 * @param context
	 * 		context of the application/activity
	 * @return
	 * 		array of accounts
	 */
	public Account[] getDeviceEmails(Context context);
	
	/**
	 * Gets OAUTH2 token for the given account, activity, and returns it as part of the registered callback.
	 * The result bundle contains the token under AccountManager.KEY_AUTHTOKEN.
	 * @param account
	 * 		the account for which to get the token
	 * @param activity
	 * 		the calling activity, required to get the AccountManager
	 * @param callback
	 * 		callback that will handle the token
	 */
	public void getOauth2Token(Account account, Activity activity, AccountManagerCallback<Bundle> callback);
	
	/**
	 * 
	 * @param subject
	 * 		subject of the email
	 * @param body
	 * 		body of the email
	 * @param senderEmail
	 * 		email of the sending account
	 * @param token
	 * 		OAUTH2 token to send the email
	 * @param recipients
	 * 		Comma separated list of recipients
	 * @param callback
	 * 		Callback used to process results of email sending operation
	 */
	public void sendEmailAsync(String subject, String body, String senderEmail, String token, String recipients, SentEmailCallback callback);
}
