package com.sevele.ds.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.lidroid.xutils.BitmapUtils;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.Result;
import com.sevele.ds.common.HandleResult;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;
import com.sevele.ds.utils.isIntegerUtil;

/**
 * @author Maozhiqi
 * @time :2015年4月14日
 * @descrption:用户个人详细信息界面
 */
@SuppressLint("ResourceAsColor")
public class UserInfoActivity extends BaseActivity {
	private TextView mTv_txtTitle;// 标题
	private RelativeLayout mRl_Back;// 返回按钮
	private RelativeLayout mRl_photo;// 头像栏
	private RelativeLayout mRl_nickname;// 昵称栏
	private RelativeLayout mRl_username;// 帐号栏
	private RelativeLayout mRl_gender;// 性别栏
	private RelativeLayout mRl_age;// 年龄栏
	private RelativeLayout mRl_area;// 地区栏
	private RelativeLayout mRl_gamerank;// 解密难度

	private ImageView mIv_userPicture;// 头像
	private TextView mTv_nickname;// 昵称
	private TextView tv_username;// 帐号
	private TextView tv_gender;// 性别
	private TextView tv_age;// 年龄
	private TextView tv_area;// 地区
	private TextView tv_gamerank;// 解密难度
	private BitmapUtils m_bUtils;
	private ImageView mIv_photoShow;// 查看头像
	private HashMap<String, Object> mHm_UpinfoMap = new HashMap<String, Object>();

	private UserTable m_UserInfo = new UserTable();// 用户实体
	private String str_Upuserinfo;// 上传到服务器的Json数据
	private Handler m_Handler;
	private HttpAPI m_HttpAPI;

	private final String[] str_gender = { "男", "女" };// 性别选择数组

	private final String[] str_gamerank = { "简单", "中等", "最难" };// 解密游戏难度数组

	private int int_Num_gender = 0;
	private int int_Num_gamerank = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_Handler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DsConstant.HANDLER_NET_REGIST_OK:
					// 更新服务器用户信息成功
					Result re = HandleResult.getResult(msg);
					if (re.isSuccess()) {
						LogUtil.LogTest("更新服务器用户信息成功");
						UIHelper.ToastMessage("修改成功");
					} else {
						UIHelper.ToastMessage("更改信息失败,请检查网络");
					}
					break;
				case DsConstant.HANDLER_NET_LINK_FAIL:
					UIHelper.ToastMessage("更改信息失败");
				default:
					break;
				}
				return false;
			}
		});
		m_HttpAPI = new HttpAPI(m_Handler);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_userinfo);

		mTv_txtTitle = (TextView) findViewById(R.id.txtTitle_banner);
		mTv_txtTitle.setText(R.string.txtUserInfo_title);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mRl_photo = (RelativeLayout) findViewById(R.id.relative_photo);
		mRl_nickname = (RelativeLayout) findViewById(R.id.relative_nickname);
		mRl_gender = (RelativeLayout) findViewById(R.id.relative_gender);
		mRl_area = (RelativeLayout) findViewById(R.id.relative_area);
		mRl_age = (RelativeLayout) findViewById(R.id.relative_age);
		mRl_gamerank = (RelativeLayout) findViewById(R.id.relative_gamerank);
		mRl_username = (RelativeLayout) findViewById(R.id.relative_username);
		mIv_userPicture = (ImageView) findViewById(R.id.img_userphoto);
		mTv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_gamerank = (TextView) findViewById(R.id.tv_gamerank);

		m_bUtils = new BitmapUtils(UserInfoActivity.this);
		if (DsApplication.user.getUserHeadPicture() != null) {
			m_bUtils.display(mIv_userPicture, DsConstant.IMG_ROOT
					+ DsApplication.user.getUserHeadPicture());
		}
		// 加载用户信息
		loadinginfo();

		mRl_Back.setOnClickListener(this);
		mRl_photo.setOnClickListener(this);
		mRl_nickname.setOnClickListener(this);
		mRl_username.setOnClickListener(this);
		mRl_gender.setOnClickListener(this);
		mRl_area.setOnClickListener(this);
		mRl_gamerank.setOnClickListener(this);
		mIv_userPicture.setOnClickListener(this);
		mRl_age.setOnClickListener(this);
	}

	/**
	 * 加载用户信息
	 */
	private void loadinginfo() {
		if (DsApplication.user.getUserName() != null) {
			mTv_nickname.setText(DsApplication.user.getUserName());
		}
		if (DsApplication.user.getUserCount() != null) {
			tv_username.setText(DsApplication.user.getUserCount());
		}
		if (DsApplication.user.getUserGender() != null) {
			tv_gender.setText(DsApplication.user.getUserGender());
		}
		if (DsApplication.user.getUserAge() + "" != null) {
			tv_age.setText(DsApplication.user.getUserAge() + "");
		}
		switch (DsApplication.user.getGameRank()) {
		case 6:
			tv_gamerank.setText("简单");
			break;
		case 7:
			tv_gamerank.setText("中等");
			break;
		case 8:
			tv_gamerank.setText("最难");
			break;
		default:
			tv_gamerank.setText("简单");
			break;
		}
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.relative_photo:
			// 查看头像
			photoShow();
			break;
		case R.id.relative_nickname:
			// 设置昵称
			setNickname();
			break;
		case R.id.relative_username:
			break;
		case R.id.relative_gender:
			// 设置性别
			setGender();
			break;
		case R.id.relative_age:
			// 设置年龄
			setAge();
			break;
		case R.id.relative_area:
			// 设置地区
			setArea();
			break;
		case R.id.relative_gamerank:
			// 设置解密游戏难度
			setGamerank();
			break;
		case R.id.img_userphoto:
			// 查看头像
			photoShow();
			break;
		default:
			break;
		}
	}

	/**
	 * 展示头像
	 */
	private void photoShow() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);// 设置是否可以被取消
		LayoutInflater inflater = getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_photoshow, null);
		mIv_photoShow = (ImageView) v.findViewById(R.id.iv_photoshow);
		if (DsApplication.user.getUserHeadPicture() != null) {
			m_bUtils.display(mIv_photoShow, DsConstant.IMG_ROOT
					+ DsApplication.user.getUserHeadPicture());
		}
		builder.setView(v);
		builder.create().show();
	}

	/**
	 * 设置年龄
	 */
	private void setAge() {
		// 创建年龄设置对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("年龄");
		LayoutInflater inflater = getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_set, null);
		builder.setView(v);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				EditText edit_age = (EditText) v
						.findViewById(R.id.editText_content);
				String age = edit_age.getText().toString();
				// 判断用户输入的年龄是否为整数
				if (isIntegerUtil.isInteger(age)) {
					if (Integer.parseInt(age) > 0 || Integer.parseInt(age) == 0) {
						tv_age.setText(age);
						UpUserInfo();
					} else {
						UIHelper.ToastMessage("您输入的年龄不合法");
					}
				} else {
					UIHelper.ToastMessage("您输入的年龄不合法");
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.create().show();
	}

	/**
	 * 设置解密游戏难度
	 */
	private void setGamerank() {
		// 创建解密难度选择对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("该选项是其他人解密你的难度哦");
		builder.setCancelable(false);// 设置是否可以被取消
		builder.setSingleChoiceItems(str_gamerank, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						int_Num_gamerank = arg1;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				tv_gamerank.setText(str_gamerank[int_Num_gamerank]);
				UpUserInfo();
				int_Num_gamerank = 0;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});

		builder.create().show();
	}

	/**
	 * 设置地区信息
	 */
	private void setArea() {
		// 创建地区设置对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改地区");
		LayoutInflater inflater = getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_set, null);
		builder.setView(v);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				EditText edit_area = (EditText) v
						.findViewById(R.id.editText_content);
				tv_area.setText(edit_area.getText().toString());
				UpUserInfo();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.create().show();
	}

	/**
	 * 设置性别信息
	 */
	private void setGender() {
		// 创建性别选择对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("性别");
		builder.setCancelable(false);// 设置是否可以被取消
		builder.setSingleChoiceItems(str_gender, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						int_Num_gender = arg1;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				tv_gender.setText(str_gender[int_Num_gender]);
				UpUserInfo();
				int_Num_gender = 0;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});

		builder.create().show();
	}

	/**
	 * 设置昵称
	 */
	private void setNickname() {
		// 创建昵称设置对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改昵称");
		LayoutInflater inflater = getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_set, null);
		builder.setView(v);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				EditText edit_nickname = (EditText) v
						.findViewById(R.id.editText_content);
				if (!edit_nickname.getText().equals("")) {
					mTv_nickname.setText(edit_nickname.getText().toString());
					UpUserInfo();
				} else {
					UIHelper.ToastMessage("昵称不能为空");
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.create().show();
	}

	/**
	 * 更新个人信息
	 */
	private void UpUserInfo() {

		m_UserInfo.setUserName(mTv_nickname.getText().toString());
		DsApplication.user.setUserName(mTv_nickname.getText().toString());

		m_UserInfo.setUserAge(Integer.parseInt(tv_age.getText().toString()));
		DsApplication.user.setUserAge(Integer.parseInt(tv_age.getText()
				.toString()));

		m_UserInfo.setUserGender(tv_gender.getText().toString());
		DsApplication.user.setUserGender(tv_gender.getText().toString());

		m_UserInfo.setUserHometown(tv_area.getText().toString());
		DsApplication.user.setUserHometown(tv_area.getText().toString());

		if (tv_gamerank.getText() == str_gamerank[0]) {
			m_UserInfo.setGameRank(6);
			DsApplication.user.setGameRank(6);
		} else if (tv_gamerank.getText() == str_gamerank[1]) {
			m_UserInfo.setGameRank(7);
			DsApplication.user.setGameRank(7);
		} else if (tv_gamerank.getText() == str_gamerank[2]) {
			m_UserInfo.setGameRank(8);
			DsApplication.user.setGameRank(8);
		} else {
			m_UserInfo.setGameRank(6);
			DsApplication.user.setGameRank(6);
		}
		// 更新本地个人信息数据库
		DsApplication.db.writeUserInfo(DsApplication.user);
		str_Upuserinfo = BeanJsonConvert.beanToJosn(m_UserInfo);
		LogUtil.LogTest("更新本地数据库个人信息" + DsApplication.user + "***"
				+ DsApplication.user.getUserName());
		mHm_UpinfoMap.put("userId", DsApplication.user.getId());
		mHm_UpinfoMap.put("registInfo", str_Upuserinfo);
		new Thread() {
			public void run() {
				m_HttpAPI.httpSendTxt(mHm_UpinfoMap,
						HttpUrl.UPDATE_USERINFO_URL,
						DsConstant.HANDLER_NET_REGIST_OK);
			};
		}.start();
	}

}
