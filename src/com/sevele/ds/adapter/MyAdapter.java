package com.sevele.ds.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author:liuge
 * @createTime:2015年4月21日
 * @descrption:适配器基类
 */
public class MyAdapter extends BaseAdapter {
	private List<?> lists;

	public MyAdapter(List<?> lists) {
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}

}
