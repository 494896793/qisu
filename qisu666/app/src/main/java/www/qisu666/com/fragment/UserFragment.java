package www.qisu666.com.fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.de.hdodenhof.circleimageview.CircleImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.badgeview.BGABadgeImageView;
import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareConfirmIdentityActivity;
import www.qisu666.com.activity.CarSharePocketActivity;
import www.qisu666.com.activity.ChargingActivity;
import www.qisu666.com.activity.CommonWebViewActivity;
import www.qisu666.com.activity.LoginActivity;
import www.qisu666.com.activity.MyInverstActivity;
import www.qisu666.com.activity.PersonalInfoActivity;
import www.qisu666.com.activity.RechargeActivity;
import www.qisu666.com.activity.SettingActivity;
import www.qisu666.com.activity.TrafficActivity;
import www.qisu666.com.adapter.ShareAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.BaseEvent;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.GlobalParams;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.Rotate3dAnimation;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_tool_recharge)
    LinearLayout llToolRecharge;
    @BindView(R.id.ll_tool_invite)
    LinearLayout llToolInvite;
    @BindView(R.id.ll_tool_coupon)
    LinearLayout llToolCoupon;
    Unbinder unbinder;
    private UserParams user = UserParams.INSTANCE;
    PopupWindow sharePopupWindow;

    private View layout_charging_record, layout_payment_pwd, layout_operation_flow, layout_option, layout_notify, layout_pay_by_others, ll_confirm_identity, layout_pocket, layout_book_tip, layout_travel, layout_deposit;
    /**
     * 我的认购,我的行程
     */
    private View ll_user_recharge, ll_user_account_detail, ll_user_subscribe, ll_service_question, ll_service_express, ll_service_corp;

    private CircleImageView img_portrait;

    private TextView tv_alias;
    private TextView tv_balance;
    private TextView tv_user_level;
    private TextView tv_confirm_identity;
    private TextView tv_traffic_insure;
    private ImageView ivSetting;

    private ImageView img_confirm_identity;
    private ImageView img_user_level;
    private ImageView img_traffic_insure;
    private BGABadgeImageView img_tool_coupon;

    private LinearLayout layout_main;

    private Integer realstatus; //是否实名


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * 支付成功或登录时会接收到消息，刷新余额
     */
    @Override
    @Subscribe
    public void onEventMainThread(BaseEvent event) {
        if (event instanceof PaySuccessEvent || event instanceof LoginEvent) {
            LogUtils.i("接收到登陆成功消息,进行F103请求");
            connToServerF103();
        }
    }

    @Subscribe
    public void onEventMainThread(String event) {
        if (event.contains("exit_login") || event.contains("失效")) {
            // 用户等级
//            tv_user_level.setText(R.string.user_level_unsigned);
//            tv_user_level.setTextColor(getResources().getColor(R.color.user_level_normal));
//            img_user_level.setImageResource(R.mipmap.hy);
            // 实名
            tv_confirm_identity.setText(R.string.confirm_identity_not_passed);
            tv_confirm_identity.setTextColor(getResources().getColor(R.color.confirm_not_passed));
            img_confirm_identity.setImageResource(R.mipmap.sm);
            // 交通违法保证金
            tv_traffic_insure.setText(R.string.traffic_insure);
            tv_traffic_insure.setTextColor(getResources().getColor(R.color.confirm_not_passed));
            img_traffic_insure.setImageResource(R.mipmap.jtwfbzj);

            img_portrait.setImageResource(R.mipmap.gr_31);
            tv_alias.setText("未登录");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container);
        if (!TextUtils.isEmpty(user.getUser_id())) {
            connToServerF103();
        }
        layout_main=view.findViewById(R.id.layout_main);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        LogUtil.e("进入 onHiddenChanged------");
//        if (!hidden && !TextUtils.isEmpty(user.getUser_id())) {
//            connToServerF103();
//        }
//        super.onHiddenChanged(hidden);
//    }

    @Override
    public void onResume() {
        super.onResume();

        tv_alias.setText(TextUtils.isEmpty(user.getCust_alias()) ? "未登录" : user.getCust_alias());
        if (user.getPicture() != -1) {
            img_portrait.setImageResource(GlobalParams.icons[user.getPicture()]);
        } else {
            img_portrait.setImageResource(R.mipmap.gr_31);
        }
//        if (user.isVip()) {
//            img_user_level.setImageResource(R.mipmap.hy2);
//            tv_user_level.setText(R.string.user_level);
//            tv_user_level.setTextColor(getResources().getColor(R.color.user_level_vip));
//        } else {
//            tv_user_level.setText(R.string.user_level_normal);
//            tv_user_level.setTextColor(getResources().getColor(R.color.user_level_normal));
//            img_user_level.setImageResource(R.mipmap.hy);
//        }
        if (UserParams.INSTANCE.getUser_id() != null) {
            connToServerF103();
        }
    }


    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // 初始化个人中心头部
        tv_confirm_identity = view.findViewById(R.id.tv_confirm_identity);
        img_confirm_identity = view.findViewById(R.id.img_confirm_identity);
        ll_confirm_identity = view.findViewById(R.id.ll_confirm_identity);

        img_portrait = view.findViewById(R.id.img_portrait);
        tv_alias = view.findViewById(R.id.tv_alias);
        tv_balance = view.findViewById(R.id.tv_balance);
        tv_user_level = view.findViewById(R.id.tv_user_level);
        img_user_level = view.findViewById(R.id.img_user_level);
        ivSetting = view.findViewById(R.id.img_home_more);
        img_traffic_insure = view.findViewById(R.id.img_traffic_insure);

        tv_confirm_identity.setText(R.string.confirm_identity_not_passed);
        tv_confirm_identity.setEnabled(true);
        img_confirm_identity.setImageResource(R.mipmap.sm);

        // 个人中心头部点击监听
        ll_confirm_identity.setOnClickListener(this);
        img_portrait.setOnClickListener(this);
        img_portrait.setOnClickListener(this);
        tv_alias.setOnClickListener(this);

        // 个人中心推荐功能初始化
        img_tool_coupon = view.findViewById(R.id.img_tool_coupon);
        img_tool_coupon.showTextBadge("12");
        img_tool_coupon.hiddenBadge();

        // 初始化个人中心功能列表
        ll_user_recharge = view.findViewById(R.id.ll_user_recharge);
        ll_user_account_detail = view.findViewById(R.id.ll_user_account_detail);
        ll_user_subscribe = view.findViewById(R.id.ll_user_subscribe);
        ll_service_question = view.findViewById(R.id.ll_service_question);
        ll_service_express = view.findViewById(R.id.ll_service_express);
        ll_service_corp = view.findViewById(R.id.ll_service_corp);
        tv_traffic_insure = view.findViewById(R.id.tv_traffic_insure);

        // 为个人中心功能列表设置点击监听
        ll_user_recharge.setOnClickListener(this);
        ll_user_account_detail.setOnClickListener(this);
        ll_user_subscribe.setOnClickListener(this);
        ll_service_question.setOnClickListener(this);
        ll_service_express.setOnClickListener(this);
        ll_service_corp.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        tv_traffic_insure.setOnClickListener(this);

        View immersionView = getImmersionView(view);
        return immersionView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 实名认证
            case R.id.ll_confirm_identity:
                if (user.checkLogin(getActivity())) {
                    //身份证认证
//                    startActivity(new Intent(getActivity(), CarShareConfirmIdentityActivity.class));
                    if (!"已实名".equals(tv_confirm_identity.getText().toString())) {
                        tv_confirm_identity.setEnabled(true);
                        startActivity(new Intent(getActivity(), CarShareConfirmIdentityActivity.class));
                    } else {
                        tv_confirm_identity.setEnabled(false);
                    }
                }  else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            // 交通保证金
            case R.id.tv_traffic_insure:
                if (user.checkLogin(getActivity())) {
                    if(realstatus>0){
                        startActivity(new Intent(getActivity(), TrafficActivity.class));
                    }else {
                        ToastUtil.showToast("在实名认证后才可以缴纳押金");
                    }
                }
                break;
            // 充值界面
            case R.id.ll_user_recharge:
                if (user.checkLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                }  else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            // 账户详情
            case R.id.ll_user_account_detail:
                if (user.checkLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), CarSharePocketActivity.class));
                }  else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            //我的认购"
            case R.id.ll_user_subscribe:
                if (user.checkLogin(getActivity())) {
                    Intent myInverstIntent = new Intent(getActivity(), MyInverstActivity.class);
                    startActivity(myInverstIntent);
                }else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
//                ToastUtil.showToast("建设中，敬请期待");
//                LogUtil.e("点击  我的认购");
//                if (user.checkLogin(getActivity())) {
//                    //我的认购页面
//                    startActivity(new Intent(getActivity(), Activity_MyInvest.class));
//                }
                break;
            case R.id.ll_service_question:
                Intent questionWeb = new Intent(getActivity(), CommonWebViewActivity.class);
                questionWeb.putExtra("act_url", "http://www.qisu666.com/app-question/index.html");
                questionWeb.putExtra("act_title", "常见问题解答");
                startActivity(questionWeb);
//                ToastUtil.showToast("建设中，敬请期待");
                break;
            case R.id.ll_service_express:
                AlertDialog dialog = new AlertDialog(getActivity(), "呼叫", "取消", "400-7569999", "服务时间：00:00 - 23:59", true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        } else {
                            if (PermissionUtil.checkPermission(getActivity(), ConstantCode.CALL_PHONE, Manifest.permission.CALL_PHONE)) {

                            }
                        }

                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:400-7569999"));
                            startActivity(i);

                        } catch (Exception e) {
                            ToastUtil.showToast("拨打电话权限受限，请在权限设置中打开该权限");
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();
//                ToastUtil.showToast("建设中，敬请期待");
                break;
            case R.id.ll_service_corp:
                Intent commWeb = new Intent(getActivity(), CommonWebViewActivity.class);
                commWeb.putExtra("act_url", "http://www.qisu666.com/app-partner/index.html");
                commWeb.putExtra("act_title", "商务合作");
                startActivity(commWeb);
//                ToastUtil.showToast("建设中，敬请期待");
                break;
            case R.id.img_home_more:
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), ConstantCode.REQ_INDEX);
                break;
            case R.id.img_portrait:
                if (user.checkLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                } else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            case R.id.tv_alias:
                if (user.checkLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                }else{
                    ToastUtil.showToast(R.string.toast_prompt_login);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                }
                break;
            //我的行程
            default:
                break;
        }
    }


    /**
     * 发送 F103 请求，获取账户余额
     */
    private void connToServerF103() {
        String url = "api/my/order/getaccount";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", user.getUser_id());

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
                        Log.e("fff", "onSuccess：" + bean.toString());
                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        Log.e("fff", "json：" + s);
                        // json转 map
                        Map m = JsonUtils.jsonToMap(s);
                        initconnToServerF103(m);
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
    }

    private void initconnToServerF103(Map<String, Object> map) {
        // 账户余额
        String balance = map.get("totalmoney").toString();
        user.setBalance(balance);
        //身份证已认证
        realstatus = Integer.valueOf(map.get("realstatus").toString());
        if (realstatus > 0) {
            // 修改实名信息
            tv_confirm_identity.setText("已实名");
            tv_confirm_identity.setTextColor(getResources().getColor(R.color.confirm_passed));
            img_confirm_identity.setImageResource(R.mipmap.sm_2);
        } else {
            tv_confirm_identity.setText(R.string.confirm_identity_not_passed);
            tv_confirm_identity.setTextColor(getResources().getColor(R.color.confirm_not_passed));
            img_confirm_identity.setImageResource(R.mipmap.sm);
        }

        //头像ID
        int picture = Integer.parseInt(map.get("picture").toString());
        img_portrait.setImageResource(GlobalParams.icons[picture]);

        // 押金状态
        String depositstatus = map.get("depositstatus").toString();
        switch (depositstatus) {
            case "0":
                // 已缴纳
                tv_traffic_insure.setTextColor(getResources().getColor(R.color.new_primary));
                img_traffic_insure.setImageResource(R.mipmap.jtwfbzj_2);
                break;
            case "1":
                // 未缴纳
                tv_traffic_insure.setTextColor(getResources().getColor(R.color.confirm_not_passed));
                img_traffic_insure.setImageResource(R.mipmap.jtwfbzj);
                break;
            case "2":
                // 退款中
                tv_traffic_insure.setTextColor(getResources().getColor(R.color.new_primary));
                img_traffic_insure.setImageResource(R.mipmap.jtwfbzj_2);
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == ConstantCode.REQ_INDEX && resultCode == ConstantCode.RES_LOGIN){
//            tv_alias.setText(user.getCust_alias());
//        }else if(requestCode == ConstantCode.REQ_INDEX && resultCode == ConstantCode.RES_UNLOGIN){
//            tv_alias.setText("未登录");
//        }
    }

    private void applyRotation(float start, float end) {
        // 计算中心点
        int centerX = img_portrait.getWidth() / 2;
        int centerY = img_portrait.getHeight() / 2;

        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 0, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        // 设置监听
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            // 动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                img_portrait.post(new SwapViews());
                if (img_portrait.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.ic_user_portrait).getConstantState())) {
                    img_portrait.setImageResource(R.mipmap.logo_512);
                } else {
                    img_portrait.setImageResource(R.mipmap.ic_user_portrait);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        img_portrait.startAnimation(rotation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_tool_recharge, R.id.ll_tool_invite, R.id.ll_tool_coupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tool_recharge:
                ToastUtil.showToast("建设中，敬请期待");
                break;
            case R.id.ll_tool_invite:
                if (UserParams.INSTANCE.checkLogin(getActivity())) {
                    Map<String,Object> map=new HashMap<>();
                    map.put("userId",UserParams.INSTANCE.getUser_id());
                    String desString=MyMessageUtils.writeMessage(map,"HL1HBF6lLND721");
                    String newUrl="http://www.qisu666.com/app-activity/qrcode.html?parameters="+desString;
                    Intent i = new Intent(getActivity(), CommonWebViewActivity.class);
                    i.putExtra("act_url", newUrl);
                    i.putExtra("act_title", "");
                    startActivity(i);
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_tool_coupon:
                ToastUtil.showToast("建设中，敬请期待");
                break;
            default:
                break;
        }
    }


    private final class SwapViews implements Runnable {

        @Override
        public void run() {

            int centerX = img_portrait.getWidth() / 2;
            int centerY = img_portrait.getHeight() / 2;
            Rotate3dAnimation rotation = null;

            img_portrait.requestFocus();

            rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 0, false);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            // 开始动画
            img_portrait.startAnimation(rotation);
        }
    }
}
