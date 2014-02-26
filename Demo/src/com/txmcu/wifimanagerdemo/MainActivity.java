package com.txmcu.wifimanagerdemo;

import java.util.List;

import android.app.Activity;
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

public class MainActivity extends Activity 
implements OnClickListener,WifiBroadCastOperations{

	
	private static String TAG = "MainActivity";
	
	private WifiHotManager wifiHotM;
	private List<ScanResult> wifiList;
	
	private ListView listView;
	
	EditText ssidText;
	EditText pwdText;
	
	TextView apInfoTextView;

	private WifiHotAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		wifiHotM = WifiHotManager.getInstance(MainActivity.this, MainActivity.this);
		
		
		ssidText = (EditText)findViewById(R.id.wifi_ssid);
		pwdText = (EditText)findViewById(R.id.wifi_pwd);
		apInfoTextView = (TextView)findViewById(R.id.ApInfo);
		// 热点列表
		listView = (ListView) findViewById(R.id.wifilist);
		((Button)findViewById(R.id.createAp)).setOnClickListener(this);
		((Button)findViewById(R.id.stopAp)).setOnClickListener(this);
		((Button)findViewById(R.id.connect)).setOnClickListener(this);
		((Button)findViewById(R.id.disconnect)).setOnClickListener(this);
		((Button)findViewById(R.id.scan)).setOnClickListener(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ScanResult result = wifiList.get(position);
				ssidText.setText(result.SSID);//= result.SSID;
				//statu.setText("连接中...");
				//Log.i(TAG, "into  onItemClick() SSID= " + result.SSID);
				//wifiHotM.connectToHotpot(result.SSID, wifiList, Global.PASSWORD);
				Log.i(TAG, "out  onItemClick() SSID= " + result.SSID);
			}
		});
	}

	
	
	// 扫描热点广播初始化
	@Override
	protected void onResume() {
		
		//wifiHotM.scanWifiHot();
		super.onResume();
	}
		
	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.createAp) {
			wifiHotM.startAWifiHot(Global.SSID,Global.PASSWORD);
			apInfoTextView.setText("SSID:"+Global.SSID+" PWd:"+Global.PASSWORD);
			
		}
		else if (v.getId()==R.id.stopAp) {
			wifiHotM.disableWifiHot();
			apInfoTextView.setText("SSID:" +" PWd:");
		}
		else if (v.getId()==R.id.connect) {
			
		}
		else if (v.getId()==R.id.disconnect) {
			
		}
		else if (v.getId()==R.id.scan) {
			wifiHotM.scanWifiHot();
		}
	}
	
	private void refreshWifiList(List<ScanResult> results) {
		Log.i(TAG, "into 刷新wifi热点列表");
		if (null == adapter) {
			Log.i(TAG, "into 刷新wifi热点列表 adapter is null！");
			adapter = new WifiHotAdapter(results, this);
			listView.setAdapter(adapter);
		} else {
			Log.i(TAG, "into 刷新wifi热点列表 adapter is not null！");
			adapter.refreshData(results);
		}
		Log.i(TAG, "out 刷新wifi热点列表");
	}
	
	// wifi 热点扫描回调
	@Override
	public void disPlayWifiScanResult(List<ScanResult> wifiList) {

		Log.i(TAG, "into 扫描结果回调函数");
		this.wifiList = wifiList;
		wifiHotM.unRegisterWifiScanBroadCast();
		refreshWifiList(wifiList);
		Log.i(TAG, "out 热点扫描结果 ： = " + wifiList);

	}

	// wifi 连接回调
	@Override
	public boolean disPlayWifiConResult(boolean result, WifiInfo wifiInfo) {

		Log.i(TAG, "into 热点连接回调函数");
		String ip = "";
		wifiHotM.setConnectStatu(false);
		wifiHotM.unRegisterWifiStateBroadCast();
		wifiHotM.unRegisterWifiConnectBroadCast();
		//initClient(ip);
		Log.i(TAG, "out 热点链接回调函数");
		return false;
	}

	// wifi 热点连接、扫描在Wifi关闭的情况下，回调
	@Override
	public void operationByType(OpretionsType type, String SSID) {
		Log.i(TAG, "into operationByType！type = " + type);
		if (type == OpretionsType.CONNECT) {
			wifiHotM.connectToHotpot(SSID, wifiList, Global.PASSWORD);
		} else if (type == OpretionsType.SCAN) {
			wifiHotM.scanWifiHot();
		}
		Log.i(TAG, "out operationByType！");

	}
}
