package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.constant.Constant;
import www.qisu666.common.utils.ToastUtil;

/**
 * 数字密码设置界面
 *
 * @author layoute
 * @date 2018.06.14
 */
public class PinSettingActivity extends BaseActivity implements View.OnClickListener {


    private String pin;
    private ViewSwitcher pinSwitcher;
    private TextView tvPinHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pin_setting);
        initView();
    }

    private void initView() {
        initTitleBar();
        tvPinHint = findViewById(R.id.pin_hint);
        // 初始化 ViewSwitcher
        pinSwitcher = findViewById(R.id.pin_switcher);
        Animation slideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        pinSwitcher.setInAnimation(slideInLeft);
        pinSwitcher.setOutAnimation(slideOutRight);

        // 第一次设置 Pin 码
        final PinEntryEditText pinEntry = findViewById(R.id.txt_pin);
        if (pinEntry != null) {
            pinEntry.setFocusable(true);
            pinEntry.requestFocus();
            pinEntry.setAnimateText(true);
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length() == 6) {
                        pin = str.toString();
                        pinSwitcher.setDisplayedChild(1);
                        tvPinHint.setText(getResources().getString(R.string.pin_hint_confirm));
                    }
                }
            });
        }

        // 确认 Pin 码
        final PinEntryEditText pinEntryConfirm = findViewById(R.id.txt_pin_confirm);
        if (pinEntryConfirm != null) {
            pinEntry.setAnimateText(true);
            pinEntryConfirm.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(final CharSequence str) {
                    if (pin.equals(str.toString())) {
                        // 本地储存密码
                        SPUtil.put(getApplicationContext(), Constant.DIGITAL_PWD_KEY, str.toString());
                        // 请求 API 储存密码
                        final String url = "api/auth/password/set";
                        HashMap<String, Object> requestMap = new HashMap<>();
                        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
                        requestMap.put("digitalPassword", str.toString());
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
                                        ToastUtil.showToast("数字密码设置成功");
                                        // 进入预约取车界面
                                        EventBus.getDefault().post("数字密码设置完成");
                                        finish();
                                    }

                                    @Override
                                    public void onFail(Message<String> bean) {

                                    }
                                });


//                         // 弹出提示，提示设置手势密码，否则进入预约用车阶段
//                         AlertDialog alertDialog = new AlertDialog(PinSettingActivity.this,
//                                 "设置手势密码", "开始用车","若需要设置手势密码请选择创建\"手势密码\",若不需要请选择\"开始用车\"", true);
//                         alertDialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
//                             @Override
//                             public void onConfirm() {
//                                 ActivityUtil.startActivity(PinSettingActivity.this, GestureSettingActivity.class);
//                                 finish();
//                             }
//
//                             @Override
//                             public void onCancel() {
//                                 // 进入预约取车界面
//                                 EventBus.getDefault().post("数字密码设置完成");
//                                 finish();
//                             }
//                         });
//                         alertDialog.show();
                    } else {
                        tvPinHint.setText(getResources().getString(R.string.pin_hint_confirm_fail));
                        pinEntryConfirm.setError(true);
                        //todo 输入密码错误提示问题
                        pinEntryConfirm.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pinEntryConfirm.setText(null);
                                pinSwitcher.setDisplayedChild(0);
                                pinEntry.setText(null);
                                tvPinHint.setText(getResources().getString(R.string.pin_hint));
                            }
                        }, 1000);
                    }
                }
            });
        }
    }

    private void initTitleBar() {
        TextView title = findViewById(R.id.tv_title);
        title.setText("设置数字密码");
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
