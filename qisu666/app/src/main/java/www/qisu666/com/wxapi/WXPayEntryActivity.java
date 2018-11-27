package www.qisu666.com.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import www.qisu666.com.event.ButtonEnable;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.common.activity.BaseActivity;
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode+" "+resp.errStr);
//
        Log.e("asd", "onPayFinish  errCode = " + resp.errCode + " " + resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                EventBus.getDefault().post(new PaySuccessEvent());
            } else if (resp.errCode == -1) {
                EventBus.getDefault().post(new PayFailEvent());
            } else if (resp.errCode == -2) {
                ToastUtil.showToast(R.string.toast_pay_cancel);
                try {
                    cancelPay();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
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
                        try {
                            EventBus.getDefault().post(new ButtonEnable());
                            LogUtil.e(" 认购  微信取消界面：" + bean.totalSubAmount);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                        try {
                            EventBus.getDefault().post(new ButtonEnable());
                            LogUtil.e("进入微信取消 failed 方法：" + bean.msg);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        finish();
                    }
                });
    }

}