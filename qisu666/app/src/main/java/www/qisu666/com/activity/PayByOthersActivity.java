package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.AlertDialog;

import me.codeboy.android.aligntextview.AlignTextView;

//代付管理
public class PayByOthersActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout layout_manage, layout_record;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pay_by_others);
        initView();
        setListeners();
    }

    /** 初始化控件 */
    private void initView() {
        initTitleBar();
        layout_manage = (LinearLayout) findViewById(R.id.layout_manage);
        layout_record = (LinearLayout) findViewById(R.id.layout_record);
    }

    /** 初始化标题栏 */
    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.pay_by_others_title));
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView right_btn = (ImageView) findViewById(R.id.img_title_right);
        right_btn.setImageResource(R.mipmap.ic_pay_by_others_help_white);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = DialogHelper.alertDialog(PayByOthersActivity.this, getString(R.string.dialog_pay_by_others_help_1_title), getString(R.string.dialog_pay_by_others_help_1));
//                alertDialog.getMessage_tv().setAlign(AlignTextView.Align.ALIGN_LEFT);
                alertDialog.getMessage_tv().setGravity(Gravity.LEFT);
            }
        });
    }

    /** 设置监听器 */
    private void setListeners() {
        layout_manage.setOnClickListener(this);
        layout_record.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_manage:
                startActivity(new Intent(this, PayByOthersManageActivity.class)); break;

            case R.id.layout_record:
                startActivity(new Intent(this, PayByOthersRecordActivity.class));  break;
        }
    }
}
