package com.example.emailmanifesto;

import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;

import com.example.emailmanifesto.DataModels.EmailMessage;
import com.example.emailmanifesto.DataModels.InfoMessageContent;

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
import android.widget.ToggleButton;

public class InfoFragment extends Fragment{
	
	// references to buttons
	EditText infoBox = null;
	Spinner typeSpinner = null;
	Button sendButton = null;
	ToggleButton respToggle = null;
	
	
	
	
	String type = "Text";
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
    	infoBox = (EditText)this.getView().findViewById(R.id.content_value);

    	infoBox.setText("");

		
    	// grab buttons and spinners
    	sendButton = (Button)this.getView().findViewById(R.id.sendInfo);
    	this.respToggle = (ToggleButton)this.getView().findViewById(R.id.toggleInfo);
    	
    	// spinners
    	// type of info message
    	Spinner typeSpinner = (Spinner)this.getView().findViewById(R.id.spinner);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this.getActivity().getApplicationContext(),
    	        R.array.type_array, android.R.layout.simple_spinner_item);
    	
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
    	
    	
		
	}
    
    
    OnClickListener sendListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// grab all values
//			typeSpinner.
			
			String contentText = infoBox.getText().toString();
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
			email.setSubject("Test Subject 1");
			email.setTo(new ArrayList<String>(Arrays.asList(new String[]{"scapegoat15@gmail.com"})));
			email.setCc(new ArrayList<String>(Arrays.asList(new String[]{""})));
			email.setBcc(new ArrayList<String>(Arrays.asList(new String[]{""})));
			email.setPriority(3);

			// create infoMessageContent
			InfoMessageContent info = new InfoMessageContent(response, 
					new ArrayList<Object>(Arrays.asList(new String[]{contentText})), 
					type);
			
			email.setMessageContent(info);
			
			
			// TODO: Not yet completed. Not proper way...
//			InboxActivity.this.getEmailManager().sendEmailAsync
//			(subject, body, recipients, callback);
			
		}
    	
    	
    	
    };
    
    
    
    @Override
    public void onStart() {
        super.onStart();
        
        
        // set up onClickListener for send button
        sendButton.setOnClickListener(sendListener);
        
        
        
        
    }
    
    
    
    

}
