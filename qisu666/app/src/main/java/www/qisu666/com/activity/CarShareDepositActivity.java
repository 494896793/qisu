package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import www.qisu666.com.R;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.pay.NewAliPayStrategy;
import www.qisu666.com.pay.NewPayStrategy;
import www.qisu666.com.pay.NewWXPayStrategy;
import www.qisu666.com.pay.PayStrategy;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.carshare.bean.Bean_deposit;

//押金充值
public class CarShareDepositActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_deposit_pay)
    TextView tvDepositPay;
    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private PopupWindow mpWindow;

    //支付金额
    private String payAmount = "";
    private String payGift = "0";
    //支付方式
    private NewPayStrategy newPayStrategy;
    private NewWXPayStrategy newWXPayStrategy;
    private NewAliPayStrategy newAliPayStrategy;

    private static final int SDK_PAY_FLAG = 1;
    private String carCode;

    //支付宝支付结果处理
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            try {
                LogUtil.e("支付结果 msg:" + msg);
            } catch (Throwable t) {
                t.printStackTrace();
            }//
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
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogHelper.alertDialog(mContext, getString(R.string.dialog_pay_alipay_8000));
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
        setView(R.layout.activity_car_share_deposit);

        //微信会员押金提交  B114
        newWXPayStrategy = new NewWXPayStrategy(this, "B114");
        newAliPayStrategy = new NewAliPayStrategy(this, "B112", mHandler);
//        newPayStrategy = newWXPayStrategy;
        newPayStrategy = newAliPayStrategy;

        initView();
        requestPay();
        EventBus.getDefault().register(this);
    }

    private void requestPay() {

        final String url = "api/user/user/deposit";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        requestMap.put("carCode", carCode);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Bean_deposit.class))
                .compose(RxNetHelper.<Bean_deposit>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Bean_deposit>(mLoadingDialog) {

                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override
                    public void onSuccess(Bean_deposit bean) {

                        if ("1".equals(bean.getDespositStatus())) {
                            payAmount = bean.getDepositMoney();
                            tvDepositPay.setText(payAmount);
                        }

                        if ("4".equals(bean.getDespositStatus())) {
                            payAmount = bean.getDepositMoney();
                            tvDepositPay.setText(payAmount);
                            tvMsg.setText("交通违法保证金（元）-保证金不足，请补缴");
                        }

                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Bean_deposit> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
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
        tvTitle.setText("交通违法保证金充值");
        carCode = getIntent().getStringExtra("carCode");
    }


    @OnClick({R.id.img_title_left, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                showPopwin(view);
                break;
            default:
                break;
        }
    }

    @Nullable
    @Optional
    private void showPopwin(View view) {
        if (mpWindow != null && mpWindow.isShowing()) {
            return;
        }

        // 一个自定义的布局，作为显示的内容
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_payment_popup, null);
        mpWindow = new PopupWindow(layout,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

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
        mpWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
        mpWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mpWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mpWindow.setBackgroundDrawable(dw);

        LinearLayout alipay = layout.findViewById(R.id.layout_alipay);
        final ImageView icon = layout.findViewById(R.id.ali_icon);
        final TextView font = layout.findViewById(R.id.ali_font);

        LinearLayout wxpay = layout.findViewById(R.id.layout_wxpay);
        final ImageView iconWx = layout.findViewById(R.id.wx_icon);
        final TextView fontWx = layout.findViewById(R.id.wx_font);


        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.mipmap.yc_43);
                font.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main_background));

                iconWx.setImageResource(R.mipmap.yc_44);
                fontWx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_gray));

                newPayStrategy = newAliPayStrategy;
            }
        });
        wxpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击微信的时候  渲染控件的颜色
                icon.setImageResource(R.mipmap.yc_42);
                font.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_gray));

                iconWx.setImageResource(R.mipmap.yc_45);
                fontWx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main_background));

                newPayStrategy = newWXPayStrategy;

            }
        });


        TextView tvsuitb = layout.findViewById(R.id.tv_submit);
        tvsuitb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    newPayStrategy.pay(null, payAmount, null, PayStrategy.TYPE_DEPOSIT, payGift);
                }
            }
        });

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
