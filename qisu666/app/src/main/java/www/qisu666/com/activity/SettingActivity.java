package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import www.qisu666.com.R;
import www.qisu666.com.application.IDianNiuApp;
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
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.DataCleanManager;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.SPParams;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.common.utils.VersionUtils;
import www.qisu666.sdk.amap.stationMap.StationLocation;
import www.qisu666.sdk.carshare.Activity_CarshareWeb;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;
import www.qisu666.sdk.utils.Update;

//设置页面  (清除缓存等)
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private View ll_about, ll_check_update, ll_protocol, ll_clear_memory, ll_uescar;//新增用车协议
    private Button btn_unlogin;
    private TextView tv_cache_size, tv_version_info;
    private UserParams user = UserParams.INSTANCE;
    private SharedPreferences sp;
    private String version;//版本号
    private LinearLayout linear_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initData() {
        sp = getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        linear_parent=findViewById(R.id.linear_parent);
        ll_about = findViewById(R.id.ll_about);
        ll_check_update = findViewById(R.id.ll_check_update);
        ll_protocol = findViewById(R.id.ll_protocol);
        ll_clear_memory = findViewById(R.id.ll_clear_memory);
        ll_uescar = findViewById(R.id.ll_uescar);

        btn_unlogin = (Button) findViewById(R.id.btn_unlogin);

        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        tv_version_info = (TextView) findViewById(R.id.tv_version_info);
        tv_version_info.setText("当前版本：v" + PhoneParams.getAppVersion(this));
        try {
            tv_cache_size.setText(DataCleanManager.getCacheSize(this));
        } catch (Exception e) {
            tv_cache_size.setText("未知");
            e.printStackTrace();
        }
        ll_about.setOnClickListener(this);
        ll_check_update.setOnClickListener(this);
        ll_protocol.setOnClickListener(this);
        ll_clear_memory.setOnClickListener(this);
        btn_unlogin.setOnClickListener(this);
        ll_uescar.setOnClickListener(this);
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("设置");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(user.getUser_id())) {//如果未登录，隐藏退出登录按钮
            btn_unlogin.setText(R.string.setting_login);
        } else {
            btn_unlogin.setText(R.string.setting_exit);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            //                startActivity(new Intent(this, PayActivity.class));
            //                startActivity(new Intent(this, FinishChargingActivity.class));
            case R.id.ll_check_update:
                connToServerA106();
                break;
            case R.id.ll_protocol:
                startActivity(new Intent(this, AgreementActivity.class));
                break;
            case R.id.ll_clear_memory:
                DialogHelper.confirmDialog(this, getString(R.string.dialog_prompt_clear_cache), new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        try {
                            IDianNiuApp.getInstance().db.delete(StationLocation.class);//
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }

                        clearCache();
                        try {
                            tv_cache_size.setText(DataCleanManager.getCacheSize(SettingActivity.this));
                        } catch (Exception e) {
                            tv_cache_size.setText("未知");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            case R.id.btn_unlogin:
                if (btn_unlogin.getText().toString().equals(getResources().getString(R.string.setting_exit))) {
                    DialogHelper.confirmDialog(this, getString(R.string.dialog_prompt_exit_login), new AlertDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onConfirm() {
                            unLogin();
                            SettingActivity.this.finish();
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                } else {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_uescar:
                Intent intent = new Intent(SettingActivity.this, Activity_CarshareWeb.class);
                intent.putExtra("fromeSetting", "true");
                startActivity(intent);
                break;
            default:
                break;


        }
    }

    /**
     * 发送 A106 请求，获取版本更新
     */
    private void connToServerA106() {

        String url = "api/version/checkVersion";
        version = String.valueOf(VersionUtils.getVersionName(this));

        HashMap<String, Object> map = new HashMap<>();
        map.put("OSType", "android");
        map.put("appVersion", version);

        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        try {
                            // LogUtil.e("获取到 更新信息：" + tag);
                            // LogUtil.e("获取到 更新信息：" + map.toString());
                            // 对象转json
                            String s = JsonUtils.objectToJson(bean);
                            // json转 map
                            final Map jsonToMap = JsonUtils.jsonToMap(s);

                            if (VersionUtils.checkVersion(version, jsonToMap.get("latestVersion").toString()) > 0) { //每次提示是否更新   //getString(R.string.dialog_version_last)
                                DialogHelper.confirmTitleDialog(SettingActivity.this, null, jsonToMap.get("returnMsg").toString(), getString(R.string.dialog_version_confirm), getString(R.string.dialog_version_cancel), new AlertDialog.OnDialogButtonClickListener() {
                                    @Override
                                    public void onConfirm() {
                                        new Update(SettingActivity.this, jsonToMap.get("extraParams").toString(), false);
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                try {
//                                    intent.setData(Uri.parse(map.get("extra_params").toString()));//intent.setData(Uri.parse("https://www.pgyer.com/YrXM"));
//                                } catch (Throwable t) {
//                                    t.printStackTrace();
//                                    intent.setData(Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=www.qisu666.com"));//intent.setData(Uri.parse("https://www.pgyer.com/YrXM"));
//                                }
//                                startActivity(intent);
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                            } else {
                                ToastUtil.showToast("暂无新版本");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }

                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        LogUtil.e("版本更新 请求失败！" + bean.toString());
                        LogUtil.e("版本更新 请求失败！" + bean.msg);

                    }
                });
    }

    /**
     * 清除内部和sd卡的缓存
     */
    private void clearCache() {
        DataCleanManager.cleanInternalCache(this);
        DataCleanManager.cleanExternalCache(this);
        ToastUtil.showToast(R.string.toast_prompt_clear_cache);
    }

    /**
     * 发送 A102 请求，退出登录
     */
    private void unLogin() {
        String url = "api/user/logout";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", user.getUser_id());
        map.put("deviceUUID", PhoneParams.getDeviceUUID(this));

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
                        user.clear();
                        sp.edit().clear().commit();
                        setResult(ConstantCode.RES_UNLOGIN);
                        btn_unlogin.setText(R.string.setting_login);
                        EventBus.getDefault().post("exit_login");//更新用户资料的ui

                        // 退出
                        EventMsg eventMsg = new EventMsg();
                        eventMsg.setType(2);
                        RxBus.getInstance().post(eventMsg);
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });


//        try {
//            HttpLogic httpLogic = new HttpLogic(this);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("req_code", "A102");
//            jsonObject.put("user_id", user.getUser_id());
//            jsonObject.put("device_uuid", PhoneParams.getDeviceUUID(this));
//            jsonObject.put("s_token", user.getS_token());
//            httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//                @SuppressLint("ApplySharedPref")
//                @Override
//                public void onResponse(Map<String, Object> map, String tag) {
//                    user.clear();
//                    sp.edit().clear().commit();
//                    setResult(ConstantCode.RES_UNLOGIN);
//                    btn_unlogin.setText(R.string.setting_login);
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
