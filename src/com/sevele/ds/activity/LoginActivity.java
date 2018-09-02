package com.sevele.ds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.app.DsNetworkCheck;
import com.sevele.ds.bean.Result;
import com.sevele.ds.common.HandleResult;
import com.sevele.ds.common.SdcardMountedCheck;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.UIHelper;
import com.sevele.ds.view.EditTextWithDel;

/**
 * @author:liu ge
 * @createTime:2015年3月21日
 * @descrption:登录
 */
public class LoginActivity extends BaseActivity {
	private TextView mTv_Regist; //注册按钮
	private Button mBtn_Login;  //登录按钮
	private com.sevele.ds.view.EditTextWithDel mEt_UserCount; //帐号输入框
	private com.sevele.ds.view.EditTextWithDel mEt_UserPwd;  // 密码输入框
	private Handler mHandler; //通知更新界面handler对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_login);

		mBtn_Login = (Button) findViewById(R.id.btn_login);
		mBtn_Login.setOnClickListener(this);

		mEt_UserCount = (EditTextWithDel) findViewById(R.id.edtTxt_username);
		mEt_UserPwd = (EditTextWithDel) findViewById(R.id.edtTxt_password);

		mTv_Regist = (TextView) findViewById(R.id.txt_register);
		mTv_Regist.setOnClickListener(this);

		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (UIHelper.isLoadingShow) {
					UIHelper.cancleLoading();
				}
				Result re = HandleResult.getResult(msg);
				switch (msg.what) {
				// 登录失败
				case DsConstant.HANDLER_NET_LINK_FAIL:
					UIHelper.ToastMessage("请求失败");
					break;
				// 登录成功
				case DsConstant.HANDLER_NET_LOGIN_OK:
					if (re.isSuccess()) {
						// 获取到登录结果 用户的id 和 JESSONID,是否是在本手机第一次登录
						if (SdcardMountedCheck.isSdcardMounted()) {
							UserTable ut = DsApplication.db
									.getUserTable(mEt_UserCount.getText()
											.toString());
							if (ut != null) {
								DsApplication.user = ut;
							} else {// 如果此时登录在本地数据库没有该用户信息,创建一个本地数据库信息
								UserTable mUser = new UserTable();
								mUser.setUserCount(mEt_UserCount.getText()
										.toString());
								String loginResult = re.getReslut();
								mUser.setId(BeanJsonConvert
										.getUserId(loginResult));
								DsApplication.db.writeUserInfo(mUser);
								DsApplication.user = mUser;
							}
						}
						BeanJsonConvert.setJessionId(re.getReslut());
						// 将用户信息写入数据库，此处值写了用户的id
						Intent l_intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(l_intent);
						DsApplication.isLogin = true;
						finish();
					}
					UIHelper.ToastMessage(re.getMessage().toString());
					break;
				}
				
			};
		};
	}

	@Override
	public void widgetClick(View a_click_view) {
		switch (a_click_view.getId()) {
		case R.id.btn_login: // 点击登录
			if (checkInputComplete()) {
				if (DsNetworkCheck.networkCheck(LoginActivity.this)) { // 有网络的情况下登录
					UIHelper.loading();
					new Thread() {
						public void run() {
							new HttpAPI(mHandler).httpLogin(mEt_UserCount
									.getText().toString(), mEt_UserPwd
									.getText().toString(), HttpUrl.LOGIN_URL);
						};
					}.start();
				} else {
					UIHelper.ToastMessage("请连接网络");
				}
			}
			break;
		case R.id.txt_register:// 点击注册界面
			Intent l_intent = new Intent(this, RegisterActivity.class);
			startActivity(l_intent);
			break;
		default:
			break;
		}

	}

	/**
	 * 检查输入是否合法
	 * @return
	 */
	private boolean checkInputComplete() {
		if ("".equals(mEt_UserCount.getText().toString())) {
			UIHelper.ToastMessage("请输入帐号");
			return false;
		} else if ("".equals(mEt_UserPwd.getText().toString())) {
			UIHelper.ToastMessage("请输入密码");
			return false;
		}
		return true;
	}
}
