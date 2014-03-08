package com.txmcu.wifimanagerdemo;

import java.util.ArrayList;
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

public class MainActivity extends Activity 
implements OnClickListener{


	
	private static String TAG = "MainActivity";
	
	//private WifiHotManager wifiHotM;
	//private List<ScanResult> wifiList;
	
	private ListView listView;


	private XiaoXinAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//wifiHotM = WifiHotManager.getInstance(MainActivity.this, MainActivity.this);
		
		

		// 热点列表
		listView = (ListView) findViewById(R.id.devicelist);
		((Button)findViewById(R.id.xiaoxin)).setOnClickListener(this);

		
		//client = new udpclient();
		//WifiInfo wi =wifiHotM.getConnectWifiInfo();
		

		
		//ssidText.setText(wi.getSSID());
		//List<String> authInfo = wifiHotM.getAuthMode(wi.getSSID());
	//	authmodeText.setText(authInfo.get(0));
	//	encryp_typeText.setText(authInfo.get(1));
		//authmodeText.setText(wi.getSupplicantState().)
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		
		startActivity(new Intent(this, AddDeviceActivity.class));
	}

	
	
	// 扫描热点广播初始化
	@Override
	protected void onResume() {
		
		//wifiHotM.scanWifiHot();
		refreshWifiList();
		super.onResume();
	}
	
	
	@Override
	public void onClick(View v) {
	 if (v.getId()==R.id.xiaoxin) {
			startActivity(new Intent(this, AddDeviceActivity.class));
			//startActivity(new Intent(this, SettingActivity.class));
			//wifiHotM.connectToHotpot("xiaoxin_AP",  "xiaoxinap");
			//wifiHotM.scanWifiHot();
		}	
		
	}
	public static List<XiaoxinInfo> scannlist = new ArrayList<XiaoxinInfo>();
	

	private void refreshWifiList() {
		//Log.i(TAG, "into 刷新wifi热点列表");
		//ScanResult sr;
		
		//scannlist = results;
		
		//channelText.setText(getChannelBySSID(ssidText.getText().toString()));
		//if (null == adapter) {
		//	Log.i(TAG, "into 刷新wifi热点列表 adapter is null！");
			adapter = new XiaoXinAdapter(scannlist, this);
			listView.setAdapter(adapter);
		//} else {
		//	Log.i(TAG, "into 刷新wifi热点列表 adapter is not null！");
		//	adapter.refreshData(results);
		//}
		//Log.i(TAG, "out 刷新wifi热点列表");
	}
	

}
