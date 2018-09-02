package com.sevele.ds.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.adapter.StrangerListAdapter;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.table.StrangerTable;
import com.sevele.ds.utils.UIHelper;

/**
 * @author:liu ge
 * @createTime:2015年4月3日
 * @descrption:显示被摇到的陌生人的界面
 */
public class StrangerShakedActivity extends BaseActivity {
	private ListView mLv_strangers_list;// 陌生人列表
	private BaseAdapter mBa_strangers_adapter;// 陌生人列表适配器
	private List<StrangerTable> m_Strangers = new ArrayList<StrangerTable>();// 陌生人List

	private TextView mTv_StrangerShake_Title;// 界面标题
	private RelativeLayout mRl_Back;// 返回
	private TextView mTv_strangerlist_hint;// 解密提示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_strangershaked);
		mLv_strangers_list = (ListView) findViewById(R.id.shaked_strangerlist);
		mBa_strangers_adapter = new StrangerListAdapter(m_Strangers, this);
		mLv_strangers_list.setAdapter(mBa_strangers_adapter);
		mTv_StrangerShake_Title = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mTv_strangerlist_hint = (TextView) findViewById(R.id.txt_strangerlist_hint);
		mTv_StrangerShake_Title.setText("陌生人");

		mRl_Back.setOnClickListener(this);
		// 数据库中读取陌生人信息
		final int[] opreationPosition = new int[1];
		List<StrangerTable> str = DsApplication.db
				.readStrangerFormDb(DsApplication.user.getId());
		if (str != null) {
			m_Strangers.addAll(str);
			mTv_strangerlist_hint.setVisibility(View.GONE);
		}

		// 若是该陌生人已经被解密，在list里面可以看见这个人的具体信息，点击后会弹出对话框让用户原则是否添加他为好友
		// 若该陌生人没有被解密，就直接进入游戏环节
		mLv_strangers_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if (m_Strangers.get(index).isStrangerIsAccess()) {
					// 进入详细信息界面，在这里可以选择是否加为好友
					Intent intent = new Intent(StrangerShakedActivity.this,
							InfoDetailsActivity.class);
					intent.putExtra("index", index);
					StrangerShakedActivity.this.startActivity(intent);
				} else {
					// 进入游戏界面
					if (DsApplication.db.getFriendTable(m_Strangers.get(index)
							.getSid()) == null) {
						Intent intent = new Intent(StrangerShakedActivity.this,
								DecryptgameActivity.class);
						intent.putExtra("addFriendId", m_Strangers.get(index)
								.getSid() + "");
						startActivity(intent);
						StrangerShakedActivity.this.finish();
					}else{
						UIHelper.ToastMessage("该陌生人已是您的好友");
					}
				}
			}
		});

		// 长按选择是否删除该陌生人，此时也要删除本地数据库中的数据
		mLv_strangers_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						opreationPosition[0] = arg2;
						AlertDialog.Builder builder = new AlertDialog.Builder(
								StrangerShakedActivity.this);
						builder.setTitle("陌生人操作").setMessage("是否删除陌生人");

						builder.setNegativeButton("否", new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

						builder.setPositiveButton("是", new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								// // 删除数据库里面的数据
								DsApplication.db
										.DeleteStrangersfromDb(m_Strangers
												.get(opreationPosition[0]));

								// 将该陌生人从用户陌生人list里面移除，并且删除数据库中的数据，更新陌生人列表
								m_Strangers.remove(opreationPosition[0]);
								mBa_strangers_adapter.notifyDataSetChanged();
							}
						});

						builder.create().show();
						return false;
					}
				});
	}

	@Override
	public void widgetClick(View v) {
		// 返回
		if (v.getId() == R.id.back_layout) {
			StrangerShakedActivity.this.finish();
		}
	}
}
