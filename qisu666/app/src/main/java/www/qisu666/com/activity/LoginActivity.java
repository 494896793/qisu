package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.model.UserBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.security.MD5;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.SPParams;
import www.qisu666.common.utils.StatusBarUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

//登陆界面
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_phone)
    EditText et_login_phone;
    @BindView(R.id.et_login_pwd)
    EditText et_login_pwd;

    private UserParams user = UserParams.INSTANCE;
    private String uuid = "";
    private HttpLogic httpLogic;
    private SharedPreferences sp;

    //    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_login, TYPE_BACKGROUND);
//      imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        initView();
        initData();
        StatusBarUtils.changeStatusBarDarkMode(getWindow());
    }

    private void initData() {
//        httpLogic = new HttpLogic(LoginActivity.this);
        sp = getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE);
    }

    private void initView() {
        String login_phone = getIntent().getStringExtra("login_phone");
        if (!TextUtils.isEmpty(login_phone)) {
            et_login_phone.setText(login_phone);
            et_login_pwd.requestFocus();
        }
        try {
            et_login_phone.setText((String) SPUtil.get(mContext, "phone", ""));
        } catch (Throwable t) {
            t.printStackTrace();
        }


    }


    @OnClick({R.id.btn_login, R.id.btn_forget_pwd, R.id.btn_register, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (checkInput()) {
                    login();
                }
                break; //发起登录请求  if(PermissionUtil.checkPermission(this, ConstantCode.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE))/检查权限
            case R.id.btn_forget_pwd:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
            case R.id.btn_register:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), ConstantCode.REQ_LOGIN);
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;

        }
    }

    private void login() {

        String url = "api/user/login";
        HashMap<String, Object> map = new HashMap<>();
        final String phoneNo = et_login_phone.getText().toString().trim();
        SPUtil.put(mContext, "phone", phoneNo);
        final String pwd = MD5.getMD5Code(et_login_pwd.getText().toString().trim());

        map.put("mobileNo", phoneNo);
        map.put("loginPWD", pwd);
        map.put("deviceUUID", PhoneParams.getDeviceUUID(this));
        LogUtil.e("请求的参数" + map.toString());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(UserBean.class))
                .compose(RxNetHelper.<UserBean>io_main())
                .subscribe(new ResultSubscriber<UserBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(UserBean bean) {
                        Log.e("aaaa", "getToken：" + bean.getToken());
                        Log.e("aaaa", "getUserId：" + bean.getUserinfo().getUserId());

                        // token 在外层
                        saveUserInfo(bean, bean.getToken() == null ? "" : bean.getToken());
                        // todo 登陆暂时注释
                        EventBus.getDefault().post(new LoginEvent());

                        // 登陆
                        EventMsg eventMsg = new EventMsg();
                        eventMsg.setType(1);
                        RxBus.getInstance().post(eventMsg);

                        setResult(ConstantCode.RES_LOGIN);
                        finish();
                    }

                    @Override
                    public void onFail(Message<UserBean> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaa", "msg:" + bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }
                });
    }

    @SuppressLint("ApplySharedPref")
    private void saveUserInfo(UserBean map, String token) {

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("mobile_no", map.getUserinfo().getMobileNo());
        editor.putString("user_name", TextUtils.isEmpty(map.getUserinfo().getUserName()) ? "未设置" : map.getUserinfo().getUserName());
        editor.putString("sex", map.getUserinfo().getSex().equals("1") ? "男" : map.getUserinfo().getSex().equals("2") ? "女" : "保密");
        editor.putString("user_id", map.getUserinfo().getUserId());
        editor.putString("s_token", token);
        editor.putInt("picture", map.getUserinfo().getPicture() == null ? 0 : Integer.valueOf(map.getUserinfo().getPicture()));
        editor.commit();

        user.clear();
        user.setTel_no(map.getUserinfo().getMobileNo());
        user.setCust_alias(TextUtils.isEmpty(map.getUserinfo().getUserName()) ? "未设置" : map.getUserinfo().getUserName());
        user.setSex(map.getUserinfo().getSex().equals("1") ? "男" : map.getUserinfo().getSex().equals("2") ? "女" : "保密");
        user.setUser_id(map.getUserinfo().getUserId());
        user.setS_token(token);
        user.setPicture(map.getUserinfo().getPicture() == null ? 0 : Integer.valueOf(map.getUserinfo().getPicture()));

        ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(et_login_phone.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_phone_is_null);
        } else if (TextUtils.isEmpty(et_login_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_is_null);
        } else if (!et_login_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$")) {
            ToastUtil.showToast(R.string.toast_pwd_illegal);
        } else if (et_login_pwd.getText().toString().trim().length() < 6 || et_login_pwd.getText().toString().trim().length() > 16) {
            ToastUtil.showToast(R.string.toast_pwd_length_illegal);
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ConstantCode.RES_REGISTER) {
            et_login_phone.setText(data.getStringExtra("mobile_no"));
            et_login_pwd.setText(data.getStringExtra("login_pwd"));
            login();
        }
    }

    // 点击空白区域 自动隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert mInputMethodManager != null;
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
