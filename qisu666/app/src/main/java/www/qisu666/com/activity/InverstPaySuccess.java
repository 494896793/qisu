package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.thridparty.G;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.event.PaySuccessActivityEvent;
import www.qisu666.common.activity.BaseActivity;

/**
 * 717219917@qq.com 2018/8/14 18:45.
 */
public class InverstPaySuccess extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    private String subType;
    @BindView(R.id.result_img)
    ImageView result_img;
    @BindView(R.id.btn_submit)
    TextView btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_inverst_pay_success_layout);
        EventBus.getDefault().post(new PaySuccessActivityEvent());
        img_title_left.setVisibility(View.GONE);
        subType=getIntent().getStringExtra("subType");
        if(subType!=null){
            if(subType.equals("1")){        // 投资型
                result_img.setImageResource(R.mipmap.rg_14);
            }else{
                result_img.setImageResource(R.mipmap.rg_15);
            }
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText("认购成功");
    }
}
