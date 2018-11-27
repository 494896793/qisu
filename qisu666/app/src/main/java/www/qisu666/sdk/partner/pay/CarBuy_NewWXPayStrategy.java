package www.qisu666.sdk.partner.pay;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

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
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.bean.Bean_Order;
import www.qisu666.sdk.partner.bean.Bean_Order_WX;

/**
 * 717219917@qq.com 2018/4/17 16:45.
 */ // 认购微信支付
public class CarBuy_NewWXPayStrategy implements CarBuy_NewPayStrategy {
    private IWXAPI api;
    private PayReq req;
    private Context context;
    private String requestNum;

    public CarBuy_NewWXPayStrategy(Context context, String requestNum) {//微信初始化
        api = WXAPIFactory.createWXAPI(context, Config.WECHAT_APPID);
        this.context = context;
        this.requestNum = requestNum;
    }


    /**  获取支付宝支付订单信息*/
    private void connToServerWxpay( String userId, String total_fee, String productCode,String subAmount,String subCount,String subType) {
        String url = "api/pay/wx/pay";
        HashMap<String, Object> map = new HashMap<>();
        map.put("productCode", productCode);
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("totalFee", total_fee);
        map.put("subType", subType);
        map.put("subCount", subCount);
        map.put("subAmount", subAmount);
        map.put("spBillCreateIP", NetworkUtils.getPhoneIp());
        map.put("payType", "1");//微信支付 固定


        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_Order_WX.class))
                .compose(RxNetHelper.<Bean_Order_WX>io_main())
                .subscribe(new ResultSubscriber<Bean_Order_WX>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(Bean_Order_WX bean_resu) {
                        LogUtil.e("获取认购详情成功"+bean_resu.getOrderNo());
                        LogUtil.e("获取认购详情成功"+bean_resu.getRequestParam());
                        try{ SPUtil.put(context,"orderNo",bean_resu.getOrderNo());}catch (Throwable t){t.printStackTrace();}

                        req = new PayReq();
                        req.appId			=  bean_resu.getRequestParam().getAppid();
                        req.nonceStr		=  bean_resu.getRequestParam().getNoncestr();
                        req.packageValue	=  bean_resu.getRequestParam().getPackage();
                        req.partnerId		= bean_resu.getRequestParam().getPartnerid();
                        req.prepayId		= bean_resu.getRequestParam().getPrepayid();
                        req.timeStamp		= bean_resu.getRequestParam().getTimestamp();
                        req.sign			= bean_resu.getRequestParam().getPaysign();
//                    req.extData = "app data"; // optional
                        LogUtil.e("req.checkArgs():"+req.checkArgs());
                        new CarBuy_NewWXPayStrategy.WxpayAsyncTask().execute();
                    }

                    @Override public void onFail(www.qisu666.com.carshare.Message<Bean_Order_WX> bean) {
                        LogUtil.e("获取认购详情失败"+bean.msg);
                    }
                });

    }


    @Override
    public void pay(String userId, String totalFee, String productCode,  String subAmount,String subCount, String subType) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(isPaySupported){
            connToServerWxpay(userId, totalFee, productCode,  subAmount,subCount,subType);
        } else {
            DialogHelper.alertDialog(context, context.getString(R.string.dialog_app_wx_version_is_too_old));
        }
    }

    class WxpayAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean flag = api.sendReq(req);
            LogUtils.e("api.sendReq(req):"+flag);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
