/*
 *   zygotebench - benchmark process creation speed on Android
 *   
 *   (c) Collin Mulliner
 *   http://www.mulliner.org/android/
 */

package org.mulliner.zygotebench;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Bench extends Service 
{
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{		
		long cur = System.currentTimeMillis();
		long start = intent.getLongExtra("start", 0);
		//System.out.println("BenchService delay: " + (cur - start));
		
		Intent i = new Intent();
		i.setAction("org.mulliner.zygotebench.rundata");
		i.putExtra("start", start);
		i.putExtra("started", cur); 
		sendBroadcast(i);
		
		// stop service
		stopSelf(startId);
		// kill process
		int pid =  android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();  
	}
}
