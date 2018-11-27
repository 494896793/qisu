package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
import www.qisu666.com.adapter.ChargeFeeAdapter;
import www.qisu666.com.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//最近充电
public class LastChargeActivity extends BaseActivity {

    private TextView tv_title;
    private ImageView img_title_left;
    private TextView tv_terminal_no;
    private TextView tv_payment_amount;
    private TextView tv_charging_amount;
    private TextView tv_charging_duration;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private NoScrollListView lv_fees;
    private List<Map<String,Object>> list;
    private ChargeFeeAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_charging_detail);
        initViews();
        setAdapter();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("title"));
        img_title_left = (ImageView) findViewById(R.id.img_title_left);
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_terminal_no = (TextView) findViewById(R.id.tv_terminal_no);
        tv_payment_amount = (TextView) findViewById(R.id.tv_payment_amount);
        tv_charging_amount = (TextView) findViewById(R.id.tv_charging_amount);
        tv_charging_duration = (TextView) findViewById(R.id.tv_charging_duration);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        lv_fees = (NoScrollListView) findViewById(R.id.lv_fees);
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("最近充电");
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        list = new ArrayList<Map<String, Object>>();
        adapter = new ChargeFeeAdapter(this, list);
        lv_fees.setAdapter(adapter);
//        list.add(new HashMap<String, Object>());
//        list.add(new HashMap<String, Object>());
//        list.add(new HashMap<String, Object>());
//        list.add(new HashMap<String, Object>());
//        list.add(new HashMap<String, Object>());
//        list.add(new HashMap<String, Object>());
    }
}
