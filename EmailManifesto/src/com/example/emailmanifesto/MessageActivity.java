package com.example.emailmanifesto;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MessageActivity extends Activity {
	public static final String TAG = "MessageActivity";

	EmailMessage message = new EmailMessage();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		//Set up visual components
		TextView subjectView = (TextView) findViewById(R.id.subjectView);
		TextView tstampView = (TextView) findViewById(R.id.tstampView);
		TextView fromView = (TextView) findViewById(R.id.fromView);
		View contentView = (View) findViewById(R.id.contentView);
		
		//Load in message
		String messageString = getIntent().getStringExtra("message");
		
		try {
			message = EmailMessage.fromJson(new JSONObject(messageString));
		} catch (JSONException ex) {
			Log.e(TAG, "Invalid JSON file", ex);
		}
		fromView.setText(message.getFrom());
		subjectView.setText(message.getSubject());
		// TODO: Do something with content view
		tstampView.setText(message.getSentTime().toLocalTime().toString());
		
		// TODO: set the proper fragment to display the content
		//similar to Compose
		if(message.getMessageContent() instanceof InfoMessageContent) {
			
		}
			
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
