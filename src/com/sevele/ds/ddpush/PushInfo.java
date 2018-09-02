package com.sevele.ds.ddpush;

import org.ddpush.im.v1.client.appserver.Pusher;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.sevele.ds.app.DsConstant;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.Util;
/**
 * 
 * @author:liu ge
 * @createTime:2015年5月22日
 * @descrption:推送信息发送线程
 */
public class PushInfo implements Runnable {

	private Context context;
	private byte[] uuid;
	private byte[] msg;// 推送的信息
	private Handler handler;//

	public PushInfo(Context context,int pushToId,String msg,Handler handler) {
		this.context = context;
		this.handler=handler;
		byteInit(pushToId, msg);
	}

	public void run() {
		Pusher pusher = null;
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TOAST");
		try {
			boolean result;
			pusher = new Pusher(HttpUrl.DD_SERVER_IP, HttpUrl.PUSH_PORT, 1000 * 5);
			result = pusher.push0x20Message(uuid, msg);
			if (result) {
			    handler.obtainMessage(DsConstant.HANDLER_TXTMSG_OK);
				
			} else {
				handler.obtainMessage(DsConstant.HANDLER_NET_FAIL);
				startSrv.putExtra("TEXT", "发送失败！格式有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.obtainMessage(DsConstant.HANDLER_NET_FAIL);
			startSrv.putExtra("TEXT", "发送失败！" + e.getMessage());
		} finally {
			if (pusher != null) {
				try {
					pusher.close();
				} catch (Exception e) {
				}
				;
			}
		}
		context.startService(startSrv);
	}
	/**
	 * 初始化发送人和发送信息
	 *@param targetUserName
	 *@param sendData
	 */
	private void byteInit(int targetUserName, String sendData) {
		try {
			uuid = Util.md5Byte(String.valueOf(targetUserName));
		} catch (Exception e) {
			LogUtil.LogTest("错误：" + uuid);
			return;
		}
		try {
			msg = sendData.getBytes("UTF-8");
		} catch (Exception e) {
			LogUtil.LogTest("错误：" + msg);
			return;
		}
	}
}
