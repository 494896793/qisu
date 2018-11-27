package www.qisu666.com.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
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
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
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
import butterknife.Optional;
import cn.iwgang.countdownview.CountdownView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.CarShareMapPreActivity;
import www.qisu666.com.carshare.CarSlidePopupHelper;
import www.qisu666.com.carshare.CarSlidePopupHelper2;
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
import www.qisu666.com.event.CarOrderLationEvent;
import www.qisu666.com.event.FinishActivityEvent;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.model.CarBeanNew;
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
import www.qisu666.sdk.amap.carShare.bean.CarInfo;
import www.qisu666.sdk.amap.carShare.bean.CarNear;
import www.qisu666.sdk.amap.stationMap.juhe.PointAggregationAty;
import www.qisu666.sdk.carshare.Activity_CarshareWeb;
import www.qisu666.sdk.carshare.bean.CarNear_LY;
import www.qisu666.sdk.carshare.juhe.MarkerImageView;
import www.qisu666.sdk.carshare.juhe.ScreenUtils;

//立即用车页面  共享汽车
public class CarShareMapActivity extends CarShareMapPreActivity implements AMapNaviListener, AMap.OnCameraChangeListener {

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
    @BindView(R.id.carinfo_right_info)
    LinearLayout carinfo_right_info;
    @BindView(R.id.carinfo_left_layout)
    LinearLayout carinfo_left_layout;
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
    @Nullable
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
    @BindView(R.id.img)
    ImageView img;
//    @BindView(R.id.rb_qiehuan)
//    RadioButton rb_qiehuan;

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
    @BindView(R.id.img_navi)
    ImageView img_navi;
//    @BindView(R.id.img_navi2)
//    ImageView img_navi2;     //导航2号 预约中

    @BindView(R.id.money_gengduo)
    ImageView money_gengduo;//还车dialog界面的更多
    @BindView(R.id.ll_item_car_use_detail)
    LinearLayout ll_item_car_use_detail;//隐藏dialog 计费粗略的布局
    @BindView(R.id.navi2_layout)
    LinearLayout navi2_layout;
    @BindView(R.id.tx_all_car)
    TextView tx_all_car;
    @BindView(R.id.tx_car)
    TextView tx_car;
    @BindView(R.id.tx_best_car)
    TextView tx_best_car;
    @BindView(R.id.tx_change_address)
    TextView tx_change_address;


    @BindView(R.id.car_share_map_fenshizuche_txt)
    TextView car_share_map_fenshizuche_txt;//分时租车 和按日租车的显示问题

//    @BindView(R.id.TextView_Normal_Dark_Gray_MarginLeft) TextView TextView_Normal_Dark_Gray_MarginLeft;//分时租车

    LatLonPoint afterGeoPoint;     //geo编码后的点

    private String distances=""; //距我多远

    /**
     * 2快充  1快充  3混合
     */
    public static final String TYPE_FAST = "2", TYPE_SLOW = "1", TYPE_MIX = "3";
    /**
     * 倒计时持续时间
     */
    private final int TIME_LAST = 30 * 60 * 1000;
    /**
     * 查询订单的实时费用
     */
    private final String TIME_SECOND = "timeSecond";

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch;

    private Marker nowClickMarker;  //当前点击的marker

    @BindView(R.id.p_station_window)
    LinearLayout p_station_window;
    /**
     * bootType  0常规  1一键启动  2-北汽
     */
    String dongli = "0";

    //    private List<Map<String, Object>> list;
    private List<PositionBean> list;

    private boolean isShowList = false;

    private Boolean isFirstLoc = true;

    //当前城市str
    private String current_city_str = "";
    //当前城市
    private TextView current_city;//当前城市

    /**
     * 动力关闭默认0
     */
    String powerStatus = "0";
    private Animator mLeftInSet;
    /**
     * 还车 新增 判断ui是否显示 更多
     */
    boolean ismoneygengduo = true;

    private AMap aMap;
    /**
     * 新增立即订车布局
     */
    CarSlidePopupHelper2 carSlidePopupHelper2;
    private double longitude_start, longitude_end, latitude_start, latitude_end;
    private boolean refreshFlag = true;
    /**
     * 网点列表
     */
    List<PositionBean> list_wangdian = new ArrayList<>();
    /**
     * 汽车列表
     */
    List<PositionBean> list_car = new ArrayList<>();
    // 自己当前定位 图标
    Marker locationMarker = null;
    int isLocationMarker = 0;
    boolean clickedDaohang = false;
    private boolean isFirst = true;
    /**
     * 动画
     */
    private Animator mRightOutSet;
    /**
     * curLat / curLon  定位的经纬度    markLat / markLon 标志点的经纬度
     */
    private double curLat, curLon, markerLat, markerLon;
    /**
     * 当前缩放级别
     */
    private float curZoom;
    /**
     * 定位城市code
     */
    private String area_code = "5810";
    /**
     * 城市编码
     */
    private String cityCode = "";
    /**
     * 选中的站点id
     */
    private String station_id;
    /**
     * 选中的站点名称
     */
    private String station_name;
    /**
     * 桩点列表列表的布局参数
     */
    private RelativeLayout.LayoutParams lp;
    /**
     * 筛选条件  充电接口, ,充电方式, ,停车费,服务时间
     */
    private String charge_interface, charge_carr, charge_method, charge_pile_bel, parking_free, service_time;
    /**
     * 是否已经定位完毕
     */
    private boolean finishLocation = false;
    /**
     * 手指是否触摸地图
     */
    private boolean isTouch = false;
    /**
     * 当前站点是否被收藏
     */
    private boolean favorFlag = false;
    /**
     * 当前点击marker的数据
     */
    private PositionBean curMarkerData;
    /**
     * 当前点击的marker
     */
    private Marker curMarker;
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 113.355642);
    boolean isyuyue = false;
    boolean location_no = false;
    String distance = "";
    Long lasttime = 0L;
    private RouteOverLay routeOverLay;
    private Context mContext;
    private String testLng = "114.1144306149";
    private String testLat = "22.5640882213";
    List<Marker> markerList = new ArrayList<>();
    /**
     * 当前筛选条件集合
     */
    private Map<String, String> filterMap;
    /**
     * 请求定位的Dialog
     */
    private LoadingDialog dialog;
    /**
     * 车辆列表的适配器
     */
//    private CarItemPagerAdapter mAdapter;
    /**
     * 开始点坐标集合［建议就一个开始点］
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 信息点的 popu  弹出立即订车
     */
    private MapPopupHelper mapPopupHelper;
    /**
     * 立即订车的 pager5个
     */
    private CarSlidePopupHelper carSlidePopupHelper;
    private final Timer timer = new Timer();
    private int counter = 0;
    /**
     * 导航
     */
    private AMapNavi mAMapNavi;
    /**
     * 车辆编码
     */
    private String carCode;
    /**
     * 订单编码
     */
    private String orderCode;
    /**
     * 司机类型
     */
    private String driverType;
    /**
     * 是否正在用车
     */
    private boolean isUsingCar = false;
    /**
     * 司机电话
     */
    private String driverPhone;
    /**
     * 新增聚合 屏幕高度(px),屏幕宽度(px)
     */
    private int screenHeight;
    private int screenWidth;
    /**
     * 所有的marker
     */
    private ArrayList<MarkerOptions> markerOptionsListall = new ArrayList<MarkerOptions>();
    /**
     * 视野内的marker
     */
    private ArrayList<MarkerOptions> markerOptionsListInView = new ArrayList<MarkerOptions>();

    // 添加临时数据
    private void addDate(double latitude, double longitude, String str) {

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOptions.title(str);
        markerOptionsListall.add(markerOptions);
    }


    //            addDate(lat, lng,
//            infoList.get(i).getTotal_pile_count()+ "," +
//            infoList.get(i).getCharge_station_type() + "." +
//            infoList.get(i).getStation_id()+"-"+
//            (Double.valueOf(infoList.get(i).pile_fast_num_free)+
//            Double.valueOf(infoList.get(i).getPile_slow_num_free())));//桩子总数,类型，station id
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (isUsingCar) {
                //如果正在用车  发送event事件
                EventBus.getDefault().post(new CarMapEvent(TIME_SECOND));
            }
        }
    };


    private void setPreferenece() {
        carCode = PrefUtil.getString(mContext, PrefUtil.CAR_CODE);
        orderCode = PrefUtil.getString(mContext, PrefUtil.ORDER_CODE);
        driverType = PrefUtil.getString(mContext, PrefUtil.DRIVER_TYPE);
    }

    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

//    private void setUpMapNavi() {
//        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
//        mAMapNavi.addAMapNaviListener(this);
//    }
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    /**
     * 前往充电等 dialog
     */
    private PopupHelper popupHelper;

    private void initMap(Bundle savedInstanceState) {
        map_view = (MapView) findViewById(R.id.map_view);
        map_view.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map_view.getMap();
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.getUiSettings().setRotateGesturesEnabled(true);
        setUpMap();
        // setUpMapNavi();
    }


    private void initTitleBar() {
        title.setText("共享汽车");
        carinfo_left_layout.getBackground().setAlpha(250);
        carinfo_right_info.getBackground().setAlpha(250);
        llCarOrder.getBackground().setAlpha(250);
        llItemCarUse.getBackground().setAlpha(250);
        dongli_map_old.getBackground().setAlpha(250);
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                testCar();
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarShareMapActivity.this, NearbyActivity.class));
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

    private String pop_stationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_map);
        mContext = this;
        // 注册 EventBus
        EventBus.getDefault().register(this);
        // 初始 view
        initView();
        // initFilter();
        // 初始化 SharedPreference
        setPreferenece();
        // 初始化高德
        initMap(savedInstanceState);
        initJuhe();

        /*synchronized (this){
            requestDoing();
            list_wangdian.clear();
            list_car.clear();
            markerOptionsListall=new ArrayList<>();
            // 查询附近网点车辆
            Log.e("asd", "onNewIntent");
            requestStation();
        }*/
//        setAnimators();
//        setCameraDistance();
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

    // 聚合桩点初始化
    private void initJuhe() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);
    }

    /**
     * 获取视野内的marker 根据聚合算法合成自定义的marker 显示视野内的marker
     */
    private void resetMarks() {
        synchronized (this) {

            // 开始刷新
            Projection projection = aMap.getProjection();
            Point p = null;
            markerOptionsListInView.clear();
            for (MarkerOptions mp : markerOptionsListall) {
                // 获取在当前视野内的marker;提高效率
                // 计算屏幕中点坐标
                p = projection.toScreenLocation(mp.getPosition());
                if (p.x < 0 || p.y < 0 || p.x > screenWidth || p.y > screenHeight) {
                    // 屏幕外
                } else {
                    //加入计算
                    markerOptionsListInView.add(mp);
                }
            }

            //自定义的聚合类MyMarkerCluster
            ArrayList<MarkerImageView> clustersMarker = new ArrayList<MarkerImageView>();
            for (MarkerOptions mp : markerOptionsListInView) {
                if (clustersMarker.size() == 0) {
                    // 添加一个新的自定义marker
                    // 80=相距多少才聚合
                    clustersMarker.add(new MarkerImageView(CarShareMapActivity.this, mp, projection, 120, 1));
                } else {
                    boolean isIn = false;
                    for (MarkerImageView cluster : clustersMarker) {
                        if (cluster.getBounds().contains(mp.getPosition())) {
                            // 判断当前的marker是否在前面marker的聚合范围内 并且每个marker只会聚合一次。
                            cluster.addMarker(mp);
                            isIn = true;
                            break;
                        }
                    }

                    if (!isIn) {
                        // 如果没在任何范围内，自己单独形成一个自定义marker。在和后面的marker进行比较
                        clustersMarker.add(new MarkerImageView(CarShareMapActivity.this, mp, projection, 120, clustersMarker.size()));// 80=相距多少才聚合
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
        }

//        EventBus.getDefault().post("我的位置");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        requestDoing();
        // 查询附近网点车辆
        Log.e("asd", "onNewIntent");
        list_wangdian.clear();
        list_car.clear();
        markerOptionsListall = new ArrayList<>();
        requestStation();
//        getDistance();
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
////                        ToastUtil.showToast("All");
//                        break;
//                    case R.id.rb_two_site_car:
////                        ToastUtil.showToast("Two");
//                        break;
//                    case R.id.rb_five_site_car:
////                        ToastUtil.showToast("Five");
//                        break;
//                    default:
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
        // 开始 1 second 的定时任务
        timer.scheduleAtFixedRate(task, 0, 1000);

        popupHelper = PopupHelper.of(this);
//        carSlidePopupHelper = CarSlidePopupHelper.of(this);
        mapPopupHelper = MapPopupHelper.of(this);
//        img_navi2 = (ImageView) navi2_layout.findViewById(R.id.img_navi2);
//        img_navi2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SPUtil.put(CarShareMapActivity.this, "yuyue", "true");
//                try {
//                    carSlidePopupHelper.dismiss();
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//                try {
//                    mapPopupHelper.show();
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//            }
//        });
    }


    /**
     * 选择城市 按钮事件
     */
    private void selectCity() {
        LogUtils.i("点击城市选择按钮");
        Intent intent = new Intent(CarShareMapActivity.this, Activity_SelectCity.class);//跳转城市选择页面  接收返回的结果
        startActivityForResult(intent, ConstantCode.SELECT_CITY);
    }

    /**
     * @param l
     * @param title
     * @param one
     * @deprecated
     */
    private void setMarkerToMap(List<PositionBean> l, String title, boolean one) {
        //false 为默认   true 只有一个车
//        aMap.clear(true);
//        markerList.clear();
        for (int i = 0; i < l.size(); i++) {
            PositionBean m = l.get(i);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(Double.valueOf(m.lat), Double.valueOf(m.lng)));
//            markerOption.draggable(true);
            markerOption.icon(getBitmapDscriptor(m.ablekingLot + "", one));
//            markerOption.period(20);
            Marker marker = aMap.addMarker(markerOption);
            marker.setTitle(title);
            marker.setObject(m);
            if (!one) {
                markerList.add(marker);
            }
        }
    }

    //查询网点外的车辆
    private void car_EveryWhere() {

        synchronized (this) {
            Log.e("asd", "car_EveryWhere+查询网点外的车辆");

            String url = "api/car/nearly/query";
            HashMap<String, Object> map = new HashMap<>();
            map.put("lat", (curLat + Config.OFF_LAT) + "");
            map.put("lng", (curLon + Config.OFF_LNG) + "");
//        if (distance.equals("")){ distance="30";  }
//        map.put("distance",distance);

            MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                    .map(new FlatListFunction<>(CarNear.class))
                    .compose(RxNetHelper.<List<CarNear>>io_main(mLoadingDialog))
                    .subscribe(new ProgressSubscriber<List<CarNear>>(mLoadingDialog) {
                        @Override
                        public void onSuccessCode(Message object) {

                        }

                        @Override
                        public void onSuccess(List<CarNear> bean) {
                            try {
                                LogUtil.e("附近 车请求成功！" + bean.toString());

                                List<PositionBean> list_point = new ArrayList<>();
                                for (int a = 0; a < bean.size(); a++) {
                                    PositionBean positionBean = new PositionBean();
                                    positionBean.label = "";
                                    positionBean.stationName = "";
                                    positionBean.stationId = bean.get(a).getCarCode();
                                    positionBean.useArkingLot = "";
                                    positionBean.status = bean.get(a).getType();
                                    positionBean.stationCode = bean.get(a).getCarCode();
                                    positionBean.parkingLot = "";
                                    positionBean.lat = (Double.valueOf(bean.get(a).getLat()) + Config.OFF_LAT) + "";
                                    positionBean.lng = (Double.valueOf(bean.get(a).getLng()) + Config.OFF_LNG) + "";
                                    positionBean.ablekingLot = Double.valueOf(bean.get(a).getCount()).intValue();
                                    list_point.add(positionBean);
                                }


//                               if (list_car.size() > 0) {
//                                   return;
//                               }//防止多次加载
                                list_car = list_point;
//                               list_car = list_point;
                                for (int a = 0; a < list_car.size(); a++) {
//            infoList.get(i).getTotal_pile_count()+ ","       +
//            infoList.get(i).getCharge_station_type() + "."   +
                                    addDate(Double.valueOf(list_car.get(a).lat), Double.valueOf(list_car.get(a).lng), Double.valueOf(list_car.get(a).ablekingLot).intValue() + ",1." + list_car.get(a).stationCode);//总数类型  stationid
                                }
                                resetMarks();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            aMap.setOnMarkerClickListener(CarShareMapActivity.this);
//                        setMarkerToMap(list_point,"new",true);
                        }

                        @Override
                        public void onFail(Message<List<CarNear>> bean) {
                            LogUtil.e("附近 车 请求失败！" + bean.toString());
                            LogUtil.e("附近 车 请求失败！" + bean.msg);
                        }
                    });
        }
    }

    public void refreshMap() {

//        synchronized (this){
//            requestDoing();
//            list_wangdian.clear();
//            list_car.clear();
//            markerOptionsListall=new ArrayList<>();
//            // 查询附近网点车辆
//            Log.e("asd", "onNewIntent");
//            requestStation();
//        }
    }

    @Nullable
    @Optional
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarMapEvent(String event) {
        LogUtil.e("收到事件 -----" + event);
        if (event.equals("预约成功")) {
            refreshMap();
//            requestStation();
//            ToastUtil.showToast("預約成功");
            if (carSlidePopupHelper != null) {
                carSlidePopupHelper.dismiss();
            }
            //更新地图数据
//            list_car.clear();
//            markerOptionsListall=new ArrayList<>();
//            requestStation();
//            String title=curMarker.getOptions().getTitle();
//            String id = title.substring(title.indexOf(".") + 1, title.length());
//            requestCars2(id);
//            getDistance();
        } else if (event.equals("返回主界面")) {
            finish();
        } else if (event.equals("立即订车")) {
            try {
                carSlidePopupHelper2.dismiss();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                mapPopupHelper.dismiss();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else if (event.contains("导航")) {
            String lat = event.substring(event.indexOf(".") + 1, event.indexOf(","));
            String lon = event.substring(event.indexOf(",") + 1, event.length());
            if (nowClickMarker != null) {
                nowClickMarker.setPosition(new LatLng(markerLat, markerLon));
            }
            LogUtil.e("导航 lat：" + lat);
            LogUtil.e("导航 lon：" + lon);

            LogUtil.e("导航 lat：" + curLat);
            LogUtil.e("导航 lon：" + curLon);

            LatLng latLng= new LatLng(Double.valueOf(lat),Double.valueOf(lon) );
            CoordinateConverter converter = new CoordinateConverter(this);
// CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
// 转换
            converter.coord(latLng);
// 获取转换之后的高德坐标
            LatLng result = converter.convert();


            LatLng latLng1=new LatLng(curLat,curLon);
            CoordinateConverter converter1 = new CoordinateConverter(this);
// CoordType.GPS 待转换坐标类型
            converter1.from(CoordinateConverter.CoordType.GPS);
// 转换
            converter1.coord(latLng1);
// 获取转换之后的高德坐标
            LatLng result1 = converter.convert();


            clickedDaohang = true;
            Intent intent = new Intent(mContext, NaviActivity.class);
//            intent.putExtra("current_lat", result1.latitude + "");
//            intent.putExtra("current_lon", result1.longitude + "");
            intent.putExtra("current_lat", curLat + "");
            intent.putExtra("current_lon", curLon + "");
            intent.putExtra("target_lat", (Double.valueOf(lat)+Config.OFF_LAT)+"");
            intent.putExtra("target_lon", (Double.valueOf(lon)+Config.OFF_LNG)+"");
            startActivity(intent);
            try {
                mapPopupHelper.dismiss();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                carSlidePopupHelper.dismiss();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 收到正在用车事件  每分钟查询一次订单
     */
    @Nullable
    @Optional
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarMapEvent(CarMapEvent event) {
        LogUtil.e("收到事件：onCarMapEvent" + event.isFromCarSlide);
        LogUtil.e("收到事件：onCarMapEvent" + event.viewId);
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
                    default:
                        break;
                }
            }
            switch (event.viewId) {
                //CarSlidePopupHelper里面的event
                case R.id.img_navi:
                    //信息点
//                    carSlidePopupHelper.dismiss();
                    mapPopupHelper.show();
                    carSlidePopupHelper.dismiss();
                    break;
                case R.id.dongli_navi:
                    //使用时间
                    mapPopupHelper.show();
                    break;
                case R.id.btn_map_charging:
//                    ToastUtil.showToast("position" + event.position+","+carCode);
//                    Intent intent1 = new Intent(mContext, CarShareConfirmActivity.class);
//                    startActivity(intent1);
                    carSlidePopupHelper.dismiss();
                    break;
                //MapPopupHelper的event，各种导航选项
                case R.id.tv_amap:
                    //高德地图
                    clickedDaohang = true;
                    mapPopupHelper.dismiss();
                    try {
//                        boolean aa=SPUtil.get(CarShareMapActivity.this, "yuyue", "false").toString().equals("true");
                        if (SPUtil.get(CarShareMapActivity.this, "yuyue", "false").toString().equals("true")) {
                            clickedDaohang = true;
                            String targetlat = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lat", "0");
                            String targetlon = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lon", "0");

                            markerLat = Double.valueOf(targetlat);
                            markerLon = Double.valueOf(targetlon);
                            if (nowClickMarker != null) {
                                markerLat = nowClickMarker.getPosition().latitude;
                                markerLon = nowClickMarker.getPosition().longitude;
                            }
                            markerLat = markerLat - Config.OFF_LAT;
                            markerLon = markerLon - Config.OFF_LNG;
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    mapPopupHelper.openAMapNavi(markerLat, markerLon);
                    break;
                case R.id.tv_start_navi:
                    //导航
                    LogUtil.e("导航-当前位置lat：" + curLat + ",lon:" + curLon);
                    LogUtil.e("导航-目标位置lat：" + markerLat + ",lon:" + markerLon);

                    try {
                        //适配预约时的导航
                        if (SPUtil.get(CarShareMapActivity.this, "yuyue", "false").toString().equals("true")) {
                            clickedDaohang = true;
                            String targetlat = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lat", "0");
                            String targetlon = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lon", "0");

                            markerLat = Double.valueOf(targetlat);
                            markerLon = Double.valueOf(targetlon);

                            if (nowClickMarker != null) {
                                markerLat = nowClickMarker.getPosition().latitude;
                                markerLon = nowClickMarker.getPosition().longitude;
                            }

//                            markerLat = markerLat - Config.OFF_LAT;
//                            markerLon = markerLon - Config.OFF_LNG;

                            Intent intent = new Intent(mContext, NaviActivity.class);
                            intent.putExtra("current_lat", curLat + "");
                            intent.putExtra("current_lon", curLon + "");
                            intent.putExtra("target_lat", markerLat + "");
                            intent.putExtra("target_lon", markerLon + "");
                            //        intent.putExtra("current", new NaviLatLng(curLat, curLon));
                            //        intent.putExtra("target", new NaviLatLng(Double.valueOf(targetlat), Double.valueOf(targetlon)));
                            startActivity(intent);
                            mapPopupHelper.dismiss();
                            break;
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                    LogUtil.e("当前经纬度lat：" + curLat);
                    LogUtil.e("当前经纬度lon：" + curLon);
                    LogUtil.e("目标经纬度lat：" + markerLat);
                    LogUtil.e("目标经纬度lon：" + markerLon);

                    clickedDaohang = true;
//                    markerLat=markerLat+Config.OFF_LAT;
//                    markerLon=markerLon+Config.OFF_LNG;

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
                    clickedDaohang = true;
                    mapPopupHelper.dismiss();
                    try {
                        if (SPUtil.get(CarShareMapActivity.this, "yuyue", "false").toString().equals("true")) {
                            clickedDaohang = true;
                            String targetlat = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lat", "0");
                            String targetlon = (String) SPUtil.get(CarShareMapActivity.this, "yuyue_lon", "0");

                            markerLat = Double.valueOf(targetlat);
                            markerLon = Double.valueOf(targetlon);
                            if (nowClickMarker != null) {
                                markerLat = nowClickMarker.getPosition().latitude;
                                markerLon = nowClickMarker.getPosition().longitude;
                            }
                            Log.i("====","=====double前:"+targetlat+"--"+targetlon);
                            Log.i("====","=====double后:"+markerLat+"--"+markerLon);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
//                    markerLat = markerLat - Config.OFF_LAT;
//                    markerLon = markerLon + Config.OFF_LNG;
                    mapPopupHelper.openBaiduMapNavi(markerLat, markerLon);
                    break;
                default:
                    break;
            }
        }
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


//                        requestNowState();
                        List<CarNowOrderBean> list = bean1.orderList;
                        if (list == null) {
                            return;
                        }
                        //todo
                        if (list.size() > 0) {
                            CarNowOrderBean bean = list.get(0);
                            try {
                                PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, bean.carCode);
                                SPUtil.put(mContext, "carCode", bean.carCode);
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

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(CarOrderBean bean) {
                        //最终花费
                        LogUtil.e("请求成功," + bean.orderInfo.costFinal);
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
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMapTouchListener(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        x.task().postDelayed(new Runnable() {
            //取消跟随模式
            @Override
            public void run() {
                // TODO 2018.06.07 16:43
                aMap.setMyLocationEnabled(true);
            }
        }, 1000);
//            requestNowState();
//            if (!TextUtils.isEmpty(orderCode) && !TextUtils.isEmpty(driverType)) {
//                requestOrderState();
//            }else {
//                clearPreference();
//                orderVisable(-1);
//            }
        try {
            String istongyi = (String) SPUtil.get(this, "奇速共享汽车使用协议", "false");
            if (!istongyi.equals("true")) {
                startActivity(new Intent(this, Activity_CarshareWeb.class));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //注释掉，防止多次加载
        requestDoing();

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
        deactivate();
    }

    //订单状态异常时 弹窗提示
    private void Err2exit(final String str) {
        try {
            DialogHelper.alertDialog(CarShareMapActivity.this, str, new AlertDialog.OnDialogButtonClickListener() {
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
        try {
            if (status.equals("0")) {
                SPUtil.put(CarShareMapActivity.this, "yuyue_lat", bean.car.latitude);
                SPUtil.put(CarShareMapActivity.this, "yuyue_lon", bean.car.longitude);

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        orderVisable(Integer.valueOf(status));
        switch (status) {
            case "0": {
                requestLeftTime();
                isyuyue = true;
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


                break;
            }
            case "1": {
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
                break;
            }
            default:
                isyuyue = false;
                break;
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
//            if (bean.){  }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            adress = bean.car.getStationInfo().label;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (TextUtils.isEmpty(adress)) {
            dongli_map_old.setVisibility(View.GONE);
        } else {
            dongli_map_old.setVisibility(View.VISIBLE);
            tvMapAddress.setText(adress);//网点地图   bean.car.getStationInfo().label
        }
        Picasso.with(mContext).load(StringUtil.addImageHost(bean.car.carImgPath)).placeholder(R.mipmap.yc_52).into(imgItemCar);  //
        tvItemCarName.setText(bean.car.getCarBrandModels().brandName + " " + bean.car.getCarBrandModels().modelNumber);//车型号
        tvItemCarType.setText("(" + Double.valueOf(bean.car.carSeatNum).intValue() + "座)");//几座
        tvItemCarMile.setText("可行驶里程：" + bean.car.oddMileage + "公里");
        tvItemCarPower.setText("(" + bean.car.oddPowerForNE + "%)");                     //电量
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
                btnMapCharging.setVisibility(View.GONE);
                llCarByTime.setVisibility(View.GONE);
                llCarBook.setVisibility(View.VISIBLE);
                img_navi.setVisibility(View.VISIBLE);
                llCarOrder.setVisibility(View.GONE);
//                if (dongli.equals("1")) {
//                    // 动力 车 隐藏鸣笛按钮
//                    LinearLayout ll_car_order_bee = (LinearLayout) llCarBook.findViewById(R.id.ll_car_bee);
//                    ll_car_order_bee.setVisibility(View.GONE);
//                }

                dongli_map_old.setVisibility(View.VISIBLE);
                break;
            case 1:
                // 开始计费
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

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置是否只定位一次
//            mLocationOption.setOnceLocation(true);
//            mLocationOption.setOnceLocationLatest(true);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            mLocationOption.setInterval(1000);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }
        ;
        mlocationClient.startLocation();
    }

    // 弹toast
    private boolean dongli() {
        if (dongli.equals("1")) {
            //展示动力页面
            ll_dongli_layout.setVisibility(View.VISIBLE);
            dongli_navi.setVisibility(View.VISIBLE);                                     //动力  地图导航
            dongli_map_old.setVisibility(View.GONE);
            dongli_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("aaaa", "view.getId():" + view.getId());
                    EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                }
            });
            if (powerStatus.equals("1")) {
                //正常颜色 可以点击
                llCarOrder.setAlpha(1.0f);
                llCarOrder.setClickable(true);
                return true;
            } else {
                // 置空 不可点击
                llCarOrder.setAlpha(0.209f);
                ToastUtil.showToast("请先打开动力!");
                return false;
            }
        } else {
            return false;
        }

    }

    // 不弹toast
    private boolean dongli2() {
        if (dongli.equals("1")) {
            //展示动力页面
            ll_dongli_layout.setVisibility(View.VISIBLE);
            dongli_navi.setVisibility(View.VISIBLE);                                     //动力  地图导航
            dongli_map_old.setVisibility(View.GONE);
            dongli_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                }
            });
            if (powerStatus.equals("1")) {
                // 正常颜色 可以点击
                llCarOrder.setAlpha(1.0f);
                llCarOrder.setClickable(true);
                return true;
            } else {
                // 置空 不可点击
                llCarOrder.setAlpha(0.209f);
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 设置一些 amap 的属性
     */
    private void setUpMap() {
        // TODO 地图调试
        dialog = DialogHelper.loadingDialog(this, getString(R.string.dialog_map_location));

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 设置圆形的填充颜色,这两句取消原形边框
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动      （地图依照设备方向旋转 修改成不旋转）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息
        myLocationStyle.showMyLocation(true);
        //设置定位频次方法，单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将按照1000计算。该方法只会作用在会执行连续定位的工作模式上
        myLocationStyle.interval(2000);

        aMap.setMyLocationStyle(myLocationStyle);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 设置缩放按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapTouchListener(this);
        CameraPosition.Builder builder = CameraPosition.builder();
        //先移动地图到广州
        builder.target(new LatLng(23.117055306224895, 113.2759952545166));
        builder.zoom(16);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
        list = new ArrayList<>();
//        lv_station_list.setAdapter(adapter);
        //拿到地图工具类
        UiSettings mUiSettings = aMap.getUiSettings();
        // 禁用倾斜手势。
        mUiSettings.setTiltGesturesEnabled(false);
        // 禁用旋转手势。
        mUiSettings.setRotateGesturesEnabled(false);
    }

    int locationCount = 0;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (locationCount == 0) {
            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }
        }
        // TODO 2018.06.07 13:45
        try {
            dialog.cancel();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        finishLocation = true;
        Log.e("asd", "获取到的地址编码：" + aMapLocation.getAdCode());
        Log.e("asd", "获取到的地址编码：" + aMapLocation.getCityCode());
        Log.e("asd", "获取到的地址编码：" + aMapLocation.getProvince());
        //不删 复用
        try {
            aMap.getMapScreenMarkers().remove(locationMarker);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                current_city_str = aMapLocation.getCity();
                // 显示高德定位图标
                mListener.onLocationChanged(aMapLocation);
                // 点击定位按钮 能够将地图的中心移动到定位点
                curLat = aMapLocation.getLatitude();
                curLon = aMapLocation.getLongitude();
                String curCityCode = aMapLocation.getCityCode();
                LogUtil.e("CurLat is " + curLat);
                LogUtil.e("CurLng is " + curLon);
                LogUtil.e("CurCityCode is " + curCityCode);
                try {
                    // 以 Marker 的形式显示定位点
                    LogUtil.e("isLocationMarker is " + isLocationMarker);
                    // 首次进入定位
                    // TODO 定位点不显示
                    if (isLocationMarker <= 1) {
                        if (!clickedDaohang) {
                            // 如果是空的添加一个新的, icon 方法就是设置定位图标，可以自定义
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                            markerOptions.visible(true);
                            markerOptions.title("当前位置");
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nearby_charge));
                            markerOptions.icon(bitmapDescriptor);
                            markerOptions.period(60);
                            aMap.addMarker(markerOptions);
                            if (locationMarker == null) {
                                locationMarker = aMap.addMarker(markerOptions);
                                // 显示 Marker 标题
                                // locationMarker.showInfoWindow();
                            }
                            isLocationMarker = isLocationMarker + 1;
                        }
                    } else {
                        //已经添加过 Marker，修改位置即可
                        try {
                            locationMarker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                // TODO 2018.06.07 17:01
                if (isFirstLoc) {
                    CameraPosition.Builder builder = CameraPosition.builder();
                    // 移动地图到定位地点
                    builder.target(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    builder.zoom(16.0f);
                    //                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                    // TODO 2018.06.07 12:00
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                    isFirstLoc = false;
                }

                // 定位后请求桩点信息，如果跟上一次定位的城市不一样，才需要请求刷新站点信息

                if (curLat != 0 && locationCount == 0) {
                    requestStation();
//                    ToastUtil.showToast("onLocation");
                    locationCount++;
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                }
                if (cityCode.equals("")) {
                    cityCode = curCityCode;
//                        requestStation();
//                    getDistance();
                } else {
                    if (!cityCode.equals(curCityCode)) {
//                            requestStation();
//                        getDistance();
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

    private void setMarkerToMap(List<PositionBean> l) {
        aMap.clear(true);
        markerList.clear();
        for (int i = 0; i < l.size(); i++) {
            PositionBean m = l.get(i);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(Double.valueOf(m.lat), Double.valueOf(m.lng)));
//            markerOption.draggable(true);
            //false 为默认值
            markerOption.icon(getBitmapDscriptor(m.ablekingLot + "", false));
//            markerOption.period(20);
            Marker marker = aMap.addMarker(markerOption);
            marker.setObject(m);
            markerList.add(marker);
        }

        try {
            if (isLocationMarker <= 1) {
                //首次
                if (!clickedDaohang) {
                    //
                    isLocationMarker = isLocationMarker + 1;
                    // 如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(curLat, curLon));
                    markerOptions.visible(true);
                    markerOptions.title("当前位置");
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nearby_charge));
                    markerOptions.icon(bitmapDescriptor);
                    aMap.addMarker(markerOptions);
                    locationMarker = aMap.addMarker(markerOptions);
                }
            } else { //已经添加过了，修改位置即可
                try {
                    locationMarker.setPosition(new LatLng(curLat, curLon));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private BitmapDescriptor getBitmapDscriptor(String count, boolean one) {
        return BitmapDescriptorFactory.fromView(getMarkerView(count, one));
    }

    private View getMarkerView(String count, boolean one) {
        View view;
        if (one) {
            view = View.inflate(this, R.layout.marker_cs_item2, null);
        } else {
            view = View.inflate(this, R.layout.marker_cs_item, null);
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
        if (geocodeResult != null && geocodeResult.getGeocodeAddressList().size() > 0) {
            LatLonPoint myPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
//			CameraPosition.Builder builder = CameraPosition.builder();
////      builder.target(new LatLng(113.2759952545166 , 23.117055306224895));//先移动地图到广州
////			builder.target(new LatLng(myPoint.getLatitude(),myPoint.getLongitude()));// 移动地图
//			builder.zoom(aMap.getCameraPosition().zoom-1);//
//			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));///
//            aMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(myPoint.getLatitude(),myPoint.getLongitude())));

            CameraPosition.Builder builder = CameraPosition.builder();
            builder.target(new LatLng(myPoint.getLatitude(), myPoint.getLongitude()));//先移动地图到广州
            builder.zoom(16.0f);
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

            afterGeoPoint = myPoint;//更新切换城市后 经纬度坐标问题
            /*isGeo = true;*/

//            curLat=myPoint.getLatitude();//尝试更新 当前位置
//            curLon=myPoint.getLongitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        current_city.setText(current_city_str);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });
        }
    }

    //mark点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        nowClickMarker = marker;
        // 预约和用车中  直接返回
        if (isyuyue) {
            return true;
        }
        if (isUsingCar) {
            return true;
        }
        refreshFlag = false;
//        marker.setZIndex(100);
        // 默认不是网点外
        try {
            if (marker.getTitle().equals("new")) {
                SPUtil.put(CarShareMapActivity.this, "everywhere", "true");
            } else {
                SPUtil.put(CarShareMapActivity.this, "everywhere", "false");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            SPUtil.put(CarShareMapActivity.this, "everywhere", "false");
        }


        curMarker = marker;
        try {
            curMarkerData = (PositionBean) marker.getObject();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // TODO 逐级缩放地图
        if (curZoom <= 14) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), curZoom + 1.0f), 500L, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onCancel() {

                }
            });
        } else {

            // 点击 Marker 时移动到 Marker 所在坐标点，并缩放到合适等级，目前设置 18， 对应比例尺为 18-25 米
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18f), 500L, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onCancel() {
                }
            });
        }
        markerLat = marker.getPosition().latitude + Config.OFF_LAT;
        markerLon = marker.getPosition().longitude + Config.OFF_LNG;

        LogUtil.e("获取到的marker点数据标题：" + marker.getTitle());
        LogUtil.e("获取到的marker点数据标题：" + marker.getOptions().getTitle());

        float leavel = 0L;
        String title = "";
        try {
            title = marker.getOptions().getTitle();
        } catch (Throwable t) {
            t.printStackTrace();
            title = "";
        }
        // TODO 2018.06.08 17:20 注释掉，实现逐级缩放
//        if (title.equals("d")) {
//            leavel = aMap.getCameraPosition().zoom;
//            leavel = leavel + 3;
////            aMap.moveCamera(CameraUpdateFactory.zoomTo(leavel));
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
//            return true;
//        } else {
//            markerLat = marker.getPosition().latitude;
//            markerLon = marker.getPosition().longitude;
////            aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
////            String tmp =marker.getOptions().getTitle();
////            station_id = tmp.substring(tmp.indexOf(".")+1,tmp.indexOf("-"));
//            if (marker.getTitle().contains("当前")) {
//                return true;
//            }  //如果是当前位置 不显示pop
//        }

        String type = title.substring(title.indexOf(",") + 1, title.indexOf("."));
        String id = title.substring(title.indexOf(".") + 1, title.length());


        LogUtil.e("获取到的marker点数据标题：" + marker.getTitle());
        LogUtil.e("获取到的marker点数据：" + type);
        LogUtil.e("获取到的marker点数据：" + id);


        openMarker(type, id);
//        openMarker(marker);


//        carSlidePopupHelper.setNewData(0);
//        carSlidePopupHelper.show();
//        drawCalculateLine(new NaviLatLng(markerLat,markerLon));
//        getAddress(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
//        showPopWindow((PositionBean) marker.getObject());
//        mAdapter.setNewData(3);
        return true;
    }

    private void openMarker(String type, String id) {

        // 设置网点名称
//        if (!TextUtils.isEmpty(bean.label)) {
//            pop_stationName=bean.label;
////            carSlidePopupHelper.setStationName(bean.label);
//        }


        try {
            if (type.equals("1")) {
                requestCars2(id);
            } else {
                pop_stationName = "";
                // 请求数据  刷新数据
                requestCars(id);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            // 请求数据 / 刷新数据 / 默认值
            requestCars(id);
        }

    }

    private void intoDetail() {
        Intent intent = new Intent(CarShareMapActivity.this, CarShareFindCarActivity.class);
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

    private void openMarker(Marker marker) {

        LogUtil.e("获取到的marker点数据：" + marker.toString());
        LogUtil.e("获取到的marker点数据：" + marker.getTitle());


        PositionBean bean = (PositionBean) (marker.getObject());
        int count = bean.ablekingLot;

        LogUtil.e("获取到的marker点数据：" + bean.stationId);
        LogUtil.e("获取到的marker点数据：" + bean.stationCode);
        LogUtil.e("获取到的marker点数据：" + bean.status);
        LogUtil.e("获取到的marker点数据：" + bean.useArkingLot);
        LogUtil.e("获取到的marker点数据：" + bean.stationName);

        LogUtil.e("获取到的marker点数据：" + bean.label);
        LogUtil.e("获取到的marker点数据：" + bean.lat);
        LogUtil.e("获取到的marker点数据：" + bean.lng);
        LogUtil.e("获取到的marker点数据：" + bean.createdTime);
        LogUtil.e("获取到的marker点数据：" + bean.distance);
        LogUtil.e("获取到的marker点数据：" + bean.cityCode);


        try {
            if (marker.getTitle().equals("new")) {
                marker.setIcon(getBitmapDscriptor(count + "", true));
            } else {
                marker.setIcon(getBitmapDscriptor(count + "", false));
            }
        } catch (Throwable t) {
            t.printStackTrace();
            marker.setIcon(getBitmapDscriptor(count + "", false));
        }   //默认值
        if (!TextUtils.isEmpty(bean.label)) {
            pop_stationName = bean.label;
//            carSlidePopupHelper.setStationName(bean.label);//设置网点名称
        }
        if(!TextUtils.isEmpty(bean.distance+"")){
            distances=bean.distance+"";
        }


        try {
            if (marker.getTitle().equals("new")) {
                requestCars2(bean.stationCode);
            } else {
                //        if (!TextUtils.isEmpty(bean.stationCode)) {
                pop_stationName = "";
                requestCars(bean.stationCode);          //请求数据  刷新数据
//        }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            //        if (!TextUtils.isEmpty(bean.stationCode)) {
            requestCars(bean.stationCode);          //请求数据  刷新数据   默认值
//        }
        }

    }

    private void closeMarker(Marker marker) {
        PositionBean bean = null;

        try {
            bean = (PositionBean) (marker.getObject());
        } catch (Throwable t) {
            t.printStackTrace();
        }

//        try {
//            if (marker.getTitle().equals("new")) {
//                marker.setIcon(getBitmapDscriptor(bean.ablekingLot + "", true));//车的点
//            } else {
//                marker.setIcon(getBitmapDscriptor(bean.ablekingLot + "", false));
//            }
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//            marker.setIcon(getBitmapDscriptor(bean.ablekingLot + "", false));//默认点
//        }
    }

    private LatLngBounds getRange(CameraPosition cameraPosition) {
        Projection projection = aMap.getProjection();
        return projection.getMapBounds(cameraPosition.target, cameraPosition.zoom);
    }

    /**
     * 根据终点的经纬度绘制当前位置到终点的路线
     */
    private void drawCalculateLine(NaviLatLng endLat) {
//        NaviLatLng startLat = new NaviLatLng(curLat, curLon);
//        startList.clear();
//        startList.add(startLat);
//        endList.clear();
//        endList.add(endLat);
//        wayList.clear();
//        mAMapNavi.calculateDriveRoute(startList, endList, wayList, PathPlanningStrategy.DRIVING_SHORT_DISTANCE);
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
//                    ToastUtil.showToast("onActivity");
//                    getDistance();
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
        } else if (requestCode == ConstantCode.SELECT_CITY) {

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
                        Log.e("aaaa", "current_city:" + current_city);
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
                        try {
                            carSlidePopupHelper2.dismiss();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        try {
                            carSlidePopupHelper.dismiss();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        try {
                            mapPopupHelper.dismiss();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        refreshMap();
                        requestDoing();
//                      requestOrderState();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }

                });
    }

    /**
     * 关动力
     */
    private void requestCloseDongli() {
        String url = "api/cmd/car/cmd/send";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);
        requestMap.put("cmdKey", "POWER_OFF");

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
        String url = "api/cmd/car/cmd/send";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);
        requestMap.put("cmdKey", "POWER_ON");

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
        String url = "api/cmd/car/cmd/send";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);
        requestMap.put("cmdKey", "LOCK");

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
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    //附近的距离
    private void getDistance() {
        String url = "api/dic/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("dicKey", "KEY_OF_NEARSTATION_RANGE");//nearStationRange

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
                        try {
                            LogUtil.e("距离  请求成功:" + bean.toString());
                            String str = bean.toString().substring(bean.toString().indexOf("dicValue"), bean.toString().indexOf("dicValue") + 20);
                            LogUtil.e("距离  请求成功2:" + str);
                            String str_tmp = str.substring(str.indexOf("=") + 1, str.indexOf(","));//不包含
                            LogUtil.e("距离  请求成功3:" + str_tmp);
                            distance = str_tmp;
                            LogUtil.e("距离  请求成功:" + distance);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            distance = "";
                        }
                        //第一个接口请求成功之后
                        LogUtil.e("获取到的距离 " + distance);
//                        requestNearStation();
                        requestStation();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        LogUtil.e("距离  请求失败3:" + bean.msg);
                        ToastUtil.showToast(bean.msg);
                    }
                });

    }

    /**
     * 立即用车  调用  附近车辆
     */
    private void requestNearStation() {
        String url = "api/car/nearest/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("lat", curLat - Config.OFF_LNG);
        map.put("lng", curLon - Config.OFF_LAT);
//        if (distance.equals("")){ distance="30";  }
//        map.put("distance", distance);//600公里 内部

        Log.e("asd", "Asd" + " CarSlidePopupHelper2");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarNear_LY.class))
                .compose(RxNetHelper.<CarNear_LY>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<CarNear_LY>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarNear_LY bean) {

                        Log.e("aaaa", "Asd" + " //" + bean.toString());
                        Log.e("aaaa", "asd *****" + bean.getCarList().get(0).getLat());
//                        boolean isMatchStation = false;
//                        for (Marker marker : markerList) {
//                            LatLng latLng = marker.getPosition();
//                            double latt = Double.parseDouble(bean.lat)+Config.OFF_LAT;
//                            double lngg = Double.parseDouble(bean.lng)+Config.OFF_LNG;
//                            LatLng latLng1 = new LatLng(latt,lngg);//Double.valueOf(latt), Double.valueOf()
//                            if (latLng.equals(latLng1)) {
//                                onMarkerClick(marker);//模拟点击桩子按钮
//                                isMatchStation = true;
//                            }
//                        }
//                        if (!isMatchStation) { ToastUtil.showToast("暂无匹配的站点"); }
                        try {
                            markerLat = Double.valueOf(bean.getCarList().get(0).getLat());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        try {
                            markerLon = Double.valueOf(bean.getCarList().get(0).getLng());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }

                        carSlidePopupHelper2 = CarSlidePopupHelper2.of(CarShareMapActivity.this, bean.getCarList().size());
                        try {
                            SPUtil.put(CarShareMapActivity.this, "yuyue", "true");
                            SPUtil.put(CarShareMapActivity.this, "yuyue_lat", bean.getCarList().get(0).getLat());
                            SPUtil.put(CarShareMapActivity.this, "yuyue_lon", bean.getCarList().get(0).getLng());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        carSlidePopupHelper2.setStationName(pop_stationName);//设置网点名称
                        carSlidePopupHelper2.setDistance(distances);
                        int count = bean.getCarList().size();

                        List<CarBeanNew.CarList> list = new ArrayList<>();
//
                        for (int a = 0; a < count; a++) {
                            CarBeanNew.CarList carBean = new CarBeanNew.CarList();
                            carBean.setLatitude((Double.valueOf(bean.getCarList().get(a).getLat()) + Config.OFF_LAT) + "");
                            carBean.setLongitude((Double.valueOf(bean.getCarList().get(a).getLng()) + Config.OFF_LNG) + "");
                            carBean.setCarCode(bean.getCarList().get(a).getCarCode());
                            carBean.setOddPowerForNE(bean.getCarList().get(a).getOddPowerForNE());
                            carBean.setOddMileage(bean.getCarList().get(a).getOddMileage());
                            carBean.setCarImgPath(bean.getCarList().get(a).getCarImgPath());
                            carBean.setCarSeatNum((int) bean.getCarList().get(a).getCarSeatNum());
                            carBean.setBrandName(bean.getCarList().get(a).getBrandName());
                            carBean.setPlateNumber(bean.getCarList().get(a).getPlateNumber());
                            carBean.setModelNumber(bean.getCarList().get(a).getModelNumber());

                            Log.e("aaaa", "Asd" + " //" + "****************" + (Double.valueOf(bean.getCarList().get(a).getLng()) + Config.OFF_LNG) + "");
//                            carBean.latitude = (Double.valueOf(bean.getCarList().get(a).getLat()) + Config.OFF_LAT) + "";
//                            carBean.longitude = (Double.valueOf(bean.getCarList().get(a).getLng()) + Config.OFF_LNG) + "";
//                            carBean.carCode = bean.getCarList().get(a).getCarCode();
//                            carBean.oddPowerForNE = bean.getCarList().get(a).getOddPowerForNE();
//                            carBean.oddMileage = bean.getCarList().get(a).getOddMileage();
//                            carBean.mileage = bean.getCarList().get(a).getOddMileage();
//                            carBean.carSeatNum = bean.getCarList().get(a).getCarSeatNum();
//                            carBean.brandName = bean.getCarList().get(a).getBrandName();
//                            carBean.carImgPath = bean.getCarList().get(a).getCarImgPath();
//                            carBean.plateNumber = bean.getCarList().get(a).getPlateNumber();
//                            carBean.modelNumber = bean.getCarList().get(a).getModelNumber();
//                            Log.e("aaa", "oddPowerForNE:" + carBean.oddPowerForNE);
                            list.add(carBean);
                        }
                        carSlidePopupHelper2.setNewData2(list);
                    }

                    @Override
                    public void onFail(Message<CarNear_LY> bean) {
                        LogUtil.e("立即用车 失败 ：" + bean.msg + ",code:" + bean.code);
                        ToastUtil.showToast(bean.msg);//失败提示
                    }

                });

    }

    /**
     * 开门
     */
    private void requestOpenDoor() {
        String url = "api/cmd/car/cmd/send";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);
        requestMap.put("cmdKey", "UN_LOCK");

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
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    /**
     * 鸣笛
     */
    private void requestBee() {
        String url = "api/cmd/car/cmd/send";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", carCode);
        requestMap.put("cmdKey", "SEARCH");

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
                        ToastUtil.showToast(bean.msg);
                    }

                });
    }

    private void clearPreference() {
        PrefUtil.saveString(mContext, PrefUtil.ORDER_CODE, "");
//        PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, "");
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
                        isyuyue = false;
                        ToastUtil.showToast("取消预约成功");
                        orderVisable(-1);
                        clearPreference();
                        list_wangdian.clear();
                        list_car.clear();
                        markerOptionsListall = new ArrayList<>();
                        // 查询附近网点车辆
                        Log.e("asd", "onNewIntent");
                        requestStation();
//                        ToastUtil.showToast("cancel");
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                    }
                });
    }

    /**
     * 点击具体  车辆之后
     */
    private void requestCars(String stationCode) {
        String url = "api/station/car/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("stationCode", stationCode);//根据网点id  查询数据
        requestMap.put("latitude", (curLat + Config.OFF_LAT) + "");
        requestMap.put("longitude", (curLon + Config.OFF_LNG) + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(CarBeanNew.class))
                .compose(RxNetHelper.<CarBeanNew>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<CarBeanNew>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(CarBeanNew bean) {
                        Log.e("ffffff", "bean:" + bean.toString());
                        LogUtil.e("cars获取到当前list的大小为：" + bean.getCarList());
                        int count = 0;
                        try {
                            //默认为0 使用原来布局
                            count = bean.getCarList().size();
                        } catch (Throwable t) {
                            t.printStackTrace();
                            count = 0;
                        }
//                        carSlidePopupHelper = CarSlidePopupHelper.of(CarShareMapActivity.this,count);
//                        try{   }catch (Throwable t){t.printStackTrace();}
//                        carSlidePopupHelper.setStationName(pop_stationName);//设置网点名称
//                        carSlidePopupHelper.setNewData2(bean);              //设置pop的信息

                        carSlidePopupHelper2 = CarSlidePopupHelper2.of(CarShareMapActivity.this, count);
                        try {
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        carSlidePopupHelper2.setStationName(bean.getStationInfo().getStationName());//设置网点名称
                        carSlidePopupHelper2.setDistance(bean.getStationInfo().getDistance()+"");
//                        for (int a = 0; a < bean.getCarList().size(); a++) {
//                            bean.getCarList().get(a).setLatitude((Double.valueOf(bean.getCarList().get(a).getLatitude() + Config.OFF_LAT) + ""));
//                            bean.getCarList().get(a).setLongitude((Double.valueOf(bean.getCarList().get(a).getLongitude() + Config.OFF_LNG) + ""));
//                        }
                        carSlidePopupHelper2.setNewData2(bean.getCarList());
                        try {
                            // 保存 carcode，确认用车界面
                            SPUtil.put(mContext, "carCode", bean.getCarList().get(0).getCarCode());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        for (CarBeanNew.CarList bean1 : bean.getCarList()) {
                            // 车名
                            tvItemCarName.setText(bean1.getBrandName() + " " + bean1.getModelNumber());
                            tvItemCarType.setText("(" + Double.valueOf(bean1.getCarSeatNum()).intValue() + "座)");
                            tvItemCarMile.setText("可行驶里程：" + bean1.getOddMileage() + "公里(");
                            tvItemCarPower.setText("(" + bean1.getOddPowerForNE() + "%)");
                            tvItemCarNum.setText(bean1.getPlateNumber());
                        }
                    }

                    @Override
                    public void onFail(Message<CarBeanNew> bean) {
                        LogUtil.e("网点信息查询失败：" + bean.msg);
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    //定位
    public void dingwei() {
        try {
            //定位一次 ,1秒后取消
            aMap.setMyLocationEnabled(true);
            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    aMap.moveCamera(CameraUpdateFactory.zoomTo(18f));
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(curLat, curLon)));
                    aMap.setMyLocationEnabled(false);
                }
            }, 1500);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 点击具体  车辆之后-----网点外还车,查询车辆信息
     */
    private void requestCars2(String stationCode) {
        String url = "api/car/info/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        // 根据 carcode 查询数据
        requestMap.put("carCode", stationCode);
        requestMap.put("latitude", (curLat + Config.OFF_LAT) + "");
        requestMap.put("longitude", (curLon + Config.OFF_LNG) + "");

        LogUtil.e("网点外：" + stationCode);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(CarInfo.class))
                .compose(RxNetHelper.<CarInfo>io_main())
                .subscribe(new ProgressSubscriber<CarInfo>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(CarInfo bean) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        try {
                            pop_stationName = bean.getLastLocationTxt();
                            distances=bean.getDistance();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }//网点名
                        LogUtil.e("cars222222获取到当前list的大小为：" + bean.toString());
                        carSlidePopupHelper = CarSlidePopupHelper.of(CarShareMapActivity.this, 1);//固定大小为1
                        carSlidePopupHelper.setStationName(pop_stationName);//设置网点名称
                        carSlidePopupHelper.setDistance(distance);
                        List<CarBean> list_adp = new ArrayList<>();
                        CarBean carBean = new CarBean();
                        carBean.latitude = (Double.valueOf(bean.getLatitude())) + "";
                        carBean.longitude = (Double.valueOf(bean.getLongitude()) ) + "";
//                        carBean.latitude = (Double.valueOf(bean.getLatitude()) + Config.OFF_LAT) + "";
//                        carBean.longitude = (Double.valueOf(bean.getLongitude()) + Config.OFF_LNG) + "";
                        carBean.carImgPath = bean.getCarImgPath();
                        carBean.modelNumber = bean.getModelNumber();
                        carBean.brandName = bean.getBrandName();
                        carBean.carSeatNum = bean.getCarSeatNum();
                        carBean.oddMileage = bean.getOddMileage();
                        if (bean.getOddPower() == null) {
                            carBean.oddPowerForNE = bean.getOddPowerForNE();
                        } else {
                            carBean.oddPowerForNE = bean.getOddPower();
                        }
                        carBean.plateNumber = bean.getPlateNumber();
                        carBean.carCode = bean.getCarCode();

//                        CarBean.carBrandModels carBrandModels = new CarBean.carBrandModels();
//                        carBrandModels.modelNumber = bean.getCarBrandModels().getModelNumber();
//                        carBrandModels.brandName = bean.getCarBrandModels().getBrandName();
//                        carBrandModels.brandId = bean.getCarBrandModels().getBrandId();
//                        carBrandModels.brandModelsCode = bean.getCarBrandModels().getBrandModelsCode();
//                        carBrandModels.carDisposition = bean.getCarBrandModels().getCarDisposition();
//                        carBrandModels.createdBy = bean.getCarBrandModels().getCreatedBy();
//                        carBrandModels.createdTime = bean.getCarBrandModels().getCreatedTime();
//                        carBrandModels.modelPrice = bean.getCarBrandModels().getModelPrice();
//                        carBean.setCarBrandModels(carBrandModels);

                        list_adp.add(carBean);
                        // 设置pop的信息
                        carSlidePopupHelper.setNewData2(list_adp);
                        try {
                            // 保存carcode 确认用车界面
                            SPUtil.put(mContext, "carCode", bean.getCarCode());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }

                        tvItemCarName.setText(bean.getBrandName() + " " + bean.getModelNumber());
                        tvItemCarType.setText("(" + Double.valueOf(bean.getCarSeatNum()).intValue() + "座)");
                        tvItemCarMile.setText("可行驶里程：" + bean.getOddMileage() + "公里(");
                        tvItemCarPower.setText(bean.getOddPowerForNE() + "%)");
                        tvItemCarNum.setText(bean.getPlateNumber());
//                        for (CarBeanNew bean1 : list_adp) {
//                            // 车名
//                            tvItemCarName.setText(bean1.getCarBrandModels().brandName + " " + bean1.getCarBrandModels().modelNumber);
//                            tvItemCarType.setText("(" + Double.valueOf(bean1.carSeatNum).intValue() + "座)");
//                            tvItemCarMile.setText("可行驶里程：" + bean1.oddMileage + "公里(");
//                            tvItemCarPower.setText(bean1.oddPowerForNE + "%)");
//                            tvItemCarNum.setText(bean1.plateNumber);
//                        }
                    }

                    @Override
                    public void onFail(Message<CarInfo> bean) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        LogUtil.e("网点信息查询失败：" + bean.msg);
                    }
                });
    }

    int counts = 0;

    //请求附近网点和桩子
    private void requestStation() {

        synchronized (this) {

            Log.e("asd", "requestStation+请求附近网点和桩子");

            String url = "api/station/nearly/query";
            HashMap<String, Object> requestMap = new HashMap<>();
//        requestMap.put("lng", curLon);
//        requestMap.put("lat", curLat);

            requestMap.put("lat", curLat + Config.OFF_LAT);
            requestMap.put("lng", curLon + Config.OFF_LNG);
//        requestMap.put("distance", distance);
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
                            // 防止多次加载
//                            if (list_wangdian.size() > 0) {
//                                return;
//                            }
                            aMap.clear(true);
                            markerList.clear();
                            list.clear();
                            int count = bean.size();
                            car_EveryWhere();
                            // 请求附件车辆
                            if (count == 0) {
                                DialogHelper.alertDialog(CarShareMapActivity.this, getString(R.string.dialog_D101_no_data));
                            } else {
                                for (int a = 0; a < count; a++) {
                                    double latt = Double.parseDouble(bean.get(a).lat) + Config.OFF_LAT;
                                    double lngg = Double.parseDouble(bean.get(a).lng) + Config.OFF_LNG;
                                    bean.get(a).lat = latt + "";
                                    bean.get(a).lng = lngg + "";
                                }
                                list.addAll(bean);

                                list_wangdian = list;

                                for (int a = 0; a < list_wangdian.size(); a++) {
                                    //总数类型
                                    addDate(Double.valueOf(list_wangdian.get(a).lat), Double.valueOf(list_wangdian.get(a).lng), Double.valueOf(list_wangdian.get(a).ablekingLot).intValue() + ",0." + list_wangdian.get(a).stationCode);
//                              addDate(Double.valueOf(list_wangdian.get(a).lat),Double.valueOf(list_wangdian.get(a).lng),"1,");
                                }
                                resetMarks();
//                            setMarkerToMap(list);
                                for (PositionBean bean1 : list) {
                                    Log.e("asd", bean1.stationName);
                                }
                            }
                            aMap.setOnMarkerClickListener(CarShareMapActivity.this);
                        }

                        @Override
                        public void onFail(Message<List<PositionBean>> bean) {
                            car_EveryWhere();
                        }

                    });
        }
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
                            DialogHelper.alertDialog(CarShareMapActivity.this, getString(R.string.dialog_D101_no_data));
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


    //------------------------------AMapNaviListener,导航回调接口---------------------------------------
//    @Override public void onCalculateRouteSuccess() {//计算路线成功回调
//        routeOverlays.clear();//清空上次计算的路径列表。
//        AMapNaviPath path = mAMapNavi.getNaviPath();
//        drawRoutes(0, path);
//    }

//    @Override  public void onCalculateRouteSuccess(int[] ints) {
//        routeOverlays.clear();//清空上次计算的路径列表。
//        AMapNaviPath path = mAMapNavi.getNaviPath();
//        AMapNavi mAMapNavi = null;
//        mAMapNavi = AMapNavi.getInstance(this);
//        mAMapNavi.setUseInnerVoice(true);
//        drawRoutes(0, path);
//    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
//        calculateSuccess = true;
//        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
//        RouteOverLay routeOverLay = new RouteOverLay(aMap, path, this);
//        routeOverLay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker));
//        routeOverLay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker));
//        routeOverLay.setTrafficLine(false);
//        routeOverLay.addToMap();
//        routeOverlays.put(routeId, routeOverLay);
//
//        int routeID = routeOverlays.keyAt(routeId);
////        //突出选择的那条路
////        for (int i = 0; i < routeOverlays.size(); i++) {
////            int key = routeOverlays.keyAt(i);
////            routeOverlays.get(key).setTransparency(0.4f);
////        }
//        routeOverlays.get(routeID).setTransparency(0.4f);//设置透明度
////        routeOverlays.get(routeID).setZindex(zindex++);/**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
//        mAMapNavi.selectRouteId(routeID); //必须告诉AMapNavi 你最后选择的哪条路
//        Toast.makeText(this, "路线标签:" + mAMapNavi.getNaviPath().getLabels(), Toast.LENGTH_SHORT).show();
//        routeIndex++;
//        chooseRouteSuccess = true;

//        /**选完路径后判断路线是否是限行路线**/
//        AMapRestrictionInfo info = mAMapNavi.getNaviPath().getRestrictionInfo();
//        if (!TextUtils.isEmpty(info.getRestrictionTitle())) {
//            Toast.makeText(this, info.getRestrictionTitle(), Toast.LENGTH_SHORT).show();
//        }
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

    private void selectColor(int position) {
        switch (position) {
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


    //R.id.ll_car_bee, 需求改动  禁止
    @OnClick({R.id.img_location, R.id.img_use_car, R.id.img_navi, R.id.ll_car_cancel_book, R.id.ll_car_get, R.id.btn_map_charging,
            R.id.ll_car_order_bee, R.id.ll_car_order_open, R.id.ll_car_order_close, R.id.ll_car_order_back, R.id.img_car_driver_message, R.id.img_car_driver_call,
            R.id.money_gengduo, R.id.ll_car_order_open2_dongli_b, R.id.ll_car_order_close2_dongli_b, R.id.dongli_navi, R.id.img_car, R.id.tx_change_address, R.id.tx_best_car, R.id.tx_car, R.id.tx_all_car})
//            ,R.id.confirm_car_type_zijia_fenshizuche_gengduo,R.id.confirm_car_type_zijia_fenshizulin_gengduo})

    @Nullable
    @Optional
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

            case R.id.img_location:
                // 定位
                dialog.show();
                // 临时变量 防止导航时候  重复加载当前位置 marker点
                clickedDaohang = false;
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curLat, curLon), 15));
                // 定位一次
//                aMap.setMyLocationEnabled(true);
                // 1秒后取消持续定位
//                x.task().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        aMap.moveCamera(CameraUpdateFactory.zoomTo(16f));
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(curLat, curLon)));
//                        aMap.setMyLocationEnabled(false);
//                        // TODO 2018.06.07 11:36
//                        aMap.setMyLocationEnabled(true);
//                    }
//                }, 1500);
                break;
            case R.id.img_use_car:
                // 立即用车
                if (System.currentTimeMillis() - lasttime < 3000) {
                    return;
                }
                lasttime = System.currentTimeMillis();
//                if (UserParams.INSTANCE.checkLogin(this)) {
                // 检查登陆情况  每个用户一个key
//                  NaviLatLng endLat = new NaviLatLng(curLat + 0.022226, curLon + 0.022226);
                // 立即用车  附近距离要动态获取
                requestNearStation()
//                }
                ;
                break;

            case R.id.img_navi:
                // 预约中页面
                EventBus.getDefault().post(new CarMapEvent(view.getId(), 0));
                break;
            case R.id.ll_car_cancel_book:
                // 取消预定
                cancle_yuyue();
                break;
            case R.id.ll_car_get:
                // 取车
                PhotoDialogHelper.alertPhotoDialog(mContext, "充电枪已拔出", "取消",
                        "取车后将开始计费", "请环绕车身确认充电枪已拔出及车身无故障，如有故障请在奇速共享服务号上传照片", R.mipmap.cs_car_inject, new AlertPhotoDialog.OnDialogButtonClickListener() {
                            @Override
                            public void onConfirm() {
                                requestGetCar();
                            }

                            @Override
                            public void onCancel() {
                            }
                        });

//                PhotoDialogHelper.alertPhotoDialog(mContext, "充电枪已拔出", "取消",
//                        "开车前请确保充电枪已拔出", "取车后将开始计费", R.mipmap.cs_car_inject, new AlertPhotoDialog.OnDialogButtonClickListener() {
//                            @Override
//                            public void onConfirm() {
//                                requestGetCar();
//                            }
//
//                            @Override
//                            public void onCancel() {
//                            }
//                        });
                break;
//            case R.id.ll_car_bee:
//                // 取车成功后，租车进行时
//                if (dongli.equals("1")) {
//                    if (dongli()) {
//                        requestBee();
//                    } //可以点击
//                } else {
//                    requestBee();
//                }
//                break;
            case R.id.btn_map_charging:
                // 立即还车
                // requestReturnCar();
                LogUtil.e("点击立即还车用车");
                ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);
//                CarShareMapActivity.this.finish();
                break;
            case R.id.ll_car_order_bee:
                // 鸣笛找车
                if ("1".equals(dongli)) {
                    // 可以点击
                    if (dongli()) {
                        requestBee();
                    }
                } else {
                    requestBee();
                }
                break;
            case R.id.ll_car_order_open:
                // 反馈问题
                if (dongli.equals("1")) {
                    //可以点击
                    if (!dongli()) {
                        return;
                    }
                }
                requestOpenDoor();
//                PhotoDialogHelper.alertTwoConfirmDialog(mContext, "车况完好，立即开门",
//                        "", "开车前，请环绕车身检查外观、轮胎、反光镜和周围路况。", R.mipmap.cs_car_yellow
//                        , new AlertPhotoDialog.OnDialogButtonClickListener() {
//                            @Override
//                            public void onConfirm() {
//                                    requestOpenDoor();
//                            }
//
//                            @Override
//                            public void onCancel() {
////                                ActivityUtil.startActivity(mContext, CarShareReportBadActivity.class);
//                            }//取消报备
//                        });
                break;
            case R.id.ll_car_order_close:
                // 锁门
                if (dongli.equals("1")) {
                    // 可以点击
                    if (dongli()) {
                        requestCloseDoor();
                    }
                } else {
                    requestCloseDoor();
                }
                break;
            case R.id.ll_car_order_back:
                // 推荐还车点
                // requestReturnCar();
//                if(dongli.equals("1")){
//                    if (dongli()){ ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);   } //新版
//                }else {
//                      ActivityUtil.startActivity(mContext, CarShareReturnCarActivity.class);
//                }
                // 直接跳到网点 去掉还车
                ActivityUtil.startActivity(mContext, CarShareReturnLockActivity.class);
                break;
            case R.id.img_car_driver_message:
                // 给司机发短信
                if (TextUtils.isEmpty(driverPhone)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }
                DialogHelper.confirmDialog(mContext, "是否给 " + driverPhone + "发短信？", new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PermissionUtil.checkPermission(CarShareMapActivity.this, ConstantCode.SEND_SMS, Manifest.permission.SEND_SMS); //检查权限
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
            case R.id.img_car_driver_call:
                // 代驾，进行呼叫电话
                if (TextUtils.isEmpty(driverPhone)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }
                DialogHelper.confirmDialog(mContext, "是否呼叫 " + driverPhone + "？", new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PermissionUtil.checkPermission(CarShareMapActivity.this, ConstantCode.CALL_PHONE, Manifest.permission.CALL_PHONE);
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
            case R.id.money_gengduo:
                // 立即还车时候的更多
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
            case R.id.img_car:
                Intent i = new Intent(this, CommonWebViewActivity.class);
                i.putExtra("act_url", "http://www.qisu666.com/app-zfsm/zfsm.html");
                i.putExtra("act_title", "资费说明");
                startActivity(i);
                break;
            default:
                break;
        }
    }


    // 取消预约 最大次数
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
                                    DialogHelper.confirmDialog(CarShareMapActivity.this, "每天最多可以取消2次,每次取消需要间隔1小时，确定取消么？", new AlertDialog.OnDialogButtonClickListener() {
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
                                    DialogHelper.confirmDialog(CarShareMapActivity.this, "每天最多可以取消" + tmp_t + "次,每次取消需要间隔1小时，确定取消么？", new AlertDialog.OnDialogButtonClickListener() {
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

    @Subscribe
    public void onEvent(FinishActivityEvent event) {
        finish();
    }

    @Subscribe
    public void onEvent(CarOrderLationEvent orderLationEvent) {
        if (nowClickMarker != null) {
            nowClickMarker.setPosition(new LatLng(Double.valueOf(orderLationEvent.lat), Double.valueOf(orderLationEvent.lon)));
        }
    }

    boolean isfistFinish = false;//首次进入地图 缩放级别16

    //地图改变时
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        LogUtil.e("CameraPosition is (Lat: " + cameraPosition.target.latitude + ", Lng: " + cameraPosition.target.longitude + ")");
//        LogUtil.e("locationMarker is " + locationMarker);
        curZoom = cameraPosition.zoom;
//        LogUtil.e("Current Zoom is " + curZoom);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        resetMarks();
    }
}
