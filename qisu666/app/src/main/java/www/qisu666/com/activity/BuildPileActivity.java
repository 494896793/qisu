package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.DensityUtil;

/**
 * 我要做桩主
 */
public class BuildPileActivity extends BaseActivity {

    private ImageView iv_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_build_pile);
        initViews();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.build_pile_title);
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
        initTitleBar();
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuildPileActivity.this, ChargingOwnerActivity.class));
                finish();
            }
        });

        iv_banner = (ImageView) findViewById(R.id.iv_banner);
        iv_banner.getLayoutParams().height = (int) (DensityUtil.getScreenWidth(this) / 2.3);
    }
}
