package www.qisu666.com.pay;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/3.
 */
public class NewWXPayStrategy implements NewPayStrategy{

    // 微信支付
    private IWXAPI api;
    private PayReq req;

    private Context context;

    private String requestNum;

    public NewWXPayStrategy(Context context, String requestNum) {
        //微信初始化
        api = WXAPIFactory.createWXAPI(context, "wxf46055df3f9eafeb");
        this.context = context;
        this.requestNum = requestNum;
    }

    @Override
    public void pay(String charge_pile_seri, String payAmount, String charge_pile_num, String type, String fee_gift) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(isPaySupported){
            connToServerWxpay(charge_pile_seri, payAmount, charge_pile_num, type,fee_gift);
        } else {
            DialogHelper.alertDialog(context, context.getString(R.string.dialog_app_wx_version_is_too_old));
        }
    }

    /**
     * 发送 E107 请求，获取微信支付订单信息
     */
    private void connToServerWxpay( String charge_pile_seri, String total_fee, String charge_pile_num, final String type,String fee_gift) {

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
                    }

                });



//        JSONObject jsonObject = new JSONObject();
//        if (TextUtils.isEmpty(requestNum)){
//            throw new IllegalArgumentException("requestNum can not be empty");
//        }
//        try {
//            jsonObject.put("req_code", requestNum);
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("total_fee", total_fee);
//            jsonObject.put("charge_pile_seri", charge_pile_seri);
//            jsonObject.put("charge_pile_num", charge_pile_num);
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//            jsonObject.put("spBillCreateIP", NetworkUtils.getPhoneIp());
//            jsonObject.put("user_name", UserParams.INSTANCE.getCust_alias());
//            jsonObject.put("fee_gift", fee_gift);
//            jsonObject.put("pay_type", type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        HttpLogic httpLogic = new HttpLogic(context);
//        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//
//            @Override
//            public void onResponse(Map<String,Object> map, String tag) {
//                /**
//                 *  E107 response:{"timestamp":"1469589211","noncestr":"luqhzgxcejw4z9hqw3pv6ynd6nuraaf0","partnerid":"1367228502","prepayid":"wx201607271112445eb916bf7b0166301682",
//                 *                  "return_msg":"操作成功.","package":"Sign=WXPay","appid":"wx73603327e87caa38","out_trade_no":"20160727111331000478","return_code":"0000",
//                 *                  "paysign":"281226436023852F774F0299CB967991"}
//                 */
////                LogUtils.e(map.toString());
//
//                if(null != map && map.size()>0 ){
//                    req = new PayReq();
//                    req.appId			= map.get("appid").toString();
//                    req.nonceStr		= map.get("noncestr").toString();
//                    req.packageValue	= map.get("package").toString();
//                    req.partnerId		= map.get("partnerid").toString();
//                    req.prepayId		= map.get("prepayid").toString();
//                    req.timeStamp		= map.get("timestamp").toString();
//                    req.sign			= map.get("paysign").toString();
//                    if(type.equals("0")) {
//                        UserParams.INSTANCE.setOut_trade_no(map.get("out_trade_no").toString());
//                    }
////                    req.extData		= "app data"; // optional
//                    LogUtil.e("req.checkArgs():"+req.checkArgs());
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    new WxpayAsyncTask().execute();
//                }else{
//                    LogUtil.e("获取订单失败"+map.get("return_msg").toString());
//                    ToastUtil.showToast(R.string.toast_pay_wxpay_get_order_failed);
//                }
//
//            }
//        });

        /*String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";

        JsonStringRequest request = HttpRequestManager.newGetStringRequest(url, new ResponseListener() {
            @Override
            public void onResponse(String response) {
                Map<String,Object> map = JsonUtils.jsonToMap(response);
                LogUtils.e(map.toString());
                String toast;
                if(null != map && !map.containsKey("retcode") ){
                    req = new PayReq();
                    req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                    req.appId			= map.get("appid").toString();
                    req.partnerId		= map.get("partnerid").toString();
                    req.prepayId		= map.get("prepayid").toString();
                    req.nonceStr		= map.get("noncestr").toString();
                    req.timeStamp		= map.get("timestamp").toString();
                    req.packageValue	= map.get("package").toString();
                    req.sign			= map.get("sign").toString();
                    req.extData		= "app data"; // optional
                    toast =  "正常调起支付";
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    new WxpayAsyncTask().execute();
                }else{
                    LogUtils.d("返回错误"+map.get("retmsg").toString());
                    toast =  "返回错误"+map.get("retmsg").toString();
                }
                ToastUtil.showToast(toast);
            }

            @Override
            public void onError(VolleyError error) {
                if(error!=null){
                    LogUtils.e(error.getMessage());
                }
            }
        });
        IDianNiuApp.addRequest(request,"TestWxpay");*/
    }

    class WxpayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean flag = api.sendReq(req);
            LogUtil.e("api.sendReq(req):"+flag);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
