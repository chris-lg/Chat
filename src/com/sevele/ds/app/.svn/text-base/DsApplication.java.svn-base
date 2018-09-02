package com.sevele.ds.app;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.decryptstranger.R;
import com.lidroid.xutils.BitmapUtils;
import com.sevele.ds.common.DbOperation;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.ImageDownLoader;

/**
 * @author:liu ge
 * @createTime:2015年3月18日
 * @descrption:程序的入口，在此处申明应用级的对象,这些对象在onCreate中初始化
 */
public class DsApplication extends Application {
	public static SharedPreferences mSharedPreferences;
	public static Context mContext; // 全局的Context
	// 在Activity跳转的时候可以将要传递的对象放入该map中,使用该map的时候小心内存泄漏，在该hashmap中的这个对象要被回收的时候，要把该对象从map移除。

	public static UserTable user;

	public static FriendTable currentFd;

	public static HashMap<String, Object> tempObject; // 用于储存临时的对象

	public static DbOperation db;

	public static Boolean isLogin = false;// 判断此时是否已经登录

	public static BitmapUtils bitmapUtils;

	public static Bitmap uBitmap;

	public static File audioFile;

	public static File imgFile;

	public static ImageDownLoader imgDownLoader;

	@Override
	public void onCreate() {

		super.onCreate();
		init();
	}

	private void init() {

		mContext = DsApplication.this;
		tempObject = new HashMap<String, Object>();
		audioFile = DsApplication.mContext.getExternalFilesDir("audioFiles");// 创建语音图片文件

		// 若存在不重复创建
		imgFile = DsApplication.mContext.getExternalFilesDir("imgFiles");
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		db = DbOperation.getDbInstance();
		bimapUtilsInit();
		imgDownLoader = new ImageDownLoader(mContext);
	}

	private void bimapUtilsInit() {

		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultLoadingImage(R.drawable.all_avatar_user_default);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtils.configDiskCacheEnabled(true);

	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getApplicationContext().getPackageManager().getPackageInfo(
					getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 切换帐号时候需要恢复初始信息
	 */
	public static void resetInit() {
		if (uBitmap != null && !uBitmap.isRecycled()) {
			uBitmap.recycle();
			uBitmap=null;
		}
		
		
		isLogin = false;
		if (tempObject != null) {
			Set<String> keys = tempObject.keySet();
			if (keys != null) {
				for (String key : keys) {
					tempObject.remove(key);
				}
			}
		}
		if(user.getUserHeadPicture()!=null){
			imgDownLoader.removeCacheBitmap(user.getUserHeadPicture());
		}
		user = null;
	}
}
