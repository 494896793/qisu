package www.qisu666.sdk.amap.carShare;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareCompleteActivity;
import www.qisu666.com.activity.CarShareMapActivity;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.event.FinishActivityEvent;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.PrefUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.AlertPhotoDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.carShare.bean.CarDiLi;
import www.qisu666.sdk.partner.Activity_ContractDetail;

/**
 * 717219917@qq.com 2018/5/16 15:58.
 *///网点外还车界面
public class Activity_ReturnCarEveryWhere extends BaseActivity implements AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.where_julizuijin)
    TextView where_julizuijin; //您已经到达网点/距离最近网点
    @BindView(R.id.where_wangdian)
    TextView where_wangdian;      //网点
    @BindView(R.id.where_layout)
    LinearLayout where_layout;       //距离等布局
    @BindView(R.id.where_gongli)
    TextView where_gongli;          //公里
    @BindView(R.id.where_yuan)
    TextView where_yuan;             //元
    @BindView(R.id.where_no)
    TextView where_no;                 //提示  不收取诺车费
    @BindView(R.id.where_sure)
    TextView where_sure;             //确认还车

    //反地理编码用
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    String adcode = "";  //根据车的经纬度获取 区域
    private String endLocationTxt;//最终还车需要传过去的位置说明
    GeocodeSearch geocodeSearch;//反地理编码

    //车经纬度
    String lat = "", lon = ""; //接口获取到的经纬度


    //需要保存的
    String stationCode = "";
    String outOfDistance = "";
    String stationLocation = "";
    String parkedAmount = "";
    String distance = "";
    String stationName = "";
    String stationId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_returncareverywhere);
        getDistanceOrMoney();//同时展示ui
        initTitleBar();
        initAmap();

    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("立即还车");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        where_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adcode.equals("'")) {
                    ToastUtil.showToast("没有获取到位置,请先打开位置权限！");
                } else {
                    if (outOfDistance.equals("1")) {
                        DialogHelper.confirmTitleDialog(Activity_ReturnCarEveryWhere.this, "当前未到达还车网点",
                                "还车需缴纳挪车费" + parkedAmount + "元", new AlertDialog.OnDialogButtonClickListener() {
                                    @Override
                                    public void onConfirm() {
                                        getLatLot();
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                    }else {
                        getLatLot();
                    }

                }

            }
        });
    }

    private void initAmap() {
        locationClient = new AMapLocationClient(getApplicationContext());//初始化定位
        locationClient.setLocationListener(this); //设置定位回调监听
        locationOption = new AMapLocationClientOption(); //初始化AMapLocationClientOption对象
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置定位模式为高精度模式
        locationOption.setOnceLocation(true); //获取一次定位结果： 该方法默认为false。
        locationClient.setLocationOption(locationOption);//给定位客户端对象设置定位参数

        //地理搜索类
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        if (null != locationClient) {
            locationClient.startLocation(); //启动定位
        }
    }


    /**
     * 2.获取车辆经纬度
     */
    private void getLatLot() {
        String url = "api/car/info/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", SPUtil.get(mContext, "carCode", ""));//动态修改carCode
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))//可以去掉object
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        // 对象转json
                        String json = JsonUtils.objectToJson(bean);
                        // json转map key vaule
                        Map jsonToMap = JsonUtils.jsonToMap(json);

                        lon = jsonToMap.get("longitude").toString();
                        lat = jsonToMap.get("latitude").toString();

                        Log.e("ffff","lon  请求成功:" + lon);
                        Log.e("ffff","lon  请求成功:" + lat);
                        
//                        try {
//                            LogUtil.e("经纬度  请求成功:" + bean.toString());//longitude=114.044952, latitude=22.601326,
//                            String lat_tmp = bean.toString().substring(bean.toString().indexOf("longitude") + 10, bean.toString().indexOf("longitude") + 25);
//                            lon = lat_tmp.substring(0, lat_tmp.indexOf(","));
//                            Log.e("ffff","lon  请求成功:" + lon);
//                            String lon_tmp = bean.toString().substring(bean.toString().indexOf("latitude") + 9, bean.toString().indexOf("latitude") + 25);
//                            lat = lon_tmp.substring(0, lon_tmp.indexOf(","));
//                            Log.e("ffff","lon  请求成功:" + lat);
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }
//                        //第一个接口请求成功之后  进行逆地理编码
//                        String lattt = "113.751043";
//                        String lonnn = "23.053412";
//                        LogUtil.e("经纬度 par：" + Double.parseDouble(lattt));
                        getAddressByLatlng(Double.parseDouble(lon), Double.parseDouble(lat));   //正式传后台
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast("经纬度 请求失败:" + bean.msg);
                    }
                });
    }


    //进行反地理编码请求  结果为异步
    private void getAddressByLatlng(double longitude, double latitude) {
        Log.e("fffff","经纬度  请求 反地理编码 lat:" + latitude + ",lon:" + longitude);
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latitude + Config.OFF_LAT, longitude + Config.OFF_LNG);//谷歌转高德 直接加
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);//异步查询
        return_car();//还车接口

    }


    //最后展示结果距离 公里 元
    public void getDistanceOrMoney() {
        String url = "api/tss/distance/return/car";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("orderCode", PrefUtil.getString(mContext, PrefUtil.ORDER_CODE));//订单编码
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(CarDiLi.class))
                .compose(RxNetHelper.<CarDiLi>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<CarDiLi>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarDiLi bean) {
                        LogUtil.e("还车成功 结果发送到 展示页面" + bean.toString());
                        stationCode = bean.getStationCode();
                        outOfDistance = bean.getOutOfDistance();
                        stationLocation = bean.getStationLocation();
                        parkedAmount = new BigDecimal(bean.getParkedAmount() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                        distance = new BigDecimal(bean.getDistance() / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                        stationName = bean.getStationName();
                        stationId = bean.getStationId();
                        LogUtil.e("还车成功 结果发送到 展示页面" + bean.getStationCode());
                        LogUtil.e("还车成功 结果发送到 展示页面" + bean.getStationId());
                        LogUtil.e("还车成功 结果发送到 展示页面" + bean.getStationLocation());
                        LogUtil.e("还车成功 结果发送到 展示页面" + bean.getStationName());
                        if (outOfDistance.equals("1")) {
                            where_no.setVisibility(View.GONE);
                            where_layout.setVisibility(View.VISIBLE);
                            where_julizuijin.setText("当前距离最近网点");
                            where_wangdian.setText(bean.getStationName());
                            where_gongli.setText(distance);          //公里
                            where_yuan.setText(parkedAmount);

                        } else if(outOfDistance.equals("2")){
                            where_no.setVisibility(View.VISIBLE);
                            where_layout.setVisibility(View.GONE);
                            where_julizuijin.setText("您已到达还车网点");
                            where_wangdian.setText(bean.getStationName());
                        }else{
                            where_no.setVisibility(View.VISIBLE);
                            where_layout.setVisibility(View.GONE);
                            where_julizuijin.setText("当前距离最近网点");
                            where_wangdian.setText(bean.getStationName());
                            where_no.setText("约"+distance+"公里,请驶入网点后再确认还车");
                        }


                    }

                    @Override
                    public void onFail(Message<CarDiLi> bean) {
                        LogUtil.e("距离获取失败:" + bean.msg);
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    //最终还车接口  还车以前需要adcode
    public void return_car() {
        if(endLocationTxt!=null&&!endLocationTxt.equals("")){
            String url = "api/tss/car/return";
            HashMap<String, Object> requestMap = new HashMap<>();
            requestMap.put("orderCode", PrefUtil.getString(mContext, PrefUtil.ORDER_CODE));//订单编码
            requestMap.put("endLocationTxt", endLocationTxt);//反地理编码
            requestMap.put("adcode", adcode);                 //订单编码
            MyNetwork.getMyApi()
                    .carRequest(url, MyMessageUtils.addBody(requestMap))
                    .map(new FlatFunction<>(Object.class))
                    .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                    .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                        @Override
                        public void onSuccessCode(Message object) {

                        }

                        @Override
                        public void onSuccess(Object bean) {
                            EventBus.getDefault().post(new FinishActivityEvent());
                            Log.e("Asd", "还车成功 结果发送到 展示页面" + bean.toString());
                            Gson g = new Gson();
                            ActivityUtil.startActivityWithOne(mContext, CarShareCompleteActivity.class, g.toJson(bean));
                            finish();
                        }

                        @Override
                        public void onFail(Message<Object> bean) {
                            if (bean.code == -1004) {
                                Log.e("asd", "还车失败:" + bean.msg);
                                Gson g = new Gson();
                                ActivityUtil.startActivityWithOne(mContext, CarShareCompleteActivity.class, g.toJson(bean.data));
                                ToastUtil.showToast(bean.msg);
                                finish();
                            } else {
                                ToastUtil.showToast(bean.msg);
                            }
                        }
                    });
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    //得到逆地理编码异步查询结果
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);
        LogUtil.e("查询经纬度对应详细地址：" + simpleAddress);
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getAdCode());
        try {
            adcode = regeocodeAddress.getAdCode();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            endLocationTxt = regeocodeAddress.getFormatAddress();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getCityCode());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getCity());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getFormatAddress());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getCity());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getBuilding());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getDistrict());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getProvince());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getNeighborhood());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getTownship());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getStreetNumber().getDirection());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getStreetNumber().getStreet());
        LogUtil.e("查询经纬度对应详细地址：" + regeocodeAddress.getStreetNumber().getNumber());

        getDistanceOrMoney();


    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
        String formatAddress = geocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);
        LogUtil.e("222查询经纬度对应详细地址：" + simpleAddress);
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getAdcode());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getCity());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getBuilding());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getDistrict());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getFormatAddress());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getProvince());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getLevel());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getNeighborhood());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getTownship());
        LogUtil.e("222查询经纬度对应详细地址：" + geocodeAddress.getLatLonPoint());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
