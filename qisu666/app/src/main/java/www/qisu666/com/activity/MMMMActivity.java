package www.qisu666.com.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.model.UserBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.security.MD5;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.stationMap.juhe.MarkerImageView;
import www.qisu666.sdk.amap.stationMap.juhe.PointAggregationAty;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

/**
 * 717219917@qq.com 2018/8/7 13:45.
 */
public class MMMMActivity extends BaseActivity implements AMapLocationListener,LocationSource {

    MapView mMapView;
    AMap aMap;
    AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;//当前位置监听
    LatLonPoint afterGeoPoint;
    double curLat;
    double curLon;
    Marker locationMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmmm);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }


        /*MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色   这两句取消原形边框
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);*/

        try {//先获取到当前位置
            mlocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);//设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置为高精度定位模式
            mlocationClient.setLocationOption(mLocationOption);//设置定位参数
            mLocationOption.setOnceLocation(true);
            mlocationClient.startLocation();
            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
                    x.task().autoPost(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mlocationClient.stopLocation();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    });
                }
            }, 1500);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        CameraPosition.Builder builder = CameraPosition.builder();
        builder.target(new LatLng(22.5472000000, 114.0842620000));//先移动地图到广州   反地理编码失效
        builder.zoom(16.0f);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

        LatLng latLng = new LatLng(mlocationClient.getLastKnownLocation().getLatitude(),mlocationClient.getLastKnownLocation().getLongitude());
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));

    }

    public void show(View view){
        String url = "api/user/login";
        HashMap<String, Object> map = new HashMap<>();
        final String phoneNo = "13128871393";
        final String pwd = MD5.getMD5Code("123456");

        map.put("mobileNo", phoneNo);
        map.put("loginPWD", pwd);
        map.put("deviceUUID", PhoneParams.getDeviceUUID(this));
        LogUtil.e("请求的参数" + map.toString());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(UserBean.class))
                .compose(RxNetHelper.<UserBean>io_main())
                .subscribe(new ResultSubscriber<UserBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(UserBean bean) {
                        Log.e("aaaa", "getToken：" + bean.getToken());
                    }

                    @Override
                    public void onFail(Message<UserBean> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation2) {
        try {

        } catch (Throwable t) {
            t.printStackTrace();
        }
        LogUtil.e("位置改变22:" + aMapLocation2.getErrorCode());
        if (aMapLocation2.getErrorCode() == 0) {
            try {
                mListener.onLocationChanged(aMapLocation2);
            } catch (Throwable t) {
                t.printStackTrace();
            }// 显示系统小蓝点
            try {
                curLat = aMapLocation2.getLatitude();
                curLon = aMapLocation2.getLongitude();
                if (afterGeoPoint == null) {
                    afterGeoPoint = new LatLonPoint(curLat, curLon);
                } else {
                    afterGeoPoint.setLatitude(curLat);
                    afterGeoPoint.setLongitude(curLon);
                }//每次new
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //这里不变 否则一直显示深圳
            try {
                /*aMapLocation2.getCity().replace("市", "");*/
            } catch (Throwable t) {
                t.printStackTrace();
            }
//				runOnUiThread(new Runnable() {
//					@Override public void run() {
            try {
                locationMarker = null;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            /*resetMarks();*/
            //可在其中解析amapLocation获取相应内容。
            try {
                aMap.getMapScreenMarkers().remove(locationMarker);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {//尝试添加自己位置的图标
                        LatLng latLng = new LatLng(curLat, curLon);//取出经纬度
                        //添加Marker显示定位位置
                        if (locationMarker == null) {
                            //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.visible(true);
                            markerOptions.title("当前位置");
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nearby_charge));
                            markerOptions.icon(bitmapDescriptor);
                            /*aMap.addMarker(markerOptions);*/
                            /*locationMarker = aMap.addMarker(markerOptions);*/
                        } else { //已经添加过了，修改位置即可
                            locationMarker.setPosition(latLng);
                        }
                        CameraPosition.Builder builder = CameraPosition.builder();
                        builder.target(latLng);//移动地图到定位地点
                        builder.zoom(16.0f);
                        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));


                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }, 2000);

        } else {
            String errText = "定位失败," + aMapLocation2.getErrorCode() + ": " + aMapLocation2.getErrorInfo();
            LogUtils.e(errText);
            if (NetworkUtils.isConnected(this)) {
                if (aMapLocation2.getErrorCode() == 12) {
                    ToastUtil.showToast(R.string.toast_permission_location);
                } else {
                    ToastUtil.showToast(getString(R.string.toast_map_location_failed) + aMapLocation2.getErrorInfo());
                }
            }
        }
    }

    /**
     * 获取视野内的marker 根据聚合算法合成自定义的marker 显示视野内的marker
     */
    /*private void resetMarks() {
        Projection projection = aMap.getProjection();         // 开始刷新
        Point p = null;
        markerOptionsListInView.clear();
        for (MarkerOptions mp : markerOptionsListall) {       // 获取在当前视野内的marker;提高效率
            p = projection.toScreenLocation(mp.getPosition());//计算屏幕中点坐标
            if (p.x < 0 || p.y < 0 || p.x > screenWidth || p.y > screenHeight) {// 屏幕外

            } else {
                markerOptionsListInView.add(mp);//加入计算
            }
        }
        //自定义的聚合类MyMarkerCluster
        ArrayList<MarkerImageView> clustersMarker = new ArrayList<MarkerImageView>();
        for (MarkerOptions mp : markerOptionsListInView) {
            if (clustersMarker.size() == 0) {             // 添加一个新的自定义marker
                clustersMarker.add(new MarkerImageView(PointAggregationAty.this, mp, projection, 120, 1));// 80=相距多少才聚合
            } else {
                boolean isIn = false;
                for (MarkerImageView cluster : clustersMarker) {
                    if (cluster.getBounds().contains(mp.getPosition())) {// 判断当前的marker是否在前面marker的聚合范围内 并且每个marker只会聚合一次。
                        cluster.addMarker(mp);
                        isIn = true;
                        break;
                    }
                }
                // 如果没在任何范围内，自己单独形成一个自定义marker。在和后面的marker进行比较
                if (!isIn) {
                    clustersMarker.add(new MarkerImageView(PointAggregationAty.this, mp, projection, 120, clustersMarker.size()));// 80=相距多少才聚合
                }
            }
        }
        // 设置聚合点的位置和icon
        for (MarkerImageView mmc : clustersMarker) {
            mmc.setpositionAndIcon();
        }
        aMap.clear();
        // 重新添加 marker
        for (MarkerImageView cluster : clustersMarker) {
            Marker marker = aMap.addMarker(cluster.getOptions());
            marker.setTitle(cluster.getOptions().getTitle());
        }

        EventBus.getDefault().post("我的位置");
    }*/

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }
}
