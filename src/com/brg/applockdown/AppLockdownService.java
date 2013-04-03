package com.brg.applockdown;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
		        handler.postDelayed(this, 2000); 
		    }
		 }, 600); 
		
		return START_STICKY;
	}
	
	public void ping() {
		Log.i(tag, "pong");
		
		// Logen's note -- just trying stuff out, can put this elsewhere.		
		// Print list of installed apps
		final PackageManager packageManager = getPackageManager();
		List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {
			Log.d(tag,"App Name: " + packageInfo.loadLabel(packageManager).toString());
		    Log.d(tag, "Package Name: " + packageInfo.packageName);
		}
	}
	
	public class AppLockdownServiceBinder extends Binder {
		public AppLockdownService getService() {	
			return AppLockdownService.this;
	    }
	}
	
	private void showNotification() {
		Log.i(tag, "beep");
		
		// logen -- hijacking your repeating task for my own devilish purpose
		taskInRecentList("com.android.contacts");
	}
	
	// logen -- simplest test
	private void taskInRecentList(String protectedAppName) {
		ActivityManager activityManager = (ActivityManager)this.getSystemService( ACTIVITY_SERVICE );
		ActivityManager.RecentTaskInfo lastTask = activityManager.getRecentTasks(1, ActivityManager.RECENT_WITH_EXCLUDED).get(0);
		String currentTaskName = lastTask.baseIntent.getComponent().getPackageName(); 
		if (protectedAppName.equals(currentTaskName)) {
			Log.i(tag,"WAS LAST APP: " + currentTaskName);
		} else {
			Log.i(tag,"Nope, last app was: " + currentTaskName);
		}
	}
}
