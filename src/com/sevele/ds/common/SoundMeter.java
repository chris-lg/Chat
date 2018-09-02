package com.sevele.ds.common;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.sevele.ds.app.DsConstant;
import com.sevele.ds.utils.LogUtil;

/**
 * @author:liu ge
 * @createTime:2015年3月20日
 * @descrption:录音工具类
 */
public class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;

	private double mEMA = 0.0;

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		// 创建语音信息文件夹
		String Sound_file = DsConstant.AUDIO_ROOT;
		// 创建语音文件
		String files = Sound_file + name;
		File file = new File(files);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException ex) {
			LogUtil.LogTest("创建语音信息文件失败");
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mRecorder.setOutputFile(file.getAbsolutePath());
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.setOnErrorListener(null);
			mRecorder.setPreviewDisplay(null);
//			mRecorder.stop();
	        try {
	        	mRecorder.stop();
            } catch (IllegalStateException e) {
                Log.e("Yixia", "stopRecord", e);
            } catch (RuntimeException e) {
                Log.e("Yixia", "stopRecord", e);
            } catch (Exception e) {
                Log.e("Yixia", "stopRecord", e);
            }
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}

}
