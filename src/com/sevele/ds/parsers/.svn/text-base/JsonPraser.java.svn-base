package com.sevele.ds.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.Result;

/**
 * @author:liu ge
 * @createTime:2015年3月24日
 * @descrption:解析来自服务器的数据
 */
public class JsonPraser {
	
	/**
	 * @param loginJson
	 * @return 解析登录时候返回的数据，并且将用户在服务器的id和jsessionId保存在全局的userBean对象中
	 */
	public static boolean loginPraser(String loginJson) {
		if (loginJson == null)
			return false;
		Result rt = new Result();
		try {
			JSONObject jObject = new JSONObject(loginJson);
			rt.getResultFlag(loginJson);
			if (rt.isSuccess()) {// 获得数据成功
				if (jObject.has("JESSIONID")) {
					DsApplication.user.setJessionId(jObject
							.getString("JESSIONID"));
				} else if (jObject.has(DsConstant.USER_ID)) {
					DsApplication.user.setId(jObject.getInt(DsConstant.USER_ID));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

}
