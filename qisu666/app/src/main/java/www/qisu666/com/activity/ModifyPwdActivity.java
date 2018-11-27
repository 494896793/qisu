package www.qisu666.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.security.MD5;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


//修改密码
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_old_pwd, et_new_reinput_pwd, et_new_pwd;
    private TextView btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_modify_pwd);
        initView();
    }

    private void initView() {
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_reinput_pwd = (EditText) findViewById(R.id.et_new_reinput_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("修改密码");
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
            case R.id.btn_submit:
                if (checkInput()) {

                    String url = "api/user/modifyloginpwd";
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", UserParams.INSTANCE.getUser_id());
                    map.put("oldLoginPassword", MD5.getMD5Code(et_old_pwd.getText().toString().trim()));
                    map.put("newLoginPassword", MD5.getMD5Code(et_new_pwd.getText().toString().trim()));

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
                                    ToastUtil.showToast(R.string.toast_B103);
                                    finish();
                                }

                                @Override
                                public void onFail(Message<Object> bean) {
                                    ToastUtil.showToast(bean.msg);
                                    LogUtil.e("aaaa", "获取失败：" + bean.toString());
                                }

                            });

//                    try {
//                        HttpLogic httpLogic = new HttpLogic(this); /** 发送 B103 请求，修改密码 */
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("req_code", "B103");
//                        jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//                        jsonObject.put("old_login_password", MD5.getMD5Code(et_old_pwd.getText().toString().trim()));
//                        jsonObject.put("new_login_password", MD5.getMD5Code(et_new_pwd.getText().toString().trim()));
//                        jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//                        httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
//                            @Override
//                            public void onResponse(Map<String, Object> map, String tag) {
//                                ToastUtil.showToast(R.string.toast_B103);
//                                finish();
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                }
            default:
                break;
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(et_old_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_old_is_null);
        } else if (TextUtils.isEmpty(et_new_pwd.getText().toString().trim()) || TextUtils.isEmpty(et_new_reinput_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_new_is_null);
        } else if (!et_old_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$") || !et_new_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$") || !et_new_reinput_pwd.getText().toString().trim().matches("^[A-Za-z0-9]+$")) {
            ToastUtil.showToast(R.string.toast_pwd_illegal);
        } else if (et_new_pwd.length() < 6 || et_new_pwd.length() > 16 || et_old_pwd.length() < 6 || et_old_pwd.length() > 16 | et_new_reinput_pwd.length() < 6 || et_new_reinput_pwd.length() > 16) {
            ToastUtil.showToast(R.string.toast_pwd_length_illegal);
        } else if (!et_new_pwd.getText().toString().trim().equals(et_new_reinput_pwd.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_pwd_not_same);
        } else {
            return true;
        }
        return false;
    }
}
