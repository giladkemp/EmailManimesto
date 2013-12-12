package com.example.emailmanifesto;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;
import com.example.emailmanifesto.DataModels.MeetingMessageContent;
import com.example.emailmanifesto.DataModels.Question;
import com.example.emailmanifesto.DataModels.QuestionMessageContent;

public class MessageActivity extends Activity {
	public static final String TAG = "MessageActivity";

	public static final String JSON_STRING_EXTRA = "JSON_STRING_EXTRA";
	
	EmailMessage mMessage = new EmailMessage();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		//Set up visual components
		TextView subjectView = (TextView) findViewById(R.id.subjectView);
		TextView tstampView = (TextView) findViewById(R.id.tstampView);
		TextView fromView = (TextView) findViewById(R.id.fromView);
		TextView contentView = (TextView) findViewById(R.id.contentView);
		
		//Load in message
		String messageString = getIntent().getStringExtra(JSON_STRING_EXTRA);
		try {
			mMessage = EmailMessage.fromJson(new JSONObject(messageString));
		} catch (JSONException ex) {
			Log.e(TAG, "Invalid JSON file", ex);
		}
		
		subjectView.setText(getString(R.string.subject) + mMessage.getSubject());
		fromView.setText(getString(R.string.from) + mMessage.getFrom());
		tstampView.setText(mMessage.getSentTime().toLocalDateTime().toString("MMMM' 'd', 'y' 'H':'m"));
		
		//set TextView accordingly
		if(mMessage.getMessageContent() instanceof InfoMessageContent) {
			InfoMessageContent content = (InfoMessageContent)mMessage.getMessageContent();
			StringBuilder sb = new StringBuilder();
			
			//parse information
			if(content.getType().equals(InfoMessageContent.TYPE_TEXT)){
				sb.append("The sender would like you to be aware of the following information:\n" );
				for(Object text : content.getAttachedContent()){
					String info = (String)text;
					sb.append(info).append("\n");
				}
			}
			
			//parse response
			sb.append("Your response is").append(content.isResponseRequested() ? "requested." : " not required.");
			
			contentView.setText(sb);
		}
		
		else if(mMessage.getMessageContent() instanceof MeetingMessageContent){
			MeetingMessageContent content = (MeetingMessageContent)mMessage.getMessageContent();
			StringBuilder sb = new StringBuilder();
			
			//process duration
			Duration duration = content.getDuration(); // in milliseconds
			PeriodFormatter formatter = new PeriodFormatterBuilder()
			     .appendHours()
			     .appendSuffix(" hour(s) and ")
			     .appendMinutes()
			     .appendSuffix(" minute(s)")
			     .toFormatter();
			String formatted = formatter.print(duration.toPeriod());
			
			sb.append("The sender would like to meet with you regarding ")
				.append(content.getTitle()).append(" at ").append(content.getLocation())
				.append(" for a period of ").append(formatted).append(".\n")
				.append("The sender is available during the following times:\n");
			
			for(Interval interval : content.getAvailableIntervals()){
				sb.append(interval.getStart().toLocalDateTime().toString("MMMM' 'd', 'y' 'H':'m"))
				.append(" to ").append(interval.getEnd().toLocalDateTime().toString("MMMM' 'd', 'y' 'H':'m"))
				.append("\n");
			}
			
			sb.append("\nYour timely response is requested.");
			contentView.setText(sb);
		}
		
		else if(mMessage.getMessageContent() instanceof QuestionMessageContent){
			QuestionMessageContent content = (QuestionMessageContent)mMessage.getMessageContent();
			StringBuilder sb = new StringBuilder();
			
			sb.append("The sender was hoping you could answer the following questions:\n");
			
			for(Question q : content.getQuestions()){
				sb.append(q.getQuestion()).append("\n");
			}
			
			sb.append("\nThank you in advance!");
			contentView.setText(sb);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
