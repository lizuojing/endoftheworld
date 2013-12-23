package com.east.customermanager.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class BitmapUtils {

	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPX = bitmap.getWidth() / 2;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return outBitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap decodeFileToBitmap(String filepath, float w, float h) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (w >= 0 && h >= 0) {
			try {
				options.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeFile(filepath, options);
				options.inJustDecodeBounds = false;
				int originalW = options.outWidth;
				int originalH = options.outHeight;
				float scale = 0;
				if (originalW > w) {
					if (originalW / w > originalH / h) {
						options.inSampleSize = (int) (originalH / h);
						scale = h / originalH * options.inSampleSize;
					} else {
						options.inSampleSize = (int) (originalW / w);
						scale = w / originalW * options.inSampleSize;
					}
				}
				Bitmap tempBitmap = BitmapFactory.decodeFile(filepath, options);
				Matrix m = new Matrix();
				m.postScale(scale, scale);
				bitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), m, true);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public static File compressImage(File file, float w) {
		if (file != null && file.exists() && w > 0) {
			try {
				ImageFormat format = getImageFormat(file);
				if (ImageFormat.GIF != format) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
					int originalW = options.outWidth;
					options.inSampleSize = (int) (originalW / w);
					options.inJustDecodeBounds = false;
					float scale = 0;
					if (originalW > w) {
						scale = w / originalW * options.inSampleSize;
					} else if (ImageFormat.JPG == format) {
						return file;
					}
					Matrix m = new Matrix();
					m.postScale(scale, scale);
					Bitmap tempBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
					bitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), m,
							false);
					tempBitmap.recycle();
					FileOutputStream out = new FileOutputStream(file);
					if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
						out.flush();
						out.close();
					}
					bitmap.recycle();
				}
			} catch (Throwable e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static ImageFormat getImageFormat(File file) {
		ImageFormat format = ImageFormat.UNKNOWN;
		try {
			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
			if (buffer != null) {
				buffer.mark(6);
				byte[] header = new byte[8];
				int head = buffer.read(header);
				if (head != -1) {
					if (header[0] == (byte) 0x47 && header[1] == (byte) 0x49 && header[2] == (byte) 0x46) {
						format = ImageFormat.GIF;
					} else if (header[0] == (byte) 0xff && header[1] == (byte) 0xd8) {
						format = ImageFormat.JPG;
					} else if (header[0] == (byte) 0x89 && header[1] == (byte) 0x50 && header[2] == (byte) 0x4e
							&& header[3] == (byte) 0x47 && header[4] == (byte) 0x0d && header[5] == (byte) 0x0a
							&& header[6] == (byte) 0x1a && header[7] == (byte) 0x0a) {
						format = ImageFormat.PNG;
					}
				}
				buffer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format;
	}

	public enum ImageFormat {
		JPG, PNG, GIF, UNKNOWN
	}

	public static void bitmapToFile(Bitmap bitmap, File tempFile) {
		if (tempFile == null || bitmap == null) {
			return;
		}
		try {
			FileOutputStream out = new FileOutputStream(tempFile);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
