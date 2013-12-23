package com.east.customermanager.net;

/**
 * 自定义的一些错误码
 */
public class HttpFailCode {

	public static final int UNDEFINED = 0xFFFF0001;
	public static final int NONETWORK = 0xFFFF0002;
	public static final int CONNECT_TIME_OUT = 0xFFFF0003;
	public static final int SOCKET_TIME_OUT = 0xFFFF0004;
	public static final int SERVER_ERROR  = 0xFFFF0005;
	public static final int HTTP_404  = 0xFFFF0006;
	public static final int UN_KNOWN_HOST  = 0xFFFF0007;	
}
