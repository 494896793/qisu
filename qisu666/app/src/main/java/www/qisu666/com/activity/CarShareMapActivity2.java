package www.qisu666.com.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.de.hdodenhof.circleimageview.CircleImageView;
import com.droid.Activity_SelectCity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CarItemPagerAdapter;
import www.qisu666.com.carshare.CarShareMapPreActivity;
import www.qisu666.com.carshare.CarSlidePopupHelper;
import www.qisu666.com.carshare.MapPopupHelper;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.PopupHelper;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyDisposableSubscriber;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.event.CarMapEvent;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.model.CarNearBean;
import www.qisu666.com.model.CarNowOrderBean;
import www.qisu666.com.model.CarOrderBean;
import www.qisu666.com.model.DoingBean;
import www.qisu666.com.model.DriverInfoBean;
import www.qisu666.com.model.PositionBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.PhotoDialogHelper;
import www.qisu666.com.util.PrefUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.TransFormUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.AlertPhotoDialog;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.com.widget.MyRadioGroup;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.carshare.Activity_CarshareWeb;

//立即用车页面  共享汽车
public class CarShareMapActivity2 extends CarShareMapPreActivity implements AMapNaviListener {

    @BindView(R.id.map_view)
    MapView map_view;
//    @BindView(R.id.radio_group)
//    MyRadioGroup radio_group;
    @BindView(R.id.layout_map)
    RelativeLayout layout_map;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.img_title_right)
    ImageView right_btn;
    @BindView(R.id.img_title_right2)
    ImageView right_btn2;
    @BindView(R.id.layout_main)
    RelativeLayout layout_main;
    @BindView(R.id.rl_book_car)
    RelativeLayout rlBookCar;
    @BindView(R.id.ll_car_book)
    LinearLayout llCarBook;
    @BindView(R.id.ll_car_order)
    LinearLayout llCarOrder;
    @BindView(R.id.cv_time)
    CountdownView cvTime;
    @BindView(R.id.img_item_car)
    ImageView imgItemCar;
    @BindView(R.id.tv_item_car_num)
    TextView tvItemCarNum;
    @BindView(R.id.tv_item_car_name)
    TextView tvItemCarName;
    @BindView(R.id.tv_item_car_type)
    TextView tvItemCarType;
    @BindView(R.id.tv_item_car_mile)
    TextView tvItemCarMile;
    @BindView(R.id.tv_item_car_power)
    TextView tvItemCarPower;
    @BindView(R.id.tv_map_address)
    TextView tvMapAddress;
    @BindView(R.id.cv_time_use)
    CountdownView cvTimeUse;
    @BindView(R.id.tv_time_use_money)
    TextView tvTimeUseMoney;   //当前费用
    @BindView(R.id.ll_item_car_use)
    LinearLayout llItemCarUse;   //使用时长
    @BindView(R.id.ll_item_car_book)
    LinearLayout llItemCarBook; //整个弹出的pop界面
    @BindView(R.id.ll_car_by_time)
    LinearLayout llCarByTime;
    @BindView(R.id.btn_map_charging)
    LinearLayout btnMapCharging;
    @BindView(R.id.ll_driver_tip)
    LinearLayout llDriverTip;
    @BindView(R.id.ll_item_driver)
    LinearLayout llItemDriver;    //司机信息
    @BindView(R.id.img_car_driver)
    CircleImageView imgCarDriver; //司机头像  圆形
    @BindView(R.id.tv_car_driver_name)
    TextView tvCarDriverName;
    @BindView(R.id.tv_car_driver_star_num)
    TextView tvCarDriverStarNum;
    @BindView(R.id.tv_car_driver_years)
    TextView tvCarDriverYears;
    @BindView(R.id.tv_use_time)
    TextView tvUseTime;                    //使用时长
    @BindView(R.id.cv_driver_time_use)
    CountdownView cvDriverTimeUse;
    @BindView(R.id.tv_driver_money)
    TextView tvDriverMoney;
    @BindView(R.id.tv_driver_time_money)
    TextView tvDriverTimeMoney;
    @BindView(R.id.tx_all_car)
    TextView tx_all_car;
    @BindView(R.id.tx_car)
    TextView tx_car;
    @BindView(R.id.tx_best_car)
    TextView tx_best_car;
    @BindView(R.id.tx_change_address)
    TextView tx_change_address;

    @BindView(R.id.ll_dongli_layout)
    LinearLayout ll_dongli_layout;//关闭动力

    @BindView(R.id.ll_car_order_open2_dongli_b)
    LinearLayout ll_car_order_open2_dongli_b;//打开动力
    @BindView(R.id.ll_car_order_close2_dongli_b)
    LinearLayout ll_car_order_close2_dongli_b;//关闭动力
    @BindView(R.id.dongli_navi)
    ImageView dongli_navi;                                     //动力  地图导航
    @BindView(R.id.dongli_map_old)
    LinearLayout dongli_map_old;


    String dongli = "0";    //bootType  0常规  1一键启动
    String powerStatus = "0";//动力关闭默认0


    @BindView(R.id.money_gengduo)
    ImageView money_gengduo;//还车dialog界面的更多
    boolean ismoneygengduo = true;   //还车 新增 判断ui是否显示 更多
    @BindView(R.id.ll_item_car_use_detail)
    LinearLayout ll_item_car_use_detail;//隐藏dialog 计费粗略的布局

    @BindView(R.id.car_share_map_fenshizuche_txt)
    TextView car_share_map_fenshizuche_txt;//分时租车 和按日租车的显示问题

//    @BindView(R.id.TextView_Normal_Dark_Gray_MarginLeft) TextView TextView_Normal_Dark_Gray_MarginLeft;//分时租车

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;

    private View p_station_window;
    private boolean isFirst = true;  //

    //    private List<Map<String, Object>> list;
    private List<PositionBean> list;

    private boolean isShowList = false;


    //当前城市str
    private String current_city_str="";
    //动画
    private Animator mRightOutSet;
    private Animator mLeftInSet;

    public static final String TYPE_FAST = "2", TYPE_SLOW = "1", TYPE_MIX = "3";//2快充   1快充  3混合

    private AMap aMap;

    private double curLat, curLon, markerLat, markerLon;//curlat/lon  定位的经纬度    marklat/lon 标志点的经纬度
    private double longitude_start, longitude_end, latitude_start, latitude_end;
    private boolean refreshFlag = true;


    private String area_code = "5810";//定位城市code
    private String cityCode = "";     //城市编码
    private String station_id;//选中的站点id
    private String station_name;//选中的站点名称
    private RelativeLayout.LayoutParams lp;//桩点列表列表的布局参数

    //筛选条件
    private String charge_interface //充电接口
            , charge_carr           //
            , charge_method         //充电方式
            , charge_pile_bel      //
            , parking_free         //停车费
            , service_time;        //服务时间

    private boolean finishLocation = false;     //是否已定位完毕
    private boolean isTouch = false;             //手指是否触摸地图
    private boolean favorFlag = false;           //当前站点是否被收藏
    private PositionBean curMarkerData;         //当前点击marker的数据
    private Marker curMarker;                   //当前点击的marker
    private Map<String, String> filterMap;      //当前筛选条件集合
    private LoadingDialog dialog;              //请求定位的Dialog
    private CarItemPagerAdapter mAdapter;      //车辆列表的适配器
    private PopupHelper popupHelper;           //前往充电等deialog
    private MapPopupHelper mapPopupHelper;     //信息点的 popu  弹出立即订车
    private CarSlidePopupHelper carSlidePopupHelper; //立即订车的 pager5个
    private AMapNavi mAMapNavi;                      //导航
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 113.355642);

    /**
     * 开始点坐标集合［建议就一个开始点］
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();

    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();

    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    private RouteOverLay routeOverLay;
    private Context mContext;
    private String testLng = "114.1144306149";
    private String testLat = "22.5640882213";
    List<Marker> markerList = new ArrayList<>();
    private final int TIME_LAST = 30 * 60 * 1000;//倒计时持续时间

    private String carCode;      //车辆编码
    private String orderCode;    //订单编码
    private String driverType;   //司机类型？

    private boolean isUsingCar = false;                  //是否正在用车
    private final String TIME_SECOND = "timeSecond";     //查询订单的实时费用
    private final Timer timer = new Timer();
    private int counter = 0;
    private String driverPhone;                           //司机电话
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (isUsingCar) {
                EventBus.getDefault().post(new CarMapEvent(TIME_SECOND));
            } //如果正在用车  发送event事件
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_map);
        mContext = this;
        EventBus.getDefault().register(this);//注册eventbus
        initView();      //初始view  //  initFilter();
        setPreferenece();            //  初始化sp
        initMap(savedInstanceState); //  高德
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_CONTACT = 10001;
//            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission_group.LOCATION}; //验证是否许可权限
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) { //申请权限
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                    return;
//                }
//            }
//        }

//        setAnimators();
//        setCameraDistance();

    }

    private void setPreferenece() {
        carCode = PrefUtil.getString(mContext, PrefUtil.CAR_CODE);
        orderCode = PrefUtil.getString(mContext, PrefUtil.ORDER_CODE);
        driverType = PrefUtil.getString(mContext, PrefUtil.DRIVER_TYPE);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        requestDoing();
        requestStation();//查询附近车辆
//        ToastUtil.showToast("onNewIntent");
    }

    private void initMap(Bundle savedInstanceState) {
        map_view = (MapView) findViewById(R.id.map_view);
        map_view.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map_view.getMap();
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.getUiSettings().setRotateGesturesEnabled(true);
//        aMap.getUiSettings().setScrollGesturesEnabled(true);
        setUpMap();
        setUpMapNavi();
    }

    private void setUpMapNavi() {
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
    }

    private void initView() {
//        rlBookCar.setVisibility(View.GONE);
//        radio_group = (MyRadioGroup) findViewById(R.id.radio_group);
//        radio_group.setCheckWithoutNotif(R.id.rb_all_car);
//        radio_group.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_all_car:
//                        ToastUtil.showToast("All");
//                        break;
//                    case R.id.rb_two_site_car:
//                        ToastUtil.showToast("Two");
//                        break;
//                    case R.id.rb_five_site_car:
//                        ToastUtil.showToast("Five");
//                        break;
//                }
//            }
//        });

        initTitleBar();
        cvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                LogUtil.e("取消预约 事件超时--------");
                requestCancle();
            }
        });
        timer.scheduleAtFixedRate(task, 0, 1000);  //开始1second的定时任务

        popupHelper = PopupHelper.of(this);
        carSlidePopupHelper = CarSlidePopupHelper.of(this, 0);
        mapPopupHelper = MapPopupHelper.of(this);

    }

    private void initTitleBar() {
        title.setText("共享汽车");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        right_btn.setImageResource(R.mipmap.ic_station_list);
//        right_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showOrHideList();
//            }
//        });
//        right_btn2.setImageResource(R.mipmap.ic_station_filter);
//        right_btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CarShareMapActivity.this, ChargingFilterActivity.class);
//                intent.putExtra("filterMap", (HashMap) filterMap);
//                startActivityForResult(intent, ConstantCode.REQ_FILTER);
//            }
//        });
    }

    private void showOrHideList() {
        LogUtil.e("进入方法：showOrHiderList" + isShowList);
//        if (!isShowList) {//显示列表
//            title.setText("列表找车");
//            right_btn.setImageResource(R.mipmap.ic_station_map);
//        } else {//隐藏列表
//            title.setText("共享汽车");
//            right_btn.setImageResource(R.mipmap.ic_station_list);
//        }
//        LogUtils.e(layout_map.getHeight() + "height");
//        isShowList = !isShowList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarMapEvent(String event) {
        LogUtil.e("收到事件 预约成功-----");
        if (event.equals("预约成功")) {
            requestStation();//更新地图数据
        } else if (event.equals("返回主界面")) {
            finish();
        }
    }


    /**
     * 收到正在用车事件  每分钟查询一次订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarMapEvent(CarMapEvent event) {
        LogUtil.e("收到事件：onCarMapEvent" + event.isFromCarSlide);
        if (event != null) {
            if (event.isFromCarSlide) {
                closeMarker(curMarker);
            }
            if (!TextUtils.isEmpty(event.text)) {
                switch (event.text) {
                    case TIME_SECOND:
                        counter++;
                        long leftTime = cvTimeUse.getRemainTime() + 1000;
                        LogUtil.e("当前时长：" + leftTime);
                        cvTimeUse.updateShow(leftTime);
                        long leftTime1 = cvDriverTimeUse.getRemainTime() + 1000;
                        cvDriverTimeUse.updateShow(leftTime1);
                        if (counter % 60 == 0) {
                            counter = 0;
//                            requestDoing();//每分钟请求一次 刷新数据
                        }
//                        requestDoing();
//                        requestNowState();
                        break;
                }
            }
            switch (event.viewId) {
                //CarSlidePopupHelper里面的event
                case R.id.img_navi://信息点
                    carSlidePopupHelper.dismiss();
                    mapPopupHelper.show();
                    break;
                case R.id.btn_map_charging:
//                    ToastUtil.showToast("position" + event.position+","+carCode);
//                    Intent intent1 = new Intent(mContext, CarShareConfirmActivity.class);
//                    startActivity(intent1);
                    carSlidePopupHelper.dismiss();
                    break;
                //MapPopupHelper的event，各种导航选项
                case R.id.tv_amap://高德地图
                    mapPopupHelper.dismiss();
                    mapPopupHelper.openAMapNavi(markerLat, markerLon);
                    break;
                case R.id.tv_start_navi://导航
                    Intent intent = new Intent(mContext, NaviActivity.class);
                    intent.putExtra("current_lat", curLat + "");
                    intent.putExtra("current_lon", curLon + "");
                    intent.putExtra("target_lat", markerLat + "");
                    intent.putExtra("target_lon", markerLon + "");
//                    intent.putExtra("current", new NaviLatLng(curLat, curLon));
//                    intent.putExtra("target", new NaviLatLng(markerLat, markerLon));
                    startActivity(intent);
                    mapPopupHelper.dismiss();
                    break;
                case R.id.tv_baidu://百度地图
                    mapPopupHelper.dismiss();
                    mapPopupHelper.openBaiduMapNavi(markerLat, markerLon);
                    break;

            }
        }
    }

//    // 设置动画
//    private void setAnimators() {
//        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_rotate_out);
//        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_rotate_in);
//
//        // 设置点击事件
//        mRightOutSet.addListener(new AnimatorListenerAdapter() {
//            @Override public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                right_btn.setClickable(false);
//            }
//        });
//        mLeftInSet.addListener(new AnimatorListenerAdapter() {
//            @Override public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                right_btn.setClickable(true);
//            }
//        });
//    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        layout_map.setCameraDistance(scale);
//        lv_station_list.setCameraDistance(scale);
    }

    /**
     * 花费  当前费用
     */
    private void requestNowState() {
        //type 0:分时,1:按日
        //网点预约租车
        LogUtil.e("进入方法：requestNowState");
        String url = "api/tss/cost/show";
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", orderCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarOrderBean.class))
                .compose(RxNetHelper.<CarOrderBean>io_main())
                .subscribe(new ResultSubscriber<CarOrderBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarOrderBean bean) {
                        LogUtil.e("请求成功," + bean.orderInfo.costFinal);//最终花费
//                        long endTime = bean.createdTimeLong + TIME_LAST;
//                        long currentTime = System.currentTimeMillis();
//                        long leftTime = endTime - currentTime;
//                        long leftTime = bean.orderInfo.returnTimeLong - bean.borrowTimeLong;
//                        //订单结束
//                        if (leftTime <= 0) {
//                            return;
//                        }
//                        cvTimeUse.updateShow(leftTime);
                        CarNowOrderBean carNowOrderBean = bean.orderInfo;
                        tvTimeUseMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.costFinal) + "元");
                        tvDriverMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.driverCost) + "元");
                        tvDriverTimeMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.costFinal) + "元");
                        tvTimeUseMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.costFinal) + "元");
                    }

                    @Override
                    public void onFail(Message<CarOrderBean> bean) {
                        LogUtil.e("异常：" + bean.msg);
                    }

                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("进入方法：onResume");
        setPreferenece();
        try {
            map_view.onResume();

            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setOnMarkerClickListener(this);
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMapTouchListener(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        x.task().postDelayed(new Runnable() {   //取消跟随模式
            @Override
            public void run() {
                aMap.setMyLocationEnabled(false);
            }
        }, 1000);

//        requestNowState();
//        if (!TextUtils.isEmpty(orderCode) && !TextUtils.isEmpty(driverType)) {
//            requestOrderState();
//        }else {
//            clearPreference();
//            orderVisable(-1);
//        }


        try {
            String istongyi = (String) SPUtil.get(this, "奇速共享汽车使用协议", "false");
            if (!istongyi.equals("true")) {
                startActivity(new Intent(this, Activity_CarshareWeb.class));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        requestDoing();//注释掉  防止多次加载

//        if (isUsingCar){
//            requestNowState();
//        }
//        requestLeftTime();
    }

    /**
     * 预定锁定剩余倒计时
     */
    private void requestLeftTime() {
        LogUtil.e("进入方法：requestLeftTime");
        String url = "api/tss/order/book/locktime/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
////                        long leftTime = bean;
////                        if (leftTime <= 0) { requestCancle();  return;  } //订单结束
//                        double a = 0.001;
//                        double b = 0.0011;
//                        BigDecimal data1 = new BigDecimal(a);
//                        BigDecimal data2 = new BigDecimal(b);
//                        data1.compareTo(data2) ;//直接用compareto
                        try {
                            String data = bean.toString().substring(bean.toString().indexOf("data") + 1, bean.toString().length());
                            double leftTime_doub = Double.parseDouble(data);
                            if (leftTime_doub <= 0.0) {
                                LogUtil.e("进入超时取消。。。。");
//                              ToastUtil.showToast("超时取消");
                                requestCancle();
                            }
                            cvTime.start((long) leftTime_doub);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            ToastUtil.showToast("获取倒计时异常！");
                        }//进入异常
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }
                });
    }


    /**
     * 租车进行中的订单查询
     */
    private void requestDoing() {
        LogUtil.e("进入方法：requestDoing");
        //状态 0:预约成功 1:开始计费(传两个则逗号隔开)
        if (TextUtils.isEmpty(UserParams.INSTANCE.getUser_id())) {
            return;
        }
        String url = "api/tss/order/query/map";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("status", "0,1");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(DoingBean.class))
                .compose(RxNetHelper.<DoingBean>io_main())
                .subscribe(new ResultSubscriber<DoingBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(DoingBean bean1) {
                        try {
                            dongli = bean1.orderList.get(0).car.bootType;
                            LogUtil.e("获取到动力源：" + dongli);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }  //默认0 为关闭
                        try {
                            powerStatus = bean1.orderList.get(0).car.powerStatus;
                            LogUtil.e("获取到动力开关状态：" + powerStatus);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            powerStatus = "0";
                        }


                        requestNowState();
                        List<CarNowOrderBean> list = bean1.orderList;
                        if (list == null) return;
                        if (list.size() > 0) {
                            CarNowOrderBean bean = list.get(0);
                            try {
                                PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, bean.carCode);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }//carcode
                            LogUtil.e("获取到carCode:" + bean.carCode);
                            try {
                                SPUtil.put(mContext, "carCode", bean.carCode);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }

                            LogUtil.e("获取到 status :" + bean.status);
                            LogUtil.e("获取到 driverType :" + bean.driverType);

                            if (bean.status.equals("1") && bean.driverType.equals("1")) {//订单进行中 & 代驾模式
                                llItemDriver.setVisibility(View.VISIBLE);
                                llItemCarUse.setVisibility(View.GONE);
                                setDriverView(bean1);
                            } else {
                                llItemDriver.setVisibility(View.GONE);
                            }

                            setDoingView(bean);


                        }

                    }

                    @Override
                    public void onFail(Message<DoingBean> bean) {
                        LogUtil.e("计费异常：" + bean.msg);
                        if (bean.code == -1035 || bean.code == -1034) {
                            Err2exit(bean.msg);
                        }
                        if (bean.code == MyDisposableSubscriber.OBJDECT_EMPTY) {
                            orderVisable(-1);
                        }

                    }
                });
    }

    //订单状态异常时 弹窗提示
    private void Err2exit(final String str) {
        try {
            DialogHelper.alertDialog(CarShareMapActivity2.this, str, new AlertDialog.OnDialogButtonClickListener() {
                @Override
                public void onConfirm() {
                    finish();
                }

                @Override
                public void onCancel() {
                    finish();
                }
            }, false);//不可以取消
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void setDriverView(DoingBean bean1) {
        LogUtil.e("进入方法：setDriverView");
        DriverInfoBean bean = bean1.driverInfo;
        CarNowOrderBean carNowOrderBean = bean1.orderList.get(0);
        long leftTime = System.currentTimeMillis() - carNowOrderBean.borrowTimeLong;
        if (leftTime <= 0) {
            return;
        } //订单结束
        cvDriverTimeUse.updateShow(leftTime);
        //需要放开注释
        tvDriverMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.driverCost) + "元");
        tvDriverTimeMoney.setText(TransFormUtil.fen2yuan(carNowOrderBean.costFinal) + "元");
        if (bean == null) {
            llDriverTip.setVisibility(View.VISIBLE);
        } else {
            llDriverTip.setVisibility(View.GONE);
            Picasso.with(mContext).load(StringUtil.addImageHost(bean.picture)).into(imgCarDriver);
            tvCarDriverName.setText(bean.relName + "(代驾)");
            tvCarDriverStarNum.setText(bean.driverLevel);
            tvCarDriverYears.setText("车龄:" + bean.driverAge + "年");
            driverPhone = bean.mobileNo;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDoingView(CarNowOrderBean bean) {
        //状态 0:预约成功 1:开始计费 2:已还车，待付费 3:订单已完成 4:订单已取消
        String need2show = "01";
        String status = bean.status;
        LogUtil.e("获取到 status :" + bean.status);
        isUsingCar = false;
        if (!need2show.contains(status)) {
            return;
        }
        LogUtil.e("获取到2 status :" + bean.status);
        orderVisable(Integer.valueOf(status));
        if (status.equals("0")) {
            requestLeftTime();

            long endTime = TransFormUtil.TimeStamp2Date(bean.createdTime) + TIME_LAST;
            long currentTime = System.currentTimeMillis();
            long leftTime = endTime - currentTime;

            LogUtil.e("进入view 取消订单-----" + currentTime);
            LogUtil.e("进入view 取消订单-----" + endTime);

            //订单结束
            if (leftTime <= 0) {
                LogUtil.e("进入view 取消订单-----" + leftTime);
                requestCancle();
                return;
            }
            cvTime.start(leftTime);


        } else if (status.equals("1")) {
            isUsingCar = true;
//            requestNowState();//不再重复调用

//            long leftTime = bean.returnTimeLong - bean.borrowTimeLong;
            long leftTime = System.currentTimeMillis() - bean.borrowTimeLong;
            //订单结束
            if (leftTime <= 0) {
                return;
            }

            LogUtil.e("当前时长：" + leftTime);
            LogUtil.e("当前时长：" + leftTime);
            System.out.println("longToDate：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(leftTime)));
            cvTimeUse.updateShow(leftTime);
//            tvTimeUseMoney.setText(TransFormUtil.fen2yuan(bean.costFinal) + "元");
        }
        try {
            PrefUtil.saveString(mContext, PrefUtil.ORDER_CODE, bean.orderCode);
            PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, bean.carCode);
        } catch (Throwable t) {
            t.printStackTrace();
        }

//        LogUtil.e("地图点："+bean.car.getStationInfo().lat);
//        LogUtil.e("地图点："+bean.car.getStationInfo().lat);

//        LogUtil.e("地图点："+bean.car.carImgPath);
//        LogUtil.e("地图点："+bean.car.getCarBrandModels().brandName);
//        LogUtil.e("地图点："+bean.car.carSeatNum);
//        LogUtil.e("地图点："+bean.car.oddMileage );
//        LogUtil.e("地图点："+bean.car.oddPower );
        //需要放开注释
        String adress = "";
        try {
            adress = bean.car.getStationInfo().label;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        tvMapAddress.setText(adress);//网点地图   bean.car.getStationInfo().label
        Picasso.with(mContext).load(StringUtil.addImageHost(bean.car.carImgPath)).into(imgItemCar);  //
        tvItemCarName.setText(bean.car.getCarBrandModels().brandName + " " + bean.car.getCarBrandModels().modelNumber);//车型号
        tvItemCarType.setText("(" + Double.valueOf(bean.car.carSeatNum).intValue() + "座)");//几座
        tvItemCarMile.setText("可行驶里程：" + bean.car.oddMileage + "公里(");
        tvItemCarPower.setText(bean.car.oddPowerForNE + "%)");                     //电量
        tvItemCarNum.setText(bean.car.plateNumber);

        try {
            if (bean.borrowType.equals("0")) {//分时租车
                car_share_map_fenshizuche_txt.setText("分时租车");
            } else {
                car_share_map_fenshizuche_txt.setText("按日租车");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }


        LogUtil.e("获取到的carCode：" + carCode);
    }

    private void requestOrderState() {
        //type 0:分时,1:按日
        //网点预约租车
        String url = "api/tss/order/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", orderCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("driverType", driverType);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarOrderBean.class))
                .compose(RxNetHelper.<CarOrderBean>io_main())
                .subscribe(new ResultSubscriber<CarOrderBean>() {
                    @Override
                    public void onFail(Message<CarOrderBean> bean) {
                    }

                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarOrderBean bean) {
                    }

                });
    }


    /**
     * 根据订单状态 分别展示
     */
    private void orderVisable(int state) {
        //状态 0:预约成功 1:开始计费 2:已还车，待付费 3:订单已完成 4:订单已取消 5:订单已取消，已付费
        switch (state) {
            case -1:                                 //隐藏
                rlBookCar.setVisibility(View.GONE);
                right_btn.setVisibility(View.VISIBLE);
                llItemDriver.setVisibility(View.GONE);
                break;
            case 0:                                 //预约成功
                rlBookCar.setVisibility(View.VISIBLE);
                right_btn.setVisibility(View.GONE);
                llItemCarBook.setVisibility(View.VISIBLE);
                llItemCarUse.setVisibility(View.GONE);
                btnMapCharging.setVisibility(View.INVISIBLE);
                llCarByTime.setVisibility(View.GONE);
                llCarBook.setVisibility(View.VISIBLE);
                llCarOrder.setVisibility(View.GONE);
//                if (dongli.equals("1")) {//动力 车 隐藏鸣笛按钮
//                    LinearLayout ll_car_order_bee = (LinearLayout) llCarBook.findViewById(R.id.ll_car_bee);
//                    ll_car_order_bee.setVisibility(View.GONE);
//                }
                break;
            case 1:                                  //开始计费
                rlBookCar.setVisibility(View.VISIBLE);   //2座位5座
                right_btn.setVisibility(View.GONE);
                llItemCarBook.setVisibility(View.GONE);
                llItemCarUse.setVisibility(View.VISIBLE);//使用时长
                btnMapCharging.setVisibility(View.VISIBLE);//立即还车
                llCarByTime.setVisibility(View.VISIBLE);    //时间等
                llCarBook.setVisibility(View.GONE);
                llCarOrder.setVisibility(View.VISIBLE);
                dongli2();//如果是动力的要对应修改 状态
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
    }


    //弹toast
    private boolean dongli() {
        if (dongli.equals("1")) { //展示动力页面
            ll_dongli_layout.setVisibility(View.VISIBLE);
            dongli_navi.setVisibility(View.VISIBLE);                                     //动力  地图导航
            dongli_map_old.setVisibility(View.GONE);
            dongli_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                }
            });
            if (powerStatus.equals("1")) {//正常颜色 可以点击
                llCarOrder.setAlpha(1.0f);
                llCarOrder.setClickable(true);
                return true;
            } else {       //置空 不可点击
                llCarOrder.setAlpha(0.209f);
                ToastUtil.showToast("请先打开动力!");
                return false;
            }
        } else {
            return false;
        }

    }

    //不弹toast
    private boolean dongli2() {
        if (dongli.equals("1")) { //展示动力页面
            ll_dongli_layout.setVisibility(View.VISIBLE);
            dongli_navi.setVisibility(View.VISIBLE);                                     //动力  地图导航
            dongli_map_old.setVisibility(View.GONE);
            dongli_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                }
            });
            if (powerStatus.equals("1")) {//正常颜色 可以点击
                llCarOrder.setAlpha(1.0f);
                llCarOrder.setClickable(true);
                return true;
            } else {       //置空 不可点击
                llCarOrder.setAlpha(0.209f);
                return false;
            }
        } else {
            return false;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        map_view.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_view.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        map_view.onDestroy();
        timer.cancel();


    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        dialog = DialogHelper.loadingDialog(this, getString(R.string.dialog_map_location));
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory .fromResource(R.drawable.my_location_marker));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(android.R.color.transparent);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(100, 55, 115, 203));// 设置圆形的填充颜色
//        myLocationStyle.anchor(0.5f, 0.5f);//设置小蓝点的锚点

        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色   这两句取消原形边框
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(true);// 设置缩放按钮是否显示             
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapTouchListener(this);
        CameraPosition.Builder builder = CameraPosition.builder();
        builder.target(new LatLng(23.117055306224895, 113.2759952545166));//先移动地图到广州
        builder.zoom(16);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
        list = new ArrayList<>();
//        lv_station_list.setAdapter(adapter);
    }

    //    @NonNull
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> l = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> m = new HashMap();
            m.put("station_name", "海心沙广场" + i);
            m.put("charge_fee_per", "2.4");
            m.put("pile_fast_num_free", String.valueOf(new Random().nextInt(6) + 1));
            m.put("pile_slow_num_free", String.valueOf(new Random().nextInt(6) + 1));
            m.put("total", String.valueOf(Integer.parseInt(m.get("pile_fast_num_free").toString()) + Integer.parseInt(m.get("pile_slow_num_free").toString())));
            m.put("charge_method", String.valueOf(new Random().nextInt(3) + 1));
            m.put("charge_distance", "1.6");
            m.put("charge_address", "定位中...");
            m.put("latitude", String.valueOf(23.110000 + Math.random() * 1 / 30));
            m.put("longitude", String.valueOf(113.320000 + Math.random() * 1 / 30));
            l.add(m);
        }
        return l;
    }

    Marker locationMarker = null;//自己当前定位 图标

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            dialog.cancel();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        finishLocation = true;
        LogUtil.e("获取到的地址编码：" + aMapLocation.getAdCode());
        LogUtil.e("获取到的地址编码：" + aMapLocation.getCityCode());
        LogUtil.e("获取到的地址编码：" + aMapLocation.getProvince());

        try {
            locationMarker = null;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            aMap.getMapScreenMarkers().remove(locationMarker);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (aMapLocation.getErrorCode() == 0) {
            current_city_str=aMapLocation.getCity();
            //mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            curLat = aMapLocation.getLatitude();
            curLon = aMapLocation.getLongitude();
            String curCityCode = aMapLocation.getCityCode();

            if (locationMarker == null) {
                //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                markerOptions.visible(true);
                markerOptions.title("当前位置");
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nearby_charge));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);
                locationMarker = aMap.addMarker(markerOptions);
            } else { //已经添加过了，修改位置即可
                locationMarker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            }
            CameraPosition.Builder builder = CameraPosition.builder();
            builder.target(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));//移动地图到定位地点
            builder.zoom(16.0f);
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

            //定位后请求桩点信息，如果跟上一次定位的城市不一样，才需要请求刷新站点信息
            if (cityCode.equals("")) {
                cityCode = curCityCode;
//                    connToServer();
                requestStation();
            } else {
                if (!cityCode.equals(curCityCode)) {
//                        connToServer();
                    requestStation();
                    cityCode = curCityCode;
                }
            }
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            LogUtil.e(errText);
            if (NetworkUtils.isConnected(this)) {
                if (aMapLocation.getErrorCode() == 12) {
                    ToastUtil.showToast(R.string.toast_permission_location);
                } else {
                    ToastUtil.showToast(getString(R.string.toast_map_location_failed) + aMapLocation.getErrorInfo());
                }
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this); //设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置为高精度定位模式
            mlocationClient.setLocationOption(mLocationOption); //设置定位参数
            mLocationOption.setOnceLocation(true);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }


//        AMapLocationClient    mLocationClient = new AMapLocationClient(getApplicationContext());
//        //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
//        mLocationClient.setLocationListener(this);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setNeedAddress(true);  //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setOnceLocation(false);  //设置是否只定位一次,默认为false
//        mLocationOption.setWifiActiveScan(true); //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setMockEnable(false);   //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setInterval(2000);    //设置定位间隔,单位毫秒,默认为2000ms
//        mlocationClient.setLocationOption(mLocationOption);     //给定位客户端对象设置定位参数
//        mlocationClient.startLocation(); //启动定位


        mlocationClient.startLocation();
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


    private void setMarkerToMap(List<PositionBean> l) {
        aMap.clear(true);
        markerList.clear();
        for (int i = 0; i < l.size(); i++) {
            PositionBean m = l.get(i);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(Double.valueOf(m.lat), Double.valueOf(m.lng)));
//            markerOption.draggable(true);
            markerOption.icon(getBitmapDscriptor(m.ablekingLot + ""));
//            markerOption.period(20);
            Marker marker = aMap.addMarker(markerOption);
            marker.setObject(m);
            markerList.add(marker);
        }
    }

    private BitmapDescriptor getBitmapDscriptor(String count) {
        return BitmapDescriptorFactory.fromView(getMarkerView(count));
    }

    private View getMarkerView(String count) {
        View view = View.inflate(this, R.layout.marker_cs_item, null);
        TextView tv_marker_num = (TextView) view.findViewById(R.id.tv_marker_num);
        tv_marker_num.setText(count);
        return view;
    }

    private View getMarkerViewBig(String type, String count) {
        View view = View.inflate(this, R.layout.marker_item_big, null);
        ImageView iv_marker_icon = (ImageView) view.findViewById(R.id.iv_marker_icon);
        if (type.equals(TYPE_FAST)) {
            iv_marker_icon.setImageResource(R.mipmap.ic_marker_fast);
        } else if (type.equals(TYPE_SLOW)) {
            iv_marker_icon.setImageResource(R.mipmap.ic_marker_slow);
        } else {
            iv_marker_icon.setImageResource(R.mipmap.ic_marker_mix);
        }
        TextView tv_marker_num = (TextView) view.findViewById(R.id.tv_marker_num);
        tv_marker_num.setText(count);
        return view;
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (rCode == 1000) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                    && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
//                tv_map_address.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
                float dis_m = AMapUtils.calculateLineDistance(new LatLng(curLat, curLon), new LatLng(markerLat, markerLon)) / 1000;
                DecimalFormat format = new DecimalFormat("##0.0");
//                tv_map_distance.setText(format.format(dis_m) + "km");
            } else {
                ToastUtil.showToast(R.string.toast_map_regeocode_no_data);
            }
        } else {
            ToastUtil.showToast(R.string.toast_map_regeocode_no_data);
            LogUtil.e("获取地址失败，错误码：" + String.valueOf(rCode));
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }


    //mark点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        refreshFlag = false;
//        marker.setZIndex(100);
        openMarker(marker);
        curMarker = marker;
        curMarkerData = (PositionBean) marker.getObject();
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 25), 500l, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
        markerLat = marker.getPosition().latitude;
        markerLon = marker.getPosition().longitude;
//        carSlidePopupHelper.setNewData(0);
//        carSlidePopupHelper.show();
//        drawCalculateLine(new NaviLatLng(markerLat,markerLon));
//        getAddress(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
//        showPopWindow((PositionBean) marker.getObject());
//        mAdapter.setNewData(3);
        return true;
    }

    private void openMarker(Marker marker) {
        PositionBean bean = (PositionBean) (marker.getObject());
        int count = bean.ablekingLot;
        marker.setIcon(getBitmapDscriptor(count + ""));
        if (!TextUtils.isEmpty(bean.label)) {
            carSlidePopupHelper.setStationName(bean.label);
        }
//        if (!TextUtils.isEmpty(bean.stationCode)) {
        requestCars(bean.stationCode);
//        }
    }


    private void closeMarker(Marker marker) {
        PositionBean bean = (PositionBean) (marker.getObject());
        marker.setIcon(getBitmapDscriptor(bean.ablekingLot + ""));
    }

    private void intoDetail() {
        Intent intent = new Intent(CarShareMapActivity2.this, CarShareFindCarActivity.class);
        intent.putExtra("station_id", station_id);
        intent.putExtra("station_name", station_name);
        startActivityForResult(intent, ConstantCode.REQ_OPEN_STATION_INFO);
    }


    private void onPopViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_map_charging:
                LogUtil.e("点击c");
//                Intent intent1 = new Intent(this, CarShareConfirmActivity.class);
//                intent1.putExtra("carCode",carCode);
//                startActivity(intent1);
                break;
            case R.id.iv_collection:
                break;
        }
    }

    /**
     * 根据终点的经纬度绘制当前位置到终点的路线
     */
    private void drawCalculateLine(NaviLatLng endLat) {
        NaviLatLng startLat = new NaviLatLng(curLat, curLon);
        startList.clear();
        startList.add(startLat);
        endList.clear();
        endList.add(endLat);
        wayList.clear();
        mAMapNavi.calculateDriveRoute(startList, endList, wayList, PathPlanningStrategy.DRIVING_SHORT_DISTANCE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantCode.REQ_FILTER && resultCode == ConstantCode.RES_FILTER) {
            LogUtil.e(data.getSerializableExtra("filter").toString());
            HashMap<String, String> map = (HashMap<String, String>) data.getSerializableExtra("filter");
            charge_interface = map.get("charge_interface");
            charge_carr = map.get("charge_carr");
            charge_method = map.get("charge_method");
            charge_pile_bel = map.get("charge_pile_bel");
//            pile_state = map.get("pile_state");
//            charging_gun = map.get("charging_gun");
            parking_free = map.get("parking_free");
            service_time = map.get("service_time");

            for (Map.Entry<String, String> entry1 : filterMap.entrySet()) {
                String m1value = entry1.getValue() == null ? "" : entry1.getValue();
                String m2value = map.get(entry1.getKey()) == null ? "" : map.get(entry1.getKey());
                if (!m1value.equals(m2value)) {//若两个map中相同key对应的value不相等 ,筛选条件变更重新请求
                    filterMap.clear();
                    filterMap = map;
                    requestStation();
                    break;
                }
            }
        } else if (requestCode == ConstantCode.REQ_OPEN_STATION_INFO && resultCode == ConstantCode.RES_OPEN_STATION_INFO && data != null) {
            if (curMarker != null) {
                PositionBean m = (PositionBean) curMarker.getObject();
                boolean is_favor = data.getBooleanExtra("is_favor", false);
//                m.setIs_favor(is_favor ? "0" : "1");
                curMarker.setObject(m);
            }
        }    else if (requestCode == ConstantCode.SELECT_CITY) {

//			aMap.setLocationSource(mListener);// 设置定位监听
            LogUtils.d("获取到的城市---:" + data);
            try {
                LogUtils.d("获取到的城市未:" + data.getStringExtra("city"));
            } catch (Throwable t) {
                t.printStackTrace();
            }//防止异常
            String str = "";
            try {
                str = data.getStringExtra("city");
                Log.e("aaa", str + ".....................");
            } catch (Throwable t) {
                t.printStackTrace();
                str = "";
            }
            if (!str.equals("")) {
                current_city_str = str;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("aaaa", "current_city_str:" + current_city_str);
                        tx_change_address.setText(current_city_str);//更新当前城市名
                    }
                });
                GeocodeSearch geocodeSearch = new GeocodeSearch(this);//构造 GeocodeSearch 对象，并设置监听。
                geocodeSearch.setOnGeocodeSearchListener(this);
                //通过GeocodeQuery设置查询参数,调用getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求。
                //address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
                GeocodeQuery query = new GeocodeQuery(current_city_str, str);//str 为城市  南京 北京
                geocoderSearch.getFromLocationNameAsyn(query);
            }
        }
    }

    private LatLngBounds getRange(CameraPosition cameraPosition) {
        Projection projection = aMap.getProjection();
        return projection.getMapBounds(cameraPosition.target, cameraPosition.zoom);
    }

    /**
     * 取车
     */
    private void requestGetCar() {
        String url = "api/tss/car/borrow";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("取车成功");
                        requestDoing();
//                        requestOrderState();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }

                });
    }

    /**
     * 关动力
     */
    private void requestCloseDongli() {
        String url = "api/tss/car/power/off";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("关闭动力成功");
                        powerStatus = "0";
                        dongli2();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    /**
     * 开动力
     */
    private void requestOpenDongli() {
        String url = "api/tss/car/power/on";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("开启动力成功");
                        powerStatus = "1";
                        dongli();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    /**
     * 关门
     */
    private void requestCloseDoor() {
        String url = "api/tss/car/door/lock";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("关门成功");
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }
                });
    }

    /**
     * 立即用车  调用  附近车辆
     */
    private void requestNearStation() {
        String url = "api/station/car/nearly/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("lat", curLat - Config.OFF_LNG);
        map.put("lng", curLon - Config.OFF_LAT);
        map.put("distance", "600");//600公里 内部

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarNearBean.class))
                .compose(RxNetHelper.<CarNearBean>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<CarNearBean>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarNearBean bean) {
                        boolean isMatchStation = false;
                        for (Marker marker : markerList) {
                            LatLng latLng = marker.getPosition();
                            double latt = Double.parseDouble(bean.lat) + Config.OFF_LAT;
                            double lngg = Double.parseDouble(bean.lng) + Config.OFF_LNG;
                            LatLng latLng1 = new LatLng(latt, lngg);//Double.valueOf(latt), Double.valueOf()
                            if (latLng.equals(latLng1)) {
                                onMarkerClick(marker);//模拟点击桩子按钮
                                isMatchStation = true;
                            }
                        }
                        if (!isMatchStation) {
                            ToastUtil.showToast("暂无匹配的站点");
                        }
                    }

                    @Override
                    public void onFail(Message<CarNearBean> bean) {
                        LogUtil.e("立即用车 失败 ：" + bean.msg + ",code:" + bean.code);

                    }

                });

    }

    /**
     * 开门
     */
    private void requestOpenDoor() {
        String url = "api/tss/car/door/unlock";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("打开车门成功");
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }
                });
    }

    /**
     * 鸣笛
     */
    private void requestBee() {
        String url = "api/tss/car/search";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);

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
                        ToastUtil.showToast("鸣笛成功");
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }

                });
    }

    /**
     * 取消预约
     */
    private void requestCancle() {
        String url = "api/tss/order/cancel";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("orderCode", orderCode);
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());

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
                        ToastUtil.showToast("取消预约成功");
                        orderVisable(-1);
                        clearPreference();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }
                });
    }

    private void clearPreference() {
        PrefUtil.saveString(mContext, PrefUtil.ORDER_CODE, "");
//        PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, "");
    }

    /**
     * 点击具体  车辆之后
     */
    private void requestCars(String stationCode) {
        String url = "api/station/car/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("stationCode", stationCode);
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatListFunction<>(CarBean.class))
                .compose(RxNetHelper.<List<CarBean>>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<List<CarBean>>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(List<CarBean> bean) {
                        carSlidePopupHelper.setNewData2(bean);//设置pop的信息
                        try {
                            SPUtil.put(mContext, "carCode", bean.get(0).carCode);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }//保存carcode 确认用车界面
                        for (CarBean bean1 : bean) {
                            tvItemCarName.setText(bean1.getCarBrandModels().brandName + " " + bean1.getCarBrandModels().modelNumber);//车名
                            tvItemCarType.setText("(" + Double.valueOf(bean1.carSeatNum).intValue() + "座)");
                            tvItemCarMile.setText("可行驶里程：" + bean1.oddMileage + "公里(");
                            tvItemCarPower.setText(bean1.oddPowerForNE + "%)");
                            tvItemCarNum.setText(bean1.plateNumber);
                        }
                    }

                    @Override
                    public void onFail(Message<List<CarBean>> bean) {
                    }
                });
    }

    private void requestStation() {
        String url = "api/station/nearly/query";
        HashMap<String, Object> requestMap = new HashMap<>();
//        requestMap.put("lng", curLon);
//        requestMap.put("lat", curLat);
        requestMap.put("lat", curLat - Config.OFF_LAT);
        requestMap.put("lng", curLon - Config.OFF_LNG);
        requestMap.put("distance", "600");
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatListFunction<>(PositionBean.class))
                .compose(RxNetHelper.<List<PositionBean>>io_main())
                .subscribe(new ResultSubscriber<List<PositionBean>>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(List<PositionBean> bean) {
                        aMap.clear(true);
                        markerList.clear();
                        list.clear();
                        int count = bean.size();
                        if (count == 0) {
                            DialogHelper.alertDialog(CarShareMapActivity2.this, getString(R.string.dialog_D101_no_data));
                        } else {
                            for (int a = 0; a < count; a++) {
                                double latt = Double.parseDouble(bean.get(a).lat) + Config.OFF_LAT;
                                double lngg = Double.parseDouble(bean.get(a).lng) + Config.OFF_LNG;
                                bean.get(a).lat = latt + "";
                                bean.get(a).lng = lngg + "";
                            }
                            list.addAll(bean);
                            setMarkerToMap(list);
                            for (PositionBean bean1 : list) {
                                Log.e("CarShareMapActivity", bean1.stationName);
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<List<PositionBean>> bean) {
                    }

                });
    }

    private void requestReturnCar() {
        String url = "api/station/nearly/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("lng", curLon);
        requestMap.put("lat", curLat);
        requestMap.put("distance", "600");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatListFunction<>(PositionBean.class))
                .compose(RxNetHelper.<List<PositionBean>>io_main())
                .subscribe(new ResultSubscriber<List<PositionBean>>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(List<PositionBean> bean) {
                        aMap.clear(true);
                        markerList.clear();
                        list.clear();
                        int count = bean.size();
                        if (count == 0) {
                            DialogHelper.alertDialog(CarShareMapActivity2.this, getString(R.string.dialog_D101_no_data));
                        } else {
                            list.addAll(bean);
                            for (int a = 0; a < count; a++) {
                                double latt = Double.parseDouble(list.get(a).lat) + Config.OFF_LAT;
                                double lngg = Double.parseDouble(list.get(a).lng) + Config.OFF_LNG;
                                list.get(a).lat = latt + "";
                                list.get(a).lng = lngg + "";
                            }
                            setMarkerToMap(list);//
                        }
                    }

                    @Override
                    public void onFail(Message<List<PositionBean>> bean) {
                    }
                });
    }


    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.e("onTouch:ACTION_DOWN");
            isTouch = true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            LogUtil.e("onTouch:ACTION_UP");
            isTouch = false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
//            lp = (RelativeLayout.LayoutParams) rl_list_frame.getLayoutParams();
//            lp.bottomMargin = layout_map.getHeight();
////            LogUtils.e("height:" + layout_map.getHeight());
////            rl_list_frame.setLayoutParams(lp);
//            isFirst = false;
//            rl_list_frame.setTranslationY(0 - layout_map.getHeight());
            isFirst = false;
        }
    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    //------------------------------AMapNaviListener,导航回调接口---------------------------------------
//    @Override public void onCalculateRouteSuccess() {//计算路线成功回调
//        routeOverlays.clear();//清空上次计算的路径列表。
//        AMapNaviPath path = mAMapNavi.getNaviPath();
//        drawRoutes(0, path);
//    }
    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        routeOverlays.clear();//清空上次计算的路径列表。
        AMapNaviPath path = mAMapNavi.getNaviPath();
        AMapNavi mAMapNavi = null;
        mAMapNavi = AMapNavi.getInstance(this);
        mAMapNavi.setUseInnerVoice(true);
        drawRoutes(0, path);


    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
//        calculateSuccess = true;
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(aMap, path, this);
        routeOverLay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker));
        routeOverLay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker));
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);

        int routeID = routeOverlays.keyAt(routeId);
//        //突出选择的那条路
//        for (int i = 0; i < routeOverlays.size(); i++) {
//            int key = routeOverlays.keyAt(i);
//            routeOverlays.get(key).setTransparency(0.4f);
//        }
        routeOverlays.get(routeID).setTransparency(0.4f);//设置透明度
//        routeOverlays.get(routeID).setZindex(zindex++);/**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        mAMapNavi.selectRouteId(routeID); //必须告诉AMapNavi 你最后选择的哪条路
//        Toast.makeText(this, "路线标签:" + mAMapNavi.getNaviPath().getLabels(), Toast.LENGTH_SHORT).show();
//        routeIndex++;
//        chooseRouteSuccess = true;

//        /**选完路径后判断路线是否是限行路线**/
//        AMapRestrictionInfo info = mAMapNavi.getNaviPath().getRestrictionInfo();
//        if (!TextUtils.isEmpty(info.getRestrictionTitle())) {
//            Toast.makeText(this, info.getRestrictionTitle(), Toast.LENGTH_SHORT).show();
//        }
    }

    private void selectColor(int position){
        switch (position){
            case 0:
                tx_all_car.setTextColor(getResources().getColor(R.color.new_primary));
                tx_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                tx_best_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                break;
            case 1:
                tx_all_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                tx_car.setTextColor(getResources().getColor(R.color.new_primary));
                tx_best_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                break;
            case 2:
                tx_all_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                tx_car.setTextColor(getResources().getColor(R.color.selector_text_color_primary));
                tx_best_car.setTextColor(getResources().getColor(R.color.new_primary));
                break;

        }
    }

    /**
     * 选择城市 按钮事件
     */
    private void selectCity() {
        LogUtils.i("点击城市选择按钮");
        Intent intent = new Intent(CarShareMapActivity2.this, Activity_SelectCity.class);//跳转城市选择页面  接收返回的结果
        startActivityForResult(intent, ConstantCode.SELECT_CITY);
    }

    // //R.id.ll_car_bee, 需求改动  禁止
    @OnClick({R.id.img_location, R.id.img_use_car, R.id.img_navi, R.id.ll_car_cancel_book, R.id.ll_car_get, R.id.btn_map_charging,
            R.id.ll_car_order_bee, R.id.ll_car_order_open, R.id.ll_car_order_close, R.id.ll_car_order_back, R.id.img_car_driver_message, R.id.img_car_driver_call,
            R.id.money_gengduo, R.id.ll_car_order_open2_dongli_b, R.id.ll_car_order_close2_dongli_b, R.id.dongli_navi,R.id.tx_all_car,R.id.tx_car,R.id.tx_best_car,R.id.tx_change_address})
//            ,R.id.confirm_car_type_zijia_fenshizuche_gengduo,R.id.confirm_car_type_zijia_fenshizulin_gengduo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_all_car:       //所有车辆
                selectColor(0);
                break;
            case R.id.tx_car:           //一般车辆
                selectColor(1);
                break;
            case R.id.tx_best_car:       //优等车
                selectColor(2);
                break;
            case R.id.tx_change_address:     //切换城市
                selectCity();//测试移动地图    移动城市无法选择
                break;
            case R.id.img_location://定位
//                if (mlocationClient != null && finishLocation) {
//                    dialog.show();
//                    finishLocation = false;
//                    mlocationClient.startLocation();
//                }
                aMap.setMyLocationEnabled(true);        //定位一次 ,1秒后取消
                x.task().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(curLat, curLon)));
                        aMap.setMyLocationEnabled(false);
                    }
                }, 1500);

//                Intent intent2 = new Intent(this, CarShareConfirmActivity.class);
//                startActivity(intent2);
                break;
            case R.id.img_use_car://立即用车
//                if (UserParams.INSTANCE.checkLogin(this)) {   //检查登陆情况  每个用户一个key
                NaviLatLng endLat = new NaviLatLng(curLat + 0.022226, curLon + 0.022226);
                requestNearStation();
                break;//当前的范围内   //drawCalculateLine(endLat);
//                }
            case R.id.img_navi:   //预约中页面
                EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                break;
            case R.id.ll_car_cancel_book://取消预定
                cancle_yuyue();
                break;
            case R.id.ll_car_get://取车
                PhotoDialogHelper.alertPhotoDialog(mContext, "充电枪已拔出", "取消",
                        "开车前请确保充电枪已拔出", "取车后将开始计费", R.mipmap.cs_car_inject, new AlertPhotoDialog.OnDialogButtonClickListener() {
                            @Override
                            public void onConfirm() {
                                requestGetCar();
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                break;
//            case R.id.ll_car_bee:
//                if (dongli.equals("1")) {
//                    if (dongli()) {
//                        requestBee();
//                    } //可以点击
//                } else {
//                    requestBee();
//                }
//
//
//                break;  //取车成功后，租车进行时

            case R.id.btn_map_charging:
                LogUtil.e("点击立即还车用车");
                ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);
                break;//立即还车 //requestReturnCar();


            case R.id.ll_car_order_bee:
                if (dongli.equals("1")) {
                    if (dongli()) {
                        requestBee();
                    } //可以点击
                } else {
                    requestBee();
                }


                break;//鸣笛找车
            case R.id.ll_car_order_open://反馈问题

                if (dongli.equals("1")) {
                    if (!dongli()) {
                        return;
                    } //可以点击
                }
                PhotoDialogHelper.alertTwoConfirmDialog(mContext, "车况完好，立即开门",
                        "发现问题，立即报备", "开车前，请环绕车身检查外观、轮胎、反光镜和周围路况。", R.mipmap.cs_car_yellow
                        , new AlertPhotoDialog.OnDialogButtonClickListener() {
                            @Override
                            public void onConfirm() {
                                requestOpenDoor();
                            }

                            @Override
                            public void onCancel() {
                                ActivityUtil.startActivity(mContext, CarShareReportBadActivity.class);
                            }//取消报备
                        });
                break;
            case R.id.ll_car_order_close:

                if (dongli.equals("1")) {
                    if (dongli()) {
                        requestCloseDoor();
                    } //可以点击
                } else {
                    requestCloseDoor();
                }


                break;//锁门
            case R.id.ll_car_order_back:
                if (dongli.equals("1")) {
                    if (dongli()) {
                        ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);
                    } //可以点击
                } else {
                    ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);
                }

//                ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);

                break;//推荐还车点  //requestReturnCar();
            case R.id.img_car_driver_message: //给司机发短信
                if (TextUtils.isEmpty(driverPhone)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }
                DialogHelper.confirmDialog(mContext, "是否给 " + driverPhone + "发短信？", new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PermissionUtil.checkPermission(CarShareMapActivity2.this, ConstantCode.SEND_SMS, Manifest.permission.SEND_SMS); //检查权限
                        }
                        try {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + driverPhone));
                            intent.putExtra("sms_body", "");
                            startActivity(intent);
                        } catch (Exception e) {
                            ToastUtil.showToast("发送短信权限受限，请在权限设置中打开该权限");
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            case R.id.img_car_driver_call://代驾，进行呼叫电话
                if (TextUtils.isEmpty(driverPhone)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }
                DialogHelper.confirmDialog(mContext, "是否呼叫 " + driverPhone + "？", new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PermissionUtil.checkPermission(CarShareMapActivity2.this, ConstantCode.CALL_PHONE, Manifest.permission.CALL_PHONE);
                        }
                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + driverPhone));
                            startActivity(i);
                        } catch (Exception e) {
                            ToastUtil.showToast("拨打电话权限受限，请在权限设置中打开该权限");
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            case R.id.money_gengduo://立即还车时候的更多
                if (ismoneygengduo) {
                    Picasso.with(this).load(R.mipmap.gengduo2).fit().noFade().centerInside().into(money_gengduo);//还需要添加数据 这里直接更新图
                    ismoneygengduo = !ismoneygengduo;
                    ll_item_car_use_detail.setVisibility(View.VISIBLE);
                } else {
                    Picasso.with(this).load(R.mipmap.gengduo1).fit().noFade().centerInside().into(money_gengduo);
                    ismoneygengduo = !ismoneygengduo;
                    ll_item_car_use_detail.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_car_order_open2_dongli_b:
                requestOpenDongli();
                break;
            case R.id.ll_car_order_close2_dongli_b:
                requestCloseDongli();
                break;
            case R.id.dongli_navi:
                EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                break;  //动力导航
            default:
                break;
        }
    }


    //取消预约 最大次数
    public void cancle_yuyue() {
        String url = "api/dic/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("dicKey", "KEY_OF_MAX_CANCELTIMES_FULLDAY");
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Object bean) {
                        LogUtil.e("获取成功：" + bean.toString());
                        String dicVallue = "";
                        String result = bean.toString();
                        String tmp = "";
                        tmp = result.substring(result.indexOf("dicValue"), result.indexOf("dicValue") + 25);
                        LogUtil.e("获取成功后1 " + tmp);
                        tmp = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(","));
                        LogUtil.e("获取成功后2 " + tmp);
                        final String tmp_t = tmp;
                        if (tmp_t.equals("")) {
                            x.task().autoPost(new Runnable() {
                                @Override
                                public void run() {
                                    DialogHelper.confirmDialog(CarShareMapActivity2.this, "每天最多可以取消2次,每次取消需要间隔1小时，确定取消么？", new AlertDialog.OnDialogButtonClickListener() {
                                        @Override
                                        public void onConfirm() {
                                            LogUtil.e("进入按钮 取消订单-----'");
                                            requestCancle();
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                                }
                            });
                        } else {
                            x.task().autoPost(new Runnable() {
                                @Override
                                public void run() {
                                    DialogHelper.confirmDialog(CarShareMapActivity2.this, "每天最多可以取消" + tmp_t + "次,每次取消需要间隔1小时，确定取消么？", new AlertDialog.OnDialogButtonClickListener() {
                                        @Override
                                        public void onConfirm() {
                                            LogUtil.e("进入按钮2 取消订单-----'");
                                            requestCancle();
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        LogUtil.e("获取失败：" + bean.toString());
                    }
                });
    }

    private void userXieyi() {
        String istongyi = (String) SPUtil.get(this, "奇速共享汽车使用协议", "false");
        if (!istongyi.equals("true")) {
            startActivity(new Intent(this, Activity_CarshareWeb.class));
        }
    }
}
