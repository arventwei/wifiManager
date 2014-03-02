package com.txmcu.xiaoxin;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View.OnClickListener;

import com.txmcu.WifiManager.WifiHotAdmin;
import com.txmcu.WifiManager.WifiHotManager;
import com.txmcu.WifiManager.WifiHotManager.OpretionsType;
import com.txmcu.WifiManager.WifiHotManager.WifiBroadCastOperations;
import com.txmcu.wifimanagerdemo.MainActivity;

public class XinStateManager 
implements WifiBroadCastOperations{
	
	private Context context;
	private WifiHotManager wifiHotM;
	private static XinStateManager instance = null;
	
	static String TAG = "XinStateManager";
	
	
	public static XinStateManager getInstance(Context context) {

		if (instance == null) {
			instance = new XinStateManager(context);

		}
		return instance;
	}

	private XinStateManager(Context context) {
		this.context = context;
		wifiHotM = WifiHotManager.getInstance(this.context, XinStateManager.this);

		wifiHotM.scanWifiHot();
	}
	
	// wifi 热点扫描回调
	@Override
	public void disPlayWifiScanResult(List<ScanResult> wifiList) {

		Log.i(TAG, " 热点扫描结果 ： = " + wifiList);

	}
	
	// wifi 连接回调
	@Override
	public boolean disPlayWifiConResult(boolean result, WifiInfo wifiInfo) {

		Log.i(TAG, "热点连接回调函数"+String.valueOf(result)+wifiInfo!=null?wifiInfo.toString():"null");

		return false;
	}

	// wifi 热点连接、扫描在Wifi关闭的情况下，回调
	@Override
	public void operationByType(OpretionsType type, String SSID,String pwd) {
		Log.i(TAG, "operationByType！type = " + type);


	}
}
