package com.sevele.ds.activity;

import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.pzf.liaotian.album.DisplayBitmapCache;
import com.pzf.liaotian.bean.album.ImageItem;
import com.sevele.ds.view.CustomViewPager;

/**
 *@desc:图片预览
 *@author:liu ge
 *@createTime:2015年5月24日
 *@descrption:TODO
 */
public class PreviewActivity extends TitleBarActivity implements
		OnPageChangeListener {

	private CustomViewPager mViewPager;
	private ImageView[] mTips;
	private ImageView[] mImageViews;
	private ViewGroup mGroup;
	// private final ImageGridAdapter2 mAdapter =
	// ImageGridActivity.getAdapter();
	private Map<Integer, ImageItem> mSelectedMap = ImageGridActivity
			.getSelectMap();
	private int mCurImagePosition = -1;

	// private IMServiceHelper imServiceHelper = new IMServiceHelper();
	private void initTitle() {
		TextView mBack = new TextView(this);
		mBack.setBackgroundResource(R.drawable.ic_back);
		setTitleLeft(mBack);

		TextView tvTitle = new TextView(this);
		tvTitle.setText(R.string.album);
		tvTitle.setTextSize(getResources().getDimension(R.dimen.title_textsize));
		tvTitle.setTextColor(getResources().getColor(R.color.white));
		setTitleMiddle(tvTitle);

	}

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_takephoto_preview);
		initTitle();
		initView();
		loadView();
	}

	private void initView() {

		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// imServiceHelper.disconnect(this);
	}

	private void loadView() {
		mImageViews = new ImageView[mSelectedMap.size()];

		if (mSelectedMap.size() > 1) {
			mTips = new ImageView[mSelectedMap.size()];
			for (int i = 0; i < mTips.length; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setLayoutParams(new LayoutParams(10, 10));
				mTips[i] = imageView;
				if (i == 0) {
					mTips[i].setBackgroundResource(R.drawable.ic_takephoto_default_dot_down);
				} else {
					mTips[i].setBackgroundResource(R.drawable.ic_takephoto_default_dot_up);
				}
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
				layoutParams.leftMargin = 5;
				layoutParams.rightMargin = 5;
				mGroup.addView(imageView, layoutParams);
			}
		}

		Iterator<?> it = mSelectedMap.keySet().iterator();
		int index = -1;
		while (it.hasNext()) {
			int key = (Integer) it.next();
			ImageItem item = mSelectedMap.get(key);
			ImageView imageView = new ImageView(this);
			mImageViews[++index] = imageView;
			Bitmap bmp = DisplayBitmapCache.getInstance(PreviewActivity.this)
					.get(item.getImagePath());
			if (bmp == null)
				bmp = DisplayBitmapCache.getInstance(PreviewActivity.this).get(
						item.getThumbnailPath());
			if (bmp != null)
				imageView.setImageBitmap(bmp);
			if (index == 0) {
				mCurImagePosition = key;
			}
		}

		// 设置view pager
		mViewPager.setAdapter(new PreviewAdapter());
		mViewPager.setOnPageChangeListener(this);
		if (mSelectedMap.size() == 1) {
			mViewPager.setScanScroll(false);
		} else {
			mViewPager.setScanScroll(true);
		}
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < mTips.length; i++) {
			if (i == selectItems) {
				mTips[i].setBackgroundResource(R.drawable.ic_takephoto_default_dot_down);
			} else {
				mTips[i].setBackgroundResource(R.drawable.ic_takephoto_default_dot_up);
			}
		}
	}

	@Override
	public void onPageSelected(int position) {
		@SuppressWarnings("rawtypes")
		Iterator it = mSelectedMap.keySet().iterator();
		int index = -1;
		while (it.hasNext()) {
			int key = (Integer) it.next();
			if (++index == position) {
				mCurImagePosition = key;// 对应适配器中图片列表的真实位置
				// if (mSelectedMap.get(key).isSelected()) {
				// mSelectIv
				// .setImageResource(R.drawable.ic_takephoto_album_img_selected);
				// } else {
				// mSelectIv
				// .setImageResource(R.drawable.ic_takephoto_album_img_select_nor);
				// }
			}
		}
		setImageBackground(position);
	}

	public class PreviewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewGroup) container).addView(mImageViews[position]);
			} catch (Exception e) {
			}
			return mImageViews[position];
		}
	}

}
