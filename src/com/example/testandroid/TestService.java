package com.example.testandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class TestService extends Service {
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	TestService getService() {
            return TestService.this;
        }
    }
    
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
    final Handler mHandler = new Handler();

    
	@Override
	public void onCreate() {
		super.onCreate();
		
		System.out.println("TestService.onCreate()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("TestService.onBind(" + intent + ')');
		
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("TestService.onUnbind()");
		
		return super.onUnbind(intent);
	}
	
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);

		System.out.println("TestService.onRebind(" + intent + ')');
	}
	
	private void stop(long delay, final int startId) {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("byby!");
				
				if(stopSelfResult(startId))
					System.out.println("stop the hole service!");
				else
					System.out.println("there is any other start!");
			}
		}, delay);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, final int startId) {
		System.out.println("onStartCommand " 
				+ intent 
				+ ", flags=" + flags 
				+ ", startId=" + startId);
		
		if(intent != null) { //stop
			if(intent.getAction().equals("2")) {
				stop(3000, startId);
			} else if (intent.getAction().equals("1")) {
				stop(5000, startId);
			}
		}
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		System.out.println("TestService.onDestroy()");
	}
}
