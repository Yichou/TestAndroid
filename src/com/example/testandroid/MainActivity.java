package com.example.testandroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import dalvik.system.DexClassLoader;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	public static final File LCK_FILE = new File("/mnt/sdcard/freesky/", "test.lck");
	
	
	private void testDex() {
		DexClassLoader classLoader = new DexClassLoader("/mnt/sdcard/Download/dyd.apk", getDir("dex", 0).getAbsolutePath(), null, getClassLoader());
		try {
			Class<?> clazz = classLoader.loadClass("com.mrpej.ads.Entry");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		DexClassLoader classLoader2 = new DexClassLoader("/mnt/sdcard/Download/dyd.apk", getDir("dex", 0).getAbsolutePath(), null, getClassLoader());
		try {
			Class<?> clazz = classLoader.loadClass("com.mrpej.ads.Entry");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void testFileLock() {
		FileLock lock = tryFileLock(LCK_FILE);
		if(lock != null)
			Log.d("", "lock Suc");
		else
			Log.e("", "lock Fail");
		
		startActivity(new Intent(this, SecondActivity.class));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				FileLock lock = tryFileLock(LCK_FILE);
				if(lock != null)
					Log.d("", "lock Suc");
				else
					Log.e("", "lock Fail");
			}
		}).start();
	}
	
	
	private void testMainLooper(final Context context) {
		new Thread(new Runnable() {
			Handler handler;
			ProgressDialog dialog = new ProgressDialog(context);
			int prog = 0;
			
			
			@Override
			public void run() {
				handler = new Handler(Looper.getMainLooper());
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.show();
				
				while (prog < 100) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							dialog.setProgress(prog);
						}
					});
					
					prog += 10;
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1: {
			testMainLooper(this);
			break;
		}
		
		case R.id.button1 +1: {
			Intent intent = new Intent(MainActivity.this, TestReceiver.class);
			intent.setAction("test");
			
			Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wap.baidu.com"));
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("intent", intent2);

			Notification n = new Notification(android.R.drawable.ic_menu_share,
					null, System.currentTimeMillis());
			intent.putExtra("notify", n);

			PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			n.setLatestEventInfo(this, "早上好！", "今天是个晴朗的天气！", contentIntent);
			n.flags |= Notification.FLAG_NO_CLEAR;
//			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			

			if(n.contentView != null ){
				n.contentView.setImageViewResource(android.R.id.icon, R.drawable.itunes);
//				n.contentView.setImageViewUri(android.R.id.icon, Uri.parse("http://plugins.mrpcdn.com/down/0f995194-b36c-4dfd-9a78-f5070136b69c/100x100"));
			}

			NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNM.notify(1001, n);
			
//			Notification.Builder builder;
//			builder.setSmallIcon(arg0)
			
//			LayoutInflater inflater = LayoutInflater.from(this);
//			View view = inflater.inflate(R.layout.notify2, null);
//			rv.apply(this, (ViewGroup) view);
			
//			android.R.id.chr
			
			break;
		}
		}
	}

	
	/**
	 * 占用某个文件锁
	 * 
	 * @param file
	 * @return
	 */
	public static FileLock tryFileLock(File file) {
		try {
//			RandomAccessFile raf = new RandomAccessFile(file, "rw");
//			FileLock fl = raf.getChannel().tryLock();
//			raf.close(); //调用 close 就无效了擦
			
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			FileLock fl = fos.getChannel().tryLock();
			
//			file.createNewFile();
//			FileInputStream fis = new FileInputStream(file);
//			FileLock fl = fis.getChannel().tryLock();
			
			if(fl.isValid()){
				return fl;
			}else {
				
			}
		}catch (Exception e) {
		}
		
		return null;
	}
	
	public static void freeFileLock(FileLock fl, File file) {
		if(fl == null || !fl.isValid()) return;
		
		try {
			fl.release();
		} catch (IOException e) {
		}
		
		file.delete();
	}
}
