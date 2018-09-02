package com.sevele.ds.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sevele.ds.activity.ChatActivity;
import com.sevele.ds.adapter.ChatAdapter.ViewHolderImg_L;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.common.DbOperation;
import com.sevele.ds.table.MsgBeanTable;
import com.sevele.ds.utils.ImageDownLoader.onImageLoaderListener;

public class onImageLoaderListenerImp implements onImageLoaderListener {
	private ImageView iv;

	public onImageLoaderListenerImp(ImageView iv) {
		this.iv = iv;
	}
	
	
	@Override
	public void onChatImageLoader(Bitmap bitmap, String url, final MsgBeanTable chat) {
	
		if (iv.getTag().equals(url)) {
			//如果是图片就显示
			if(bitmap!=null){
				iv.setImageBitmap(bitmap);
			}
			RelativeLayout rl = (RelativeLayout) iv.getParent().getParent().getParent();
			ViewHolderImg_L vhl = (ViewHolderImg_L) rl.getTag();
			vhl.pb_progress.setVisibility(View.GONE);
             //下载失败了
			if (chat.getLoadState() == DsConstant.PUSH_LOAD_FAIL) {
				vhl.iv_fail.setVisibility(View.VISIBLE);
			}
		}
		new Thread(){
			public void run() {
				//将聊天存入数据库
				DsApplication.db.saveChatRecord(chat);
			}
		}.start();
	}
	@Override
	public void onImageLoader(Bitmap bitmap, String url) {
		
		
	}
}
