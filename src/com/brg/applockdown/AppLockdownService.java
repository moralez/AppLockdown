package com.brg.applockdown;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AppLockdownService extends Service {

	private String tag = AppLockdownService.class.getSimpleName();
	private Binder mBinder = new AppLockdownServiceBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(tag, "Service Successfully Created");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(tag, "Service onStartCommand");
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    public void run() {
		        showNotification();
		        handler.postDelayed(this, 600); 
		    }
		 }, 600); 
		
		return START_STICKY;
	}
	
	public void ping() {
		Log.i(tag, "pong");
	}
	
	public class AppLockdownServiceBinder extends Binder {
		public AppLockdownService getService() {	
			return AppLockdownService.this;
	    }
	}
	
	private void showNotification() {
		Log.i(tag, "beep");
	}
}
