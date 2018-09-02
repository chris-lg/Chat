package com.sevele.ds.http;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.UIHelper;

/**
 * 
 * @author:liu ge
 * @createTime:2015年4月10日
 * @descrption:服务器交互工具类
 */
public class HttpAPI {

	private Handler handler;
	private HttpUtils hUtils;

	public HttpAPI(Handler handler) {
		this.handler = handler;
		hUtils = XutilsHttpClient.getInstence(DsApplication.mContext);
	}

	/**
	 * @param registInfo
	 *            注册时候必须上传的数据
	 * @param registUrl
	 *            注册是后的url
	 * @return 返回服务器返回的数据
	 */
	public void httpRegister(String registInfo, String registUrl) {
		if (registInfo == null)
			return;
		RequestParams params = new RequestParams();// 默认的是utf-8
		params.addBodyParameter("registInfo", registInfo);
		hUtils.send(HttpRequest.HttpMethod.POST, registUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {// 连接错误的时候返回一个空字符串
						handler.sendEmptyMessage(DsConstant.HANDLER_NET_LINK_FAIL);
					}

					@Override
					public void onSuccess(ResponseInfo<String> reJson) {
						if (reJson.statusCode == 200) {
							Log.e("", "服务器返回的Json数据 --：" + reJson.result);
							sendMsg(reJson.result,
									DsConstant.HANDLER_NET_REGIST_OK);
						}
					}
				});
	}

	/**
	 * 
	 * @param userName
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param url
	 *            登录服务器的url
	 */
	public void httpLogin(String userName, String pwd, String loginUrl) {

		RequestParams loginParams = new RequestParams();// 默认的是utf-8
		// loginParams.setContentType("multipart/form-data");

		loginParams.addQueryStringParameter(DsConstant.USER_COUNT, userName);
		loginParams.addQueryStringParameter(DsConstant.USER_PASSWORD, pwd);

		hUtils.send(HttpRequest.HttpMethod.POST, loginUrl, loginParams,
				new RequestCallBack<String>() {
					@Override
					// 上传失败
					public void onFailure(HttpException arg0, String re) {
						Log.e("操作失败 -", "" + re); // 操作失败
						sendMsg(re, DsConstant.HANDLER_NET_LINK_FAIL);
					}

					@Override
					public void onSuccess(ResponseInfo<String> reJson) {
						if (reJson.statusCode == 200) {
							Log.e("服务器返回的Json数据 ------：：",
									"" + reJson.result.toString());
							sendMsg(reJson.result,
									DsConstant.HANDLER_NET_LOGIN_OK);
						}
					}
				});
	}

	/**
	 * 用于发送消息给应用服务器让其发推送消息给ddpush服务器
	 * 
	 * @param map
	 *            存放一系列要发送的键值对
	 * @param user
	 *            UserBean 对象，主要用于发送JESSIONID，和UserId
	 * @param sendUrl
	 *            发送的请求位置
	 * @param 操作成功后Message
	 *            .what的值
	 * 
	 */
	public void httpSendTxt(HashMap<String, Object> map, UserTable user,
			String sendUrl, final int msgWhat, final Integer index,
			final MsgBeanTable chat) {
		RequestParams sendParams = new RequestParams();
		if (user != null) {
			sendParams.addBodyParameter(DsConstant.USER_JESSIONID,
					user.getJessionId());
			sendParams.addBodyParameter(DsConstant.USER_ID,
					String.valueOf(user.getId()));
		}
		// 迭代map里面的值
		if (map != null) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sendParams.addBodyParameter(key, String.valueOf(map.get(key)));
			}
		}
		// HttpUtils hUtils = new HttpUtils();
		hUtils.send(HttpRequest.HttpMethod.POST, sendUrl, sendParams,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						UIHelper.ToastMessage("上传失败");
						if (index != null && chat != null) {
							Message msg = new Message();
							msg.what = DsConstant.HANDLER_QINIU_NET_UPLOAD_FAIL;
							msg.arg1 = index;
							msg.obj = chat;
							handler.sendMessage(msg);
							chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
							return;
						}
						handler.sendEmptyMessage(DsConstant.HANDLER_NET_FAIL);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onSuccess(ResponseInfo<String> reJson) {
						if (reJson.statusCode == 200) {
							if (index != null) {
								Message msg = new Message();
								msg.what = msgWhat;
								chat.setLoadState(DsConstant.PUSH_LOAD_OK);
								msg.arg1 = index;
								msg.obj = chat;
								handler.sendMessage(msg);
								Log.e("服务器返回的Json数据 -：：",
										"" + reJson.result.toString());
							} else {
								Log.e("服务器返回的Json数据 -：：",
										"" + reJson.result.toString());
								sendMsg(reJson.result, msgWhat);
							}
						}
					}
				});
	}

	/**
	 * 用于向服务器发送一般的请求
	 * 
	 * @param map
	 * @param sendUrl
	 * @param msgWhat
	 */
	public void httpSendTxt(HashMap<String, Object> map, String sendUrl,
			final int msgWhat) {
		RequestParams sendParams = new RequestParams();
		sendParams.addBodyParameter(DsConstant.USER_JESSIONID,
				DsApplication.user.getJessionId());
		// 迭代map里面的值
		if (map != null) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sendParams.addBodyParameter(key, String.valueOf(map.get(key)));
			}
		}
		// HttpUtils hUtils = new HttpUtils();
		hUtils.send(HttpRequest.HttpMethod.POST, sendUrl, sendParams,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						UIHelper.ToastMessage("上传失败");
						handler.obtainMessage(DsConstant.HANDLER_NET_FAIL)
								.sendToTarget();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onSuccess(ResponseInfo<String> reJson) {
						if (reJson.statusCode == 200) {
							handler.obtainMessage(msgWhat, reJson.result)
									.sendToTarget();
						}
					}
				});
	}

	/**
	 * 
	 * @param requstUrl
	 *            请求文件url
	 * @param restorePath
	 *            得到文件后保存地方
	 */
	public void requestFile(String requstUrl, String restorePath,
			final MsgBeanTable chat, final Integer pos) {
		hUtils.download(requstUrl, restorePath, false, false,
				new RequestCallBack<File>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.e("", "" + arg1);
						Log.e("", "" + arg0);
						chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
						handler.sendEmptyMessage(DsConstant.HANDLER_QINIU_NET_DOWNLOAD_FAIL);
					}

					@Override
					public void onSuccess(ResponseInfo<File> re) {
						if (re.statusCode == 200) {
							chat.setLoadState(DsConstant.PUSH_LOAD_OK);
							if (DsConstant.VALUE_LEFT_IMAGE == chat
									.getShowType()) {
								chat.setMsgContent(re.result.getName());
								handler.obtainMessage(
										DsConstant.HANDLER_QINIU_NET_DOWNLOAD_PIC_OK,
										pos, 0, chat).sendToTarget();
								;
							} else if (DsConstant.VALUE_LEFT_AUDIO == chat
									.getShowType()) {
								chat.setMsgContent(re.result.getAbsolutePath());
								handler.obtainMessage(
										DsConstant.HANDLER_QINIU_NET_DOWNLOAD_VOICE_OK,
										pos, 0, chat).sendToTarget();
								;
							} else {
								sendMsg(re.result,
										DsConstant.HANDLER_QINIU_NET_DOWNLOAD_OK);
							}
						} else {
							chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
						}
					}
				});
	}

	/**
	 * 
	 * @param token
	 *            上传到七牛必须的token
	 * @param file
	 *            上传的文件
	 * @param key
	 *            上传到七牛后的文件命名 用户 规定：id+当前时间
	 */
	public void upLoadFileToQiNiu(String token, File file, String key,
			final int msgWhat) {
		UploadManager up = new UploadManager();
		up.put(file, key, token, new UpCompletionHandler() {
			@Override
			public void complete(String arg0,
					com.qiniu.android.http.ResponseInfo info, JSONObject arg2) {
				if (info.isOK()) {
					sendMsg(info.toString(), msgWhat);
				}
			}
		}, null);
	}

	public void upLoadFileToQiNiu(String token, String file, String key,
			final Integer msgWhat, final HashMap<String, Object> map,
			final Integer index, final MsgBeanTable chat) {
		UploadManager up = new UploadManager();
		up.put(file, key, token, new UpCompletionHandler() {
			@Override
			public void complete(String arg0,
					com.qiniu.android.http.ResponseInfo info, JSONObject arg2) {
				if (info.isOK()) {
					// 成功上传后，请求服务器推送消息
					if (msgWhat != null && map != null && index != null) {
						httpSendTxt(map, DsApplication.user,
								HttpUrl.PUSHMSG_URL, msgWhat, index, chat);
					}
				} else {
					chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
					handler.obtainMessage(
							DsConstant.HANDLER_QINIU_NET_UPLOAD_FAIL, index, 0,
							chat).sendToTarget();

				}
			}
		}, null);
	}

	public void requestFile(String netUrl, String localUrl, final int msgWhat) {
		hUtils.download(netUrl, localUrl, false, false,
				new RequestCallBack<File>() {
					@Override
					public void onSuccess(ResponseInfo<File> re) {
						sendMsg(re.result, msgWhat);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessage(DsConstant.HANDLER_NET_FAIL);
					}
				});
	}

	/**
	 * 单例模式获取HttpUtils对象
	 */
	static class XutilsHttpClient {
		private static HttpUtils client;

		/**
		 * 单例模式获取实例对象
		 * 
		 * @param context
		 *            应用程序上下文
		 * @return HttpUtils对象实例
		 */
		public synchronized static HttpUtils getInstence(Context context) {
			if (client == null) {
				// 设置请求超时时间为10秒
				client = new HttpUtils(1000 * 10);
				client.configSoTimeout(1000 * 10);
				client.configResponseTextCharset("UTF-8");
				// 保存服务器端(Session)的Cookie
				PreferencesCookieStore cookieStore = new PreferencesCookieStore(
						context);
				cookieStore.clear(); // 清除原来的cookie
				client.configCookieStore(cookieStore);
			}
			return client;
		}
	}

	/**
	 * 
	 * @param result
	 *            将服务器返回的数据handle出去，函数重载
	 */
	private void sendMsg(String result, int msgWhat) {
		Message msg = handler.obtainMessage(msgWhat, result);
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @param result
	 *            将服务器返回的数据handle出去
	 */
	private void sendMsg(File result, int msgWhat) {
		Message msg = handler.obtainMessage(msgWhat, result);
		handler.sendMessage(msg);
	}
}
