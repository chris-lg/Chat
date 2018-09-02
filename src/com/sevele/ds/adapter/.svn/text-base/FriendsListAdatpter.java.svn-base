package com.sevele.ds.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.table.FriendTable;

/**
 * @author:liuge
 * @createTime:2015年4月21日
 * @descrption:好友列表适配器
 */
public class FriendsListAdatpter extends MyAdapter {
	private Context context;
	private static List<FriendTable> friends;
	@SuppressWarnings("unchecked")
	public FriendsListAdatpter(List<?> lists, Context context) {
		super(lists);
		friends = (List<FriendTable>) lists;
		this.context = context;
	}

	public void addFriendToList(FriendTable ft) {
		if (friends != null){
			friends.add(ft);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		LayoutInflater inflater = null;
		FriendTable friend = friends.get(index);
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.strangers_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.headPic = (ImageView) convertView
					.findViewById(R.id.stranger_head);
			viewHolder.friendName = (TextView) convertView
					.findViewById(R.id.friend_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.friendName.setVisibility(View.VISIBLE);
		if (friend.getUserHeadPicture() != null) {
			File file = new File(DsConstant.IMG_ROOT
					+ friend.getUserHeadPicture());
			if (!file.exists()) {
				DsApplication.bitmapUtils.display(viewHolder.headPic,
						DsConstant.MY_QINIU + friend.getUserHeadPicture()
								+ DsConstant.SMALL_PIC,
						new CustomBitmapLoadCallBack(friend));
			} else {
				DsApplication.bitmapUtils.display(viewHolder.headPic,
						file.getAbsolutePath());
			}
		} else {
			viewHolder.headPic
					.setImageResource(R.drawable.all_avatar_user_default);
		}
		viewHolder.friendName.setText(friends.get(index).getUserName());
		return convertView;
	}

	public static class ViewHolder {
		private ImageView headPic;
		private TextView friendName;
	}

	public class CustomBitmapLoadCallBack extends
			DefaultBitmapLoadCallBack<ImageView> {
		private FriendTable friend;

		public CustomBitmapLoadCallBack(FriendTable friend) {
			this.friend = friend;
		}

		@Override
		public void onLoading(ImageView container, String uri,
				BitmapDisplayConfig config, long total, long current) {
		}

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			fadeInDisplay(container, bitmap);
			FileOutputStream fileOutputStream = null;
			try {
				// 保存头像在本地

				String picPath = DsConstant.IMG_ROOT
						+ friend.getUserHeadPicture();
				fileOutputStream = new FileOutputStream(picPath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						fileOutputStream);
				fileOutputStream.flush();
				Log.e("", "" + picPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.transparent);

	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}
}
