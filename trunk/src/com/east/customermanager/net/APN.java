package com.east.customermanager.net;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.east.customermanager.util.StringUtils;


public class APN {
	public String proxy = null;
	public int port = 0;
	
	public APN(String proxy, int port)
	{
		this.proxy = proxy;
		this.port = port;
	}
	
	public boolean hasProxy()
	{
		return StringUtils.isNotNullOrEmpty(proxy) && (port != 0);
	}
	
	public static APN getPreferAPN(Context context)
	{
		//判断wifi是否可用
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
    	NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo(); 
    	boolean WifiOK= ( activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI );
		if(!WifiOK){
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
			if (cursor != null && cursor.moveToFirst())
			{
				String proxy = cursor.getString(cursor.getColumnIndex("proxy"));
				int port = cursor.getInt(cursor.getColumnIndex("port"));
				String mcc = cursor.getString(cursor.getColumnIndex("mcc"));   
	            String mnc = cursor.getString(cursor.getColumnIndex("mnc"));
	            if( mcc!=null && mcc.equals("460") && mnc!=null && (mnc.equals("03")|| mnc.equals("05")) ) {
	            	//电信手机， 电信wap接入点get请求返回404
	            	cursor.close();
	            	return null;
	            }
				cursor.close();
				return new APN(proxy, port);
			}
		}
		return null;
	}
	
}
