package com.example.emailmanifesto.Managers;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.emailmanifesto.DataModels.SQLiteEmail;
import com.example.emailmanifesto.SQLite.SQLiteInbox;

public class SQLiteInboxManager {
	private SQLiteInbox dbHelper;
	private SQLiteDatabase database;

	public static final String[] ALL_COLUMNS = { SQLiteInbox.INBOX_COLUMN_ID,
			SQLiteInbox.INBOX_COLUMN_UID, SQLiteInbox.INBOX_COLUMN_ATTACHMENT };

	public SQLiteInboxManager(Context context) {
		dbHelper = new SQLiteInbox(context);
	}

	public void openForWrite() {
		// guarantee idempotence
		if (database == null) {
			database = dbHelper.getWritableDatabase();
		}
	}

	public void openForRead() {
		// guarantee idempotence
		if (database == null) {
			database = dbHelper.getReadableDatabase();
		}
	}

	public boolean insertEmails(List<SQLiteEmail> emailsToAdd) {
		try {
			openForWrite();
			database.beginTransaction();
			for (SQLiteEmail email : emailsToAdd) {
				if (email.getUID() > 0 && email.getJsonAttachment() != null) {
					ContentValues values = new ContentValues();
					values.put(SQLiteInbox.INBOX_COLUMN_UID, email.getUID());
					values.put(SQLiteInbox.INBOX_COLUMN_ATTACHMENT,
							email.getJsonAttachment());
					database.insert(SQLiteInbox.TABLE_INBOX, null, values);
				}
			}
			database.setTransactionSuccessful();
			return true;
		} catch (SQLException ex) {
			return false;
		} finally {
			database.endTransaction();
		}
	}
	
	public Cursor getCursorToEmails(){
		openForWrite();
		
		Cursor emailCursor = database.rawQuery("SELECT " + SQLiteInbox.INBOX_COLUMN_ID
				+ ", " + SQLiteInbox.INBOX_COLUMN_UID
				+ ", " + SQLiteInbox.INBOX_COLUMN_ATTACHMENT
				+ " FROM " + SQLiteInbox.TABLE_INBOX
				+ " ORDER BY " + SQLiteInbox.INBOX_COLUMN_UID + " DESC"
				, null);
		emailCursor.moveToFirst();
		
		return emailCursor;
	}

// Retain for legacy purposes
	
//	public long getMaximumUIDFromInbox() {
//		long uid = 0;
//
//		openForRead();
//
//		// get maximum UID
//		Cursor uidCursor = database.rawQuery("SELECT MAX("
//				+ SQLiteInbox.INBOX_COLUMN_UID + ") FROM "
//				+ SQLiteInbox.TABLE_INBOX, null);
//		// set cursor to first record
//		boolean areResultsReturned = uidCursor.moveToFirst();
//		if (areResultsReturned) {
//			// if no results, return zero
//			uid = uidCursor.getLong(0);
//		}
//		// close the cursor
//		uidCursor.close();
//
//		return uid;
//	}

	public void close() {
		database = null;
		dbHelper.close();
	}
}
