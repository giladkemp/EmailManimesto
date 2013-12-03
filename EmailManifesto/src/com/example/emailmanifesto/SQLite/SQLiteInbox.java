package com.example.emailmanifesto.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteInbox extends SQLiteOpenHelper {

	public static final String TABLE_INBOX = "inbox";
	public static final String INBOX_COLUMN_ID = "_id";
	public static final String INBOX_COLUMN_UID = "uid";
	public static final String INBOX_COLUMN_ATTACHMENT = "attachment";

	private static final String DATABASE_NAME = "manifesto_inbox.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_INBOX
			+ "(" + INBOX_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ INBOX_COLUMN_UID + " INTEGER," + INBOX_COLUMN_ATTACHMENT
			+ " TEXT NOT NULL);";;

	public SQLiteInbox(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INBOX);
		onCreate(db);
	}
}
