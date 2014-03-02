package com.txmcu.WifiManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
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
		 * @param wifiList 鐑帀鎵弿缁撴灉
		 */
		public void disPlayWifiScanResult(List<ScanResult> wifiList);

		/**
		 * @param result wifi 杩炴帴缁撴灉
		 * @param wifiInfo wifi杩炴帴淇℃伅
		 * @return wifi杩炴帴缁撴灉
		 */
		public boolean disPlayWifiConResult(boolean result, WifiInfo wifiInfo);

		/**
		 * @param type conntect wifi or scan wifi
		 * @param SSID wifi 杩炴帴鏃舵寚瀹氱殑SSID
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
	public List<String> getAuthMode(String SSID)
	{
		SSID="\""+SSID+"\"";
		List<String> ret = new ArrayList<String>();
		ret.add("OPEN");
		ret.add("None");
		if (mWifimanager == null) {
			mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		List<WifiConfiguration> existingConfigs = mWifimanager.getConfiguredNetworks();
		if (existingConfigs == null) {
			return ret;
		}
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equalsIgnoreCase(SSID)) {
				
				//capabilities
				//existingConfig.
			//	existingConfig.allowedAuthAlgorithms.get(index)
				if (existingConfig.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.OPEN)
					&&existingConfig.allowedProtocols.get(WifiConfiguration.Protocol.RSN)) {
					ret.set(0, "WPA2PSK");
				}
				else if (existingConfig.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.OPEN)
						&&existingConfig.allowedProtocols.get(WifiConfiguration.Protocol.WPA)) {
						ret.set(0, "WPAPSK");
					}
				else if (existingConfig.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.SHARED)
					) {
						ret.set(0, "WEP");
					}
				if (existingConfig.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.CCMP)
						) {
					ret.set(1, "AES");
				}
				else if (existingConfig.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.TKIP)
						) {
					ret.set(1, "TKIP");
				}
				else if (existingConfig.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP104)
						||existingConfig.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP40)) {
							ret.set(1, "WEP");
						}
		
			}
		}
		return ret;
	}
	public WifiInfo getConnectWifiInfo()
	{
		if (mWifimanager == null) {
			mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		WifiInfo wifiInfo = mWifimanager.getConnectionInfo();
		return wifiInfo;
	}
	// 妫�祴Wifi鏄惁鎵撳紑
	public boolean wifiIsOpen() {
		if (mWifimanager == null) {
			mWifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		return mWifimanager.isWifiEnabled();
	}

	// 鎵弿Wifi鐑偣
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

	// 杩炴帴鐑偣
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

	// 妫�煡杩炴帴SSID鏄惁鍦ㄦ悳绱㈠埌鐨剋ifi鍒楄〃涓�
	public boolean checkConnectHotIsEnable(String wifiName, List<ScanResult> wifiList) {

		for (ScanResult result : wifiList) {
			if (result.SSID.contains(wifiName)) {
				return true;
			}
		}
		return false;
	}

	// 杩炴帴鐑偣
	public void enableNetwork(final String SSID, final String password) {
		deleteMoreCon(SSID);
		Log.i(TAG, "into enableNetwork(WifiConfiguration wifiConfig)");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String newSSID = "\"" + SSID + "\"";
				String newpassword = "\"" + password + "\"";
				WifiConfiguration config = WifiHotConfigAdmin.createWifiWpaInfo(newSSID, newpassword);
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

	/* 杩炴帴鐑偣 */
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

	// 鍚姩wifi涓�釜Wifi鐑偣
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

	// 鍏抽棴Wifi鐑偣
	public void closeAWifiHot() {
		Log.i(TAG, "into closeAWifiHot()");
		if (wifiApadmin != null) {
			wifiApadmin.closeWifiAp();
		}
		Log.i(TAG, "out closeAWifiHot()");
	}

	// 鎼滅储闄勮繎Wifi鐑偣
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

	// 娉ㄥ唽wifi 鐘舵�鐩戝惉骞挎挱
	private void registerWifiStateBroadcast(String SSID,String pwdString) {
		IntentFilter filter = new IntentFilter();
		if (wifiStateReceiver == null) {
			wifiStateReceiver = new WifiStateBroadCast(operations, SSID,pwdString);
		}
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		context.registerReceiver(wifiStateReceiver, filter);
	}

	// 娉ㄥ唽wifi 鎵弿缁撴灉鐩戝惉骞挎挱
	private void registerWifiScanBroadCast() {
		IntentFilter filter = new IntentFilter();
		if (wifiScanReceiver == null) {
			wifiScanReceiver = new WifiScanRsultBroadCast(operations);

		}
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(wifiScanReceiver, filter);
	}

	// 娉ㄥ唽wifi鐑偣杩炴帴骞挎挱
	private void registerWifiConnectBroadCast() {
		if (wifiConnectReceiver == null) {
			wifiConnectReceiver = new WifiConnectBroadCast(operations);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		context.registerReceiver(wifiConnectReceiver, filter);
	}

	// 鍘绘帀wifi鐘舵�骞挎挱鐩戝惉
	public void unRegisterWifiStateBroadCast() {
		if (wifiStateReceiver != null) {
			context.unregisterReceiver(wifiStateReceiver);
			wifiStateReceiver = null;
		}
	}

	// 鍘绘帀wifi鎵弿缁撴灉鐩戝惉
	public void unRegisterWifiScanBroadCast() {
		if (wifiScanReceiver != null) {
			context.unregisterReceiver(wifiScanReceiver);
			wifiScanReceiver = null;
		}
	}

	// 鍘绘帀wifi杩炴帴骞挎挱鐩戝惉
	public void unRegisterWifiConnectBroadCast() {
		if (wifiConnectReceiver != null) {
			context.unregisterReceiver(wifiConnectReceiver);
			wifiConnectReceiver = null;
		}
	}

	// 鍏抽棴閲嶅鐨勭儹鐐癸紝閬垮厤杩炴帴涓嶄笂
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

	// 鍏抽棴鐑偣
	public void disableWifiHot() {
		wifiApadmin.closeWifiAp();
	}

	public void disconnectWifi(String SSID) {
		 mWifimanager.disconnect();
	}
}
