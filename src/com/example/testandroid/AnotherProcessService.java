package com.example.testandroid;

import java.util.Timer;
import java.util.TimerTask;

import com.yichou.common.utils.TimeUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler.Callback;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

public class AnotherProcessService extends Service {

	/**
	 * 本地消息处理器
	 */
	final Messenger mLocalMessenger = new Messenger(new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Bundle data = (Bundle) msg.obj;
			
			System.out.println(TimeUtils.getTimeNow() + ": " 
					+ "how are you, i got your message! \n"
					+ data);
			
			if(msg.replyTo != null) {
				Message relay = Message.obtain(null, msg.what);
				
				Bundle bundle = new Bundle();
				bundle.putString("msg", "hello, nice to meet you to!");
				
				relay.obj = bundle;
				
				try {
					msg.replyTo.send(relay);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			return true;
		}
	}));

	final Handler mHandler = new Handler();
	
	final Timer mTimer = new Timer();
	
	
	public void onCreate() {
		Log.d("mutil", "onCreate");
		
		
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("I am alive!");
			}
		}, 100, 3000);

		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("mutil", "onBind");
		
		return mLocalMessenger.getBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("mutil", "onUnBind");

		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		Log.d("mutil", "onDestroy");
		
		Process.killProcess(Process.myPid());

		super.onDestroy();
	}
}
