package com.sevele.ds.table;

import java.sql.Date;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

/**
 * @author:liu ge
 * @createTime:2015年3月31日
 * @descrption:存放摇一摇的陌生人数据库
 */
@Table(name = "stranger")
public class StrangerTable extends EntityBase {

	private int _id;// 本地数据库的id值，默认自增长

	@Column(column = "sid")
	private int sid; // 好友在服务器上的id

	@Column(column = "strangerGetTime")
	private String shakedTime;// 该用用户被摇到的时间

	@Column(column = "strangerDistance")
	private Double distance;// 用户和陌生人之间的距离

	@Column(column = "strangerIsAccess")
	private boolean strangerIsAccess;// 用户是否已经解密陌生人

	@Foreign(column = "userId", foreign = "id")
	private UserTable user;

	@Transient
	private Double strangerLongItude;
	@Transient
	private Double strangerLatItude;
	
	

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getShakedTime() {
		return shakedTime;
	}

	public void setShakedTime(String shakedTime) {
		this.shakedTime = shakedTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public boolean isStrangerIsAccess() {
		return strangerIsAccess;
	}

	public void setStrangerIsAccess(boolean strangerIsAccess) {
		this.strangerIsAccess = strangerIsAccess;
	}

	public UserTable getUser() {
		return user;
	}

	public void setUser(UserTable user) {
		this.user = user;
	}

	public Double getStrangerLongItude() {
		return strangerLongItude;
	}

	public void setStrangerLongItude(Double strangerLongItude) {
		this.strangerLongItude = strangerLongItude;
	}

	public Double getStrangerLatItude() {
		return strangerLatItude;
	}

	public void setStrangerLatItude(Double strangerLatItude) {
		this.strangerLatItude = strangerLatItude;
	}

	@Override
	public String toString() {
		return "StrangerTable [_id=" + _id + ", sid=" + sid + ", shakedTime="
				+ shakedTime + ", distance=" + distance + ", strangerIsAccess="
				+ strangerIsAccess + ", user=" + user + ", strangerLongItude="
				+ strangerLongItude + ", strangerLatItude=" + strangerLatItude
				+ "]";
	}


}
