package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.config.Config;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.pay.AliPayStrategy;
import www.qisu666.com.pay.PayStrategy;
import www.qisu666.com.pay.WXPayStrategy;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;

/**
 * 年费支付
 */
public class AnnualFeeActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_balance;
    private EditText et_amount;
    private LinearLayout layout_wxpay;
    private LinearLayout layout_alipay;
    private ImageView img_wxpay_checked;
    private ImageView img_alipay_checked;
    private TextView btn_submit;
    private TextView tv_annual_pay;

    /**
     * 支付金额
     */
    private String payAmount = "";
    /**
     * 支付方式
     */
    private PayStrategy payStrategy;
    private WXPayStrategy wxpayStrategy;
    private AliPayStrategy alipayStrategy;

    private static final int SDK_PAY_FLAG = 1;

    //支付宝支付结果处理
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new PaySuccessEvent());
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogHelper.alertDialog(AnnualFeeActivity.this, getString(R.string.dialog_pay_alipay_8000));
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            LogUtils.e(payResult.toString());
                            ToastUtil.showToast(R.string.toast_pay_cancel);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Subscribe
    public void onEventMainThread(PaySuccessEvent event) {
        ToastUtil.showToast(R.string.toast_pay_recharge_success);
        finish();
    }

    @Subscribe
    public void onEventMainThread(PayFailEvent event) {
        DialogHelper.alertDialog(this, getString(R.string.dialog_pay_failed));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_annual_fee);

        requestPay();

        wxpayStrategy = new WXPayStrategy(this);
        alipayStrategy = new AliPayStrategy(this, mHandler);
//        payStrategy = wxpayStrategy;
        payStrategy = alipayStrategy;

        initView();
        setListeners();
//        registerReceiver(finishReceiver, new IntentFilter(ReceiverAction.ACTION_PAY_SUCCESS));
        EventBus.getDefault().register(this);
    }

    private void requestPay() {
        HttpLogic httpLogic = new HttpLogic(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "B111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {

            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                payAmount = map.get("member_deposit").toString();
                tv_annual_pay.setText("¥" + payAmount + "/年");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finishReceiver);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initTitleBar();
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        et_amount = (EditText) findViewById(R.id.et_amount);
        layout_wxpay = (LinearLayout) findViewById(R.id.layout_wxpay);
        layout_alipay = (LinearLayout) findViewById(R.id.layout_alipay);
        img_wxpay_checked = (ImageView) findViewById(R.id.img_wxpay_checked);
        img_alipay_checked = (ImageView) findViewById(R.id.img_alipay_checked);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        tv_annual_pay = (TextView) findViewById(R.id.tv_annual_pay);

        tv_balance.setText(TextUtils.isEmpty(UserParams.INSTANCE.getBalance())?"0.00":UserParams.INSTANCE.getBalance());
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.annual_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        layout_wxpay.setOnClickListener(this);
        layout_alipay.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_wxpay:
                img_wxpay_checked.setImageResource(R.mipmap.ic_pay_mode_checked);
                img_alipay_checked.setImageResource(R.mipmap.ic_pay_mode_unchecked);
                payStrategy = wxpayStrategy;
                break;
            case R.id.layout_alipay:
                img_wxpay_checked.setImageResource(R.mipmap.ic_pay_mode_unchecked);
                img_alipay_checked.setImageResource(R.mipmap.ic_pay_mode_checked);
                payStrategy = alipayStrategy;
                break;
            case R.id.btn_submit:
                if (checkInput()) {
                    payStrategy.pay(null, payAmount, null, PayStrategy.TYPE_ANNUAL);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查输入内容是否合法
     *
     * @return
     */
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
