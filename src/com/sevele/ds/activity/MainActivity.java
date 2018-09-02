package com.sevele.ds.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsAppManager;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.Result;
import com.sevele.ds.common.HandleResult;
import com.sevele.ds.ddpush.OnlineService;
import com.sevele.ds.fragment.ContactsFragment;
import com.sevele.ds.fragment.ContactsFragment.updateMsgPageInterface;
import com.sevele.ds.fragment.FindFragment;
import com.sevele.ds.fragment.MessageFragment;
import com.sevele.ds.fragment.MessageFragment.updageFriendPagerInterface;
import com.sevele.ds.fragment.UserFragment;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;

/**
 * @author:Maozhiqi
 * @createTime:2015年3月19日
 * @descrption:应用的主界面，
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends FragmentActivity implements OnClickListener,
		updateMsgPageInterface, updageFriendPagerInterface {
	
	


	private ViewPager m_ViewPager;// ViewPager组件，用于装载Fragment实现主界面
	private long lng_firstTime = 0;// 按返回键次数
	private int int_CurrentItem = 0;// 记录上一个Item
	private FragmentPagerAdapter m_Adapter;// FragmentPager适配器
	private ArrayList<Fragment> m_Fragments = new ArrayList<Fragment>();

	private ImageView mIv_TabBtnMeassage;// 消息tab
	private RelativeLayout mRl_TabMeassage;
	private TextView mTv_TabTxtMeassage;

	private ImageView mIv_TabBtnContacts;// 联系人列表tab
	private RelativeLayout mRl_TabContacts;
	private TextView mTv_TabTxtContacts;

	private ImageView mIv_TabBtnSquare;// 发现tab
	private RelativeLayout mRl_TabSquare;
	private TextView mTv_TabTxtSquare;

	private ImageView mIv_TabBtnUser;// 个人中心tab
	private RelativeLayout mRl_TabUser;
	private TextView mTv_TabTxtUser;

	private MessageFragment m_MessageFragment;// 消息Fragment
	private ContactsFragment m_ContactsFragment;// 联系人Fragment
	private FindFragment m_FindFragment;// 发现Fragment
	private UserFragment m_UserFragment;// 个人中心Fragment

	private Handler m_Handler;
	private UserTable m_UserTable;//用户个人信息
	private boolean bln_isFirst;//判断是否为第一次进入应用的标志
	private HttpAPI m_HttpAPI;

	private ServiceConnection m_Servconn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DsAppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_main);

		m_Servconn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName arg0) {

			}

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {

			}
		};

		/** 登陆成功，启动DDpush服务 */
		if (Integer.valueOf(DsApplication.user.getId()) != null) {
			Intent intent = new Intent(this, OnlineService.class);
			bindService(intent, m_Servconn, Service.BIND_AUTO_CREATE);
		}
		// 获取个人信息
		m_Handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case DsConstant.HANDLER_NET_OK:
					Result re = HandleResult.getResult(msg);
					String userInfo = re.getReslut();
					m_UserTable = BeanJsonConvert.jsonToBean(userInfo,
							UserTable.class);
					// 将用户基本信息数据写入DsApplication.user
					addDsUser(m_UserTable);
					// 更新本地数据库
					DsApplication.db.writeUserInfo(m_UserTable);
					// 将用户头像存入手机存储器
					if (m_UserTable.getUserHeadPicture() != null) {

						AdduserPicture(m_UserTable.getUserHeadPicture());
					}
					Editor edit = DsApplication.mSharedPreferences.edit();
					edit.putBoolean(
							String.valueOf(DsApplication.user.getId() + "info"),
							false);
					edit.commit();
					break;
				default:
					break;
				}
			}
		};

		initView();// 界面初始化
		m_Adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return m_Fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return m_Fragments.get(arg0);
			}
		};

		m_ViewPager.setAdapter(m_Adapter);
		m_ViewPager.setOffscreenPageLimit(4);
		m_ViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				resetTabBtn();// 初始化TabBtn状态
				switch (position) {
				case 0:
					mIv_TabBtnMeassage
							.setImageResource(R.drawable.icon_meassage_sel);
					mRl_TabMeassage.setBackgroundColor(MainActivity.this
							.getResources().getColor(R.color.white));
					mTv_TabTxtMeassage.setTextColor(MainActivity.this
							.getResources().getColor(R.color.blue));
					break;
				case 1:
					mIv_TabBtnContacts
							.setImageResource(R.drawable.icon_selfinfo_sel);
					mRl_TabContacts.setBackgroundColor(MainActivity.this
							.getResources().getColor(R.color.white));
					mTv_TabTxtContacts.setTextColor(MainActivity.this
							.getResources().getColor(R.color.blue));
					break;
				case 2:
					mIv_TabBtnSquare
							.setImageResource(R.drawable.icon_square_sel);
					mRl_TabSquare.setBackgroundColor(MainActivity.this
							.getResources().getColor(R.color.white));
					mTv_TabTxtSquare.setTextColor(MainActivity.this
							.getResources().getColor(R.color.blue));
					break;
				case 3:
					mIv_TabBtnUser.setImageResource(R.drawable.icon_home_sel);
					mRl_TabUser.setBackgroundColor(MainActivity.this
							.getResources().getColor(R.color.white));
					mTv_TabTxtUser.setTextColor(MainActivity.this
							.getResources().getColor(R.color.blue));
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		bln_isFirst = DsApplication.mSharedPreferences.getBoolean(
				String.valueOf(DsApplication.user.getId() + "info"), true);
		if (bln_isFirst) {
			// 网络请求个人信息
			m_HttpAPI = new HttpAPI(m_Handler);
			m_UserTable = DsApplication.db.getUserTable(DsApplication.user
					.getId());
			String picString = m_UserTable.getUserHeadPicture();
			if (picString == null || !(new File(picString).exists())) {
				m_HttpAPI.httpSendTxt(null, m_UserTable,
						HttpUrl.GET_USERINFO_URL, DsConstant.HANDLER_NET_OK,
						null, null);
			}
		} else {
			// 本地数据库读取
			m_UserTable = DsApplication.db.getUserTable(DsApplication.user
					.getId());
			LogUtil.LogTest("****本地数据库读取用户信息**");
			addDsUser(m_UserTable);
			AdduserPicture(m_UserTable.getUserHeadPicture());
		}
	}

	/**
	 * 将用户基本信息数据写入DsApplication.user
	 * 
	 * @param user
	 */
	private void addDsUser(UserTable user) {
		DsApplication.user.setUserName(user.getUserName());
		DsApplication.user.setUserAge(user.getUserAge());
		DsApplication.user.setUserHeadPicture(user.getUserHeadPicture());
		DsApplication.user.setUserGender(user.getUserGender());
		DsApplication.user.setUserHometown(user.getUserHometown());
	}

	/**
	 * 将用户头像存入手机存储器
	 * 
	 * @param urlPath
	 */
	private void AdduserPicture(String urlPath) {
		if (urlPath == null) {
			return;
		}
		Bitmap bp = DsApplication.imgDownLoader.downloadImage(urlPath);
		if (bp != null) {
			DsApplication.uBitmap = bp;
		}
	}

	/**
	 * 初始化TabBtn状态
	 */
	protected void resetTabBtn() {
		mRl_TabMeassage.setBackgroundColor(this.getResources().getColor(
				R.color.frenchgrey));
		mRl_TabContacts.setBackgroundColor(this.getResources().getColor(
				R.color.frenchgrey));
		mRl_TabSquare.setBackgroundColor(this.getResources().getColor(
				R.color.frenchgrey));
		mRl_TabUser.setBackgroundColor(this.getResources().getColor(
				R.color.frenchgrey));
		mIv_TabBtnMeassage.setImageResource(R.drawable.icon_meassage_nor);
		mIv_TabBtnContacts.setImageResource(R.drawable.icon_selfinfo_nor);
		mIv_TabBtnSquare.setImageResource(R.drawable.icon_square_nor);
		mIv_TabBtnUser.setImageResource(R.drawable.icon_home_nor);
		mTv_TabTxtMeassage.setTextColor(this.getResources().getColor(
				R.color.white));
		mTv_TabTxtContacts.setTextColor(this.getResources().getColor(
				R.color.white));
		mTv_TabTxtSquare.setTextColor(this.getResources().getColor(
				R.color.white));
		mTv_TabTxtUser
				.setTextColor(this.getResources().getColor(R.color.white));
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		m_ViewPager = (ViewPager) findViewById(R.id.viewPager);
		mIv_TabBtnMeassage = (ImageView) findViewById(R.id.btn_meassage);
		mRl_TabMeassage = (RelativeLayout) findViewById(R.id.meassage_relative);
		mTv_TabTxtMeassage = (TextView) findViewById(R.id.txt_message);

		mIv_TabBtnContacts = (ImageView) findViewById(R.id.btn_contacts);
		mRl_TabContacts = (RelativeLayout) findViewById(R.id.contacts_relative);
		mTv_TabTxtContacts = (TextView) findViewById(R.id.txt_contacts);

		mIv_TabBtnSquare = (ImageView) findViewById(R.id.btn_square);
		mRl_TabSquare = (RelativeLayout) findViewById(R.id.square_relative);
		mTv_TabTxtSquare = (TextView) findViewById(R.id.txt_square);

		mIv_TabBtnUser = (ImageView) findViewById(R.id.btn_user);
		mRl_TabUser = (RelativeLayout) findViewById(R.id.user_relative);
		mTv_TabTxtUser = (TextView) findViewById(R.id.txt_user);

		mRl_TabMeassage.setOnClickListener(this);
		mRl_TabContacts.setOnClickListener(this);
		mRl_TabSquare.setOnClickListener(this);
		mRl_TabUser.setOnClickListener(this);

		m_MessageFragment = new MessageFragment();
		m_ContactsFragment = new ContactsFragment();
		m_FindFragment = new FindFragment();
		m_UserFragment = new UserFragment();

		m_Fragments.add(m_MessageFragment);
		m_Fragments.add(m_ContactsFragment);
		m_Fragments.add(m_FindFragment);
		m_Fragments.add(m_UserFragment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.meassage_relative:
			m_ViewPager.setCurrentItem(0);
			break;
		case R.id.contacts_relative:
			m_ViewPager.setCurrentItem(1);
			break;
		case R.id.square_relative:
			m_ViewPager.setCurrentItem(2);
			break;
		case R.id.user_relative:
			m_ViewPager.setCurrentItem(3);
			break;
		default:
			m_ViewPager.setCurrentItem(0);
			break;
		}
	}

	/**
	 * 监听返回--是否退出程序
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - lng_firstTime > 2000) {
				UIHelper.ToastMessage("再按一次退出程序");
				lng_firstTime = secondTime;
				return true;
			} else {
				moveTaskToBack(true);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_ViewPager.setCurrentItem(int_CurrentItem);
	}

	@Override
	protected void onPause() {
		super.onPause();
		int_CurrentItem = m_ViewPager.getCurrentItem();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(m_Servconn);
		Intent in = new Intent();// 结束后台服务
		in.setAction("android.intent.action.EXIT_DDPUSHSERVICE");
		sendBroadcast(in);
	}

	@SuppressWarnings("static-access")
	@Override
	public void removePushMsgItem(int fid) {
		for (int i = 0; i < m_MessageFragment.m_msgs.size(); i++) {
			if (m_MessageFragment.m_msgs.get(i).getPusherId() == fid) {
				m_MessageFragment.m_msgs.remove(i);
				m_MessageFragment.m_pushAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void addFriend(FriendTable ft) {
		ContactsFragment.m_FriendsAdapter.addFriendToList(ft);
	}
}
