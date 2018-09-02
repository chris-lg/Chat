package com.sevele.ds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.utils.UIHelper;

/**
 * @author:Maozhiqi
 * @createTime:2015年4月21日
 * @descrption:关于界面
 */
public class AboutActivity extends BaseActivity {
	private RelativeLayout mRl_Introduction;// 功能介绍
	private RelativeLayout mRl_Update;// 版本更新

	private TextView mTv_about_title;// 界面标题
	private RelativeLayout mRl_Back;// 返回

	private Handler m_Handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_about);
		mRl_Introduction = (RelativeLayout) findViewById(R.id.relative_introduction);
		mRl_Update = (RelativeLayout) findViewById(R.id.relative_update);
		mTv_about_title = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mTv_about_title.setText(R.string.txtAbout_title);

		mRl_Introduction.setOnClickListener(this);
		mRl_Update.setOnClickListener(this);
		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.relative_introduction:
			// 进入功能介绍界面
			Intent intent = new Intent(AboutActivity.this,
					IntroductionActivity.class);
			startActivity(intent);
			break;
		case R.id.relative_update:
			// 检查版本更新
			UIHelper.loading();
			m_Handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (UIHelper.isLoadingShow) {
						UIHelper.cancleLoading();
						UIHelper.ToastMessage("当前已是最新版本");
					}
				}
			}, 2000);
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
