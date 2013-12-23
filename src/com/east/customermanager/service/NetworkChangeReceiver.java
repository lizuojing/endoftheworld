package com.east.customermanager.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.east.customermanager.log.CMLog;
import com.east.customermanager.util.NetUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

	private static final String TAG = "NetworkChangeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		CMLog.d(TAG, "onReceive start run");
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(action)) {
		}
	}

}
