package com.nautilus.ywlfair.module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.common.utils.StringUtils;

@SuppressLint("HandlerLeak")
public class BaiduMapActivity extends BaseActivity implements OnClickListener {
	private MapView mMapView = null;

	private Double x = .0, y = .0, x1 = .0, y1 = .0;

	private LatLng cenpt, mycenpt;

	private ImageView image_map_back;

	private BaiduMap mBaiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidu_map);

		image_map_back = (ImageView) findViewById(R.id.image_map_back);
		image_map_back.setOnClickListener(this);

		mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        String gps = getIntent().getStringExtra(Constant.KEY.ADDRESS);

		y1 = MyApplication.getInstance().getLatitude();
		x1 = MyApplication.getInstance().getLongitude();

		cenpt = StringUtils.gps(gps);

		mycenpt = new LatLng(y1, x1);

		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18)
				.build();

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);

        mBaiduMap.setMapStatus(mMapStatusUpdate);

		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.ywldw);

		OverlayOptions option = new MarkerOptions().position(cenpt)
				.icon(bitmap);

		mBaiduMap.addOverlay(option);

		BitmapDescriptor my = BitmapDescriptorFactory// Set user location icon
				.fromResource(R.drawable.user_gps);

		OverlayOptions option1 = new MarkerOptions().position(mycenpt).icon(my);

		mBaiduMap.addOverlay(option1);

		mBaiduMap
				.setMapStatus(MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder().zoom(mapLevel())
								.build()));
	}

	private int mapLevel() {// Automatically adjust the map display range
		int b = 19;

		return b;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_map_back:
			finish();
			break;
		}
	}

}
