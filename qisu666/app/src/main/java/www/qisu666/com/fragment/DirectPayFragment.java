package www.qisu666.com.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.PayResult;
import www.qisu666.com.pay.AliPayStrategy;
import www.qisu666.com.pay.PayStrategy;
import www.qisu666.com.pay.WXPayStrategy;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.DialogHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectPayFragment extends Fragment implements View.OnClickListener {

    private View view;

    private RadioGroup radio_group;
    private RadioButton radio_amount_1;
    private RadioButton radio_amount_2;
    private RadioButton radio_amount_3;
    private int currentRadioButtonId = -1;

    private EditText et_amount;
    private LinearLayout layout_wxpay;
    private LinearLayout layout_alipay;
    private ImageView img_wxpay_checked;
    private ImageView img_alipay_checked;
    private TextView btn_submit;

    //支付金额
    private String payAmount = "";
    //桩号
    private String charge_pile_seri;
    //枪号
    private String charge_pile_num;

    //支付方式
    private PayStrategy payStrategy;
    private WXPayStrategy wxpayStrategy;
    private AliPayStrategy alipayStrategy;


//    //支付信息
//    private String payInfo;
//    //商户网站唯一订单号
//    private String out_trade_no;

    private static final int SDK_PAY_FLAG = 1;

    private static final int PAY_COMFIRM = 2;

    // 微信支付
//    private IWXAPI api;
//    private PayReq req;


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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new PaySuccessEvent());
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogHelper.alertDialog(getActivity(), getString(R.string.dialog_pay_alipay_8000));
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


    public DirectPayFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String charge_pile_seri, String charge_pile_num){
        DirectPayFragment fragment = new DirectPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("charge_pile_seri", charge_pile_seri);
        bundle.putString("charge_pile_num", charge_pile_num);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_direct_pay, container, false);

        wxpayStrategy = new WXPayStrategy(getActivity());
        alipayStrategy = new AliPayStrategy(getActivity(), mHandler);
        payStrategy = wxpayStrategy;

        if(getArguments()!=null){
            charge_pile_seri = getArguments().getString("charge_pile_seri");
            charge_pile_num = getArguments().getString("charge_pile_num");
        }
        initView();
        setListeners();
        return view;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        radio_group = (RadioGroup) view.findViewById(R.id.radio_group);
        radio_amount_1 = (RadioButton) view.findViewById(R.id.radio_amount_1);
        radio_amount_2 = (RadioButton) view.findViewById(R.id.radio_amount_2);
        radio_amount_3 = (RadioButton) view.findViewById(R.id.radio_amount_3);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        layout_wxpay = (LinearLayout) view.findViewById(R.id.layout_wxpay);
        layout_alipay = (LinearLayout) view.findViewById(R.id.layout_alipay);
        img_wxpay_checked = (ImageView) view.findViewById(R.id.img_wxpay_checked);
        img_alipay_checked = (ImageView) view.findViewById(R.id.img_alipay_checked);
        btn_submit = (TextView) view.findViewById(R.id.btn_submit);
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        layout_wxpay.setOnClickListener(this);
        layout_alipay.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        radio_amount_1.setOnClickListener(this);
        radio_amount_2.setOnClickListener(this);
        radio_amount_3.setOnClickListener(this);

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("") && currentRadioButtonId != -1) {
                    radio_group.check(currentRadioButtonId);
                    LogUtils.d(((RadioButton) view.findViewById(R.id.radio_amount_3)).isChecked() + "");
                } else if (!s.toString().equals("")) {
                    if (currentRadioButtonId != -1) {
                        radio_group.clearCheck();
                        currentRadioButtonId = -1;
                    }

                    payAmount = s.toString();
                }
//                LogUtils.d("currentRadioButtonId="+currentRadioButtonId);
//                LogUtils.d("RadioGroup:"+((RadioButton)findViewById(R.id.radio_amount_1)).isChecked()+","+((RadioButton)findViewById(R.id.radio_amount_2)).isChecked()
//                        +","+((RadioButton)findViewById(R.id.radio_amount_3)).isChecked());
            }
        });

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
                    ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
//                    payStrategy.pay(charge_pile_seri, "0.01", charge_pile_num, PayStrategy.TYPE_CONSUME);
                    payStrategy.pay(charge_pile_seri, String.valueOf(Integer.valueOf(payAmount)), charge_pile_num, PayStrategy.TYPE_CONSUME);
                }
                break;
            case R.id.radio_amount_1:
            case R.id.radio_amount_2:
            case R.id.radio_amount_3:
                onRadioButonClick(v);
                break;
        }
    }

    /**
     * 检查输入内容是否合法
     *
     * @return
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(payAmount)) {
            ToastUtil.showToast(R.string.toast_pay_amount_is_null);
        } else if (Integer.valueOf(payAmount) <= 0 || Integer.valueOf(payAmount) > 10000) {
            ToastUtil.showToast(R.string.toast_pay_amount_illegal);
        } else {
            return true;
        }
        return false;
    }

    private void onRadioButonClick(View v) {
        currentRadioButtonId = v.getId();
        et_amount.setText("");
//        ((RadioButton)v).setChecked(true);
        String text = ((RadioButton) v).getText().toString();
        payAmount = text.substring(0, text.length() - 1);
    }

//    /**
//     * 发送 E101 请求，获取支付宝支付订单信息
//     */
//    private void connToServerAlipay(String total_fee) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "E101");
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("appenv", "android" + PhoneParams.getOsVersion());
//            jsonObject.put("charge_pile_seri", charge_pile_seri);
//            jsonObject.put("total_fee", total_fee);
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token() == null ? "" : UserParams.INSTANCE.getS_token());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        HttpLogic httpLogic = new HttpLogic(getActivity());
//        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                /**
//                 * 订单信息
//                 */
//                LogUtils.e(map.toString());
//                alipay(map);
//            }
//        });
//    }
//
//    private void alipay(Map<String, Object> map) {
//        out_trade_no = map.get("out_trade_no").toString();
//        /**
//         * 完整的符合支付宝参数规范的订单信息
//         */
////        out_trade_no = map.get("out_trade_no").toString();
//        payInfo = map.get("payorderstring").toString();
////        payInfo = payInfo.replace("\\", "");
//        LogUtils.e("payinfo:" + payInfo);
//        //异步发起支付请求
//        new AlipayAsyncTask().execute();
//
//    }
//
//    /**
//     * 发送 E107 请求，获取微信支付订单信息
//     */
//    private void connToServerWxpay(String total_fee) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "E107");
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("total_fee", total_fee);
//            jsonObject.put("charge_pile_seri", charge_pile_seri);
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//            jsonObject.put("spBillCreateIP", NetworkUtils.getPhoneIp());
//            jsonObject.put("user_name", UserParams.INSTANCE.getCust_alias());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        HttpLogic httpLogic = new HttpLogic(getActivity());
//        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                /**
//                 *  E107 response:{"timestamp":"1469589211","noncestr":"luqhzgxcejw4z9hqw3pv6ynd6nuraaf0","partnerid":"1367228502","prepayid":"wx201607271112445eb916bf7b0166301682",
//                 *                  "return_msg":"操作成功.","package":"Sign=WXPay","appid":"wx73603327e87caa38","out_trade_no":"20160727111331000478","return_code":"0000",
//                 *                  "paysign":"281226436023852F774F0299CB967991"}
//                 */
////                LogUtils.e(map.toString());
//                String toast;
//                if (null != map && map.size() > 0) {
//                    req = new PayReq();
//                    req.appId = map.get("appid").toString();
//                    req.nonceStr = map.get("noncestr").toString();
//                    req.packageValue = map.get("package").toString();
//                    req.partnerId = map.get("partnerid").toString();
//                    req.prepayId = map.get("prepayid").toString();
//                    req.timeStamp = map.get("timestamp").toString();
//                    req.sign = map.get("paysign").toString();
//
//                    UserParams.INSTANCE.setOut_trade_no(map.get("out_trade_no").toString());
////                    req.extData		= "app data"; // optional
//                    LogUtils.d("req.checkArgs():" + req.checkArgs());
//                    toast = "正常调起支付";
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    new WxpayAsyncTask().execute();
//                } else {
//                    LogUtils.d("获取订单失败" + map.get("return_msg").toString());
//                    toast = "获取订单失败";
//                }
//                ToastUtil.showToast(toast);
//            }
//        });
//
//        /*String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//
//        JsonStringRequest request = HttpRequestManager.newGetStringRequest(url, new ResponseListener() {
//            @Override
//            public void onResponse(String response) {
//                Map<String,Object> map = JsonUtils.jsonToMap(response);
//                LogUtils.e(map.toString());
//                String toast;
//                if(null != map && !map.containsKey("retcode") ){
//                    req = new PayReq();
//                    req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
////                    req.appId			= map.get("appid").toString();
//                    req.partnerId		= map.get("partnerid").toString();
//                    req.prepayId		= map.get("prepayid").toString();
//                    req.nonceStr		= map.get("noncestr").toString();
//                    req.timeStamp		= map.get("timestamp").toString();
//                    req.packageValue	= map.get("package").toString();
//                    req.sign			= map.get("sign").toString();
//                    req.extData		= "app data"; // optional
//                    toast =  "正常调起支付";
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    new WxpayAsyncTask().execute();
//                }else{
//                    LogUtils.d("返回错误"+map.get("retmsg").toString());
//                    toast =  "返回错误"+map.get("retmsg").toString();
//                }
//                ToastUtil.showToast(toast);
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                if(error!=null){
//                    LogUtils.e(error.getMessage());
//                }
//            }
//        });
//        IDianNiuApp.addRequest(request,"TestWxpay");*/
//    }
//
//    class AlipayAsyncTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            // 构造PayTask 对象
//            PayTask alipay = new PayTask(getActivity());
//            // 调用支付接口，获取支付结果
//            String result = alipay.pay(payInfo, true);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Message msg = mHandler.obtainMessage();
//            msg.what = SDK_PAY_FLAG;
//            msg.obj = result;
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    class WxpayAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//            boolean flag = api.sendReq(req);
//            LogUtils.e("api.sendReq(req):" + flag);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
//    }

}
