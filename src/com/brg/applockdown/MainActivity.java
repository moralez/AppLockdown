package com.brg.applockdown;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String tag = MainActivity.class.getSimpleName();
	private Button startButton;
	private Button stopButton;
	private Button pingButton;
	private Boolean appLockdownServiceIsBound = false;
	private AppLockdownService appLockdownService;
	
	private ServiceConnection appLockdownServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			appLockdownService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			appLockdownService = ((AppLockdownService.AppLockdownServiceBinder) service).getService();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startButton = (Button) findViewById(R.id.start_activity_button);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.startAppLockdownService(getApplicationContext());
			}
		});
		
		stopButton = (Button) findViewById(R.id.stop_activity_button);
		stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.stopAppLockdownService(getApplicationContext());
			}
		});
		
		pingButton = (Button) findViewById(R.id.ping_activity_button);
		pingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.pingService();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startAppLockdownService(Context context) {
		Log.i(tag, "Starting App Lockdown Service");
		bindService(new Intent(this, AppLockdownService.class), appLockdownServiceConnection, Context.BIND_AUTO_CREATE);
	    appLockdownServiceIsBound = true;
	}

	public void stopAppLockdownService(Context context) {
		Log.i(tag, "Stopping App Lockdown Service");
		if (appLockdownServiceIsBound) {
			unbindService(appLockdownServiceConnection);
			appLockdownServiceIsBound = false;
		}
	}
	
	public void pingService() {
		if (appLockdownServiceIsBound && appLockdownService != null) {			
			Log.i(tag, "ping");
			appLockdownService.ping();
		} else {
			Log.e(tag, "Error: AppLockdownService Not Bound or Is Null");
			Toast.makeText(getApplicationContext(), "AppLockdown Service Not Found", Toast.LENGTH_LONG).show();
		}
	}
}
