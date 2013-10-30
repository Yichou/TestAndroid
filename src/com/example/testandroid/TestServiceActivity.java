package com.example.testandroid;

import android.app.ListActivity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yichou.common.utils.CrashUtils;
import com.yichou.common.utils.TimeUtils;

public class TestServiceActivity extends ListActivity {

	static final String[] ITEMS = { 
		"bind 1", 
		"bind 2", 
		"unbind 2", 
		"start 1 and stopSelf after 5s", 
		"start 2 and stopSelf after 3s", 
		"bind mutil process",
		"unbind mutil process",
		"stop by context",
		"exit", 
		"crash" };

	
	final ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("final service disconnect " + name);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("final service connect " + name + service);
		}
	};
	
	static final int MSG_HELLO = 0x1001;
	
	/**
	 * 本地消息处理器
	 */
	final Messenger mLocalMessenger = new Messenger(new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == MSG_HELLO) {
				Bundle data = (Bundle) msg.obj;
				
				System.out.println(TimeUtils.getTimeNow() + ": " 
						+ "how are you, i got your replay! \n"
						+ data);
				
				return true;
			}
			
			return false;
		}
	}));
	
	/**
	 * 远程消息处理器，在 connected 后创建
	 */
	Messenger mRemoteMessenger;
	
	final ServiceConnection mutilConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("mutil process service disconnect " + name);
			
			mRemoteMessenger = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("mutil process service connect " + name + service);
			mRemoteMessenger = new Messenger(service);
			
			try {
				Message msg = Message.obtain(null, MSG_HELLO);
				
				Bundle bundle = new Bundle();
				bundle.putString("msg", "hello, nice to meet you!");
				
				msg.replyTo = mLocalMessenger;
				msg.obj = bundle;
				
				mRemoteMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			bindService(new Intent(this, TestService.class), new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {
					System.out.println("service1 disconnect " + name);
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					System.out.println("service1 connect " + name + service);
				}
			}, BIND_AUTO_CREATE);
			break;

		case 1:
			bindService(new Intent(this, TestService.class), conn, BIND_AUTO_CREATE);
			break;

		case 2:
			unbindService(conn);
			break;

		case 3:
			startService(new Intent(this, TestService.class).setAction("1"));
			break;

		case 4:
			startService(new Intent(this, TestService.class).setAction("2"));
			break;
			
		case 5:
			bindService(new Intent(this, AnotherProcessService.class), 
					mutilConn, 
					Service.BIND_AUTO_CREATE);
			break;

		case 6:
			unbindService(mutilConn);
			break;
			
		case 7:
			stopService(new Intent(this, TestService.class));
			break;
			
		case 8:
			System.exit(0);
			
			Process.killProcess(Process.myPid());
			break;
			
		case 9:
			CrashUtils.enableCrashHandle(this);
			throw new RuntimeException("you make me crash!");

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ITEMS));
	}
}
