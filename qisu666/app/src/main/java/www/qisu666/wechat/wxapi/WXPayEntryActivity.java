package www.qisu666.wechat.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.bean.Bean_TotalAmount;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    //	private static final String TAG = "WXPayEntryActivity";
//
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_wxpay_entry);

        api = WXAPIFactory.createWXAPI(this, "wxf46055df3f9eafeb", false);
        api.handleIntent(getIntent(), this);
    }

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//        api.handleIntent(intent, this);
//	}

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode+" "+resp.errStr);
//
        LogUtils.d("onPayFinish,微信支付回调 errCode = " + resp.errCode + " " + resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                EventBus.getDefault().post(new PaySuccessEvent());
            } else if (resp.errCode == -1) {
                EventBus.getDefault().post(new PayFailEvent());
            } else if (resp.errCode == -2) {
//				EventBus.getDefault().post(new PayFailEvent());
//				ToastUtil.showToast("进入微信支付取消。。。");
//				try{cancelPay();}catch (Throwable t){t.printStackTrace();}
                ToastUtil.showToast(R.string.toast_pay_cancel);

            }
        }

        finish();

    }


    //取消支付
    private void cancelPay() {
        String url = "api/vip/pay/reback";
        LogUtil.e("取消支付：" + url);
        HashMap<String, Object> map = new HashMap<>();
        map.put("responseStatus", "2");
        map.put("orderNo", (String) SPUtil.get(WXPayEntryActivity.this, "orderNo", ""));//orderNo
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TotalAmount.class))
                .compose(RxNetHelper.<Bean_TotalAmount>io_main())
                .subscribe(new ResultSubscriber<Bean_TotalAmount>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Bean_TotalAmount bean) {
                        LogUtil.e(" 认购 获取到的 总计金额" + bean.totalSubAmount);
                        LogUtil.e(" 认购 获取到的 用户编码" + bean.userCode);
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                        LogUtil.e("进入failed 方法：" + bean.msg);
                        finish();
                    }
                });
    }


}