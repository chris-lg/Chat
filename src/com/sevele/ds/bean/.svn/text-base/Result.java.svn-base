package com.sevele.ds.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author:liu ge
 * @createTime:2015年3月27日
 * @descrption:判断是否成功收到服务器返回数据
 */
public class Result {
	private String code;

	private String message;
	
	private String reslut;
	
	

	public String getReslut() {
		return reslut;
	}

	public void setReslut(String reslut) {
		this.reslut = reslut;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void getResultFlag(String jsonData) {
		if (jsonData == null) {
			return;
		}
		try {
			JSONObject jb = new JSONObject(jsonData);
			setCode(jb.getString("code"));
			setMessage(jb.getString("message"));
			if(jb.has("result")){
				setReslut(jb.getString("result"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean isSuccess() {
		if (code == null)
			return false;
		else if ("0000".equals(code)) {
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return "Result [code=" + code + ", message=" + message + "]";
	}

}
