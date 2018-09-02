package com.sevele.ds.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.android.adapter.CommonBaseAdapter;
import com.baidu.android.itemview.helper.IData;
import com.baidu.android.itemview.helper.IStyle;
import com.pzf.liaotian.bean.album.ImageBucket;
import com.sevele.ds.view.PickPhotoSingleTypeView;

/**
 * @author:liuge
 * @createTime:2015年4月21日
 * @descrption:图集adapter
 */
public class ImageBucketAdapter extends CommonBaseAdapter<ImageBucket> {

	public static final int MESSAGE_TYPE_INVALID = -1;
	private static final int VIEW_TYPE_COUNT = 9;

	public ImageBucketAdapter(Context arg0) {
		super(arg0);
	}

	@Override
	protected View generateView(int position, View convertView,
			ViewGroup viewGroup) {
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case 0X0001:
				convertView = new PickPhotoSingleTypeView(mContext);
				break;

			default:
				break;
			}
		}

		IStyle item = mData.get(position);
		if (item != null) {
			IData<IStyle> data = (IData<IStyle>) convertView;
			data.setData(item);
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {

		if (position >= mData.size()) {
			return MESSAGE_TYPE_INVALID;
		}
		return getItem(position).getStyle();
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

}
