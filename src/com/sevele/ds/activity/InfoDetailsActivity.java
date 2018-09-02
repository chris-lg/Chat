package com.sevele.ds.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.view.CircleImageView;

/**
 * @author:liu ge
 * @createTime:2015年4月3日
 * @descrption:好友，已经解密的陌生人详细信息界面
 */
public class InfoDetailsActivity extends BaseActivity {
	private com.sevele.ds.view.CircleImageView friendHeadPic; // 好友头像

	private TextView txtFriendRealName; // 用户真实名字
	private TextView txtFriendGender; // 用户性别
	private TextView txtFriendAge; // 用户年龄
	private TextView txtFriendHometown; // 用户家乡

	private Bitmap fBitmap;

	private Button mBtn_Exit;// 点击返回聊天界面

	private TextView Info_title;// 界面标题
	private RelativeLayout back_layout;// 返回

	private int fId;

	private FriendTable friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {

		setContentView(R.layout.activity_strangerfriends_info);
		friendHeadPic = (CircleImageView) findViewById(R.id.imgView_userphoto);
		Info_title = (TextView) findViewById(R.id.txtTitle_banner);
		back_layout = (RelativeLayout) findViewById(R.id.back_layout);

		txtFriendRealName = (TextView) findViewById(R.id.txt_userRealName);
		txtFriendGender = (TextView) findViewById(R.id.txt_gender);
		txtFriendAge = (TextView) findViewById(R.id.txt_yearsOld);
		txtFriendHometown = (TextView) findViewById(R.id.txt_hometown);
		friend = DsApplication.currentFd;
		if (friend != null) {
			txtFriendRealName.setText("用户名：" + friend.getUserName());
			txtFriendGender.setText("性别："
					+ String.valueOf(friend.getUserGender()));
			txtFriendAge.setText("年龄：" + String.valueOf(friend.getUserAge()));
			txtFriendHometown.setText("家乡：" + friend.getUserHometown());
			fBitmap = BitmapFactory.decodeFile(DsConstant.IMG_ROOT
					+ friend.getUserHeadPicture());
			if (fBitmap != null) {
				friendHeadPic.setImageBitmap(fBitmap);
			}
		}
		mBtn_Exit = (Button) findViewById(R.id.btnExit);
		Info_title.setText("好友信息");
		mBtn_Exit.setOnClickListener(this);
		back_layout.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {

		case R.id.btnExit: // 进入聊天界面
			InfoDetailsActivity.this.finish();
			break;
		case R.id.back_layout:
			InfoDetailsActivity.this.finish();
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		InfoDetailsActivity.this.finish();
		return super.onKeyUp(keyCode, event);
	}

}
