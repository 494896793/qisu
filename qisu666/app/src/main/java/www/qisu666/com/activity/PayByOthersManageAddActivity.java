package www.qisu666.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.MatchUtils;
import www.qisu666.com.util.UserParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

//新增被代付人
public class PayByOthersManageAddActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_phone, et_name;
    private TextView btn_submit;
    private UserParams user = UserParams.INSTANCE;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pay_by_others_manage_add);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_name = (EditText) findViewById(R.id.et_name);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.pay_by_others_manage_add_title);
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(checkInput()) { connToServer();  break; }
        }
    }

    private boolean checkInput() {
        if(TextUtils.isEmpty(et_phone.getText().toString().trim())){
            ToastUtil.showToast(R.string.toast_phone_is_null);
        }else if(!MatchUtils.matchMobileNo(et_phone.getText().toString().trim())){
            ToastUtil.showToast(R.string.toast_phone_length_illegal);
        }else if(TextUtils.isEmpty(et_name.getText().toString().trim())){
            ToastUtil.showToast(R.string.toast_name_is_null);
        }else{
            return true;
        }
        return false;
    }

    /** 发送 I103 请求，添加被代付人 */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "I103");
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("mobile_no", et_phone.getText().toString().trim());
            jsonObject.put("user_name", et_name.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                ToastUtil.showToast(R.string.toast_I103);
                setResult(ConstantCode.RES_ADD_BE_PAYING_AGENT);
                finish();
            }
        });
    }
}
