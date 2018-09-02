package com.sevele.ds.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decryptstranger.R;
import com.pzf.liaotian.album.AlbumHelper;
import com.pzf.liaotian.bean.album.ImageBucket;
import com.sevele.ds.adapter.ChatAdapter;
import com.sevele.ds.adapter.ChatAdapter.ViewHolderText_R;
import com.sevele.ds.app.ConstantKeys;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.common.RefreshListView;
import com.sevele.ds.common.RefreshListView.IOnRefreshListener;
import com.sevele.ds.common.SoundMeter;
import com.sevele.ds.fragment.MessageFragment;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.PushMessageTable;
import com.sevele.ds.utils.FileUtils;
import com.sevele.ds.utils.ImageTool;
import com.sevele.ds.utils.TimeUtil;

/**
 * @author:liu ge
 * @createTime:2015年3月20日
 * @descrption:聊天界面
 */
public class ChatActivity extends BaseActivity implements IOnRefreshListener {

	private final int PRESSED = 2;
	private final int NO_PRESSED = 1;

	private TextView mBtn_Rcd; // 按住说话，发送语音
	private EditText mEt_TextContent;// 发送内容
	private Button mBtn_Send; // 发送信息按钮

	private TextView mTv_ChatTitle;// 界面标题
	private RelativeLayout mRl_backLayout;// 返回

	private RelativeLayout mRl_showFriendInfo;// 查看好友信息

	private RelativeLayout mRl_Bottom; // 界面底部发送布局，发送文字的布局

	private RelativeLayout mRl_Midea; // 发送图片布局

	private LinearLayout mLL_voice_rcd_hint_loading, // 正常录音时候的UI
			mLL_voice_rcd_hint_rcding, // 刚开始录音时候的一个圆形进度条
			mLL_voice_rcd_hint_tooshort; // 录音太短
	private LinearLayout mLL_delre; // 取消录音

	private View mV_rcChat_popup; // 录音的时候显示的UI

	private RefreshListView mLv_ListView;// 聊天的listVeiw

	private static ChatAdapter m_chatAdapter;
	
	final myHandler m_Handler = new myHandler();

	private List<MsgBeanTable> m_chats = new ArrayList<MsgBeanTable>(); // 从本地数据库当中读取聊天消息

	private boolean isShosrt = false;

	private boolean is_leave = false;
	private boolean is_voice = false;

	private int flag = NO_PRESSED;

	private ImageView mIv_chatting_mode_btn; // 点击该按钮后跳出mBtnRcd，点击这个按钮可以编辑语音发送

	private ImageView mIv_volume;// 显示音量的大小

	private SoundMeter mSm_Sensor = new SoundMeter(); // 录音的一个对象

	private MediaPlayer m_MediaPlayer = new MediaPlayer();

	private static final int POLL_INTERVAL = 300;// 300毫秒

	private long lng_startVoiceT, lng_endVoiceT; // 录音时间

	private int int_mRcdStartTime = 0;// 录制的开始时间

	private int int_mRcdVoiceDelayTime = 1000; // 连续执行之间的周期

	private int int_mRcdVoiceStartDelayTime = 200; // 首次执行的延迟时间 单位毫秒

	private TextView mTv_VoiceRecorderTime;// 录制的时间
	private int int_voiceRecordTime;

	private ScheduledExecutorService mSES_Executor;// 录制计时器

	private VoiceRcdTimeTask m_VoiceRcdTimeTask;

	private String mStr_voiceName;// 录音的文件名

	private File mf_recAudioFile;
	private int int_fid;
	private MessageComeBroadCast m_MCBCReceiver;
	private Bitmap mBtp_fBitmap;

	private InputMethodManager m_InputMethodManager;

	private Button mBtn_Media; // 发送多媒体按钮

	private TextView mTv_Takepic; // 拍照发送图片

	private TextView mTv_Albumpic; // 从相册选取图片发送
	private AlbumHelper m_albumHelper = null;// 相册管理类

	private static List<ImageBucket> m_albumList = null;// 相册数据list
	private String mStr_choosePicPath;

	private static final int CAMERA_WITH_DATA = 10;
	public static Boolean mHasNewMsg = false;// 聊天的消息条数

	// private DbOperation db;
	public int MSGPAGERNUM;
	private LoadMoreDataAsynTask m_lmdTask;
	private PushMessageTable m_pushMsg;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 代码注册reciver
		m_MCBCReceiver = new MessageComeBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.com.sevele.ds.broadcast.updateChating");
		this.registerReceiver(m_MCBCReceiver, filter);
		if(mHasNewMsg==null){
			mHasNewMsg=new Boolean(false);
		}
	}

	@Override
	public void initWidget() {
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.chat);

		Intent intent = getIntent();
		int_fid = intent.getIntExtra("fId", -1);
		mLv_ListView = (RefreshListView) findViewById(R.id.listview);
		if (int_fid == -1) {// 从推送界面过来的
			if (DsApplication.tempObject.containsKey("CurrentPushMsg")) {
				m_pushMsg = (PushMessageTable) DsApplication.tempObject
						.get("CurrentPushMsg");
				int_fid = m_pushMsg.getPusherId();
				m_pushMsg.setMsgs(0);
				DsApplication.db.writePushMsg(m_pushMsg);
			}

		} else {
			for (PushMessageTable pmb : MessageFragment.m_msgs) {
				// 如果有推送消息在推送界面，但是此时没有进去看而是点击发送次消息的人来头像进入聊天界面的时候 显示当时的推送消息
				if (pmb.getPusherId() == int_fid && pmb.getMsgs() > 0) {
					MsgBeanTable mbt = pmb.getLists().get(0);
					if (mbt.getMsgType() != DsConstant.GAME_HELP
							&& mbt.getMsgType() != DsConstant.DES_OK&&mbt.getMsgType() != DsConstant.ADD_FRIEND_MSG) {
						m_chats.add(mbt);
					}
					pmb.setMsgs(0);
					DsApplication.db.writePushMsg(pmb);
					break;
				}
			}
		}
		DsApplication.currentFd = DsApplication.db.getFriendTable(int_fid);
		

		if (DsApplication.currentFd != null) {// 不是陌生人发送的消息
			mBtp_fBitmap = BitmapFactory.decodeFile(DsConstant.IMG_ROOT
					+ DsApplication.currentFd.getUserHeadPicture());
			Log.e("","-------1----"+mBtp_fBitmap);
		} else {
			FriendTable cf = new FriendTable();
			cf.setUserName("陌生人");
			cf.setSid(int_fid);
			DsApplication.currentFd = cf;

		}
		Log.e("","-----------"+mBtp_fBitmap);
		LoadMoreDataAsynTask l_lmdTask = new LoadMoreDataAsynTask();
		l_lmdTask.execute(DsApplication.user.getId(),
				DsApplication.currentFd.getId(), MSGPAGERNUM);

		m_chatAdapter = new ChatAdapter(m_chats, this, mBtp_fBitmap, m_Handler,
				m_MediaPlayer);
		mBtn_Send = (Button) findViewById(R.id.btn_send);
		mBtn_Send.setOnClickListener(this);
		m_InputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mBtn_Rcd = (TextView) findViewById(R.id.btn_rcd);
		mBtn_Media = (Button) findViewById(R.id.btn_media);
		mBtn_Media.setOnClickListener(this);

		mRl_backLayout = (RelativeLayout) findViewById(R.id.chat_back_layout);
		mRl_backLayout.setOnClickListener(this);
		mTv_ChatTitle = (TextView) findViewById(R.id.txtTitle_chat);
		mTv_ChatTitle.setText(DsApplication.currentFd.getUserName());
		mRl_showFriendInfo = (RelativeLayout) findViewById(R.id.relative_showfriendinfo);
		mRl_showFriendInfo.setOnClickListener(this);

		mTv_VoiceRecorderTime = (TextView) findViewById(R.id.voice_time);
		mRl_Midea = (RelativeLayout) findViewById(R.id.media_rl);

		mEt_TextContent = (EditText) findViewById(R.id.et_sendmessage);
		mEt_TextContent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (mRl_Midea.isShown()) {
					mRl_Midea.setVisibility(View.GONE);
				}
				return false;
			}
		});

		mEt_TextContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			// 文字编辑框如果有文字弹出发送button
			public void afterTextChanged(Editable arg0) {
				if (arg0.length() > 0) {
					mBtn_Send.setVisibility(View.VISIBLE);
					mBtn_Media.setVisibility(View.INVISIBLE);
				} else {
					mBtn_Send.setVisibility(View.GONE);
					mBtn_Media.setVisibility(View.VISIBLE);
				}

			}
		});
		mRl_Bottom = (RelativeLayout) findViewById(R.id.btn_bottom);
		mTv_Takepic = (TextView) findViewById(R.id.pic_txt);
		mTv_Takepic.setOnClickListener(this);
		mTv_Albumpic = (TextView) findViewById(R.id.tphoto_txt);
		mTv_Albumpic.setOnClickListener(this);

		mLv_ListView.setAdapter(m_chatAdapter);
		mLv_ListView.setOnRefreshListener(this);

		mLL_voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);

		mLL_voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);

		mLL_voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);

		mV_rcChat_popup = this.findViewById(R.id.rcChat_popup);

		mIv_chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);

		mIv_chatting_mode_btn.setOnClickListener(this);
		mIv_volume = (ImageView) this.findViewById(R.id.volume);
		mLL_delre = (LinearLayout) this.findViewById(R.id.del_re);
		mBtn_Rcd.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				return false;
			}
		});
		initAlbumData();// 初始化相册
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:// 发送文字信息
			if (!"".equals(mEt_TextContent.getText().toString())) {
				// 将信息组装
				addSendMsg(DsConstant.TXT_MSG, mEt_TextContent.getText()
						.toString());
				mEt_TextContent.setText("");
				if (mBtn_Send.getVisibility() == View.VISIBLE) {
					mBtn_Send.setVisibility(View.GONE);
					mBtn_Media.setVisibility(View.VISIBLE);
				}
			}
			break;

		case R.id.ivPopUp: // 点击语音他弹出按钮
			if (is_voice) {
				mBtn_Rcd.setVisibility(View.GONE);
				mRl_Bottom.setVisibility(View.VISIBLE);
				is_voice = false;
				mIv_chatting_mode_btn
						.setImageResource(R.drawable.chatting_setmode_msg_btn);

				// 变为发送语音那个按钮

			} else {
				mBtn_Rcd.setVisibility(View.VISIBLE);
				mRl_Bottom.setVisibility(View.GONE);
				is_voice = true;
				mIv_chatting_mode_btn
						.setImageResource(R.drawable.chatting_setmode_voice_btn);// 变为发送文本那个按钮
				m_InputMethodManager.hideSoftInputFromWindow(
						mEt_TextContent.getWindowToken(), 0);
				if (mRl_Midea.isShown()) {
					mRl_Midea.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.btn_media: // 发送图片按钮
			if (!mRl_Midea.isShown()) {
				m_InputMethodManager.hideSoftInputFromWindow(
						mEt_TextContent.getWindowToken(), 0);
				mRl_Midea.setVisibility(View.VISIBLE);
			} else {
				mRl_Midea.setVisibility(View.GONE);
			}
			break;
		case R.id.tphoto_txt: // 拍照
			Intent pcIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mStr_choosePicPath = DsConstant.ROOT + File.separator + "aaa"
					+ File.separator + System.currentTimeMillis() + ".jpg"; // 选择照片存放路径
			pcIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(mStr_choosePicPath)));
			startActivityForResult(pcIntent, CAMERA_WITH_DATA);
			mRl_Midea.setVisibility(View.GONE);
			break;
		case R.id.pic_txt: // 图片选择
			// 相册
			if (m_albumList.size() < 1) {
				Toast.makeText(ChatActivity.this, "相册中没有图片", Toast.LENGTH_LONG)
						.show();
				return;
			}
			Intent intent = new Intent(ChatActivity.this,
					PickPhotoActivity.class);
			startActivityForResult(intent, ConstantKeys.ALBUM_BACK_DATA);
			mRl_Midea.setVisibility(View.GONE);
			break;
		case R.id.chat_back_layout: // 返回键
			finish();
			break;
		case R.id.relative_showfriendinfo: // 看好友个人信息
			Intent mIntent = new Intent(ChatActivity.this,
					InfoDetailsActivity.class);
			startActivity(mIntent);
		default:
			break;
		}
	}

	public static ChatAdapter getMessageAdapter() {
		return m_chatAdapter;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("fff", "结果:" + resultCode);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		// 拍照返回
		case CAMERA_WITH_DATA:
			hanlderTakePhotoData(data);
			break;

		default:
			break;
		}
	}

	/**
	 * 处理拍照返回的相片
	 * 
	 * @param data
	 */
	private void hanlderTakePhotoData(Intent data) {
		if (data == null) {
			// 新建bitmap
			Bitmap nBitmap = ImageTool.createImageThumbnail(mStr_choosePicPath);
			FileUtils fu = new FileUtils(this);
			String imageName = System.currentTimeMillis() + ".jpg";
			try {
				fu.savaBitmap(imageName, nBitmap);
				fu.deleteFile(new File(mStr_choosePicPath).getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
			addSendMsg(DsConstant.PIC_MSG, fu.getFilePathByName(imageName));
		} else {
			Bundle extras = data.getExtras();
			Bitmap bitmap = extras == null ? null : (Bitmap) extras.get("data");
			if (bitmap == null) {
				return;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int[] location = new int[2];
		mBtn_Rcd.getLocationInWindow(location); // 获取按住说话UI在当前窗口内的绝对坐标
		int btn_rc_Y = location[1];
		int btn_rc_X = location[0];

		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}
		if (is_voice) { // 如果已经点击了录音的按钮
			int[] del_location = new int[2];
			mLL_delre.getLocationInWindow(del_location); // 获取取消发送语音布局在窗口内的绝对坐标
			// 当屏幕被按下的时候
			if (event.getAction() == MotionEvent.ACTION_DOWN
					&& flag == NO_PRESSED) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				// 判断手势按下的位置是否是语音录制按钮的范围内
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {
					mBtn_Rcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					mV_rcChat_popup.setVisibility(View.VISIBLE);
					mLL_voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					// 300毫秒后取消进度条，显示录音图标
					m_Handler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								mLL_voice_rcd_hint_loading
										.setVisibility(View.GONE);
								mLL_voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					lng_startVoiceT = System.currentTimeMillis();
					mStr_voiceName = DsApplication.user.getId()
							+ lng_startVoiceT + ".amr";
					// 开始录音
					start(mStr_voiceName);
					flag = PRESSED;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP
					&& flag == PRESSED) {// 松开手势时执行录制完成
				flag = NO_PRESSED;
				mLL_voice_rcd_hint_rcding.setVisibility(View.GONE);
				mLL_voice_rcd_hint_loading.setVisibility(View.GONE);
				mBtn_Rcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				stop();
				if ((event.getY() < btn_rc_Y || event.getX() < btn_rc_X)
						&& is_leave) {
					mLL_delre.setVisibility(View.GONE);
					is_leave = false;
					canclVoice(mf_recAudioFile);
					return true;
				}
				lng_endVoiceT = SystemClock.currentThreadTimeMillis();
				int time = (int) ((lng_endVoiceT - lng_startVoiceT) / 50);
				Log.e("", "time===" + time);
				// 发送语音，时间太短不能发送
				if (time < 1 || isShosrt) {
					isShosrt = true;
					mLL_voice_rcd_hint_loading.setVisibility(View.GONE);
					mLL_voice_rcd_hint_rcding.setVisibility(View.GONE);
					mLL_voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
					m_Handler.postDelayed(new Runnable() {
						public void run() {
							mLL_voice_rcd_hint_tooshort
									.setVisibility(View.GONE);
							mV_rcChat_popup.setVisibility(View.GONE);
							isShosrt = false;
							
						}
					}, 500);
					canclVoice(mf_recAudioFile);
					
					return false;
				}
				// 否则，在此处发送文件
				if (mf_recAudioFile != null) {
					mV_rcChat_popup.setVisibility(View.GONE);
					addSendMsg(DsConstant.VOICE_MSG,
							mf_recAudioFile.getAbsolutePath());
				}

			}
			if (event.getY() < btn_rc_Y || event.getX() < btn_rc_X) {
				mLL_delre.setVisibility(View.VISIBLE);
				is_leave = true;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * @Description 初始化相册数据
	 */
	private void initAlbumData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				m_albumHelper = AlbumHelper.getHelper(ChatActivity.this);
				m_albumList = m_albumHelper.getImagesBucketList(false);
			}
		}).start();
	}

	// 获取到声音的大小，然后更新界面，每个300毫秒抽样一次声音的大小
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSm_Sensor.getAmplitude();
			updateDisplay(amp);
			m_Handler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	// 开始录音
	private void start(String name) {
		mSm_Sensor.start(name);
		m_Handler.postDelayed(mPollTask, POLL_INTERVAL);

		m_VoiceRcdTimeTask = new VoiceRcdTimeTask(int_mRcdStartTime);
		if (mSES_Executor == null) {
			mSES_Executor = Executors.newSingleThreadScheduledExecutor();
			mSES_Executor.scheduleAtFixedRate(m_VoiceRcdTimeTask,
					int_mRcdVoiceStartDelayTime, int_mRcdVoiceDelayTime,
					TimeUnit.MILLISECONDS);
		}
		mf_recAudioFile = new File(DsConstant.AUDIO_ROOT + name);
	}

	// 停止录音
	private void stop() {
		m_Handler.removeCallbacks(mPollTask);
		if (mSES_Executor != null && !mSES_Executor.isShutdown()) {
			mSES_Executor.shutdown();
			mSES_Executor = null;
		}
		if (!mTv_VoiceRecorderTime.getText().toString().equals("")) {
			int_voiceRecordTime = Integer.valueOf(mTv_VoiceRecorderTime
					.getText().toString());
		} else {
			isShosrt = true;
		}
		mTv_VoiceRecorderTime.setText("");
		mSm_Sensor.stop();
		mIv_volume.setImageResource(R.drawable.amp1);
	}

	// 显示音量的大小
	private void updateDisplay(double signalEMA) {
		switch ((int) signalEMA) {
		case 0:
		case 1:
			mIv_volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			mIv_volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			mIv_volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			mIv_volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			mIv_volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			mIv_volume.setImageResource(R.drawable.amp6);
			break;
		default:
			mIv_volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	/**
	 * 录制语音计时器
	 * 
	 * @desc:
	 * @author: pangzf
	 * @date: 2014年11月10日 下午3:46:46
	 */
	private class VoiceRcdTimeTask implements Runnable {
		int time = 0;

		public VoiceRcdTimeTask(int startTime) {
			time = startTime;
		}

		@Override
		public void run() {
			time++;
			updateTimes(time);
		}
	}

	/**
	 * 更新录音时间内容
	 * 
	 * @param time
	 */
	public void updateTimes(final int time) {
		Log.e("fff", "时间:" + time);
		ChatActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTv_VoiceRecorderTime.setText(TimeUtil
						.getVoiceRecorderTime(time));
			}
		});

	}

	/**
	 * 添加聊天界面的消息
	 * 
	 * @param msgType
	 * @param msgContent
	 */
	private void addSendMsg(int msgType, String msgContent) {
		MsgBeanTable chat = new MsgBeanTable();
		chat.setIsUserMsg(DsConstant.MSG_TO);

		if (msgType == DsConstant.VOICE_MSG) {
			chat.setVoiceTime(int_voiceRecordTime);
		}
		chat.setMsgType(msgType);
		chat.putShowtype();
		chat.setMsgContent(msgContent);
		chat.setLoadState(DsConstant.NO_PUSH_OR_LOAD);
		chat.setChatTime(System.currentTimeMillis());
		chat.setSid(DsApplication.user.getId());// 推送人的id
		m_chats.add(chat);
		updateList();
		mHasNewMsg=true;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 添加推送界面过来的消息
	 * 
	 * @param msgContent
	 */
	@SuppressWarnings("unused")
	private void addMsg(List<MsgBeanTable> msgContent) {
		m_chats.addAll(msgContent);
		updateList();
	}

	private void canclVoice(File file) {
		if (file != null && file.exists()) {
			file.delete();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		return super.dispatchTouchEvent(event);

	}

	// 与正在聊天的好友发送过来的消息
	class MessageComeBroadCast extends BroadcastReceiver {
		public MessageComeBroadCast() {
		}

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String msg = intent.getStringExtra("pushMsg");
			MsgBeanTable mMsg = BeanJsonConvert.jsonToBean(msg,
					MsgBeanTable.class);
			mMsg.setIsUserMsg(DsConstant.MSG_COME);
			mMsg.putShowtype();
			mMsg.setLoadState(DsConstant.NO_PUSH_OR_LOAD);
			mMsg.setChatTime(System.currentTimeMillis());
			m_chats.add(mMsg);
			updateList();
			if (mMsg.getMsgType() == DsConstant.TXT_MSG) {// 接受到文字消息直接存入数据库
				saveMsg(mMsg);
			}
		}
	}

	/**
	 * 更新聊天界面
	 */
	private void updateList() {
		m_chatAdapter.notifyDataSetChanged();
		mLv_ListView.setSelection(m_chatAdapter.getCount() + 1);
	}

	private void saveMsg(MsgBeanTable msg) {
		Log.e("", "save---" + DsApplication.currentFd.getSid());
		msg.setUser(DsApplication.user);
		msg.setFriend(DsApplication.currentFd);
		DsApplication.db.saveChatRecord(msg);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mRl_Midea.isShown()) {
			mRl_Midea.setVisibility(View.GONE);
			return false;
		}
		return super.onKeyUp(keyCode, event);
	}

	public class myHandler extends Handler {
		public void handleMessage(Message msg) {
			int fVisibleItem = mLv_ListView.getFirstVisiblePosition();
			int lVisibleItem = mLv_ListView.getLastVisiblePosition();
			ChatAdapter.BaseViewHolder vh = null;
			Integer pos = (Integer) msg.arg1;
			final MsgBeanTable mbt = (MsgBeanTable) msg.obj;
			++pos;
			if (pos != null && (pos >= fVisibleItem && pos <= lVisibleItem)) {
				View view = mLv_ListView.getChildAt(pos - fVisibleItem);
				if (view.getTag() instanceof ChatAdapter.BaseViewHolder) {
					vh = (ChatAdapter.BaseViewHolder) view.getTag();
				}
			} else {// 该条消息不在屏幕中等待显示了
				return;
			}
			if (vh == null) {
				return;
			}
			vh.pb_progress.setVisibility(View.GONE);
			switch (msg.what) {
			case DsConstant.HANDLER_TXTMSG_OK:// 文字信息推送成功
				ViewHolderText_R t_vh = (ViewHolderText_R) vh;
				t_vh.iv_fail.setVisibility(View.GONE);

				Log.e("", "文字消息推送完毕");
				break;
			case DsConstant.HANDLER_VOICEMSG_OK:// 语音信息推送成功
				vh.iv_fail.setVisibility(View.GONE);
				Log.e("", "语音上传成功");
				break;
			case DsConstant.HANDLER_PICMSG_OK:// 图片信息推送成功
				vh.iv_fail.setVisibility(View.GONE);
				Log.e("", "HANDLER_PICMSG_OK");
				break;
			case DsConstant.HANDLER_QINIU_NET_UPLOAD_FAIL:// 文件上传失败
				Log.e("", "uploadfail");
				vh.iv_fail.setVisibility(View.VISIBLE);
				break;
			case DsConstant.HANDLER_NET_FAIL:// 推送失败
				Log.e("", "pushfail");
				ChatAdapter.ViewHolderText_R t_vhl = (ChatAdapter.ViewHolderText_R) vh;
				t_vhl.iv_fail.setVisibility(View.VISIBLE);
				break;
			case DsConstant.HANDLER_QINIU_NET_DOWNLOAD_PIC_OK:// 图片文件下载成功
				break;
			case DsConstant.HANDLER_QINIU_NET_DOWNLOAD_VOICE_OK:// 语音文件下载成功

				break;
			case DsConstant.HANDLER_QINIU_NET_DOWNLOAD_FAIL:// 文件下载失败
				vh.iv_fail.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			new Thread() {
				public void run() {
					// 将聊天存入数据库
					if (mbt != null)
					// new
					{
						saveMsg(mbt);
					}

				}
			}.start();
		}
	}

	@Override
	public void OnRefresh() {// 下拉刷新时候做的操作
		Log.e("", "MSGPAGERNUM=" + MSGPAGERNUM);
		m_lmdTask = new LoadMoreDataAsynTask();
		m_lmdTask.execute(DsApplication.user.getId(),
				DsApplication.currentFd.getId(), MSGPAGERNUM);
	}

	class LoadMoreDataAsynTask extends
			AsyncTask<Integer, Void, List<MsgBeanTable>> {

		@Override
		protected List<MsgBeanTable> doInBackground(Integer... arg) {
			return DsApplication.db
					.getChatRecordHistory(arg[0], arg[1], arg[2]);

		}

		@Override
		protected void onPostExecute(List<MsgBeanTable> result) {
			if (result != null) {
				Log.e("", "" + result.toString());
				if (result.size() != 0) {
					++MSGPAGERNUM;
					List<MsgBeanTable> mList = new ArrayList<MsgBeanTable>();
					// 需要将查出来的聊天记录顺序改变一下
					for (int i = result.size() - 1; i > -1; i--) {
						mList.add(result.get(i));
					}
					int position = m_chatAdapter.getCount(); // 获得为更新前的数据总数
					m_chatAdapter.setChatList(mList); // 更新数据
					mLv_ListView.setSelection(m_chatAdapter.getCount()
							- position - 1); // 更新后显示的位置
				}
			}
			mLv_ListView.onRefreshComplete();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (m_MediaPlayer == null) {
			m_MediaPlayer = new MediaPlayer();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (m_MediaPlayer != null) {
			if (m_MediaPlayer.isPlaying()) {
				m_MediaPlayer.stop();
				m_MediaPlayer = null;
			}
		}
	}

	private void addPushMsg(PushMessageTable pmt) {
		pmt.setPushTime(System.currentTimeMillis());
		String strPushmsg = BeanJsonConvert.beanListToJson(pmt.getLists());
		pmt.setPushContent(strPushmsg);
		DsApplication.db.writePushMsg(pmt);

	}

	/** 
	 * 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(m_MCBCReceiver);
		Log.e("",""+mHasNewMsg);
		if (m_chats.size() > 0 && mHasNewMsg) {
			MsgBeanTable lmbt = m_chats.get(m_chats.size() - 1);
			if (m_pushMsg != null) {
				m_pushMsg.getLists().clear(); // 清楚掉以前未显示的消息，从推送界面过来的
				m_pushMsg.setMsgs(0);
				m_pushMsg.setPusherId(DsApplication.currentFd.getSid());
				m_pushMsg.getLists().add(lmbt);
				addPushMsg(m_pushMsg);
				if (DsApplication.tempObject.containsKey("CurrentPushMsg")) {
					DsApplication.tempObject.remove("CurrentPushMsg");
				}
			} else {// 从好友列表过来的
				boolean flag = false;
				for (PushMessageTable mm : MessageFragment.m_msgs) {// 推送消息列表有该消息
					if (mm.getPusherId() == DsApplication.currentFd.getSid()
							&& mm.getMsgs() == 0 &&mm.getLists().get(0).getMsgType()!=DsConstant.HELP_FD_OK) {
						mm.getLists().clear();
						mm.getLists().add(lmbt);
						mm.setPusherId(DsApplication.currentFd.getSid());
						mm.setMsgs(0);
						addPushMsg(mm);
						flag = true;
						break;
					}
				}
				if (!flag) {// 推送列表无该消息
					PushMessageTable pmb = new PushMessageTable();
					pmb.setMsgs(0);
					pmb.setPushTime(System.currentTimeMillis());
					pmb.setPusherId(DsApplication.currentFd.getSid());
					pmb.setPusherName(DsApplication.currentFd.getUserName());
					pmb.setfPicPath(DsApplication.currentFd
							.getUserHeadPicture());
					pmb.getLists().add(lmbt);

					MessageFragment.m_msgs.add(pmb);
					addPushMsg(pmb);
					DsApplication.db.writePushMsg(pmb);
				}
			}
		} 
		MessageFragment.m_pushAdapter.notifyDataSetChanged();
		if (mBtp_fBitmap != null && mBtp_fBitmap.isRecycled()) {
			mBtp_fBitmap.recycle();
		}

		DsApplication.currentFd = null;
		mHasNewMsg=null;
	}
}
