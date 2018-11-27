package www.qisu666.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.VersionUtils;
import www.qisu666.com.R;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountPayFragment extends BaseFragment {

    private UserParams user = UserParams.INSTANCE;

    private View view;
    private TextView tv_balance;
    private TextView btn_submit;

    public AccountPayFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        connToServerF103();
    }

    public static Fragment newInstance(String charge_pile_seri, String charge_pile_num){
        AccountPayFragment fragment = new AccountPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("charge_pile_seri", charge_pile_seri);
        bundle.putString("charge_pile_num", charge_pile_num);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_pay, container, false);
        initView();
        setListeners();
        connToServerF103();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        btn_submit = (TextView) view.findViewById(R.id.btn_submit);

        btn_submit.setEnabled(false);
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
                connToServer();
            }
        });
    }

    /**
     * 发送 E108 请求，账户支付
     */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "E108");
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_name", user.getCust_alias());
            jsonObject.put("charge_pile_seri", getArguments().get("charge_pile_seri"));
            jsonObject.put("charge_pile_num", getArguments().get("charge_pile_num"));
            jsonObject.put("order_type", "4");
            LogUtil.e(String.valueOf(VersionUtils.getVersionName(getContext())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpLogic httpLogic = new HttpLogic(getContext());
        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, false, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {

            @Override
            public void onResponse(Map<String,Object> map, String tag) {
                user.setOut_trade_no(map.get("out_trade_no").toString());
                EventBus.getDefault().post(new PaySuccessEvent());
            }

        });
    }

    /**
     * 发送 F103 请求，获取账户余额
     */
    private void connToServerF103() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "F103");
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("s_token", user.getS_token());
            LogUtil.e(String.valueOf(VersionUtils.getVersionName(getContext())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpLogic httpLogic = new HttpLogic(getContext());
        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {

            @Override
            public void onResponse(Map<String,Object> map, String tag) {
                String balance = map.get("total_money").toString();
                user.setBalance(balance);
                tv_balance.setText("￥"+balance);

                if(!TextUtils.isEmpty(balance)){
                    if(Float.parseFloat(balance) > 0) {
                        btn_submit.setEnabled(true);
                    }
                }
            }

        });
    }
}
