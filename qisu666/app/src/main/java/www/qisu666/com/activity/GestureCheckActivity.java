package www.qisu666.com.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.live.LiveResultActivity;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.PatternHelper;
import www.qisu666.com.util.SPUtil;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.constant.Constant;

/**
 * @author layoute
 * 手势密码检查
 */
public class GestureCheckActivity extends BaseActivity {

    private TextView tvCheckMsg;
    private PatternLockerView patternCheck;
    private PatternHelper patternHelper;
    private int errorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_gesture_check);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("asd", "errorCount:" + errorCount);
        // TODO 检测到到达上限就弹窗
//        if (errorCount <= Constant.MAX_ERROR_PWD) {
//            errorCount += 1;
//            SPUtil.put(mContext, Constant.ERROR_GESTURE_KEY, errorCount);
//            Log.e("asd", "errorCount:" + errorCount);
//        }

        if (errorCount == Constant.MAX_ERROR_PWD) {
            closeKeyboard();
            patternCheck.setEnabled(false);
            tvCheckMsg.setText("您的错误次数已达上限，请前往重置密码");
        }
    }

    private void initView() {
        initTitleBar();
        initData();

        this.tvCheckMsg = findViewById(R.id.text_check_msg);
        this.patternCheck = findViewById(R.id.pattern_lock_check_view);

//        patternCheck.setFillColor(getResources().getColor(R.color.primary))
//                .setNormalColor(getResources().getColor(R.color.bg_white))
//                .setHitColor(getResources().getColor(R.color.colorPrimaryDark))
//                .setErrorColor(getResources().getColor(android.R.color.holo_red_dark))
//                .setLineWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,
//                        getResources().getDisplayMetrics()))
//                .buildWithDefaultStyle();

        this.tvCheckMsg.setText("解锁手势图案");
        this.patternHelper = new PatternHelper(this);


        this.patternCheck.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {

            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                boolean isError = !isPatternOk(hitList);
                view.updateStatus(isError);
                updateMsg();
                if (isError) {
                    if (errorCount <= Constant.MAX_ERROR_PWD) {
                        errorCount += 1;
                        SPUtil.put(mContext, Constant.ERROR_GESTURE_KEY, errorCount);
                        Log.e("asd", "errorCount:" + errorCount);
                        if (errorCount == Constant.MAX_ERROR_PWD) {
                            closeKeyboard();
                            patternCheck.setEnabled(false);
                            tvCheckMsg.setText(getString(R.string.max_error_pwd));
                        }
                    }
////                        // TODO 弹窗提醒是否重置密码
////                       EventBus.getDefault().post("重置密码");
                } else {
                    SPUtil.remove(mContext, Constant.ERROR_GESTURE_KEY);
                    EventBus.getDefault().post("密码验证成功");
                    finish();
                }
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

    }

    private void initTitleBar() {
        TextView title = findViewById(R.id.tv_title);
        title.setText("验证手势密码");
        View leftBtn = findViewById(R.id.img_title_left);
        findViewById(R.id.btn_title_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivity(GestureCheckActivity.this, PinCheckActivity.class);
                finish();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
            }
        });
        findViewById(R.id.tv_chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                ActivityUtil.startActivityWithOne(mContext, PhoneCheckActivity.class, "手势重置密码");
                finish();
            }
        });
    }

    private void initData() {
        errorCount = (int) SPUtil.get(GestureCheckActivity.this, Constant.ERROR_GESTURE_KEY, 0);
    }

    private boolean isPatternOk(List<Integer> hitList) {
        this.patternHelper.validateForChecking(hitList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.tvCheckMsg.setText(this.patternHelper.getMessage());
        this.tvCheckMsg.setTextColor(this.patternHelper.isOk() ?
                getResources().getColor(R.color.new_primary) :
                getResources().getColor(R.color.orange));
    }

    private void finishIfNeeded() {
        if (patternHelper.isFinish()) {
            patternCheck.setEnabled(false);
//            finish();
        }
    }

    /**
     * 隐藏软键盘
     **/
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputmanger != null;
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 转换为 1-9 的字符串
     */
    private String list2String(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : list) {
            stringBuilder.append(integer + 1);
        }
        return stringBuilder.toString();
    }

}
