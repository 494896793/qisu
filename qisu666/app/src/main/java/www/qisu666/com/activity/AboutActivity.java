package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import www.qisu666.com.R;
import www.qisu666.com.application.PhoneParams;
import www.qisu666.common.activity.BaseActivity;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        //测试svn0001 3333
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        TextView tv_company = (TextView) findViewById(R.id.tv_company);
        tv_company.setText(tv_company.getText().toString());
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("v" + PhoneParams.getAppVersion(this));
        TextView tv_html = (TextView) findViewById(R.id.tv_html);
        tv_html.setText(Html.fromHtml("<u>http://www.qisu666.com</u>"));
        tv_html.setTextColor(ContextCompat.getColor(this, R.color.primary));
        tv_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.qisu666.com"));
                startActivity(intent);
            }
        });
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("关于我们");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
}
