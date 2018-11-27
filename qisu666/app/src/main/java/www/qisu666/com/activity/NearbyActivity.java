package www.qisu666.com.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.adapter.NearbyActivityAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MessageUtil;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.entity.NearbyEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.MapUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;

/**
 * 717219917@qq.com 2018/8/13 9:27.
 */
public class NearbyActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshListView pull_refresh_load_recycler_view;
    //点击时的经纬度
    private double clickLat;
    private double clickLng;
    //当前位置经纬度
    private String lng;
    private String lat;
    private AMapLocation aMapLocation;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private List<NearbyEntity> list;
    private PopupWindow mapPopupWindow;

    private NearbyActivityAdapter adapter;
    private TextView tv_title;
    private ImageView img_title_left;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_nearby_layout);
        initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else {
            gps();
        }
    }

    private void initView(){
        img_title_left=findViewById(R.id.img_title_left);
        tv_title=findViewById(R.id.tv_title);
        pull_refresh_load_recycler_view=findViewById(R.id.pull_refresh_load_recycler_view);
        initMapPopupWindow();

        tv_title.setText("附近车辆");
        pull_refresh_load_recycler_view.setMode(PullToRefreshBase.Mode.DISABLED);
        img_title_left.setOnClickListener(this);
    }

    private void gps(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext()) ;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null) {
                    if(aMapLocation.getErrorCode()==0) {
                        lng = aMapLocation.getLongitude() + "";
                        lat = aMapLocation.getLatitude() + "";
                        lat = (Double.valueOf(lat) - Config.OFF_LAT) + "";
                        lng = (Double.valueOf(lng) - Config.OFF_LNG) + "";
                        init();
                    }else{
                        ToastUtil.showToast(getString(R.string.toast_map_location_failed) + aMapLocation.getErrorInfo());
                    }
                }else{
                    ToastUtil.showToast(getString(R.string.toast_map_location_failed) + aMapLocation.getErrorInfo());
                }
            }
        });
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    /**
     * 选择地图PopupWindow
     */
    private void initMapPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_choice_map, null);

        TextView tv_start_navi = (TextView) contentView.findViewById(R.id.tv_start_navi);
        TextView tv_amap = (TextView) contentView.findViewById(R.id.tv_amap);
        TextView tv_baidu = (TextView) contentView.findViewById(R.id.tv_baidu);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_start_navi.setOnClickListener(this);
        tv_amap.setOnClickListener(this);
        tv_baidu.setOnClickListener(this);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPopupWindow.dismiss();
            }
        });

        mapPopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mapPopupWindow.setTouchable(true);
        mapPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mapPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.bg_white));
        mapPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0){
                gps();
            }else{
                ToastUtil.showToast("获取定位权限失败，定位失败");
            }
        }
    }

    private void init(){
        String url="api/car/nearest/query";
        HashMap<String,Object> map=new HashMap<>();
        map.put("distance","30");
        map.put("lng",lng);
        map.put("lat",lat);
        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        try {
                            String jsonString=JsonUtils.objectToJson(bean);
                            JSONArray jsonArray=new JSONObject(jsonString).optJSONArray("carList");
                            if(jsonArray!=null&&jsonArray.length()>0){
                                list=new ArrayList<>();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.optJSONObject(i);
                                    NearbyEntity entity=new NearbyEntity();
                                    entity.setBrandName(jsonObject.optString("brandName"));
                                    entity.setCarCode(jsonObject.optString("carCode"));
                                    entity.setCarId(jsonObject.optString("carId"));
                                    entity.setCarImgPath(jsonObject.optString("carImgPath"));
                                    entity.setCarSeatNum(jsonObject.optString("carSeatNum"));
                                    entity.setColor(jsonObject.optString("color"));
                                    entity.setCount(jsonObject.optString("count"));
                                    entity.setCreatedTime(jsonObject.optString("createdTime"));
                                    entity.setDistance(jsonObject.optString("distance"));
                                    entity.setLat(jsonObject.optString("lat"));
                                    entity.setLng(jsonObject.optString("lng"));
                                    entity.setModelNumber(jsonObject.optString("modelNumber"));
                                    entity.setType(jsonObject.optString("type"));
                                    entity.setStatus(jsonObject.optString("status"));
                                    entity.setOddMileage(jsonObject.optString("oddMileage"));
                                    entity.setOddPower(jsonObject.optString("oddPowerForNE"));
                                    entity.setPlateNumber(jsonObject.optString("plateNumber"));
                                    entity.setRM(jsonObject.optString("RM"));
                                    entity.setLastLocation(jsonObject.optString("lastLocationTxt"));
                                    list.add(entity);
                                }
                                adapter=new NearbyActivityAdapter(NearbyActivity.this,list);
                                adapter.setOnClickListener(new NearbyActivityAdapter.OnClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        clickLat = Double.valueOf(list.get(position).getLat());
                                        clickLng = Double.valueOf(list.get(position).getLng());
                                        clickLat+=Config.OFF_LAT;
                                        clickLng+=Config.OFF_LNG;
                                        mapPopupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                                    }
                                });
                                pull_refresh_load_recycler_view.setAdapter(adapter);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        Log.i("","");
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_navi:
                Intent intent = new Intent(this, NaviActivity.class);
                intent.putExtra("current_lat", lat);
                intent.putExtra("current_lon", lng);
                intent.putExtra("target_lat", clickLat + "");
                intent.putExtra("target_lon", clickLng + "");

//                intent.putExtra("current", new NaviLatLng(Double.valueOf(locationLat), Double.valueOf(locationLng)));
//                intent.putExtra("target", new NaviLatLng(Double.valueOf(clickLat), Double.valueOf(clickLng)));
                startActivity(intent);
                break;
            case R.id.tv_amap:
                openAMapNavi();
                break;
            case R.id.tv_baidu:
                openBaiduMapNavi();
                break;
            case R.id.img_title_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 打开高德地图
     */
    public void openAMapNavi() {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        LatLng latLng = new LatLng(clickLat, clickLng);
        naviPara.setTargetPoint(latLng);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
    }

    /**
     * 打开百度地图
     */
    public void openBaiduMapNavi() {
        try {
            clickLat-=Config.OFF_LAT;
            clickLng-=Config.OFF_LNG;
            Intent intent = Intent.parseUri("intent://map/direction?destination=" + MapUtils.bd_encrypt(clickLat, clickLng) + "&mode=driving&src=iDianNiu|iDianNiu#Intent;scheme=b" +
                    "dapp;package=com.baidu.BaiduMap;end", 0);
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent); //启动调用
                LogUtils.e("百度地图客户端已经安装");
            } else {
                LogUtils.e("没有安装百度地图客户端");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否安装了某应用
     *
     * @param packageName
     * @return
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
