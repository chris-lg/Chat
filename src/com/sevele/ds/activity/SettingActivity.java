package com.sevele.ds.activity;

import java.security.interfaces.DSAPublicKey;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsAppManager;

/**
 * @author Maozhiqi
 * @time 2015年5月1日
 * @descrption 设置界面
 */
public class SettingActivity extends BaseActivity {
	private RelativeLayout mRl_Exit;// 退出

	private TextView mTv_Setting_Title;// 标题
	private RelativeLayout mRl_Back;// 返回

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_setting);
		mRl_Exit = (RelativeLayout) findViewById(R.id.relative_exit);
		mTv_Setting_Title = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mTv_Setting_Title.setText(R.string.txtSetting_title);

		mRl_Exit.setOnClickListener(this);
		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.relative_exit:
			DsAppManager.getAppManager().AppExit(SettingActivity.this);
			// 退出应用
			break;
		case R.id.back_layout:
			// 返回
			finish();
			break;
		default:
			break;
		}
	}
}
