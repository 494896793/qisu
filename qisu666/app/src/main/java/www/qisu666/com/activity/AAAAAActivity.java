package www.qisu666.com.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.AAAAMarkerView;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.stationMap.JsonUtil;
import www.qisu666.sdk.amap.stationMap.StationLocation;
import www.qisu666.sdk.amap.stationMap.juhe.MarkerImageView;
import www.qisu666.sdk.amap.stationMap.juhe.ScreenUtils;

/**
 * 717219917@qq.com 2018/10/18 15:48.
 */
public class AAAAAActivity extends BaseActivity implements LocationSource, AMapLocationListener,AMap.OnCameraChangeListener,AMap.OnMarkerClickListener {

    private OnLocationChangedListener mListener;
    private MapView mMapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private double lat;
    private double lon;
    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaaaa_layout);
        mMapView=findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);
        if(aMap==null){
            aMap=mMapView.getMap();
        }
        initMyLocation();
        d109();
    }

    private void  initMyLocation(){
        myLocationStyle=new MyLocationStyle();
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    private List<StationLocation> stationLocations=new ArrayList<>();
    private List<MarkerOptions> markerOptionsss=new ArrayList<>();
    private List<MarkerOptions> markerOptionsListInView=new ArrayList<>();

    /**
     * 测试 d109 获取附近30km桩子的重载
     */
    public void d109() {

        String url = "api/pile/station/map/query";
        final HashMap<String, Object> map = new HashMap<>();
//        map.put("latitude", lat + "");
//        map.put("longitude", lon + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(Object bean) {
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        Log.e("aaaa", ":s" + s);
                        // json转 list
                        List<String> stringlist = JsonUtils.jsonToList(s);
                        try {
                            JSONArray array = new JSONArray(stringlist.toString());
                            int count = array.length();
                            LogUtil.e("aaa" + "返回结果-----数据大小----" + count);
                            if (count == 0) { //没有数据
                                LogUtil.e("aaaa" + "返回结果-----没有数据");
                            } else {
                                LogUtil.e("aaaa" + "返回结果-----有数据----");
                                for (int i = 0; i < count; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    StationLocation stationLocation = JsonUtil.parse(object.toString(), StationLocation.class);
                                    LogUtil.e("aaaa" + i + "-每个对象station id: " + stationLocation.getStationId());
                                    try {
                                        IDianNiuApp.getInstance().db.saveOrUpdate(stationLocation);
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }//保存数据库中
                                    LogUtil.e("aaaaa" + "0每个对象: " + object.toString());
                                    String oString = object.toString();
                                    LogUtil.e("aaaaa" + "每个对象: " + oString);
//                            list.add(JsonUtils.jsonToMap(oString));//添加桩子

                                    stationLocations.add(stationLocation);

                                    LatLng latLng=new LatLng(Double.valueOf(stationLocation.getLatitude()),Double.valueOf(stationLocation.getLongitude()));
                                    MarkerOptions markerOptions=new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title("1");
                                    markerOptions.draggable(false);
                                    markerOptions.visible(true);
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(getResources(),R.mipmap.yc_4)));
                                    markerOptionsss.add(markerOptions);

//                                    Marker marker=aMap.addMarker(markerOptions);
//                                    marker.setTitle("1");
                                }
                                reMaker();
                            }
                        } catch (Exception t) {
                            t.printStackTrace();
                        }

                        LogUtil.e("aaaaa" + "0每个对象: " );
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });
    }

    private void reMaker(){
        aMap.clear();
        markerOptionsListInView.clear();
        Projection projection=aMap.getProjection();
        for(int i=0;i<markerOptionsss.size();i++){
            Point point=projection.toScreenLocation(markerOptionsss.get(i).getPosition());
            if (point.x < 0 || point.y < 0 || point.x > screenWidth || point.y > screenHeight) {// 屏幕外

            } else {
                markerOptionsListInView.add(markerOptionsss.get(i));//加入计算
            }
        }

        Log.i("======","====位置移动，大小缩放--markerOptionsListInView大小为:"+markerOptionsListInView.size());

        List<AAAAMarkerView> aaaaMarkerViews=new ArrayList<>();
        for(int i=0;i<markerOptionsListInView.size();i++){
            if(aaaaMarkerViews.size()==0){
                aaaaMarkerViews.add(new AAAAMarkerView(AAAAAActivity.this,markerOptionsListInView.get(i),projection,120));
            }else{
                boolean isIn = false;
                for(int j=0;j<aaaaMarkerViews.size();j++){
                    if(aaaaMarkerViews.get(j).getBounds().contains(markerOptionsListInView.get(i).getPosition())){
                        aaaaMarkerViews.get(j).addMarker(markerOptionsListInView.get(i));
                        isIn=true;
                        break;
                    }
                }

                if(!isIn){
                    aaaaMarkerViews.add(new AAAAMarkerView(AAAAAActivity.this,markerOptionsListInView.get(i),projection,120));
                }
            }
        }

        for(AAAAMarkerView aaaaMarkerView:aaaaMarkerViews){
            aaaaMarkerView.setPositionAndIcon();
        }


        aMap.clear();
        // 重新添加 marker
        for (AAAAMarkerView cluster : aaaaMarkerViews) {
            MarkerOptions a=cluster.getOptions();
            Log.i("===","====lat:"+a.getPosition().latitude+"----lon:"+a.getPosition().longitude);
            Marker marker = aMap.addMarker(cluster.getOptions());
            marker.setTitle(cluster.getOptions().getTitle());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色

            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    int statu=1;

    @Override
    public void onLocationChanged(AMapLocation amapLocation ) {
        if (mListener != null&&amapLocation != null) {
            if (amapLocation != null
                    &&amapLocation.getErrorCode() == 0) {
                if(statu==1){
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                    lat=amapLocation.getLatitude();
                    lon=amapLocation.getLongitude();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 18));
                    statu++;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        reMaker();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ToastUtil.showToast("Touch");
        return true;
    }
}
