package com.txmcu.wifimanagerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddDeviceActivity extends Activity 
implements OnClickListener{

	
	private static String TAG = "AddDeviceActivity";
	
	//private WifiHotManager wifiHotM;
	//private List<ScanResult> wifiList;
	
	

	//private WifiHotAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_adddevice);
		
		//wifiHotM = WifiHotManager.getInstance(MainActivity.this, MainActivity.this);
		
		
		
		// 热点列表
		
		((Button)findViewById(R.id.btnadddevice)).setOnClickListener(this);
		
	}

	
	
	
	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.btnadddevice) {
			startActivity(new Intent(this, SettingActivity.class));
	//		wifiHotM.startAWifiHot(Global.SSID,Global.PASSWORD);
			//apInfoTextView.setText("SSID:"+Global.SSID+" PWd:"+Global.PASSWORD);
			
		}
		
	}
	
}
