package com.sevele.ds.activity;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.decryptstranger.R;
import com.sevele.ds.app.DsApplication;
import com.sevele.ds.app.DsConstant;
import com.sevele.ds.bean.PositionBean;
import com.sevele.ds.http.HttpAPI;
import com.sevele.ds.http.HttpUrl;
import com.sevele.ds.parsers.BeanJsonConvert;
import com.sevele.ds.table.StrangerTable;
import com.sevele.ds.utils.LogUtil;
import com.sevele.ds.utils.UIHelper;

/**
 * @author： Maozhiqi
 * @time ：2015年3月31日
 * @descrption： 用户定位界面
 */
public class LocationActivity extends BaseActivity implements LocationSource,
		AMapLocationListener, OnMarkerClickListener, OnInfoWindowClickListener,
		OnMapLoadedListener, OnMapClickListener {
	private RelativeLayout mRl_Back;// 返回
	private TextView mTv_Location_Title;// 界面标题
	private AMap aMap;// 地图属性设置对象
	private MapView m_MapView;// 地图View
	private OnLocationChangedListener m_Listener;// 定位事件监听者
	private LocationManagerProxy m_AMapLocationManager;// 定位信息管理器
	private UiSettings m_UiSettings;// 地图组件设置对象
	private Double dbl_geoLat;// 纬度
	private Double dbl_geoLng;// 经度
	private long lng_sendTime;// 定位时间
	private AMapLocation m_AMapLocation;// 用于判断定位超时
	private Handler m_handler;
	private List<StrangerTable> mLit_strangers;// 陌生人数据列表
	private PositionBean m_Position = new PositionBean();// 我的位置实体
	private Thread m_SendPosition;// 发送位置线程
	private HashMap<String, Object> mHm_PositionMap = new HashMap<String, Object>();
	private Marker m_currentMarker = null; // 用于保存被点击的Marker

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_MapView.onCreate(savedInstanceState);// 此方法必须重写
		init();// 初始化AMap对象
		m_handler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DsConstant.HANDLER_NET_OK:
					LogUtil.LogTest("成功接收陌生人数据");
					try {
						JSONObject ob = new JSONObject((String) msg.obj);
						String jsonString = (String) ob.get("result");
						if (jsonString != null) {
							mLit_strangers = BeanJsonConvert
									.jsonToBeanList(jsonString);
							LogUtil.LogTest("陌生人列表" + mLit_strangers);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 判断是否有陌生人，若有则添加陌生人在地图上
					if (mLit_strangers == null) {
						UIHelper.ToastMessage("没有和您同时摇的陌生人，稍后再试试吧");
					} else {
						addMarkersToMap(mLit_strangers);
					}
					break;
				case DsConstant.HANDLER_NET_LINK_FAIL:
					UIHelper.ToastMessage("小陌也不知道怎么了，再试试吧");
					break;
				case DsConstant.HANDLER_NET_FAIL:
					UIHelper.ToastMessage("请检查网络");
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_location);
		m_MapView = (MapView) findViewById(R.id.map);
		mTv_Location_Title = (TextView) findViewById(R.id.txtTitle_banner);
		mTv_Location_Title.setText(R.string.txtLocation_title);
		mRl_Back = (RelativeLayout) findViewById(R.id.back_layout);

		mRl_Back.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			// 返回
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = m_MapView.getMap();
			m_UiSettings = aMap.getUiSettings();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.myposition));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.alpha(0));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.alpha(0));// 设置圆形的填充颜色
		myLocationStyle.strokeWidth(0.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		m_UiSettings.setZoomControlsEnabled(true);// 设置默认缩放按钮是否显示
		m_UiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置高德logo显示在右下方
		m_UiSettings.setCompassEnabled(false);// 设置指南针是否显示
		m_UiSettings.setScrollGesturesEnabled(true);// 设置是否可以手势滑动
		m_UiSettings.setZoomGesturesEnabled(true);// 设置是否可以手势缩放
		m_UiSettings.setScaleControlsEnabled(true);// 显示地图的默认比例尺
		aMap.moveCamera(CameraUpdateFactory.zoomTo(17));// 设置默认缩放级别
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setOnMapClickListener(this);// 点击地图事件监听器
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (m_Listener != null && location != null) {
			m_Listener.onLocationChanged(location);// 显示系统小蓝点
			this.m_AMapLocation = location;// 判断超时机制
			dbl_geoLat = location.getLatitude();
			dbl_geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			lng_sendTime = System.currentTimeMillis();
			m_Position.setLongItude(dbl_geoLng);
			m_Position.setLatitude(dbl_geoLat);
			m_Position.setSendTime(lng_sendTime);
			mHm_PositionMap.put("userPositionJson",
					BeanJsonConvert.beanToJosn(m_Position));
			mHm_PositionMap.put("userId", DsApplication.user.getId());
			// 发送位置信息到服务器
			m_SendPosition = new Thread(Postlocationinfo);
			m_SendPosition.start();
		}
		// 定位一次之后，停止定位
		deactivate();
	}

	/**
	 * 激活定位
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void activate(OnLocationChangedListener listener) {
		m_Listener = listener;
		if (m_AMapLocationManager == null) {
			m_AMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			m_AMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 1, this);
		}
	}

	/**
	 * 停止定位
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void deactivate() {
		m_Listener = null;
		if (m_AMapLocationManager != null) {
			m_AMapLocationManager.removeUpdates(this);
			m_AMapLocationManager.destory();
		}
		m_AMapLocationManager = null;
	}

	/**
	 * 发送位置信息到服务器
	 */
	Runnable Postlocationinfo = new Runnable() {

		@Override
		public void run() {
			HttpAPI mPostlocationinfo = new HttpAPI(m_handler);
			mPostlocationinfo
					.httpSendTxt(mHm_PositionMap, DsApplication.user,
							HttpUrl.POSITION_URL, DsConstant.HANDLER_NET_OK,
							null, null);
		}
	};

	/**
	 * 添加marker在地图上
	 */
	private void addMarkersToMap(List<StrangerTable> strangers) {
		for (int i = 0; i < strangers.size(); i++) {
			LatLng position = new LatLng(
					strangers.get(i).getStrangerLatItude(), strangers.get(i)
							.getStrangerLongItude());
			// 在地图上标记位置的经纬度值,参数不能为空
			aMap.addMarker(
					new MarkerOptions().position(position)
					// 当用户点击标记，在信息窗口上显示的字符串
							.title("昵称：" + strangers.get(i).getUserName())
							// 附加文本，显示在标题下方
							.snippet("点击进入解密游戏")
							// 图标
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.position)))
					.setObject(strangers.get(i).getSid());

			// 判断要到的人是否为好友，如不是，则加入陌生人列表
			if (DsApplication.db.getFriendTable(strangers.get(i).getSid()) == null) {
				DsApplication.db.writeStrangerToDb(strangers.get(i));
			}
		}
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {

	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {

		LogUtil.LogTest("陌生人ID" + String.valueOf(marker.getObject()));
		// 判断该陌生人是否已经为好友
		if (DsApplication.db.getFriendTable(Integer.parseInt(String
				.valueOf(marker.getObject()))) == null) {
			Intent intent = new Intent(LocationActivity.this,
					DecryptgameActivity.class);
			intent.putExtra("addFriendId", String.valueOf(marker.getObject()));
			startActivity(intent);
		} else {
			UIHelper.ToastMessage("该用户已经是您的好友啦,换个试试~");
		}
	}

	/**
	 * 对Map点击响应事件
	 */
	@Override
	public void onMapClick(LatLng arg0) {
		LogUtil.LogTest("**********点击地图");
		if (m_currentMarker != null) {
			LogUtil.LogTest("**********窗口消失");
			m_currentMarker.hideInfoWindow();
		}
		m_currentMarker = null;
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		m_currentMarker = marker;
		return false;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		m_MapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		m_MapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		m_MapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_MapView.onDestroy();
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
