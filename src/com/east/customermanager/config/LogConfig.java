package com.east.customermanager.config;

import java.io.File;

import android.util.Log;

/**
 * 日志输出相关配置
 */
public class LogConfig {
	public static int LEVEL_PRINT_TO_SYSTEM = Log.VERBOSE;
	public static int LEVEL_PRINT_TO_FILE = Log.ERROR;
	public static final boolean PRINT_TO_SYSTEM = true;
	public static final boolean PRINT_TO_FILE = true;
	public static final String LOG_FILENAME = "log";
	public static final String LOG_CRASH_FILENAME = "crash_log";
	private static final String LOG_DIR = CacheConfig.getRootDir() + "log/";
	public static final int MAX_LOG_SIZE = 1024 * 300;

	public static String getLogDir() {
		File file = new File(LOG_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath()+"/";
	}
}
