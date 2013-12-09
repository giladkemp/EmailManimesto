package com.example.emailmanifesto;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.Managers.GmailManager;
import com.example.emailmanifesto.Managers.InterfaceEmailManager;
import com.example.emailmanifesto.Managers.SQLiteInboxManager;

public class TestActivity extends Activity{
	
	private InterfaceEmailManager mEmailManager;
	private SQLiteInboxManager mInboxManager;
	
	private TextView mTestTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_layout);
		
		mTestTextView = (TextView) findViewById(R.id.testView);
		
		managerInitialization();
		
		//This code will send email: requires OnEmailSent below
		mEmailManager.sendEmailAsync("Test Email", "This is a test email", "kuba1000@gmail.com", new OnEmailSent());
		
		Toast.makeText(this, "Getting new emails", Toast.LENGTH_LONG).show();
		
		//operation to get new emails and load them into the inbox database
		UpdateInboxOperation updateOperation = new UpdateInboxOperation();
		updateOperation.execute(new Void[0]);
		
		// run this code to get cursor to get emails in descending order by UID
//		Cursor cursor = mInboxManager.getCursorToEmails();
		
	}
	
	private void managerInitialization(){
		//set email account to use
		//all options should be presented to user unless there's only one
		Account emailAccount = GmailManager.getDeviceEmailAddresses(this)[0];
		
		//create instance of emailManager
		mEmailManager = GmailManager.CreateGmailManager(emailAccount, this);
		
		//set up inboxManager
		mInboxManager = new SQLiteInboxManager(this);
		
	}
	
	private class UpdateInboxOperation extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			InboxUpdateOperation.updateInboxOperation(mEmailManager, mInboxManager);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
	         //Do something
			Toast.makeText(TestActivity.this, "Update operation complete!", Toast.LENGTH_LONG).show();
	     }

		
	}

	
	private class OnEmailSent implements SentEmailCallback{

		@Override
		public void onSuccess() {
			mTestTextView.setText("Successfully sent");
		}

		@Override
		public void onFailure() {
			mTestTextView.setText("Email was not sent");
		}
		
	}
}
