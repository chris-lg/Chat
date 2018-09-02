package com.sevele.ds.bean;

/**
 * @author:liu ge
 * @createTime:2015年3月20日
 * @descrption:用户注册信息实体类
 */
public class UserRegistInfo {
	private String userName;
	private String userPwd;
	private String userPhoneNumber;

	private String userCount;

	private String userHeadpic;

	private String userGender;
	private int userAge;

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getUserHeadpic() {
		return userHeadpic;
	}

	public void setUserHeadpic(String userHeadpic) {
		this.userHeadpic = userHeadpic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	@Override
	public String toString() {
		return "UserRegistInfo [userName=" + userName + ", userPwd=" + userPwd
				+ ", userPhoneNumber=" + userPhoneNumber + ", userCount="
				+ userCount + ", userHeadpic=" + userHeadpic + ", userGender="
				+ userGender + ", userAge=" + userAge + "]";
	}

}
