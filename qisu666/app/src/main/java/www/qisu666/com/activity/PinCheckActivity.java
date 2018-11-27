package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import www.qisu666.com.R;
import www.qisu666.com.live.LiveResultActivity;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.constant.Constant;

/**
 * @author layoute
 * 数字密码检查
 */
public class PinCheckActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvCheckMsg, tvCheckForGesture;
    private Button btnGesture;
    private ImageView imgCancel;
    private PinEntryEditText pinCheck, pinCheckForGesture;
    private SlidingUpPanelLayout mSlidingLayout;
    private int errorCount;
    private String ERROR_COUNT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pin_check);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        tvCheckMsg.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.holo_red_dark));
//        if (errorCount <= Constant.MAX_ERROR_PWD) {
//            errorCount += 1;
//            pinCheck.setText(null);
//            tvCheckMsg.setText("密码错误" + errorCount + "次请重试");
//            SPUtil.put(mContext, Constant.ERROR_COUNT_KEY, errorCount);
//            Log.e("asd", "8888888" + errorCount);
//        }

        if (errorCount == Constant.MAX_ERROR_PWD) {
            closeKeyboard();
            pinCheck.setError(true);
            pinCheck.setEnabled(false);
            tvCheckMsg.setText(getString(R.string.max_error_pwd));
        }
    }

    @SuppressLint("CutPasteId")
    private void initView() {
        initTitleBar();
        initData();
        pinCheck = findViewById(R.id.txt_check_pin);
        pinCheckForGesture = findViewById(R.id.pin_check_for_gesture);
        tvCheckMsg = findViewById(R.id.pin_check_hint);
        tvCheckForGesture = findViewById(R.id.pin_check_hint);
        btnGesture = findViewById(R.id.btn_title_right);
        btnGesture.setOnClickListener(this);
        imgCancel = findViewById(R.id.cancel);
        imgCancel.setOnClickListener(this);

        mSlidingLayout = findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                LogUtil.e("onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                LogUtil.e("onPanelStateChanged " + newState);
            }
        });
        mSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        showKeyboard(pinCheck);
        pinCheck.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPinEntered(CharSequence str) {
                String tmpPin = (String) SPUtil.get(getApplicationContext(), Constant.DIGITAL_PWD_KEY, "");
                Log.e("asd", "保存的" + tmpPin);
                if(tmpPin.equals("")){

                }
                if (str.toString().equals(tmpPin)) {
                    // 验证成功，跳转下一界面
                    tvCheckMsg.setText("验证成功");
                    /*隐藏软键盘**/
                    closeKeyboard();
                    EventBus.getDefault().post("密码验证成功");
                    SPUtil.remove(mContext,Constant.ERROR_COUNT_KEY);
                    finish();
                } else {
                    tvCheckMsg.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.holo_red_dark));
                    if (errorCount <= Constant.MAX_ERROR_PWD) {
                        errorCount += 1;
                        pinCheck.setText(null);
                        tvCheckMsg.setText("密码错误" + errorCount + "次请重试");
                        SPUtil.put(mContext, Constant.ERROR_COUNT_KEY, errorCount);
                        Log.e("asd", "8888888" + errorCount);
                        if (errorCount == Constant.MAX_ERROR_PWD) {
                            closeKeyboard();
                            pinCheck.setError(true);
                            pinCheck.setEnabled(false);
                            tvCheckMsg.setText(getString(R.string.max_error_pwd));
                        }
                    }
                    // TODO 弹窗提醒是否重置密码
//                        EventBus.getDefault().post("重置密码");
                }
            }
        });

//        pinCheckForGesture.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
//            @Override
//            public void onPinEntered(CharSequence str) {
//                String tmpPin = (String) SPUtil.get(getApplicationContext(), Constant.DIGITAL_PWD_KEY, "");
//                if (str.toString().equals(tmpPin)) {
//                    Log.e("asd", "00000000000000000000000000");
//                    // 验证成功,开始设置手势密码
//                    tvCheckForGesture.setText("验证成功");
//                    mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                    ActivityUtil.startActivity(PinCheckActivity.this, GestureSettingActivity.class);
//                } else {
//                    Log.e("asd", "1111111111111111111111111111");
//                    tvCheckForGesture.setText("密码错误，请重试");
//                    tvCheckForGesture.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.holo_red_dark));
//                }
//            }
//        });
    }

    private void initTitleBar() {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("数字密码验证");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*隐藏软键盘**/
                closeKeyboard();
                finish();
            }
        });
        findViewById(R.id.tv_chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*隐藏软键盘**/
                closeKeyboard();
                ActivityUtil.startActivityWithOne(mContext, PhoneCheckActivity.class, "数字重置密码");
                finish();
            }
        });
    }

    private void initData() {
        ERROR_COUNT_KEY = UserParams.INSTANCE.getUser_id() + "_error_count";
        errorCount = (int) SPUtil.get(PinCheckActivity.this, Constant.ERROR_COUNT_KEY, 0);
        Log.e("asd", "errorCount:" + errorCount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_title_right:
                String gestureStatus = (String) SPUtil.get(getApplicationContext(), Constant.GESTURE_STATUS_KEY, "");
                if (Constant.SET.equals(gestureStatus)) {
                    // TODO 手势密码已设置
                    /*隐藏软键盘**/
                    closeKeyboard();
                    ActivityUtil.startActivity(PinCheckActivity.this, GestureCheckActivity.class);
                    finish();
                } else {
                    // TODO 上拉界面验证数字密码
                    if (mSlidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                }
                break;
            case R.id.cancel:
                // 隐藏上拉界面
                if (mSlidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                    mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            default:
                break;
        }
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
}
