package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.SPParams;
import www.qisu666.com.R;
import www.qisu666.com.util.UserParams;
import www.qisu666.sdk.amap.stationMap.HttpLogic_amap;
import www.qisu666.sdk.amap.stationMap.JsonUtil;
import www.qisu666.sdk.amap.stationMap.StationLocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//启动页面
public class SplashActivity extends BaseActivity implements AMapLocationListener {

    private int count = 2;//倒计时秒数
    private SharedPreferences sp;
    private UserParams user = UserParams.INSTANCE;

    //定位相关
    double lat = 0.0, lon = 0.0;//定位用
    String cityCode = "";   //城市编码
    public AMapLocationClient locationClient; //声明mlocationClient对象
    public AMapLocationClientOption locationOption = null;//声明mLocationOption对象


    @SuppressLint("HandlerLeak")//计时器handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count > 0) {
                count--;
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                if (getSharedPreferences(SPParams.CONFIG_INFO, MODE_PRIVATE).getBoolean("isFirst", true)) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        setView(R.layout.activity_splash, TYPE_BACKGROUND);
        initData();
        setUserInfo();
        countDown();//跳转页面
        location();//开始定位

    }

    private void initData() {
        sp = getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE);
    }

    private void setUserInfo() {
        user.setTel_no(sp.getString("mobile_no", ""));
        user.setCust_alias(sp.getString("user_name", ""));
        user.setS_token(sp.getString("s_token", ""));
        user.setSex(sp.getString("sex", ""));
        user.setUser_id(sp.getString("user_id", ""));
        user.setPicture(sp.getInt("picture", -1));
    }

    //开始倒计时
    private void countDown() {
//        handler.sendEmptyMessage(0);
        handler.sendEmptyMessageDelayed(0, 2000);   //发送消息
    }


    /**
     * 测试定位,获取经纬度
     */
    private void location() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();//初始化定位参数
//        locationOption.setInterval(2000);//设置定位间隔ms
        locationOption.setOnceLocation(true);//只定位一次
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(this); // 设置定位监听
        locationClient.startLocation();           //开始定位
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        LogUtil.e("定位成功");
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
//                LogUtil.e("定位成功 来源 "+  amapLocation.getLocationType());
//                LogUtil.e("定位成功 纬度 "+  amapLocation.getLatitude());
//                LogUtil.e("定位成功 经度 "+  amapLocation.getLongitude());
//                LogUtil.e("定位成功 精度 "+  amapLocation.getAccuracy());
//                LogUtil.e("定位成功 时间 "+  df.format(date));
//                LogUtil.e("定位成功 时间 "+  amapLocation.getCityCode());//城市编码
                cityCode = amapLocation.getCityCode();
                lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();//
                d109();
//                test_D109();
            } else {
                countDown();//定位失败直接跳转  防止无法继续正常流程
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtil.e("定位失败AmapError" + "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    //新增加的接口

    /**
     * 测试 d109 获取附近30km桩子的重载
     */
    public void d109() {

        String url = "api/pile/station/map/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("latitude", lat + "");
        map.put("longitude", lon + "");

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
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }


                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "D109");
//            //lon
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HttpLogic_amap httpLogic_amap = new HttpLogic_amap(this);
//        httpLogic_amap.sendRequest(Config.REQUEST_URL, jsonObject, true, new AbstractResponseCallBack() {
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                LogUtil.e(tag + "返回结果tag:" + tag);
//                LogUtil.e(tag + "返回结果:" + map.toString());
//                LogUtil.e(tag + "返回结果:" + map.get("record_list"));
////                List <StationLocation> sList = new ArrayList<StationLocation>();
//                try {
//                    JSONArray array = new JSONArray(map.get("record_list").toString());
//                    int count = array.length();
//                    LogUtil.e(tag + "返回结果-----数据大小----" + count);
//                    if (count == 0) { //没有数据
//                        LogUtil.e(tag + "返回结果-----没有数据");
//                    } else {
//                        LogUtil.e(tag + "返回结果-----有数据----");
//                        for (int i = 0; i < count; i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            StationLocation stationLocation = JsonUtil.parse(object.toString(), StationLocation.class);
//                            LogUtil.e(tag + i + "-每个对象station id: " + stationLocation.getStation_id());
//                            try {
//                                IDianNiuApp.getInstance().db.saveOrUpdate(stationLocation);
//                            } catch (Throwable t) {
//                                t.printStackTrace();
//                            }//保存数据库中
//                            LogUtil.e(tag + "0每个对象: " + object.toString());
//                            String oString = object.toString();
//                            LogUtil.e(tag + "每个对象: " + oString);
////                            list.add(JsonUtils.jsonToMap(oString));//添加桩子
//                        }
//                    }
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
////                countDown();
//            }
//        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        IDianNiuApp.cancelAllRequests("D109");//取消所有的地图预加载请求
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
