package com.sevele.ds.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

/**
 * @author:liu ge
 * @createTime:2015年3月20日
 * @descrption:聊天信息类，对应数据库中的chat_content表
 */
@Table(name = "chat_content")
class ChatContentTable {

	@Foreign(column = "userId", foreign = "id")
	public UserTable user; // 用户自己的id

	@Foreign(column = "firendId", foreign = "id")
	public FriendTable friend; // 好友的id

	@Column(column = "voiceMsg")
	private String voiceMsg;// 语音信息

	@Column(column = "txtMsg")
	private String txtMsg;// 文本信息

	@Column(column = "pictureMsg")
	private String pictureMsg;// 图片信息

	@Column(column = "chatTime")
	private java.sql.Date chatTime;

	@Column(column = "whoWrite")
	// 是否是用户自己的信息
	private int isUserMsg;

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

	public String getVoiceMsg() {
		return voiceMsg;
	}

	public void setVoiceMsg(String voiceMsg) {
		this.voiceMsg = voiceMsg;
	}

	public String getTxtMsg() {
		return txtMsg;
	}

	public void setTxtMsg(String txtMsg) {
		this.txtMsg = txtMsg;
	}

	public String getPictureMsg() {
		return pictureMsg;
	}

	public void setPictureMsg(String pictureMsg) {
		this.pictureMsg = pictureMsg;
	}

	public java.sql.Date getChatTime() {
		return chatTime;
	}

	public void setChatTime(java.sql.Date chatTime) {
		this.chatTime = chatTime;
	}

	public int getIsUserMsg() {
		return isUserMsg;
	}

	public void setIsUserMsg(int isUserMsg) {
		this.isUserMsg = isUserMsg;
	}

	@Override
	public String toString() {
		return "ChatContentTable [user=" + user + ", friend=" + friend
				+ ", voiceMsg=" + voiceMsg + ", txtMsg=" + txtMsg
				+ ", pictureMsg=" + pictureMsg + ", chatTime=" + chatTime
				+ ", isUserMsg=" + isUserMsg + "]";
	}

}
