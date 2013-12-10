package com.example.emailmanifesto;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
public class NewComposeActivity extends Activity {

	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_compose);



		final ActionBar actionBar = getActionBar();


		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// show the given tab
				// commit the fragment transaction
				mFragmentManager = getFragmentManager();
				
				FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
				// add to fragment transaction --> the specific fragment we need
				// based on tab selection
				// check the tab's text, add appropiate fragment
				
				
				
				
				
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// hide the given tab


				mFragmentManager = getFragmentManager();

				FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
				// hide to fragment transaction --> the specific fragment we need
				// based on tab selection
				// check the tab's text, hide appropiate fragment???
				
				
//				fragmentTransaction.remove(fragment)

			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener

		actionBar.addTab(
				actionBar.newTab()
				.setText("Info")
				.setTabListener(tabListener));


		actionBar.addTab(
				actionBar.newTab()
				.setText("Question")
				.setTabListener(tabListener));


		actionBar.addTab(
				actionBar.newTab()
				.setText("Meeting")
				.setTabListener(tabListener));



	}


}
