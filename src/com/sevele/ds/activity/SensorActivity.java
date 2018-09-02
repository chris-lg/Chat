package com.sevele.ds.activity;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;

/**
 * @author Maozhiqi
 * @time 2015年3月26日
 * @descrption 摇一摇
 */
public class SensorActivity extends BaseActivity implements SensorEventListener {

	private ImageView mIv_handshake;// 握手Imageview
	private TextView mTv_Sensor_Title;// 界面标题
	private RelativeLayout mRl_Back;// 返回
	private SensorManager m_SensorManager = null;// 传感器管理器
	private Vibrator m_Vibrator = null;// 手机震动管理器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_sensor);
		m_SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		m_Vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		mIv_handshake = (ImageView) findViewById(R.id.img_handshake);
		mTv_Sensor_Title = (TextView) findViewById(R.id.txtTitle_banner);
		mTv_Sensor_Title.setText(R.string.txtSensor_title);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);

		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			// 返回
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		m_SensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_SensorManager.registerListener(this,
				m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math
					.abs(values[2]) > 15)) {
				// 握手图片晃动动画
				Animation anim = AnimationUtils.loadAnimation(
						SensorActivity.this, R.anim.myanim);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// 摇动手机后，震动
						m_Vibrator.vibrate(1000);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// 进入定位界面
						Intent mIntent = new Intent(SensorActivity.this,
								LocationActivity.class);
						startActivity(mIntent);
					}
				});
				mIv_handshake.startAnimation(anim);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// 当传感器精度改变时回调该方法
	}
}
