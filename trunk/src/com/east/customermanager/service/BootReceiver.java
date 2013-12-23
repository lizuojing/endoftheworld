package com.east.customermanager.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.east.customermanager.log.CMLog;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		CMLog.d(TAG, "onReceive start run");
	}
}
