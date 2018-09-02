package com.sevele.ds.adapter;

import java.util.HashMap;
import java.util.List;

import org.ddpush.im.v1.client.appserver.Pusher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.qiniu.common.GetToken;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.ddpush.OnlineService;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.utils.ImageDownLoader;
import com.sevele.ds.utils.ImageTool;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.TimeUtil;
import com.sevele.ds.utils.Util;
import com.sevele.ds.utils.onImageLoaderListenerImp;

/**
 * @author:liuge
 * @createTime:2015年4月21日
 * @descrption:聊天内容列表适配器
 */
public class ChatAdapter extends MyAdapter {

	public static final int VALUE_LEFT_TEXT = 0;
	public static final int VALUE_LEFT_IMAGE = 1;
	public static final int VALUE_LEFT_AUDIO = 2;
	public static final int VALUE_RIGHT_TEXT = 3;
	public static final int VALUE_RIGHT_IMAGE = 4;
	public static final int VALUE_RIGHT_AUDIO = 5;

	public static final int VALUE_TOTAL = 6;

	private onImageLoaderListenerImp imageLoadCallback;

	private Context context;
	private Handler handler;
	private HttpAPI httpApi;

	private Bitmap fBitmap; // 头像的bitmap

	private List<MsgBeanTable> chats;
	private MediaPlayer mMediaPlayer;
	private int mPosition;
	private LayoutInflater inflater;

	@SuppressWarnings("unchecked")
	@SuppressLint("HandlerLeak")
	public ChatAdapter(List<?> lists, final Context context, Bitmap fBitmap,
			final Handler handler, MediaPlayer mMediaPlayer) {
		super(lists);
		chats = (List<MsgBeanTable>) lists;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.handler = handler;
		this.fBitmap = fBitmap;
		this.mMediaPlayer = mMediaPlayer;
		httpApi = new HttpAPI(handler);
	}

	@Override
	// 返回布局类型
	public int getItemViewType(int position) {
		return chats.get(position).getShowType();
	}

	// 清楚数据源再添加新消息
	public void setChatList(List<MsgBeanTable> msgs) {
		chats.clear();
		chats.addAll(msgs);
		notifyDataSetChanged();
	}

	// 添加新数据
	public void upDateMsgByList(List<MsgBeanTable> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				chats.add(list.get(i));
			}
		}
		notifyDataSetChanged();
	}

	@Override
	// 返回布局总数
	public int getViewTypeCount() {
		return VALUE_TOTAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final MsgBeanTable chat = chats.get(position);
		mPosition = position;// 要显示的消息的位置
		int type = chat.getShowType();
		// 六个消息布局
		ViewHolderText_L viewHolderT_l = null;
		ViewHolderImg_L viewHolderI_l = null;
		ViewHolderAudio_L viewHolderA_l = null;

		ViewHolderText_R viewHolderT_r = null;
		ViewHolderImg_R viewHolderI_r = null;
		ViewHolderAudio_R viewHolderA_r = null;

		if (convertView == null) {// 从布局文件中加载view
			switch (type) {
			case VALUE_LEFT_TEXT:
				viewHolderT_l = new ViewHolderText_L();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
				viewHolderT_l.tv_chatContent_l = (TextView) convertView
						.findViewById(R.id.tv_txt);
				viewHolderT_l.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime);
				viewHolderT_l.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_txt);
				viewHolderT_l.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_txt);
				viewHolderT_l.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_l_txtFail);
				viewHolderT_l.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderT_l);
				break;

			case VALUE_LEFT_IMAGE:
				viewHolderI_l = new ViewHolderImg_L();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_image_left, null);
				viewHolderI_l.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime);
				viewHolderI_l.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_img);
				viewHolderI_l.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_l_imgFail);
				viewHolderI_l.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_img);
				viewHolderI_l.iv_chatImage_l = (ImageView) convertView
						.findViewById(R.id.iv_chatimg);
				viewHolderI_l.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderI_l);
				break;

			case VALUE_LEFT_AUDIO:
				viewHolderA_l = new ViewHolderAudio_L();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_voice_left, null);
				viewHolderA_l.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime);
				viewHolderA_l.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_l_voiceFail);
				viewHolderA_l.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_voice);
				viewHolderA_l.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_voice);
				viewHolderA_l.tvVoiceTime_l = (TextView) convertView
						.findViewById(R.id.voice_time);
				viewHolderA_l.tv_voiceImage_l = (TextView) convertView
						.findViewById(R.id.tv_voice_l);
				viewHolderA_l.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderA_l);
				break;
			// 右边
			case VALUE_RIGHT_TEXT:
				viewHolderT_r = new ViewHolderText_R();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
				viewHolderT_r.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime);
				viewHolderT_r.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_r_txtFail);
				viewHolderT_r.tv_chatContent_r = (TextView) convertView
						.findViewById(R.id.tv_chattxt_r);
				viewHolderT_r.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_txt_r);
				viewHolderT_r.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_txt_r);
				viewHolderT_r.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderT_r);
				break;

			case VALUE_RIGHT_IMAGE:
				viewHolderI_r = new ViewHolderImg_R();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_image_right, null);
				viewHolderI_r.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime_r);
				viewHolderI_r.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_r_imgFail);
				viewHolderI_r.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_img_r);
				viewHolderI_r.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_img_r);
				viewHolderI_r.iv_chatImage_r = (ImageView) convertView
						.findViewById(R.id.iv_img_r);
				viewHolderI_r.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderI_r);
				break;

			case VALUE_RIGHT_AUDIO:
				viewHolderA_r = new ViewHolderAudio_R();
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_voice_right, null);
				viewHolderA_r.tv_chatTime = (TextView) convertView
						.findViewById(R.id.tv_chatTime);
				viewHolderA_r.iv_fail = (ImageView) convertView
						.findViewById(R.id.iv_r_voiceFail);
				viewHolderA_r.pb_progress = (ProgressBar) convertView
						.findViewById(R.id.pb_voice_r);
				viewHolderA_r.iv_headpic = (ImageView) convertView
						.findViewById(R.id.ivh_voice_r);
				viewHolderA_r.tvVoiceTime_r = (TextView) convertView
						.findViewById(R.id.tv_voiceTime_r);
				viewHolderA_r.tv_voiceImage_r = (TextView) convertView
						.findViewById(R.id.tv_voice_r);
				viewHolderA_r.iv_fail.setVisibility(View.GONE);
				convertView.setTag(viewHolderA_r);
				break;
			default:
				break;
			}

		} else {// 从convertView当中取出view 经行复用
			switch (type) {
			case VALUE_LEFT_TEXT: // 接受到问价消息 自动显示
				viewHolderT_l = (ViewHolderText_L) convertView.getTag();
				viewHolderT_l.iv_fail.setVisibility(View.GONE);
				break;
			case VALUE_LEFT_IMAGE:// 接收到图片先下载再显示
				viewHolderI_l = (ViewHolderImg_L) convertView.getTag();
				viewHolderI_l.iv_fail.setVisibility(View.GONE);
				break;
			case VALUE_LEFT_AUDIO:
				viewHolderA_l = (ViewHolderAudio_L) convertView.getTag();
				viewHolderA_l.iv_fail.setVisibility(View.GONE);
				break;
			case VALUE_RIGHT_TEXT:
				viewHolderT_r = (ViewHolderText_R) convertView.getTag();
				viewHolderT_r.iv_fail.setVisibility(View.GONE);
				break;
			case VALUE_RIGHT_IMAGE:
				viewHolderI_r = (ViewHolderImg_R) convertView.getTag();
				viewHolderI_r.iv_fail.setVisibility(View.GONE);
				break;
			case VALUE_RIGHT_AUDIO:
				viewHolderA_r = (ViewHolderAudio_R) convertView.getTag();
				viewHolderA_r.iv_fail.setVisibility(View.GONE);
				break;
			default:
				break;
			}

		}
		switch (type) {
		case VALUE_LEFT_TEXT: // 收到文字消息 只有接收到和没有接收到这两个状态 没有接收到也不显示
								// 所以这里只要问接受文字消息直接显示
			if (fBitmap != null) {
				viewHolderT_l.iv_headpic.setImageBitmap(fBitmap);
			}
			viewHolderT_l.tv_chatTime.setText(TimeUtil.getChatTime(chat.getChatTime()));
			viewHolderT_l.tv_chatContent_l.setText(chat.getMsgContent());
			viewHolderT_l.pb_progress.setVisibility(View.GONE);
			viewHolderT_l.iv_fail.setVisibility(View.GONE);

			break;
		case VALUE_LEFT_IMAGE:// 收到图片，下载后展示
			if (fBitmap != null) {
				viewHolderI_l.iv_headpic.setImageBitmap(fBitmap);
			}
			viewHolderI_l.tv_chatTime.setText(TimeUtil.getChatTime(chat
					.getChatTime()));
			viewHolderI_l.iv_chatImage_l
					.setImageResource(R.drawable.zf_default_message_image);
			String subUrl = chat.getMsgContent();
			if (subUrl != null) {
				viewHolderI_l.iv_chatImage_l.setTag(subUrl); // 设置tag 防止错位显示
			}
			viewHolderI_l.iv_fail.setVisibility(View.GONE);
			switch (chat.getLoadState()) {
			case DsConstant.NO_PUSH_OR_LOAD:// 收到时 开始下载
				chat.setLoadState(DsConstant.LOAING_OR_PUSHING);
				imageLoadCallback = new onImageLoaderListenerImp(
						viewHolderI_l.iv_chatImage_l);// 下载成功后的回调显示
				Bitmap bitmap = DsApplication.imgDownLoader.downloadImage(
						subUrl, chat, imageLoadCallback);
				break;
			case DsConstant.LOAING_OR_PUSHING: // 正在下载
				viewHolderI_l.pb_progress.setVisibility(View.VISIBLE);
				break;
			case DsConstant.PUSH_LOAD_OK:// 是下载成功了的，从sd卡或者缓存中读取显示
				String subUrlo = chat.getMsgContent();
				viewHolderI_l.iv_chatImage_l.setTag(subUrlo);
//				if (chat.getMsgContent() != null) {
//					if (!chat.getMsgContent().equals(viewHolderI_l.iv_chatImage_l.getTag())) {
//						DsApplication.bitmapUtils.display(viewHolderI_l.iv_chatImage_l,
//								DsConstant.IMG_ROOT + chat.getMsgContent());
//						viewHolderI_l.iv_chatImage_l.setTag(chat.getMsgContent());
//					}
//				}
//				if(subUrlo!=)
				imageLoadCallback = new onImageLoaderListenerImp(
						viewHolderI_l.iv_chatImage_l);
				ImageDownLoader idlo = new ImageDownLoader(context);
				Bitmap bitmapo = idlo.downloadImage(subUrlo, chat,
						imageLoadCallback);
				if (bitmapo != null) {
					viewHolderI_l.iv_chatImage_l.setImageBitmap(bitmapo);
					viewHolderI_l.pb_progress.setVisibility(View.GONE);
				}
				break;
			case DsConstant.PUSH_LOAD_FAIL:
				viewHolderI_l.iv_fail.setVisibility(View.VISIBLE);
				viewHolderI_l.pb_progress.setVisibility(View.GONE);

				break;
			default:
				break;
			}

			break;
		case VALUE_LEFT_AUDIO:// 收到语音，先下载
			if (fBitmap != null) {
				viewHolderA_l.iv_headpic.setImageBitmap(fBitmap);
			}
			viewHolderA_l.iv_fail.setTag(chat.getChatTime());

			viewHolderA_l.tv_chatTime.setText(TimeUtil.getChatTime(chat
					.getChatTime()));
			viewHolderA_l.tvVoiceTime_l.setText(chat.getVoiceTime() + "\"");
			viewHolderA_l.tv_voiceImage_l
					.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.chatto_voice_playing_left, 0);
			viewHolderA_l.iv_fail.setVisibility(View.GONE);// 防止错位
			switch (chat.getLoadState()) {
			case DsConstant.NO_PUSH_OR_LOAD: // 接收到语音信号，下载语音
				chat.setLoadState(DsConstant.LOAING_OR_PUSHING);
				downFile(chat, mPosition);
				break;
			case DsConstant.LOAING_OR_PUSHING: // 当屏幕返回到屏幕中某条消息正在被发送时候，就继续发送
				viewHolderA_l.pb_progress.setVisibility(View.VISIBLE); // 此处不做处理（未验证）
				break;
			case DsConstant.PUSH_LOAD_OK: // 推送消息成功了的，直接显示
				viewHolderA_l.pb_progress.setVisibility(View.GONE);
				break;
			case DsConstant.PUSH_LOAD_FAIL: // 推送失败
				viewHolderA_l.iv_fail.setVisibility(View.VISIBLE);
				viewHolderA_l.pb_progress.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			break;
		case VALUE_RIGHT_TEXT:// 发送文本消息
			viewHolderT_r.iv_fail.setTag(mPosition);
			viewHolderT_r.tv_chatContent_r.setTag(chat);
			
			if (DsApplication.uBitmap != null) {
				viewHolderT_r.iv_headpic.setImageBitmap(DsApplication.uBitmap);
			} else {
				viewHolderT_r.iv_headpic
						.setImageResource(R.drawable.all_avatar_user_default);
			}
			viewHolderT_r.tv_chatTime.setText(TimeUtil.getChatTime(chat
					.getChatTime()));
			viewHolderT_r.tv_chatContent_r.setText(chat.getMsgContent());
			switch (chat.getLoadState()) {
			case DsConstant.NO_PUSH_OR_LOAD:
				viewHolderT_r.pb_progress.setVisibility(View.VISIBLE);
				chat.setLoadState(DsConstant.LOAING_OR_PUSHING);
				sendPush(DsApplication.currentFd.getSid(),
						BeanJsonConvert.beanToJosn(chat), mPosition, chat);
				break;
			case DsConstant.LOAING_OR_PUSHING:
				viewHolderT_r.pb_progress.setVisibility(View.VISIBLE);
				viewHolderT_r.iv_fail.setVisibility(View.GONE);
				break;
			case DsConstant.PUSH_LOAD_OK:
				viewHolderT_r.pb_progress.setVisibility(View.GONE);
				viewHolderT_r.iv_fail.setVisibility(View.GONE);
				break;
			case DsConstant.PUSH_LOAD_FAIL:
				viewHolderT_r.iv_fail.setVisibility(View.VISIBLE);
				viewHolderT_r.pb_progress.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			break;
		case VALUE_RIGHT_IMAGE:// 发送图片消息
			viewHolderI_r.iv_chatImage_r.setTag(chat);
			viewHolderI_r.iv_fail.setTag(mPosition);
			if (DsApplication.uBitmap != null) {
				viewHolderI_r.iv_headpic.setImageBitmap(DsApplication.uBitmap);
			} else {
				viewHolderI_r.iv_headpic
						.setImageResource(R.drawable.all_avatar_user_default);
			}
			
			viewHolderI_r.tv_chatTime.setText(TimeUtil.getChatTime(chat.getChatTime()));
			viewHolderI_r.iv_chatImage_r.setImageBitmap(ImageTool.createImageThumbnail(chat.getMsgContent()));
			switch (chat.getLoadState()) {
			case DsConstant.NO_PUSH_OR_LOAD: // 从未处理过的消息，发送图片
				chat.setLoadState(DsConstant.LOAING_OR_PUSHING);
				viewHolderI_r.pb_progress.setVisibility(View.VISIBLE);
				upLoadFile(chat, DsConstant.HANDLER_PICMSG_OK, mPosition);
				
				break;
			case DsConstant.LOAING_OR_PUSHING: // 当屏幕返回到屏幕中某条消息正在被发送时候，就继续发送
				viewHolderI_r.iv_fail.setVisibility(View.GONE);	
				viewHolderI_r.pb_progress.setVisibility(View.VISIBLE);// 此处不做处理（未验证）
				break;
			case DsConstant.PUSH_LOAD_OK: // 推送消息成功了的，直接显示
				viewHolderI_r.pb_progress.setVisibility(View.GONE);
				viewHolderI_r.iv_fail.setVisibility(View.GONE);
				break;
			case DsConstant.PUSH_LOAD_FAIL: // 推送失败
				viewHolderI_r.iv_fail.setVisibility(View.VISIBLE);
				viewHolderI_r.pb_progress.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			break;
		case VALUE_RIGHT_AUDIO:// 发送语音消息
			viewHolderA_r.tv_voiceImage_r.setTag(chat);
			viewHolderA_r.iv_fail.setTag(mPosition);
			if (DsApplication.uBitmap != null) {
				viewHolderA_r.iv_headpic.setImageBitmap(DsApplication.uBitmap);
			} else {
				viewHolderA_r.iv_headpic
						.setImageResource(R.drawable.all_avatar_user_default);
			}

			viewHolderA_r.tv_chatTime.setText(TimeUtil.getChatTime(chat
					.getChatTime()));
			viewHolderA_r.tvVoiceTime_r.setText(chat.getVoiceTime() + "\"");
			viewHolderA_r.tv_voiceImage_r
					.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.chatto_voice_playing, 0);
			switch (chat.getLoadState()) {
			case DsConstant.NO_PUSH_OR_LOAD: // 从未处理过的消息，下载语音消息
				chat.setLoadState(DsConstant.LOAING_OR_PUSHING);
				viewHolderA_r.pb_progress.setVisibility(View.VISIBLE);
				upLoadFile(chat, DsConstant.HANDLER_VOICEMSG_OK, mPosition);
				break;
			case DsConstant.LOAING_OR_PUSHING: // 当屏幕返回到屏幕中某条消息正在被发送时候，就继续发送
				viewHolderA_r.iv_fail.setVisibility(View.GONE);	// 此处不做处理（未验证）
				viewHolderA_r.pb_progress.setVisibility(View.VISIBLE);

				break;
			case DsConstant.PUSH_LOAD_OK: // 推送或者下载成功的消息，直接显示
				viewHolderA_r.pb_progress.setVisibility(View.GONE);
				viewHolderA_r.iv_fail.setVisibility(View.GONE);
				break;
			case DsConstant.PUSH_LOAD_FAIL: // 推送或者下载失败
				viewHolderA_r.iv_fail.setVisibility(View.VISIBLE);
				viewHolderA_r.pb_progress.setVisibility(View.GONE);
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
		// 注册语音监听
		if (viewHolderA_l != null) {
			viewHolderA_l.tv_voiceImage_l
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (chat.getShowType() == VALUE_LEFT_AUDIO) {
								playMusic(chat.getMsgContent());
							}
						}
					});
		}

		if (viewHolderA_r != null) {//语音监听
			viewHolderA_r.tv_voiceImage_r
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (chat.getShowType() == VALUE_RIGHT_AUDIO) {
								playMusic(chat.getMsgContent());
							}
						}
					});
		}
		if (viewHolderT_r != null) {//重新发送文字消息
			if (viewHolderT_r.iv_fail.getVisibility() == View.VISIBLE) {
				final ViewHolderText_R vhTr = viewHolderT_r;
				viewHolderT_r.iv_fail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						int pos=(Integer)vhTr.iv_fail.getTag();
						vhTr.iv_fail.setVisibility(View.GONE);
						MsgBeanTable msg=(MsgBeanTable) vhTr.tv_chatContent_r.getTag();
						msg.user=null;
						msg.friend=null;
						vhTr.pb_progress.setVisibility(View.VISIBLE);
						sendPush(DsApplication.currentFd.getSid(),
								BeanJsonConvert.beanToJosn(msg), pos,
								msg);
						
					}
				});

			}

		}
		if (viewHolderI_r != null) {//重新发送图片
			if (viewHolderI_r.iv_fail.getVisibility() == View.VISIBLE) {
				final ViewHolderImg_R vhir = viewHolderI_r;
				viewHolderI_r.iv_fail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						int pos=(Integer)vhir.iv_fail.getTag();
						vhir.iv_fail.setVisibility(View.GONE);
						MsgBeanTable msg=(MsgBeanTable) vhir.iv_chatImage_r.getTag();
						msg.user=null;
						msg.friend=null;
						vhir.pb_progress.setVisibility(View.VISIBLE);
						upLoadFile(msg, DsConstant.HANDLER_PICMSG_OK, pos);
					}
				});
			}
		}
		if (viewHolderA_r != null) {//重新发送语音
			if (viewHolderA_r.iv_fail.getVisibility() == View.VISIBLE) {
				final ViewHolderAudio_R vhar = viewHolderA_r;
				viewHolderA_r.iv_fail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						int pos=(Integer)vhar.iv_fail.getTag();
						vhar.iv_fail.setVisibility(View.GONE);
						MsgBeanTable msg=(MsgBeanTable) vhar.tv_voiceImage_r.getTag();
						msg.user=null;
						msg.friend=null;
						vhar.pb_progress.setVisibility(View.VISIBLE);
						upLoadFile(msg, DsConstant.HANDLER_VOICEMSG_OK, pos);
					}
				});
			}
		}

		return convertView;
	}

	public static abstract class BaseViewHolder {
		public TextView tv_chatTime;// 聊天时间
		public ImageView iv_headpic;// 头像
		public ProgressBar pb_progress;// 进度
		public ImageView iv_fail;// 上传或者下载失败显示的图片
	}

	public static class ViewHolderText_L extends BaseViewHolder {
		public TextView tv_chatContent_l;// 聊天内容
	}

	public static class ViewHolderText_R extends BaseViewHolder {
		public TextView tv_chatContent_r;// 聊天内容
	}

	public static class ViewHolderImg_R extends BaseViewHolder {
		public ImageView iv_chatImage_r;// 图片内容
	}

	public static class ViewHolderImg_L extends BaseViewHolder {
		public ImageView iv_chatImage_l;// 图片内容
	}

	public static class ViewHolderAudio_L extends BaseViewHolder {
		public TextView tv_voiceImage_l;// 语音图像
		public TextView tvVoiceTime_l;// 语音时间
	}

	public static class ViewHolderAudio_R extends BaseViewHolder {
		public TextView tv_voiceImage_r;// 语音图像
		public TextView tvVoiceTime_r;// 语音时间
	}

	private void downFile(final MsgBeanTable chat, final int pos) {
		final String fileUrl = DsConstant.MY_QINIU + chat.getMsgContent();
		// 保存文件地点
		final String localName = DsConstant.AUDIO_ROOT + chat.getMsgContent();
		chat.setMsgContent(localName); // 换成本地语音文件内容
		new Thread() {
			public void run() {
				httpApi.requestFile(fileUrl, localName, chat, pos);
			};
		}.start();
	}

	/**
	 * 上传图片文件和语音文件到七牛云,上传成功后请求服务器推送至好友
	 * 
	 * @param chat
	 *            具体的某一条内容
	 * @param msgWhat
	 *            上传成功的msg what值
	 */
	private void upLoadFile(final MsgBeanTable chat, final int msgWhat,
			final int position) {
		new Thread() {
			public void run() {
				String token = GetToken.getHeadpicToken();
				String key = null;
				if (chat.getShowType() == DsConstant.VALUE_RIGHT_AUDIO)
					key = DsApplication.user.getId()
							+ System.currentTimeMillis() + ".amr";
				else if (chat.getShowType() == DsConstant.VALUE_RIGHT_IMAGE) {
					key = DsApplication.user.getId()
							+ System.currentTimeMillis() + ".jpg";
				}
				Log.e("",""+chat.getMsgContent());
				httpApi.upLoadFileToQiNiu(token, chat.getMsgContent(), key,
						msgWhat, mapMsg(chat, key), position, chat);
			};
		}.start();
	}

	/**
	 * 自定义推送
	 * 
	 * @param targetUserName
	 */
	private void sendPush(int targetUserName, String sendData, Integer pos,
			final MsgBeanTable chat) {
		byte[] uuid = null;
		try {
			uuid = Util.md5Byte(String.valueOf(targetUserName));
		} catch (Exception e) {
			LogUtil.LogTest("错误：" + uuid);
			return;
		}
		byte[] msg = null;
		try {
			msg = sendData.getBytes("UTF-8");
		} catch (Exception e) {
			LogUtil.LogTest("错误：" + msg);
			return;
		}
		new pushThread(context, uuid, msg, pos, chat).start();
	}

	/**
	 * 将要推送的消息组装成map
	 * 
	 * @param chat
	 * @return
	 */
	private HashMap<String, Object> mapMsg(MsgBeanTable chat, String key) {
		String uri = chat.getMsgContent(); // 将文件的位置临时保存在tempUri中
		chat.setMsgContent(key);          // 该文件在七牛上的文件名字
		String msg = BeanJsonConvert.beanToJosn(chat);
		Log.e("","msg ha--"+msg.getBytes().length);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pId", DsApplication.currentFd.getSid());
		map.put("pushMsg", msg);
		chat.setMsgContent(uri); // 还是保存自己文件的位置
		return map;
	}

	/**
	 * @Description 播放语音
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
				return;
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class pushThread extends Thread {

		private Context context;
		private byte[] uuid;
		private byte[] msg;
		private int pos;
		private MsgBeanTable chat;

		public pushThread(Context context, byte uuid[], byte msg[], int pos,
				MsgBeanTable chat) {

			this.context = context;
			this.msg = msg;
			this.pos = pos;
			this.uuid = uuid;
			this.chat = chat;
		}

		@Override
		public void run() {
			boolean result = false;
			Pusher pusher = null;
			Intent startSrv = new Intent(context, OnlineService.class);
			startSrv.putExtra("CMD", "TOAST");
			try {
				pusher = new Pusher(HttpUrl.DD_SERVER_IP,
						Integer.valueOf(HttpUrl.PUSH_PORT), 1000 * 5);
				result = pusher.push0x20Message(uuid, msg);
				if (result) {
					chat.setLoadState(DsConstant.PUSH_LOAD_OK);
					handler.obtainMessage(DsConstant.HANDLER_TXTMSG_OK, pos, 0,
							chat).sendToTarget();
				} else {
					startSrv.putExtra("TEXT", "发送失败！格式有误");
					chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
					handler.obtainMessage(DsConstant.HANDLER_TXTMSG_FAIL, pos,
							0, chat).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
				startSrv.putExtra("TEXT","发送失败！");
				chat.setLoadState(DsConstant.PUSH_LOAD_FAIL);
				handler.obtainMessage(DsConstant.HANDLER_NET_FAIL, pos, 0, chat)
						.sendToTarget();
			} finally {
				if (pusher != null) {
					try {
						pusher.close();
					} catch (Exception e) {
					}
					;
				}
			}
			context.startService(startSrv);
		}

	}

}
