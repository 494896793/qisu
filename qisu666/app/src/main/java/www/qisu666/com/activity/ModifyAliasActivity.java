package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.SPParams;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//修改昵称
public class ModifyAliasActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView img_title_left;
    private ClearEditText et_alias;
    private Button btn_submit;
    private String oldAlias;
    private UserParams user = UserParams.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_modify_alias);
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.modify_alias_title));
        img_title_left = (ImageView) findViewById(R.id.img_title_left);
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_alias = (ClearEditText) findViewById(R.id.et_alias);
        oldAlias = getIntent().getStringExtra("alias");
        et_alias.setText(oldAlias);
        et_alias.setSelection(oldAlias.length());
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                final String alias = et_alias.getText().toString().trim();

                if (!oldAlias.equals(alias) && !"".equals(alias)) {
                    String url = "api/user/modifyalisaname";
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", user.getUser_id());
                    map.put("userName", alias);

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
                                    ToastUtil.showToast(R.string.toast_B101);
                                    user.setCust_alias(et_alias.getText().toString());
                                    getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE).edit().putString("user_name", alias).apply();
                                    setResult(ConstantCode.RES_MODIFY_ALIAS, new Intent());
                                    finish();
                                }

                                @Override
                                public void onFail(Message<Object> bean) {
                                    Log.e("aaaa", "获取失败：" + bean.toString());
                                }

                            });
                }
                break;
            default:
                break;
        }
    }
}
