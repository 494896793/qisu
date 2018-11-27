package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

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
import www.qisu666.com.util.UserParams;
import www.qisu666.com.util.VerifyCodeTimer;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.security.MD5;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;

//忘记密码
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone, et_code, et_new_pwd;
    private TextView btn_getcode, btn_submit;
    private VerifyCodeTimer timer;
    private HttpLogic httpLogic;
    private UserParams user = UserParams.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_forget_pwd);
        initData();
        initView();
    }

    private void initData() {
        initTimer();
        httpLogic = new HttpLogic(this);
    }

    //初始化计时器
    private void initTimer() {
        timer = new VerifyCodeTimer(60000, 1000);
        timer.setListener(new VerifyCodeTimer.OnTimerListener() {

            @SuppressLint("SetTextI18n")
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

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);

        btn_getcode = (TextView) findViewById(R.id.btn_getcode);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_getcode.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("找回密码");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit://点击确认
                if (checkInput()) {
                    forgetPwd();
                }
                break;
            case R.id.btn_getcode://获取验证码
                if (!TextUtils.isEmpty(et_phone.getText().toString())) {
                    getCode();
                } else {
                    ToastUtil.showToast(R.string.toast_phone_is_null);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 发送 A105 请求，重置密码
     */

    private void forgetPwd() {

        String url = "api/user/resetPassword";
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobileNo", et_phone.getText().toString().trim());
        map.put("verifyCode", et_code.getText().toString().trim());//1、注册；2、忘记密码；3、修改手机号
        map.put("loginPWD", MD5.getMD5Code(et_new_pwd.getText().toString()));

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
                        ToastUtil.showToast(R.string.toast_A105);
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.msg);
                    }

                });
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_phone_is_null);
        } else if (TextUtils.isEmpty(et_new_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_is_null);
        } else if (!et_new_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$")) {
            ToastUtil.showToast(R.string.toast_pwd_illegal);
        } else if (et_new_pwd.getText().toString().trim().length() < 6 || et_new_pwd.getText().toString().trim().length() > 16) {
            ToastUtil.showToast(R.string.toast_pwd_length_illegal);
        } else if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_code_is_null);
        } else if (TextUtils.isEmpty(et_new_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_code_length_illegal);
        } else {
            return true;
        }
        return false;
    }

    private void getCode() {

        String url = "api/sms/verify";
        // 单个参数加密
        try {
            String s = MyMessageUtils.writeMessage("qisu666" + et_phone.getText().toString().trim());
            HashMap<String, Object> map = new HashMap<>();
            map.put("mobileNo", et_phone.getText().toString().trim());
            map.put("tempType", "2");    //1、注册；2、忘记密码；3、修改手机号
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
