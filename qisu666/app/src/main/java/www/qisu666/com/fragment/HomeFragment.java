package www.qisu666.com.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.badgeview.BGABadgeImageView;
import www.qisu666.com.R;
import www.qisu666.com.activity.BePartnerActivity;
import www.qisu666.com.activity.CaptureActivity;
import www.qisu666.com.activity.CarShareMapActivity;
import www.qisu666.com.activity.ChargingActivity;
import www.qisu666.com.activity.CollectionActivity;
import www.qisu666.com.activity.CommonWebViewActivity;
import www.qisu666.com.activity.LoginActivity;
import www.qisu666.com.activity.MMMMActivity;
import www.qisu666.com.activity.NearbyActivity;
import www.qisu666.com.activity.NearbyStationActivity;
import www.qisu666.com.activity.NotificationActivity;
import www.qisu666.com.activity.NotificationDetailActivity;
import www.qisu666.com.activity.StationMapActivity;
import www.qisu666.com.adapter.JDViewAdapter;
import www.qisu666.com.adapter.NotificationAdapter;
import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.entity.AdverNotice;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.JDAdverView;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.common.utils.VersionUtils;
import www.qisu666.sdk.amap.stationMap.HttpLogic_amap;
import www.qisu666.sdk.amap.stationMap.JsonUtil;
import www.qisu666.sdk.amap.stationMap.StationLocation;
import www.qisu666.sdk.amap.stationMap.juhe.PointAggregationAty;
import www.qisu666.sdk.partner.Activity_CarBuy;
import www.qisu666.sdk.partner.Activity_Invest;
import www.qisu666.sdk.utils.Update;

/**
 * 首页的fragment
 */
public class HomeFragment extends BaseFragment implements AMapLocationListener {

    @BindView(R.id.iv_chengwei)
    ImageView ivChengwei;
    @BindView(R.id.iv_shuoming)
    ImageView ivShuoming;
    Unbinder unbinder;
    @BindView(R.id.ll_index_message)
    RelativeLayout ll_index_message;
    @BindView(R.id.jdadver)
    JDAdverView jdadver;
    protected LoadingDialog mLoadingDialog;//加载中弹框
    private TextView ll_index_message_time;
    private TextView ll_index_message_content;
    private BGABadgeImageView img_home_more;
    private JDViewAdapter jdAdapter;
    private View line_three;

    private List<Map> imgList = new ArrayList();
    private Banner mBanner;
    private List<Map<String, Object>> mLists = new ArrayList<>();
    List<AdverNotice> datas=new ArrayList<>();
    /**
     * 网络加载图片地址
     */
    private String[] images;

    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * 版本号
     */
    private String version;

    /**
     * 定位相关,为了优化地图数据加载速度  每次进入该界面都请求数据
     * 定位用
     */
    double lat = 0.0, lon = 0.0;
    String cityCode = "";
    /**
     * 声明mlocationClient对象
     */
    public AMapLocationClient locationClient;
    /**
     * 声明mLocationOption对象
     */
    public AMapLocationClientOption locationOption = null;

    protected Activity mActivity;


    @Override
    public void onAttach(Context context) {//
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container);
        // 检查版本更新
        connToServerA106();
            unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingDialog = DialogHelper.loadingAletDialog(getActivity(), "正在加载中...");
        connectServer();
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


//        initPartner();

        // 顶部更多按钮
        img_home_more = view.findViewById(R.id.img_home_more);
//        img_home_more.showCirclePointBadge();

        ll_index_message_time=view.findViewById(R.id.ll_index_message_time);
        ll_index_message_content=view.findViewById(R.id.ll_index_message_content);
        // 轮播图
        mBanner = view.findViewById(R.id.banner);
        line_three=view.findViewById(R.id.line_three);

        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);

        // TODO 首页 Fragment 与合伙人相关的两个 ImageView 需要将图片素材进行圆角处理

        View immersionView = getImmersionView(view);

        line_three.getBackground().setAlpha(50);

        return immersionView;
    }

    /**
     * 首页合伙人相关 ImageView 设置图片圆角
     */
    private void initPartner() {
        RoundedBitmapDrawable roundedDrawable_partner = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.cwhhr));
        roundedDrawable_partner.getPaint().setAntiAlias(true);
        roundedDrawable_partner.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        ivChengwei.setImageDrawable(roundedDrawable_partner);

        RoundedBitmapDrawable roundedDrawable_partner_intro = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.hhrsm));
        roundedDrawable_partner_intro.getPaint().setAntiAlias(true);
        roundedDrawable_partner_intro.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        ivShuoming.setImageDrawable(roundedDrawable_partner_intro);
    }


    /**
     * 初始化Banner * @param map
     */
    private void initBanner(Map<String, Object> map) {
//        try {
//            LogUtil.e("获取到的banner" + map.toString());
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
        String dataListStr = map.get("datalist").toString();
        JSONArray array;
        try {
            array = new JSONArray(dataListStr);
            images = new String[array.length()];
            int imageSize = array.length();
            for (int i = 0; i < imageSize; i++) {
                Map m = JsonUtils.jsonToMap(array.get(i).toString());
                images[i] = m.get("imgUrl").toString();
                imgList.add(m);
            }

            // 请求成功之后 显示banner
            Log.e("aaaa", "Arrays.asList(images):" + Arrays.asList(images).size());
            mBanner.setImages(Arrays.asList(images));
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (imgList.size() > 0 && imgList.get(position).get("actUrl").toString().startsWith("http")) {
                        Intent i = new Intent(getActivity(), CommonWebViewActivity.class);
                        i.putExtra("act_url", imgList.get(position).get("actUrl").toString());
                        i.putExtra("act_title", imgList.get(position).get("actTitle").toString());
                        startActivity(i);
                    }
                }
            });
            //必须最后调用的方法，启动轮播图。
            mBanner.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(mLoadingDialog!=null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @OnClick({R.id.tv_kefu, R.id.iv_zhuangdian, R.id.tv_fujin, R.id.tv_zhinan, R.id.iv_share_car, R.id.tv_Car_fu, R.id.tv_zhinan_yong,
            R.id.iv_chengwei, R.id.iv_shuoming, R.id.img_home_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 客服
            case R.id.tv_kefu:
                AlertDialog dialog = new AlertDialog(getActivity(), "呼叫", "取消", "400-7569999", "服务时间：00:00 - 23:59", true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        } else {
                            if (PermissionUtil.checkPermission(getActivity(), ConstantCode.CALL_PHONE, Manifest.permission.CALL_PHONE)) {

                            }
                        }

                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:400-7569999"));
                            startActivity(i);

                        } catch (Exception e) {
                            ToastUtil.showToast("拨打电话权限受限，请在权限设置中打开该权限");
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();


                break;
            // 装点地图
            case R.id.iv_zhuangdian:
                // 桩点地图跳转之前检查权限
                if (PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_MAP, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_MAP, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    startActivity(new Intent(getActivity(), PointAggregationAty.class));
//                    startActivity(new Intent(getActivity(), MMMMActivity.class));
                }
                break;
            // 附近电站
            case R.id.tv_fujin:
//                DialogHelper.alertDialog(mActivity, "建设中，敬请期待");
                if (PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_NEARBY,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_NEARBY,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                    startActivity(new Intent(getActivity(), NearbyStationActivity.class));
                }
                break;
            // 充电指南
            case R.id.tv_zhinan:                       
                if (UserParams.INSTANCE.checkLogin(getActivity())) {
                    Intent i = new Intent(getActivity(), CommonWebViewActivity.class);
                    i.putExtra("act_url", "http://www.qisu666.com/app-cdzn/cdzn.html");
                    i.putExtra("act_title", "充电指南");
                    startActivity(i);
                }   else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            // 用车
            case R.id.iv_share_car:
                // 共享汽车跳转之前检查权限
                if (PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_MAP, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        PermissionUtil.checkPermission(getActivity(), ConstantCode.LOCATION_MAP, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    startActivity(new Intent(getActivity(), CarShareMapActivity.class));
                }

                break;
            //附近车辆
            case R.id.tv_Car_fu:
                Intent nearbyIntent=new Intent(getActivity(), NearbyActivity.class);
                startActivity(nearbyIntent);
//                DialogHelper.alertDialog(mActivity, "建设中，敬请期待");
                break;
            // 用车指南
            case R.id.tv_zhinan_yong:
                if (UserParams.INSTANCE.checkLogin(getActivity())) {
                    Intent i = new Intent(getActivity(), CommonWebViewActivity.class);
                    i.putExtra("act_url", "http://www.qisu666.com/app-yczn/yczn.html");
                    i.putExtra("act_title", "用车指南");
                    startActivity(i);
                }  else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            case R.id.iv_chengwei:
                // 合伙人人
                if (UserParams.INSTANCE.checkLogin(getActivity())) {
                    Intent partnerIntent = new Intent(getActivity(), BePartnerActivity.class);
                    startActivity(partnerIntent);
                }else{
                    getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                }
//                DialogHelper.alertDialog(mActivity, "建设中，敬请期待");
//                if (UserParams.INSTANCE.checkLogin(getActivity())) {
//                    // 条款灰色页
//                    startActivity(new Intent(getActivity(), Activity_Invest.class));
//                }
                break;
            case R.id.iv_shuoming:
                Intent i = new Intent(getActivity(), CommonWebViewActivity.class);
                i.putExtra("act_url", "http://www.qisu666.com/app-hhr/hhr.html");
                i.putExtra("act_title", "合伙人说明");
                startActivity(i);
                break;
            // 首页更多按钮
            case R.id.img_home_more:
                showPopView(view);
                break;
//            case R.id.ll_index_message:
//                if(mLists!=null&&mLists.size()>0&&mLists.get(0).toString()!=null) {
//                    Intent messageIntent = new Intent(getActivity(), NotificationDetailActivity.class);
//                    messageIntent.putExtra("notification", mLists.get(0).toString());
//                    messageIntent.putExtra("position", 0);
//                    getActivity().startActivity(messageIntent);
//                }else{
//                    getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
//                }
//                break;
            default:
                break;
        }
    }

    /**
     * 获取系统消息
     */
    private void connectServer() {
        if(UserParams.INSTANCE.getUser_id()!=null){
            String url = "api/jpush/page/list/query";
            final HashMap<String, Object> map = new HashMap<>();

            map.put("platform", "0");
            map.put("pageIndex", 1 + "");
            map.put("pageNum", "10");
            map.put("userId", UserParams.INSTANCE.getUser_id() + "");

            MyNetwork.getMyApi()
                    .carRequest(url, MyMessageUtils.addBody(map))
                    .map(new FlatFunction<>(Object.class))
                    .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                    .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                        @Override
                        public void onSuccessCode(Message object) {

                        }

                        @Override
                        @SuppressWarnings("unchecked")
                        public void onSuccess(Object bean) {

                            // 对象转json
                            String s = JsonUtils.objectToJson(bean);
                            // json转 list
                            List<String> strings = JsonUtils.jsonToList(s);

                            if (strings != null) {

                                Log.e("aaaa", "rowNum: " + strings.toString());
                                try {
                                    JSONArray array = new JSONArray(strings);
                                    int count = array.length();
                                    if (count == 0) {
                                        DialogHelper.alertDialog(getActivity(), "暂无数据");
                                    } else {
                                        List<Map<String, Object>> listTem = new ArrayList<>();
                                        for(int i=0;i<array.length();i++){
                                            JSONObject object = array.getJSONObject(i);
                                            String oString = object.toString();
                                            LogUtils.e("oString: " + oString);
                                            listTem.add(JsonUtils.jsonToMap(oString));
                                        }
                                        mLists = listTem;
                                        int size=0;
                                        if(mLists.size()>=3){            
                                            size=3;
                                        }else{
                                             size=mLists.size();
                                        }
                                        if(datas.size()==0){
                                            for(int i=0;i<size;i++){
                                                datas.add(new AdverNotice(mLists.get(i).get("title").toString(),mLists.get(i).get("jpushTime").toString().substring(5,10),i+""));
                                            }
                                        }
                                        if(jdAdapter==null){
                                            datas.addAll(datas);
                                            jdAdapter=new JDViewAdapter(datas,getActivity(),mLists);
                                            jdadver.setAdapter(jdAdapter);
                                            jdadver.start();
                                        }
//                                        ll_index_message_content.setText(mLists.get(0).get("title").toString());
//                                        ll_index_message_time.setText(mLists.get(0).get("jpushTime").toString().substring(5,10));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFail(Message<Object> bean) {
                            try {
                                Log.e("asd", "获取我的行程详情失败" + bean.msg);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    });

        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(path).into(imageView);
        } //Glide 加载图片简单用法
    }

    /**
     * 获取弹出菜单
     *
     * @return PopUpView
     */
    private PopupWindow mpWindow;

    private void showPopView(View v) {
        if (mpWindow != null && mpWindow.isShowing()) {
            return;
        }

        // 一个自定义的布局，作为显示的内容
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.home_meun_item, null);
        mpWindow = new PopupWindow(layout,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mpWindow.setTouchable(true);
        mpWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mpWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        // 显示动画
        //mpWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
        //显示某个控件的下方
        mpWindow.showAsDropDown(v);

        TextView tvCd = layout.findViewById(R.id.tv_gongxiang);
        TextView tvGf = layout.findViewById(R.id.tv_guanfang);
        TextView tvXx = layout.findViewById(R.id.tv_xiaoxi);
        View lines_two=layout.findViewById(R.id.lines_two);
        View lines_one=layout.findViewById(R.id.lines_one);

        lines_one.getBackground().setAlpha(150);
        lines_two.getBackground().setAlpha(150);
        tvCd.setOnClickListener(new myListener());
        tvGf.setOnClickListener(new myListener());
        tvXx.setOnClickListener(new myListener());

    }


    class myListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_gongxiang:
                    //共享充电
                    if (UserParams.INSTANCE.checkLogin(getActivity())) {
                        doCharge();
                    }   else{
                        ToastUtil.showToast(R.string.toast_prompt_login);
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                    }
                    break;
                case R.id.tv_guanfang:
                    final String wechatNo = "奇速共享";
                    DialogHelper.confirmDialog(getActivity(), "即将跳转到微信页面，官方微信公众号为“" + wechatNo + "”，点击确认复制并跳转。", new AlertDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                            if (intent != null) {
                                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(wechatNo);
                                ToastUtil.showToast(R.string.toast_prompt_copy);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                ToastUtil.showToast(R.string.toast_app_uninstall_wx);
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    break;
                case R.id.tv_xiaoxi:
                    /*DialogHelper.alertDialog(getActivity(), "建设中，敬请期待");*/
                    if (UserParams.INSTANCE.checkLogin(getActivity())) {
                        startActivity(new Intent(getActivity(), NotificationActivity.class));
                    }   else{
                        ToastUtil.showToast(R.string.toast_prompt_login);
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void doCharge() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (PermissionUtil.cameraIsCanUse()) {
                startActivity(new Intent(getActivity(), CaptureActivity.class));
            } else {
                ToastUtil.showToast(R.string.toast_permission_camera);
            }
        } else {
            if (PermissionUtil.checkPermission(getActivity(), ConstantCode.CAMERA,
                    Manifest.permission.CAMERA)) {
                Log.e("aaa", "asdadsadsadsadasadsadsadsa");
                connToServerE106(true);
            }
        }
    }

    /**
     * 取消预约 / 模拟
     */
    public void cancle_yuyue() {
        String url = "api/dic/query";
        HashMap<String, Object> map = new HashMap<>();
        //maxCancelTimesFullDay
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
                        tmp = result.substring(result.indexOf("dicValue"),
                                result.indexOf("dicValue") + 25);
                        LogUtil.e("获取成功后1 " + tmp);
                        tmp = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(","));
                        LogUtil.e("获取成功后2 " + tmp);
                        if (tmp.equals("")) {
                            DialogHelper.confirmDialog(getActivity(), "每天最多可以取消2次,每次取消需要间隔1小时，确定取消么？",
                                    new AlertDialog.OnDialogButtonClickListener() {
                                        @Override
                                        public void onConfirm() {
                                        }


                                        @Override
                                        public void onCancel() {
                                        }
                                    });
                        } else {
                            DialogHelper.confirmDialog(getActivity(),
                                    "每天最多可以取消" + tmp + "次,每次取消需要间隔1小时，确定取消么？",
                                    new AlertDialog.OnDialogButtonClickListener() {
                                        @Override
                                        public void onConfirm() {
                                        }


                                        @Override
                                        public void onCancel() {
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


    /**
     * 首次优惠卷
     */
    private void firstFace() {
        String url = "api/auth/acquire/coupon";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        //这里直接跳转
                        LogUtil.e("首次用车优惠 请求成功！" + bean.toString());

                    }


                    @Override
                    public void onFail(Message<Object> bean) {
                        LogUtil.e("首次用车优惠 请求失败！" + bean.toString());
                        LogUtil.e("首次用车优惠 请求失败！" + bean.msg);

                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantCode.CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity(), CaptureActivity.class));
                } else {
                    ToastUtil.showToast(R.string.toast_permission_camera);
                }
                break;
            case ConstantCode.LOCATION_MAP:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity(), StationMapActivity.class));
                } else {
                    //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                    ToastUtil.showToast(R.string.toast_permission_location);
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
                break;
            case ConstantCode.LOCATION_NEARBY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity(), NearbyStationActivity.class));
                } else {
                    ToastUtil.showToast(R.string.toast_permission_location);
                }
                break;
            case ConstantCode.LOCATION_COLL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity(), CollectionActivity.class));
                } else {
                    ToastUtil.showToast(R.string.toast_permission_location);
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onResume() {
        super.onResume();
        connectServer();
        LogUtil.e("ChargeStatus:" + ChargeStatus.INSTANCE.getStatus());
        //        if (ChargeStatus.INSTANCE.getStatus() == ChargeStatus.STATUS_CHARGING) {
        //            tv_index_charge.setText(getString(R.string.home_is_charging));
        //        } else if (ChargeStatus.INSTANCE.getStatus() == ChargeStatus.STATUS_FREE || ChargeStatus.INSTANCE.getStatus() == ChargeStatus.STATUS_PREPARE_STOP) {
        //            tv_index_charge.setText(getString(R.string.home_charge_now));
        //        } else if (!TextUtils.isEmpty(UserParams.INSTANCE.getUser_id()) && ChargeStatus.INSTANCE.getStatus() == ChargeStatus.STATUS_UNKNOWN) {
        //            //如果充电状态未知，需要重新发送E106请求，获取用户充电状态
        //            connToServerE106(false);
        //        }
        // 每次进入都进行更新数据 确保数据一直最新
        location();
    }


    /**
     * 发送 A106 请求，获取版本更新
     */
    private void connToServerA106() {

        String url = "api/version/checkVersion";
        version = String.valueOf(VersionUtils.getVersionName(getContext()));

        HashMap<String, Object> map = new HashMap<>();
        map.put("OSType", "android");
        map.put("appVersion", version);

        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        try {
//                             LogUtil.e("获取到 更新信息：" + bean.toString());
                            // 对象转json
                            String s = JsonUtils.objectToJson(bean);
                            // json转 map
                            final Map jsonToMap = JsonUtils.jsonToMap(s);

                            if (VersionUtils.checkVersion(version, jsonToMap.get("minVersion").toString()) >
                                    0) {
                                // 强制更新
                                AlertDialog alertDialog = new AlertDialog(getActivity(),
                                        jsonToMap.get("returnMsg").toString(), "",
                                        getString(R.string.dialog_version_min), false);
                                alertDialog.setCancelable(false);
                                alertDialog.setSampleDialogListener(
                                        new AlertDialog.OnDialogButtonClickListener() {
                                            @Override
                                            @SuppressLint("NewApi")
                                            public void onConfirm() {
                                                new Update(Objects.requireNonNull(getActivity()),
                                                        jsonToMap.get("extraParams").toString(), true);
                                            }


                                            @Override
                                            public void onCancel() {
                                            }
                                        });
                                alertDialog.show();
                            } else if (VersionUtils.checkVersion(version,
                                    jsonToMap.get("latestVersion").toString()) > 0) {
                                //每次提示是否更新
                                DialogHelper.confirmTitleDialog(getActivity(), null,
                                        jsonToMap.get("returnMsg").toString(),
                                        getString(R.string.dialog_version_confirm),
                                        getString(R.string.dialog_version_cancel),
                                        new AlertDialog.OnDialogButtonClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onConfirm() {
                                                new Update(Objects.requireNonNull(getActivity()),
                                                        jsonToMap.get("extraParams").toString(), false);
                                            }


                                            @Override
                                            public void onCancel() {

                                            }
                                        });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            connToServerA110();
                        }

                    }


                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        LogUtil.e("版本更新 请求失败！" + bean.toString());
                        LogUtil.e("版本更新 请求失败！" + bean.msg);

                    }
                });
    }


    /**
     * 请求 banner 数据
     */
    private void connToServerA110() {
        String url = "api/photo/queryphoto";
        HashMap<String, Object> map = new HashMap<>();

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(Object bean) {
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 map
                        Map m = JsonUtils.jsonToMap(s);
                        initBanner(m);
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });
    }


    /**
     * 发送 E106 请求，获取充电状态
     */
    private void connToServerE106(final boolean isOnClick) {

        String url = "api/charge/order/status/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(Object bean) {
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 map
                        Map jsonToMap = JsonUtils.jsonToMap(s);

                        String state = jsonToMap.get("orderStatus").toString();
//                        if (ChargeStatus.INSTANCE.getStatus() != ChargeStatus.STATUS_PREPARE_STOP) {
//                            if ("02".equals(state)) {
//                                ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_CHARGING);
//                                // tv_index_charge.setText(R.string.home_is_charging);
//                            } else if ("03".equals(state)) {
//                                ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
//                                // tv_index_charge.setText(R.string.home_charge_now);
//                            } else {
//                                ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_FREE);
//                                // tv_index_charge.setText(R.string.home_charge_now);
//                            }
//
//
//                        }
                        //                out_trade_no = map.get("out_trade_no").toString();

                        if (isOnClick) {
                            if ("02".equals(state)) {
                                //todo
                                // 充电页面
                                UserParams.INSTANCE.setOut_trade_no(jsonToMap.get("outTradeNo").toString());
                                Log.e("aaaa", "outTradeNo" + jsonToMap.get("outTradeNo").toString());
                                Log.e("aaaa", "INSTANCE" + UserParams.INSTANCE.getOut_trade_no());

                                startActivity(new Intent(getActivity(), ChargingActivity.class));
                            } else {
                                // 扫码充电
                                startActivity(new Intent(getActivity(), CaptureActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });
    }


    /**
     * 测试定位,获取经纬度  每次进入先进行加载更新数据
     */
    private void location() {
        locationClient = new AMapLocationClient(getContext());
        //初始化定位参数
        locationOption = new AMapLocationClientOption();
        //设置定位间隔ms
        //        locationOption.setInterval(2000);
        //只定位一次
        locationOption.setOnceLocation(true);
        //高精度定位
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(this);
        //开始定位
        locationClient.startLocation();
    }


    /**
     * 发送D109请求,获取地图附近30km的数据
     */
    public void test_D109(String distance) {
        LogUtil.e("定位成功后  开始请求缓存数据---");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "D109");
            jsonObject.put("distance", distance);

            // lat
            jsonObject.put("latitude", lat + "");
            // lon
            jsonObject.put("longitude", lon + "");

            // 城市编码
            jsonObject.put("area_code", cityCode);
            // 充电接口 0国标  1特斯拉
            jsonObject.put("charge_interface", "");
            // 0自营 1非自营
            jsonObject.put("self_support", "");
            // 充电方式
            jsonObject.put("charge_method", "");
            // 公共桩  0公共    1个人
            jsonObject.put("charge_pile_bel", "");
            // 是否免费停车  0免费停车   空值为所有的
            jsonObject.put("parking_free", "");
            // 服务时间
            jsonObject.put("service_time", "");
            // 用户id
            jsonObject.put("user_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpLogic_amap httpLogic_amap = new HttpLogic_amap(getActivity());
        httpLogic_amap.sendRequest(Config.REQUEST_URL, jsonObject, true,
                new AbstractResponseCallBack() {
                    @Override
                    public void onResponse(Map<String, Object> map, String tag) {
                        // LogUtil.e(tag+"返回结果tag:"+tag);
                        // LogUtil.e(tag+"返回结果:"+map.toString());
                        // LogUtil.e(tag+"返回结果:"+map.get("record_list"));
                        // List <StationLocation> sList = new ArrayList<StationLocation>();
                        try {
                            JSONArray array = new JSONArray(map.get("record_list").toString());
                            int count = array.length();
                            LogUtil.e(tag + "返回结果-----数据大小----" + count);
                            if (count == 0) {
                                // 没有数据
                                LogUtil.e(tag + "返回结果-----没有数据");
                            } else {
                                // LogUtil.e(tag+"返回结果-----有数据----");
                                for (int i = 0; i < count; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    StationLocation stationLocation = JsonUtil.parse(object.toString(), StationLocation.class);
                                    // double latt=  Double.parseDouble(stationLocation.latitude)+ Config.OFF_LAT;
                                    // double lngg=  Double.parseDouble(stationLocation.longitude)+ Config.OFF_LNG;
                                    double latt = Double.parseDouble(stationLocation.latitude);
                                    double lngg = Double.parseDouble(stationLocation.longitude);

                                    // LogUtil.e(tag+i+"-每个对象station id: " + stationLocation.getStation_id()+",lat："+latt+",lng="+lngg);
                                    stationLocation.latitude = latt + "";
                                    stationLocation.longitude = lngg + "";
                                    try {
                                        // 保存数据库中
                                        IDianNiuApp.getInstance().db.saveOrUpdate(stationLocation);
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                    // LogUtil.e(tag+"0每个对象: " + object.toString());
                                    // String oString = object.toString();
                                    // LogUtil.e(tag+"每个对象: " + oString);
                                    // // 添加桩子
                                    // list.add(JsonUtils.jsonToMap(oString));
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }

                    }
                });

    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
//        LogUtil.e("定位成功");
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                // 获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLocationType();
                // 获取纬度
                amapLocation.getLatitude();
                // 获取经度
                amapLocation.getLongitude();
                // 获取精度信息
                amapLocation.getAccuracy();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                // 定位时间
                df.format(date);
//                LogUtil.e("定位成功 来源 " + amapLocation.getLocationType());
//                LogUtil.e("定位成功 纬度 " + amapLocation.getLatitude());
//                LogUtil.e("定位成功 经度 " + amapLocation.getLongitude());
//                LogUtil.e("定位成功 精度 " + amapLocation.getAccuracy());
//                LogUtil.e("定位成功 时间 " + df.format(date));
                // 城市编码
//                LogUtil.e("定位成功 时间 " + amapLocation.getCityCode());
                cityCode = amapLocation.getCityCode();
                lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();
                // 两个请求并行
//                test_D109("30");
//                test_D109("1000");
            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtil.e("定位失败AmapError" + "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                lat = 22.561583831813046;
                lon = 113.97497656885412;
                cityCode = "5801";
//                test_D109("1000");
            }
        }
    }

    //如果你需要考虑更好的体验，可以这么操作

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //取消所有的地图预加载请求
        IDianNiuApp.cancelAllRequests("D109");
        //结束轮播
        mBanner.stopAutoPlay();
    }
}
