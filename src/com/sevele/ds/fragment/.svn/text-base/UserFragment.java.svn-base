package com.sevele.ds.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.activity.AboutActivity;
import com.sevele.ds.activity.LoginActivity;
import com.sevele.ds.activity.MainActivity;
import com.sevele.ds.activity.SettingActivity;
import com.sevele.ds.activity.UserInfoActivity;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.view.CircleImageView;

/**
 * @author:liu ge
 * @createTime:2015年3月19日
 * @descrption:用户界面
 */
@SuppressLint("HandlerLeak")
public class UserFragment extends Fragment implements OnClickListener {
	private CircleImageView m_ImguserPicture;// 用户头像
	private TextView mTv_Username;// 用户昵称
	private ImageView mIv_Usergender;// 用户性别
	private RelativeLayout mRl_userphoto;// 头像栏
	private RelativeLayout mRl_userinfo;// 个人信息栏
	private RelativeLayout mRl_settings;// 设置栏
	private RelativeLayout mRl_about;// 关于栏
	private RelativeLayout mRl_logout;// 切换登录栏
	private Intent m_Intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.LogTest("个人中心Fragment已创建");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		init(view);
		return view;
	}

	/**
	 * 界面初始化
	 * 
	 * @param view
	 */
	private void init(View view) {
		m_ImguserPicture = (CircleImageView) view
				.findViewById(R.id.imgView_userheadphoto);
		mTv_Username = (TextView) view.findViewById(R.id.txtUsername);
		mIv_Usergender = (ImageView) view.findViewById(R.id.imgUsergender);
		mRl_userphoto = (RelativeLayout) view
				.findViewById(R.id.relative_userphoto);
		mRl_userinfo = (RelativeLayout) view
				.findViewById(R.id.relative_userinfo);
		mRl_settings = (RelativeLayout) view
				.findViewById(R.id.relative_setting);
		mRl_about = (RelativeLayout) view.findViewById(R.id.relative_about);
		mRl_logout = (RelativeLayout) view.findViewById(R.id.relative_logout);

		mRl_userphoto.setOnClickListener(this);
		mRl_userinfo.setOnClickListener(this);
		mRl_settings.setOnClickListener(this);
		mRl_about.setOnClickListener(this);
		mRl_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 进入个人信息界面
		case R.id.relative_userphoto:
			m_Intent = new Intent((MainActivity) getActivity(),
					UserInfoActivity.class);
			startActivity(m_Intent);
			break;
		// 进入个人信息界面
		case R.id.relative_userinfo:
			m_Intent = new Intent((MainActivity) getActivity(),
					UserInfoActivity.class);
			startActivity(m_Intent);
			break;
		case R.id.relative_setting:
			// 进入设置界面
			m_Intent = new Intent((MainActivity) getActivity(),
					SettingActivity.class);
			startActivity(m_Intent);
			break;
		case R.id.relative_about:
			// 进入关于界面
			m_Intent = new Intent((MainActivity) getActivity(),
					AboutActivity.class);
			startActivity(m_Intent);
			break;
		case R.id.relative_logout:
			DsApplication.resetInit();// 恢复Application相关变量
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();// 将主界面finish
			break;
		default:
			break;

		}
	}

	/**
	 * 加载基本信息
	 */
	private void LoadingUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				LogUtil.LogTest("" + DsApplication.uBitmap);
				// 加载用户头像
				m_ImguserPicture.post(new Runnable() {
					@Override
					public void run() {
						if (DsApplication.uBitmap != null) {
							m_ImguserPicture
									.setImageBitmap(DsApplication.uBitmap);
						}
						mTv_Username.setText(DsApplication.user.getUserName());
						if (DsApplication.user.getUserGender() != null
								&& DsApplication.user.getUserGender().equals(
										"男")) {
							mIv_Usergender.setImageResource(R.drawable.boy);
						} else if (DsApplication.user.getUserGender() != null
								&& DsApplication.user.getUserGender().equals(
										"女")) {
							mIv_Usergender.setImageResource(R.drawable.girl);
						} else {
							return;
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			LogUtil.LogTest("********显示");
			// 加载用户基本信息
			LoadingUserInfo();
		}
	}
}
