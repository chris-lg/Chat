package com.sevele.ds.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author Maozhiqi
 * @time:2015年4月26日
 * @descrption:游戏视图组件成员
 */
public class Card extends FrameLayout {

	public static int int_Width;// 宽度
	private TextView mTv_label;
	private int int_Num = 0;

	public Card(Context context) {
		super(context);
		LayoutParams lp = null;
		View background = new View(getContext());
		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		background.setBackgroundColor(Color.WHITE);
		addView(background, lp);
		mTv_label = new TextView(getContext());
		mTv_label.setTextSize(28);
		mTv_label.setTextColor(Color.RED);
		mTv_label.setGravity(Gravity.CENTER);
		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(mTv_label, lp);
		setNum(0);
	}

	public int getNum() {
		return int_Num;
	}

	public void setNum(int num) {
		this.int_Num = num;
		if (num <= 0) {
			mTv_label.setText("");
		} else {
			mTv_label.setText(String.valueOf(num));
		}
		switch (num) {
		case 1:
			mTv_label.setBackgroundColor(0xffeee4da);
			break;
		case 2:
			mTv_label.setBackgroundColor(0xffede0c8);
			break;
		case 3:
			mTv_label.setBackgroundColor(0xfff2b179);
			break;
		case 4:
			mTv_label.setBackgroundColor(0xfff59563);
			break;
		case 5:
			mTv_label.setBackgroundColor(0xfff67c5f);
			break;
		case 6:
			mTv_label.setBackgroundColor(0xfff65e3b);
			break;
		case 7:
			mTv_label.setBackgroundColor(0xffedcf72);
			break;
		case 8:
			mTv_label.setBackgroundColor(0xffedcc61);
			break;
		case 9:
			mTv_label.setBackgroundColor(0xffedc850);
			break;
		default:
			mTv_label.setBackgroundColor(0xff3c3a32);
			break;
		}
	}

	public boolean equals(Card another) {
		return getNum() == another.getNum();
	}

	public TextView getLabel() {
		return mTv_label;
	}

	/**
	 * 添加格子动画
	 */
	public void addScaleAnimation() {
		ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1000);
		setAnimation(null);
		getLabel().startAnimation(sa);
	}
}
