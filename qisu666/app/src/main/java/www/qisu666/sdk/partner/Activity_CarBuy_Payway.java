package www.qisu666.sdk.partner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.sdk.partner.bean.Bean_TotalAmount;
import www.qisu666.sdk.partner.bean.Event_PayRequest;
import www.qisu666.sdk.partner.pay.CarBuy_NewAliPayStrategy;
import www.qisu666.sdk.partner.pay.CarBuy_NewPayStrategy;
import www.qisu666.sdk.partner.pay.CarBuy_NewWXPayStrategy;

/**认购支付方式*/
public class Activity_CarBuy_Payway extends BaseActivity {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.img_wxpay_checked) ImageView imgWxpayChecked;
    @BindView(R.id.img_alipay_checked) ImageView imgAlipayChecked;
    @BindView(R.id.tv_deposit_pay) TextView tvDepositPay;
    @BindView(R.id.carbuyway_txt) TextView carbuyway_txt;  //付款说明


    //支付金额
    private String payAmount = "";
    private String payGift = "0";
    //支付方式
    private CarBuy_NewPayStrategy newPayStrategy;
    private CarBuy_NewWXPayStrategy newWXPayStrategy;
    private CarBuy_NewAliPayStrategy newAliPayStrategy;

    private static final int SDK_PAY_FLAG = 1;

    Event_PayRequest payRequest;//接收到的支付相关的参数

    //支付宝支付结果处理
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            try{
                LogUtil.e("支付结果 msg:"+msg);}catch (Throwable t){t.printStackTrace();}//
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new PaySuccessEvent());
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogHelper.alertDialog(mContext, getString(R.string.dialog_pay_alipay_8000));
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            LogUtils.e(payResult.toString());
//                            ToastUtil.showToast(R.string.toast_pay_cancel);
                            cancelPay();
                        }
                    } break;
                }
                default: break;
            }
        }
    };

    @Override protected void onResume() {
        super.onResume();
    }

    //接收到支付的参数
    @Subscribe(threadMode = ThreadMode.MAIN ,sticky = true)
    public void rec_CarBuy(Event_PayRequest payRequest_){
              payRequest=payRequest_;
            LogUtil.e("获取到支付参数 用户id："+payRequest.userId);
             requestPay();//每次更新付款金额
    }

    //微信支付成功不进入
    @Subscribe(threadMode = ThreadMode.MAIN)
      public void onEventMainThread(PaySuccessEvent event) {
        ToastUtil.showToast(R.string.toast_pay_success);//支付成功
        Intent intent = new Intent(Activity_CarBuy_Payway.this,Activity_CarbuyComplete.class);
        if(payRequest.subType.equals("1")){//投资型
            intent.putExtra("carbuy_model","投资型");
        }else{
            intent.putExtra("carbuy_model","消费型");
        }
        startActivity(intent);
        finish();
    }


    //微信支付  取消 不进入
    @Subscribe(threadMode = ThreadMode.MAIN)
     public void onEventMainThread(PayFailEvent event) {
        LogUtil.e("接收到微信取消事件："+event);
//        DialogHelper.alertDialog(this, getString(R.string.dialog_pay_failed));
        cancelPay();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.carbuy_payway);

        //微信会员押金提交  B114
        newWXPayStrategy = new CarBuy_NewWXPayStrategy(this, "B114");
        newAliPayStrategy = new CarBuy_NewAliPayStrategy(this, "B112", mHandler);
//        newPayStrategy = newWXPayStrategy;
        newPayStrategy = newAliPayStrategy;

        initView();
        EventBus.getDefault().register(this);
    }

//    {"msg":"查询成功","code":4003,"data":{"subscribeConsume":{"firstPhaseTime":"2018-05-18","contractExpiresTime":"2023-04-18","contractStatus":"1","subType":"2","canCancleTime":"2019-04-18","useBonusAmount":"0","subTime":"2018-04-18","subStatus":"1"},"carInfoMap":{"withdrawPeriods":12.0,"period":60.0,"color":"黑","cityCode":"CT00002","carImgPath":"/images/car/20180123/1516641530523.jpg","productStatus":"1","productNumber":10.0,"surplusNumber":8.0,"plateNumber":"粤BD78580","vinNo":"123456789","productTitle":"迈凯伦","totalAmount":200000.0,"subAmount":20000.0,"productCode":"CP000004","carCode":"CA000001","balance":160000.0,"cityName":"深圳市","subRebate":7.0,"carWpmi":"Chnnb25111","productType":"2","productTypeCn":"消费型"}}}

    //获取总金额
    private void requestPay() {
            String url = "api/vip/product/subscribe";
            HashMap<String, Object> map = new HashMap<>();
            map.put("userCode", UserParams.INSTANCE.getUser_id());
            map.put("productCode",payRequest.productCode);
            map.put("subAmount", payRequest.subAmount);
            map.put("subCount",payRequest.subCount);
            MyNetwork.getMyApi()
                    .carRequest(url, MyMessageUtils.addBody(map))
                    .map(new FlatFunction<>(Bean_TotalAmount.class))
                    .compose(RxNetHelper.<Bean_TotalAmount>io_main())
                    .subscribe(new ResultSubscriber<Bean_TotalAmount>() {
                        @Override
                        public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                        }

                        @Override public void onSuccess(Bean_TotalAmount bean) {
                            LogUtil.e(" 认购 获取到的 总计金额"+bean.totalSubAmount);
                            LogUtil.e(" 认购 获取到的 用户编码"+bean.userCode);
                            tvDepositPay.setText("¥" + bean.totalSubAmount + "元");
                            payAmount =bean.totalSubAmount;
                       }
                        @Override public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                            LogUtil.e("进入failed 方法："+bean.msg);
                            finish();
                        }
                    });
    }


    //取消支付
    private void cancelPay(){
        String url = "api/vip/pay/reback";
        LogUtil.e("取消支付："+url);
        HashMap<String, Object> map = new HashMap<>();
        map.put("responseStatus", "2");
        map.put("orderNo",(String)SPUtil.get(Activity_CarBuy_Payway.this,"orderNo",""));//orderNo
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TotalAmount.class))
                .compose(RxNetHelper.<Bean_TotalAmount>io_main())
                .subscribe(new ResultSubscriber<Bean_TotalAmount>() {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override public void onSuccess(Bean_TotalAmount bean) {
                        LogUtil.e(" 认购 获取到的 总计金额"+bean.totalSubAmount);
                        LogUtil.e(" 认购 获取到的 用户编码"+bean.userCode);
                        tvDepositPay.setText("¥" + bean.totalSubAmount + "元");
                        payAmount =bean.totalSubAmount;
                    }
                    @Override public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                        LogUtil.e("进入failed 方法："+bean.msg);
                        finish();
                    }
                });
    }


    @Override protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finishReceiver);
        EventBus.getDefault().unregister(this);
    }

    /** 初始化控件 */
    private void initView() {
        tvTitle.setText("产品认购");
        carbuyway_txt.setText("您将认购"+getIntent().getStringExtra("num")+"份"+
         getIntent().getStringExtra("type")+"汽车产品,本次支付"
        );
    }


    @OnClick({R.id.img_title_left, R.id.layout_wxpay, R.id.layout_alipay, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.layout_wxpay:
                imgWxpayChecked.setImageResource(R.mipmap.ic_pay_mode_checked);
                imgAlipayChecked.setImageResource(R.mipmap.ic_pay_mode_unchecked);
                newPayStrategy = newWXPayStrategy;
                break;
            case R.id.layout_alipay:
                imgWxpayChecked.setImageResource(R.mipmap.ic_pay_mode_unchecked);
                imgAlipayChecked.setImageResource(R.mipmap.ic_pay_mode_checked);
                newPayStrategy = newAliPayStrategy;
                break;
            case R.id.btn_submit:
//                if (checkInput()) {
                    newPayStrategy.pay(payRequest.userId, payRequest.totalFee, payRequest.productCode, payRequest.subAmount, payRequest.subCount,payRequest.subType);
//                }
                break;
        }
    }

    private boolean checkInput() {
//        payAmount = et_amount.getText().toString().trim();
        if (TextUtils.isEmpty(payAmount)) {
            ToastUtil.showToast(R.string.toast_pay_recharge_amount_is_null);
        } else if (Integer.valueOf(payAmount) <= 0 || Integer.valueOf(payAmount) > 10000) {
            ToastUtil.showToast(R.string.toast_pay_recharge_amount_illegal);
        } else {
            return true;
        }
        return false;
    }
}
