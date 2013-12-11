package com.example.emailmanifesto;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InfoFragment extends Fragment{
	
	
	
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
		
		
	}
    
    @Override
    public void onStart() {
        super.onStart();
        
    }

}
