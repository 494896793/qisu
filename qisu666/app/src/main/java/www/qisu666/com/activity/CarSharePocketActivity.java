package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.de.hdodenhof.circleimageview.CircleImageView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.GlobalParams;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;

//账户详情
public class CarSharePocketActivity extends BaseActivity {

    @BindView(R.id.tv_pocket_total_money)
    TextView tvPocketTotalMoney;
    @BindView(R.id.btn_title_right)
    TextView btnTitleRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_shiming)
    ImageView ivShiming;
    @BindView(R.id.iv_jiaotong)
    ImageView ivJiaotong;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.tv_jianglijin)
    TextView tvJianglijin;
    @BindView(R.id.aaaa)
    TextView aaaa;

    Map m;

    private CircleImageView img_portrait;
    private UserParams user = UserParams.INSTANCE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_pocket);
        requestBookDay();
        initView();
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        connToServer();
//    }
    // 查询账户信息
    private void requestBookDay() {
        String url = "api/my/order/getaccount";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
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
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 map
                        m = JsonUtils.jsonToMap(s);
                        modUISet(m);

                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
    }

    String s = "";

    @SuppressLint("SetTextI18n")
    private void modUISet(Map m) {
        tvPocketTotalMoney.setText("¥" + m.get("totalmoney").toString() + "元");
        double bb = Double.parseDouble(m.get("monthAbleAmount").toString());
        BigDecimal b = new BigDecimal(bb / 100);
        double dis = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvJianglijin.setText(String.valueOf(dis) + "元");

        // 身份证状态
        s = m.get("realstatus").toString();
        if ("0".equals(s)) {
            ivShiming.setImageResource(R.mipmap.gr_22);
        } else {
            ivShiming.setImageResource(R.mipmap.gr_23);
        }
        // 押金状态
        String depositstatus = m.get("depositstatus").toString();
        switch (depositstatus) {
            case "0":
                // 交通违法保证金
                ivJiaotong.setImageResource(R.mipmap.gr_25);
                break;
            case "1":
                // 交通违法保证金
                ivJiaotong.setImageResource(R.mipmap.gr_24);
                break;
            case "2":
                // 交通违法保证金
                ivJiaotong.setImageResource(R.mipmap.gr_25);
                break;
            default:
                break;
        }

        //数字密码
        if (m.get("digitalStatus").toString().equals("2")) {
            ivPassword.setImageResource(R.mipmap.gr_26);
        } else {
            ivPassword.setImageResource(R.mipmap.gr_27);
        }
    }

    private void initView() {
//        btnTitleRight.setVisibility(View.VISIBLE);
//        btnTitleRight.setText("明细");
        tvTitle.setText("账户详情");
        img_portrait = (CircleImageView) findViewById(R.id.img_portrait);
        img_portrait.setImageResource(GlobalParams.icons[user.getPicture()]);
    }

    @OnClick({R.id.btn_title_right, R.id.img_title_left, R.id.btn_submit, R.id.iv_jiaotong, R.id.iv_shiming,R.id.aaaa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aaaa:
                ActivityUtil.startActivity(mContext,MoneyDetailActivity.class);
                break;
            case R.id.btn_title_right:
                //ActivityUtil.startActivity(mContext, BillActivity.class);//明细
                ActivityUtil.startActivity(mContext,MoneyDetailActivity.class);
                break;
            // 实名
            case R.id.iv_shiming:
                if ("0".equals(s)) {
                    ActivityUtil.startActivity(mContext, CarShareConfirmIdentityActivity.class);
                }
                break;
            case R.id.iv_jiaotong:
                //  交通保证金
                if(s.equals("0")){
                    ToastUtil.showToast("在实名认证后才可以缴纳押金");
                }else {
                    ActivityUtil.startActivity(mContext, TrafficActivity.class);
                }
                break;
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                ActivityUtil.startActivity(mContext, RechargeActivity.class);
                break;
            default:
                break;
        }
    }

}
