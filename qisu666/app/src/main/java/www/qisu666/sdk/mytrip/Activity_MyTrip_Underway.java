package www.qisu666.sdk.mytrip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.stationMap.JsonUtil;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;
import www.qisu666.sdk.mytrip.bean.Bean_TripDetail;

/**
 * 717219917@qq.com 2018/7/16 10:44.
 */
public class Activity_MyTrip_Underway extends BaseActivity {

    String orderCode = "";
    String status = "";//5 是已取消的  不显示创建时间
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mytripdetail_canceltime2)
    TextView mytripdetailCanceltime2;
    @BindView(R.id.mytripdetail_orderCode)
    TextView mytripdetailOrderCode;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_mytrip_underway);
        initView();

    }

    private void initView() {

        tvTitle.setText("行程详情");
        imgTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {

        orderCode = getIntent().getStringExtra("orderCode");
        try {
            status = getIntent().getStringExtra("status");
        } catch (Throwable t) {
            t.printStackTrace();
            status = "";
        }
        LogUtil.e("接收到的行程详情：" + orderCode);
        getTripUnderway(orderCode);
    }

    private void getTripUnderway(String orderCode) {

        String url = "api/tss/order/list";
        final HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", orderCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id() + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        Log.e("aaa", "bean:" + bean.toString());
                        // 对象转 json
                        String s = JsonUtils.objectToJson(bean);
                        // json 转MAP
                        Map map1 = JsonUtils.jsonToMap(s);
                        // 得到订单对象
                        String b = map1.get("oneOrder").toString();
                        // 在转一次 map
                        Map map2 = JsonUtils.jsonToMap(b);

                        mytripdetailCanceltime2.setText(map2.get("createdTime").toString());
                        mytripdetailOrderCode.setText(map2.get("outTradeNo").toString());

                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaa", bean.msg);
                    }
                });
    }

}
