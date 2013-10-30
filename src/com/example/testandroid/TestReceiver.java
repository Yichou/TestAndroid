package com.example.testandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TestReceiver extends BroadcastReceiver {
	// static {
	// System.out.println("static init TestReceiver");
	//
	// ClassLoader loader = TestReceiver.class.getClassLoader();
	// try {
	// Class<?> clazz = loader.loadClass("android.app.ActivityThread");
	// Field field = clazz.getDeclaredField("mSystemContext");
	// field.setAccessible(true);
	// Context context = (Context) field.get(null);
	//
	// IntentFilter filter = new
	// IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	// filter.setPriority(Integer.MAX_VALUE);
	// context.registerReceiver(new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// Toast.makeText(context, "收到短信", Toast.LENGTH_SHORT).show();
	// }
	// }, filter);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	// public void setLatestEventInfo(Context context, CharSequence
	// contentTitle, CharSequence contentText, PendingIntent contentIntent) {
	// RemoteViews contentView = new RemoteViews(context.getPackageName(),
	// android.R.layout.notification_template_base);
	// if (this.icon != 0) {
	// contentView.setImageViewResource(android.R.id.icon, this.icon);
	// }
	// if (priority < PRIORITY_LOW) {
	// contentView.setInt(android.R.id.icon,
	// "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
	// contentView.setInt(android.R.id.status_bar_latest_event_content,
	// "setBackgroundResource", R.drawable.notification_bg_low);
	// }
	// if (contentTitle != null) {
	// contentView.setTextViewText(android.R.id.title, contentTitle);
	// }
	// if (contentText != null) {
	// contentView.setTextViewText(android.R.id.text1, contentText);
	// }
	// if (this.when != 0) {
	// contentView.setViewVisibility(android.R.id.time, View.VISIBLE);
	// contentView.setLong(android.R.id.time, "setTime", when);
	// }
	// if (this.number != 0) {
	// NumberFormat f = NumberFormat.getIntegerInstance();
	// contentView.setTextViewText(android.R.id.info, f.format(this.number));
	// }
	//
	// this.contentView = contentView;
	// this.contentIntent = contentIntent;
	// }

	public void onReceive1(final Context context, Intent intent) {
		System.out.println("onReceive " + intent.getAction());

		Toast.makeText(context, "onReceive " + intent.getAction(), Toast.LENGTH_SHORT).show();

		Intent intent2 = (Intent) intent.getParcelableExtra("intent");
		// intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// context.startActivity(intent2);

		final Notification notification = intent.getParcelableExtra("notify");
		final Notification n2 = new Notification();

		RemoteViews views = notification.contentView;
		if (views != null) {
		}
		n2.contentIntent = notification.contentIntent;
		n2.contentView = notification.contentView;

		NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNM.notify(1002, notification);

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// }
		// }).start();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "I have receive your happy!\n " + intent.getAction(), Toast.LENGTH_SHORT).show();
	}
}
