package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.carshare.utils.TabEntity;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.live.LiveResultActivity;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.model.CarConfirmBean;
import www.qisu666.com.model.CarNowOrderBean;
import www.qisu666.com.model.CarPolicyBean;
import www.qisu666.com.model.CarPolicyBeanNew;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.network.ResultCode;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.PrefUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.MyRadioGroup;
import www.qisu666.common.activity.BaseProgressActivity;
import www.qisu666.common.constant.Constant;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.carshare.bean.Bean_deposit;

/**
 * 确认用车界面
 */
public class CarShareConfirmActivity extends BaseProgressActivity {

    @BindView(R.id.rb_car_split_hour)
    RadioButton rbCarSplitHour;
    @BindView(R.id.rb_car_split_day)
    RadioButton rbCarSplitDay;
    @BindView(R.id.rg_car_out)
    MyRadioGroup rgCarOut;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tab_car_share)
    CommonTabLayout tabCarShare;
    @BindView(R.id.tv_confirm_car_num)
    TextView tvConfirmCarNum;
    @BindView(R.id.tv_confirm_ele_level)
    TextView tvConfirmEleLevel;
    @BindView(R.id.tv_confirm_mile)
    TextView tvConfirmMile;
    @BindView(R.id.tv_confirm_car_model)
    TextView tvConfirmCarModel;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.img_confirm_car)
    ImageView imgConfirmCar;
    @BindView(R.id.ll_confirm_driver)
    LinearLayout llConfirmDriver;
    @BindView(R.id.top_linear)
    LinearLayout top_linear;
    @BindView(R.id.ll_ll)
    LinearLayout ll_ll;
    @BindView(R.id.time_money_tx)
    TextView time_money_tx;
    @BindView(R.id.electricityCharge_tx)
    TextView electricityCharge_tx;
    @BindView(R.id.chargingByMileage_tx)
    TextView chargingByMileage_tx;
    @BindView(R.id.time_relative)
    RelativeLayout time_relative;

    /**
     * 自驾租车 日套餐txt
     */
    @BindView(R.id.tv_order_type_tip)
    TextView tv_order_type_tip;

    @BindView(R.id.tv_order_day)
    TextView tv_order_day;
    /**
     * 司机费用txt
     */
    @BindView(R.id.confirm_car_type_zijia_driverfee)
    TextView confirm_car_type_zijia_driverfee;


    @BindView(R.id.minute_img)
    ImageView minute_img;
    @BindView(R.id.day_img)
    ImageView day_img;
    @BindView(R.id.triangles_icon)
    ImageView triangles_icon;

    /**
     * 分时租车的更多按钮
     */
    @BindView(R.id.confirm_car_type_zijia_fenshizuche_gengduo)
    ImageView confirm_car_type_zijia_fenshizuche_gengduo;
    /**
     * 分时租车txt
     */
    @BindView(R.id.confirm_car_type_zijia_fenshizuche)
    TextView confirm_car_type_zijia_fenshizuche;
    /**
     * 分时租车的更多按钮
     */
    @BindView(R.id.confirm_car_type_zijia_fenshizulin_gengduo)
    ImageView confirm_car_type_zijia_fenshizulin_gengduo;
    /**
     * 分时租赁txt
     */
    @BindView(R.id.confirm_car_type_zijia_fenshizulin)
    TextView confirm_car_type_zijia_fenshizulin;

    /**
     * 记录计费规则的bean
     */
    CarPolicyBeanNew carPolicyBeanNew = null;


    private Context mContent;

    private String[] mTitles = {"自驾租车"};

    private int selectType;

    private final int TPYE_HOUR = 0;
    private final int TPYE_DAY = 1;

    private CarBean carBean;

    String carCode = "";

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
//        connToServer(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 分时租车
     */
    boolean is_fenshizuche_gengduo = false;


    private void requestNet() {
        // 获取计费规则
        getBillingRules();
    }

    private void initTitleBar() {
        // 用车界面
        tvTitle.setText("确认用车");
    }

    /**
     * 分时租赁
     */
    boolean is_fenshizulin_gengduo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_confirm);
        mContent = this;
        initTitleBar();
        String result = ActivityUtil.getOneExtra(getIntent());
        if (!TextUtils.isEmpty(result)) {
            carBean = new Gson().fromJson(result, CarBean.class);
            LogUtil.e("接收到的carCode：" + carBean.carCode);
            LogUtil.e("接收到的carCode：" + carCode);
            initCarView(carBean);
        }
        //初始化界面
        initViews();
        requestNet();
//        requestPolicy(0);
//        initMapPopupWindow();
//        connToServer(true);
        EventBus.getDefault().register(this);
    }

    @SuppressLint("SetTextI18n")
    private void initCarView(CarBean carBean) {
        try {
            tvConfirmCarModel.setText(carBean.getCarBrandModels().brandName + " " + carBean.getCarBrandModels().modelNumber);
        } catch (Throwable t) {
            t.printStackTrace();
            tvConfirmCarModel.setText(carBean.brandName + " " + carBean.modelNumber);
        }//车型
        tvConfirmCarNum.setText(carBean.plateNumber);
        tvConfirmEleLevel.setText("\t"+carBean.oddPowerForNE + "%");

        tvConfirmMile.setText(carBean.oddMileage + "km");
        Picasso.with(mContent).load(StringUtil.addImageHost(carBean.carImgPath)).placeholder(R.mipmap.yc_52).into(imgConfirmCar);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        ll_ll.setBackgroundColor(Color.WHITE);
        top_linear.setBackground(getResources().getDrawable(R.drawable.white_shape));
        tvTitle.setText("确认用车");
        rgCarOut.setCheckWithoutNotif(R.id.rb_car_split_hour);
        rgCarOut.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_car_split_hour:
                        day_img.setImageResource(R.mipmap.yc_17);
                        minute_img.setImageResource(R.mipmap.yc_18);
                        selectType = TPYE_HOUR;
                        break;
                    case R.id.rb_car_split_day:
                        day_img.setImageResource(R.mipmap.yc_18);
                        minute_img.setImageResource(R.mipmap.yc_17);
                        selectType = TPYE_DAY;
                        break;
                    default:
                        break;
                }
            }
        });

        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (String text : mTitles) {
            mTabEntities.add(new TabEntity(text));
        }
        tabCarShare.setTabData(mTabEntities);
        tabCarShare.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                switch (i) {
                    case 0:
//                        requestPolicy(0);
                        getBillingRules();
                        btnSubmit.setText("确认用车(预约保留30分钟)");
                        rgCarOut.setVisibility(View.VISIBLE);
                        llConfirmDriver.setVisibility(View.GONE);
                        // hourSelected();
                        break;
                    case 1:
                        if (true) {
                            return;
                        }//代驾 注释掉
//                        requestPolicy(1);
                        getBillingRules();
                        btnSubmit.setText("确认代驾(预约保留30分钟)");
                        rgCarOut.setVisibility(View.GONE);
                        llConfirmDriver.setVisibility(View.VISIBLE);
                        // hourSelected();
                        // 代驾页面初始值
                        String txt = "";
                        if (carPolicyBeanNew == null) {
                            ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                            return;
                        }
                        // 先置空
                        confirm_car_type_zijia_fenshizulin.setText(txt);

                        int count = carPolicyBeanNew.policyList.size();
                        txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                        for (int a = 0; a < 2; a++) {
                            txt += "（" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ","
                                    + new BigDecimal(carPolicyBeanNew.chargingByMileage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里+"
                                    + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟)\n";
                        }
                        confirm_car_type_zijia_fenshizulin.setText(txt);
                        String one = "10公里以内费用:" + Integer.parseInt(carPolicyBeanNew.driverPolicy1) / 100 + "元;\n";
                        String two = "10-20公里费用:" + Integer.parseInt(carPolicyBeanNew.driverPolicy2) / 100 + "元;\n";
                        String three = "超过20公里部分:" + Integer.parseInt(carPolicyBeanNew.driverPolicy3) / 100 + "元/公里;";
                        // 司机费用
                        confirm_car_type_zijia_driverfee.setText(one + two + three);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
    }

    private void requestPolicy(int type) {
        //type 0:分时,1:按日
        //选择租车时计费规则
        String url = "api/policy/driver/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("cityCode", "SZ");
        map.put("driverType", type);
        map.put("modelNumber", "IEV4");
        map.put("seatNum", "5");
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarPolicyBean.class))
                .compose(RxNetHelper.<CarPolicyBean>io_main())
                .subscribe(new ResultSubscriber<CarPolicyBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarPolicyBean bean) {
//                        LogUtils.i("租车时 计费规则"+bean.policyList.size());
//                        String text = bean.policyList.get(0).timeType;
//                        ToastUtil.showToast(text);
                    }

                    @Override
                    public void onFail(Message<CarPolicyBean> bean) {

                    }

                });
    }

    /**
     * type 0:分时,1:按日
     *
     * @param driverType 0:自驾，1:代驾
     */
    private void requestBook(final int driverType) {
        //网点预约租车
        String url = "api/tss/order/book";
        HashMap<String, Object> map = new HashMap<>();
        map.put("carCode", carBean.carCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("city", "SZ");
        map.put("driverType", driverType + "");
        map.put("borrowType", selectType + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarNowOrderBean.class))
                .compose(RxNetHelper.<CarNowOrderBean>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<CarNowOrderBean>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {
                        
                    }

                    @Override
                    public void onSuccess(CarNowOrderBean bean) {
                        if (!TextUtils.isEmpty(bean.carCode)) {
                            PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, bean.carCode);
                        }
                        //todo
                        Log.e("asd", PrefUtil.getString(mContext, PrefUtil.CAR_CODE));
                        if (!TextUtils.isEmpty(bean.orderCode)) {
                            PrefUtil.saveString(mContext, PrefUtil.ORDER_CODE, bean.orderCode);
                        }
                        PrefUtil.saveString(mContext, PrefUtil.DRIVER_TYPE, driverType + "");
                        // 发送到上个页面,更新ui
                        EventBus.getDefault().post("预约成功");
                        LogUtil.e("进行跳转 预约成功-----");
                        ActivityUtil.startActivity(mContent, CarShareMapActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(Message<CarNowOrderBean> bean) {
                        //用户不存在 跳转到用户认证页面
                        LogUtils.i("请求自驾失败------" + bean.msg);
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    @OnClick({R.id.time_relative,R.id.img_title_left, R.id.ll_car_split_hour, R.id.ll_car_split_day, R.id.btn_submit, R.id.confirm_car_type_zijia_fenshizuche_gengduo, R.id.confirm_car_type_zijia_fenshizulin_gengduo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_relative:
                if(triangles_icon.getTag()==null||triangles_icon.getTag().toString().equals("close")){
                    triangles_icon.setImageResource(R.mipmap.triangles);
                    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_RIGHT);
                    time_money_tx.setLayoutParams(layoutParams);
                    triangles_icon.setTag("expan");
//                    time_money_tx.setGravity(Gravity.RIGHT);
                }else{
                    triangles_icon.setTag("close");
                    triangles_icon.setImageResource(R.mipmap.triangless);
                    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_20dp));
                    layoutParams.addRule(RelativeLayout.ALIGN_RIGHT);
                    time_money_tx.setLayoutParams(layoutParams);
//                    time_money_tx.setGravity(Gravity.RIGHT);
                }
                break;
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.ll_car_split_hour:
                hourSelected();
                break;
            case R.id.ll_car_split_day:
                daySelected();
                break;
            //确认用车 按钮
            case R.id.btn_submit:
                // 用车逻辑
//                int driverType = tabCarShare.getCurrentTab();
                // 判断用户身份证认证 / 驾驶证认证 / 押金认证，并进行跳转  放在判断押金状态后面
                requestUserConfirm();
                // 这里跳过 / 确认用车 身份认证
//                requestBook(driverType);
                break;
            case R.id.confirm_car_type_zijia_fenshizuche_gengduo:
                LogUtil.e("点击 分时租车 更多:");
                if (carPolicyBeanNew == null) {
                    ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                    return;
                }
                if (!is_fenshizuche_gengduo) {
                    Picasso.with(this).load(R.mipmap.gengduo2).fit().noFade().centerInside().into(confirm_car_type_zijia_fenshizuche_gengduo);
                    String txt = "";
                    confirm_car_type_zijia_fenshizuche.setText(txt);//先置空
                    int count = carPolicyBeanNew.policyList.size();
                    txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                    for (int a = 0; a < count; a++) {
                        txt += "（" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ","
                                + new BigDecimal(carPolicyBeanNew.chargingByMileage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里+"
                                + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟)\n";
                    }
                    confirm_car_type_zijia_fenshizuche.setText(txt);
                } else {
                    Picasso.with(this).load(R.mipmap.gengduo1).fit().noFade().centerInside().into(confirm_car_type_zijia_fenshizuche_gengduo);
                    String txt = "";
                    if (carPolicyBeanNew == null) {
                        ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                        return;
                    }
                    confirm_car_type_zijia_fenshizuche.setText(txt);//先置空

                    int count = carPolicyBeanNew.policyList.size();
                    txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                    for (int a = 0; a < 2; a++) {
                        txt += "（" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ","
                                + new BigDecimal(carPolicyBeanNew.chargingByMileage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里+"
                                + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟)\n";
                    }
                    confirm_car_type_zijia_fenshizuche.setText(txt);
                }
                // 改变租车状态
                is_fenshizuche_gengduo = !is_fenshizuche_gengduo;
                break;
            case R.id.confirm_car_type_zijia_fenshizulin_gengduo:
                LogUtil.e("点击 分时租赁 更多:");
                if (!is_fenshizulin_gengduo) {
                    Picasso.with(this).load(R.mipmap.gengduo2).fit().noFade().centerInside().into(confirm_car_type_zijia_fenshizulin_gengduo);

                    String txt = "";
                    if (carPolicyBeanNew == null) {
                        ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                        return;
                    }
                    // 先置空
                    confirm_car_type_zijia_fenshizulin.setText(txt);

                    int count = carPolicyBeanNew.policyList.size();
                    txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                    for (int a = 0; a < count; a++) {
                        try {
                            txt += "（" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ","
                                    + new BigDecimal(carPolicyBeanNew.chargingByMileage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里+"
                                    + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟)\n";
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    confirm_car_type_zijia_fenshizulin.setText(txt);
                    // 不足24小时按1天计算。
                    String ritaocan = carPolicyBeanNew.dayCost + "";
                    tv_order_day.setText(ritaocan);
                } else {
                    Picasso.with(this).load(R.mipmap.gengduo1).fit().noFade().centerInside().into(confirm_car_type_zijia_fenshizulin_gengduo);

                    String txt = "";
                    if (carPolicyBeanNew == null) {
                        ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                        return;
                    }
                    // 先置空
                    confirm_car_type_zijia_fenshizulin.setText(txt);

                    int count = carPolicyBeanNew.policyList.size();
                    txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                    for (int a = 0; a < 2; a++) {
                        try {
                            txt += "（" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ","
                                    + new BigDecimal(carPolicyBeanNew.chargingByMileage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里+"
                                    + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟)\n";
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    confirm_car_type_zijia_fenshizulin.setText(txt);
                }
                // 改变租赁状态
                is_fenshizulin_gengduo = !is_fenshizulin_gengduo;
                break;
            default:
                break;
        }
    }


    /**
     * 活体验证成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFaceEvent(String event) {
        if ("活体验证成功".equals(event)) {
            // 设置 Pin 码与手势密码
            ActivityUtil.startActivity(CarShareConfirmActivity.this, GestureSettingActivity.class);
            return;
            // TODO 2018.06.15 15:27
            // firstFace();
        }
        if ("密码验证成功".equals(event)) {
            // 开始用车
            int deliverType = tabCarShare.getCurrentTab();
            Log.e("aaaa", "tabCarShare.getCurrentTab():" + tabCarShare.getCurrentTab());
            requestBook(deliverType);
            return;
        }
        if("数字密码设置完成".equals(event)){
            int deliverType = tabCarShare.getCurrentTab();
            Log.e("aaaa", "tabCarShare.getCurrentTab():" + tabCarShare.getCurrentTab());
            requestBook(deliverType);
            return;
        }

    }

    /**
     * 身份认证判断
     * type 0:分时,1:按日
     */
    private void requestUserConfirm() {
        //网点预约租车
        String url = "api/auth/info/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id() + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarConfirmBean.class))
                .compose(RxNetHelper.<CarConfirmBean>io_main())
                .subscribe(new ResultSubscriber<CarConfirmBean>() {
                    @Override
                    public void onSuccessCode(Message message) {
                        if(message.code==ResultCode.U_AUTH_IDCARD_FAILD){
                            ToastUtil.showToast(message.msg);
                        }

                        if(message.code==ResultCode.U_AUTH_DRIVER_FAILD){
                            ToastUtil.showToast(message.msg);
                        }
                    }

                    @Override
                    public void onSuccess(CarConfirmBean bean) {
                        Log.e("x_log请求结果", "当前身份认证 身份证" + bean.idcardIsAuth);
                        Log.e("x_log请求结果", "当前身份认证 驾驶证" + bean.licenseIsAuth);
                        Log.e("x_log请求结果", "aaaa" + bean.gesturesStatus);

                        // 身份证已认证
                        if (bean.idcardIsAuth == 1) {
                            // 身份认证逻辑
                            // 驾驶证已认证
                            if (bean.licenseIsAuth == 1) {
                                // 押金缴纳状态
                                getChongzhi(bean);
                                return;
                            } else {
                                // 驾驶证认证
                                ActivityUtil.startActivity(mContext, CarShareConfirmDriverActivity.class);
                                return;
                            }
                        } else {
                            //进行身份证认证
                            ActivityUtil.startActivity(mContext, CarShareConfirmIdentityActivity.class);
                            CarShareConfirmActivity.this.finish();
                        }
                        int driverType = tabCarShare.getCurrentTab();
                        Log.e("aaaa", "driverType:" + tabCarShare.getCurrentTab());
                        try {
                            requestBook(driverType);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Message<CarConfirmBean> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaa", "nimabi:" + bean.msg);
                        LogUtil.e("进入登陆超时");
                        if (bean.code == ResultCode.U_PUB_PHONE_ERROR) {
                            // 重新登陆
                            LogUtil.e("进入登陆超时");
                            UserParams.INSTANCE.clear();
                            UserParams.INSTANCE.checkLogin(CarShareConfirmActivity.this);
                        }

                        // 身份证未认证
                        if (bean.code == ResultCode.U_AUTH_IDCARD_NO) {
                            ActivityUtil.startActivity(mContext, CarShareConfirmIdentityActivity.class);
                        }
                    }
                });
    }

    private void hourSelected() {
        selectType = TPYE_HOUR;
        rbCarSplitHour.setChecked(true);
    }

    private void daySelected() {
        selectType = TPYE_DAY;
        rbCarSplitDay.setChecked(true);
    }

    /**
     * 新添加接口  获取计费规则
     */
    private void getBillingRules() {
        LogUtil.e("计费规则接口！");
        try {
            carCode = (String) SPUtil.get(mContext, "carCode", "");
        } catch (Throwable t) {
            t.printStackTrace();
            carCode = "";
        }
        String url = "api/policy/driver/query";
        HashMap<String, Object> map = new HashMap<>();
        // 使用获取到的carCode
        map.put("carCode", carCode);

        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarPolicyBeanNew.class))
                .compose(RxNetHelper.<CarPolicyBeanNew>io_main())
                .subscribe(new ResultSubscriber<CarPolicyBeanNew>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarPolicyBeanNew bean) {
                        carPolicyBeanNew = bean;

                        x.task().autoPost(new Runnable() {
                            // 默认更新一下电度费
                            @Override
                            public void run() {
                                String txt = "";
                                if (carPolicyBeanNew == null) {
                                    ToastUtil.showToast("计费规则加载失败,请检查网络并重试!");
                                    return;
                                }

                                chargingByMileage_tx.setText(carPolicyBeanNew.chargingByMileage+"元/公里");
                                electricityCharge_tx.setText(carPolicyBeanNew.electricityCharge+"元/公里");

                                // 先置空
                                confirm_car_type_zijia_fenshizuche.setText(txt);
                                int count = carPolicyBeanNew.policyList.size();
//                                txt = "电度费：" + new BigDecimal(carPolicyBeanNew.electricityCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/公里\n";
                                for (int a = 0; a < count; a++) {
                                    txt += "(" + carPolicyBeanNew.policyList.get(a).getTimeBegin() + "-" + carPolicyBeanNew.policyList.get(a).getTimeEnd() + ")"
                                            + new BigDecimal(carPolicyBeanNew.policyList.get(a).getChargePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元/分钟\n\n";
                                }
                                time_money_tx.setText(txt);
                                confirm_car_type_zijia_fenshizuche.setText(txt);
                                String ritaocan = carPolicyBeanNew.dayCost + "";
                                try {
                                    SPUtil.put(mContext, "Fee2Day", "" + carPolicyBeanNew.dayCost);
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }//日租价格
                                tv_order_day.setText(ritaocan);
                            }
                        });

                        LogUtil.e("计费接口请求成功！" + bean.toString());
                        LogUtil.e("计费接口请求成功！" + bean.carCode);
                        LogUtil.e("计费接口请求成功！" + bean.miniConsum);
                        LogUtil.e("计费接口请求成功！" + bean.dayCost);
                        LogUtil.e("计费接口请求成功！" + bean.driverPolicy1);
                        LogUtil.e("计费接口请求成功！" + bean.driverPolicy2);
                        LogUtil.e("计费接口请求成功！" + bean.driverPolicy3);
                    }

                    @Override
                    public void onFail(Message<CarPolicyBeanNew> bean) {
                        ToastUtil.showToast(bean.msg);
                        LogUtil.e("计费接口请求失败！" + bean.toString());
                        if (bean.code != 1010) {
                            ToastUtil.showToast("请求失败,请检查网络并重试！");
                        }
                    }
                });
    }


    /**
     * 新添加的 首次人脸识别 20元优惠卷
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
                        LogUtil.e("首次用车优惠 请求成功！" + bean.toString());

                        AlertDialog alertDialog = new AlertDialog(CarShareConfirmActivity.this, "", "", "首次用车,赠送20元优惠卷！", false);
                        alertDialog.setCancelable(false);
                        alertDialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                            @Override
                            public void onConfirm() {
                                // 用车逻辑
                                requestBook(0);
                            }

                            @Override
                            public void onCancel() {
                                // 用车逻辑
                                requestBook(0);
                            }
                        });
                        alertDialog.show();

                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        LogUtil.e("首次用车优惠 请求失败！" + bean.toString());
                        // 用车逻辑
                        requestBook(0);
                    }
                });
    }


    /**
     * 获取充值
     */
    private void getChongzhi(final CarConfirmBean bean) {

        final String url = "api/user/user/deposit";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        requestMap.put("carCode", carBean.carCode);
        Log.e("asd", "............" + carBean.carCode);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Bean_deposit.class))
                .compose(RxNetHelper.<Bean_deposit>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Bean_deposit>(mLoadingDialog) {
                    // 传递上级信息便于判断
                    CarConfirmBean carConfirmBean = bean;

                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Bean_deposit bean) {
                        // 本地存储 Pin 码及手势密码
                        SPUtil.put(getApplicationContext(), Constant.DIGITAL_STATUS_KEY, carConfirmBean.digitalStatus == null ? 2 : carConfirmBean.digitalStatus);
                        SPUtil.put(getApplicationContext(), Constant.DIGITAL_PWD_KEY, carConfirmBean.digitalPassword == null ? "" : carConfirmBean.digitalPassword);
                        SPUtil.put(getApplicationContext(), Constant.GESTURE_STATUS_KEY, carConfirmBean.gesturesStatus == null ? 2 : carConfirmBean.digitalStatus);
                        if (carConfirmBean.gesturesPassword != null) {
                            final List<Integer> intList = new ArrayList<>();
                            for (int i = 0; i < carConfirmBean.gesturesPassword.length(); i++) {
                                //先由字符串转换成char,再转换成String,然后Integer
                                int item = Integer.parseInt(String.valueOf(carConfirmBean.gesturesPassword.charAt(i)));
                                intList.add(item);
                            }
                            SPUtil.put(getApplicationContext(), Constant.GESTURE_PWD_KEY, intList.toString());
                            Log.e("asd", "手势密码:" + intList.toString());
                        }
                        switch (bean.getDespositStatus()) {
                            case "1":
                                // 跳转到押金充值界面
                                Intent it = new Intent(mContext, CarShareDepositActivity.class);
                                it.putExtra("carCode", carBean.carCode);
                                startActivity(it);
//                                ActivityUtil.startActivity(mContext, CarShareDepositActivity.class);
                                break;
                            case "2":
                                ToastUtil.showToast("当前押金退还在审核中");
                                break;
                            case "3":
//                                ActivityUtil.startActivity(mContext, PhoneCheckActivity.class);
//                                 如果 Pin 码与手势密码都已经设置则让用户选择使用哪种验证方式
                                if(Constant.NOT_SET.equals(carConfirmBean.digitalStatus)){
                                    ActivityUtil.startActivity(mContext, PhoneCheckActivity.class);
                                }else{
                                    if (Constant.SET.equals(carConfirmBean.gesturesStatus)&&Constant.SET.equals(carConfirmBean.digitalStatus)) {
                                        // 验证数字密码界面
                                        ActivityUtil.startActivity(mContext, GestureCheckActivity.class);
                                    }else {
                                        ActivityUtil.startActivity(mContext, PinCheckActivity.class);
                                    }
                                }
                                break;
                            case "4":
                                // 跳转到押金充值界面
                                it = new Intent(mContext, CarShareDepositActivity.class);
                                it.putExtra("carCode", carBean.carCode);
                                startActivity(it);
                                break;
                            default:
                                break;
                        }

//                        if ("0".equals(bean.getReturn_stauts())) {
//                            // 正常
////                            LogUtil.e("押金缴纳状态：已缴纳");
//                            // 如果 Pin 码与手势密码都已经设置则让用户选择使用哪种验证方式
//                            if (Constant.SET.equals(carConfirmBean.digitalStatus)) {
//                                // 验证数字密码界面
//                                ActivityUtil.startActivity(mContext, PinCheckActivity.class);
//                            }
//                            if (Constant.NOT_SET.equals(carConfirmBean.digitalStatus) && Constant.NOT_SET.equals(carConfirmBean.gesturesStatus)) {
//                                // 进行人脸识别
//                                ActivityUtil.startActivity(mContext, LiveResultActivity.class);
//                            }
//                        } else {
//                            LogUtil.e("押金缴纳状态：退款");
//                            ToastUtil.showToast("押金未缴纳！");
//                        }
                    }

                    @Override
                    public void onFail(Message<Bean_deposit> bean) {
                        LogUtil.e("押金缴纳状态 失败：" + bean.msg);
                        // 跳转到押金充值界面
                        ActivityUtil.startActivity(mContext, CarShareDepositActivity.class);
                    }
                });
    }
}
