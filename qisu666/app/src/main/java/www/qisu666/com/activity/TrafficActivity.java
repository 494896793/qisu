package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
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
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.times3.Activity_ShenFen2JiaShi;

/**
 * 717219917@qq.com 2018/7/5 15:33.
 * // 交通保证金
 */
public class TrafficActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;
    @BindView(R.id.tv_qian)
    TextView tvQian;
    @BindView(R.id.tv_jiaona)
    Button tvJiaona;
    @BindView(R.id.tv_meeage)
    TextView tvMeeage;

    //支付金额
    private String payAmount = "";
    private String payGift = "0";
    //支付方式
    private NewPayStrategy newPayStrategy;
    private NewWXPayStrategy newWXPayStrategy;
    private NewAliPayStrategy newAliPayStrategy;

    private static final int SDK_PAY_FLAG = 1;
    private PopupWindow mpWindow;
    // 状态type
    private int type;

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

    @Subscribe
    public void onEventMainThread(String event) {
        if (event != null) {
            if ("撤销申请成功".equals(event) || "申请已提交".equals(event)) {
                requestPay();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_traffic);
        tvTitle.setText("交通违法保证金");

        //微信会员押金提交  B114
        newWXPayStrategy = new NewWXPayStrategy(this, "B114");
        newAliPayStrategy = new NewAliPayStrategy(this, "B112", mHandler);
//        newPayStrategy = newWXPayStrategy;
        newPayStrategy = newAliPayStrategy;

        requestPay();
        EventBus.getDefault().register(this);
    }

    private void requestPay() {

        final String url = "api/user/user/deposit";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Object bean) {

                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 map
                        Map jsonToMap = JsonUtils.jsonToMap(s);

                        switch (jsonToMap.get("despositStatus").toString()) {
                            case "1":
                                payAmount = jsonToMap.get("depositMoney").toString();
                                tvQian.setText(payAmount);
                                type = 1;
                                break;
                            case "2":
                                payAmount = jsonToMap.get("depositMoneyOn").toString();
                                tvQian.setText(payAmount);
                                tvMeeage.setText("交通违法保证金(元)-退款中");
                                tvQian.setTextColor(ContextCompat.getColor(TrafficActivity.this, R.color.text_gray));
                                tvJiaona.setText("撤销申请");
                                type = 2;
                                break;
                            case "3":
                                payAmount = jsonToMap.get("depositMoneyOn").toString();
                                tvQian.setText(payAmount);
                                tvMeeage.setText("已缴纳（元）");
                                tvQian.setTextColor(ContextCompat.getColor(TrafficActivity.this, R.color.orange));
                                tvJiaona.setText("申请退款");
                                type = 3;
                                break;
                            case "4":
                                payAmount = jsonToMap.get("depositMoneyOn").toString();
                                tvQian.setText(payAmount);
                                tvMeeage.setText("已缴纳（元）");
                                tvQian.setTextColor(ContextCompat.getColor(TrafficActivity.this, R.color.orange));
                                tvJiaona.setText("申请退款");
                                type = 4;
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    @OnClick({R.id.img_title_left, R.id.btn_title_right, R.id.tv_qian, R.id.tv_jiaona})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.tv_jiaona:
                switch (type) {
                    case 1:
                        showPopwin(view);
                        break;
                    case 2:
                        getQuXiao();
                        break;
                    case 3:
                        getTuikuan();
                        break;
                    case 4:
                        getTuikuan();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    // 撤销申请
    private void getQuXiao() {

        DialogHelper.confirmDialog(TrafficActivity.this, "确定撤销申请吗！", new AlertDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirm() {

                String url = "api/pay/deposit/back/refund";
                HashMap<String, Object> map = new HashMap<>();
                map.put("userCode", UserParams.INSTANCE.getUser_id());

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
                                ToastUtil.showToast("撤销申请成功");
                                EventBus.getDefault().post("撤销申请成功");
                            }

                            @Override
                            public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                                ToastUtil.showToast(bean.msg);
                                Log.e("aaaa", "获取失败：" + bean.toString());
                            }


                        });
            }

            @Override
            public void onCancel() {
            }
        });
    }

    // 申请退款
    private void getTuikuan() {

        DialogHelper.confirmDialog(TrafficActivity.this, "确定申请退款吗！", new AlertDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirm() {

                String url = "api/pay/deposit/refund/apply";
                HashMap<String, Object> map = new HashMap<>();
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
                                ToastUtil.showToast("申请已提交");
                                EventBus.getDefault().post("申请已提交");
                            }

                            @Override
                            public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                                ToastUtil.showToast(bean.msg);
                                Log.e("aaaa", "获取失败：" + bean.toString());
                            }


                        });
            }

            @Override
            public void onCancel() {
            }
        });


    }


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
        mpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
        mpWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mpWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        // 设置弹出窗体的背景
//        mpWindow.setBackgroundDrawable(dw);

        //添加pop窗口关闭事件
//        mpWindow.setOnDismissListener(new poponDismissListener());

        LinearLayout alipay = layout.findViewById(R.id.layout_alipay);
        final ImageView icon = layout.findViewById(R.id.ali_icon);
        final TextView font = layout.findViewById(R.id.ali_font);

        LinearLayout wxpay = layout.findViewById(R.id.layout_wxpay);
        final ImageView iconWx = layout.findViewById(R.id.wx_icon);
        final TextView fontWx = layout.findViewById(R.id.wx_font);

        TextView tvsuitb = layout.findViewById(R.id.tv_submit);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
