package com.example.testandroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LauncherActivity extends ListActivity {
	static final String[] ITEMS = {
		"test service",
		"test receiver"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, 
				ITEMS));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			startActivity(new Intent(this, TestServiceActivity.class));
			break;

		case 1:
			startActivity(new Intent(this, TestReceiverActivity.class));
			break;

		default:
			break;
		}
	}

}
