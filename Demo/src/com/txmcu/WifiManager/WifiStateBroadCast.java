package com.txmcu.WifiManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.txmcu.WifiManager.WifiHotManager.OpretionsType;
import com.txmcu.WifiManager.WifiHotManager.WifiBroadCastOperations;

public class WifiStateBroadCast extends BroadcastReceiver {

	private WifiBroadCastOperations operations;

	private OpretionsType type;

	private String SSID;

	public WifiStateBroadCast(WifiBroadCastOperations operations, String SSID) {

		this.operations = operations;
		this.SSID = SSID;
	}

	public void setOpType(OpretionsType type) {
		this.type = type;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
			Log.i("WIFI状态", "wifiState" + wifiState);
			switch (wifiState) {
			case WifiManager.WIFI_STATE_DISABLED:
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				if (type != null) {
					operations.operationByType(type, SSID);
				}
				break;
			}
		}

	}

}
