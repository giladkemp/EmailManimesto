package com.example.emailmanifesto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.example.emailmanifesto.CallBacks.SentEmailCallback;
import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.Question;
import com.example.emailmanifesto.DataModels.QuestionMessageContent;
import com.example.emailmanifesto.Managers.GmailManager;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class QuestionFragment extends Fragment {
	
String subject, to, cc, description, question, responseText;
	
	// references to buttons
	EditText subjectBox= null;
	EditText toBox = null;
	EditText ccBox = null;
	EditText questionBox = null;
	EditText responseBox = null;
	Spinner prioritySpinner = null;
	
	Button sendButton = null;
	ToggleButton respToggle = null;
	
	String priority = "1";
	int intPriority;
	boolean response = false;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.new_question_fragment, container,
				false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		
		subjectBox = (EditText) this.getView().findViewById(R.id.question_subject);
		toBox = (EditText) this.getView().findViewById(R.id.question_to);
		ccBox = (EditText) this.getView().findViewById(R.id.question_cc);
		questionBox = (EditText) this.getView().findViewById(R.id.question_question);
		responseBox = (EditText) this.getView().findViewById(R.id.question_response);
	
		sendButton = (Button) this.getView().findViewById(R.id.question_send);
	
		this.respToggle = (ToggleButton) this.getView().findViewById(
				R.id.question_togglebutton);
		
		Spinner prioritySpinner = (Spinner) this.getView().findViewById(
				R.id.question_priority);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getActivity().getApplicationContext(), R.array.priority_array,
				R.layout.custom_spinner);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
		// Apply the adapter to the spinner
		prioritySpinner.setAdapter(adapter);
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
			question = questionBox.getText().toString();
			responseText = responseBox.getText().toString();
			// type should already be set

			String test = (String) respToggle.getText();
			if (test.trim().equalsIgnoreCase("Yes")) {
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
			

			Question q = new Question(question,Arrays.asList( new String[]{responseText}));
			List<Question> questions = new ArrayList<Question>();
			questions.add(q);
			
			// create infoMessageContent
			QuestionMessageContent info = new QuestionMessageContent(response,questions);
			
			email.setMessageContent(info);
			
			GmailManager m = (GmailManager)((ComposeActivity) QuestionFragment.this.getActivity()).mEmailManager;
			String from = m.getAccount().name;
			email.setFrom(from);
			// TODO: Not yet completed. Not proper way...
			// InboxActivity.this.getEmailManager().sendEmailAsync
			// (subject, body, recipients, callback);
			QuestionFragment.this.getActivity().getIntent();

			// CSV format
			ArrayList<String> allRecepients = new ArrayList<String>();
			allRecepients.addAll(email.getTo());
			allRecepients.addAll(email.getCc());
			allRecepients.addAll(email.getBcc());
			String csv = allRecepients.toString().replace("[", "")
					.replace("]", "").replace(", ", ",");
			
			//Parse to plaintext
			QuestionMessageContent content = info;
			StringBuilder sb = new StringBuilder();
			
			sb.append("The sender was hoping you could answer the following questions:\n");
			
			for(Question qu : content.getQuestions()){
				sb.append(qu.getQuestion()).append("\n");
			}
			
			sb.append("\nThank you in advance!");

			m.sendEmailWithJsonAttachmentAsync(email.getSubject(),
							sb.toString(), csv, email.toJson().toString(),
							new OnEmailSent());
		}

	};
	
	
	@Override
	public void onStart() {
		super.onStart();

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
