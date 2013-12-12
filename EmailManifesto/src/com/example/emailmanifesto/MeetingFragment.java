package com.example.emailmanifesto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;
import com.example.emailmanifesto.DataModels.MeetingMessageContent;
import com.example.emailmanifesto.Managers.GmailManager;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MeetingFragment extends Fragment{

	static final int TIME_DIALOG_ID = 0;
	EditText subject, to, meetingName, location, description;
	
	Spinner prioritySpinner; 
	int intPriority = 1;
	String priority;
	
	Button sendButton = null;
	
	private static Button mPickTime;
	private static Button mPickDate;
	private int mHour;
	private int mMinute;
	private int mYear, mMonth, mDay;

	private static String mStringDate;
	private static String mStringTime;

	public static class DatePickerFragment extends DialogFragment implements
	DatePickerDialog.OnDateSetListener {



		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mStringDate = (year) + "-" + (monthOfYear+1) + "-" + dayOfMonth;
			MeetingFragment.mPickDate.setText("set date (" + mStringDate + ")");
		}
	}




	public static class TimePickerFragment extends DialogFragment implements
	TimePickerDialog.OnTimeSetListener {



		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			mStringTime = hourOfDay + ":" + minute;
			MeetingFragment.mPickTime.setText("set time (" + mStringTime + ")");
			
			

		}
	}



	public void showDatePickerDialog(View v) {

		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");

	}

	public void showTimePickerDialog(View v) {

		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");

	}








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


	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		final View ourView = this.getView();
		
		this.subject = (EditText) this.getView().findViewById(R.id.meeting_subject);
		this.to = (EditText) this.getView().findViewById(R.id.meeting_to);
		this.meetingName = (EditText) this.getView().findViewById(R.id.meetingValue);
		this.location = (EditText) this.getView().findViewById(R.id.locationValue);
		this.description = (EditText) this.getView().findViewById(R.id.descriptionValue);
		
		this.sendButton = (Button)this.getView().findViewById(R.id.sendButton);
		
		MeetingFragment.mPickTime = (Button)this.getView().findViewById(R.id.pickTime);
		MeetingFragment.mPickDate = (Button)this.getView().findViewById(R.id.pickDate);




		// get the current time
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mStringTime = mHour + ":" + mMinute;
		MeetingFragment.mPickTime.setText("set time (" + mStringTime + ")");

		MeetingFragment.mPickTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showTimePickerDialog(ourView);
			}
			
		});


		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mStringDate = mYear + "-" + mMonth + "-" + mDay;
		MeetingFragment.mPickDate.setText("set date (" + mStringDate + ")");

		
		MeetingFragment.mPickDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDatePickerDialog(ourView);
			}
			
		});
		
		
		this.prioritySpinner = (Spinner) this.getView().findViewById(
				R.id.meeting_priority);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this.getActivity().getApplicationContext(), R.array.priority_array,
				R.layout.custom_spinner);

		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		// Apply the adapter to the spinner
		prioritySpinner.setAdapter(adapter2);
		prioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				priority = (String) parent.getItemAtPosition(pos);
				intPriority = Integer.parseInt(priority);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		
		
		
		
		
		
		
		

	}
	
	
	
	
	OnClickListener sendListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// grab all values
			// typeSpinner.
			String subjectText = subject.getText().toString();
			String toText = to.getText().toString();
			String descriptionText = description.getText().toString();
			// type should already be set

			String meeting =  meetingName.getText().toString();
			String locationText =  location.getText().toString();
			
			// create email object
			EmailMessage email = new EmailMessage();
			// TODO: set email subject, to, cc, bcc, sentTime AND PRIORITY
			email.setSentTime(new DateTime());
			email.setSubject(subjectText);
			email.setTo(new ArrayList<String>(Arrays
					.asList(new String[] { toText })));
			email.setCc(new ArrayList<String>(Arrays
					.asList(new String[] { "" })));
			email.setBcc(new ArrayList<String>(Arrays
					.asList(new String[] { "" })));
			email.setPriority( intPriority);

			
			
			
			String toParse = mStringDate + " " + mStringTime; // Results in "2-5-2012 20:43"
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
			Date date = null;
			try {
				date = formatter.parse(toParse);
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // You will need try/catch around this
			long millis = date.getTime();
			
			
			
			
			DateTime meetingDateTime;
			boolean s = false;
			try {
				meetingDateTime = new DateTime(millis);
//				fmt.parseMillis(arg0)
				s = true;
			} catch (RuntimeException e) {
				meetingDateTime = new DateTime();
				s = false;
			}
			DateTime next;
			if (s) {
				
				next = meetingDateTime
				        .withDayOfWeek(meetingDateTime.getDayOfWeek())
				        .withHourOfDay(meetingDateTime.getHourOfDay()+1)
				        .withMinuteOfHour(meetingDateTime.getMinuteOfHour())
				        .withSecondOfMinute(0)
				        .withMillisOfSecond(0);
			} else {
				next = new DateTime();
			}
			ArrayList<Interval> ints = new ArrayList<Interval>();
			ints.add(new Interval(meetingDateTime, next ));
			// create infoMessageContent
			MeetingMessageContent meetingContent = new MeetingMessageContent
					(meeting, locationText, descriptionText, 
							new Duration(3600000), 
							ints);
			email.setMessageContent(meetingContent);
			
			GmailManager m = (GmailManager)((ComposeActivity) MeetingFragment.this.getActivity()).mEmailManager;
			String from = m.getAccount().name;
			email.setFrom(from);
			// TODO: Not yet completed. Not proper way...
			// InboxActivity.this.getEmailManager().sendEmailAsync
			// (subject, body, recipients, callback);

			// CSV format
			ArrayList<String> allRecepients = new ArrayList<String>();
			allRecepients.addAll(email.getTo());
			allRecepients.addAll(email.getCc());
			allRecepients.addAll(email.getBcc());
			String csv = allRecepients.toString().replace("[", "")
					.replace("]", "").replace(", ", ",");
			
			
			
			m.sendEmailWithJsonAttachmentAsync(email.getSubject(),
							descriptionText + 
							"Meeting Date/Time:" + mStringDate + " " + mStringTime + " @ " + locationText
									, csv, email.toJson().toString(),
							new OnEmailSent());

		}

	};
	
	
	
	

	@Override
	public void onStart() {
		super.onStart();
		
		this.sendButton.setOnClickListener(sendListener);

	}



	
	private class OnEmailSent implements SentEmailCallback {

		@Override
		public void onSuccess() {
			Toast.makeText(getActivity(), ("Successfully sent"),
					Toast.LENGTH_LONG).show();

		}

		@Override
		public void onFailure() {
			Toast.makeText(getActivity(), ("Email was not sent"),
					Toast.LENGTH_LONG).show();
		}

	}

}
