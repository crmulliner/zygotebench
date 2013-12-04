/*
 *   zygotebench - benchmark process creation speed on Android
 *   
 *   (c) Collin Mulliner
 *   http://www.mulliner.org/android/
 */


package org.mulliner.zygotebench;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements Runnable
{
	private int waiting;
	private long data;
	private int iter = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		        
		waiting = 0;
		data = 0;
		
		BenchRec mReceiver = new BenchRec();
		this.registerReceiver(mReceiver, new IntentFilter("org.mulliner.zygotebench.rundata"));
		
		Thread t = new Thread(this);
		t.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			update((Integer)msg.obj);
		}
	};
    
	private void notify(int iteration)
	{
		Message m = new Message();
		m.obj = Integer.valueOf(iteration);
		handler.sendMessage(m);
	}
    
	private void update(int iteration)
	{
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.textView1);
		t.setText("Iteration: " + iteration + "/" + iter);
		if (iteration == iter) {
			t = (TextView) findViewById(R.id.textView2);
			double dd = data;
			double d = iter;
			t.setText("Average Time: " + dd / d + " ms");
		}
	}
    
	public class BenchRec extends BroadcastReceiver
	{    	
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			long start = bundle.getLong("start", 0);
			long started = bundle.getLong("started", 0);
			data = data + (started - start);
			waiting = 0;
		}
	}
    
	public void run()
	{
		for (int i = 0; i < iter; i++) {
			//System.out.println("BenchService start: " + i);
			waiting = 1;
			runbench();
			do {
				try {
					// sleep long to avoid being scheduled
					Thread.sleep(5000);
				} catch (Exception e) {}
			} while (waiting == 1);
			try {
				// sleep, after notification the service still needs to terminate itself
				Thread.sleep(3000);
			} catch (Exception e) {}
			notify(i+1);
		}
		waiting = 2;
		//double dd = data;
		//double d = iter;
		//System.out.println("average time needed to start a new process: " + dd / d + " ms");
	}
        
	public void runbench()
	{
		Intent service = new Intent(this, Bench.class);
		service.putExtra("start", System.currentTimeMillis());
		this.startService(service);
	}
}
