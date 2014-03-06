package com.txmcu.xiaoxin;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View.OnClickListener;

import com.txmcu.WifiManager.Global;
import com.txmcu.WifiManager.WifiHotAdmin;
import com.txmcu.WifiManager.WifiHotManager;
import com.txmcu.WifiManager.WifiHotManager.OpretionsType;
import com.txmcu.WifiManager.WifiHotManager.WifiBroadCastOperations;
import com.txmcu.wifimanagerdemo.MainActivity;

public class XinStateManager 
implements WifiBroadCastOperations{
	
	private Context context;
	private XinOperations operations;
	private WifiHotManager wifiHotM;
	private static XinStateManager instance = null;
	
	static String TAG = "XinStateManager";
	
	public enum ConfigType {
		Succeed,
		Failed_Connect_XiaoXin,
		Failed_TimeOut,
		Failed_XiaoXinConfig
	}
	public static int TimeOutSecond = 120;
	
	public static interface XinOperations {

		/**
		 * @param init callback ,close wait dialog
		 */
		public void initResult(boolean result);

		/**
		 * @param invoke callback
		 */
		public void configResult(ConfigType type );
	}
	public static void destroy() {
		instance = null;
	}
	public static XinStateManager getInstance(Context context,XinOperations operations) {

		if (instance == null) {
			instance = new XinStateManager(context,operations);

		}
		return instance;
	}

	private XinStateManager(Context context,XinOperations operations) {
		this.context = context;
		this.operations = operations;
		wifiHotM = WifiHotManager.getInstance(this.context, XinStateManager.this);

		wifiHotM.scanWifiHot();
	}
	
	public void Init()
	{
		wifiHotM.scanWifiHot();
		//backupCurrentWifiState();
	}
	public void Config(String SSID,String Pwd)
	{
		wifiHotM.connectToHotpot(SSID, Pwd);
	}
	public void Destroy()
	{
		restoreCurrentWifiState();
		wifiHotM.unRegisterWifiConnectBroadCast();
		wifiHotM.unRegisterWifiScanBroadCast();
		wifiHotM.unRegisterWifiStateBroadCast();
		WifiHotManager.destroy();
		XinStateManager.destroy();
		
	}
	int wifibackupNetId;
	String wifibackupSSID;
	String wifibackupChannel;
	private void backupCurrentWifiState(WifiInfo info,List<ScanResult> scannlist ) {
		wifibackupNetId=0;
		wifibackupSSID="";
		if (info!=null) {
			wifibackupNetId = info.getNetworkId();
			wifibackupSSID = info.getSSID();
			
			
			wifibackupChannel = "6";
			for (ScanResult sr : scannlist) 
			{
				if (sr.SSID.equalsIgnoreCase(wifibackupSSID))
				{
					int  channel =  Global.getChannel(sr.frequency);
					wifibackupChannel= String.valueOf(channel);
					break;
				}
			}
			
		}
	}
	private void restoreCurrentWifiState() {
		wifiHotM.enableNetWorkById(wifibackupNetId);
	}
	// wifi 热点扫描回调
	@Override
	public void disPlayWifiScanResult(List<ScanResult> wifiList) {

		wifiHotM.unRegisterWifiScanBroadCast();
		Log.i(TAG, " 热点扫描结果 ： = " + wifiList);
		backupCurrentWifiState(wifiHotM.getConnectWifiInfo(),wifiList);
		if (wifibackupSSID.length()==0) {
			operations.initResult(false);
		}
		else {
			operations.initResult(true);
		}
		
		
		
		//wifiHotM.enableNetwork(SSID, password)
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
