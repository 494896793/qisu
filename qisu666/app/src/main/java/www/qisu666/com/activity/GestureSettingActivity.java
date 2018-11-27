package www.qisu666.com.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternIndicatorView;
import com.github.ihsg.patternlocker.PatternLockerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.PatternHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;

/**
 * 手势密码设置界面
 *
 * @author layoute
 */
public class GestureSettingActivity extends BaseActivity {

    private PatternIndicatorView patternIndicatorView;
    private TextView tvMsg;
    private PatternHelper patternHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_gesture_setting);
        initView();
    }

    private void initView() {
        initTitleBar();

        this.patternIndicatorView = findViewById(R.id.pattern_indicator_view);
        PatternLockerView patternLockerView = findViewById(R.id.pattern_lock_view);
        this.tvMsg = findViewById(R.id.text_msg);

        patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                boolean isOk = isPatternOk(hitList);
                view.updateStatus(!isOk);
                patternIndicatorView.updateState(hitList, !isOk);
                updateMsg();
                Log.e("asd", "pwd is " + list2String(hitList));
                // 请求 API 储存密码
                final String url = "api/auth/password/set";
                HashMap<String, Object> requestMap = new HashMap<>();
                requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
                requestMap.put("gesturesPassword", list2String(hitList));

                MyNetwork.getMyApi()
                        .carRequest(url, MyMessageUtils.addBody(requestMap))
                        .map(new FlatFunction<>(String.class))
                        .compose(RxNetHelper.<String>io_main(mLoadingDialog))
                        .subscribe(new ProgressSubscriber<String>(mLoadingDialog) {

                            @Override
                            public void onSuccessCode(Message object) {

                            }

                            @Override
                            public void onSuccess(String bean) {
                                EventBus.getDefault().post("密码设置成功");
                            }

                            @Override
                            public void onFail(Message<String> bean) {

                            }
                        });
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        this.tvMsg.setText("设置解锁图案");
        this.patternHelper = new PatternHelper(getApplicationContext());
    }

    private boolean isPatternOk(List<Integer> hitList) {
        this.patternHelper.validateForSetting(hitList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.tvMsg.setText(this.patternHelper.getMessage());
        this.tvMsg.setTextColor(this.patternHelper.isOk() ?
                getResources().getColor(R.color.new_primary) :
                getResources().getColor(R.color.orange));
    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()) {
            finish();
        }
    }

    private void initTitleBar() {
        TextView title = findViewById(R.id.tv_title);
        title.setText("设置手势密码");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 转换为 1-9 的字符串
     */
    private String list2String(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : list) {
            stringBuilder.append(integer);
        }
        return stringBuilder.toString();
    }

}