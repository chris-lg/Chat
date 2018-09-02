package com.sevele.ds.common;

import android.os.Environment;

/**
 * @author:liu ge
 * @createTime:2015年3月20日
 * @descrption:检测SD卡
 */
public class SdcardMountedCheck {
	public static boolean isSdcardMounted() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
}
