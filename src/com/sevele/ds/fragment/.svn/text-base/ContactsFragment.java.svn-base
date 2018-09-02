package com.sevele.ds.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.decryptstranger.R;
import com.sevele.ds.activity.ChatActivity;
import com.sevele.ds.activity.FindFriendsActivity;
import com.sevele.ds.activity.MainActivity;
import com.sevele.ds.adapter.FriendsListAdatpter;
import com.sevele.ds.app.DsAppManager;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.Result;
import com.sevele.ds.common.HandleResult;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.UserTable;

/**
 * 
 * @author:liu ge
 * @createTime:2015年4月4日
 * @descrption:联系人界面
 */
public class ContactsFragment extends ListFragment implements
		android.view.View.OnClickListener {
	public  static FriendsListAdatpter m_FriendsAdapter; // 显示联系人数据adpter
	private static List<FriendTable> m_friends; // 要显示的好友，
	public  static Boolean isUpdateList; // 是否更新好友列表
	private int int_pos = -1;// 点击的要删除的那个好友
	private HttpAPI m_Http;
	private RelativeLayout mRl_findFriends; // 查找好友
	private updateMsgPageInterface m_Interface;
	private Handler m_Handler;

	
	//删除好友后，通知mainActivity 删除消息界面与该好友的消息记录接口
	public interface updateMsgPageInterface {
		public void removePushMsgItem(int a_pushId);
	}

	@SuppressLint({ "HandlerLeak", "UseValueOf" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isUpdateList == null) {
			isUpdateList = new Boolean(false);
		}
		m_friends = new ArrayList<FriendTable>();
		m_FriendsAdapter = new FriendsListAdatpter(m_friends, getActivity());
		m_Handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				// 网络获取好友成功
				case DsConstant.HANDLER_NET_GETFRIENDS_OK:
					Result re = HandleResult.getResult(msg);
					if (re.isSuccess()) {
						String result = re.getReslut();
						if (result != null) { // 显示，并且写入本地数据库
							final List<FriendTable> fs = BeanJsonConvert
									.jsonToBeanListf(result);
							new Thread() {
								public void run() {
									DsApplication.db.writeFriendsToDb(fs);
								};
							}.start();
							m_friends.addAll(fs);
							m_FriendsAdapter.notifyDataSetChanged();
							Editor edit = DsApplication.mSharedPreferences
									.edit();
							edit.putBoolean(
									String.valueOf(DsApplication.user.getId()),
									false);
							edit.commit();
						}
					}
					break;
				// 网络获取好友失败
				case DsConstant.HANDLER_DB_GERFRIEND_OK:
					@SuppressWarnings("unchecked")
					List<FriendTable> fds = (List<FriendTable>) msg.obj;
					for (FriendTable ft : fds) {
						Log.e("",""+ft.getSid());
						
					}
					m_friends.addAll(fds);
					m_FriendsAdapter.notifyDataSetChanged();
					break;
				// 通知服务器删除好友成功
				case DsConstant.HANDLER_DELETEFD_OK:
					int fid = m_friends.get(int_pos).getSid();
					m_Interface.removePushMsgItem(fid); //删除好友的时候看是否该好有在推送界面有推送消息
					m_friends.remove(int_pos);
					m_FriendsAdapter.notifyDataSetChanged();
					DsApplication.db.deleteFriendFromDb(fid);// 删除好友在本地数据库的信息
					break;
				default:
					break;
				}

			};
		};
		m_Http = new HttpAPI(m_Handler);
		// 是否第一次在该设备上登录
		boolean isFirst = DsApplication.mSharedPreferences.getBoolean(
				String.valueOf(DsApplication.user.getId()), true);
		if (isFirst) {
			// 从网络加载信息
			new Thread() {
				@Override
				public void run() {
					super.run();
					m_Http.httpSendTxt(null, DsApplication.user,
							HttpUrl.GETFRIEND_URL,
							DsConstant.HANDLER_NET_GETFRIENDS_OK, null, null);
				}
			}.start();

		} else {
			// 从数据库加载好友信息
			new Thread() {
				public void run() {
					List<FriendTable> friends = DsApplication.db
							.readFriendFromDb(DsApplication.user.getId());
					
					if (friends != null) {
					 m_Handler.obtainMessage(
								DsConstant.HANDLER_DB_GERFRIEND_OK, friends).sendToTarget();
					}
				};
			}.start();

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof updateMsgPageInterface) {
			m_Interface = (updateMsgPageInterface) activity;
		}
	}

	@Override
	// 返回当前布局给mainActivity
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container,
				false);
		mRl_findFriends = (RelativeLayout) view
				.findViewById(R.id.relative_find_friends);
		mRl_findFriends.setOnClickListener(this);
		setListAdapter(m_FriendsAdapter);
		if (m_FriendsAdapter != null) {
			m_FriendsAdapter.notifyDataSetChanged();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.relative_find_friends) {
			Intent l_intent = new Intent(getActivity(),
					FindFriendsActivity.class);
			startActivity(l_intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// 长按好友列表选择是否删除好友
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int_pos = arg2;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("是否删除该好友")
						.setPositiveButton("是", new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								int fId = m_friends.get(int_pos).getSid();
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("fId", fId);
								m_Http.httpSendTxt(map, DsApplication.user,
										HttpUrl.DELETEFRIEND_URL,
										DsConstant.HANDLER_DELETEFD_OK, null,
										null);
							}
						}).setNegativeButton("否", null);
				builder.create().show();
				return true;
			}
		});
	}

	@Override
	// 点击好友列表，进入聊天界面
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent l_intent = new Intent(getActivity(), ChatActivity.class);
		l_intent.putExtra("fId", m_friends.get(position).getSid());
		startActivity(l_intent);
	}

	public static class addFriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String adderJson = intent.getStringExtra("ft");
			UserTable adder = BeanJsonConvert.jsonToBean(adderJson,
					UserTable.class);
			FriendTable ft = new FriendTable();
			ft.setSid(adder.getId());
			ft.setUserAge(adder.getUserAge());
			ft.setUserGender(adder.getUserGender());
			ft.setUserHometown(adder.getUserHometown());
			ft.setUserHeadPicture(adder.getUserHeadPicture());
			ft.setUserName(adder.getUserName());
			ft.setUser(DsApplication.user);
			if(DsApplication.db.getFriendTable(ft.getSid())==null){
				DsApplication.db.writeFriendToDb(ft);
				m_friends.add(ft);
			}
			if (DsAppManager.getAppManager().currentActivity() instanceof MainActivity) {
				m_FriendsAdapter.notifyDataSetChanged();
			} else {
				isUpdateList = true;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isUpdateList) {
			m_FriendsAdapter.notifyDataSetChanged();
			isUpdateList = false;
		}
	}

	private void release() {
		if (m_FriendsAdapter != null) {
			m_FriendsAdapter = null;
		}

		if (m_friends != null) {
			m_friends = null;
		}
		if (isUpdateList != null) {
			isUpdateList = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
	}

}
