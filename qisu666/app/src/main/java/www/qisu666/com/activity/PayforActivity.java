package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.ButtonEnable;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessActivityEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.Activity_CarBuy_Payway;
import www.qisu666.sdk.partner.bean.Bean_TotalAmount;
import www.qisu666.sdk.partner.pay.CarBuy_NewAliPayStrategy;
import www.qisu666.sdk.partner.pay.CarBuy_NewPayStrategy;
import www.qisu666.sdk.partner.pay.CarBuy_NewWXPayStrategy;

/**
 * 717219917@qq.com 2018/8/14 16:32.
 */
public class PayforActivity extends BaseActivity implements View.OnClickListener{

    private CarBuy_NewPayStrategy newPayStrategy;
    private CarBuy_NewWXPayStrategy newWXPayStrategy;
    private CarBuy_NewAliPayStrategy newAliPayStrategy;
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.layout_wxpay)
    LinearLayout layout_wxpay;
    @BindView(R.id.layout_alipay)
    LinearLayout layout_alipay;
    @BindView(R.id.img_wxpay_checked)
    ImageView img_wxpay_checked;
    @BindView(R.id.img_alipay_checked)
    ImageView img_alipay_checked;
    @BindView(R.id.tv_corol_wxpay)
    TextView tvCorolWx;
    @BindView(R.id.tv_corol_alipay)
    TextView tvCorolAi;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tx_money_title)
    TextView tx_money_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.btn_title_left)
    TextView btn_title_left;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private String totalFee;
    private String productCode;
    private String subCount;
    private String subType;
    private String subAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payfor_layout);
        EventBus.getDefault().register(this);
        initIntent();
        //微信会员押金提交  B114
        newWXPayStrategy = new CarBuy_NewWXPayStrategy(this, "B114");
        newAliPayStrategy = new CarBuy_NewAliPayStrategy(this, "B112", mHandler);
//        newPayStrategy = newWXPayStrategy;
        newPayStrategy = newAliPayStrategy;
        initListenner();
    }

    private void initIntent(){
        totalFee=getIntent().getStringExtra("totalFee");
        productCode=getIntent().getStringExtra("productCode");
        subCount=getIntent().getStringExtra("subCount");
        subType=getIntent().getStringExtra("subType");
        subAmount=getIntent().getStringExtra("subAmount");
        tv_balance.setText(totalFee);
        tx_money_title.setText("您将认购"+subCount+"份认购型汽车，本次须支付（元）");
        tv_title.setText("缴费认购");
    }

    private void initListenner(){
        btn_title_left.setText("认购缴费");
        img_title_left.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        layout_alipay.setOnClickListener(this);
        layout_wxpay.setOnClickListener(this);
    }

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

    //取消支付
    private void cancelPay(){
        String url = "api/vip/pay/reback";
        LogUtil.e("取消支付："+url);
        HashMap<String, Object> map = new HashMap<>();
        map.put("responseStatus", "2");
        map.put("orderNo",(String) SPUtil.get(PayforActivity.this,"orderNo",""));//orderNo
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TotalAmount.class))
                .compose(RxNetHelper.<Bean_TotalAmount>io_main())
                .subscribe(new ResultSubscriber<Bean_TotalAmount>() {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override public void onSuccess(Bean_TotalAmount bean) {
                        btn_submit.setEnabled(true);
                        btn_submit.getBackground().setAlpha(255);
                        LogUtil.e(" 认购 获取到的 总计金额"+bean.totalSubAmount);
                        LogUtil.e(" 认购 获取到的 用户编码"+bean.userCode);
//                        tv_balance.setText("¥" + bean.totalSubAmount + "元");
                        totalFee =bean.totalSubAmount;
                    }
                    @Override public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                        btn_submit.setEnabled(true);
                        btn_submit.getBackground().setAlpha(255);
                        LogUtil.e("进入failed 方法："+bean.msg);
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.layout_wxpay:
                img_wxpay_checked.setImageResource(R.mipmap.rg_132);
                tvCorolWx.setTextColor(ContextCompat.getColor(this, R.color.new_primary));

                img_alipay_checked.setImageResource(R.mipmap.rg_12);
                tvCorolAi.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                newPayStrategy = newWXPayStrategy;
                break;
            case R.id. layout_alipay:
                img_wxpay_checked.setImageResource(R.mipmap.rg_13);
                tvCorolWx.setTextColor(ContextCompat.getColor(this, R.color.text_gray));

                img_alipay_checked.setImageResource(R.mipmap.rg_122);
                tvCorolAi.setTextColor(ContextCompat.getColor(this, R.color.new_primary));
                newPayStrategy = newAliPayStrategy;
                break;
            case R.id.btn_submit:
                btn_submit.setEnabled(false);
                btn_submit.getBackground().setAlpha(100);
                newPayStrategy.pay(UserParams.INSTANCE.getUser_id(), totalFee, productCode, subAmount, subCount,subType);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(PaySuccessEvent paySuccessEvent){
        btn_submit.setEnabled(true);
        btn_submit.getBackground().setAlpha(255);
        Intent payIntent=new Intent(this,InverstPaySuccess.class);
        payIntent.putExtra("subType",subType);
        startActivity(payIntent);
    }

    @Subscribe
    public void onEvent(PayFailEvent paySuccessEvent){
        ToastUtil.showToast("支付失败");
        btn_submit.setEnabled(true);
        btn_submit.getBackground().setAlpha(255);
    }

    @Subscribe
    public void onEvent(PaySuccessActivityEvent event){
        finish();
        btn_submit.setEnabled(true);
        btn_submit.getBackground().setAlpha(255);
    }

    @Subscribe
    public void onEvent(ButtonEnable enable){
        btn_submit.setEnabled(true);
        btn_submit.getBackground().setAlpha(255);
    }

}
