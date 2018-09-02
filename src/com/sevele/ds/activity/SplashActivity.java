package com.sevele.ds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.example.decryptstranger.R;

/**
 * @author:liu ge
 * @createTime:2015年3月19日
 * @descrption:闪屏
 */
public class SplashActivity extends BaseActivity {
	private ImageView mIv_Splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.splash);
		mIv_Splash = (ImageView) findViewById(R.id.iv_splash);
		splashAnimation();
	}

	private void splashAnimation() {
		//闪屏动画
		AlphaAnimation splashAnimation = new AlphaAnimation(0.2f, 1.0f);
		splashAnimation.setDuration(2000);
		mIv_Splash.setAnimation(splashAnimation);
		splashAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {

			}
			@Override
			public void onAnimationRepeat(Animation arg0) {

			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				// 结束动画后就进入登录界面
				Intent l_intent = new Intent(SplashActivity.this, LoginActivity.class);
				SplashActivity.this.startActivity(l_intent);
				//退出闪屏界面和进入登录界面的动画
				overridePendingTransition(R.anim.login_in, R.anim.splash_out);
				SplashActivity.this.finish();
			}
		});

	}
	@Override
	public void widgetClick(View v) {

	}
}
