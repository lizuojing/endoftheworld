package com.east.customermanager.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

	public enum NetType {
		WIFI, MOBILE, UNKONW
	}

	public static NetType getNetType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null) {
			return NetType.UNKONW;
		} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return NetType.WIFI;
		} else {
			return NetType.MOBILE;
		}
	}

	public static boolean isWifiNet(Context context) {
		if (getNetType(context) == NetUtils.NetType.WIFI) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkNetwork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conn.getActiveNetworkInfo();
		if (net != null && net.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
