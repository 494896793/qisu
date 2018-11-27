package www.qisu666.com.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import www.qisu666.com.event.FinishActivityEvent;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
import www.qisu666.com.util.ActivityUtil;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.sdk.amap.carShare.Activity_ReturnCarEveryWhere;

/**
 * 还车Activity   确认还车
 */
public class CarShareReturnCarActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_return_car);
        EventBus.getDefault().register(this);
        mContext = this;
        initViews();
    }

    private void initViews() {
        tvTitle.setText("还车准备");
    }

    @OnClick({R.id.img_title_left, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                ActivityUtil.startActivity(mContext, Activity_ReturnCarEveryWhere.class);
//                ActivityUtil.startActivity(mContext, CarShareReturnLockActivity.class);
//                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(FinishActivityEvent event){
        finish();
    }


}
