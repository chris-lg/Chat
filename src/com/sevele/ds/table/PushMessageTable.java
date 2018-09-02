package com.sevele.ds.table;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * @author:liu ge
 * @createTime:2015年5月25日
 * @descrption:推送消息列表
 */
public class PushMessageTable {

	@Foreign(column = "userId", foreign = "id")
	private UserTable user; // 外键列表

	@Column(column = "id")
	private int id; // 主键

	@Column(column = "pushTime")
	private long pushTime; // 最新显示时间

	@Column(column = "msgs")
	private int msgs; // 未读消息条数
	
	@Column(column = "pusherId")
	private int pusherId; // 推送人id

	@Column(column = "pusherName")
	private String pusherName; //推送人姓名

	@Column(column = "fPicPath")
	private String fPicPath; //推送人头像路径

	@Column(column = "pushContent")
	private String pushContent; // 消息内容
	
	@Transient
	private List<MsgBeanTable> lists=new ArrayList<MsgBeanTable>();//未读的消息内容
	
	

	public List<MsgBeanTable> getLists() {
		return lists;
	}

	public void setLists(List<MsgBeanTable> lists) {
		this.lists = lists;
	}

	public UserTable getUser() {
		return user;
	}

	public void setUser(UserTable user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPushTime() {
		return pushTime;
	}

	public void setPushTime(long pushTime) {
		this.pushTime = pushTime;
	}

	public int getMsgs() {
		return msgs;
	}

	public void setMsgs(int msgs) {
		this.msgs = msgs;
	}

	public int getPusherId() {
		return pusherId;
	}

	public void setPusherId(int pusherId) {
		this.pusherId = pusherId;
	}

	public String getPusherName() {
		return pusherName;
	}

	public void setPusherName(String pusherName) {
		this.pusherName = pusherName;
	}

	public String getfPicPath() {
		return fPicPath;
	}

	public void setfPicPath(String fPicPath) {
		this.fPicPath = fPicPath;
	}

	public String getPushContent() {
		return pushContent;
	}

	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}

	@Override
	public String toString() {
		return "PushMessageTable [user=" + user + ", id=" + id + ", pushTime="
				+ pushTime + ", msgs=" + msgs + ", pusherId=" + pusherId
				+ ", pusherName=" + pusherName + ", fPicPath=" + fPicPath
				+ ", pushContent=" + pushContent + "]";
	}


}
