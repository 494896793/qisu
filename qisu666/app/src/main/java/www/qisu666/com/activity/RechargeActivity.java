package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.SorterFunction;
import www.qisu666.com.R;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.pay.AliPayStrategy;
import www.qisu666.com.pay.PayStrategy;
import www.qisu666.com.pay.WXPayStrategy;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.stationMap.JsonUtil;

//充电页面  付款
public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_amount)
    EditText et_amount;
    private TextView tv_balance;
    //    private EditText et_amount;
    private LinearLayout layout_wxpay;
    private LinearLayout layout_alipay;
    private ImageView img_wxpay_checked;
    private ImageView img_alipay_checked;
    private TextView btn_submit;
    private TextView tvCorolWx, tvCorolAi;
    private TextView tv_pay_get_fee;
    private RecyclerView rcv_recharge;
    private RechargeAdapter mAdapter;
    private List<Map<String, Object>> mData = new ArrayList<>();
    private String payAmount = "";//支付金额
    private String payGift = "0";
    private PayStrategy payStrategy;//支付方式
    private WXPayStrategy wxpayStrategy;
    private AliPayStrategy alipayStrategy;

    private int selectPosition = -1;//记录选中位置
    private boolean isSelectMode;//是否是选中状态
    private int textLength; //记录输入金额长度
    private static final int SDK_PAY_FLAG = 1;

    //支付宝支付结果处理
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new PaySuccessEvent());
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogHelper.alertDialog(RechargeActivity.this, getString(R.string.dialog_pay_alipay_8000));
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
        //支付成功  刷新账户余额
        connToServer();
//        finish();
    }

    @Subscribe
    public void onEventMainThread(PayFailEvent event) {
        DialogHelper.alertDialog(this, getString(R.string.dialog_pay_failed));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_recharge);

        //获取账户余额
        connToServer();
        requestData();

//        Log.e("aaaa", UserParams.INSTANCE.getUser_id());
//        Log.e("aaaa", UserParams.INSTANCE.getSex());
//        Log.e("aaaa", UserParams.INSTANCE.getS_token());
        wxpayStrategy = new WXPayStrategy(this);
        alipayStrategy = new AliPayStrategy(this, mHandler);
//        payStrategy = wxpayStrategy;
        payStrategy = alipayStrategy;

        initView();
        setListeners();
//        registerReceiver(finishReceiver, new IntentFilter(ReceiverAction.ACTION_PAY_SUCCESS));
        EventBus.getDefault().register(this);
    }

    private void connToServer() {
        String url = "api/my/order/getaccount";
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
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 map
                        Map m = JsonUtils.jsonToMap(s);
                        tv_balance.setText(m.get("totalmoney").toString());
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
    }

    private void requestData() {

        String url = "api/pay/donate";
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
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 list
                        List<String> list = JsonUtils.jsonToList(s);

                        mData.clear();
                        if (list != null && !"".equals(list.toString())) {
                            try {
                                JSONArray array = new JSONArray(list.toString());
                                int count = array.length();
                                if (count != 0) {
                                    for (int i = 0; i < count; i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        object.put("position", i);
                                        mData.add(JsonUtils.jsonToMap(object.toString()));
                                    }
                                }
                                mAdapter.setNewData(mData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initTitleBar();
        tv_balance = (TextView) findViewById(R.id.tv_balance);
//        et_amount = (EditText) findViewById(R.id.et_amount);
        layout_wxpay = findViewById(R.id.layout_wxpay);
        layout_alipay = findViewById(R.id.layout_alipay);
        img_wxpay_checked = (ImageView) findViewById(R.id.img_wxpay_checked);
        img_alipay_checked = (ImageView) findViewById(R.id.img_alipay_checked);
        btn_submit = (TextView) findViewById(R.id.btn_submit);

        tvCorolWx = (TextView) findViewById(R.id.tv_corol_wxpay);
        tvCorolAi = (TextView) findViewById(R.id.tv_corol_alipay);

        tv_pay_get_fee = (TextView) findViewById(R.id.tv_pay_get_fee);
        rcv_recharge = (RecyclerView) findViewById(R.id.rcv_recharge);

        tv_balance.setText(TextUtils.isEmpty(UserParams.INSTANCE.getBalance()) ? "0.00" : UserParams.INSTANCE.getBalance());

        mAdapter = new RechargeAdapter(null);

        List<Map<String, Object>> list = new ArrayList<>();


        final GridLayoutManager mgr = new GridLayoutManager(this, 2);
        //VERTICAL纵向，类似ListView，HORIZONTAL<span style="font-family: Arial, Helvetica, sans-serif;">横向，类似Gallery</span>
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_recharge.setLayoutManager(mgr);
        rcv_recharge.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int i) {
                Map<String, Object> item = (Map<String, Object>) adapter.getItem(i);
                isSelectMode = true;
                selectPosition = i;
                if (checkInput(item.get("minimumRecharge").toString())) {
                    textLength = payAmount.length();
                    et_amount.setText(payAmount);
//                    Log.w("guanglog",textLength + " textLength");
//                    payStrategy.pay(null, payAmount, null, PayStrategy.TYPE_RECHARGE,payGift);
                }
                mAdapter.notifyDataSetChanged();

            }
        });


//        et_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ToastUtil.showToast("Focus Change");
//            }
//        });

        //监听EditText数据变化
        RxTextView.afterTextChangeEvents(et_amount)
                .map(new Function<TextViewAfterTextChangeEvent, String>() {
                    @Override
                    public String apply(TextViewAfterTextChangeEvent event) throws Exception {
                        return et_amount.getText().toString();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        tv_pay_get_fee.setText(TextUtils.isEmpty(value) ? "到账金额" : "到账金额" + value + "元");
                        payAmount = et_amount.getText().toString();
                        payGift = "0";

                        int payLength = et_amount.getText().toString().trim().length();
//                        Log.w("guanglog",textLength + " textLength in next");
                        if (isSelectMode && textLength != payLength) {
                            isSelectMode = false;
                            mAdapter.notifyDataSetChanged();
                            textLength = payLength;
                        }
                        if (!TextUtils.isEmpty(payAmount)) {
                            int pay = Integer.parseInt(payAmount);
                            if (mData != null && mData.size() > 0) {
                                int maxLevel = 0;
                                for (Map<String, Object> map : mData) {
                                    int minLevel = Integer.parseInt(map.get("minimumRecharge").toString());
                                    if (pay >= minLevel && minLevel >= maxLevel) {
                                        int giftFee = Integer.parseInt(map.get("giftQuota").toString());
//                                        ToastUtil.showToast(giftFee + "");
                                        tv_pay_get_fee.setText("到账金额" + (pay + giftFee) + "元");
                                        payGift = giftFee + "";
                                        if (minLevel >= maxLevel) {
                                            maxLevel = minLevel;
                                        }
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void testData() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("minimumRecharge", "100");
            map.put("giftQuota", "20");
            list.add(map);
        }
        mAdapter.setNewData(list);

    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.recharge_title);
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
                img_wxpay_checked.setImageResource(R.mipmap.rg_132);
                tvCorolWx.setTextColor(ContextCompat.getColor(this, R.color.new_primary));

                img_alipay_checked.setImageResource(R.mipmap.rg_12);
                tvCorolAi.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                payStrategy = wxpayStrategy;
                break;

            case R.id.layout_alipay:
                img_wxpay_checked.setImageResource(R.mipmap.rg_13);
                tvCorolWx.setTextColor(ContextCompat.getColor(this, R.color.text_gray));

                img_alipay_checked.setImageResource(R.mipmap.rg_122);
                tvCorolAi.setTextColor(ContextCompat.getColor(this, R.color.new_primary));
                payStrategy = alipayStrategy;
                break;
            case R.id.btn_submit:
                if (checkInput(et_amount.getText().toString().trim())) {
                    payStrategy.pay(null, payAmount, null, PayStrategy.TYPE_RECHARGE, payGift);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查输入内容是否合法
     */
    private boolean checkInput(String pay) {
        payAmount = pay;
        if (TextUtils.isEmpty(payAmount)) {
            ToastUtil.showToast(R.string.toast_pay_recharge_amount_is_null);
        } else if (Integer.valueOf(payAmount) <= 0 || Integer.valueOf(payAmount) > 100000) {
            ToastUtil.showToast(R.string.toast_pay_recharge_amount_illegal);
        } else {
            return true;
        }
        return false;
    }

    private class RechargeAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {
        public RechargeAdapter(List data) {
            super(R.layout.item_select_charge, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<String, Object> item) {
            int pay = Integer.parseInt(item.get("minimumRecharge").toString());
            int payGift = Integer.parseInt(item.get("giftQuota").toString());
            int position = Integer.parseInt(item.get("position").toString());
            helper.setText(R.id.tv_pay, pay + "元");
            helper.setText(R.id.tv_pay_gift, "到账" + (pay + payGift) + "元");
            LinearLayout llOut = helper.getView(R.id.ll_select_out);
            if (isSelectMode
                    && selectPosition == position) {
                llOut.setSelected(true);
                helper.setTextColor(R.id.tv_pay, ContextCompat.getColor(getApplication(), R.color.bg_white));
                helper.setTextColor(R.id.tv_pay_gift, ContextCompat.getColor(getApplication(), R.color.orange));
            } else {
                llOut.setSelected(false);
                helper.setTextColor(R.id.tv_pay, getResources().getColor(R.color.bg_white));
                helper.setTextColor(R.id.tv_pay_gift, getResources().getColor(R.color.text_gray));
            }
        }
    }

}
