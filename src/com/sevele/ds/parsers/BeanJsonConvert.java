package com.sevele.ds.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.StrangerBean;
import com.sevele.ds.table.FriendTable;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.table.PushMessageTable;
import com.sevele.ds.table.StrangerTable;

/**
 * @author:liu ge
 * @createTime:2015年3月31日
 * @descrption:将json数据转化为javabean，将javabean转化为json数据类
 */
public class BeanJsonConvert {

	/**
	 * 将list<Object>类型转化为json数据，还原为list<object>类型
	 * 
	 * @param jsonString
	 *            要被解析的json数据
	 * @param cls
	 *            javabean.calss
	 * @return
	 */
	public static <T> List<T> jsonToBeanList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;

	}

	public static List<MsgBeanTable> jsonToBeanListM(String jsonString) {
		List<MsgBeanTable> list = new ArrayList<MsgBeanTable>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<List<MsgBeanTable>>() {
					}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;

	}

	/**
	 * 将javabean转化为json数据
	 * 
	 * @param bean
	 *            需要被转化的javabean
	 * @return
	 */
	public static String beanToJosn(Object bean) {
		Gson gson = new Gson();
		return gson.toJson(bean);
	}

	/**
	 * 将List<javabean>转化为json数据
	 * 
	 * @param list
	 *            被转化的list
	 * @return
	 */

	public static String beanListToJson(List<MsgBeanTable> list) {
		Gson gson = new Gson();
		return gson.toJson(list);

	}

	/**
	 * 将json数据转化为javabean
	 * 
	 * @param json
	 *            要被转化的json数据
	 * @param clas
	 *            将要被转化为javabean的 Class对象
	 * @return
	 */
	public static <T> T jsonToBean(String json, Class<T> clas) {
		Gson gson = new Gson();
		return gson.fromJson(json, clas);
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<StrangerTable> jsonToBeanList(String jsonString) {
		List<StrangerTable> list = new ArrayList<StrangerTable>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<ArrayList<StrangerTable>>() {
					}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<FriendTable> jsonToBeanListf(String jsonString) {
		List<FriendTable> list = new ArrayList<FriendTable>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<ArrayList<FriendTable>>() {
					}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 */

	public static List<MsgBeanTable> jsonToBeanListMsg(String jsonString) {
		List<MsgBeanTable> list = new ArrayList<MsgBeanTable>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<ArrayList<MsgBeanTable>>() {
					}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	/**
	 * 
	 * @param registJson
	 *            服务器返回的json数据 主要是判断状态码
	 * @return
	 */
	public static int userRegist(String registJson) {
		try {
			JSONObject uObject = new JSONObject(registJson);
			if (uObject.has("result")) {
				return Integer.valueOf(uObject.getString("result"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @param loginJson
	 *            服务器返回的Json数据，主要是状态码 JESSIONID
	 * @throws JSONException
	 */
	public static void setJessionId(String resultJson) {
		JSONObject uObject;
		try {
			uObject = new JSONObject(resultJson);
			if (uObject.has(DsConstant.USER_JESSIONID)) {
				DsApplication.user.setJessionId(uObject
						.getString(DsConstant.USER_JESSIONID));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param resultJson
	 * @return
	 */
	public static int getUserId(String resultJson) {
		JSONObject uObject;
		try {
			uObject = new JSONObject(resultJson);
			if (uObject.has(DsConstant.USER_ID)) {
				return uObject.getInt(DsConstant.USER_ID);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @param json
	 * @return
	 */

	public static String getJsonEntity(String json) {
		JSONObject jb;
		try {
			jb = new JSONObject(json);
			if (jb.has("attrs")) {
				return jb.getString("attrs");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
}
