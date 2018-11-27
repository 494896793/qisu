package www.qisu666.com.pay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/3.
 */
public class NewAliPayStrategy implements NewPayStrategy{

    //支付信息
    private String payInfo;
    //商户网站唯一订单号
    private String out_trade_no;
    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler;

    private Context context;

    private String requestNum;

    public NewAliPayStrategy(Context context, String requestNum, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        this.requestNum = requestNum;
    }

    @Override
    public void pay(String charge_pile_seri, String payAmount, String charge_pile_num, String type, String fee_gift) {
        connToServerAlipay(charge_pile_seri, payAmount, charge_pile_num, type,fee_gift);
    }

    /**
     * 发送 E101 请求，获取支付宝支付订单信息
     */
    private void connToServerAlipay( String charge_pile_seri, String total_fee, String charge_pile_num, final String type,final  String fee_gift) {

        String url = "api/pay/charge/alipay";
        final HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("totalFee", total_fee);
        map.put("spbillCreateIp", NetworkUtils.getPhoneIp());
        map.put("feeGift", fee_gift);
        map.put("payType", type);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(String.class))
                .compose(RxNetHelper.<String>io_main())
                .subscribe(new ResultSubscriber<String>() {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(String bean) {

                        // 对象转json
//                        String s = JsonUtils.objectToJson(bean);
                        Log.e("aaaa", "s:" + bean);
                        // json转 map
//                        Map jsonToMap = JsonUtils.jsonToMap(s);
//                        /*
//                          订单信息
//                         */
//                        Log.e("aaaa", "asd:" + jsonToMap.toString());
                        alipay(bean);
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<String> bean) {

                    }
                });


//        JSONObject jsonObject = new JSONObject();
//        if (TextUtils.isEmpty(requestNum)){
//            throw new IllegalArgumentException("requestNum can not be empty");
//        }
//        try {
//            jsonObject.put("req_code", requestNum);
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("appenv", "android" + PhoneParams.getOsVersion());
//            jsonObject.put("charge_pile_seri", charge_pile_seri);
//            jsonObject.put("charge_pile_num", charge_pile_num);
//            jsonObject.put("total_fee", total_fee);
//            jsonObject.put("fee_gift", fee_gift);
//            jsonObject.put("pay_type", type);
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token() == null ? "" : UserParams.INSTANCE.getS_token());
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
//                 * 订单信息
//                 */
//                LogUtil.e(map.toString());
//                alipay(map, type);
//            }
//        });
    }

    private void alipay(String s) {
//        out_trade_no = map.get("out_trade_no").toString();
//        if(type.equals("0")) {
//            UserParams.INSTANCE.setOut_trade_no(out_trade_no);
//        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
//        out_trade_no = map.get("out_trade_no").toString();
        payInfo = s;
//        payInfo = payInfo.replace("\\", "");
        LogUtil.e("payinfo:" + payInfo);
        //异步发起支付请求
        new AlipayAsyncTask().execute();

    }

    class AlipayAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // 构造PayTask 对象
            PayTask alipay = new PayTask((Activity) context);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo, true);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Message msg = mHandler.obtainMessage();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

}
