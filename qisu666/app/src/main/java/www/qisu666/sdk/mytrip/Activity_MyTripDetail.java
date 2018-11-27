package www.qisu666.sdk.mytrip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlreadyPromo;
import www.qisu666.sdk.mytrip.bean.Bean_TripDetail;

/**
 * 717219917@qq.com  2016/12/14 0:26
 */
public class Activity_MyTripDetail extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    //    @BindView(R.id.mytripdetail_createtime)
//    TextView mytripdetail_createtime;  //预约时间
//    @BindView(R.id.mytripdetail_canceltime)
//    TextView mytripdetail_canceltime;  //取消时间
//    @BindView(R.id.mytripdetail_cancelmoney)
//    TextView mytripdetail_cancelmoney;//取消费用
//    @BindView(R.id.mytripdetail_deductmoney)
//    TextView mytripdetail_deductmoney;//抵扣金额
//    @BindView(R.id.mytripdetail_paymoney)
//    TextView mytripdetail_paymoney;      //实付款
    @BindView(R.id.mytripdetail_tips)
    TextView mytripdetail_tips;

    //    @BindView(R.id.mytripdetail_createtime2)
//    TextView mytripdetail_createtime2;  //预约时间
    @BindView(R.id.mytripdetail_canceltime2)
    TextView mytripdetail_canceltime2;  //取消时间
    @BindView(R.id.mytripdetail_cancelmoney2)
    TextView mytripdetail_cancelmoney2;//取消费用
    //    @BindView(R.id.mytripdetail_deductmoney2)
//    TextView mytripdetail_deductmoney2;//抵扣金额
    @BindView(R.id.mytripdetail_paymoney2)
    TextView mytripdetail_paymoney2;      //实付款
    @BindView(R.id.mytripdetail_orderCode)
    TextView mytripdetail_orderCode;      //订单编号

    //    @BindView(R.id.tripalready_dikou_layout)
//    LinearLayout tripalready_dikou_layout;//抵扣布局
    @BindView(R.id.lines)
    View lines;
    @BindView(R.id.tripalready_cancel_layout)
    LinearLayout tripalready_cancel_layout;//取消费用布局
    @BindView(R.id.tripalready_shiji_layout)
    LinearLayout tripalready_shiji_layout;//实际费用布局

//    @BindView(R.id.mytripdetail_canceltime3)
//    TextView mytripdetail_canceltime3;    //

    String outTradeNo = "";
    String orderCode = "";
    String status = "";//5 是已取消的  不显示创建时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_mytripdetail);
        initView();

    }

    private void initView() {
        tv_title.setText("行程详情");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }


    private void initData() {
        orderCode = getIntent().getStringExtra("orderCode");
        try {
            status = getIntent().getStringExtra("status");
        } catch (Throwable t) {
            t.printStackTrace();
            status = "";
        }
        LogUtil.e("接收到的行程详情：" + orderCode);
        getTripDetail(orderCode);
        findViewById(R.id.iv_wenhao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取优惠详情
                getPromoDetail(outTradeNo);
            }
        });

    }

    private void getPromoDetail(String outTradeNo) {

        String url = "api/tss/active/list/find";
        HashMap<String, Object> map = new HashMap<>();
        map.put("outTradeNo", outTradeNo);
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatListFunction<>(Bean_TripAlreadyPromo.class))
                .compose(RxNetHelper.<List<Bean_TripAlreadyPromo>>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<List<Bean_TripAlreadyPromo>>(mLoadingDialog) {

                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(List<Bean_TripAlreadyPromo> beans) {
                        LogUtil.e("优惠列表为 " + beans);
                        StringBuilder promoSb = new StringBuilder();
                        for (Bean_TripAlreadyPromo promo : beans) {
                            promoSb.append("『").append(promo.getActiveTitle()).append("』 优惠了： ").
                                    append(new BigDecimal(promo.getBenifitAmount() / 100).setScale(2,
                                            BigDecimal.ROUND_HALF_UP).doubleValue()).append("元");
                        }
                        DialogHelper.alertDialog(Activity_MyTripDetail.this, "优惠详情", promoSb.toString());
                    }

                    @Override
                    public void onFail(Message<List<Bean_TripAlreadyPromo>> bean) {
                        LogUtil.e("获取我的行程详情优惠失败");
                        ToastUtil.showToast("服务器繁忙, 请稍后重试");
                    }
                });
    }

    /**
     * 行程详情
     */
    private void getTripDetail(String orderCode) {

        String url = "api/tss/order/list";
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", orderCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TripDetail.class))
                .compose(RxNetHelper.<Bean_TripDetail>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Bean_TripDetail>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Bean_TripDetail bean_resu) {

//                        Log.e("asd", "预约时间    " + bean_resu.getOneOrder().getBorrowTime());
//                        Log.e("asd", "取消时间    " + bean_resu.getOneOrder().getReturnTime());
//                        Log.e("asd", "总计费用    " + bean_resu.getOneOrder().getTotalConsum());
//                        Log.e("asd", "抵扣金额    " + bean_resu.getOneOrder().getDeductAmount());
//                        Log.e("asd", "订单编号    " + bean_resu.getOneOrder().getOutTradeNo());
////                            Log.e( + "获取我的行程成功  更新时间    " + bean_resu.getOrderList().get(a).getUpdatedTime());
//                        Log.e("asd", "获取我的行程成功  费用        " + bean_resu.getOneOrder().getPayAmount());


//                        if (bean_resu.getOneOrder().getBorrowTime() == null) {
//                            mytripdetail_createtime.setVisibility(View.GONE);//为空不显示
//                        } else {
//                            mytripdetail_createtime.setText("预约时间：" + bean_resu.getOneOrder().getBorrowTime());//预约时间
//                        }
//                        mytripdetail_canceltime.setText("取消时间：" + bean_resu.getOneOrder().getReturnTime());//取消时间
//                        mytripdetail_cancelmoney.setText(bean_resu.getOneOrder().getTotalConsum() + "元");//总计费用
//                        mytripdetail_deductmoney.setText(bean_resu.getOneOrder().getDeductAmount() + "元");//抵扣金额
//
//                        if (bean_resu.getOneOrder().getBorrowTime() == null) {
//                            mytripdetail_createtime2.setVisibility(View.GONE);//为空不显示
//                        } else {
//                            mytripdetail_createtime2.setText("预约时间：" + bean_resu.getOneOrder().getBorrowTime());//预约时间
//                        }

                        try {
                            outTradeNo = bean_resu.getOneOrder().getOutTradeNo();
                            if (bean_resu.getOneOrder().getOutTradeNo().equals("-1")) {
                                mytripdetail_orderCode.setVisibility(View.GONE);
                            } else {
                                mytripdetail_orderCode.setText(bean_resu.getOneOrder().getOutTradeNo());
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            mytripdetail_orderCode.setVisibility(View.GONE);
                        }
                        mytripdetail_canceltime2.setText(bean_resu.getOneOrder().getReturnTime());  //取消时间
                        mytripdetail_cancelmoney2.setText(bean_resu.getOneOrder().getTotalConsum() + "元");//总计费用
//                        mytripdetail_deductmoney2.setText(bean_resu.getOneOrder().getDeductAmount() + "元");//抵扣金额


//                           if (bean_resu.getOneOrder().getBorrowTime()==null){
//                               mytripdetail_createtime2.setVisibility(View.GONE);//为空不显示
//                           }else {
//                        try {
//                            mytripdetail_createtime2.setText("预约时间：" + bean_resu.getOneOrder().getBorrowTime());//预约时间
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }


                        double total = 0.0;
                        try {
                            total = bean_resu.getOneOrder().getTotalConsum() / 100;
                            new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                            mytripdetail_cancelmoney.setText("" + total + "元");
                            mytripdetail_cancelmoney2.setText("" + total + "元");
                        } catch (Throwable t) {
                            t.printStackTrace();
//                            mytripdetail_cancelmoney.setText("" + total + "元");
                            mytripdetail_cancelmoney2.setText("" + total + "元");
                        }

//                        double dikou = 0.0;
//                        try {
//                            dikou = bean_resu.getOneOrder().getDeductAmount() / 100;
//                            new BigDecimal(dikou).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                            mytripdetail_deductmoney.setText("" + dikou + "元");
//                            mytripdetail_deductmoney2.setText("" + dikou + "元");
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                            mytripdetail_deductmoney.setText("" + dikou + "元");
//                            mytripdetail_deductmoney2.setText("" + dikou + "元");
//                        }

                        double shiji = 0.0;
                        try {
                            shiji = bean_resu.getOneOrder().getPayAmount() / 100;
                            new BigDecimal(shiji).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                            mytripdetail_paymoney.setText("" + shiji + "元");
                            mytripdetail_paymoney2.setText("" + shiji + "元");
                        } catch (Throwable t) {
                            t.printStackTrace();
//                            mytripdetail_paymoney.setText("" + shiji + "元");
                            mytripdetail_paymoney2.setText("" + shiji + "元");
                        }

                        try {
                            if (status.equals("4")) {
                                String str = "温馨提示：3分钟内取消预约订单不收取取消费用。";
                                mytripdetail_tips.setVisibility(View.VISIBLE);//设置提醒
                                tripalready_cancel_layout.setVisibility(View.GONE);
                                tripalready_shiji_layout.setVisibility(View.GONE);
                                lines.setVisibility(View.GONE);
                            } else {
                                String str = "温馨提示：3分钟内取消预约订单不收取取消费用。";
                                mytripdetail_tips.setVisibility(View.VISIBLE);//设置提醒
                                tripalready_cancel_layout.setVisibility(View.VISIBLE);
//                                tripalready_dikou_layout.setVisibility(View.GONE);
                                tripalready_shiji_layout.setVisibility(View.VISIBLE);
                                lines.setVisibility(View.VISIBLE);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            String str = "温馨提示：3分钟内取消预约订单不收取取消费用。";
                            mytripdetail_tips.setVisibility(View.VISIBLE);//设置提醒
                            tripalready_cancel_layout.setVisibility(View.GONE);
//                            tripalready_dikou_layout.setVisibility(View.GONE);
                            tripalready_shiji_layout.setVisibility(View.GONE);
                            lines.setVisibility(View.GONE);
                        }


//                        try {
//                            if (bean_resu.getOneOrder().getDeductAmount() <= 0.0) {
//                                tripalready_dikou_layout.setVisibility(View.GONE);
//                            }
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }
                        //抵扣金额 没有时候  不显示

//                        Log.e("asd", "获取我的行程成功" + bean_resu.toString());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getBorrowTime());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getOrderCode());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getStatus());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getDeductAmount());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getPayAmount());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getElectricConsum());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getMileageConsum());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getTimeConsum());
//                        Log.e("asd", "获取我的行程成功" + bean_resu.getOneOrder().getTotalConsum());

                    }

                    @Override
                    public void onFail(Message<Bean_TripDetail> bean) {
                        LogUtil.e("获取我的行程详情失败" + bean.msg);
                    }
                });
    }


}

