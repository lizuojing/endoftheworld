package com.east.customermanager.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.east.customermanager.config.CacheConfig;

public class ImageUtil {

	/**
	 * compress bitmap
	 * 
	 * @param filePath
	 * @param newW
	 * @param newH
	 * @return
	 */
	public static Bitmap getThumbnail(final String filePath, final int newW,
			final int newH, int orientation) {
		Bitmap bitmap = getBitmapFromGalleryThumbnailCache(filePath);
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = scaleBitmap(filePath, newW, newH, orientation);
		// 异步保存图片到缓存
		if (bitmap != null) {
			new AsyncTask<Bitmap, Object, Object>() {
				@Override
				protected Result doInBackground(Bitmap... params) {
					saveBitmapToGalleryThumbnailCache(filePath, params[0]);
					return null;
				}
			}.execute(bitmap);
		}
		return bitmap;
	}
	
	private static Bitmap getBitmapFromGalleryThumbnailCache(String filePath) {
		String fileName = getFileNameNoExtension(filePath);
		String thumbnailFilePath = CacheConfig.getGalleryThumbnailCacheDir()
				.getAbsolutePath()
				+ "/" + fileName;
		return BitmapFactory.decodeFile(thumbnailFilePath);
	}
	private static String getFileNameNoExtension(String filePath) {
		if (StringUtils.isNullOrEmpty(filePath)) {
			return "";
		}
		int begin = filePath.lastIndexOf("/") + 1;
		int end = filePath.lastIndexOf(".");
		int length = filePath.length();
		String fileName = "";
		if (end < length && begin < end) {
			fileName = filePath.substring(begin, end);
		}
		return fileName;
	}
	private static Bitmap scaleBitmap(final String filePath, final int newW,
			final int newH, int orientation) {
		Bitmap bitmap;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		int originalH = options.outHeight;
		int originalW = options.outWidth;
		final float scaleWidth = ((float) newW) / originalW;
		final float scaleHeight = ((float) newH) / originalH;
		float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
		options.inJustDecodeBounds = false;
		options.inSampleSize = (int) (1 / scale);
		options.inDither = true;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		Matrix matrix = new Matrix();
		matrix.postRotate(orientation);
		scale = scale
				/ (1.0f / ((int) (1 / scale) <= 1 ? 1 : (int) (1 / scale)));
		matrix.postScale(scale, scale);
		if (bitmap == null) {
			return bitmap;
		}
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
				.getHeight(), matrix, true);
		return bitmap;
	}

	private static void saveBitmapToGalleryThumbnailCache(String filePath,
			Bitmap bitmap) {
		String fileName = getFileNameNoExtension(filePath);
		File dir = CacheConfig.getGalleryThumbnailCacheDir();
		if (bitmap == null) {
			return;
		}

		File f = new File(dir, fileName);// 构建文件
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


}
