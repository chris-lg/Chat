package com.sevele.ds.app;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * @author:liu ge
 * @createTime:2015年3月18日
 * @descrption:应用级的常量在此处申明
 */
public class DsConstant {

	/** 用户信息 **/
	public static final String USER_ID = "userId";
	public static final String USER_SERVICEID = "userServiceId";
	public static final String USER_NAME = "userName";
	public static final String USER_COUNT = "userCount";
	public static final String USER_PASSWORD = "userPassword";
	public static final String USER_GENDER = "userGender";
	public static final String USER_HOMETOWN = "userHometown";
	public static final String USER_AGE = "userAge";
	public static final String USER_PHONENUMBER = "userPhoneNumber";
	public static final String USER_JESSIONID = "JESSIONID";
	public static final String USER_RESULT = "result";

	/** handler 处理结果 **/
	public static final int HANDLER_NET_OK = 0x01; // 服务器成功返回数据

	public static final int HANDLER_NET_FAIL = 0x02; // 服务器已经执行，但是操做不和法

	public static final int HANDLER_NET_LINK_FAIL = 0x03; // 没有网络或者连接服务器失败

	public static final int HANDLER_NET_LOGIN_OK = 0x04; // 登录成功

	public static final int HANDLER_NET_REGIST_OK = 0x05; // 注册成功

	public static final int HANDLER_NET_GETTOKEN = 0x06; // 获取到了token

	public static final int HANDLER_DB_GERFRIEND_OK = 0x07; // 从本地数据库获取好友信息

	public static final int HANDLER_NET_GETFRIENDS_OK = 0x08; // 获取朋友信息成功

	public static final int HANDLER_NET_GETFRIENDS_FAIL = 0x09; // 获取朋友信息失败

	public static final int HANDLER_QINIU_NET_DOWNLOAD_OK = 0x010; // 从七牛服务器下载成功
	public static final int HANDLER_QINIU_NET_DOWNLOAD_PIC_OK = 0x080; // 从七牛服务器图片下载成功
	public static final int HANDLER_QINIU_NET_DOWNLOAD_VOICE_OK = 0x090; // 从七牛服务器语音下载成功

	public static final int HANDLER_QINIU_NET_DOWNLOAD_FAIL = 0x020; // 从七牛服务器下载失败

	public static final int HANDLER_QINIU_NET_UPLOAD_PIC_OK = 0x030; // 上传到七牛服务器成功
	public static final int HANDLER_QINIU_NET_UPLOAD_VOICE_OK = 0x100; // 上传到七牛服务器成功

	public static final int HANDLER_QINIU_NET_UPLOAD_FAIL = 0x040; // 上传到七牛服务器失败

	public static final int HANDLER_TXTMSG_OK = 0x050; // 文字消息推送成功

	public static final int HANDLER_TXTMSG_FAIL = 0x055; // 文字消息推送失败
	public static final int HANDLER_PICMSG_OK = 0x060; // 图片消息推送成功

	public static final int HANDLER_VOICEMSG_OK = 0x070; // 语音消息推送成功

	public static final int HANDLER_DELETEFD_OK = 0x080; // 删除好友成功

	/** 聊天的消息类型 **/

	public static final int MSG_TO = 0; // 用户自己发送的消息
	public static final int MSG_COME = 1; // 用户接受到消息

	public static final int TXT_MSG = 2; // 文本消息
	public static final int VOICE_MSG = 3; // 语音消息
	public static final int PIC_MSG = 4; // 图片消息
	public static final int ADD_FRIEND_MSG = 5; // 添加好友消息

	public static final int GAME_HELP = 6; // 请求游戏帮助

	public static final int DES_OK = 7; // 解密成功

	public static final int HELP_FD_OK = 8; // 帮助好友成功

	public static final int VALUE_LEFT_TEXT = 0;
	public static final int VALUE_LEFT_IMAGE = 1;
	public static final int VALUE_LEFT_AUDIO = 2;
	public static final int VALUE_RIGHT_TEXT = 3;
	public static final int VALUE_RIGHT_IMAGE = 4;
	public static final int VALUE_RIGHT_AUDIO = 5;

	/** 下载或者推送状态 **/
	public static final int NO_PUSH_OR_LOAD = 0; // 还没经行下载或者推送的消息
	public static final int LOAING_OR_PUSHING = 1; // 正在下载或者推送
	public static final int PUSH_LOAD_FAIL = 2; // 下载或者推送失败
	public static final int PUSH_LOAD_OK = 3; // 下载或者推送成功
	/** 文件路径 **/
	public static final String ROOT = Environment.getExternalStorageDirectory()
			.toString();

	/** 七牛服务器空间域名 **/
	public static final String MY_QINIU = "http://7xip0k.com1.z0.glb.clouddn.com/";
	public static final String SMALL_PIC = "?imageView2/1/w/200/h/200/interlace/1";// 缩略图大小

	public static final String AUDIO_ROOT = DsApplication.audioFile
			.getAbsolutePath() + File.separator;
	public static final String IMG_ROOT = DsApplication.imgFile
			.getAbsolutePath() + File.separator;
}
