package com.sevele.ds.common;

import com.sevele.ds.bean.Result;
/**
 * 
 *@author:liu ge
 *@createTime:2015年5月22日
 *@descrption:取出handler 中的msg 给result 赋值
 */
public class HandleResult {
	public static Result getResult(android.os.Message msg) {
		String resultJson = (String) msg.obj;
		if (resultJson != null) {
			Result re = new Result();
			re.getResultFlag(resultJson);
			return re;
		}
		return null;
	}
}
