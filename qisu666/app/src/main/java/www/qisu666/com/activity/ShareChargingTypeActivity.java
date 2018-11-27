package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;

//分享充电桩
public class ShareChargingTypeActivity extends BaseActivity implements View.OnClickListener {

    private TextView btn_private;
    private TextView btn_public;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_share_charging_type);
        initViews();
    }

    private void initViews() {
        initTitleBar();
        btn_private = (TextView) findViewById(R.id.btn_private);
        btn_private.setOnClickListener(this);
        btn_public = (TextView) findViewById(R.id.btn_public);
        btn_public.setOnClickListener(this);
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.share_charging_type_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_private:
                Intent intent = new Intent(this, ShareChargingActivity.class);
                intent.putExtra("charge_pile_bel", "02");
                startActivity(intent);
                finish();
                break;
            case R.id.btn_public:
                Intent intent1 = new Intent(this, ShareChargingActivity.class);
                intent1.putExtra("charge_pile_bel", "01");
                startActivity(intent1);
                finish();
                break;
        }
    }
}
