package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.adapter.CarInfoAdapter;
import www.qisu666.com.config.Config;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;

/**
 * 我的车辆 信息
 */
public class CarInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageView img_title_left;

    private ListView lv_car_info;
    private Button btn_add;

    private List<Map<String,Object>> carInfos = new ArrayList<Map<String,Object>>();
    private CarInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_info);
        initViews();
        setListeners();
        connToServer();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.car_info_title));
        img_title_left = (ImageView) findViewById(R.id.img_title_left);


        lv_car_info = (ListView) findViewById(R.id.lv_car_info);
        adapter = new CarInfoAdapter(this, carInfos);
        lv_car_info.setAdapter(adapter);
        btn_add = (Button) findViewById(R.id.btn_add);
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        img_title_left.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        lv_car_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CarInfoActivity.this, ModifyCarInfoActivity.class);
                intent.putExtra("carInfo", (com.alibaba.fastjson.JSONObject)(carInfos.get(position)));
                startActivityForResult(intent, ConstantCode.REQ_ADD_CAR_INFO);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btn_add:
                startActivityForResult(new Intent(this, AddCarInfoActivity.class), ConstantCode.REQ_ADD_CAR_INFO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_ADD_CAR_INFO && resultCode==ConstantCode.RES_ADD_CAR_INFO){
            connToServer();
        }
    }

    /**
     * 发送 B107 请求，查询车辆信息
     */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "B107");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String,Object> map, String tag) {

                carInfos.clear();

                if (map.get("record_list") != null && !"".equals(map.get("record_list".toString()))) {
                    try {
                        JSONArray array = new JSONArray(map.get("record_list").toString());

                        int count = array.length();
                        for (int i = 0; i < count; i++) {
                            JSONObject object = array.getJSONObject(i);
                            carInfos.add(JsonUtils.jsonToMap(object.toString()));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
