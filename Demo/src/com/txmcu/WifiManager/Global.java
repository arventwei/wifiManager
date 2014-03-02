package com.txmcu.WifiManager;

public class Global {

	public static int WIFI_CONNECTING = 0;

	public static int WIFI_CONNECTED = 1;

	public static int WIFI_CONNECT_FAILED = 2;

	public static String SSID = "WIFI_TEST";

	public static String PASSWORD = "1234567890";

	public static int WIFICIPHER_NOPASS = 1;

	public static int WIFICIPHER_WEP = 2;

	public static int WIFICIPHER_WPA = 3;

	public static String INT_SERVER_FAIL = "INTSERVER_FAIL";

	public static String INT_SERVER_SUCCESS = "INTSERVER_SUCCESS";

	public static String INT_CLIENT_FAIL = "INTCLIENT_FAIL";

	public static String INT_CLIENT_SUCCESS = "INTCLIENT_SUCCESS";

	public static String CONNECT_SUCESS = "connect_success";

	public static String CONNECT_FAIL = "connect_fail";

	// 鏁版嵁浼犺緭鍛戒护
	public static final int IPMSG_SNEDCLIENTDATA = 0x00000050; // 鍙戦�鍗曚釜client淇℃伅锛坰ocket杩炴帴鎴愬姛鍚庢墽琛岋級

	public static final int IPMSG_SENDALLCLIENTS = 0x00000051; // 鍙戦�鍏ㄩ儴瀹㈡埛绔俊鎭紙Server
																// 鎺ユ敹涓�釜client杩炴帴鍚庡彂閫佸綋鍓嶆墍鏈夊鎴风淇℃伅锛�

	public static final int IPMSG_SENDROTARYDATA = 0x00000060; // 鍙戦�鏃嬭浆瑙掑害淇℃伅

	public static final int IPMSG_SENDROTARYRESULT = 0x00000061; // 鍙戦�鏃嬭浆鐨勭粨鏋�

	public static final int IPMSG_SENDCHANGECONTROLLER = 0x00000062; // 鍙戦�淇敼鎺у埗鏉�

	public static final int IPMSG_REQUESTCHANGECONTROLLER = 0x00000062; // 璇锋眰淇敼鎺у埗鏉�
	
	/**
     * 根据频率获得信道
     * @param frequency
     * @return
     */
    public static int getChannel(int frequency)
    {
            int channel = 0;
            switch(frequency){
            case 2412:
                    channel = 1;
                    break;
            case 2417:
                    channel = 2;
                    break;
            case 2422:
                    channel = 3;
                    break;
            case 2427:
                    channel = 4;
                    break;
            case 2432:
                    channel = 5;
                    break;
            case 2437:
                    channel = 6;
                    break;
            case 2442:
                    channel = 7;
                    break;
            case 2447:
                    channel = 8;
                    break;
            case 2452:
                    channel = 9;
                    break;
            case 2457:
                    channel = 10;
                    break;
            case 2462:
                    channel = 11;
                    break;
            case 2467:
                    channel = 12;
                    break;
            case 2472:
                    channel = 13;
                    break;
            case 2484:
                    channel = 14;
                    break;
            }
            return channel;
    }

}
