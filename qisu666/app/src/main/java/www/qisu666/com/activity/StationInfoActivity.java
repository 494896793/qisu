package www.qisu666.com.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.navi.model.NaviLatLng;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.DensityUtil;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.MapUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CustomFragmentPagerAdapter;
import www.qisu666.com.adapter.DeviceDetailAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.fragment.StationCommentFragment;
import www.qisu666.com.fragment.StationDetailFragment;
import www.qisu666.com.fragment.StationParkingFragment;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.CustomListView;
import www.qisu666.com.widget.CustomRadioGroup;
import www.qisu666.com.widget.CustomViewPager;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.com.widget.NetworkImageHolderView;
import www.qisu666.com.widget.PullToRefreshLayout;
import www.qisu666.sdk.amap.stationMap.juhe.PointAggregationAty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//站信息
public class StationInfoActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener, GestureDetector.OnGestureListener {
    private LinearLayout layout_content;
    private TextView tv_title;
    private ImageView right_btn;
    private boolean isFirst = false;

    private TextView tv_station_name;
    private TextView tv_open_time;
    private LinearLayout layout_phone;
    private TextView tv_address;
    private ImageView img_navi;
    private TextView tv_charging_fee;
    private TextView tv_service_fee;
    private TextView tv_station_parking_fee;
    private CustomRadioGroup radioGroup;
    private RadioButton radio_detail;
    private RadioButton radio_parking;
    private RadioButton radio_comment;
    private CustomViewPager viewPager;
    private RelativeLayout line_re_three;
    private RelativeLayout line_re_one;
    private RelativeLayout line_re_two;
    private CustomFragmentPagerAdapter adapter;

    private PopupWindow mapPopupWindow;
    private List<Map<String, Object>> list = new ArrayList<>();

    private double lat;
    private double lng;

    private Bundle detailBundle, parkingBundle, commentBundle;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    DeviceDetailAdapter detailAdapter;//详情列表的适配器
    List<Map<String, Object>> list_detail_all, list_detail_two;//默认两个 展开全部


    //定位经纬度
    private double locationLat;
    private double locationLng;
    private boolean favorFlag = false; //当前站点是否被收藏
    private String station_id = "";//站点id
    private int curPage = 0;//当前页数，用来处理刷新pager错位的bug
    private boolean isRefresh = false;
    private ConvenientBanner convenientBanner;//广告栏
    private List<Integer> localImages = new ArrayList<>();  //网络加载图片地址
    private ScrollView scrollView;
    private ValueAnimator expandAnim;
    private ValueAnimator collapseAnim;
    private GestureDetector gestureDetector;
    private ViewGroup.LayoutParams layoutParams;
    private int maxHeight;//ConvenientBanner最大高度
    private View bg_view;
    private boolean isScroll = false; //是否正在滚动
    private boolean isOnCreate = true;//是否是第一次创建页面
    private List<String> networkImages;//网络加载图片地址
    private PullToRefreshLayout refresh_layout; //下拉刷新父布局
    private float down_x, down_y, cur_x, cur_y, d_x, d_y;
    private CustomListView listView;


    private TextView station_info_diandufei;//桩子的分时段  充电费用
    private ImageView station_info_gengduo; //计费策略的更多
    private boolean is_info_gengduo = false; //是否点击更多
    ArrayList list_gengduo = new ArrayList();//保存计费策略文本
    private String station_info_gengduo_txt; //


    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        connToServer(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_station_info);
        initTitleBar();
        initViews();
        initMapPopupWindow();
        connToServer(true);
        EventBus.getDefault().register(this);
    }

    private void initTitleBar() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("station_name"));
//        tv_title.setAlpha(0);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right_btn = (ImageView) findViewById(R.id.img_title_right);
        right_btn.setImageResource(R.mipmap.ic_title_collection);
        station_info_diandufei = (TextView) findViewById(R.id.station_info_diandufei);
        station_info_gengduo = (ImageView) findViewById(R.id.station_info_gengduo);    //更多的ui

        if (TextUtils.isEmpty(UserParams.INSTANCE.getUser_id())) {
            right_btn.setVisibility(View.GONE);
        } else {
            right_btn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        line_re_three=findViewById(R.id.line_re_three);
        line_re_one=findViewById(R.id.line_re_one);
        line_re_two=findViewById(R.id.line_re_two);
        bg_view = findViewById(R.id.bg_view);
//        bg_view.setAlpha(0);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        layout_phone = (LinearLayout) findViewById(R.id.layout_phone);
        tv_station_name = (TextView) findViewById(R.id.tv_station_name);
        tv_open_time = (TextView) findViewById(R.id.tv_open_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        img_navi = (ImageView) findViewById(R.id.img_navi);
        tv_charging_fee = (TextView) findViewById(R.id.tv_charging_fee);
        tv_service_fee = (TextView) findViewById(R.id.tv_service_fee);
        tv_station_parking_fee = (TextView) findViewById(R.id.tv_station_parking_fee);
        radioGroup = (CustomRadioGroup) findViewById(R.id.radioGroup);
        radio_detail = (RadioButton) findViewById(R.id.radio_detail);
        radio_parking = (RadioButton) findViewById(R.id.radio_parking);
        radio_comment = (RadioButton) findViewById(R.id.radio_comment);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        refresh_layout = (PullToRefreshLayout) findViewById(R.id.refresh_layout);
        listView = (CustomListView) findViewById(R.id.listView);
        station_info_gengduo = (ImageView) findViewById(R.id.station_info_gengduo);//桩点地图更多
//        listView.setFocusable(false);
        listView.setDispatch(true);
        viewPager.setDispatch(false);
        radioGroup.setDispatch(true);
        initBanner();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        gestureDetector = new GestureDetector(this, this);
        layoutParams = convenientBanner.getLayoutParams();
        refresh_layout.setCanPull(true);
        refresh_layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                curPage = viewPager.getCurrentItem();
                connToServer(false);
            }
        });
    }

    private void initBanner() {
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        maxHeight = convenientBanner.getLayoutParams().height = (int) (DensityUtil.getScreenWidth(this) / 2.3);
        //使用本地图片
//        localImages.add(R.mipmap.ic_home_banner_1);
//        localImages.add(R.mipmap.ic_home_banner_2);
//        localImages.add(R.mipmap.ic_home_banner_3);

//        convenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView(ImageView.ScaleType.CENTER_CROP);
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
//        convenientBanner.getViewPager().setPageTransformer(true, new DepthPageTransformer());

        //使用网络图片
        convenientBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
//        //设置切换页面的动画
        convenientBanner.getViewPager().setPageTransformer(true, new DepthPageTransformer());
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(StationInfoActivity.this, ZoomImageActivity.class);
                i.putExtra("img_path", networkImages.get(position));
                startActivity(i);
            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        try {
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(StationDetailFragment.newInstance(detailBundle));
            fragments.add(StationParkingFragment.newInstance(parkingBundle));
            fragments.add(StationCommentFragment.newInstance(commentBundle));
            adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(adapter);
        } catch (Throwable t) {
            t.printStackTrace();
        }//防止handler获取不到崩溃异常
    }

    private RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location); // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        return new RectF(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
    }

    /**
     * 设置监听器
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        layout_phone.setOnClickListener(this);
        img_navi.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        radio_detail.setOnClickListener(this);
        radio_parking.setOnClickListener(this);
        radio_comment.setOnClickListener(this);
        station_info_gengduo.setOnClickListener(this);
//        radio_detail.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if(event.getAction() == MotionEvent.ACTION_UP){
////                    LogUtils.d("aaaaa||||aaaaa");
//                    viewPager.setCurrentItem(0);
////                }
//                return true;
//            }
//        });
//        radio_parking.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if(event.getAction() == MotionEvent.ACTION_UP){
////                    LogUtils.d("bbbbb||||bbbbb");
//                    viewPager.setCurrentItem(1);
////                }
//                return true;
//            }
//        });

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_detail){
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(1);
                }
            }
        });*/

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(convenientBanner.getHeight()!=0){
                        LogUtils.d("viewPager.setDispatch(true);");
                        viewPager.setDispatch(true);
                        return true;
                    } else {
                        viewPager.setDispatch(false);
                        LogUtils.d("viewPager.setDispatch(false);");
                    }
                }
                return false;
            }
        });*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radio_detail);
                        line_re_one.setVisibility(View.VISIBLE);
                        line_re_two.setVisibility(View.INVISIBLE);
                        line_re_three.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        radioGroup.check(R.id.radio_parking);
                        line_re_one.setVisibility(View.INVISIBLE);
                        line_re_two.setVisibility(View.VISIBLE);
                        line_re_three.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        radioGroup.check(R.id.radio_comment);
                        line_re_one.setVisibility(View.INVISIBLE);
                        line_re_two.setVisibility(View.INVISIBLE);
                        line_re_three.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                LogUtils.d("viewPager.isDispatch():"+viewPager.isDispatch());
//                RectF rectF = calcViewScreenLocation(convenientBanner);
//                if(rectF.contains(event.getRawX(), event.getRawY())){
//                    refresh_layout.setCanPull(false);
//                }
//                if(event.getAction() == MotionEvent.ACTION_UP){
//
//                }
                if (!isScroll) {
                    gestureDetector.onTouchEvent(event);
//                    if(event.getAction() == MotionEvent.ACTION_DOWN){
//                        LogUtils.d("viewPager.isDispatch():"+viewPager.isDispatch());
//                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int height = convenientBanner.getHeight();
                        if (height < maxHeight / 2 && height >= 0) {
                            startCollapseAnim(height);
                        } else if (height >= maxHeight / 2 && height <= maxHeight) {
                            startExpandAnim(height);
                        }
                    }
                }
                return true;
//                } else {
                    /*if(scrollView.getScrollY()>0){
                        isScroll = true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = event.getX();
                        downY = event.getY();
                        lastMoveX = downX;
                        lastMoveY = downY;
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        moveX = event.getX();
                        moveY = event.getY();
                        if (lastMoveY < moveY && scrollView.getScrollY() == 0 && !isScroll) {
                            isShow = true;
                            return true;
                        }
                        lastMoveX = moveX;
                        lastMoveY = moveY;
                    } else if(event.getAction()==MotionEvent.ACTION_UP){
                        isScroll = false;
                    }*/

//                    return true;
//                }
            }
        });
    }

    /**
     * 设置展开动画
     */
    private void startExpandAnim(int height) {
        expandAnim = ValueAnimator.ofFloat(height, maxHeight).setDuration((long) (400 * (((float) maxHeight - (float) height) / (float) maxHeight)));
        expandAnim.setTarget(convenientBanner);
        expandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) (Float.parseFloat(animation.getAnimatedValue().toString()));
                convenientBanner.setLayoutParams(layoutParams);
            }
        });
        expandAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScroll = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScroll = false;
                viewPager.setDispatch(false);
//                radioGroup.setDispatch(false);
                refresh_layout.setCanPull(true);
                LogUtils.d("viewPager.isDispatch():" + viewPager.isDispatch());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        expandAnim.start();
    }

    /**
     * 设置展开动画
     */
    private void startCollapseAnim(int height) {
        collapseAnim = ValueAnimator.ofFloat(height, 0.0F).setDuration((long) (400 * ((float) height / (float) maxHeight)));
        collapseAnim.setTarget(convenientBanner);
        collapseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) (Float.parseFloat(animation.getAnimatedValue().toString()));
                convenientBanner.setLayoutParams(layoutParams);
            }
        });
        collapseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScroll = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScroll = false;
                viewPager.setDispatch(true);
//                radioGroup.setDispatch(true);
                refresh_layout.setCanPull(false);
                LogUtils.d("viewPager.isDispatch():" + viewPager.isDispatch());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        collapseAnim.start();
    }

    private boolean isInstallByread(String packageName) {
//        return new File("/data/data/" + packageName).exists();
        return new File(IDianNiuApp.getInstance().getFilesDir().getParentFile().getPath()).exists();
    }

    /**
     * 选择地图PopupWindow
     */
    private void initMapPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_choice_map, null); // 一个自定义的布局，作为显示的内容

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
        mapPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_phone:
                AlertDialog dialog = new AlertDialog(this, "呼叫", "取消", "400-7569999", "服务时间：09:00-22:00", true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            try {
                                callPhone();
                            } catch (Exception e) {
                                ToastUtil.showToast("拨打电话权限受限，请在权限设置中打开该权限");
                            }
                        } else {
                            if (PermissionUtil.checkPermission(StationInfoActivity.this, ConstantCode.CALL_PHONE, Manifest.permission.CALL_PHONE)) {
                                callPhone();
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                dialog.show();
                break;
            case R.id.img_navi:
                mapPopupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_start_navi:
                startLocation();
                mapPopupWindow.dismiss();
                break;
            case R.id.tv_amap:
                openAMapNavi();
                mapPopupWindow.dismiss();
                break;
            case R.id.tv_baidu:
                openBaiduMapNavi();
                mapPopupWindow.dismiss();
                break;
            case R.id.img_title_right:
                favorAction();
                break;
            case R.id.radio_detail:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radio_parking:
                viewPager.setCurrentItem(1);
                break;
            case R.id.radio_comment:
                viewPager.setCurrentItem(2);
                break;
            case R.id.station_info_gengduo:
                if (is_info_gengduo) {
                    try {
                        Glide.with(this).load(R.mipmap.gengduo1).into(station_info_gengduo);
                        station_info_diandufei.setText(list_gengduo.get(0).toString() + list_gengduo.get(1).toString());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } else {
                    Glide.with(this).load(R.mipmap.gengduo2).into(station_info_gengduo);
                    String str = "";
                    try {
                        for (int a = 0; a < list_gengduo.size(); a++) {
                            str += list_gengduo.get(a);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    station_info_diandufei.setText(str);
                }
                is_info_gengduo = !is_info_gengduo;
                break;
            default:
                break;

        }
    }

    private void callPhone() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:400-7569999"));
        startActivity(i);
    }

    /**
     * 打开高德地图
     */
    public void openAMapNavi() {
        NaviPara naviPara = new NaviPara(); // 构造导航参数
        LatLng latLng = new LatLng(lat, lng);// 设置终点位置
        naviPara.setTargetPoint(latLng);
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);// 设置导航策略，这里是避免拥堵


        try { // 调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            AMapUtils.getLatestAMapApp(getApplicationContext()); // 如果没安装会进入异常，调起下载页面
        }
    }

    /**
     * 打开百度地图
     */
    public void openBaiduMapNavi() {
        try {
            Intent intent = Intent.parseUri("intent://map/direction?destination=" + MapUtils.bd_encrypt(lat, lng)
                    + "&mode=driving&src=iDianNiu|iDianNiu#Intent;scheme=b" + "dapp;package=com.baidu.BaiduMap;end", 0);
//            Intent intent = Intent.parseUri("intent://map/direction?origin=latlng:"+MapUtils.bd_encrypt(locationLat,locationLng)+
//                    "|name:富力盈泰&destination="+ MapUtils.bd_encrypt(lat,lng)+"&mode=driving&region=广州&src=iDianNiu|iDianNiu#Intent;scheme=b" +
//                    "dapp;package=com.baidu.BaiduMap;end",0);
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
     * 发送 D102 请求，获取电站详情
     */
    private void connToServer(final boolean flag) {
        station_id = getIntent().getStringExtra("station_id");
        String url = "api/pile/station/info/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("stationId", station_id);
        map.put("userId", UserParams.INSTANCE.getUser_id());

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
                        Log.e("aaaa", "StationInfoActivity.java:" + s);
                        // json转 map
                        Map jsonToMap = JsonUtils.jsonToMap(s);

                        if (refresh_layout.isRefresh()) {
                            refresh_layout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                        }
                        try {
                            if (list.size() > 0) {
                                list.clear();
                            }
                            if (list_gengduo.size() > 0) {
                                list_gengduo.clear();
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        refresh_layout.setRefreshBarVisible(true);
                        tv_station_name.setText(jsonToMap.get("stationName").toString());
                        tv_address.setText(jsonToMap.get("addrDetail").toString());
                        lat = Double.valueOf(jsonToMap.get("latitude").toString());
                        lng = Double.valueOf(jsonToMap.get("longitude").toString());
                        tv_charging_fee.setText(jsonToMap.get("chargeFee").toString());
                        tv_service_fee.setText(jsonToMap.get("serviceFee").toString());
                        tv_station_parking_fee.setText(jsonToMap.get("parkingFee").toString());
                        right_btn.setImageResource(jsonToMap.get("isFavor").toString().equals("0") ? R.mipmap.ic_collection_yellow : R.mipmap.ic_title_collection);
                        favorFlag = jsonToMap.get("isFavor").toString().equals("0");
                        tv_open_time.setText(jsonToMap.get("serviceTime").toString());

                        networkImages = Arrays.asList(jsonToMap.get("stationPic").toString().split(","));
                        LogUtils.d("networkImages:" + networkImages.toString());
                        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                            @Override
                            public NetworkImageHolderView createHolder() {
                                return new NetworkImageHolderView(ImageView.ScaleType.CENTER_CROP);
                            }
                        }, networkImages);

                        if (jsonToMap.get("recordList") != null && !"".equals(jsonToMap.get("recordList").toString())) {
                            try {
                                JSONArray array = new JSONArray(jsonToMap.get("recordList").toString());
                                int count = array.length();
                                for (int i = 0; i < count; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    list.add(JsonUtils.jsonToMap(object.toString()));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        if (jsonToMap.get("timeList") != null && !"".equals(jsonToMap.get("timeList").toString())) {
                            try {
                                JSONArray array = new JSONArray(jsonToMap.get("timeList").toString());
                                int count = array.length();
                                if (count != 0) {
//                            String txt="电度费\n";
                                    String txt = "", txt_tmp = "";
                                    for (int i = 0; i < count; i++) {
                                        try {
                                            JSONObject object = array.getJSONObject(i);
//                                            LogUtil.e(i + "当前桩子信息数据" + object.toString());
                                            txt = "(" + StringUtil.formatHour(object.get("startTime").toString()) + "-" + StringUtil.formatHour(object.get("endTime").toString()) + ")\t\t"
                                                    + new BigDecimal(object.get("chargePrice").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/度\n\n";
                                            txt_tmp += txt;
                                            list_gengduo.add(txt);//已经有换行符
//                                list1.add(JsonUtils.jsonToMap(object.toString()));
                                        } catch (Throwable t) {
                                            t.printStackTrace();
                                        }
                                    }
                                    if (list_gengduo != null) {
                                        try {
                                            if (list_gengduo.size() >= 2) {
                                                station_info_diandufei.setText(list_gengduo.get(0).toString() + list_gengduo.get(1).toString());//显示 分时段费用问题
                                            } else if (list_gengduo.size() == 1) {
                                                station_info_diandufei.setText(list_gengduo.get(0).toString());
                                            } else {
                                                station_info_diandufei.setText(list_gengduo.get(0).toString());
                                            }
                                        } catch (Throwable t) {
                                            t.printStackTrace();
                                        }
                                    }

                                }
//                        listView.setAdapter(new DeviceDetailAdapter(StationInfoActivity.this, list1,true));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        detailBundle = new Bundle();
                        detailBundle.putSerializable("list", (ArrayList) list);
                        parkingBundle = new Bundle();
                        parkingBundle.putString("parking_fee", jsonToMap.get("parkingFee").toString());
                        parkingBundle.putString("parking_lot", jsonToMap.get("parkingLot").toString());
                        parkingBundle.putString("direction_area", jsonToMap.get("directionArea").toString());
                        layout_content.setVisibility(View.VISIBLE);
                        connForComment(flag);

                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });


//        station_id = getIntent().getStringExtra("station_id");
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "D102");
//            jsonObject.put("station_id", station_id);
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        HttpLogic httpLogic = new HttpLogic(this);
//        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, flag, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                /**
//                 *  D102 response:{"station_name":"312创意中心充电站","charge_fee_per":"1.85","is_favor":"2","return_msg":"操作成功.","service_time":"0","charge_fee":"1.35",
//                 *                  "direction_area":"","station_pic":"http://120.76.165.5:80/idnManager/upload/station_img/201608011202380670_366949975200890281.jpg,http://120.76.165.5:80/idnManager/upload/station_img/201607291048360166_125597973681486284.jpg",
//                 *                  "record_list":[{"charge_pile_power":"7","parking_lot":"1","charge_pile_efficiency":"02","charge_pile_type":"02","charge_pile_model":"00000","charge_interface":"01","charge_pile_num":"10000001","charging_gun":"0","pile_state":"00","charge_pile_bel":"01"},
//                 *                  {"charge_pile_power":"7","parking_lot":"1","charge_pile_efficiency":"02","charge_pile_type":"02","charge_pile_model":"00000","charge_interface":"01","charge_pile_num":"10000002","charging_gun":"0","pile_state":"04","charge_pile_bel":"01"},
//                 *                  {"charge_pile_power":"60","parking_lot":"1","charge_pile_efficiency":"01","charge_pile_type":"01","charge_pile_model":"00000","charge_interface":"01","charge_pile_num":"10000003","charging_gun":"0","pile_state":"00","charge_pile_bel":"01"}],
//                 *                  "parking_lot":"","service_fee":"0.5","parking_fee":"5","longitude":"113.355728","station_id":89,"latitude":"23.112574","return_code":"0000"}
//                 */
//                if (refresh_layout.isRefresh()) {
//                    refresh_layout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
//                }
//                try {
//                    if (list.size() > 0) {
//                        list.clear();
//                    }
//                    if (list_gengduo.size() > 0) {
//                        list_gengduo.clear();
//                    }
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//                refresh_layout.setRefreshBarVisible(true);
//                tv_station_name.setText(map.get("station_name").toString());
//                tv_address.setText(map.get("addr_detail").toString());
//                lat = Double.valueOf(map.get("latitude").toString());
//                lng = Double.valueOf(map.get("longitude").toString());
//                tv_charging_fee.setText(map.get("charge_fee").toString());
//                tv_service_fee.setText(map.get("service_fee").toString());
//                tv_station_parking_fee.setText(map.get("parking_fee").toString());
//                right_btn.setImageResource(map.get("is_favor").toString().equals("0") ? R.mipmap.ic_collection_yellow : R.mipmap.ic_title_collection);
//                favorFlag = map.get("is_favor").toString().equals("0");
//                tv_open_time.setText(map.get("service_time").toString());
//
//                networkImages = Arrays.asList(map.get("station_pic").toString().split(","));
//                LogUtils.d("networkImages:" + networkImages.toString());
//                convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//                    @Override
//                    public NetworkImageHolderView createHolder() {
//                        return new NetworkImageHolderView(ImageView.ScaleType.CENTER_CROP);
//                    }
//                }, networkImages);
//
//                if (map.get("record_list") != null && !"".equals(map.get("record_list").toString())) {
//                    try {
//                        JSONArray array = new JSONArray(map.get("record_list").toString());
//                        int count = array.length();
//                        for (int i = 0; i < count; i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            list.add(JsonUtils.jsonToMap(object.toString()));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
//                if (map.get("time_list") != null && !"".equals(map.get("time_list".toString()))) {
//                    try {
//                        JSONArray array = new JSONArray(map.get("time_list").toString());
//                        int count = array.length();
//                        if (count != 0) {
////                            String txt="电度费\n";
//                            String txt = "", txt_tmp = "";
//                            for (int i = 0; i < count; i++) {
//                                try {
//                                    JSONObject object = array.getJSONObject(i);
//                                    LogUtil.e(i + "当前桩子信息数据" + object.toString());
//                                    txt = "(" + StringUtil.formatHour(object.get("start_time").toString()) + "-" + StringUtil.formatHour(object.get("end_time").toString()) + ")               电费:"
//                                            + new BigDecimal(object.get("charge_price").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/度\n";
//                                    txt_tmp += txt;
//                                    list_gengduo.add(txt);//已经有换行符
////                                list1.add(JsonUtils.jsonToMap(object.toString()));
//                                } catch (Throwable t) {
//                                    t.printStackTrace();
//                                }
//                            }
//                            if (list_gengduo != null) {
//                                try {
//                                    if (list_gengduo.size() >= 2) {
//                                        station_info_diandufei.setText(list_gengduo.get(0).toString() + list_gengduo.get(1).toString());//显示 分时段费用问题
//                                    } else if (list_gengduo.size() == 1) {
//                                        station_info_diandufei.setText(list_gengduo.get(0).toString());
//                                    } else {
//                                        station_info_diandufei.setText(list_gengduo.get(0).toString());
//                                    }
//                                } catch (Throwable t) {
//                                    t.printStackTrace();
//                                }
//                            }
//
//                        }
////                        listView.setAdapter(new DeviceDetailAdapter(StationInfoActivity.this, list1,true));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//                detailBundle = new Bundle();
//                detailBundle.putSerializable("list", (ArrayList) list);
//                parkingBundle = new Bundle();
//                parkingBundle.putString("parking_fee", map.get("parking_fee").toString());
//                parkingBundle.putString("parking_lot", map.get("parking_lot").toString());
//                parkingBundle.putString("direction_area", map.get("direction_area").toString());
//                layout_content.setVisibility(View.VISIBLE);
//                connForComment(flag);
//            }
//        });
    }

    // 桩点评论
    private void connForComment(boolean flag) {

        String url = "api/comment/page/list/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("stationId", station_id);
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("pageIndex", "1");
        map.put("pageNum", "10");

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
                        // json转 map
                        Map jsonToMap = JsonUtils.jsonToMap(s);

//                        try {
//                            radio_comment.setText("评论(" + map.get("total_records").toString() + ")");
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }
                        commentBundle = new Bundle();
                        List dataList = new ArrayList();
                        try {
                            JSONArray array = new JSONArray(jsonToMap.get("commentList").toString());
                            int count = array.length();
                            for (int i = 0; i < count; i++) {
                                JSONObject object = array.getJSONObject(i);
                                dataList.add(JsonUtils.jsonToMap(object.toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        commentBundle.putSerializable("data_list", (ArrayList) dataList);
                        commentBundle.putString("station_id", station_id);
                        setAdapter();
                        setListeners();
                        if (isRefresh) {
                            viewPager.setCurrentItem(curPage);
                            isRefresh = false;
                        }
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });


//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "I102");
//            jsonObject.put("station_id", station_id);
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("cur_page_no", "1");
//            jsonObject.put("page_size", "10");
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new HttpLogic(StationInfoActivity.this).sendRequest(Config.REQUEST_URL, jsonObject, true, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                try {
//                    radio_comment.setText("评论(" + map.get("total_records").toString() + ")");
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//                commentBundle = new Bundle();
//                List dataList = new ArrayList();
//                try {
//                    JSONArray array = new JSONArray(map.get("data_list").toString());
//                    int count = array.length();
//                    for (int i = 0; i < count; i++) {
//                        JSONObject object = array.getJSONObject(i);
//                        dataList.add(JsonUtils.jsonToMap(object.toString()));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                commentBundle.putSerializable("data_list", (ArrayList) dataList);
//                commentBundle.putString("station_id", station_id);
//                setAdapter();
//                setListeners();
//                if (isRefresh) {
//                    viewPager.setCurrentItem(curPage);
//                    isRefresh = false;
//                }
//            }
//        });
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this); //设置定位监听
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置为高精度定位模式
        locationOption.setOnceLocation(true); //设置是否单次定位
        locationClient.setLocationOption(locationOption); //设置定位参数
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
            locationLat = amapLocation.getLatitude();
            locationLng = amapLocation.getLongitude();
            Intent intent = new Intent(this, NaviActivity.class);

            intent.putExtra("current_lat", locationLat + "");
            intent.putExtra("current_lon", locationLng + "");
            intent.putExtra("target_lat", lat + "");
            intent.putExtra("target_lon", lng + "");
//            intent.putExtra("current", new NaviLatLng(Double.valueOf(locationLat), Double.valueOf(locationLng)));
//            intent.putExtra("target", new NaviLatLng(Double.valueOf(lat), Double.valueOf(lng)));
            startActivity(intent);
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            LogUtils.e("AmapErr:" + errText);
            if (NetworkUtils.isConnected(this)) {
                if (amapLocation.getErrorCode() == 12) {
                    ToastUtil.showToast(R.string.toast_permission_location);
                } else {
                    ToastUtil.showToast(getString(R.string.toast_map_location_failed) + amapLocation.getErrorInfo());
                }
            }
        }

    }

    /**
     * 发送请求，收藏或者取消收藏充电站
     */
    private void favorAction() {

        String url = "api/favor/station/operation";
        HashMap<String, Object> map = new HashMap<>();
        if (!favorFlag) {
            //收藏站点
            map.put("userId", UserParams.INSTANCE.getUser_id());
            map.put("favorType", "1");
            map.put("stationId", station_id);
        } else {
            //取消收藏站点
            map.put("userId", UserParams.INSTANCE.getUser_id());
            map.put("favorType", "2");
            map.put("stationId", station_id);
        }

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
                        favorFlag = !favorFlag;
                        if (favorFlag) {
                            ToastUtil.showToast(R.string.toast_D104);
                            right_btn.setImageResource(R.mipmap.ic_collection_yellow);
                            setResult(ConstantCode.RES_OPEN_STATION_INFO, new Intent().putExtra("is_favor", true));
                        } else {
                            ToastUtil.showToast(R.string.toast_D106);
                            right_btn.setImageResource(R.mipmap.ic_title_collection);
                            setResult(ConstantCode.RES_OPEN_STATION_INFO, new Intent().putExtra("is_favor", false));
                        }
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });


//        JSONObject jsonObject = new JSONObject();
//        if (!favorFlag) {//收藏站点
//            try {
//                jsonObject.put("req_code", "D104");
//                jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//                jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//                jsonObject.put("station_id", station_id);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {//取消收藏站点
//            try {
//                jsonObject.put("req_code", "D106");
//                jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//                jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//                jsonObject.put("station_id", station_id);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        HttpLogic httpLogic = new HttpLogic(this);
//        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                favorFlag = !favorFlag;
//                if (favorFlag) {
//                    ToastUtil.showToast(R.string.toast_D104);
//                    right_btn.setImageResource(R.mipmap.ic_collection_yellow);
//                    setResult(ConstantCode.RES_OPEN_STATION_INFO, new Intent().putExtra("is_favor", true));
//                } else {
//                    ToastUtil.showToast(R.string.toast_D106);
//                    right_btn.setImageResource(R.mipmap.ic_title_collection);
//                    setResult(ConstantCode.RES_OPEN_STATION_INFO, new Intent().putExtra("is_favor", false));
//                }
//            }
//        });

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        refresh_layout.setCanPull(false);
        if (Math.abs(distanceY) > 100) { //由于滑动时，distanceY会出现异常值，因此为其设置一个阈值
            return false;
        }
        if (layoutParams.height - (int) distanceY < 0) {
            layoutParams.height = 0;
//            isShow = false;
//            bg_view.setAlpha(1);
//            tv_title.setAlpha(1);
        } else if (layoutParams.height - (int) distanceY > maxHeight) {
            layoutParams.height = maxHeight;
//            isShow = true;
//            bg_view.setAlpha(0);
//            tv_title.setAlpha(0);
        } else {
            layoutParams.height = layoutParams.height - (int) distanceY;
//            isShow = true;
//            float alpha = 1 - (((float) (layoutParams.height - DensityUtil.dip2px(this, 40))) / (maxHeight-DensityUtil.dip2px(this, 40)));
//            bg_view.setAlpha(alpha);
//            tv_title.setAlpha(alpha);
//            LogUtils.d("" + (1 - (((float) (layoutParams.height - DensityUtil.dip2px(this, 40))) / (maxHeight-DensityUtil.dip2px(this, 40)))));
        }
        convenientBanner.setLayoutParams(layoutParams);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            refresh_layout.setRefreshBarVisible(false);
        }
        if (hasFocus && isOnCreate) {
//            LogUtils.d("onWindowFocusChanged");
            isOnCreate = false;
//            LogUtils.d("scrollView.getHeight()"+scrollView.getHeight());
//            LogUtils.d("layout_content.getHeight()"+layout_content.getHeight());
//            viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollView.getHeight()-layout_content.getHeight()+maxHeight-getResources().getDimensionPixelOffset(R.dimen.title_height)));
            viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollView.getHeight() - layout_content.getHeight() + maxHeight));
        }
        isFirst = false;
    }

    @Override
    protected void onResume() {
        convenientBanner.startTurning(5000);
        confirmCanPullRefresh();
        super.onResume();
    }

    private void confirmCanPullRefresh() {
        LogUtils.d("convenientBanner.getHeight() || maxHeight :" + convenientBanner.getHeight() + " || " + maxHeight + " || " + scrollView.getScrollY());
//        initBanner();
        if (convenientBanner != null && convenientBanner.getHeight() < maxHeight) {
            refresh_layout.setCanPull(false);
        }
    }

    @Override
    protected void onPause() {
        convenientBanner.stopTurning();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == ConstantCode.REQ_ZOOM_IMG){
//            setResult(ConstantCode.RES_STATION_INFO);
//            finish();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantCode.CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                ToastUtil.showToast("拨打电话权限受限，请在权限设置中打开该权限");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}