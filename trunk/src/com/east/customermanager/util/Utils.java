package com.east.customermanager.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.east.customermanager.CustomerApp;
import com.east.customermanager.entity.VersionInfo;
import com.east.customermanager.log.CMLog;

public class Utils {

	private static final String TAG = "Utils";

	public static Typeface typeface;

	public static Typeface getCustomFont(Context context) {
		if (typeface == null) {
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/custom.ttf");
		}
		return typeface;
	}

	public static final int byteToShort(byte[] bytes) {
		return (bytes[0] << 8) + (bytes[1] & 0xFF);
	}

	public static boolean currentIsNight(long currentTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTime);
		int H = calendar.get(Calendar.HOUR_OF_DAY);
		if (H >= 18 || H < 6) {
			return true;
		}
		return false;
	}

	public static boolean currentIsNight() {
		return currentIsNight(System.currentTimeMillis());
	}

	public static boolean isSDCardEnable() {
		String SDState = Environment.getExternalStorageState();
		if (SDState != null
				&& SDState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 将dip转换为pix
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPixels(Context context, float dip) {
		return (int) (context.getResources().getDisplayMetrics().density * dip);
	}

	public static VersionInfo.UpdateType getAppVersionUpdateType(
			Context context, String newVersion) {
		VersionInfo.UpdateType type = VersionInfo.UpdateType.NO_UPDATE;
		String localVersion = getLocalAppVersion(context);
		try {
			int[] localVersionArray = getVersionNum(localVersion);
			int[] newVersionArray = getVersionNum(newVersion);
			if (newVersionArray[0] > localVersionArray[0]) {
				return VersionInfo.UpdateType.UPDATE_AND_PROMPT;
			} else if (newVersionArray[0] == localVersionArray[0]) {
				if (newVersionArray[1] > localVersionArray[1]) {
					return VersionInfo.UpdateType.UPDATE_AND_PROMPT;
				} else if (newVersionArray[1] == localVersionArray[1]) {
					if (newVersionArray[2] > localVersionArray[2]) {
						return VersionInfo.UpdateType.UPDATE_AND_PROMPT;
					} else if (newVersionArray[2] == localVersionArray[2]) {
						if (newVersionArray[3] > localVersionArray[3]) {
							return VersionInfo.UpdateType.UPDATE_NO_PROMPT;
						}
					}
				}
			}
		} catch (Exception e) {
			CMLog.e(TAG, e);
		}
		return type;
	};

	public static int[] getVersionNum(String version) {
		int[] versionIntArray = new int[4];
		String[] versionStringArray = version.split("[.]");
		int versionStringLenth = versionStringArray.length;
		for (int i = 0; i < 4; i++) {
			if (i < versionStringLenth) {
				try {
					versionIntArray[i] = Integer
							.parseInt(versionStringArray[i]);
				} catch (Exception e) {
					CMLog.e(TAG, e);
					versionIntArray[i] = 0;
				}
			} else {
				versionIntArray[i] = 0;
			}
		}
		return versionIntArray;
	}

	/**
	 * 获取本地app版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalAppVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		String version = null;
		try {
			version = pm.getPackageInfo("com.mlgb.aaparty", 0).versionName;
		} catch (NameNotFoundException e) {
			CMLog.e(TAG, e);
		}
		return version;
	}

	/**
	 * 获取屏幕宽度(像素)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getWidth();
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param window
	 * @return
	 */
	public static int getStatusBarHeight(Window window) {
		Rect outRect = new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(outRect);
		return outRect.top;
	}

	/**
	 * 获取屏幕高度(像素)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getHeight();
	}

	/**
	 * 根据不同设备的分辨率获取适合的图片尺寸
	 * 
	 * @param url
	 * @return
	 */
	public static String getFitScreenImageUrl(String url, int size) {

		if (size <= 0 || StringUtils.isNullOrEmpty(url)) {
			return url;
		}

		String returnUrl = "";
		int i = url.lastIndexOf(".");
		int length = url.length();
		String urlName = "";
		String suffix = "";
		if (i < length && i > 0) {
			urlName = url.substring(0, i);
			suffix = url.substring(i, length);
		} else {
			urlName = url;
		}
		int imageW = size;
		/**
		 * 锁定比例,按照固定尺寸取小值缩小(只允许缩小) 协议类型 < width >x< height>) 例如：600x200)
		 * 首先该协议转换后的图片不能是放大的，缩小的时候按照比例，取小值进行缩小
		 */
		returnUrl = urlName + "=C" + imageW + "x" + imageW + suffix
				+ "?quality=70";
		return returnUrl;
	}

	/**
	 * 获取设备版本
	 */
	public static String getPlatform() {
		String releaseversion = Build.VERSION.RELEASE;
		String version = "AR";
		if (releaseversion.contains("1.5")) {
			version = "AR_15";
		} else if (releaseversion.contains("1.6")) {
			version = "AR_16";
		} else if (releaseversion.contains("2.0")) {
			version = "AR_20";
		} else if (releaseversion.contains("2.1")) {
			version = "AR_21";
		} else if (releaseversion.contains("2.2")) {
			version = "AR_22";
		} else if (releaseversion.contains("2.3")) {
			version = "AR_23";
		} else if (releaseversion.contains("3.0")) {
			version = "AR_30";
		} else if (releaseversion.contains("3.1")) {
			version = "AR_31";
		} else if (releaseversion.contains("3.2")) {
			version = "AR_32";
		} else if (releaseversion.contains("4.0")) {
			version = "AR_40";
		}
		return version;
	}

	public static void showNotification(Context context, int id,
			boolean autoClear,String msg) {
		/*NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String tickerText = context.getResources().getString(
				R.string.app_notify_title);
//		String tickerContent = context.getResources().getString(
//				R.string.app_notify_content);
		Notification notification = new Notification(R.drawable.app_icon,
				tickerText, System.currentTimeMillis());
		if (autoClear) {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
		} else {
			notification.flags = Notification.FLAG_ONGOING_EVENT;
		}
		Intent intent = new Intent(context,StartUpActvity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.defaults = Notification.DEFAULT_ALL;
		notification.setLatestEventInfo(context, tickerText, msg,
				contentIntent);
		notificationManager.notify(id, notification);*/
	}

	/*
	 * public static void showNotification(Context context,int id) {
	 * NotificationManager notificationManager =
	 * (NotificationManager)context.getSystemService
	 * (Context.NOTIFICATION_SERVICE);
	 * 
	 * String tickerText =
	 * context.getResources().getString(R.string.app_notify_title);
	 * 
	 * Notification notification = new Notification(); notification.flags =
	 * Notification.FLAG_AUTO_CANCEL; notification.defaults |=
	 * Notification.DEFAULT_SOUND; notification.icon = R.drawable.ic_launcher;
	 * notification.tickerText = tickerText; RemoteViews contentView = new
	 * RemoteViews(context.getPackageName(),R.layout.notify_layout);
	 * notification.contentView = contentView; notificationManager.notify(id,
	 * notification); }
	 */

	public static void deleteNotification(Context context, int id) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}
	public static void deleteAllNotification(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
	

	public static boolean isIntenetAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(activeNetInfo!=null) {
			return true;
		}else {
			return false;
		}
	}

	public static boolean IsNetworkAvailable(Context context) {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = null;
		if (cm != null) {
			netInfo = cm.getActiveNetworkInfo();
		}
		if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
			flag = true;
		}

		return flag;
	}

	public static boolean isWiFiAvailable(Context context) {
		boolean WifiOK = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			WifiOK = true;
		}

		return WifiOK;
	}


	public static void showToast(Context context,int msgId) {
		if(CustomerApp.isAppOnForeground()) {
			Toast.makeText(context, context.getResources().getString(msgId), Toast.LENGTH_SHORT).show();
		}
	}


	public static void openGPSSettings(Context context) {       
		//获取GPS现在的状态（打开或是关闭状态）
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER );
		if(gpsEnabled) {
			//打开GPS 
			Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, true);
		}
	}
	public static void closeGPSSettings(Context context) {       
		//获取GPS现在的状态（打开或是关闭状态）
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER );
		if(gpsEnabled) {
			//关闭GPS
			Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, false );
		}
	}

	// 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }
    
    public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
    
    public static String getTimeForMinuts(long create) {
		if(create<=60) {
			return create + "秒";
		}else {
			return (create/60) + "分" + create % 60 + "秒";
		}
	}
    
	public static String processTime(long time) {
		long current = System.currentTimeMillis();
		long t = (current - time)/1000;
		if(t<60) {
			return t + "秒前";
		}else if(t>=60&&t<60*60) {
			return t/60 + "分钟前";
		}else if(t>=60*60&&t<24*60*60) {
			return t/(60*60) +"小时前";
		}else if(t>=24*60*60) {
			return t/(24*60*60) + "天前";
		}
		return null;
	}
	public static String processTimeStamp(String stamp) {
		try {
			long time = Long.parseLong(stamp);
			long current = System.currentTimeMillis();
			long t = (current - time)/1000;
			if(t<60) {
				return t + "秒前";
			}else if(t>=60&&t<60*60) {
				return t/60 + "分钟前";
			}else if(t>=60*60&&t<24*60*60) {
				return t/(60*60) +"小时前";
			}else if(t>=24*60*60) {
				return t/(24*60*60) + "天前";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String processTime(String created_at) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(created_at);
			long time = date.getTime();
			long current = System.currentTimeMillis();
			long t = (current - time)/1000;
			if(t<60) {
				return t + "秒前";
			}else if(t>=60&&t<60*60) {
				return t/60 + "分钟前";
			}else if(t>=60*60&&t<24*60*60) {
				return t/(60*60) +"小时前";
			}else if(t>=24*60*60) {
				return t/(24*60*60) + "天前";
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	

	public static long getTimeInMillis(String result) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = format.parse(result);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static String getDateInMillis(String result) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(result);
			return date.getTime()+"";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String processTimeToDate(String createTime) {
		if(StringUtils.isNullOrEmpty(createTime)) {
			return "";
		}
		long time = getTimeInMillis(createTime);
		long current = System.currentTimeMillis();
		long t = (current - time)/1000;
		if(t<24*60*60) {
			return "今天";
		}else{
			Date date = new Date(time);
			return date.getDay() + date.getMonth()+"月";
		}
	}
	
	public static String getAge(String birth) {
		if(StringUtils.isNullOrEmpty(birth)) {
			return "";
		}
		try {
			//{"birthday":"2013-11-09","code":"200","data":[],"introduce":"","label":"","message":"查询个人信息成功","nick":"嘎嘎","proffession":"","sex":0}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
			Date date = format.parse(birth);
		
			Calendar cal = Calendar.getInstance();

			if (cal.before(date)) {
				return "";

			}

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH)+1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(date);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			int age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					} 
				} else {
					age--;
				}
			} 
			return age +"";
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }
	
	public static String getDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return format.format(date);
	}
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(time);
		return format.format(date);
	}
	
	public static String fromatTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(time);
		return format.format(date);
	}
	public static String fromatTimeInChina(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
		Date date = new Date(time);
		return format.format(date);
	}
	public static String fromatTimeInChina(String time) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
			Date date = new Date(Long.parseLong(time));
			return format.format(date);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "未知";
	}

	public static String getWeek(int week) {
		switch (week) {
		case Calendar.SUNDAY:
			return "星期日";
		case Calendar.MONDAY:
			return "星期一";
		case Calendar.TUESDAY:
			return "星期二";
		case Calendar.WEDNESDAY:
			return "星期三";
		case Calendar.THURSDAY:
			return "星期四";
		case Calendar.FRIDAY:
			return "星期五";
		case Calendar.SATURDAY:
			return "星期六";
		default:
			break;
		}
		return "";
	}

	public static boolean hasSmartBar() {

		try {
			// 新型号可用反射调用 Build.hasSmartBar()
			Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
			return ((Boolean) method.invoke(null)).booleanValue();
		} catch (Exception e) {
		}
		// 反射不到 Build.hasSmartBar() ，则用 Build.DEVICE 判断
		if (Build.DEVICE.equals("mx2")) {
			return true;
		} else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
			return false;
		}
		return false;
	}

	/**
	 * 浏览器下载文件
	 * @param download_url
	 */
	public static void openUrl(Context context,String url) {
		if(StringUtils.isNullOrEmpty(url)) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri downUrl = Uri.parse(url);
		intent.setData(downUrl);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
	
	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getTime(timesamp);
			break;
		}

		return result;
	}

	
	public static int getJSONType(String str){
		if(!StringUtils.isNullOrEmpty(str)){
			final char[] strChar = str.substring(0, 1).toCharArray();
			final char firstChar = strChar[0];

			if(firstChar == '{'){
				return 1;
			}else if(firstChar == '['){
				return 2;
			}else{
				return 0;
			}
		}
		return 0;
	}


}
