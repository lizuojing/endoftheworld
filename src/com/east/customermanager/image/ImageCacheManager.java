package com.east.customermanager.image;

import java.io.File;
import java.io.FileFilter;
import java.net.URLEncoder;
import java.util.Calendar;

import android.os.AsyncTask;

import com.east.customermanager.config.CacheConfig;
import com.east.customermanager.util.StringUtils;

@SuppressWarnings("deprecation")
public class ImageCacheManager {

	public static String getCachePath(String url) {
		return getCachePath(CacheConfig.getImgDir(), url);
	}

	public static String getCachePath(String cacheDir, String url) {
		return StringUtils.modifyLongFileName(cacheDir + URLEncoder.encode(url) + ".cache");
	}

	/**
	 * 清空缓存文件夹
	 */
	public static void clearAllCache() {
		File file = new File(CacheConfig.getImgDir());
		if (file.exists() && file.isDirectory()) {
			deleteDir(file);
		}
	}

	/**
	 * 清除过期的缓存文件
	 */
	public static void clearOverdueCache() {
		new AsyncTask<Integer, Integer, Integer>() {
			@Override
			protected Integer doInBackground(Integer... params) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -3);
				File file = new File(CacheConfig.getImgDir());
				if (file.exists() && file.isDirectory()) {
					deleteFileBeforeTime(file, calendar.getTimeInMillis());
				}
				return 0;
			}
		}.execute(0);
	}

	/**
	 * 获取缓存的大小
	 * 
	 * @return
	 */
	public static int getCacheSize() {
		File file = new File(CacheConfig.getImgDir());
		if (file.exists() && file.isDirectory()) {
			return computeDirSize(file);
		}
		return 0;
	}

	private static void deleteFileBeforeTime(File dir, final long time) {
		File[] listFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.lastModified() <= time ? true : false;
			}
		});
		for (File file : listFiles) {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					deleteFileBeforeTime(file, time);
				}
			}
		}
	}

	private static int computeDirSize(File dir) {
		int size = 0;
		File[] listFiles = dir.listFiles();
		for (File file : listFiles) {
			if (file.exists()) {
				if (file.isFile()) {
					size += (int) file.length();
				} else if (file.isDirectory()) {
					size += computeDirSize(file);
				}
			}
		}
		return size;
	}

	private static void deleteDir(File dir) {
		File[] listFiles = dir.listFiles();
		for (File file : listFiles) {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					deleteDir(file);
				}
			}
		}
		dir.delete();
	}

}
