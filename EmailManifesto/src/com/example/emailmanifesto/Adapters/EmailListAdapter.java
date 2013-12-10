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
import com.example.emailmanifesto.SQLite.SQLiteInbox;

public class EmailListAdapter extends ResourceCursorAdapter{
	public static final String TAG = "EmailListAdapter";
	
	public EmailListAdapter(Context context, int layout, Cursor c, int flags){
		super(context, layout, c, flags);
		
	}

    @Override  
    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
        return inflater.inflate(R.layout.message_item, parent, false);  
  
    }  
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView fromView = (TextView) view.findViewById(R.id.fromView);
		TextView subjectView = (TextView) view.findViewById(R.id.subjectView);
		TextView previewView = (TextView) view.findViewById(R.id.previewView);
		TextView timeView = (TextView) view.findViewById(R.id.timeView);
		TextView priorityView = (TextView) view.findViewById(R.id.priorityView);
		
		String jsonText = cursor.getString(cursor.getColumnIndex(SQLiteInbox.INBOX_COLUMN_ATTACHMENT));
		EmailMessage message = new EmailMessage();
		
		try {
			message.fromJson(new JSONObject(jsonText));
		} catch (JSONException ex) {
			Log.e(TAG, "Invalid JSON file", ex);
		}
		
		fromView.setText(message.getFrom());
		subjectView.setText(message.getSubject());
		// TODO: Do something with previewView
		timeView.setText(message.getSentTime().toLocalTime().toString());
		priorityView.setText("" + message.getPriority());
		
		
	}
}