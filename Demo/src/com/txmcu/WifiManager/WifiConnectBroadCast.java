package com.txmcu.WifiManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.txmcu.WifiManager.WifiHotManager.WifiBroadCastOperations;

public class WifiConnectBroadCast extends BroadcastReceiver {

	private WifiBroadCastOperations operations;

	private WifiManager wifiManager;
	private String sSID;

	public WifiConnectBroadCast(WifiBroadCastOperations operations,String SSID) {

		this.operations = operations;
		this.sSID = SSID;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
			Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (null != parcelableExtra) {
				NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
				wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String SSID = wifiInfo.getSSID();
				
				switch (networkInfo.getState()) {
				case CONNECTED:
					Log.e("APActivity", "CONNECTED");
					//if (SSID.contains(sSID)) {
						operations.disPlayWifiConResult(true, wifiInfo);
					//} else {
					//	operations.disPlayWifiConResult(false, wifiInfo);
					//}
					break;
				case CONNECTING:
					Log.e("APActivity", "CONNECTING");
					operations.disPlayWifiConResult(false, wifiInfo);
					break;
				case DISCONNECTED:
					Log.e("APActivity", "DISCONNECTED");
					operations.disPlayWifiConResult(false, wifiInfo);
					break;
				case DISCONNECTING:
					Log.e("APActivity", "DISCONNECTING");
					operations.disPlayWifiConResult(false, wifiInfo);
					break;
				case SUSPENDED:
					Log.e("APActivity", "SUSPENDED");
					operations.disPlayWifiConResult(false, wifiInfo);
					break;
				case UNKNOWN:
					Log.e("APActivity", "UNKNOWN");
					operations.disPlayWifiConResult(false, wifiInfo);
					break;
				default:
					break;
				}
			}
		}
	}
}
