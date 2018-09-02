package com.sevele.ds.table;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

/**
 * @author:liu ge
 * @createTime:2015年3月19日
 * @descrption:好友类，对应数据库中的friends_list表
 */
@Table(name = "friends_list")
// 建议加上注解， 混淆后表名不受影响
public class FriendTable extends EntityBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FriendTable() {
	}

	public FriendTable(int fid,String fName,String fGender,int fAge,String fHomeTown,String fHeadpic) {
		id=fid;
		this.userAge=fAge;
		this.userCount=fName;
		this.userGender=fGender;
		this.userHeadPicture=fHeadpic;
		this.userHometown=fHomeTown;
	}
	
	private int id;// 本地数据库的id值，默认自增长
	
	@Column(column = "sid")
	private int sid; // 好友在服务器上的id

	@Foreign(column = "userId", foreign = "id")
	private UserTable user;
   

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	@Override
	public String toString() {
		return "FriendTable [id=" + id + ", sid=" + sid + ", user=" + user
				+ "]";
	}

}
