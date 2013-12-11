package com.example.emailmanifesto.Managers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.emailmanifesto.InboxActivity;
import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.Clients.GmailIMAPClient;
import com.example.emailmanifesto.Clients.GmailSMTPClient;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.SQLiteEmail;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class GmailManager implements InterfaceEmailManager {
	public static final String TAG = "GmailManager";
	public static final String GMAIL_OAUTH2_SCOPE = "oauth2:https://mail.google.com/";

	/**
	 * used for exponential backoff
	 */
	public static final long MAX_TIMEOUT = 3000;

	private Account mAccount;
	private String mToken;

	private GmailManager(Account account) {
		mAccount = account;
	}

	public static Account[] getDeviceEmailAddresses(Context context) {
		AccountManager accountMgr = AccountManager.get(context);
		Account[] accounts = accountMgr.getAccountsByType("com.google");

		if (accounts == null) {
			return new Account[0];
		} else {
			return accounts;
		}
	}

	/**
	 * Factory to create instance of GmailManager
	 * 
	 * @param account
	 *            The account object that holds the gmail email account
	 * @param activity
	 *            The activity that this request is coming from
	 * @return GmailManager object with account set and and callback that will
	 *         eventually set token
	 */
	public static GmailManager CreateGmailManager(Account account,
			Activity activity) {
		GmailManager manager = new GmailManager(account);

		// setup manager with OAUTH2 token
		AccountManagerCallback<Bundle> callback = manager.new OnTokenAcquired();
		manager.getOauth2Token(account, activity, callback);

		return manager;
	}

	/**
	 * Callback used to set token in the manager class
	 * 
	 * @author jbialas
	 */
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			Bundle bundle;
			mToken = null;
			try {
				bundle = result.getResult();
				mToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
			} catch (Exception ex) {
				Log.e(TAG, "Error getting token", ex);
			}
		}

	}

	/**
	 * Gets OAUTH2 token for the given account, activity, and returns it as part
	 * of the registered callback. The result bundle contains the token under
	 * AccountManager.KEY_AUTHTOKEN.
	 * 
	 * @param account
	 *            the account for which to get the token
	 * @param activity
	 *            the calling activity, required to get the AccountManager
	 * @param callback
	 *            callback that will handle the token
	 */
	private void getOauth2Token(Account account, Activity activity,
			AccountManagerCallback<Bundle> callback) {
		AccountManager mgr = AccountManager.get(activity);
		mgr.getAuthToken(account, GMAIL_OAUTH2_SCOPE, null, activity, callback,
				null);
	}
	
	@Override
	public List<SQLiteEmail> getEmails(long minUID, Context context) {

		ArrayList<SQLiteEmail> emailList = new ArrayList<SQLiteEmail>();

		// see if token exists
		// Performs exponential backoff to wait for token
		long currBackoff = 50;
		for (currBackoff = 50; currBackoff <= MAX_TIMEOUT; currBackoff *= 2) {
			try {
				Thread.sleep(currBackoff);
			} catch (Exception ex) {
				// This should never happen unless Dalvik is stupid
				ex.printStackTrace();
			}
			// token has been set
			if (mToken != null) {
				break;
			}
		}
		if (currBackoff > MAX_TIMEOUT) {
			Log.e(TAG, "Token has not been set");
			return null;
		}
		try {
			IMAPStore imapStore = GmailIMAPClient.instance.connectToImap(
					mAccount.name, mToken, false);
			IMAPFolder imapFolder = (IMAPFolder) imapStore.getFolder("Inbox");
			imapFolder.open(Folder.READ_ONLY);
			
			//if true then no new emails
			if(minUID > imapFolder.getUIDNext()){
				return emailList;
			}
			
			// get all messages since last one
			Message[] messages = imapFolder.getMessagesByUID(minUID,
					IMAPFolder.LASTUID);
			
			// Set highest UID processed
			SharedPreferences settings = context.getSharedPreferences(
					InboxActivity.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong(InboxActivity.UID, imapFolder.getUIDNext());
			editor.commit();

			SQLiteEmail email;
			long uid;
			String jsonAttachment;
			// process messages i.e. make sure they have the correct attachment
			for (Message message : messages) {
				if (containsApplicationAttachment(message)) {
					uid = imapFolder.getUID(message);
					jsonAttachment = getAttachmentAsString(message);
					email = new SQLiteEmail(uid, jsonAttachment);
					emailList.add(email);
				}
			}

		} catch (Exception ex) {
			Log.e(TAG, "Error opening IMAP", ex);
			return null;
		}

		return emailList;
	}

	private boolean containsApplicationAttachment(Message message) {
		try {
			// suppose 'message' is an object of type Message
			String contentType = message.getContentType();

			if (contentType.contains("multipart")) {
				// this message may contain attachment
				Multipart multiPart = (Multipart) message.getContent();

				for (int i = 0; i < multiPart.getCount(); i++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
					// check if this is an attachment with a matching name
					if (part.getFileName() != null
							&& part.getFileName().equalsIgnoreCase(
									EmailMessage.ATTACHMENT_FILENAME)) {
						return true;
					}
				}
			}
		} catch (Exception ex) {
			Log.w(TAG, "Unable to get attachment", ex);
		}
		return false;
	}

	private String getAttachmentAsString(Message message) {
		StringBuffer attachmentContent;
		InputStream is = null;
		BufferedReader reader = null;

		try {
			// suppose 'message' is an object of type Message
			String contentType = message.getContentType();

			if (contentType.contains("multipart")) {
				// this message may contain attachment
				Multipart multiPart = (Multipart) message.getContent();

				for (int i = 0; i < multiPart.getCount(); i++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
					// check if this is an attachment with a matching name
					if (part.getFileName() != null
							&& part.getFileName().equalsIgnoreCase(
									EmailMessage.ATTACHMENT_FILENAME)) {
						attachmentContent = new StringBuffer();
						is = part.getInputStream();
						reader = new BufferedReader(new InputStreamReader(is));
						if (is != null) {
							String str = "";
							while ((str = reader.readLine()) != null) {
								attachmentContent.append(str + "\n");
							}
						}
						reader.close();
						is.close();

						return attachmentContent.toString();
					}
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Unable to get attachment", ex);
		}

		return null;
	}

	@Override
	public void sendEmailAsync(String subject, String body, String recipients,
			SentEmailCallback callback) {
		SendGmailEmailTask emailTask = new SendGmailEmailTask(callback);
		emailTask.execute(new String[] { subject, body, mAccount.name,
				recipients, null });
	}

	@Override
	public void sendEmailWithJsonAttachmentAsync(String subject, String body,
			String recipients, String jsonAttachment, SentEmailCallback callback) {

		SendGmailEmailTask emailTask = new SendGmailEmailTask(callback);
		emailTask.execute(new String[] { subject, body, mAccount.name,
				recipients, jsonAttachment });
	}

	private class SendGmailEmailTask extends AsyncTask<String, Void, Boolean> {
		private SentEmailCallback callback;

		public SendGmailEmailTask(SentEmailCallback callback) {
			this.callback = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// see if token exists
			// Performs exponential backoff to wait for token

			long currBackoff = 50;
			for (currBackoff = 50; currBackoff <= MAX_TIMEOUT; currBackoff *= 2) {
				try {
					Thread.sleep(currBackoff);
				} catch (Exception ex) {
					// This should never happen unless Dalvik is stupid
					ex.printStackTrace();
				}
				// token has been set
				if (mToken != null) {
					break;
				}
			}
			if (currBackoff > MAX_TIMEOUT) {
				Log.e(TAG, "Token has not been set");
				return false;
			}

			boolean success = false;
			try {
				//check if an attachment parameter was provided
				if (params[4] == null) {
					GmailSMTPClient.instance.sendMail(params[0], params[1],
							params[2], mToken, params[3]);
				} else {
					GmailSMTPClient.instance.sendMailWithAttachment(params[0],
							params[1], params[2], mToken, params[3], params[4]);
				}

				success = true;
			} catch (Exception ex) {
				Log.e(TAG, "Error sending email", ex);
				return false;
			}

			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				callback.onSuccess();
			} else {
				callback.onFailure();
			}
		}
	}

}
