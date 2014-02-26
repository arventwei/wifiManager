package com.txmcu.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 热点搜索，创建，关闭
 */
public class WifiHotAdmin {

	public static final String TAG = "WifiApAdmin";

	private WifiManager mWifiManager = null;

	private Context mContext = null;

	private static WifiHotAdmin instance;

	public void closeWifiAp() {
		closeWifiAp(mWifiManager);
	}

	public static WifiHotAdmin newInstance(Context context) {
		if (instance == null) {
			instance = new WifiHotAdmin(context);
		}
		return instance;
	}

	private WifiHotAdmin(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		closeWifiAp(mWifiManager);
	}

	public void startWifiAp(String wifiName) {
		Log.i(TAG, "into startWifiAp（）");
		stratWifiAp(wifiName);
	}

	// 啟動一個Wifi 熱點
	private boolean stratWifiAp(String wifiName) {

		Log.i(TAG, "into startWifiAp（） 启动一个Wifi 热点！");
		Method method1 = null;
		boolean ret = false;
		try {
			method1 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration apConfig = WifiHotConfigAdmin.createWifiWpaInfo(wifiName, Global.PASSWORD);
					// createPassHotWifiConfig(wifiName, Global.PASSWORD);
			ret = (Boolean) method1.invoke(mWifiManager, apConfig, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() IllegalArgumentException e");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() IllegalAccessException e");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() InvocationTargetException e");
		} catch (SecurityException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() SecurityException e");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() NoSuchMethodException e");
		}
		Log.i(TAG, "out startWifiAp（） 启动一个Wifi 热点！");
		return ret;

	}

	// 關閉Wifi熱點
	private boolean closeWifiAp(WifiManager wifiManager) {

		Log.i(TAG, "into closeWifiAp（） 关闭一个Wifi 热点！");
		boolean ret = false;
		if (isWifiApEnabled(wifiManager)) {
			try {
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);
				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
				ret = (Boolean) method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "out closeWifiAp（） 关闭一个Wifi 热点！");
		return ret;
	}

	// 检测Wifi 热点是否可用
	public boolean isWifiApEnabled(WifiManager wifiManager) {
		try {
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// wep密码保护 config.wepKeys[0] = "" + mPasswd + ""; 是关键
	private WifiConfiguration createPassHotWifiConfig(String mSSID, String mPasswd) {

		Log.i(TAG, "out createPassHotWifiConfig（） 新建一个Wifi配置！ SSID =" + mSSID + " password =" + mPasswd);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();

		config.SSID = mSSID;
		config.wepKeys[0] = mPasswd;
		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.wepTxKeyIndex = 0;
		config.priority = 0;

		Log.i(TAG, "out createPassHotWifiConfig（） 启动一个Wifi配置！ config.SSID=" + config.SSID + "password =" + config.wepKeys[0]);
		return config;
	}
}
