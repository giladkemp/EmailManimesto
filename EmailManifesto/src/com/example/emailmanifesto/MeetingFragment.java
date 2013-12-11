package com.example.emailmanifesto;

import java.util.Calendar;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MeetingFragment extends Fragment{
	
	static final int TIME_DIALOG_ID = 0;
	private TextView mTimeDisplay;
	private Button mPickTime;

	private int mHour;
	private int mMinute;

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};
	
	
	 public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	    }
	    
	 	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.new_meeting_fragment, container, false);
		}
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        
	        
	        // TODO: You need to set up time display, get the view from the fragment
	        
	        
			// get the current time
			final Calendar c = Calendar.getInstance();
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);

			// display the current date
			updateDisplay();

	    }

	    @Override
	    public void onActivityCreated(Bundle savedState) {
			super.onActivityCreated(savedState);
			
			
		}
	    
	    @Override
	    public void onStart() {
	        super.onStart();
	        
	    }
	    
	    private void updateDisplay() {
//			mTimeDisplay.setText(new StringBuilder().append(pad(mHour)).append(":")
//					.append(pad(mMinute)));
		}

		private static String pad(int c) {
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}

		
}
