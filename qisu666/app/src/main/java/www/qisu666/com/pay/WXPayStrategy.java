package www.qisu666.com.pay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;

/**
 * Created by Administrator on 2016/8/3.
 */
public class WXPayStrategy implements PayStrategy {

    // 微信支付
    private IWXAPI api;
    private PayReq req;

    private Context context;

    private String feeGift;

    public WXPayStrategy(Context context) {
        //微信初始化
        api = WXAPIFactory.createWXAPI(context, "wxf46055df3f9eafeb", false);
        this.context = context
        ;
    }

    public void setFeeGift(String gift) {
        feeGift = gift;
    }

    @Override
    public void pay(String charge_pile_seri, String payAmount, String charge_pile_num, String type) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (isPaySupported) {
            connToServerWxpay(charge_pile_seri, payAmount, charge_pile_num, type, "0");
        } else {
            DialogHelper.alertDialog(context, context.getString(R.string.dialog_app_wx_version_is_too_old));
        }
    }

    @Override
    public void pay(String charge_pile_seri, String payAmount, String charge_pile_num, String type, String fee_gift) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (isPaySupported) {
            connToServerWxpay(charge_pile_seri, payAmount, charge_pile_num, type, fee_gift);
        } else {
            DialogHelper.alertDialog(context, context.getString(R.string.dialog_app_wx_version_is_too_old));
        }
    }

    /**
     * 发送 E107 请求，获取微信支付订单信息
     */
    private void connToServerWxpay(String charge_pile_seri, String total_fee, String charge_pile_num, final String type, String fee_gift) {

        String url = "api/pay/charge/wxpay";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("totalFee", total_fee);
        map.put("spbillCreateIp", NetworkUtils.getPhoneIp());
        map.put("feeGift", fee_gift);
        map.put("payType", type);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(Object bean) {

                        Log.e("aaa", "bean:" + bean);

                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        Log.e("aaa", "s:" + s);
                        // json转 map
                        Map jsonToMap = JsonUtils.jsonToMap(s);
                        Log.e("aaa", "jsonToMap:" + jsonToMap);
                        if (null != jsonToMap && jsonToMap.size() > 0) {
                            req = new PayReq();
                            req.appId = jsonToMap.get("appid").toString();
                            req.nonceStr = jsonToMap.get("noncestr").toString();
                            req.packageValue = jsonToMap.get("package").toString();
                            req.partnerId = jsonToMap.get("partnerid").toString();
                            req.prepayId = jsonToMap.get("prepayid").toString();
                            req.timeStamp = jsonToMap.get("timestamp").toString();
                            req.sign = jsonToMap.get("paysign").toString();

                            if (type.equals("0")) {
                                UserParams.INSTANCE.setOut_trade_no(jsonToMap.get("out_trade_no").toString());
                            }
//                    req.extData		= "app data"; // optional
//                    LogUtil.e("req.checkArgs():"+req.checkArgs());
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            new WxpayAsyncTask().execute();
                        } else {
                            assert jsonToMap != null;
                            LogUtil.e("获取订单失败" + jsonToMap.get("return_msg").toString());
                            ToastUtil.showToast(R.string.toast_pay_wxpay_get_order_failed);
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                        Log.i("===","==");
                        if(bean.code==-1001&&bean.msg.equals("系统异常")){
                            ToastUtil.showToast("该账户异常，请联系客服");
                        }
                    }

                });
    }

    class WxpayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean flag = api.sendReq(req);
            LogUtil.e("api.sendReq(req):" + flag);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
