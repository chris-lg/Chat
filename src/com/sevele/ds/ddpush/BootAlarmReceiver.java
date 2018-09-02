package com.sevele.ds.ddpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 
 *@author:liu ge
 *@createTime:2015年5月22日
 *@descrption:该广播接收到退出ddpush 服务的时候 发送消息让ddpush 服务退出
 */
public class BootAlarmReceiver extends BroadcastReceiver {

	public BootAlarmReceiver() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "EXIT");
		context.startService(startSrv);
	}

}
