package com.example.emailmanifesto.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;

public class EmailListAdapter extends ResourceCursorAdapter{
	public static final String TAG = "EmailListAdapter";
	
	public EmailListAdapter(Context context, int layout, Cursor c, int flags){
		super(context, layout, c, flags);
		
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}
}