package com.east.customermanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class CustomerApp extends Application {

	private static final String TAG = "CustomerApp";
	private static Context appContext;
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	
	private static CustomerApp mInstance;
	private static int screenWidth;
	private static int screenHeight;

	@Override
	public void onCreate() {
		mInstance = this;
		appContext = this.getApplicationContext();
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager WM = (WindowManager)appContext.getSystemService(Context.WINDOW_SERVICE);
		WM.getDefaultDisplay().getMetrics(dm);
		screenHeight=dm.heightPixels;
		screenWidth=dm.widthPixels;
		super.onCreate();
	}

	
	public static CustomerApp getInstance() {
		return mInstance;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	

	public static boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) appContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if ("com.east.customermanager"
					.equals(tasksInfo.get(0).topActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isActivityOnForeground(Activity activity) {
		ActivityManager activityManager = (ActivityManager) appContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// activity位于堆栈的顶层
			if (activity.getClass().getName()
					.equals(tasksInfo.get(0).topActivity.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void exitApp() {
		for (Activity activity : allActivity) {
			activity.finish();
		}
		allActivity.clear();
		// Process.killProcess(Process.myPid());
		// System.exit(0);
	}
	public static void finishAllActivity() {
		for (Activity activity : allActivity) {
			activity.finish();
		}
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static Context getAppContext() {
		return appContext;
	}
}
