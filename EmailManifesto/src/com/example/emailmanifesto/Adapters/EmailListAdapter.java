package com.example.emailmanifesto.Adapters;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.emailmanifesto.R;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;
import com.example.emailmanifesto.DataModels.MeetingMessageContent;
import com.example.emailmanifesto.DataModels.QuestionMessageContent;
import com.example.emailmanifesto.SQLite.SQLiteInbox;

public class EmailListAdapter extends ResourceCursorAdapter{
	public static final String TAG = "EmailListAdapter";
	
	private int mLayout;
	
	public EmailListAdapter(Context context, int layout, Cursor c, int flags){
		super(context, layout, c, flags);
		mLayout = layout;
	}

    @Override  
    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
        return inflater.inflate(mLayout, parent, false);  
  
    }  
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView fromView = (TextView) view.findViewById(R.id.fromView);
		TextView subjectView = (TextView) view.findViewById(R.id.subjectView);
		TextView previewView = (TextView) view.findViewById(R.id.previewView);
		TextView timeView = (TextView) view.findViewById(R.id.timeView);
		TextView priorityView = (TextView) view.findViewById(R.id.priorityView);
		
		String jsonText = cursor.getString(cursor.getColumnIndex(SQLiteInbox.INBOX_COLUMN_ATTACHMENT));
		EmailMessage message = null;
		
		try {
			message = EmailMessage.fromJson(new JSONObject(jsonText));
		} catch (JSONException ex) {
			Log.e(TAG, "Invalid JSON file", ex);
		}
		if (message == null) {
			// if json rets w/ error
			// TODO: Fix accordingly
			// lets just hope this doesn't happen for now >.>
			return;
		}
		fromView.setText(context.getString(R.string.from) + message.getFrom());
		subjectView.setText(message.getSubject());
		
		//deal with preview
		if(message.getMessageContent() instanceof InfoMessageContent){
			InfoMessageContent cont = (InfoMessageContent)message.getMessageContent();
			previewView.setText(context.getString(R.string.email_type_info) + cont.getType());
		}
		else if(message.getMessageContent() instanceof MeetingMessageContent){
			previewView.setText(context.getString(R.string.email_type_meeting));
		}
		else if(message.getMessageContent() instanceof QuestionMessageContent){
			previewView.setText(context.getString(R.string.email_type_question));
		}
		
		//set time view to display "Mon 00 00:00"
		timeView.setText(message.getSentTime().toLocalDateTime().toString("MMM' 'd' 'H':'m"));
		
		//set priority view and background color accordingly
		priorityView.setText(context.getString(R.string.priority) + message.getPriority());
		switch(message.getPriority()){
			case 1:priorityView.setBackgroundColor(0x600000FF); break;
			case 2:priorityView.setBackgroundColor(0x6000B000); break;
			case 3:priorityView.setBackgroundColor(0x60FFFF00); break;
			case 4:priorityView.setBackgroundColor(0x60FF8000); break;
			case 5:priorityView.setBackgroundColor(0x60FF0000); break;
		}		
	}
}