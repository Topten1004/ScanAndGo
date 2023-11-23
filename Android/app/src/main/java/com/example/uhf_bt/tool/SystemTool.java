package com.example.uhf_bt.tool;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * 系统工具类
 * @author WuShengjun
 * @date 2017年2月28日
 */
public class SystemTool {
	private final static String TAG = "SystemTool";
	/**
	 * 设置系统时间
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 是否设置成功
	 */
	public static boolean setSysDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		long when = cal.getTimeInMillis();
		if(when / 1000 < Integer.MAX_VALUE) {
			return SystemClock.setCurrentTimeMillis(when);
		}
		return false;
	}

	/**
	 * 设置系统时间
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @param context
	 * @return
	 */
	public static boolean setSysTime(int hourOfDay, int minute, int second, Context context) {
		ContentResolver resolver = context.getContentResolver();
		// 设置为24小时制
		android.provider.Settings.System
				.putString(resolver, android.provider.Settings.System.TIME_12_24, "24");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, 0);
		long when = cal.getTimeInMillis();
		if(when / 1000 < Integer.MAX_VALUE) {
			return SystemClock.setCurrentTimeMillis(when);
		}
		return false;
	}

	/**
	 * 获取屏幕的Width(不包括虚拟导航栏)
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		getDefaultDisplay(context).getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕的Height(不包括虚拟导航栏)
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		getDefaultDisplay(context).getMetrics(dm);
		return dm.heightPixels;
	}
	/**
	 * 获取屏幕的真实Width
	 * @param context
	 * @return
	 */
	public static int getRealScreenWidth(Context context) {
		DisplayMetrics realMetrics = new DisplayMetrics();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			getDefaultDisplay(context).getRealMetrics(realMetrics);
		} else {
			getDefaultDisplay(context).getMetrics(realMetrics);
		}
		return realMetrics.widthPixels;
	}

	/**
	 * 获取屏幕的真实Height
	 * @param context
	 * @return
	 */
	public static int getRealScreenHeight(Context context) {
		DisplayMetrics realMetrics = new DisplayMetrics();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			getDefaultDisplay(context).getRealMetrics(realMetrics);
		} else {
			getDefaultDisplay(context).getMetrics(realMetrics);
		}
		return realMetrics.heightPixels;
	}

	/**
	 * 获取状态栏高度
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}

	/**
	 * 设置状态栏颜色
	 * @param activity
	 * @param color
	 * @param isDarkText
	 */
	public static void setStatusBarColor(Activity activity, int color, boolean isDarkText) {
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			if(isDarkText) {
				setStatusDarkText(activity); // 设置状态栏字体为深色
			}
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
		}
	}

	/**
	 * 设置状态栏字体为深色（Api>=23）
	 * @param activity
	 */
	public static void setStatusDarkText(Activity activity) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 设置状态栏字体为深色
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
	}
	/**
	 * 清除状态栏深色字体，还原为浅色
	 * @param activity
	 */
	public static void resetStatusText(Activity activity) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 清除状态栏深色字体，还原为浅色
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		}
	}

	/**
	 * 设置底部导航栏颜色
	 * @param activity
	 * @param color
	 */
	public static void setNavigationBarColor(Activity activity, int color) {
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, color));
		}
	}

	/**
	 * 隐藏导航栏
	 * @param activity
	 */
	private static void hideNavigationBar(Activity activity) {
		hideNavigationBar(activity, false, false);
	}

	/**
	 * 隐藏导航栏
	 * @param activity
	 * @param fullscreen 是否设置全屏
	 * @param lithtStatusBar 是否设置状态栏深色字体
	 */
	private static void hideNavigationBar(Activity activity, boolean fullscreen, boolean lithtStatusBar) {
		View decorView = activity.getWindow().getDecorView();
		int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
		if(fullscreen) {
			flags = flags | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
		}
		if(lithtStatusBar) {
			flags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		}
		decorView.setSystemUiVisibility(flags);
	}

	/**
	 * 是否存在导航栏（底部或者右边虚拟按键 api>=17
	 * @param context
	 * @return
	 */
	public static boolean hasNavigationBar(Context context) {
		int screenRealWidth = getRealScreenWidth(context);
		int screenRealHeight = getRealScreenHeight(context);

		int screenWidth = getScreenWidth(context);
		int screenHeight = getScreenHeight(context);
//		Log.e(TAG, "wid=" + screenWidth + ", hei=" + screenHeight + ", realWid=" + screenRealWidth + ", realHei=" + screenRealHeight);
		return screenRealWidth > screenWidth || screenRealHeight > screenHeight;
	}

	/**
	 * 获取底部虚拟按键导航栏高度
	 * @param context
	 * @return
	 */
	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		if (hasNavigationBar(context)) {
			Resources res = context.getResources();
			int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = res.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}

	/**
	 * 获取导航栏的位置（一般只有在底部和右边两种情况）
	 * @param context
	 * @return 0：在底部，1：在右边
	 */
	public static int getNavigationBarLacation(Context context) {
		int screenRealWidth = getRealScreenWidth(context);
		int screenWidth = getScreenWidth(context);
		if (screenRealWidth != screenWidth) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 判断虚拟按键栏是否重写(Api>=21)
	 * @return
	 */
	private static String getNavigationBarOverride() {
		String sNavBarOverride = null;
		try {
			Class c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("get", String.class);
			m.setAccessible(true);
			sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
		} catch (Throwable e) {
		}
		return sNavBarOverride;
	}

	private static Display getDefaultDisplay(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay();
	}

	/**
	 * 显示软键盘
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view != null) {
			imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
		}
	}

	/**
	 * 延迟显示软键盘
	 * @param context
	 * @param view
	 * @param delay 延迟时间(ms)
	 */
	public static void showSoftInput(final Context context, final View view, long delay) {
		if(delay > 0) {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					showSoftInput(context, view);
				}
			};
			new Timer().schedule(task, delay);
		} else {
			showSoftInput(context, view);
		}
	}

	/**
	 * 隐藏软键盘
	 * @param context
	 * @param v
	 * @return
	 */
	public static Boolean hideInputMethod(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && v != null) {
			return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
		return false;
	}

	/**
	 * dp转px
	 * @param context
	 * @param dpVal
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale * dpVal + 0.5f);
	}

	/**
	 * px转dp
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public static int px2dp(Context context, float pxVal) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxVal / scale + 0.5f);
	}

	/**
	 * 安装app
	 * @param context
	 * @param contentUri
	 */
	public static void installApp(Context context,Uri contentUri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 安装app
	 * @param context
	 * @param apkFileName apk绝对路径
	 */
	public static void installApp(Context context, String apkFileName) {
		installApp(context, new File(apkFileName));
	}

	/**
	 * 安装app
	 * @param context
	 * @param apkFile apk文件
	 */
	public static void installApp(Context context, File apkFile) {
		installApp(context, Uri.fromFile(apkFile));
	}

	/**
	 * 安装app
	 * @param context
	 * @param apkFile apk文件
	 */
	public static void installAppForMoreSdk24(Context context, String authority, File apkFile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		//判断是否是AndroidN以及更高的版本
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		Uri contentUri = FileProvider.getUriForFile(context, authority, apkFile);
		intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static class InstallAppBroadcast extends BroadcastReceiver {
		public final static String ADD = "add";
		public final static String REMOVE = "add";
		public final static String RESTART = "add";
		public final static String CHANGED = "add";
		public final static String REPLACE = "add";
		private OnReveiceListener onReviceListener;
		public InstallAppBroadcast(OnReveiceListener onReviceListener) {
			this.onReviceListener = onReviceListener;
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			if(onReviceListener != null) {
				if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) { // 有应用被添加
					onReviceListener.onReceive(context, ADD);
				} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) { // 有应用被删除
					onReviceListener.onReceive(context, REMOVE);
				} else if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) { // 有应用被改变
					onReviceListener.onReceive(context, CHANGED);
				} else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) { // 有应用被替换
					onReviceListener.onReceive(context, REPLACE);
				} else if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) { // 有应用被重启
					onReviceListener.onReceive(context, RESTART);
				}
			}
		}

		public interface OnReveiceListener {
			void onReceive(Context context, String action);
		}
	}

	/**
	 * 获取设备唯一标识号
	 * @return 若不存在返回"0"
	 */
	public static String getIMEI(Context context) {
		String imei = "";
		if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
			return TextUtils.isEmpty(imei) ? "0" : imei;
		}
		return imei;
	}

	/**
	 * 获取app版本名字
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * 获取app版本码
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		if(info == null) {
			info = new PackageInfo();
		}
		return info;
	}

	private static PackageInfo getProviderPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
//			ProviderInfo[] providers = info.providers;
//			for (ProviderInfo provider : providers) {
//				Log.d(TAG, "name is " + provider.name);
//				Log.d(TAG, "authority is " + provider.authority);
//				if (provider.metaData != null) {
//					Log.d(TAG, "metadata is " + provider.metaData.toString());
//					Log.d(TAG, "resource in metadata is "
//							+ provider.metaData.getString("THE_KEY", "Unkonown"));
//				}
//			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "package not found");
			e.printStackTrace();
		}
		return info;
	}
}
