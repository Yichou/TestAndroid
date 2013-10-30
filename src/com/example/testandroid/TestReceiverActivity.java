package com.example.testandroid;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestReceiverActivity extends ListActivity {
	static final String[] ITEMS = {
		"显式启动",
		"隐式启动",
		"动态注册"
	};
	
	
	static final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("TestReceiverActivity.InnerReceiver.onReceive()");
		}
		
	}
	
	final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			System.err.println("3receive " + intent.getAction() 
					+ "\n  data " + intent.getData().getSchemeSpecificPart());				
		}
	};
	
	private void regReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addDataScheme("package");
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
		
		getApplicationContext().registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				System.err.println("2receive " + intent.getAction() 
						+ "\n  data " + intent.getData().getSchemeSpecificPart());				
			}
		}, filter);
		
		this.registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, 
				ITEMS));
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			sendBroadcast(new Intent(this, TestReceiver.class).setAction("I wan't you!"));
			break;
			
		case 1:
			sendBroadcast(new Intent("com.example.testandroid.myaction"));
			break;
			
		case 2: 
			regReceiver();
			break;

		default:
			break;
		}
	}

}
