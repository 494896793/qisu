package www.qisu666.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;

import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.util.VerifyCodeTimer;
import www.qisu666.com.widget.ClearEditText;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;

/**
 * 717219917@qq.com 2018/9/17 16:27.
 */
public class PhoneCheckActivity extends BaseActivity implements View.OnClickListener{

    private TextView et_login_phone;
    private ClearEditText et_login_code;
    private TextView btn_getcode;
    private TextView btn_register;
    private int second=60;
    private VerifyCodeTimer timer;
    private ImageView img_title_left;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_phonecheck);
        initView();
        initLisenner();
        initTimer();
    }

    private void initView(){
        et_login_phone=findViewById(R.id.et_login_phone);
        et_login_code=findViewById(R.id.et_login_code);
        btn_getcode=findViewById(R.id.btn_getcode);
        btn_register=findViewById(R.id.btn_register);
        img_title_left=findViewById(R.id.img_title_left);
        tv_title=findViewById(R.id.tv_title);

        UserParams a=UserParams.INSTANCE;
        tv_title.setText("手机验证");
        et_login_phone.setText(UserParams.INSTANCE.getTel_no());
    }

    private void initLisenner(){
        img_title_left.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_getcode.setOnClickListener(this);
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

    private void getCode() {

        String url = "api/sms/verify";
        // 单个参数加密
        try {
            String s = MyMessageUtils.writeMessage("qisu666" + et_login_phone.getText().toString().trim());
            HashMap<String, Object> map = new HashMap<>();
            map.put("mobileNo", et_login_phone.getText().toString().trim());
            map.put("tempType", "18");
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

    private void checkCode(){
        String url="weChat/sms/msg/verify";
        HashMap<String,Object> map=new HashMap<>();
        map.put("mobileNo",et_login_phone.getText().toString());
        map.put("tempType","18");
        map.put("verifyCode",et_login_code.getText().toString());
        MyNetwork.getMyApi()
                .carRequest(url,MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        ActivityUtil.startActivity(mContext, PinSettingActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btn_register:
                if(TextUtils.isEmpty(et_login_phone.getText().toString())){
                    ToastUtil.showToast("请输入手机号");
                }else if(et_login_phone.getText().toString().length()!=11){
                    ToastUtil.showToast("请输入正确的手机号");
                }else{
                    if(TextUtils.isEmpty(et_login_code.getText().toString())){
                        ToastUtil.showToast("请输入验证码");
                    }else{
                        checkCode();
                    }
                }
//                checkCode();
                break;
            case R.id.btn_getcode:
                if(TextUtils.isEmpty(et_login_phone.getText().toString())){
                    ToastUtil.showToast("请输入手机号");
                }else if(et_login_phone.getText().toString().length()!=11){
                    ToastUtil.showToast("请输入正确的手机号");
                }else{
                    getCode();
                }
                break;
        }
    }
}
