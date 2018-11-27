package www.qisu666.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;

import org.json.JSONObject;

import java.util.Map;

//充电桩定位
public class ShareChargingActivity extends BaseActivity implements View.OnClickListener, LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;
    private MarkerOptions markerOption;

    private LinearLayout layout_search;
    private TextView tv_address;
    private TextView tv_lat;
    private TextView tv_lng;
    private TextView btn_submit;

    private String city;
    private boolean finishLocation = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_share_charging);
        mapView = (MapView) findViewById(R.id.mapView); //获取地图控件引用
        mapView.onCreate(savedInstanceState); //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        initView();
        initAmap();
    }
    @Override  protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
    @Override protected void onResume() {
        super.onResume();
        mapView.onResume();//在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
    }
    @Override protected void onPause() {
        super.onPause();
        mapView.onPause();//在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        deactivate();
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState); //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
    }

    /** 初始化控件 */
    private void initView() {
        initTitleBar();
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_lng = (TextView) findViewById(R.id.tv_lng);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
    }

    /** 初始化标题栏 */
    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.share_charging_title));
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** 设置监听器 */
    private void setListeners() {
        layout_search.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_search:
                Intent intent = new Intent(this, PoiSearchActivity.class);
                intent.putExtra("city", city);
                startActivityForResult(intent, ConstantCode.REQ_POI_SEARCH);
                break;
            case R.id.btn_submit:
                DialogHelper.confirmDialog(ShareChargingActivity.this, getString(R.string.dialog_prompt_share_charging), new AlertDialog.OnDialogButtonClickListener() {
                    @Override public void onConfirm() {
                        submitToServer();
                    }
                    @Override public void onCancel() {  }
                });
                break;
        }
    }

    /** 发送 B105 请求，分享充电桩*/
    private void submitToServer() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("req_code", "B105");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("longitude", tv_lng.getText().toString());
            jsonObject.put("latitude", tv_lat.getText().toString());
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
            jsonObject.put("charge_pile_bel", getIntent().getStringExtra("charge_pile_bel"));
            jsonObject.put("charge_address", tv_address.getText().toString());
        }catch (Exception e){
              e.printStackTrace();
        }
        new HttpLogic(ShareChargingActivity.this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                ToastUtil.showToast(R.string.toast_B105);
                finish();
            }
        });
    }

    /** 初始化AMap对象*/
    private void initAmap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /** 设置一些amap的属性 */
    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle(); // 自定义系统定位小蓝点
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_nearby_charge));// 设置小蓝点的图标
        myLocationStyle.strokeColor(android.R.color.transparent);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100,55,115,203));// 设置圆形的填充颜色
        myLocationStyle.anchor(0.5f, 0.5f);//设置小蓝点的锚点

//        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType();
        aMap.setOnCameraChangeListener(this); //设置对移动地图事件的监听
    }

    /** 定位成功后回调函数 */
    @Override public void onLocationChanged(AMapLocation amapLocation) {
//        markerOption = new MarkerOptions();
//        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_share_charging_marker)).draggable(false);
//        Marker marker = aMap.addMarker(markerOption);
//        marker.setPositionByPixels(DensityUtil.getScreenWidth(this)/2, (int)(mapView.getY()+mapView.getHeight()/2));
//        marker.setZIndex(20);
//        LogUtils.d(DensityUtil.getScreenWidth(this)/2+","+(int)(mapView.getY()+mapView.getHeight()/2));

        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点.
//                LogUtils.d("定位啦啦啦啦啦！！！");
                setListeners();
                city = amapLocation.getCity();
                tv_address.setText(amapLocation.getAddress());
                tv_lat.setText(String.valueOf(amapLocation.getLatitude()));
                tv_lng.setText(String.valueOf(amapLocation.getLongitude()));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                if(NetworkUtils.isConnected(this)){
                    if(amapLocation.getErrorCode() == 12){
                        ToastUtil.showToast(R.string.toast_permission_location);
                    }else{
                        ToastUtil.showToast(getString(R.string.toast_map_location_failed) + amapLocation.getErrorInfo());
                    }
                }
            }
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        LogUtils.d("1:"+aMap.getCameraPosition().zoom);
        finishLocation = true;
    }

    /** 激活定位 */
    @Override public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this); //设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置为高精度定位模式
			mLocationOption.setOnceLocation(true); //设置是否单次定位
            mlocationClient.setLocationOption(mLocationOption);//设置定位参数
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /** 停止定位 */
    @Override public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override public void onCameraChange(CameraPosition cameraPosition) {
        btn_submit.setEnabled(false);
    }

    @Override public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if(finishLocation){
            btn_submit.setEnabled(true);
            double lat = cameraPosition.target.latitude;
            double lng = cameraPosition.target.longitude;
            getAddress(new LatLonPoint(lat, lng));
            tv_lat.setText(StringUtil.formatLatLng(lat));
            tv_lng.setText(StringUtil.formatLatLng(lng));
            LatLngBounds mapBounds = aMap.getProjection().getMapBounds(cameraPosition.target, cameraPosition.zoom);
//            LogUtils.d("屏幕范围："+mapBounds.northeast+","+mapBounds.southwest+",中心点："+cameraPosition.target+",zoom:"+cameraPosition.zoom);
            LogUtils.d("中心点："+cameraPosition.target);
        }
    }

    /** 响应逆地理编码 */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (rCode == 1000) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null&& regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                tv_address.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
                LogUtils.d("地址:"+regeocodeResult.getRegeocodeAddress().getFormatAddress());
            } else {
                ToastUtil.showToast(R.string.toast_map_regeocode_no_data);
            }
        } else {
            ToastUtil.showToast(R.string.toast_map_regeocode_no_data);
            LogUtils.d("获取地址失败，错误码："+String.valueOf(rCode));
        }
    }

    @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_POI_SEARCH && resultCode==ConstantCode.RES_POI_SEARCH){
            finishLocation = true;
            PoiItem poiItem = data.getParcelableExtra("data");
            LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
            aMap.moveCamera(cameraUpdate);
//            tv_lat.setText(String.valueOf(poiItem.getLatLonPoint().getLatitude()));
//            tv_lng.setText(String.valueOf(poiItem.getLatLonPoint().getLongitude()));
        }
    }
}
