package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;

/**
 * 717219917@qq.com 2018/8/7 18:29.
 */
public class CarMoneyExplainActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.cardmoneyexplain_layout);

        tv_title.setText("资费说明");
    }

    @OnClick(R.id.img_title_left)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case  R.id.img_title_left:
                finish();
                break;
        }
    }


}
