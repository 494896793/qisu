package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.VerifyCodeTimer;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.security.MD5;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.StatusBarUtils;
import www.qisu666.common.utils.ToastUtil;

//注册页面
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_register_phone, et_regisger_pwd, et_code;
    private TextView btn_getcode, btn_register, btn_login, btn_register_protocol;
    private View btn_back, ll_apply_protocol;
    private ImageView iv_apply_protocol;
    private VerifyCodeTimer timer;
    private HttpLogic httpLogic;
    private boolean isApply = true;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.edit_recommend)
    EditText edit_recommend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_register);
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        initData();
        initView();
        StatusBarUtils.changeStatusBarDarkMode(getWindow());
    }

    private void initData() {
        tv_title.setText("注册");
        initTimer();
        httpLogic = new HttpLogic(RegisterActivity.this);
    }

    private void initView() {
        img_title_left.setOnClickListener(this);

        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_regisger_pwd = (EditText) findViewById(R.id.et_regisger_pwd);
        et_code = (EditText) findViewById(R.id.et_code);

        ll_apply_protocol = findViewById(R.id.ll_apply_protocol);
        iv_apply_protocol = (ImageView) findViewById(R.id.iv_apply_protocol);

        btn_getcode = (TextView) findViewById(R.id.btn_getcode);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_register_protocol = (TextView) findViewById(R.id.btn_register_protocol);

        btn_getcode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register_protocol.setOnClickListener(this);
        ll_apply_protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                finish();
                break;
            case R.id.btn_getcode:
                if (!TextUtils.isEmpty(et_register_phone.getText().toString().trim()) && et_register_phone.getText().toString().trim().length() == 11) {
                    getCode();
                } else if (et_register_phone.getText().toString().trim().length() != 11) {
                    ToastUtil.showToast(R.string.toast_phone_length_illegal);
                } else {
                    ToastUtil.showToast(R.string.toast_phone_is_null);
                }
                break;
            case R.id.btn_register:
                if (checkInput()) {
                    if (isApply) {
                        register();
                    } else {
                        ToastUtil.showToast(getString(R.string.toast_apply_protocol));
                    }
                }
                break;
            case R.id.img_title_left:
                finish();
            case R.id.btn_register_protocol:
                startActivity(new Intent(RegisterActivity.this, RegisterAgreementActivity.class));
                break;//跳转到用户协议
            case R.id.ll_apply_protocol:
                applyToggle();
                break;//更新状态 按钮与值
            default:
                break;
        }
    }

    private void applyToggle() {
        if (isApply) {
            iv_apply_protocol.setImageResource(R.mipmap.yc_17);
        } else {
            iv_apply_protocol.setImageResource(R.mipmap.yc_18);
        }
        isApply = !isApply;
    }

    /**
     * 发送 A104 请求，获取验证码
     */
    private void getCode() {

        String url = "api/sms/verify";
        // 单个参数加密
        try {
            String s = MyMessageUtils.writeMessage("qisu666" + et_register_phone.getText().toString().trim());
            HashMap<String, Object> map = new HashMap<>();
            map.put("mobileNo", et_register_phone.getText().toString().trim());
            map.put("tempType", "1");    //1、注册；2、忘记密码；3、修改手机号
            map.put("appkey", s);

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
                            timer.start();
                            btn_getcode.setEnabled(false);
                            ToastUtil.showToast(R.string.toast_A104);
                        }

                        @Override
                        public void onFail(Message<Object> bean) {
                            ToastUtil.showToast(bean.msg);
                            Log.e("aaaa", "获取失败：" + bean.toString());
                        }

                    });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 A103 请求，注册账号
     */
    private void register() {

        String url = "api/user/register";
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobileNo", et_register_phone.getText().toString().trim());
        map.put("verifyCode", et_code.getText().toString().trim());
        map.put("loginPWD", MD5.getMD5Code(et_regisger_pwd.getText().toString().trim()));
        if(!TextUtils.isEmpty(edit_recommend.getText().toString())){
            map.put("invitationFlag",edit_recommend.getText().toString().trim());
        }

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
                        ToastUtil.showToast(R.string.toast_A103);
                        Intent i = new Intent();
                        i.putExtra("mobile_no", et_register_phone.getText().toString().trim());
                        i.putExtra("login_pwd", et_regisger_pwd.getText().toString().trim());
                        setResult(ConstantCode.RES_REGISTER, i);
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.msg);
                    }

                });
    }

    /**
     * 检查输入内容是否合法
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(et_register_phone.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_phone_is_null);
        } else if (TextUtils.isEmpty(et_regisger_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_is_null);
        } else if (!et_regisger_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$")) {
            ToastUtil.showToast(R.string.toast_pwd_illegal);
        } else if (et_regisger_pwd.getText().toString().trim().length() < 6 || et_regisger_pwd.getText().toString().trim().length() > 16) {
            ToastUtil.showToast(R.string.toast_pwd_length_illegal);
        } else if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_code_is_null);
        } else if (et_code.getText().toString().trim().length() != 6) {
            ToastUtil.showToast(R.string.toast_code_length_illegal);
        } else {
            return true;
        }
        return false;
    }

    //初始化计时器
    private void initTimer() {
        timer = new VerifyCodeTimer(60000, 1000);
        timer.setListener(new VerifyCodeTimer.OnTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_getcode.setText(String.valueOf(millisUntilFinished / 1000) + "秒后重试");
            }

            @Override
            public void onFinish() {
                btn_getcode.setEnabled(true);
                btn_getcode.setText("获取验证码");
            }
        });
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
