package com.example.testandroid;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class TestFloatWindow extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		floatView();

//		 finish();
	}

	private static class MyView extends View {
		Timer mTimer;

		public MyView(Context context) {
			super(context);

			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					postInvalidate();
				}
			}, 100, 100);

			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// postInvalidate();
			//
			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// }
			// }
			// }).start();
		}

		Rect mRect = new Rect(0, 0, 100, 100);
		Random mRandom = new Random();
		Paint mPaint = new Paint();

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			canvas.drawColor(0xff000000);

			mRandom.setSeed(System.currentTimeMillis());
			mRect.offsetTo(mRandom.nextInt(100), mRandom.nextInt(100));

			mPaint.setColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
			canvas.drawRect(mRect, mPaint);
		}
	}

	private WindowManager wm;

	private void floatView() {
		// 获取WindowManager
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams wmParams = ((MyApplication) getApplication()).getWmParams();

		/**
		 * 以下都是WindowManager.LayoutParams的相关属性 具体用途可参考SDK文档
		 */
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		Display display = wm.getDefaultDisplay();
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		Random random = new Random();
		
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = random.nextInt(metrics.widthPixels-200);
		wmParams.y = random.nextInt(metrics.heightPixels-200);

		// 设置悬浮窗口长宽数据
		wmParams.width = 200;
		wmParams.height = 200;

		// 显示myFloatView图像
		wm.addView(new MyView(this), wmParams);
	}

}
