package com.txmcu.WifiManager;

import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiHotManager {
	public static String TAG = WifiHotManager.class.getName();

	private WifiHotAdmin wifiApadmin;

	private WifiManager mWifimanager;

	private static WifiHotManager instance = null;

	private Context context;

	private WifiBroadCastOperations operations;

	private WifiScanRsultBroadCast wifiScanReceiver;

	private WifiStateBroadCast wifiStateReceiver;

	private WifiConnectBroadCast wifiConnectReceiver;

	public boolean isConnecting;

	private String mSSID;

	public enum OpretionsType {

		CONNECT,

		SCAN;
	}

	public static interface WifiBroadCastOperations {

		/**
		 * @param wifiList 热掉扫描结果
		 */
		public void disPlayWifiScanResult(List<ScanResult> wifiList);

		/**
		 * @param result wifi 连接结果
		 * @param wifiInfo wifi连接信息
		 * @return wifi连接结果
		 */
		public boolean disPlayWifiConResult(boolean result, WifiInfo wifiInfo);

		/**
		 * @param type conntect wifi or scan wifi
		 * @param SSID wifi 连接时指定的SSID
		 */
		public void operationByType(OpretionsType type, String SSID,String pWd);

	}

	public static WifiHotManager getInstance(Context context, WifiBroadCastOperations operations) {

		if (instance == null) {
			instance = new WifiHotManager(context, operations);

		}
		return instance;
	}

	private WifiHotManager(Context context, WifiBroadCastOperations operations) {
		this.context = context;
		this.operations = operations;
		wifiApadmin = WifiHotAdmin.newInstance(context);
		mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	// 检测Wifi是否打开
	public boolean wifiIsOpen() {
		if (mWifimanager == null) {
			mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		return mWifimanager.isWifiEnabled();
	}

	// 扫描Wifi热点
	public void scanWifiHot() {
		Log.i(TAG, "into wifiHotScan()");
		if (!wifiIsOpen()) {
			Log.i(TAG, "out wifiHotScan() wifi is not open!");
			registerWifiStateBroadcast("","");
			wifiStateReceiver.setOpType(OpretionsType.SCAN);
			openWifi();
		} else {
			Log.i(TAG, "out wifiHotScan() wifi is  open!");
			scanNearWifiHots();
		}
		Log.i(TAG, "out wifiHotScan()");
	}

	// 连接热点
	public void connectToHotpot(final String SSID, final String password) {
		if (SSID == null || SSID.equals("")) {
			Log.d(TAG, "WIFI ssid is null or ");
			return;
		}
		if (SSID.equalsIgnoreCase(mSSID) && isConnecting) {
			Log.d(TAG, "same ssid is  connecting!");
			operations.disPlayWifiConResult(false, null);
			return;
		}
//		if (!checkConnectHotIsEnable(SSID, wifiList)) {
//			Log.d(TAG, "ssid is not in the wifiList!");
//			operations.disPlayWifiConResult(false, null);
//			return;
//		}
		if (!wifiIsOpen()) {
			registerWifiStateBroadcast(SSID,password);
			wifiStateReceiver.setOpType(OpretionsType.CONNECT);
			openWifi();
		} else {
			enableNetwork(SSID, password);
		}
	}

	public void setConnectStatu(boolean connecting) {
		this.isConnecting = connecting;
	}

	// 检查连接SSID是否在搜索到的wifi列表中
	public boolean checkConnectHotIsEnable(String wifiName, List<ScanResult> wifiList) {

		for (ScanResult result : wifiList) {
			if (result.SSID.contains(wifiName)) {
				return true;
			}
		}
		return false;
	}

	// 连接热点
	public void enableNetwork(final String SSID, final String password) {
		deleteMoreCon(SSID);
		Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig)");
		new Thread(new Runnable() {
			@Override
			public void run() {
				WifiConfiguration config = WifiHotConfigAdmin.createWifiNoPassInfo(SSID, password);
				isConnecting = connectHotSpot(config);
				registerWifiConnectBroadCast();
				mSSID = SSID;
				if (!isConnecting) {
					operations.disPlayWifiConResult(false, null);
					Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig) isConnecting =" + isConnecting);
					return;
				}
			}
		}).start();
		Log.i(TAG, "out enableNetwork(WifiConfiguration wifiConfig)");
	}

	/* 连接热点 */
	private boolean connectHotSpot(WifiConfiguration wifiConfig) {
		Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig)");
		int wcgID = mWifimanager.addNetwork(wifiConfig);
		Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig) wcID = " + wcgID);
		if (wcgID < 0) {
			Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig) addNetWork fail!");
			operations.disPlayWifiConResult(false, null);
			return false;
		}
		boolean flag = mWifimanager.enableNetwork(wcgID, true);
		Log.i(TAG, "out enableNetwork(WifiConfiguration wifiConfig)");
		return flag;
	}

	// 启动wifi一个Wifi热点
	public void startAWifiHot(String wifiName,String password) {
		Log.i(TAG, "into startAWifiHot(String wifiName) wifiName =" + wifiName);
		if (mWifimanager.isWifiEnabled()) {
			mWifimanager.setWifiEnabled(false);
		}
		if (wifiApadmin != null) {
			wifiApadmin.startWifiAp(wifiName,password);
		}
		Log.i(TAG, "out startAWifiHot(String wifiName)");
	}

	// 关闭Wifi热点
	public void closeAWifiHot() {
		Log.i(TAG, "into closeAWifiHot()");
		if (wifiApadmin != null) {
			wifiApadmin.closeWifiAp();
		}
		Log.i(TAG, "out closeAWifiHot()");
	}

	// 搜索附近Wifi热点
	private void scanNearWifiHots() {
		Log.i(TAG, "into scanNearWifiHots()");
		registerWifiScanBroadCast();
		mWifimanager.startScan();
		Log.i(TAG, "out scanNearWifiHots()");
	}

	public void openWifi() {
		Log.i(TAG, "into OpenWifi()");
		if (mWifimanager == null) {
			mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		if (!mWifimanager.isWifiEnabled()) {
			mWifimanager.setWifiEnabled(true);
		}
		Log.i(TAG, "out OpenWifi()");
	}

	// 注册wifi 状态监听广播
	private void registerWifiStateBroadcast(String SSID,String pwdString) {
		IntentFilter filter = new IntentFilter();
		if (wifiStateReceiver == null) {
			wifiStateReceiver = new WifiStateBroadCast(operations, SSID,pwdString);
		}
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		context.registerReceiver(wifiStateReceiver, filter);
	}

	// 注册wifi 扫描结果监听广播
	private void registerWifiScanBroadCast() {
		IntentFilter filter = new IntentFilter();
		if (wifiScanReceiver == null) {
			wifiScanReceiver = new WifiScanRsultBroadCast(operations);

		}
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(wifiScanReceiver, filter);
	}

	// 注册wifi热点连接广播
	private void registerWifiConnectBroadCast() {
		if (wifiConnectReceiver == null) {
			wifiConnectReceiver = new WifiConnectBroadCast(operations);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		context.registerReceiver(wifiConnectReceiver, filter);
	}

	// 去掉wifi状态广播监听
	public void unRegisterWifiStateBroadCast() {
		if (wifiStateReceiver != null) {
			context.unregisterReceiver(wifiStateReceiver);
			wifiStateReceiver = null;
		}
	}

	// 去掉wifi扫描结果监听
	public void unRegisterWifiScanBroadCast() {
		if (wifiScanReceiver != null) {
			context.unregisterReceiver(wifiScanReceiver);
			wifiScanReceiver = null;
		}
	}

	// 去掉wifi连接广播监听
	public void unRegisterWifiConnectBroadCast() {
		if (wifiConnectReceiver != null) {
			context.unregisterReceiver(wifiConnectReceiver);
			wifiConnectReceiver = null;
		}
	}

	// 关闭重复的热点，避免连接不上
	public void deleteMoreCon(String SSID) {
		Log.i(TAG, "into deleteMoreCon(String SSID) SSID= " + SSID);
		String destStr = "\"" + SSID + "\"";
		Log.i(TAG, "connectConfig  SSID= " + destStr);
		List<WifiConfiguration> existingConfigs = mWifimanager.getConfiguredNetworks();
		if (existingConfigs == null) {
			return;
		}
		for (WifiConfiguration existingConfig : existingConfigs) {
			Log.i(TAG, "existingConfig SSID = " + existingConfig.SSID);
			if (existingConfig.SSID.equalsIgnoreCase(destStr)) {
				Log.i(TAG, "existingConfig contain SSID = " + existingConfig.SSID);
				mWifimanager.disableNetwork(existingConfig.networkId);
				mWifimanager.removeNetwork(existingConfig.networkId);
			}
		}
		mWifimanager.saveConfiguration();
		Log.i(TAG, "out deleteMoreCon(String SSID) SSID= " + SSID);

	}

	// 关闭热点
	public void disableWifiHot() {
		wifiApadmin.closeWifiAp();
	}

	public void disconnectWifi(String SSID) {
		// mWifimanager.disconnect();
	}
}
