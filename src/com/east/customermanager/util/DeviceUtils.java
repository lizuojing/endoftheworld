package com.east.customermanager.util;

import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.east.customermanager.CustomerApp;

/**
 * 设备相关
 * @author start
 *
 */
public class DeviceUtils {

	/**
	 * 获取设备id
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String deviceId = null;
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (manager != null) {
			deviceId = manager.getDeviceId();
		}
		if (deviceId != null) {
			return "IMEI:" + deviceId;
		}
		deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		if (deviceId != null) {
			return "ANDROID:" + deviceId;
		}
		deviceId = Installation.id(context);
		return "UUID:" + deviceId;
	}
	

	//获取手机系统版本信息   
	public static String getPlatform() {
		int sdkINT = android.os.Build.VERSION.SDK_INT ;
		String version = "AR";
        if( sdkINT == 5 || sdkINT == 6 ) {
			version = "AR_20";
		} else if( sdkINT == 7 ) {
			version = "AR_21";
		} else if( sdkINT == 8 ) {
			version = "AR_22";
		} else if( sdkINT == 9 || sdkINT == 10 ) {
			version = "AR_23";
		} else if( sdkINT == 11 || sdkINT == 12 || sdkINT == 13 ) {
			version = "AR_30" ;
		} else if( sdkINT == 14 || sdkINT == 15 ) {
			version = "AR_40";
		}
		return version; 
	}
	
	public static int getSDK_INT() {
		int sdkINT = android.os.Build.VERSION.SDK_INT ;
		return sdkINT;
	}
	
	public static String getOSInfo() {
		String osInfo = android.os.Build.VERSION.RELEASE;
		return osInfo;
	}
	
	
	//判断是否为android2.2或以上版本
	public static Boolean isAndroid22() {   
		boolean Android22 = true;
		
		int sdkINT = android.os.Build.VERSION.SDK_INT ;
		if( sdkINT < 8 ) {
			Android22 = false;
		} 
		
		return Android22;
	}
	
	
	//系统语言
	public static String getLocalName(){
		Locale locale = Locale.getDefault();
		return locale.getDisplayName();	
	}
	
	//手机品牌
	public static String getBrand() {
		return android.os.Build.BRAND;
	}
	
	public static String getModel() {
		return android.os.Build.MODEL;
	}
	
	public static String getTimeZone() {
		String offset = null; 
		
		Calendar cal = Calendar.getInstance();
		int timezone = cal.getTimeZone().getRawOffset()/3600000;
		String top = "";
		if(Math.abs(timezone)<10)
		{
			top = "0";
		}
		String fen = String.valueOf(cal.getTimeZone().getRawOffset()%3600000/60000);
		if(2>fen.length())
		{
			fen = "0"+fen;
		}
		else
		{
			fen = fen.substring(0, 2);
		}
		if( timezone >= 0 )
		{
			offset= "+"+top+timezone+":"+fen ;
		}
		else if ( timezone < 0 )
		{
			offset=  "-"+top+Math.abs(timezone)+":"+fen;
		}
		return offset;
	}
	
	public static String getLanguage() {
		Locale locale = Locale.getDefault();
		String language=  locale.getLanguage();
		return language;
	}
	
	public static String getLocaleCountry() {
		  Locale locale = Locale.getDefault();
		  return locale.getCountry();
    }  
	
	
	public static boolean IsNetworkAvailable() {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) CustomerApp.getAppContext()
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
	
	public static boolean isWiFiAvailable() {
		boolean WifiOK=false;
    	
    	ConnectivityManager connectivityManager = (ConnectivityManager) CustomerApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
    	NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
    	if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)  {  
    		WifiOK=true;
    	}  

        return WifiOK;
	}
	
	
	public static boolean isSIMcardAvailable() {
		TelephonyManager tm = (TelephonyManager)CustomerApp.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
    	if(tm.getSimState()!=TelephonyManager.SIM_STATE_READY) {
    		return false;
    	} else {
    		return true;
    	}
	}

}
