package com.sevele.ds.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.lidroid.xutils.BitmapUtils;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.ddpush.PushInfo;
import com.sevele.ds.fragment.MessageFragment;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.PushMessageTable;
import com.sevele.ds.table.StrangerTable;
import com.sevele.ds.table.UserTable;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;
import com.sevele.ds.view.GameView;

/**
 * @author Maozhiqi
 * @time 2015年4月26日
 * @descrption:解密游戏界面
 */
public class DecryptgameActivity extends BaseActivity {
	private Button mBtn_NewGame;// 重新开始按钮
	private GameView m_GameView = null;// 游戏视图
	private String str_addFriendId = null;// 解密陌生人的ID

	private TextView mTv_Gametitle;// 界面标题
	private RelativeLayout mRl_Back;// 返回
	private RelativeLayout mRl_score;// 分数显示栏
	private TextView mTv_score;// 游戏分数
	private Score m_Score;
	private HashMap<String, Object> mHm_addFriendMap = new HashMap<String, Object>();
	private Handler m_Handler;
	private isComplete m_Signal = new isComplete();// 游戏完成标志

	private ImageView mIv_addFriend;// 陌生人头像
	private TextView mTv_addFriend;// 陌生人昵称
	private ImageView mIv_addFriend_gender;// 陌生人性别

	private BitmapUtils m_BiUtils;

	private StrangerTable m_Stranger;// 陌生人实体
	private UserTable m_Userfriend = new UserTable();// 好友实体
	private Intent mIntent;
	private String str_Gogame = null;// 非解密的游戏请求

	private List<FriendTable> m_list_Friend;// 好友列表
	String[] str_friends;// 好友数组
	private int int_Num = -1;
	private PushInfo m_PushInfo;// 推送
	private Thread m_SendHelpInfo;// 发送请求线程
	private int int_PushID = -1;// 请求你帮助的好友的ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_Handler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DsConstant.HANDLER_NET_OK:
					UIHelper.ToastMessage("添加好友成功");
					// 将该陌生人加入好友数据库
					addStrangertoFriend();
					break;
				case DsConstant.HANDLER_NET_LINK_FAIL:
					UIHelper.ToastMessage("请检查网络");
					break;
				case DsConstant.HANDLER_NET_FAIL:
					UIHelper.ToastMessage("请检查网络");
					break;
				default:
					break;
				}
				return false;
			}
		});
		m_BiUtils = new BitmapUtils(this);
		// 读取好友列表以便选择求助对象
		m_list_Friend = DsApplication.db.readFriendFromDb(DsApplication.user
				.getId());
		if (m_list_Friend != null) {
			str_friends = new String[m_list_Friend.size()];
			for (int i = 0; i < m_list_Friend.size(); i++) {
				str_friends[i] = m_list_Friend.get(i).getUserName();
			}
		}
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_decryptgame);
		mTv_Gametitle = (TextView) findViewById(R.id.txtTitle_banner);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);
		mBtn_NewGame = (Button) findViewById(R.id.btnNewGame);
		m_GameView = (GameView) findViewById(R.id.gameView);
		mRl_score = (RelativeLayout) findViewById(R.id.relative_score);
		mTv_score = (TextView) findViewById(R.id.txt_score);

		Intent intent = getIntent();
		str_addFriendId = intent.getStringExtra("addFriendId");
		str_Gogame = intent.getStringExtra("Gogame");
		LogUtil.LogTest("陌生人ID" + str_addFriendId);
		// 判断以何种方式进入游戏
		if (str_addFriendId != null && str_Gogame == null) {
			// 自己解密陌生人
			m_Stranger = DsApplication.db.getStrangerFromDb((Integer
					.parseInt(str_addFriendId)));
			m_GameView.setGameRank(m_Stranger.getGameRank());// 设置解密游戏难度
			// 游戏提示
			GameStartHint();
		} else if ((str_addFriendId == null || str_addFriendId.equals(""))
				&& str_Gogame == null
				&& DsApplication.tempObject.containsKey("CurrentPushMsg")) {
			// 帮助好友解密陌生人
			PushMessageTable pmb = (PushMessageTable) DsApplication.tempObject
					.get("CurrentPushMsg");
			pmb.setMsgs(0);
			DsApplication.db.writePushMsg(pmb);
			pmb.getLists().get(0).getMsgContent();
			m_Stranger = BeanJsonConvert.jsonToBean(pmb.getLists().get(0)
					.getMsgContent(), StrangerTable.class);
			int_PushID = pmb.getPusherId();
			str_addFriendId = m_Stranger.getSid() + "";
			m_GameView.setGameRank(m_Stranger.getGameRank());// 设置解密游戏难度
			// 游戏提示
			GameStartHint();
		} else if (str_Gogame != null) {
			// 自己游戏
			mRl_score.setVisibility(View.VISIBLE);// 显示分数
			m_GameView.setGameRank(Integer.parseInt(str_Gogame));// 设置解密游戏难度
		}

		m_GameView.setComplete(m_Signal);
		mTv_Gametitle.setText("解密游戏");
		m_Score = new Score();
		m_GameView.setScore(m_Score);
		mBtn_NewGame.setOnClickListener(this);
		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btnNewGame:
			// 重新开始游戏
			startNewGame();
			break;
		case R.id.back_layout:
			// 返回
			finish();
		default:
			break;
		}
	}

	/**
	 * 重新开始游戏
	 */
	private void startNewGame() {
		m_Score.clearScore();// 将分数清零
		showScore();
		m_GameView.startGame();
	}

	/**
	 * 请求好友帮助
	 */
	private void doSeekHelp() {
		if (m_list_Friend == null) {
			UIHelper.ToastMessage("您还没有好友，靠自已吧");
			return;
		}
		// 创建好友选择对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择好友");
		builder.setSingleChoiceItems(str_friends, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						int_Num = arg1;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (int_Num >= 0) {
					m_list_Friend.get(int_Num).getSid();
					UIHelper.ToastMessage("被选择好友的ID"
							+ m_list_Friend.get(int_Num).getSid());
					MsgBeanTable msg = new MsgBeanTable();
					msg.setMsgType(DsConstant.GAME_HELP);
					msg.setSid(DsApplication.user.getId());
					msg.setMsgContent(BeanJsonConvert.beanToJosn(m_Stranger));
					m_PushInfo = new PushInfo(DecryptgameActivity.this,
							m_list_Friend.get(int_Num).getSid(),
							BeanJsonConvert.beanToJosn(msg), m_Handler);
					m_SendHelpInfo = new Thread(m_PushInfo);
					m_SendHelpInfo.start();
					DecryptgameActivity.this.finish();
					int_Num = -1;
				} else {
					UIHelper.ToastMessage("请选择一个好友");
					doSeekHelp();
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
	 * 解密结果判断执行方向
	 * 
	 * @param signal
	 */
	public void isSuccess(int signal) {
		if (signal == 1) {
			if (DsApplication.tempObject.containsKey("CurrentPushMsg")) {
				PushMessageTable pp = (PushMessageTable) DsApplication.tempObject
						.get("CurrentPushMsg");
				pp.getLists().get(0).setMsgType(DsConstant.HELP_FD_OK);
//				MessageFragment.m_msgs.remove(pp);
			}
			UIHelper.ToastMessage("解密成功");
			// 创建加好友对话框
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("陌生人信息");
			builder.setCancelable(false);// 设置不可以被取消
			LayoutInflater inflater = getLayoutInflater();
			final View v = inflater.inflate(R.layout.dialog_addfriend, null);
			mIv_addFriend = (ImageView) v.findViewById(R.id.iv_addfriend);
			mIv_addFriend_gender = (ImageView) v
					.findViewById(R.id.iv_addfriend_gender);
			mTv_addFriend = (TextView) v.findViewById(R.id.te_addfriend);
			if (m_Stranger.getUserHeadPicture() != null) {
				m_BiUtils.display(mIv_addFriend, DsConstant.MY_QINIU
						+ m_Stranger.getUserHeadPicture()
						+ DsConstant.SMALL_PIC);
			}
			mTv_addFriend.setText(m_Stranger.getUserName());
			if (m_Stranger.getUserGender() != null
					&& m_Stranger.getUserGender().equals("男")) {
				mIv_addFriend_gender.setImageResource(R.drawable.boy);
			} else if (m_Stranger.getUserGender() != null
					&& m_Stranger.getUserGender().equals("女")) {
				mIv_addFriend_gender.setImageResource(R.drawable.girl);
			}
			builder.setView(v);
			builder.setPositiveButton("加好友",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// 自己加好友
							if (DsApplication.db
									.getFriendTable(Integer.valueOf(str_addFriendId)) == null) {
								mHm_addFriendMap.put("addFriendId",
										str_addFriendId);
								HttpAPI MeaddFriend = new HttpAPI(m_Handler);
								MeaddFriend.httpSendTxt(mHm_addFriendMap,
										DsApplication.user,
										HttpUrl.ADDFRIEND_URL,
										DsConstant.HANDLER_NET_OK, null, null);
								mHm_addFriendMap.clear();
								UIHelper.loading();
								// 请求帮助的人加好友
								if (int_PushID > 0) {
									mHm_addFriendMap.put("userId", ""
											+ int_PushID);
									mHm_addFriendMap.put("addFriendId",
											str_addFriendId);
									HttpAPI OtaddFriend = new HttpAPI(m_Handler);
									OtaddFriend.httpSendTxt(mHm_addFriendMap,
											HttpUrl.ADDFRIEND_URL,
											DsConstant.ADD_FRIEND_MSG);
									mHm_addFriendMap.clear();
									MsgBeanTable msg = new MsgBeanTable();
									msg.setMsgType(DsConstant.DES_OK);
									msg.setSid(DsApplication.user.getId());
									msg.setMsgContent(BeanJsonConvert
											.beanToJosn(m_Stranger));
									m_PushInfo = new PushInfo(
											DecryptgameActivity.this,
											int_PushID, BeanJsonConvert
													.beanToJosn(msg), m_Handler);
									m_SendHelpInfo = new Thread(m_PushInfo);
									m_SendHelpInfo.start();
								}
							} else {
								// 请求帮助的人加好友
								if (int_PushID > 0) {
									mHm_addFriendMap.put("userId", ""
											+ int_PushID);
									mHm_addFriendMap.put("addFriendId",
											str_addFriendId);
									HttpAPI OtaddFriend = new HttpAPI(m_Handler);
									OtaddFriend.httpSendTxt(mHm_addFriendMap,
											HttpUrl.ADDFRIEND_URL,
											DsConstant.ADD_FRIEND_MSG);
									mHm_addFriendMap.clear();
									MsgBeanTable msg = new MsgBeanTable();
									msg.setMsgType(DsConstant.DES_OK);
									msg.setSid(DsApplication.user.getId());
									msg.setMsgContent(BeanJsonConvert
											.beanToJosn(m_Stranger));
									m_PushInfo = new PushInfo(
											DecryptgameActivity.this,
											int_PushID, BeanJsonConvert
													.beanToJosn(msg), m_Handler);
									m_SendHelpInfo = new Thread(m_PushInfo);
									m_SendHelpInfo.start();
								}
								UIHelper.ToastMessage("帮助好友解密成功");
								DecryptgameActivity.this.finish();
							}
						}
					});
			builder.setNegativeButton("继续解密",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							mIntent = new Intent(DecryptgameActivity.this,
									SensorActivity.class);
							startActivity(mIntent);
							DecryptgameActivity.this.finish();
						}
					});
			builder.create().show();
		} else {
			GameOverHint();
		}
	}

	/**
	 * 解密成功陌生人加入好友数据库
	 */
	private void addStrangertoFriend() {
		LogUtil.LogTest("解密成功的陌生人" + m_Stranger);
		LogUtil.LogTest("我的ID" + DsApplication.user.getId());
		if (m_Stranger != null) {
			m_Userfriend.setId(m_Stranger.getSid());
			m_Userfriend.setUserName(m_Stranger.getUserName());
			m_Userfriend.setUserAge(m_Stranger.getUserAge());
			m_Userfriend.setUserCount(m_Stranger.getUserCount());
			m_Userfriend.setUserGender(m_Stranger.getUserGender());
			m_Userfriend.setUserHeadPicture(m_Stranger.getUserHeadPicture());
			m_Userfriend.setUserHometown(m_Stranger.getUserHometown());
			// 将其在陌生人数据中删除
			DsApplication.db.DeleteStrangersfromDb(m_Stranger);
		}
		mIntent = new Intent();
		mIntent.setAction("android.com.sevele.ds.broadcast.addFriendOk");
		mIntent.putExtra("ft", BeanJsonConvert.beanToJosn(m_Userfriend));
		DecryptgameActivity.this.sendBroadcast(mIntent);
		m_Handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (int_PushID < 0) {
					if (UIHelper.isLoadingShow) {
						UIHelper.cancleLoading();
					}
					mIntent = new Intent(DecryptgameActivity.this,
							ChatActivity.class);
					mIntent.putExtra("fId", Integer.parseInt(str_addFriendId));
					startActivity(mIntent);
					DecryptgameActivity.this.finish();
				} else {
					DecryptgameActivity.this.finish();
				}
			}
		}, 1500);
	}

	public class isComplete {
		private int signal = 0;

		public void clearSuccess() {
			signal = 0;
		}

		public int getScore() {
			return signal;
		}

		public void Complete(int s) {
			signal = s;
			isSuccess(signal);
		}
	}

	/**
	 * 游戏开始提示
	 */
	private void GameStartHint() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("移动格子，累加数字到" + m_Stranger.getGameRank()
				+ "即可解密成功，加对方为好友");// 设置提示信息
		builder.setCancelable(false);// 设置是否可以被取消
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		// 显示对话框
		builder.create().show();
	}

	/**
	 * 游戏结束提示
	 */
	private void GameOverHint() {
		// 判断为自己的解密游戏还是好友的解密游戏失败
		if (int_PushID < 0 && str_Gogame == null) {
			// 自己解密失败
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("解密失败,您可以请求好友帮助");// 设置提示信息
			builder.setCancelable(false);// 设置是否可以被取消
			builder.setPositiveButton("请求帮助",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// 请求好友帮助
							doSeekHelp();
						}
					});
			builder.setNegativeButton("我自己能行",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startNewGame();
						}
					});
			// 显示对话框
			builder.create().show();
		} else if (int_PushID > 0 && str_Gogame == null) {
			// 帮助好友解密失败
			UIHelper.ToastMessage("解密失败");
			DecryptgameActivity.this.finish();
		} else if (str_Gogame != null) {
			// 游戏失败
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("GameOver");// 设置提示信息
			builder.setCancelable(false);// 设置是否可以被取消
			builder.setPositiveButton("重新游戏",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							startNewGame();
						}
					});
			builder.setNegativeButton("退出游戏",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DecryptgameActivity.this.finish();
						}
					});
			// 显示对话框
			builder.create().show();
		}
	}

	private void showScore() {
		mTv_score.setText(String.valueOf(m_Score.getScore()));
	}

	public class Score {
		private int score = 0;

		public void clearScore() {
			score = 0;
		}

		public int getScore() {
			return score;
		}

		public void addScore(int s) {
			score += s;
			showScore();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DsApplication.tempObject.containsKey("CurrentPushMsg")) {
			DsApplication.tempObject.remove("CurrentPushMsg");
		}
	}
}
