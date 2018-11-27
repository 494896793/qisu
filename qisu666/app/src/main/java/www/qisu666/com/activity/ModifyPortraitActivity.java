package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import www.qisu666.com.adapter.PortraitAdapter;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//更换头像
public class ModifyPortraitActivity extends BaseActivity {

    private GridView gridView;
    private PortraitAdapter adapter;
    private TextView btn_submit;
    private int current; //当前头像id
    private UserParams user = UserParams.INSTANCE;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_modify_portrait);
        initView();
        setListeners();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initTitleBar();

        gridView = (GridView) findViewById(R.id.gridView);
        btn_submit = (TextView) findViewById(R.id.btn_submit);

        current = user.getPicture();
        adapter = new PortraitAdapter(this, current);
        gridView.setAdapter(adapter);
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.modify_portrait_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListeners() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView img_checked = (ImageView) ((RelativeLayout)gridView.getChildAt(current)).findViewById(R.id.img_checked);
                img_checked.setVisibility(View.GONE);
                img_checked = (ImageView) ((RelativeLayout)gridView.getChildAt(position)).findViewById(R.id.img_checked);
                img_checked.setVisibility(View.VISIBLE);
                current = position;
                if(current == user.getPicture()){
                    btn_submit.setEnabled(false);
                } else {
                    btn_submit.setEnabled(true);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connToServer();
            }
        });
    }

    /**
     * 发送 A109 请求，修改头像信息
     */
    private void connToServer() {

        String url = "api/user/updatePicture";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("picture", current);

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
                        ToastUtil.showToast(R.string.toast_A109);
                        user.setPicture(current);
                        getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE).edit().putInt("picture", current).apply();
                        setResult(ConstantCode.RES_MODIFY_PORTRAIT, new Intent().putExtra("portraitId", current));
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "A109");
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("picture", current);
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
//            @Override
//            public void onResponse(Map<String,Object> map, String tag) {
//                ToastUtil.showToast(R.string.toast_A109);
//                user.setPicture(current);
//                getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE).edit().putInt("picture", current).commit();
//                setResult(ConstantCode.RES_MODIFY_PORTRAIT, new Intent().putExtra("portraitId", current));
//                finish();
//            }
//        });
    }

}
