package com.east.customermanager.config;

import java.io.File;

import android.os.Environment;

/**
 * 缓存的相关配置
 */
public class CacheConfig {
	
	
	private static final String ROOT_DIR = Environment.getExternalStorageDirectory() + "/customer/";
	private static final String IMG_DIR = CacheConfig.ROOT_DIR + "img/";
	private static final String WALLPAPER_DIR = CacheConfig.ROOT_DIR + "wallpaper/";
	private static final String GalleryThumbnailCache_Dir = "/party/cache/galleryThumbnailCache/";

	
	public static String getRootDir() {
		File file = new File(ROOT_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath()+"/";
	}
	
	public static String getWallpaperDir() {
		File file = new File(WALLPAPER_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath()+"/";
	}

	public static String getImgDir() {
		File file = new File(IMG_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath()+"/";
	}
	
	/**
	 * 获取缓存路径
	 * @return
	 */
	public static File getCachePath() {
		File fileDir = new File(IMG_DIR);
		if(!fileDir.exists()) {
			fileDir.mkdir();
		}
		return fileDir;
	}

		public static File getGalleryThumbnailCacheDir() {
		File cacheDir = new File(Environment.getExternalStorageDirectory(), GalleryThumbnailCache_Dir);
		if (!cacheDir.exists())
		{
			cacheDir.mkdirs();
		}
		
		return cacheDir;
	}
}
