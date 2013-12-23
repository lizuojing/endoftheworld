package com.east.customermanager.util;

import java.lang.Thread.UncaughtExceptionHandler;

import com.east.customermanager.log.CMLog;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler INSTANCE;
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	public void init() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		CMLog.writeCrashLogToFile("CRASH", e.getMessage(), e);
		mDefaultHandler.uncaughtException(t, e);
	}

}
