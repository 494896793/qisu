package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.event.NotificationUpdateEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

//push过来的消息详情
public class NotificationDetailActivity extends BaseActivity {

    private TextView tv_title;
    private ImageView img_title_left;

    private ScrollView scrollView;
    private TextView tv_notice_title;
    private TextView tv_time;
    private TextView tv_content;

    private Map<String,Object> map;
    private UserParams user = UserParams.INSTANCE;
    private boolean isFromNotify;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_notification_detail);
        initViews();
        setListeners();
//        connToServer();
    }

    /** 初始化控件 */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_title_left = (ImageView) findViewById(R.id.img_title_left);
        tv_title.setText(R.string.notification_title);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

        tv_notice_title = (TextView) findViewById(R.id.tv_notice_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_content = (TextView) findViewById(R.id.tv_content);

        String aa=getIntent().getStringExtra("notification");
        if(getIntent().getStringExtra("notification")!=null){
            map = JsonUtils.jsonToMap(getIntent().getStringExtra("notification"));
            setNotificationData(map);
            isFromNotify = false;
        } else {
            connToServerG102(getIntent().getExtras().getString(JPushInterface.EXTRA_MSG_ID));
            isFromNotify = true;
        }


    }

    /** 设置监听器*/
    private void setListeners() {
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**  发送 G102 请求，查询消息内容  @param msg_id */
    private void connToServerG102(String msg_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "G102");
//            jsonObject.put("s_token", user.getS_token());
//            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("msg_id", msg_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                setNotificationData(map);
            }
        });
    }

    /** 设置页面消息详情  @param map */
    private void setNotificationData(Map<String, Object> map) {
        tv_notice_title.setText(map.get("title").toString());
        tv_time.setText(map.get("jpushTime").toString().substring(0,10));
        /*if(map.get("content").toString().replace(" ", "").contains("ios")){
            tv_content.setText(map.get("content").toString().replace(" ", "").substring(0,map.get("content").toString().replace(" ", "").indexOf("ios")));
        }else if(map.get("content").toString().replace(" ", "").contains("IOS")){
            tv_content.setText(map.get("content").toString().replace(" ", "").substring(0,map.get("content").toString().replace(" ", "").indexOf("IOS")));
        } else {*/
            tv_content.setText(map.get("content").toString().replace(" ", ""));
        /*}*/
    }

    /** 发送 G103 请求，更新消息状态 */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "G103");
//            jsonObject.put("s_token", user.getS_token());
//            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("info_id", map.get("info_id").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                LogUtils.d("更新消息状态成功");
                EventBus.getDefault().post(new NotificationUpdateEvent(getIntent().getIntExtra("position", -1)));
            }
        });
    }

    @Override public void onBackPressed() {
        if(isFromNotify){
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onBackPressed();
    }
}
