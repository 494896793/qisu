package www.qisu666.sdk.mytrip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import www.qisu666.sdk.mytrip.bean.Bean_TripAlreadyDetail;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlreadyPromo;

/**
 * 717219917@qq.com  2016/12/14 0:26
 */
public class Activity_MyTrip_AlreadyDetail extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;

    //    @BindView(R.id.mytrip_already_time_img)
//    ImageView mytrip_already_time_img;           //详情旁边的图标
    @BindView(R.id.mytrip_already_getcartime)
    TextView mytrip_already_getcartime;         //取车时间
    @BindView(R.id.mytrip_already_returncartime)
    TextView mytrip_already_returncartime;   //还车时间

    //    @BindView(R.id.mytrip_already_time_img2)
//    ImageView mytrip_already_time_img2;           //时间旁边的图标
    @BindView(R.id.mytrip_already_car_type)
    TextView mytrip_already_car_type;             //用车类型
    //    @BindView(R.id.mytrip_item_detail_jifei)
//    TextView mytrip_item_detail_jifei;            //计费类型
    @BindView(R.id.mytrip_alreadydetail_licheng)
    TextView mytrip_alreadydetail_licheng;          //里程  公里
    @BindView(R.id.mytrip_alreadydetail_lichengmoney)
    TextView mytrip_alreadydetail_lichengmoney;//里程费用  需要加元

    @BindView(R.id.mytrip_alreadydetail_shichang)
    TextView mytrip_alreadydetail_shichang;//时长txt
    @BindView(R.id.mytrip_alreadydetail_shichangmoney)
    TextView mytrip_alreadydetail_shichangmoney;//时长费 需要加元
    @BindView(R.id.mytrip_alreadydetail_shichangmoney2)
    TextView mytrip_alreadydetail_shichangmoney2;
    @BindView(R.id.mytrip_alreadydetail_lichengmoney2)
    TextView mytrip_alreadydetail_lichengmoney2;
    @BindView(R.id.mytrip_alreadydetail_dianliangmoney2)
    TextView mytrip_alreadydetail_dianliangmoney2;

    //    @BindView(R.id.mytrip_alreadydetail_dianliang)
//    TextView mytrip_alreadydetail_dianliang;//电量txt
    @BindView(R.id.mytrip_alreadydetail_dianliangmoney)
    TextView mytrip_alreadydetail_dianliangmoney;//电量费用  需要加元

    @BindView(R.id.mytrip_alreadydetail_nuochemoney)
    TextView mytrip_alreadydetail_nuochemoney;//挪车费用 需要加元
    @BindView(R.id.mytrip_alreadydetail_totalmoney)
    TextView mytrip_alreadydetail_totalmoney;  //总计费用  需要加元
    @BindView(R.id.minue_linear)
    LinearLayout minue_linear;
    @BindView(R.id.day_linear)
    LinearLayout day_linear;
//    @BindView(R.id.mytrip_alreadydetail_dikoumoney)
//    TextView mytrip_alreadydetail_dikoumoney;   //抵扣费用  加元   /100
//    @BindView(R.id.mytrip_alreadydetail_dikoumoney_layout)
//    LinearLayout mytrip_alreadydetail_dikoumoney_layout;//抵扣布局


    @BindView(R.id.mytrip_alreadydetail_paymoney)
    TextView mytrip_alreadydetail_paymoney;       //实付款  加元   /100

    @BindView(R.id.mytripdetail_tips)
    ImageView mytripdetail_tips;                              //优惠提示i
    @BindView(R.id.mytripdetail_tips1)
    TextView mytripdetail_tips1;                            //红字提示
    @BindView(R.id.mytrip_item_detail_dingdanbianhao)
    TextView mytrip_item_detail_dingdanbianhao;//订单编号


    String orderCode = "";
    /**
     * 订单流水号
     */
    String outTradeNo = "";
//    @BindView(R.id.tv_youhui)
//    TextView tvYouhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_mytrip_alreadydetail);
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
        // 点击优惠介绍显示优惠详情
        mytripdetail_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取优惠详情
                getPromoDetail(outTradeNo);
            }
        });
        initData();
    }


    private void initData() {
        orderCode = getIntent().getStringExtra("orderCode");
        LogUtil.e("接收到的行程详情：" + orderCode);
        getTripDetail(orderCode);
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
                .map(new FlatFunction<>(Bean_TripAlreadyDetail.class))
                .compose(RxNetHelper.<Bean_TripAlreadyDetail>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Bean_TripAlreadyDetail>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Bean_TripAlreadyDetail bean_resu) {
                        if (bean_resu.getOneOrder().getStatus().equals("3")) {
                            mytrip_already_getcartime.setText(bean_resu.getOneOrder().getBorrowTime());//预约时间
                        } else {
                            mytrip_already_getcartime.setText(bean_resu.getOneOrder().getCreatedTime());//预约时间
                        }

                        mytrip_already_returncartime.setText(bean_resu.getOneOrder().getReturnTime());//还车时间
//                        mytrip_already_car_type.setText(bean_resu.getOneOrder().getDriverType().equals("0") ? "自驾租车" : "代驾");//是否自驾用车
                        double gongli = new BigDecimal(bean_resu.getOneOrder().getEndMileage() - bean_resu.getOneOrder().getBeginMileage()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        mytrip_alreadydetail_licheng.setText("里程费(" + gongli + "公里)"); //里程
                        double gongli_money = new BigDecimal(bean_resu.getOneOrder().getMileageConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        mytrip_alreadydetail_lichengmoney.setText(gongli_money + "元"); //里程费用


                        if (bean_resu.getOneOrder().getBorrowType().equals("0")) {
                            day_linear.setVisibility(View.GONE);
                            minue_linear.setVisibility(View.VISIBLE);
                            //分时租车
//                            mytrip_item_detail_jifei.setText("计费类型：分时租车");
                            String fen = time_fen(bean_resu.getOneOrder().getBorrowTime(), bean_resu.getOneOrder().getReturnTime());
                            //时长
                            mytrip_already_car_type.setText("自驾/分时租赁");
                            mytrip_alreadydetail_shichang.setText("时长费(" + fen + ")");
                            Date date_borro, date_return;
                            long borrowTime = 0L;
                            long returnTime = 0L;
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                date_borro = myFmt2.parse(bean_resu.getOneOrder().getBorrowTime());
                                borrowTime = date_borro.getTime();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                            try {
                                date_return = myFmt2.parse(bean_resu.getOneOrder().getReturnTime());
                                returnTime = date_return.getTime();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                            //时长
                            mytrip_alreadydetail_shichang.setText("时长费(" + getTime(returnTime - borrowTime) + ")");
                        } else {
                            Date date_borro, date_return;
                            day_linear.setVisibility(View.VISIBLE);
                            minue_linear.setVisibility(View.GONE);
//                            mytrip_item_detail_jifei.setText("计费类型：按日租车");
                            int day = time(bean_resu.getOneOrder().getBorrowTime(), bean_resu.getOneOrder().getReturnTime());
                            mytrip_alreadydetail_shichangmoney2.setText(new BigDecimal(bean_resu.getOneOrder().getCarConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"元");
                            //时长
                            mytrip_alreadydetail_shichang.setText("时长费(" + day + "日)");
                            mytrip_already_car_type.setText("自驾/按日租赁");
                            long borrowTime = 0L;
                            long returnTime = 0L;
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                date_borro = myFmt2.parse(bean_resu.getOneOrder().getBorrowTime());
                                borrowTime = date_borro.getTime();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                            try {
                                date_return = myFmt2.parse(bean_resu.getOneOrder().getReturnTime());
                                returnTime = date_return.getTime();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                            mytrip_alreadydetail_lichengmoney2.setText(getTime(returnTime - borrowTime));
                        }

                        //时长费用
                        mytrip_alreadydetail_shichangmoney.setText(new BigDecimal(bean_resu.getOneOrder().getTimeConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        //电量费用
                        mytrip_alreadydetail_dianliangmoney.setText(new BigDecimal(bean_resu.getOneOrder().getElectricConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        mytrip_alreadydetail_dianliangmoney2.setText(new BigDecimal(bean_resu.getOneOrder().getElectricConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        //挪车费用
                        mytrip_alreadydetail_nuochemoney.setText(new BigDecimal(bean_resu.getOneOrder().getParkedConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        //总计费用
                        mytrip_alreadydetail_totalmoney.setText(new BigDecimal(bean_resu.getOneOrder().getTotalConsum() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        //抵扣金额
//                        mytrip_alreadydetail_dikoumoney.setText(new BigDecimal(bean_resu.getOneOrder().getDeductAmount() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
                        //实付款
                        mytrip_alreadydetail_paymoney.setText(new BigDecimal(bean_resu.getOneOrder().getPayAmount() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "元");
//                        try {
//                            if (bean_resu.getOneOrder().getDeductAmount() <= 0.0) {
//                                mytrip_alreadydetail_dikoumoney_layout.setVisibility(View.GONE);
//                            }
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }

//                        try {
//                            LogUtil.e("Remark is " + bean_resu.getOneOrder().getRemark());
//                            if (bean_resu.getOneOrder().getRemark() == null) {
//                                tvYouhui.setVisibility(View.GONE);
//                            } else {
//
//                                String s = bean_resu.getOneOrder().getRemark().substring(9, 13);
//                                tvYouhui.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
//                                tvYouhui.setTextSize(22);
//                                //进行提示
//                                tvYouhui.setText(s);
//                            }
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                            tvYouhui.setVisibility(View.GONE);
////                            mytripdetail_tips.setText("");
//
//                        }

                        mytripdetail_tips1.setVisibility(View.VISIBLE);
                        double min = bean_resu.getOneOrder().getCostMin() / 100;
                        mytripdetail_tips1.setText("提示：消费不满 " + min + " 元，按最低消费 " + min + " 元计算！");

                        try {
                            if (bean_resu.getOneOrder().getOutTradeNo() == null) {
                                mytrip_item_detail_dingdanbianhao.setVisibility(View.GONE);
                            } else {
                                // 获取订单流水号
                                outTradeNo = bean_resu.getOneOrder().getOutTradeNo();
                                mytrip_item_detail_dingdanbianhao.setText(bean_resu.getOneOrder().getOutTradeNo());
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            mytrip_item_detail_dingdanbianhao.setVisibility(View.GONE);
                        }

                        double total = 0.0;
//                           try {
//                               total = bean_resu.getOneOrder().getTotalConsum() / 100;
//                               new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                               mytripdetail_cancelmoney.setText(""+total+"元");
//                           }catch (Throwable t){t.printStackTrace();
//                               mytripdetail_cancelmoney.setText(""+total+"元");
//                           }
//
//                           double dikou=0.0;
//                           try {
//                               dikou = bean_resu.getOneOrder().getDeductAmount() / 100;
//                               new BigDecimal(dikou).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                               mytripdetail_deductmoney.setText(""+dikou+"元");
//                           }catch (Throwable t){t.printStackTrace();
//                               mytripdetail_deductmoney.setText(""+dikou+"元");
//                           }
//
//                           double shiji=0.0;
//                           try {
//                               shiji = bean_resu.getOneOrder().getPayAmount() / 100;
//                               new BigDecimal(shiji).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                               mytripdetail_deductmoney.setText(""+shiji+"元");
//                           }catch (Throwable t){t.printStackTrace();
//                               mytripdetail_deductmoney.setText(""+shiji+"元");
//                           }

//                        LogUtil.e("获取我的行程成功" + bean_resu.toString());
//                        LogUtil.e("获取我的行程成功 借车时间 " + bean_resu.getOneOrder().getBorrowTime());
//                        LogUtil.e("获取我的行程成功 订单编号 " + bean_resu.getOneOrder().getOrderCode());
//                        LogUtil.e("获取我的行程成功 订单状态 " + bean_resu.getOneOrder().getStatus());
//                        LogUtil.e("获取我的行程成功 折扣金额 " + bean_resu.getOneOrder().getDeductAmount());
//                        LogUtil.e("获取我的行程成功 订单金额 " + bean_resu.getOneOrder().getPayAmount());
//                        LogUtil.e("获取我的行程成功 电量费用 " + bean_resu.getOneOrder().getElectricConsum());
//                        LogUtil.e("获取我的行程成功 里程费用 " + bean_resu.getOneOrder().getMileageConsum());
//                        LogUtil.e("获取我的行程成功 时长费用 " + bean_resu.getOneOrder().getTimeConsum());
//                        LogUtil.e("获取我的行程成功 总计费用 " + bean_resu.getOneOrder().getTotalConsum());

                    }

                    @Override
                    public void onFail(Message<Bean_TripAlreadyDetail> bean) {
                        LogUtil.e("获取我的行程详情失败" + bean.msg);
                    }
                });
    }

    /**
     * Layoute add 2018.06.07 18:00
     */
    private void getPromoDetail(String outTradeNo) {
        LogUtil.e("进入 获取行程详情优惠中.....");
        LogUtil.e("订单流水号为 " + outTradeNo);
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
                        DialogHelper.alertDialog(Activity_MyTrip_AlreadyDetail.this, "优惠详情", promoSb.toString());
                    }

                    @Override
                    public void onFail(Message<List<Bean_TripAlreadyPromo>> bean) {
                        LogUtil.e("获取我的行程详情优惠失败");
                        ToastUtil.showToast("服务器繁忙, 请稍后重试");
                    }
                });

    }

    private int time(String begin, String end) {
        int d = 0;
        try {
            SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = myFmt2.parse(begin);
            LogUtil.e("当前时间2--" + myFmt2.format(date2));
            long str2 = 0L;
            try {
                str2 = myFmt2.parse(myFmt2.format(date2)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Date date3 = myFmt2.parse(end);
            LogUtil.e("当前时间2--" + myFmt2.format(date3));
            long str3 = 0L;
            try {
                str3 = myFmt2.parse(myFmt2.format(date3)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LogUtil.e("当前时间2--" + str3);
            LogUtil.e("当前时间000000--" + (str3 - str2));

            d = (int) (str3 - str2) / 1000 / 3600 / 24;
            d = d + 1;
            LogUtil.e("获取几日：" + d + "日");
        } catch (Throwable t) {
            t.printStackTrace();
//          tvMileMoney.setText("0日");
        }
        return d;
    }

    private String time_fen(String begin, String end) {
        String str = "0分钟";
        int d = 0;
        try {
            SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date2 = myFmt2.parse(begin);
            LogUtil.e("当前时间2--" + myFmt2.format(date2));
            long str2 = 0L;
            try {
                str2 = myFmt2.parse(myFmt2.format(date2)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Date date3 = myFmt2.parse(end);
            LogUtil.e("当前时间2--" + myFmt2.format(date3));
            long str3 = 0L;
            try {
                str3 = myFmt2.parse(myFmt2.format(date3)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LogUtil.e("当前时间2--" + str3);
            LogUtil.e("当前时间000000--" + (str3 - str2));

//            d=(int) (str3-str2);
//            d = (int) (str3 - str2) / 1000 / 3600 ;
            int fen = ((int) (str3 - str2)) / 60 / 1000;
            int hour = fen / 60;
            if (fen > 60) {
                str = hour + "小时" + (fen % 60) + "分钟";
            } else {
                if (fen == 0) {
                    fen = fen + 1;
                }
                str = fen + "分钟";
            }
//            d = (int) (str3 - str2) / 1000 / 3600 / 24;
//            d=d+1;
            LogUtil.e("获取几日：" + d + "日");
        } catch (Throwable t) {
            t.printStackTrace();
//          tvMileMoney.setText("0日");
        }
        return str;
    }

    /**
     * 计算时间差
     *
     * @param time 相差的时间
     * @return xx时xx分xx秒
     */
    public static String getTime(long time) {
        String str = "";
        try {
            time = time / 1000;
            int s = (int) (time % 60);
            int m = (int) (time / 60 % 60);
            int h = (int) (time / 3600);
            //精确时分秒
            String hh = "", mm = "", ss = "";
            if (h >= 0 && h <= 9) {
                hh = "0" + h;
            } else {
                hh = "" + h;
            }
            if (m >= 0 && m <= 9) {
                mm = "0" + m;
            } else {
                mm = "" + m;
            }
            if (s >= 0 && s <= 9) {
                ss = "0" + s;
            } else {
                ss = "" + s;
            }
            str = hh + ":" + mm + ":" + ss;
        } catch (Throwable t) {
            t.printStackTrace();
            str = "时间异常,请联系客服！";
        }
        return str;
    }

}

