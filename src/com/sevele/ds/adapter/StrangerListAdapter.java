package com.sevele.ds.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.sevele.ds.table.StrangerTable;

/**
 * @author:liu ge
 * @param <T>
 * @createTime:2015年4月2日
 * @descrption:展示陌生人列表的adapter
 */
public class StrangerListAdapter extends MyAdapter {
	private Context context = null;
	private List<StrangerTable> strangers;

	@SuppressWarnings("unchecked")
	public StrangerListAdapter(List<?> lists, Context context) {
		super(lists);
		strangers = (List<StrangerTable>) lists;
		this.context = context;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder viewHolder = null;
		StrangerTable st=strangers.get(index);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.strangers_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.strangerHead = (ImageView) convertView
					.findViewById(R.id.stranger_head);
			viewHolder.strangerGender = (ImageView) convertView
					.findViewById(R.id.stranger_gender);
			viewHolder.strangerName = (TextView) convertView
					.findViewById(R.id.stranger_name);
			viewHolder.strangerDistance = (TextView) convertView
					.findViewById(R.id.stranger_distance);
			viewHolder.strangerAccess = (TextView) convertView
					.findViewById(R.id.stranger_access);
			
			viewHolder.info_layout = (RelativeLayout) convertView
					.findViewById(R.id.stranger_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 已经被解密的陌生人
		Log.e("",""+st.getUserHeadPicture());
		Log.e("",""+st.getUserName());
		viewHolder.info_layout.setVisibility(View.VISIBLE);
		if (st.isStrangerIsAccess()) {
			viewHolder.strangerHead.setImageBitmap(BitmapFactory
					.decodeFile(st.getUserHeadPicture()));

			if ("女".equals(st.getUserGender())) {
				viewHolder.strangerGender
						.setBackgroundResource(R.drawable.girl);
			} else {
				viewHolder.strangerGender
						.setBackgroundResource(R.drawable.boy);
			}
			viewHolder.strangerDistance.setText("");
			viewHolder.strangerAccess.setText("(已经解密)");
		} else {// 没有被解密
			viewHolder.strangerHead.setBackgroundResource(R.drawable.all_avatar_user_default);
			viewHolder.strangerAccess.setText("(解密后查看)");
		}
		viewHolder.strangerName.setText(st.getUserName());

		return convertView;
	}

	private static class ViewHolder {
		private RelativeLayout info_layout;
		private ImageView strangerHead;
		private ImageView strangerGender;
		private TextView strangerName;
		private TextView strangerDistance;
		private TextView strangerAccess;
	}

}
