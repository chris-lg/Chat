package com.sevele.ds.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsAppManager;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * <P>
 * CreateTime 2014/12/9
 * 
 * @author Administrator
 * 
 */
public class UIHelper {
	private static Dialog loadingDialog;
	public static boolean isLoadingShow=false;
	/**
	 * 显示正在加载。。。
	 */
	public static void loading() {
		Activity current = DsAppManager.getAppManager().currentActivity();
		loadingDialog = new Dialog(current, R.style.LodingDialog);
		loadingDialog.setContentView(R.layout.loadingdialog);
		loadingDialog.show();
		isLoadingShow=true;
	}

	/**
	 * 取消显示正在加载。。
	 */
	public static void cancleLoading() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			isLoadingShow=false;
		}
	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(String msg) {
		Toast.makeText(DsAppManager.getAppManager().currentActivity(), msg,
				Toast.LENGTH_SHORT).show();
	}
	
}
