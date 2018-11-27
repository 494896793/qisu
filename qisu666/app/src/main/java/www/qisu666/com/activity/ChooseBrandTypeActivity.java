package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.R;
import www.qisu666.com.adapter.BrandTypeAdapter;

import java.util.ArrayList;

//选择车型
public class ChooseBrandTypeActivity extends BaseActivity {


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_choose_brand_type);
        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.choose_brand_type_title);
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
        ListView lv_type = (ListView) findViewById(R.id.lv_type);
        lv_type.setAdapter(new BrandTypeAdapter(this, (ArrayList<String>)getIntent().getSerializableExtra("types")));
        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("type", (String)parent.getItemAtPosition(position+3));
                setResult(ConstantCode.RES_CHOOSE_BRAND_TYPE, intent);
                finish();
            }
        });
    }

}
