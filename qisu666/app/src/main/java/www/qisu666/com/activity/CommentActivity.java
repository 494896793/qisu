package www.qisu666.com.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.com.widget.StarBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//评论页面
public class CommentActivity extends BaseActivity {

    private EditText et_comment_content;
    private TextView tv_comment_count, tv_total_score;
    private StarBar starBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_comment);
        initView();
    }

    private void initView() {
        initTitleBar();
        starBar = (StarBar) findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        starBar.setStarMark(5.0f);

        et_comment_content = (EditText) findViewById(R.id.et_comment_content);
        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 300) {
                    et_comment_content.setText(s.subSequence(0, 300));
                    et_comment_content.setSelection(300);
                    tv_comment_count.setText("0");
                } else {
                    tv_comment_count.setText(String.valueOf(300 - s.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_total_score = (TextView) findViewById(R.id.tv_total_score);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);

        starBar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                tv_total_score.setText((int) mark + "分");
            }
        });
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("写点评");
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView rightBtn = (TextView) findViewById(R.id.tv_referring);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    connToServer();
                }
            }
        });
    }

    private boolean checkInput() {
        if (!TextUtils.isEmpty(et_comment_content.getText().toString())) {
            return true;
        }
        ToastUtil.showToast(getString(R.string.comment_empty));
        return false;
    }

    private void connToServer() {

        String url = "api/comment/add";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserParams.INSTANCE.getUser_id());
        map.put("stationId", getIntent().getStringExtra("station_id"));
        map.put("chargeOrderId", getIntent().getStringExtra("charge_order_id"));
        map.put("content", et_comment_content.getText().toString());
        map.put("grade", (int) starBar.getStarMark());

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
                        ToastUtil.showToast(getString(R.string.comment_success));
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });


//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "I101");
//            jsonObject.put("station_id", getIntent().getStringExtra("station_id"));
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("charge_order_id", getIntent().getStringExtra("charge_order_id"));
//            jsonObject.put("content", et_comment_content.getText().toString());
//            jsonObject.put("grade", (int) starBar.getStarMark());
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, true, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
//            @Override
//            public void onResponse(Map<String, Object> map, String tag) {
//                ToastUtil.showToast(getString(R.string.comment_success));
//                finish();
//            }
//        });
    }

}
