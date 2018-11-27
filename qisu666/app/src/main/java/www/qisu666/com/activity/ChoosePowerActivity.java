package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.R;

//选择动力类型
public class ChoosePowerActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_choose_power);
        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.choose_power_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        LinearLayout layout_hybird = (LinearLayout) findViewById(R.id.layout_hybird);
        LinearLayout layout_electric = (LinearLayout) findViewById(R.id.layout_electric);

        layout_hybird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePowerActivity.this, ChooseBrandActivity.class);
                intent.putExtra("power", 1);
                startActivityForResult(intent, ConstantCode.REQ_CHOOSE_BRAND_TYPE);
            }
        });

        layout_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePowerActivity.this, ChooseBrandActivity.class);
                intent.putExtra("power", 2);
                startActivityForResult(intent, ConstantCode.REQ_CHOOSE_BRAND_TYPE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_CHOOSE_BRAND_TYPE && resultCode==ConstantCode.RES_CHOOSE_BRAND_TYPE){
            setResult(ConstantCode.RES_CHOOSE_BRAND_TYPE, data);
            finish();
        }
    }
}
