/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sevele.ds.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

/**
 * 
 * @author:liu ge
 * @createTime:2015年3月31日
 * @descrption:
 */
public abstract class EntityBase {

	// @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
	// @NoAutoIncrement
	// public int id; // 用户，好友，陌生人在本地数据库的id都是和他们在数据库上的id对应的，就是数据库上的id

	@Column(column = "userCount")
	public String userCount;// 用户昵称

	@Column(column = "gameRank",defaultValue="6")
	public int gameRank;// 用户密码

	@Column(column = "userName")
	public String userName;// 用户昵称

	@Column(column = "userGender")
	public String userGender;// 用户性别

	@Column(column = "userAge")
	public int userAge;// 用户年龄

	@Column(column = "userHeadPicture")
	public String userHeadPicture; // 用户头像路径

	@Column(column = "userHometown")
	public String userHometown;// 用户来自那里
	
	public int getGameRank() {
		return gameRank;
	}

	public void setGameRank(int gameRank) {
		this.gameRank = gameRank;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getUserHeadPicture() {
		return userHeadPicture;
	}

	public void setUserHeadPicture(String userHeadPicture) {
		this.userHeadPicture = userHeadPicture;
	}

	public String getUserHometown() {
		return userHometown;
	}

	public void setUserHometown(String userHometown) {
		this.userHometown = userHometown;
	}

	@Override
	public String toString() {
		return "EntityBase [userCount=" + userCount + ", gameRank=" + gameRank
				+ ", userName=" + userName + ", userGender=" + userGender
				+ ", userAge=" + userAge + ", userHeadPicture="
				+ userHeadPicture + ", userHometown=" + userHometown + "]";
	}


}