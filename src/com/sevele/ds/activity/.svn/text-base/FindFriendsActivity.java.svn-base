package com.sevele.ds.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;

/**
 * @author:Maozhiqi
 * @createTime:2015年5月1日
 * @descrption:联系人列表查找指定好友
 */
public class FindFriendsActivity extends BaseActivity {
	private EditText mEt_find_friends;// 查找输入
	private Button mBtn_find_friends;// 开始查找按钮
	private TextView mTv_find_hint;// 提示信息
	private RelativeLayout mRl_findfriendinfo;// 查找到的联系人基本信息布局
	private ImageView mIv_findfriend;// 查找到的联系人的头像
	private TextView mTv_findfriend;// 查找到的联系人昵称

	private TextView mTv_FindFriends_title;// 界面标题
	private RelativeLayout mRl_Back;// 返回

	private Bitmap m_Bitmap = null;

	private FriendTable m_FindFriend = new FriendTable();// 查找到的联系人

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_findfirends);
		mEt_find_friends = (EditText) findViewById(R.id.edit_find_friends);
		mBtn_find_friends = (Button) findViewById(R.id.btn_find_friends);
		mTv_find_hint = (TextView) findViewById(R.id.txt_find_hint);
		mRl_findfriendinfo = (RelativeLayout) findViewById(R.id.relative_findfriendinfo);
		mIv_findfriend = (ImageView) findViewById(R.id.img_findfriend);
		mTv_findfriend = (TextView) findViewById(R.id.txt_findfriend);
		mTv_FindFriends_title = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mTv_FindFriends_title.setText("查找联系人");

		mBtn_find_friends.setOnClickListener(this);
		mRl_Back.setOnClickListener(this);
		mRl_findfriendinfo.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_find_friends:
			// 查找并显示联系人
			displayFindfriends(mEt_find_friends.getText().toString());
			break;
		case R.id.relative_findfriendinfo:
			// 点击与查找到的好友聊天
			Intent intent = new Intent(FindFriendsActivity.this,
					ChatActivity.class);
			intent.putExtra("fId", m_FindFriend.getSid());
			startActivity(intent);
			finish();
			break;
		case R.id.back_layout:
			// 返回
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 查找并显示联系人
	 * 
	 * @param friendName
	 */
	private void displayFindfriends(String friendName) {
		mTv_find_hint.setVisibility(View.GONE);
		mRl_findfriendinfo.setVisibility(View.GONE);
		if (friendName != null) {
			UIHelper.loading();
			m_FindFriend = DsApplication.db.getFriendTable(friendName);
			// 判断是否有该联系人
			if (m_FindFriend != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (m_FindFriend.getUserHeadPicture() != null) {
							m_Bitmap = BitmapFactory
									.decodeFile(DsConstant.IMG_ROOT
											+ m_FindFriend.getUserHeadPicture());
						}
						mIv_findfriend.post(new Runnable() {
							@Override
							public void run() {
								if (m_Bitmap != null) {
									mIv_findfriend.setImageBitmap(m_Bitmap);
								}
								mTv_findfriend.setText(m_FindFriend
										.getUserName());
							}
						});
						if (UIHelper.isLoadingShow) {
							UIHelper.cancleLoading();
						}
					}
				}).start();
				mRl_findfriendinfo.setVisibility(View.VISIBLE);
			} else {
				// 没有有查找到该联系人
				UIHelper.ToastMessage("没有查找到该联系人");
				mTv_find_hint.setText("没有查找到该联系人");
				mTv_find_hint.setVisibility(View.VISIBLE);
				if (UIHelper.isLoadingShow) {
					UIHelper.cancleLoading();
				}
			}
			LogUtil.LogTest("查找到的好友" + m_FindFriend);
		} else {
			UIHelper.ToastMessage("请输入正确的好友ID");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
