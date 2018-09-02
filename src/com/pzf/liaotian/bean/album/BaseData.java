package com.pzf.liaotian.bean.album;

import java.io.Serializable;

import com.baidu.android.itemview.helper.BaseStyle;


/**
 * @author:liu ge
 * @createTime:2015年5月18日
 * @desc:网络数据类的基类
 */
public abstract class BaseData extends BaseStyle implements Serializable {

	protected static final int STATUS_OK = 0;
	protected static final int STATUS_ERROR = -1;
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private transient int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
