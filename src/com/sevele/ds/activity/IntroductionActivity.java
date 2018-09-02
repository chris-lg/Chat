package com.sevele.ds.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;

/**
 * @author:Maozhiqi
 * @createTime:2015年5月10日
 * @descrption:软件介绍界面
 */
public class IntroductionActivity extends BaseActivity {
	private TextView mTv_Introduction_tilte;// 界面标题
	private RelativeLayout mRl_Back;// 返回

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_introduction);
		mTv_Introduction_tilte = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mTv_Introduction_tilte.setText(R.string.txtIntroduction_title);

		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		if (v.getId() == R.id.back_layout) {
			//返回
			finish();
		}
	}

}
