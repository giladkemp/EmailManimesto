package com.example.emailmanifesto;

import java.util.List;

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
	public static void updateInboxOperation(InterfaceEmailManager emailManager, SQLiteInboxManager inboxManager){
		//open inbox database for reads
		inboxManager.openForRead();
		
		//retrieve highest UID from stored inbox
		long minUID = inboxManager.getMaximumUIDFromInbox();
		
		//close database to then open up for writes
		inboxManager.close();
		
		//open for writes
		inboxManager.openForWrite();
		
		//retrieve messages with greater UID from IMAP connection
		List<SQLiteEmail> emailList = emailManager.getEmails(minUID);
		
		//put emails into inbox
		inboxManager.insertEmails(emailList);
		
		//close inbox database
		inboxManager.close();
	}
}
