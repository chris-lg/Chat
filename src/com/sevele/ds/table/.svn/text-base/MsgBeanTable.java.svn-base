package com.sevele.ds.table;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.sevele.ds.app.DsConstant;

/**
 * @author:liu ge
 * @createTime:2015年3月28日
 * @descrption:聊天内容
 */

@Table(name = "chat_content")
public class MsgBeanTable implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Foreign(column = "uId", foreign = "id")
	public UserTable user; // 用户自己的id; //用户id 
	
	@Foreign(column = "fId", foreign = "id")
	public FriendTable friend; //朋友的id就是本地数据库朋友的id 而不是服务器id
	
	@Transient
	private int sid;//推送人id  不写入数据库
	
	@Column(column = "id")
	private int id;

	@Column(column = "chatTime")
	private long chatTime;

	@Column(column = "msgContent")
	private String msgContent; // 分别保存的是 文本聊天记录，或者文件路径uri

	@Column(column = "isUserMsg")
	private int isUserMsg;    // 0代表自己发送的 ，1代表接收到的消息

	@Column(column = "msgType")
	private int msgType;   // 通过它判断是那种类型的信息，2代表文本消息，3代表语音消息，4代表图片消息 /5代表新闻

	@Column(column = "showType")
	private int showType;// 用于显示的类型

	@Column(column = "loadState")
	private int loadState;//下载状态 ：还没下载/推送 操作过   下载/推送失败，下载/推送成功，下载/推送中。。 0 1 2 3  //在更新消息界面的时候为和好友某次聊天的消息条数
	
	@Column(column = "voiceTime")
	private int voiceTime;// 语音时长
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public int getLoadState() {
		return loadState;
	}

	public void setLoadState(int loadState) {
		this.loadState = loadState;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}


	public UserTable getUser() {
		return user;
	}

	public void setUser(UserTable user) {
		this.user = user;
	}

	public FriendTable getFriend() {
		return friend;
	}

	public void setFriend(FriendTable friend) {
		this.friend = friend;
	}


	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public long getChatTime() {
		return chatTime;
	}

	public void setChatTime(long chatTime) {
		this.chatTime = chatTime;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public int getIsUserMsg() {
		return isUserMsg;
	}

	public void setIsUserMsg(int isUserMsg) {
		this.isUserMsg = isUserMsg;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public void putShowtype() {
		if (isUserMsg == DsConstant.MSG_TO) {
			switch (msgType) {
			case DsConstant.TXT_MSG:
				setShowType(DsConstant.VALUE_RIGHT_TEXT);
				break;
			case DsConstant.VOICE_MSG:
				setShowType(DsConstant.VALUE_RIGHT_AUDIO);
				break;
			case DsConstant.PIC_MSG:
				setShowType(DsConstant.VALUE_RIGHT_IMAGE);
				break;
			default:
				break;
			}
		} else if (isUserMsg == DsConstant.MSG_COME) {
			switch (msgType) {
			case DsConstant.TXT_MSG:
				setShowType(DsConstant.VALUE_LEFT_TEXT);
				break;
			case DsConstant.VOICE_MSG:

				setShowType(DsConstant.VALUE_LEFT_AUDIO);

				break;
			case DsConstant.PIC_MSG:
				setShowType(DsConstant.VALUE_LEFT_IMAGE);
				break;
			default:
				break;
			}

		}

	}

	@Override
	public String toString() {
		return "MsgBeanTable [user=" + user + ", friend=" + friend + ", sid="
				+ sid + ", id=" + id + ", chatTime=" + chatTime
				+ ", msgContent=" + msgContent + ", isUserMsg=" + isUserMsg
				+ ", msgType=" + msgType + ", showType=" + showType
				+ ", loadState=" + loadState + ", voiceTime=" + voiceTime + "]";
	}


}
