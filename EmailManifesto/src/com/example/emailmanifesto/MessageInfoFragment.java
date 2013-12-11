package com.example.emailmanifesto;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MessageInfoFragment extends Fragment{
	
	// references to buttons
	TextView text = null;
	
	
	
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
		

    	
    	return inflater.inflate(R.layout.info_message, container, false);
		
		
	}
    
    @Override
    public void onActivityCreated(Bundle savedState) {
    	super.onActivityCreated(savedState);

    	// TODO: set up references to buttons and other views here
    	text = (EditText)this.getView().findViewById(R.id.content_value);

    	text.setText("");
		
	}
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    
    
    

}
