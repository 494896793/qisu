package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.PrefUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.TransFormUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.CurrencyUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.carShare.bean.CarNowOrderBean_Comple;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlreadyPromo;

/**
 * 行程结束Activity (还车成功)
 */
public class CarShareCompleteActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;               //标题
    @BindView(R.id.img_title_left)
    ImageView img_title_left; //标题左边按钮
    @BindView(R.id.tv_mile)
    TextView tvMile;                 //
    @BindView(R.id.tv_mile_money)
    TextView tvMileMoney;      //里程费用
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_time_money)
    TextView tvTimeMoney;       //时长费用
    @BindView(R.id.tv_total_money_down)
    TextView tvTotalMoneyDown;   //总计费用
    @BindView(R.id.tv_total_money_real)
    TextView tvTotalMoneyReal;   //实付费
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;            //展示  大字  元
    @BindView(R.id.tv_driver_money)
    TextView tvDriverMoney;          //代驾费用
    @BindView(R.id.ll_driver_money)
    LinearLayout llDriverMoney;      //代驾的布局
    @BindView(R.id.tv_car_complete_tip)
    TextView tvCarCompleteTip;   //提示最低消费
    @BindView(R.id.tv_car_complete_sure)
    TextView tv_car_complete_sure;//最下面的确认按钮
    @BindView(R.id.tv_car_complete_vip)
    TextView tv_car_complete_vip;//您是本平台的vip 优惠xx元
    @BindView(R.id.tv_driver_money_nuoche)
    TextView tv_driver_money_nuoche;      //挪车费用
    @BindView(R.id.tv_driver_money_dianliang)
    TextView tv_driver_money_dianliang;//电镀费
    @BindView(R.id.tv_total_money_deduct)
    TextView tv_total_money_deduct;        //本次抵扣金额

    @BindView(R.id.complete_dikou_layout)
    LinearLayout complete_dikou_layout;    //抵扣布局


    JSONObject jsonObject;//接收到的信息  不用fastjson
    String outTradeNo;

    /**
     * 2      * <li>功能描述：时间相减得到天数
     * 3      * @param beginDateStr
     * 4      * @param endDateStr
     * 5      * @return
     * 6      * long
     * 7      * @author Administrator
     * 8
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            //System.out.println("相隔的天数="+day);
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day;
    }

    private void clearPreference() {
        PrefUtil.saveString(mContext, PrefUtil.ORDER_CODE, "");
        PrefUtil.saveString(mContext, PrefUtil.CAR_CODE, "");
    }

    //分时租车的时间
    public static String getTime(long time) {
        String str = "";
        try {
            time = time / 1000;
            int s = (int) (time % 60);
            int m = (int) (time / 60 % 60);
            int h = (int) (time / 3600);
            String hh = "", mm = "", ss = "";              //精确时分秒
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

    //按日租车
    public static String getTime2Day(long time) {
        String str = "";
        try {
            time = time / 1000;
            int s = (int) (time % 60);
            int m = (int) (time / 60 % 60);
            int h = (int) (time / 3600);
            int d = (int) (time / 3600 / 24);//天
            if (d > 0) {
                str = d + "日";
            } else {
                str = 1 + "日";
            }
        } catch (Throwable t) {
            t.printStackTrace();
            str = "时间异常,请联系客服！";
        }

        return str;
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.startActivity(mContext, CarShareMapActivity.class);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_complete);
        initViews();
        clearPreference();
    }

    private void initViews() {
        img_title_left.setVisibility(View.GONE);
        tvTitle.setText("行程结束");
        String extra = ActivityUtil.getOneExtra(getIntent());
        LogUtil.e("获取到的还车结果：" + extra);
        try {
            jsonObject = JSONObject.parseObject(extra);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (jsonObject == null) {
            return;
        }


        setFeeView(jsonObject);
        LogUtil.e("拿到解析之后的结果：" + jsonObject.getString("status"));

    }

    @OnClick({R.id.img_title_left, R.id.tv_car_complete_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.tv_car_complete_vip:
                getPromo(outTradeNo);
                break;
            default:
                break;
        }
    }

    private void getPromo(String outTradeNo) {
        LogUtil.e("进入 获取用车结算优惠中.....");
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
                        DialogHelper.alertDialog(mContext, "优惠详情", promoSb.toString());
                    }

                    @Override
                    public void onFail(Message<List<Bean_TripAlreadyPromo>> bean) {
                        LogUtil.e("获取用车结算优惠失败");
                        ToastUtil.showToast("服务器繁忙, 请稍后重试");
                    }
                });
    }

    @OnClick(R.id.tv_car_complete_sure)
    public void sure(View view) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void setFeeView(JSONObject bean) {
        LogUtil.e("解析成功 花费结果状态：" + bean.get("status").toString());
        outTradeNo = bean.get("outTradeNo").toString();
        LogUtil.e("OutTradeNo is " + outTradeNo);
        if (bean.get("status").toString().equals("3")) {
//         onBackPressed();
        } else {
            ToastUtil.showToast("订单状态不对！");
            return;
        }

        //本次抵扣
        try {
            tv_total_money_deduct.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.get("deductAmount").toString())) + "元");
            if (new BigDecimal(bean.get("deductAmount").toString()).doubleValue() == 0) {
                tv_total_money_deduct.setText("0.00元");
                complete_dikou_layout.setVisibility(View.GONE);//
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        tvCarCompleteTip.setText("提示：消费不满" + TransFormUtil.fen2yuan(new BigDecimal(bean.get("costMin").toString())) + "元，按最低消费" + TransFormUtil.fen2yuan(new BigDecimal(bean.get("costMin").toString())) + "元计算。");
        String totalMoney = "";
        if (bean.get("driverType").toString().equals("0")) {
            totalMoney = TransFormUtil.fen2yuan(new BigDecimal(bean.get("totalConsum").toString()));//总计
            llDriverMoney.setVisibility(View.GONE);
        } else if (bean.get("driverType").toString().equals("1")) {//这里需要加代驾费用  +bean.dri
            totalMoney = TransFormUtil.fen2yuan(new BigDecimal(bean.get("totalConsum").toString()));   //+司机费用 bean.costFinal.add(bean.driverCost)
            llDriverMoney.setVisibility(View.VISIBLE);
            //需要放开注释
//            tvDriverMoney.setText(TransFormUtil.fen2yuan(bean.driverCost) + "元");   //这是代驾费用
        }

//        tvTotalMoney.setText(totalMoney);
        tvMile.setText("里程费（" + (new BigDecimal(bean.get("endMileage").toString()).subtract(new BigDecimal(bean.get("beginMileage").toString())).intValue()) + "公里）");

        try {
            DecimalFormat df = new DecimalFormat("######0.00");
            double timCon = Double.parseDouble(bean.get("timeConsum").toString()) / 100;
            tvTimeMoney.setText(df.format(timCon) + "元");//时长费

        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            DecimalFormat df = new DecimalFormat("######0.00");
            double timCon = Double.parseDouble(bean.get("mileageConsum").toString()) / 100;
            tvMileMoney.setText(df.format(timCon) + "元");  //里程费
        } catch (Throwable t) {
            t.printStackTrace();
        }


        try {
            String borrow_type = bean.get("borrowType").toString();
            if (borrow_type.equals("0")) {//分时租车
                BigDecimal rt = new BigDecimal(bean.get("returnTimeLong").toString());
                BigDecimal bt = new BigDecimal(bean.get("borrowTimeLong").toString());
                long returnTimeLong = Long.parseLong(rt.toPlainString());
                long borrowTimeLong = Long.parseLong(bt.toPlainString());
//              tvTime.setText("时长费（" + TransFormUtil.second2minute(returnTimeLong - borrowTimeLong) + "分钟）");
                tvTime.setText("时长费（" + getTime(returnTimeLong - borrowTimeLong) + "）");//时间 精确到秒
                LogUtil.e("时长费:" + bean.get("timeConsum").toString());

                DecimalFormat df = new DecimalFormat("######0.00");
                double timCon = Double.parseDouble(bean.get("timeConsum").toString()) / 100;
                tvTimeMoney.setText(df.format(timCon) + "元");//时长费
            } else if (borrow_type.equals("1")) {//日租
                tvMile.setText("使用时长");//原里程费
                tvTime.setText("日套餐费");//原时长费
                int d = 0;//天数
                try {
                    long beginTime = Long.parseLong(bean.get("borrowTimeLong").toString());
                    long endTime = Long.parseLong(bean.get("returnTimeLong").toString());
                    SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date2 = new Date();
                    date2.setTime(beginTime);
                    LogUtil.e("当前时间2--" + myFmt2.format(date2));
                    long str2 = 0L;
                    try {
                        str2 = myFmt2.parse(myFmt2.format(date2)).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Date date3 = new Date();
                    date3.setTime(endTime);
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
                    tvMileMoney.setText((d + 1) + "日");
                } catch (Throwable t) {
                    t.printStackTrace();
                    tvMileMoney.setText("0日");
                }
                try {
                    int fee2day = Integer.parseInt((String) SPUtil.get(mContext, "Fee2Day", "168"));
                    int timeMoney = fee2day * (d + 1);
                    DecimalFormat df = new DecimalFormat("0.00");
                    tvTimeMoney.setText(df.format(timeMoney) + "元");           //时长费
                } catch (Throwable t) {
                    t.printStackTrace();
                    tvTimeMoney.setText("0.00元");
                }
            }//状态完毕

        } catch (Throwable t) {
            t.printStackTrace();
        }


        tvTotalMoneyDown.setText(totalMoney + "元");//总计费用
        tvTotalMoneyReal.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.get("payAmount").toString())) + "元");//实付款
        tvTotalMoney.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.get("payAmount").toString())));
        //挪车费用
        String parkedAmount = "0";
        try {
            parkedAmount = bean.get("parkedAmount").toString();
            if (parkedAmount.equals("0")) {
                tv_driver_money_nuoche.setText("0.00元");
            } else {
//             double nuoche = Double.parseDouble(bean.get("parkedAmount").toString()); nuoche=nuoche/100;
//             tv_driver_money_nuoche.setText((new BigDecimal(nuoche))+"元");
                tv_driver_money_nuoche.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.get("parkedAmount").toString())) + "元");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            tv_driver_money_nuoche.setText("0.0元");
        }
        //挪车

        //优惠  折扣类型   0无折扣   1汽车投资产品折扣   2汽车消费产品  3黄金会员折扣
        String remark = "";
        try {
            remark = bean.get("remark").toString();
        } catch (Throwable t) {
            t.printStackTrace();
            remark = "";
        }
        if (remark.equals("")) {
            tv_car_complete_vip.setVisibility(View.GONE);
            tv_car_complete_vip.setText("(您是本平台的普通会员,本次订单为您优惠0.00元)");
        } else {
            tv_car_complete_vip.setVisibility(View.VISIBLE);
            tv_car_complete_vip.setText(remark + "");
        }

        String diandu = "";
        try {
            diandu = bean.get("electricConsum").toString();
            LogUtil.e("获取到的电量费：" + bean.get("electricConsum").toString());
        } catch (Throwable t) {
            t.printStackTrace();
            diandu = "";
        }
        if (diandu.equals("")) {
            tv_driver_money_dianliang.setVisibility(View.VISIBLE);
            tv_driver_money_dianliang.setText("0.00元");
        } else {
            try {
                DecimalFormat df = new DecimalFormat("######0.00");
                double timCon = Double.parseDouble(bean.get("electricConsum").toString()) / 100;
                tv_driver_money_dianliang.setText(df.format(timCon) + "元");  //电度费
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //网点订单查询
    private void requestOrderRequest() {
        //type 0:分时,1:按日
        //网点预约租车
        final String driverType = PrefUtil.getString(mContext, PrefUtil.DRIVER_TYPE);
        String url = "api/tss/order/query";
        final HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", PrefUtil.getString(mContext, PrefUtil.ORDER_CODE));
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("driverType", driverType);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarNowOrderBean_Comple.class))
                .compose(RxNetHelper.<CarNowOrderBean_Comple>io_main())
                .subscribe(new ResultSubscriber<CarNowOrderBean_Comple>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarNowOrderBean_Comple bean1) {
                        //状态 0:预约成功 1:开始计费 2:已还车，待付费 3:订单已完成 4:订单已取消
//                        CarNowOrderBean_Comple bean  = bean1.orderInfo;
                        setFeeView(bean1);
                    }

                    @Override
                    public void onFail(Message<CarNowOrderBean_Comple> bean) {
                        LogUtil.e("失败：" + bean.msg);
                    }

                });
    }

    @SuppressLint("SetTextI18n")
    private void setFeeView(CarNowOrderBean_Comple bean) {
        LogUtil.e("解析成功 花费结果状态：" + bean.getStatus());
        String status = bean.status;
        if (status.equals("3")) {
//         onBackPressed();
        } else {
            ToastUtil.showToast("订单状态不对！");
            return;
        }
        tvCarCompleteTip.setText("提示：里程费+时长费消费不满" + TransFormUtil.fen2yuan(new BigDecimal(bean.costMin)) + "元，按最低消费" + TransFormUtil.fen2yuan(new BigDecimal(bean.costMin)) + "元扣费。");
        String totalMoney = "";
        if (bean.driverType.equals("0")) {
            totalMoney = TransFormUtil.fen2yuan(new BigDecimal(bean.totalConsum));//总计
            llDriverMoney.setVisibility(View.GONE);
        } else if (bean.driverType.equals("1")) {//这里需要加代驾费用  +bean.dri
            totalMoney = TransFormUtil.fen2yuan(new BigDecimal(bean.totalConsum));   //+司机费用 bean.costFinal.add(bean.driverCost)
            llDriverMoney.setVisibility(View.VISIBLE);
            //需要放开注释
//            tvDriverMoney.setText(TransFormUtil.fen2yuan(bean.driverCost) + "元");   //这是代驾费用
        }

//        tvTotalMoney.setText(totalMoney);
        tvMile.setText("里程费（" + (new BigDecimal(bean.totalConsum).subtract(new BigDecimal(bean.beginMileage)).intValue()) + "公里）");
        tvMileMoney.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.mileageConsum)) + "元");  //里程费
        tvTime.setText("时长费（" + TransFormUtil.second2minute(bean.returnTimeLong - bean.borrowTimeLong) + "分钟）");
        tvTimeMoney.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.timeConsum)) + "元");//时长费

        tvTotalMoneyDown.setText(totalMoney + "元");
        tvTotalMoneyReal.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.payAmount)) + "元");//实付款
        tvTotalMoney.setText(TransFormUtil.fen2yuan(new BigDecimal(bean.payAmount)));
    }
}
