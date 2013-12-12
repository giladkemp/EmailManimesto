package com.example.emailmanifesto;

import java.util.ArrayList;

import java.util.Arrays;

import org.joda.time.DateTime;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;
import com.example.emailmanifesto.Managers.GmailManager;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class InfoFragment extends Fragment {

	String subject, to, cc, description;
	
	// references to buttons
	EditText subjectBox= null;
	EditText toBox = null;
	EditText ccBox = null;
	EditText descriptionBox = null;
	Spinner typeSpinner = null;
	Spinner prioritySpinner = null;
	
	Button sendButton = null;
	ToggleButton respToggle = null;

	String type = "Text";
	
	String priority = "1";
	int intPriority;
	boolean response = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.new_info_fragment, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		// TODO: set up references to buttons and other views here
		subjectBox = (EditText) this.getView().findViewById(R.id.info_subject);
		toBox = (EditText) this.getView().findViewById(R.id.info_to);
		ccBox = (EditText) this.getView().findViewById(R.id.info_cc);
		descriptionBox = (EditText) this.getView().findViewById(R.id.info_description);
		
		descriptionBox.setText("");

		// grab buttons and spinners
		sendButton = (Button) this.getView().findViewById(R.id.sendInfo);
		this.respToggle = (ToggleButton) this.getView().findViewById(
				R.id.toggleInfo);

		// spinners
		// type of info message
		Spinner typeSpinner = (Spinner) this.getView().findViewById(
				R.id.type_spinner);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getActivity().getApplicationContext(), R.array.type_array,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		typeSpinner.setAdapter(adapter);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				type = (String) parent.getItemAtPosition(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
			

		});
		
		Spinner prioritySpinner = (Spinner) this.getView().findViewById(
				R.id.info_priority);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this.getActivity().getApplicationContext(), R.array.priority_array,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
			subject = subjectBox.getText().toString();
			to = toBox.getText().toString();
			cc = ccBox.getText().toString();
			description = descriptionBox.getText().toString();
			// type should already be set

			String test = (String) respToggle.getText();
			if (test.equals("Yes")) {
				response = true;
			} else {
				response = false;
			}

			// create email object
			EmailMessage email = new EmailMessage();
			// TODO: set email subject, to, cc, bcc, sentTime AND PRIORITY
			email.setSentTime(new DateTime());
			email.setSubject(subject);
			email.setTo(new ArrayList<String>(Arrays
					.asList(new String[] { to })));
			email.setCc(new ArrayList<String>(Arrays
					.asList(new String[] { cc })));
			email.setBcc(new ArrayList<String>(Arrays
					.asList(new String[] { "" })));
			email.setPriority( intPriority);

			// create infoMessageContent
			InfoMessageContent info = new InfoMessageContent(response,
					new ArrayList<Object>(
							Arrays.asList(new String[] { description })), type);

			email.setMessageContent(info);
			
			GmailManager m = (GmailManager)((ComposeActivity) InfoFragment.this.getActivity()).mEmailManager;
			String from = m.getAccount().name;
			email.setFrom(from);
			// TODO: Not yet completed. Not proper way...
			// InboxActivity.this.getEmailManager().sendEmailAsync
			// (subject, body, recipients, callback);
			InfoFragment.this.getActivity().getIntent();

			// CSV format
			ArrayList<String> allRecepients = new ArrayList<String>();
			allRecepients.addAll(email.getTo());
			allRecepients.addAll(email.getCc());
			allRecepients.addAll(email.getBcc());
			String csv = allRecepients.toString().replace("[", "")
					.replace("]", "").replace(", ", ",");
			
			

			m.sendEmailWithJsonAttachmentAsync(email.getSubject(),
							description, csv, email.toJson().toString(),
							new OnEmailSent());

		}

	};

	@Override
	public void onStart() {
		super.onStart();

		// set up onClickListener for send button
		sendButton.setOnClickListener(sendListener);

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
