package www.qisu666.sdk.partner.pay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.sdk.partner.bean.Bean_Order;
import www.qisu666.sdk.partner.bean.Product;

/**
 * 717219917@qq.com 2018/4/17 16:45.
 *///认购 阿里支付
public class CarBuy_NewAliPayStrategy  implements CarBuy_NewPayStrategy {
        private String payInfo;
        private String out_trade_no;//商户网站唯一订单号
        private static final int SDK_PAY_FLAG = 1;
        private Handler mHandler;
        private Context context;
        private String requestNum;

    public CarBuy_NewAliPayStrategy(Context context, String requestNum, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        this.requestNum = requestNum;
    }

        /**  获取支付宝支付订单信息*/
        private void connToServerAlipay( String userId, String total_fee, String productCode,String subAmount,String subCount,String subType) {
            String url = "api/pay/ali/pay";
            HashMap<String, Object> map = new HashMap<>();
            map.put("productCode", productCode);
            map.put("userId", UserParams.INSTANCE.getUser_id());
            map.put("totalFee", total_fee);
            map.put("subType", subType);
            map.put("subCount", subCount);
            map.put("subAmount", subAmount);

            MyNetwork.getMyApi()
                    .carRequest(url, MyMessageUtils.addBody(map))
                    .map(new FlatFunction<>(Bean_Order.class))
                    .compose(RxNetHelper.<Bean_Order>io_main())
                    .subscribe(new ResultSubscriber<Bean_Order>() {
                        @Override
                        public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                        }

                        @Override public void onSuccess(Bean_Order bean_resu) {
                            LogUtil.e("获取认购详情成功"+bean_resu.getOrderNo());
                            LogUtil.e("获取认购详情成功"+bean_resu.getRequestParam());
                            try{SPUtil.put(context,"orderNo",bean_resu.getOrderNo());}catch (Throwable t){t.printStackTrace();}
                            alipay(bean_resu.getRequestParam());
                        }

                        @Override public void onFail(www.qisu666.com.carshare.Message<Bean_Order> bean) {
                            LogUtil.e("获取认购详情失败"+bean.msg);
                        }

                    });



    }

    //拿到信息  并 发起支付请求
        private void alipay(Map<String, Object> map, String type) {
        out_trade_no = map.get("out_trade_no").toString();
        if(type.equals("0")) {
            UserParams.INSTANCE.setOut_trade_no(out_trade_no);
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
//        out_trade_no = map.get("out_trade_no").toString();
        payInfo = map.get("payorderstring").toString();
//        payInfo = payInfo.replace("\\", "");
        LogUtils.e("payinfo:" + payInfo);
        //异步发起支付请求
        new CarBuy_NewAliPayStrategy.AlipayAsyncTask().execute();

    }

    //拿到信息  并 发起支付请求
    private void alipay(String payinfo_) {
       try{ UserParams.INSTANCE.setOut_trade_no(out_trade_no);}catch (Throwable t){t.printStackTrace();}
        payInfo =  payinfo_;
        LogUtils.e("payinfo:" + payInfo);
        //异步发起支付请求
        new CarBuy_NewAliPayStrategy.AlipayAsyncTask().execute();

    }



    @Override
    public void pay(String userId, String totalFee, String productCode, String subAmount,String subCount, String subType) {
        connToServerAlipay(userId, totalFee, productCode, subAmount,subCount,subType);
    }

    class AlipayAsyncTask extends AsyncTask<Void, Void, String> {
            @Override  protected String doInBackground(Void... params) {
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
