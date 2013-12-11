package com.example.emailmanifesto;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.emailmanifesto.DataModels.SQLiteEmail;
import com.example.emailmanifesto.Managers.InterfaceEmailManager;
import com.example.emailmanifesto.Managers.SQLiteInboxManager;

public class InboxUpdateOperation {
	
	public static final String TAG = "InboxUpdateOperation";
	
	/**
	 * This operation is required to be in a non-UI thread
	 * @param emailManager
	 * 		Initialized instance of an email manager
	 * @param inboxManager
	 * 		Initialized instance of the SQLite inbox manager
	 */
	public static void updateInboxOperation(Context context, InterfaceEmailManager emailManager, SQLiteInboxManager inboxManager){
		//open inbox database for reads
		inboxManager.openForRead();
		
		//retrieve highest UID from stored inbox
		SharedPreferences settings = context.getSharedPreferences(InboxActivity.PREFS_NAME, 0);
		long minUID = settings.getLong(InboxActivity.UID, 1);
		
		Log.d(TAG, "MY UID: "+ minUID);
		
		//close database to then open up for writes
		inboxManager.close();
		
		//open for writes
		inboxManager.openForWrite();
		
		//retrieve messages with greater UID from IMAP connection
		List<SQLiteEmail> emailList = emailManager.getEmails(minUID, context);
		
		//put emails into inbox
		inboxManager.insertEmails(emailList);
		
		//close inbox database
		inboxManager.close();
	}
}
