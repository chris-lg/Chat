package com.sevele.ds.common;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.amap.api.mapcore2d.en;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.PushMessageTable;
import com.sevele.ds.table.StrangerTable;
import com.sevele.ds.table.UserTable;

/**
 * @author:liu ge
 * @createTime:2015年4月7日
 * @descrption:相关数据库的操作
 */
public class DbOperation {

	private DbUtils dbUtil = DbUtils.create(DsApplication.mContext);

	private DbOperation() { // 单例模式
	};

	private static DbOperation hand;

	public static DbOperation getDbInstance() {
		if (hand == null) {
			hand = new DbOperation();
		}
		return hand;
	}

	// 若是第一进入应用，应该将自己的信息写进数据库
	public void writeUserInfo(UserTable user) {
		try {
			dbUtil.saveOrUpdate(user);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 通过用户帐号 查找用户信息
	 * 
	 * @param userCount
	 * @return
	 */
	public UserTable getUserTable(String userCount) {
		UserTable ut = null;
		try {
			ut = dbUtil.findFirst(Selector.from(UserTable.class).where(
					"userCount", "=", userCount));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return ut;

	}

	/**
	 * 通过用户帐号 查找用户信息
	 * 
	 * @param userCount
	 * @return
	 */
	public UserTable getUserTable(int sid) {
		UserTable ut = null;
		try {
			ut = dbUtil.findFirst(Selector.from(UserTable.class).where("id",
					"=", sid));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return ut;

	}

	/**
	 * 获取本地数据库中某一个好友
	 * 
	 * @param sid
	 *            注意：好友在服务器上的id
	 * @return
	 */
	public FriendTable getFriendTable(int sid) {
		FriendTable ft = null;
		try {
			ft = dbUtil.findFirst(Selector.from(FriendTable.class)
					.where("sid", "=", sid)
					.and("userId", "=", DsApplication.user.getId()));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return ft;

	}

	/**
	 * 根据好友昵称获取本地数据库中某一个好友
	 * 
	 * @param fName
	 * @return
	 */
	public FriendTable getFriendTable(String fName) {
		FriendTable ft = null;
		try {
			ft = dbUtil.findFirst(Selector.from(FriendTable.class).where(
					"userName", "=", fName));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return ft;

	}

	/**
	 * 读取数据库中的好友(包括好友的信息)
	 */
	public List<FriendTable> readFriendFromDb(int userId) {
		try {
			List<FriendTable> friends = dbUtil.findAll(Selector.from(
					FriendTable.class).where("userId", "=", userId));
			if (friends != null && friends.size() != 0) {
				return friends;
			}
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return null;

	}

	/**
	 * 将某条消息记录存入本地数据库中
	 * 
	 * @param mbt
	 */
	public void saveChatRecord(MsgBeanTable mbt) {
		try {
			if (mbt != null) {
				dbUtil.saveOrUpdate(mbt);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 采用分页获取聊天记录
	 * 
	 * @param uId
	 * @param fId
	 * @param pager
	 *            当前聊天页数
	 * @return
	 */
	public List<MsgBeanTable> getChatRecordHistory(int uId, int fId, int pager) {
		try {
			List<MsgBeanTable> chats = dbUtil.findAll(Selector
					.from(MsgBeanTable.class).where("uId", "=", uId)
					.and("fId", "=", fId).limit((pager + 1) * 10)
					.orderBy("chatTime", true));
			return chats;
		} catch (DbException e) {
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * 从数据库中读取指定条数消息记录
	 * 
	 * @param uId
	 * @param fId
	 * @param num
	 * @return
	 */
	public List<MsgBeanTable> getChatNumRecordHistory(int uId, int fId, int num) {
		try {
			List<MsgBeanTable> chats = dbUtil.findAll(Selector
					.from(MsgBeanTable.class).where("uId", "=", uId)
					.and("fId", "=", fId).limit(num).orderBy("chatTime", true));
			return chats;
		} catch (DbException e) {
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * 第一次从该手机登录的时候，从服务器上读取好友信息，放入数据库中
	 */
	public void writeFriendsToDb(List<FriendTable> friends) {
		for (FriendTable fb : friends) {
			writeFriendToDb(fb);
		}
	}

	/**
	 * 将某个好友写进数据库
	 * 
	 * @param friend
	 */
	public void writeFriendToDb(FriendTable friend) {
		friend.setUser(DsApplication.user);
		try {
			FriendTable ft = dbUtil.findFirst(Selector.from(FriendTable.class)
					.where("userId", "=", DsApplication.user.getId())
					.and("sid", "=", friend.getSid()));
			if (ft == null) {
				dbUtil.saveOrUpdate(friend);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fId
	 *            好友在服务器上的id
	 * @param uId
	 *            用户在服务器上的id
	 * @return
	 */
	public boolean deleteFriendFromDb(int sid) {
		try {
			dbUtil.delete(FriendTable.class, WhereBuilder.b("sid", "=", sid)
					.and("userId", "=", DsApplication.user.getId()));
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 从本地数据库中读取陌生人信息,
	 * 
	 * @param userId
	 *            用户id
	 */
	public List<StrangerTable> readStrangerFormDb(int userId) {
		try {
			List<StrangerTable> strangers = dbUtil.findAll(Selector.from(
					StrangerTable.class).where("userId", "=", userId));
			if (strangers != null && strangers.size() != 0)
				return strangers;

		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将多个陌生人添加进数据库
	 */
	public void addStrangersToDb(List<StrangerTable> strangers) {
		for (StrangerTable st : strangers) {
			writeStrangerToDb(st);
		}
	}

	/**
	 * 将陌生人添加进数据库
	 */
	public void writeStrangerToDb(StrangerTable st) {
		st.setUser(DsApplication.user);
		try {
			StrangerTable stb = dbUtil.findFirst(Selector
					.from(StrangerTable.class)
					.where("userId", "=", DsApplication.user.getId())
					.and("sid", "=", st.getSid()));
			if (stb == null) {
				dbUtil.saveOrUpdate(st);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取到某个陌生人信息
	 * 
	 * @param sid
	 *            陌生人在服务器上的id
	 * @return
	 */

	public StrangerTable getStrangerFromDb(int sid) {
		StrangerTable st;
		try {
			st = dbUtil.findFirst(Selector.from(StrangerTable.class)
					.where("sid", "=", sid)
					.and("userId", "=", DsApplication.user.getId()));
			return st;
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除单个陌生人数据
	 * 
	 * @param st
	 */
	public void DeleteStrangersfromDb(StrangerTable st) {
		st.setUser(DsApplication.user);
		try {
			dbUtil.delete(st);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void writePushMsg(PushMessageTable pmt) {
		pmt.setUser(DsApplication.user);
		try {
			dbUtil.saveOrUpdate(pmt);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public void writePushMsgs(List<PushMessageTable> pmts) {
		for (PushMessageTable pmt : pmts) {
			writePushMsg(pmt);
		}
	}

	public List<PushMessageTable> getPushMsgs() {
		try {
			return dbUtil.findAll(Selector.from(PushMessageTable.class)
					.where("userId", "=", DsApplication.user.getId())
					.orderBy("pushTime", true));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void deletePushMsg(PushMessageTable pmt) {
		try {
			dbUtil.delete(pmt);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void deletePushMsgs(List<PushMessageTable> pmts) {
		for (PushMessageTable pmt : pmts) {
			deletePushMsg(pmt);
		}
	}

}
