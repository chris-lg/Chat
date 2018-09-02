package com.sevele.ds.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.decryptstranger.R;
import com.qiniu.common.GetToken;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.Result;
import com.sevele.ds.bean.UserRegistInfo;
import com.sevele.ds.common.HandleResult;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;
import com.sevele.ds.utils.isIntegerUtil;

/**
 * @author:liu ge
 * @createTime:2015年3月21日
 * @descrption:注册界面
 */
public class RegisterActivity extends BaseActivity {
	private Button mBtn_Regist; // 确认注册按钮
	private ImageView mIv_UserPhonto; // 用户头像
	private static EditText mEt_UserCount; // 帐号输入框
	private EditText mEt_UsertPwd; // 密码输入框
	private EditText mEt_UserName; // 昵称输入框
	private EditText mEt_UserAge; // 年龄输入框
	private RadioButton mRb_Gril; // 性别 女
	private RadioButton mRb_Man; // 性别男
	private RelativeLayout mRl_back;// 返回按钮

	private static final int NONE = 0;
	private static final int PHOTO_GRAPH = 1;// 拍照
	private static final int PHOTO_ZOOM = 2; // 缩放
	private static final int PHOTO_RESOULT = 3;// 结果

	private static final String IMAGE_UNSPECIFIED = "image/*"; //照相输出格式

	private static final String FILE_TYPE = ".jpg"; //文件扩展名

	private static File mPicture;// 保存自己拍照后头像文件
	private Bitmap mBitmap;     // 选择图库生产的一个位图

	private Handler mHandler;
	private String filePath = DsConstant.IMG_ROOT
			+ UUID.randomUUID().toString() + FILE_TYPE; //照相后保存路径
	
	private Uri mUritempFile = Uri.parse("file://" + "/"
			+ Environment.getExternalStorageDirectory().getPath() + "/"
			+ "small.jpg");  //最后相片显示资源路径
	private static HttpAPI mHttp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler=new registHandler();
		mHttp = new HttpAPI(mHandler);
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_register);

		mRl_back = (RelativeLayout) findViewById(R.id.layout_back);
		mBtn_Regist = (Button) findViewById(R.id.regist_sure);
		mIv_UserPhonto = (ImageView) findViewById(R.id.user_phonto);
		mEt_UserName = (EditText) findViewById(R.id.regist_inputname);
		mEt_UsertPwd = (EditText) findViewById(R.id.regist_inputpwd);
		mEt_UserAge = (EditText) findViewById(R.id.yearsold_input);
		mEt_UserCount = (EditText) findViewById(R.id.regist_inputcount);
		mRb_Gril = (RadioButton) findViewById(R.id.gril);
		mRb_Man = (RadioButton) findViewById(R.id.man);

		mIv_UserPhonto.setOnClickListener(this);
		mBtn_Regist.setOnClickListener(this);
		mRl_back.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == NONE)
			return;
		if (requestCode == PHOTO_GRAPH) {
			// 设置文件保存路径 ,照相返回
			mPicture = new File(filePath);
			if (!mPicture.exists()) {
				try {
					mPicture.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//进行缩放
			startPhotoZoom(Uri.fromFile(mPicture));
		}
		if (data == null)
			return;
		// 读取相册缩放图片
		if (requestCode == PHOTO_ZOOM) {
			Uri mUri = data.getData();
			mPicture = uriToFile(mUri);
			startPhotoZoom(mUri);
		}
		// 处理结果
		if (requestCode == PHOTO_RESOULT) {
			try {
				mBitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(mUritempFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			mIv_UserPhonto.setImageBitmap(mBitmap); // 把图片显示在ImageView控件上
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	// 点击事件
	public void widgetClick(View a_click_view) {
		switch (a_click_view.getId()) {
		case R.id.user_phonto: {// 点击头像可以选择获取头像方式
			LayoutInflater inflater = LayoutInflater
					.from(RegisterActivity.this);
			
			
			//弹出的popwindow以供获取头像方式选择
			ViewGroup popview = (ViewGroup) inflater.inflate(
					R.layout.choose_phoneways, null, true);
			final PopupWindow choose = new PopupWindow(popview,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			choose.setBackgroundDrawable(new BitmapDrawable());
			choose.setFocusable(true);
			choose.showAsDropDown(mIv_UserPhonto);

			TextView takePhotoes = (TextView) popview
					.findViewById(R.id.choose_camera);
			takePhotoes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 拍照获得照片
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(filePath)));
					startActivityForResult(intent, PHOTO_GRAPH);
					choose.dismiss();
				}
			});
			TextView lTv_picPhotoes = (TextView) popview
					.findViewById(R.id.choose_pic);
			lTv_picPhotoes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 从图库中获取照片
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							IMAGE_UNSPECIFIED);
					startActivityForResult(intent, PHOTO_ZOOM);
					choose.dismiss();
				}
			});
			TextView picCancle = (TextView) popview.findViewById(R.id.cancle);
			picCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					choose.dismiss();
				}
			});
		}
			break;
		// 点击注册
		case R.id.regist_sure:
			if (infoCompelet()) {
				UserRegistInfo ur = new UserRegistInfo();
				ur.setUserName(mEt_UserName.getText().toString());
				ur.setUserPwd(mEt_UsertPwd.getText().toString());
				ur.setUserCount(mEt_UserCount.getText().toString());
				if (mPicture != null) {
					// 如果上传了头像
					ur.setUserHeadpic(mEt_UserCount.getText().toString()
							+ "head.jpg");
				}
				if (mRb_Gril.isChecked()) {
					ur.setUserGender("女");
				} else {
					ur.setUserGender("男");
				}
				ur.setUserAge(Integer.parseInt(mEt_UserAge.getText().toString()));
				final String registJson = BeanJsonConvert.beanToJosn(ur);
				UIHelper.loading();
				new Thread() {
					public void run() {
						mHttp.httpRegister(registJson, HttpUrl.REGIST_URL);
					};
				}.start();
			}
			break;
		// 返回事件
		case R.id.layout_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 收缩图片
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent l_intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
		l_intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		l_intent.putExtra("crop", "true");// 进行修剪
		// aspectX aspectY 是宽高的比例
		l_intent.putExtra("aspectX", 1);
		l_intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		l_intent.putExtra("outputX", 300);
		l_intent.putExtra("outputY", 300);
		// intent.putExtra("return-data", true);
		l_intent.putExtra(MediaStore.EXTRA_OUTPUT, mUritempFile);
		l_intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(l_intent, PHOTO_RESOULT);
	}

	/**
	 * 判断用户注册的信息是否填写完全
	 * 
	 * @return
	 */
	public boolean infoCompelet() {
		if ("".equals(mEt_UserCount.getText().toString())) {
			UIHelper.ToastMessage("输入帐号");
			return false;
		} else if ("".equals(mEt_UserName.getText().toString())) {
			UIHelper.ToastMessage("昵称不能为空");
			return false;
		} else if ("".equals(mEt_UsertPwd.getText().toString())) {
			UIHelper.ToastMessage("请设置密码");
			return false;
		} else if ((!mRb_Gril.isChecked()) && (!mRb_Man.isChecked())) {
			UIHelper.ToastMessage("请选择性别");
			return false;
		} else if ("".equals(mEt_UserAge.getText().toString())) {
			UIHelper.ToastMessage("请输入用户年龄");
			return false;
		} else if (!isIntegerUtil.isInteger(mEt_UserAge.getText().toString())) {
			UIHelper.ToastMessage("请输入合法的年龄");
			return false;
		} else if (Integer.parseInt(mEt_UserAge.getText().toString()) < 0) {
			UIHelper.ToastMessage("请输入合法的年龄");
			return false;
		}
		return true;

	}

	/**
	 * 获取uri所标识的文件的路径
	 * 
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public File uriToFile(Uri uri) {

		String[] l_proj = { MediaStore.Images.Media.DATA };

		Cursor l_actualimagecursor = managedQuery(uri, l_proj, null, null, null);

		int l_actual_image_column_index = l_actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		l_actualimagecursor.moveToFirst();

		String l_img_path = l_actualimagecursor
				.getString(l_actual_image_column_index);
		File l_file = new File(l_img_path);
		return l_file;
	}

	public static class registHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (UIHelper.isLoadingShow) {
				UIHelper.cancleLoading();
			}
			switch (msg.what) {
			case DsConstant.HANDLER_NET_REGIST_OK: // 从服务器获取返回信息
				Result re = HandleResult.getResult(msg);
				if (re.isSuccess()) {// 注册成功
					// 操作成功
					if (mPicture != null) {
						final String token = GetToken
								.getHeadpicToken(mEt_UserCount.getText()
										.toString() + "head.jpg");
						new Thread() {
							public void run() {
								mHttp.upLoadFileToQiNiu(
										token,
										mPicture,
										mEt_UserCount.getText().toString()
												+ "head.jpg",
										DsConstant.HANDLER_QINIU_NET_UPLOAD_PIC_OK);
							};
						}.start();
					}
				}
				// 注册失败
				UIHelper.ToastMessage(re.getMessage());
				break;
			case DsConstant.HANDLER_QINIU_NET_UPLOAD_PIC_OK:
				UIHelper.ToastMessage("头像上传成功");
				break;
			case DsConstant.HANDLER_QINIU_NET_UPLOAD_FAIL:
				UIHelper.ToastMessage("头像上传失败");
				break;
			case DsConstant.HANDLER_NET_LINK_FAIL:
				UIHelper.ToastMessage("请求失败");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 释放累静态对象
	 */
	private void releaseStaticObj() {
		if (mHttp != null) {
			mHttp = null;
		}
		if (mPicture == null) {
			mPicture = null;
		}
		if (mEt_UserCount != null) {
			mEt_UserCount = null;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseStaticObj();
	}
}
