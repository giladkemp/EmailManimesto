package com.example.emailmanifesto;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.emailmanifesto.Adapters.EmailListAdapter;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.Managers.GmailManager;
import com.example.emailmanifesto.Managers.InterfaceEmailManager;
import com.example.emailmanifesto.Managers.SQLiteInboxManager;
import com.example.emailmanifesto.SQLite.SQLiteInbox;

public class InboxActivity extends ListActivity implements
		ActionBar.OnNavigationListener {

	public static final String TAG = "InboxActivity";
	public static final String PREFS_NAME = "EmailManifestoPreferences";
	public static final String UID = "MAX_UID";

	private InterfaceEmailManager mEmailManager;
	private SQLiteInboxManager mInboxManager;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		
		//UI CREATION
		
//		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putLong(UID, 11000);
//		editor.commit();

		// Set up the action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Inbox");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); 
		
		//Set icon if on 4.0 or greater
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			actionBar.setIcon(R.drawable.ic_action_email);
		} else {
			actionBar.setDisplayUseLogoEnabled(false);
		}
		
		//DATA INITIALIZATION
		managerInitialization();
		
		Toast.makeText(this, "Getting new emails", Toast.LENGTH_LONG).show();
		
		//operation to get new emails and load them into the inbox database
		new UpdateInboxOperation().execute(new Void[0]);
		
		//set up connection to MessageActivity
		getListView().setOnItemClickListener(new OnMessageSelectedListener());

	}
	
	private class OnMessageSelectedListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position,
				long id) {
			if(listView == null){
				return;
			}
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			
			//get the JSON attachment
			String attachment = cursor.getString(cursor.getColumnIndex(SQLiteInbox.INBOX_COLUMN_ATTACHMENT));
			if(attachment != null && !attachment.equals("")){
				//send intent to open up message reader
				Intent messageReader = new Intent(InboxActivity.this, MessageActivity.class);
				messageReader.putExtra(MessageActivity.JSON_STRING_EXTRA, attachment);
				InboxActivity.this.startActivity(messageReader);
			}
			
		}
		
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
		
		ProgressDialog p = new ProgressDialog
				(InboxActivity.this.getApplicationContext(), ProgressDialog.STYLE_SPINNER);
		
		@Override
		protected Void doInBackground(Void... params) {
			
			
			
			
			
			
			InboxUpdateOperation.updateInboxOperation(InboxActivity.this, mEmailManager, mInboxManager);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(InboxActivity.this, "Update operation complete!", Toast.LENGTH_LONG).show();
			
			Log.e(TAG, "Done getting emails!");
			
			Cursor c = mInboxManager.getCursorToEmails();;
			
			//Set list adapter
			setListAdapter(new EmailListAdapter(InboxActivity.this, R.layout.message_item, c, 0));
	     
		
			
		
		}

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_compose:
	            //Compose action started
	        	Intent composeActivityIntent = new Intent(this, ComposeActivity.class);
	        	composeActivityIntent.putExtra("EmailManager", mEmailManager);
	        	this.startActivity(composeActivityIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO: open the message in reading window
		return false;
	}

	
}
