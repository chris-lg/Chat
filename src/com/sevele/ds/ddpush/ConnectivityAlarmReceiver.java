package com.sevele.ds.ddpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sevele.ds.app.DsApplication;
import com.sevele.ds.utils.Util;
/**
 * 
 *@author:liu ge
 *@createTime:2015年5月22日
 *@descrption:当网络该变的时候该广播接收到，在登录的前提下可以启动ddpush service 发送心跳包
 */
public class ConnectivityAlarmReceiver extends BroadcastReceiver {

	public ConnectivityAlarmReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Util.hasNetwork(context) == false) {
			return;
		}
		if (DsApplication.isLogin!=null && DsApplication.isLogin) {
			Intent startSrv = new Intent(context, OnlineService.class);
			startSrv.putExtra("CMD", "RESET");
			context.startService(startSrv);
		}
	}
}
