package com.example.testandroid;

import java.nio.channels.FileLock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		FileLock lock = MainActivity.tryFileLock(MainActivity.LCK_FILE);
		if(lock != null)
			Log.d("", "lock Suc");
		else
			Log.e("", "lock Fail");
		
//		throw new RuntimeException();
	}
}
