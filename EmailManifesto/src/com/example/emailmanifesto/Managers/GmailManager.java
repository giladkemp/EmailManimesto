package com.example.emailmanifesto.Managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.Clients.GmailSMTPClient;

public class GmailManager implements InterfaceEmailManager {
	public static final String TAG = "GmailManager";
	public static final String GMAIL_OAUTH2_SCOPE = "oauth2:https://mail.google.com/";

	@Override
	public Account[] getDeviceEmails(Context context) {
		AccountManager accountMgr = AccountManager.get(context);
		Account[] accounts = accountMgr.getAccountsByType("com.google");

		if (accounts == null) {
			return new Account[0];
		} else {
			return accounts;
		}
	}
	
	@Override
	public void getOauth2Token(Account account, Activity activity, AccountManagerCallback<Bundle> callback){
		AccountManager mgr = AccountManager.get(activity);
		mgr.getAuthToken(account, GMAIL_OAUTH2_SCOPE, null, activity, callback, null);
	}
	
	@Override
	public void sendEmailAsync(String subject, String body, String senderEmail, String token, String recipients, SentEmailCallback callback){
		SendGmailEmailTask emailTask = new SendGmailEmailTask(callback);
		emailTask.execute(new String[]{subject, body, senderEmail, token, recipients});
	}
	
	
	
	
	
	
	private class SendGmailEmailTask extends AsyncTask<String, Void, Boolean>{
		private SentEmailCallback callback;
		
		public SendGmailEmailTask(SentEmailCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean success = false;
			try {
				GmailSMTPClient.instance.sendMail(params[0], params[1], params[2], params[3], params[4]);
				success = true;
			} catch (Exception ex) {
				Log.e(TAG, "Error sending email", ex);
			}
			
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			if(result){
				callback.onSuccess();
			}
			else{
				callback.onFailure();
			}
		}
	}	

}
