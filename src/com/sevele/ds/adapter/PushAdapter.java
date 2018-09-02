package com.sevele.ds.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.lidroid.xutils.BitmapUtils;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.PushMessageTable;

/**
 * @author:liu ge
 * @createTime:2015年4月12日
 * @descrption:展示推送消息的adapter
 */
public class PushAdapter extends MyAdapter {
	private Context context;
	private List<PushMessageTable> messages;
	private BitmapUtils bUtils;

	@SuppressWarnings("unchecked")
	public PushAdapter(List<?> lists, Context context) {
		super(lists);
		messages = (List<PushMessageTable>) lists;
		this.context = context;
		bUtils = new BitmapUtils(context);
	}

	public void addItemPushMsg(PushMessageTable msg) {
		if (messages.size() == 0) {
			messages.add(msg);
		} else {
			messages.add(0, msg);
		}
	}

	public void addItemMsgBean(MsgBeanTable mb, int position, int msgNoRead) {
		messages.get(position).getLists().add(mb);
		messages.get(position).setMsgs(msgNoRead);
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		LayoutInflater inflater = null;
		PushMessageTable mBean = messages.get(index);
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.strangers_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.headPic = (ImageView) convertView
					.findViewById(R.id.stranger_head);
			viewHolder.pusherName = (TextView) convertView
					.findViewById(R.id.stranger_name);
			viewHolder.pushContent = (TextView) convertView
					.findViewById(R.id.stranger_distance);

			viewHolder.lLayout = (LinearLayout) convertView
					.findViewById(R.id.push_layout);
			viewHolder.pushMessageCount = (TextView) convertView
					.findViewById(R.id.messageCount);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		convertView.findViewById(R.id.stranger_info)
				.setVisibility(View.VISIBLE);

		if (mBean.getfPicPath() != null) {
			if (!mBean.getfPicPath().equals(viewHolder.headPic.getTag())) {
				DsApplication.bitmapUtils.display(viewHolder.headPic,
						DsConstant.IMG_ROOT + mBean.getfPicPath());
				viewHolder.headPic.setTag(mBean.getfPicPath());
			}
		}else{
			viewHolder.headPic.setImageResource(R.drawable.all_avatar_user_default);
		}

		if (mBean.getMsgs() != 0) {// 未读消息条数不为0 的时候
			viewHolder.lLayout.setVisibility(View.VISIBLE);
			viewHolder.pushMessageCount.setText(mBean.getMsgs() + "");// 显示未读消息tia
		} else {
			viewHolder.lLayout.setVisibility(View.GONE);
			viewHolder.pushMessageCount.setText("");
		}
		int lastIndex = -1;
		lastIndex = mBean.getLists().size() - 1;
		MsgBeanTable pMc = mBean.getLists().get(lastIndex);

		if (pMc.getSid() ==DsApplication.user.getId() ) {
			viewHolder.pusherName.setText("我说：");
		} else {
			viewHolder.pusherName.setText(mBean.getPusherName() + "说：");
		}

		switch (pMc.getMsgType()) {
		case DsConstant.TXT_MSG:
			if (pMc.getMsgContent().length() < 25)
				viewHolder.pushContent.setText(pMc.getMsgContent());
			else
				viewHolder.pushContent.setText(pMc.getMsgContent().substring(0,
						25)
						+ "...");
			break;
		case DsConstant.VOICE_MSG:
			viewHolder.pushContent.setText("//语音");
			break;
		case DsConstant.PIC_MSG:
			viewHolder.pushContent.setText("//图片");
			break;

		case DsConstant.ADD_FRIEND_MSG:
			viewHolder.pushContent.setText("//" + "我已经把你为好友了。。。");
			break;
		case DsConstant.GAME_HELP:
			viewHolder.pushContent.setText("//" + "帮我解密。。。");
			break;
		case DsConstant.DES_OK:
			viewHolder.pushContent.setText("//" + "你要的人，我给你解密了。。。");
		case DsConstant.HELP_FD_OK:
			viewHolder.pushContent.setText("//" + "你已经帮助好友解密了");
			break;
		default:
			break;
		}
		return convertView;
	}

	public static class ViewHolder {
		private ImageView headPic;
		private TextView pusherName;
		private TextView pushContent;

		private LinearLayout lLayout;
		private TextView pushMessageCount;
	}

//	public class CustomBitmapLoadCallBack extends
//			DefaultBitmapLoadCallBack<ImageView> {
//		private PushMessageBean pmb;
//
//		public CustomBitmapLoadCallBack(PushMessageBean pmb) {
//			this.pmb = pmb;
//		}
//
//		@Override
//		public void onLoading(ImageView container, String uri,
//				BitmapDisplayConfig config, long total, long current) {
//		}
//
//		@Override
//		public void onLoadCompleted(ImageView container, String uri,
//				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
//			pmb.setfBitmap(bitmap);
//		}
//	}

}
