package com.app.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.utils.DBPreferenceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;


public class BaseApplication extends Application implements
		UncaughtExceptionHandler {

	private ArrayList<Activity> actList;

	static String pakage;

	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public BaseApplication() {

		actList = new ArrayList<Activity>();
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	public int getVerCode() {
		int verCode = -1;
		try {
			verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verCode;
	}

	public String getVerName() {
		String verName = "";
		try {
			verName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verName;
	}

	/**
	 * 退出
	 * */
	@SuppressLint("NewApi")
	public void quit() {
		// 清空网络请求队列
		// client.cancelAll();

		int version = android.os.Build.VERSION.SDK_INT;

		for (Activity act : actList) {
			if (!act.isFinishing())
				act.finish();
		}
		actList.clear();
		
		if (version <= 7) {
			System.out.println("   version  < 7");
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.restartPackage(getPackageName());
		} else {
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(getPackageName());
		}
		
		 int pid = android.os.Process.myPid();
		 Log.d("base app", "pid="+pid);
		 android.os.Process.killProcess(pid);
		// System.exit(0);
	}

	public void addActivitToStack(Activity act) {
		actList.add(act);
	}

	public void removeFromStack(Activity act) {
		actList.remove(act);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		if (!treatErr(ex) && mDefaultHandler != null)
			mDefaultHandler.uncaughtException(thread, ex);
		else
			quit();

	}

	public boolean treatErr(Throwable ex) {
		return false;
	}

}
