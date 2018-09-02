package com.sevele.ds.ddpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

import com.sevele.ds.utils.Util;

/**
 * @author:liu ge
 * @createTime:2015年5月22日
 * @descrption:时钟
 */
public class TickAlarmReceiver extends BroadcastReceiver {

	WakeLock wakeLock;

	public TickAlarmReceiver() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Util.hasNetwork(context) == false) {
			return;
		}
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}
}
