package com.txmcu.WifiManager;

public class Global {

	public static int WIFI_CONNECTING = 0;

	public static int WIFI_CONNECTED = 1;

	public static int WIFI_CONNECT_FAILED = 2;

	public static String SSID = "YRCCONNECTION";

	public static String PASSWORD = "2012110312123";

	public static int WIFICIPHER_NOPASS = 1;

	public static int WIFICIPHER_WEP = 2;

	public static int WIFICIPHER_WPA = 3;

	public static String INT_SERVER_FAIL = "INTSERVER_FAIL";

	public static String INT_SERVER_SUCCESS = "INTSERVER_SUCCESS";

	public static String INT_CLIENT_FAIL = "INTCLIENT_FAIL";

	public static String INT_CLIENT_SUCCESS = "INTCLIENT_SUCCESS";

	public static String CONNECT_SUCESS = "connect_success";

	public static String CONNECT_FAIL = "connect_fail";

	// 数据传输命令
	public static final int IPMSG_SNEDCLIENTDATA = 0x00000050; // 发送单个client信息（socket连接成功后执行）

	public static final int IPMSG_SENDALLCLIENTS = 0x00000051; // 发送全部客户端信息（Server
																// 接收一个client连接后发送当前所有客户端信息）

	public static final int IPMSG_SENDROTARYDATA = 0x00000060; // 发送旋转角度信息

	public static final int IPMSG_SENDROTARYRESULT = 0x00000061; // 发送旋转的结果

	public static final int IPMSG_SENDCHANGECONTROLLER = 0x00000062; // 发送修改控制权

	public static final int IPMSG_REQUESTCHANGECONTROLLER = 0x00000062; // 请求修改控制权

}
