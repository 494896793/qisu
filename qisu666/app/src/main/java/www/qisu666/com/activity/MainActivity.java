package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import www.qisu666.com.R;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.config.Config;
import www.qisu666.com.fragment.HomeFragment;
import www.qisu666.com.fragment.ServiceFragment;
import www.qisu666.com.fragment.UserFragment;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.StatusBarCompat;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.CustomFragmentTabHost;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.ActivityController;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.SPParams;
import www.qisu666.common.utils.ToastUtil;

/**
 * 主界面  三个tab的宿主
 */
public class MainActivity extends BaseActivity implements AMapLocationListener {

    private CustomFragmentTabHost tabHost;
    private Class fragArray[] = {HomeFragment.class, ServiceFragment.class, UserFragment.class};
    private boolean isFirst = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isFirst = true;
        }
    };

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    private ImageView topImg;


    boolean logining = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.setStatusBarColor(getResources().getColor(R.color.main_background));
//        }

        setView(R.layout.activity_main, TYPE_NULL);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.i("=====","=====宽:"+width);
        Log.i("=====","=====高:"+height);
        getDiment();

        initViews();

        SharedPreferences.Editor editor = getSharedPreferences(SPParams.CONFIG_INFO, MODE_PRIVATE).edit().putBoolean("isFirst", false);
        editor.commit();
        startLocation();
        LogUtil.e("Registration Id :" + JPushInterface.getRegistrationID(this));

    }

    private void getDiment(){
        StringBuffer stringBuilder=new StringBuffer();
        for(int i=1;i<50;i++){
            stringBuilder.append("<dimen name=\"dimen_"+i+"sp\">"+i+"sp</dimen>\n");
        }
        Log.e("==========","============dimen:\n"+stringBuilder.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(final String event) {
        // TODO 用户唯一编号为空处理 目前是不处理
        // 登录失效处理
        if (event.contains("登陆失效")) {
            if (logining) {
                return;
            }
            logining = true;
            UserParams.INSTANCE.clear();
            ToastUtil.showToast(R.string.toast_prompt_login);
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(i, ConstantCode.REQ_LOGIN);
            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
                    logining = false;
                }
            }, 2000);
        }
        if (event.contains("身份证人工审核中")) {
            ToastUtil.showToast("身份证审核中，功能受限");
        }

    }


    /**
     * 发送 A102 请求，退出登录
     */
    private void unLogin() {
        //更新用户资料的ui
        EventBus.getDefault().post("exit_login");
        try {
            HttpLogic httpLogic = new HttpLogic(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("req_code", "A102");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("device_uuid", PhoneParams.getDeviceUUID(this));
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
            httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onResponse(Map<String, Object> map, String tag) {
//                    UserParams.INSTANCE.clear();
//                    UserParams.INSTANCE.checkLogin(MainActivity.this);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserParams.INSTANCE.clear();
        ToastUtil.showToast(R.string.toast_prompt_login);
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, ConstantCode.REQ_LOGIN);
    }


    /**
     * 初始化控件
     */
    private void initViews() {
        tabHost = (CustomFragmentTabHost) findViewById(R.id.tab_host);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fragment_container);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setBackgroundColor(getResources().getColor(R.color.content_bg));

        topImg = (ImageView) findViewById(R.id.top_img);

        int count = fragArray.length;
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab" + i).setIndicator(getTabItemView(i));
            tabHost.addTab(tabSpec, fragArray[i], null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // 切换 MainActivity 头部图片
                // 首页
                if (tabId.equals("tab0")) {
                    topImg.setVisibility(View.VISIBLE);
                    topImg.setImageResource(R.mipmap.sy_8);
                }
                // 我的订单
                if (tabId.equals("tab1")) {
                    topImg.setVisibility(View.GONE);
                }
                // 个人中心
                if (tabId.equals("tab2")) {
                    topImg.setVisibility(View.VISIBLE);
                    topImg.setImageResource(R.mipmap.grzx_bg);
                }
            }
        });

    }

    /**
     * 初始化tabItem
     */
    private View getTabItemView(int index) {
        int tabArray[] = {R.string.home_title, R.string.service_title, R.string.user_title};
        int iconArray[] = {R.drawable.selector_item_th_main_1, R.drawable.selector_item_th_main_2, R.drawable.selector_item_th_main_3};

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_th_main_activity, null);

        //设置tab图标
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
        imageView.setImageResource(iconArray[index]);

        //设置tab标题
        TextView textView = (TextView) view.findViewById(R.id.tv_icon);
        textView.setText(tabArray[index]);

        return view;
    }

    /**
     * 点击返回键2次，退出应用
     */
    @Override
    public void onBackPressed() {
        if (isFirst) {
            isFirst = false;
            ToastUtil.showToast(R.string.toast_prompt_double_click_exit);
            handler.sendEmptyMessageDelayed(0, 3000);
        } else {
            ActivityController.getInstance().closeAllActivity();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserParams.INSTANCE.clear();
        ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
        EventBus.getDefault().unregister(this);
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        //设置定位监听
        locationClient.setLocationListener(this);
        //设置为高精度定位模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        locationClient.startLocation();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            LogUtils.d("当前位置：" + amapLocation.getLatitude() + "," + amapLocation.getLongitude() + "," + amapLocation.getAddress());
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            LogUtils.e("AmapErr:" + errText);
        }

    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case ConstantCode.CAMERA:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, CaptureActivity.class));
                    } else {
                        ToastUtil.showToast(R.string.toast_permission_camera);
                    }
                    break;
                case ConstantCode.LOCATION_MAP:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, StationMapActivity.class));
                    } else {
                        ToastUtil.showToast(R.string.toast_permission_location);
                    }
                    break;
                case ConstantCode.LOCATION_NEARBY:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, NearbyStationActivity.class));
                    } else {
                        ToastUtil.showToast(R.string.toast_permission_location);
                    }
                    break;
                case ConstantCode.LOCATION_COLL:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, CollectionActivity.class));
                    } else {
                        ToastUtil.showToast(R.string.toast_permission_location);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
