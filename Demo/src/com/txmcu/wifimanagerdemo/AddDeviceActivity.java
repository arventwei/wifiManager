package com.txmcu.wifimanagerdemo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.txmcu.WifiManager.Global;
import com.txmcu.WifiManager.WifiHotManager;
import com.txmcu.WifiManager.WifiHotManager.OpretionsType;
import com.txmcu.WifiManager.WifiHotManager.WifiBroadCastOperations;
import com.txmcu.xiaoxin.Udpclient;

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
