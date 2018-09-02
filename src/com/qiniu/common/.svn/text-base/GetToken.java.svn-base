package com.qiniu.common;

/**
 * @author:liu ge
 * @createTime:2015年4月26日
 * @descrption:获取上传到七牛服务器的token
 */
public class GetToken {
	/**
	 * @param key
	 *            上传到七牛上的文件名,上传图片的时候该scope模式下允许文件覆盖
	 * @return Toaken
	 */
	public static String getHeadpicToken(String key) {
		return Config.auth.uploadToken(Config.BUNK_NAME, key);
	}

	/**
	 * 默认的上传策略
	 * 
	 * @return
	 */

	public static String getHeadpicToken() {
		return Config.auth.uploadToken(Config.BUNK_NAME);
	}
	
}
