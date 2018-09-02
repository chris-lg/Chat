package com.sevele.ds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.example.decryptstranger.R;
import com.sevele.ds.activity.DecryptgameActivity;
import com.sevele.ds.activity.MainActivity;
import com.sevele.ds.activity.SensorActivity;
import com.sevele.ds.activity.StrangerShakedActivity;
import com.sevele.ds.utils.LogUtil;

/**
 * @author:liu ge
 * @createTime:2015年3月19日
 * @descrption:查找界面
 */
public class FindFragment extends Fragment implements OnClickListener {
	private RelativeLayout mRl_shaking; // 摇一摇
	private RelativeLayout mRl_shakedStrangers; // 摇到的人
	private RelativeLayout mRl_game;// 游戏

	private Intent m_Intent;
	private Animation m_Anim;//动画效果

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();//初始化
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		mRl_shaking = (RelativeLayout) getActivity().findViewById(
				R.id.relative_find);
		mRl_shakedStrangers = (RelativeLayout) getActivity().findViewById(
				R.id.relative_found_strangers);
		mRl_game = (RelativeLayout) getActivity().findViewById(R.id.relative_game);

		mRl_shaking.setOnClickListener(this);
		mRl_shakedStrangers.setOnClickListener(this);
		mRl_game.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_find:
			//摇一摇
			m_Anim = addAnimation();
			m_Anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					m_Intent = new Intent((MainActivity) getActivity(),
							SensorActivity.class);
					startActivity(m_Intent);
				}
			});
			mRl_shaking.startAnimation(m_Anim);
			break;
		case R.id.relative_found_strangers:
			//陌生人列表
			m_Anim = addAnimation();
			m_Anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					m_Intent = new Intent((MainActivity) getActivity(),
							StrangerShakedActivity.class);
					startActivity(m_Intent);
				}
			});
			mRl_shakedStrangers.startAnimation(m_Anim);
			break;
		case R.id.relative_game:
			//游戏
			m_Anim = addAnimation();
			m_Anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					m_Intent = new Intent((MainActivity) getActivity(),
							DecryptgameActivity.class);
					m_Intent.putExtra("Gogame", "" + 9999999);
					startActivity(m_Intent);
				}
			});
			mRl_game.startAnimation(m_Anim);
			break;
		default:
			break;
		}
	}

	/**
	 * 点击动画
	 * 
	 * @return anims
	 */
	private Animation addAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
		// 动画集
		AnimationSet anims = new AnimationSet(true);
		anims.addAnimation(alphaAnimation);
		// 设置动画时间 (作用到每个动画)
		anims.setDuration(500);
		return anims;
	}
}
