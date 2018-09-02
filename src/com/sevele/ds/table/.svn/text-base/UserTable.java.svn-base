package com.sevele.ds.table;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * @author:liu ge
 * @createTime:2015年3月19日
 * @descrption:用户信息类，对应数据库中的user表,在域中使用注解和静态字段都不会存入数据库中
 * */
@Table(name ="user")
// 建议加上注解， 混淆后表名不受影响 ,用户表中的主键id就是用户在服务器上的id
public class UserTable extends EntityBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NoAutoIncrement
	public int id; // 用户，好友，陌生人在本地数据库的id都是和他们在数据库上的id对应的，就是数据库上的id

	@Column(column = "userPassword")
	private String userPassword;// 用户密码
	
    @Transient //该注解不会让该字段存入数据库
	private  String jessionId;

	public  String getJessionId() {
		return jessionId;
	}

	public  void setJessionId(String jsessionId) {
		this.jessionId = jsessionId;
	}

	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserTable [id=" + id + ", userPassword=" + userPassword
				+ ", jessionId=" + jessionId + "]";
	}
	

}
