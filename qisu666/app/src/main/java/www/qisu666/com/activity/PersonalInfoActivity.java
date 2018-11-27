package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.de.hdodenhof.circleimageview.CircleImageView;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.SPParams;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.GlobalParams;
import www.qisu666.com.util.UserParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//个人信息
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private ImageView img_title_left;
    private LinearLayout layout_portrait, layout_alias, layout_gender, layout_phone, layout_car_info, layout_modify_pwd;
    private TextView tv_alias, tv_gender, tv_phone;
    private CircleImageView img_portrait;
    private PopupWindow genderPopupWindow;
    private UserParams user = UserParams.INSTANCE;
    private HttpLogic httpLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_personal_info);
        initData();
        initViews();
        setListeners();
        initGenderPopupWindow();
    }

    private void initData() {
        httpLogic = new HttpLogic(this);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.personal_info_title));
        img_title_left = (ImageView) findViewById(R.id.img_title_left);

        layout_portrait = (LinearLayout) findViewById(R.id.layout_portrait);
        layout_alias = (LinearLayout) findViewById(R.id.layout_alias);
        layout_gender = (LinearLayout) findViewById(R.id.layout_gender);
        layout_phone = (LinearLayout) findViewById(R.id.layout_phone);
//        layout_car_info = (LinearLayout) findViewById(R.id.layout_car_info);
        layout_modify_pwd = (LinearLayout) findViewById(R.id.layout_modify_pwd);

        img_portrait = (CircleImageView) findViewById(R.id.img_portrait);
        tv_alias = (TextView) findViewById(R.id.tv_alias);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        img_portrait.setImageResource(GlobalParams.icons[user.getPicture()]);
        tv_alias.setText(user.getCust_alias());
        tv_gender.setText(user.getSex());
        tv_phone.setText(user.getTel_no());
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        img_title_left.setOnClickListener(this);
        layout_portrait.setOnClickListener(this);
        layout_alias.setOnClickListener(this);
        layout_gender.setOnClickListener(this);
        layout_phone.setOnClickListener(this);
//        layout_car_info.setOnClickListener(this);
        layout_modify_pwd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.layout_portrait:
                Intent portraitIntent = new Intent(this, ModifyPortraitActivity.class);
//                portraitIntent.putExtra("portraitId", 0);
                startActivityForResult(portraitIntent, ConstantCode.REQ_MODIFY_PORTRAIT);
                break;
            case R.id.layout_alias:
                Intent intent = new Intent(this, ModifyAliasActivity.class);
                intent.putExtra("alias", tv_alias.getText().toString());
                startActivityForResult(intent, ConstantCode.REQ_MODIFY_ALIAS);
                break;
            case R.id.layout_gender:
                genderPopupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.layout_phone:
                break;
//            case R.id.layout_car_info:
//                startActivity(new Intent(PersonalInfoActivity.this, CarInfoActivity.class));
//                break;
            case R.id.layout_modify_pwd:
                startActivity(new Intent(PersonalInfoActivity.this, ModifyPwdActivity.class));
                break;
            case R.id.tv_male:
            case R.id.tv_female:
                final String sex = ((TextView) v).getText().toString();
                String url = "api/user/modifyusersex";
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", user.getUser_id());
                map.put("sex", sex.equals("男") ? "1" : "2");
                MyNetwork.getMyApi()
                        .carRequest(url, MyMessageUtils.addBody(map))
                        .map(new FlatFunction<>(Object.class))
                        .compose(RxNetHelper.<Object>io_main())
                        .subscribe(new ResultSubscriber<Object>() {
                            @Override
                            public void onSuccessCode(Message object) {

                            }

                            @Override
                            @SuppressWarnings("unchecked")
                            public void onSuccess(Object bean) {

                                getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE).edit().putString("sex", sex).apply();
                                tv_gender.setText(sex);
                                user.setSex(sex);
                            }

                            @Override
                            public void onFail(Message<Object> bean) {
                                Log.e("aaaa", "获取失败：" + bean.toString());
                            }

                        });

//                try { /** 发送 B102 请求，修改性别 */
//                    final String sex = ((TextView) v).getText().toString();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("req_code", "B102");
//                    jsonObject.put("user_id", user.getUser_id());
//                    jsonObject.put("sex", sex.equals("男") ? "1" : "2");
//                    jsonObject.put("s_token", user.getS_token());
//                    httpLogic.sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
//                        @Override
//                        public void onResponse(Map<String, Object> map, String tag) {
//                            getSharedPreferences(SPParams.USER_INFO, MODE_PRIVATE).edit().putString("sex", sex).commit();
//                            tv_gender.setText(sex);
//                            user.setSex(sex);
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                genderPopupWindow.dismiss();
                break;
            default:
                break;

        }
    }

    /**
     * 选择性别PopupWindow
     */
    private void initGenderPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_personal_info_gender, null);// 一个自定义的布局，作为显示的内容
        TextView tv_male = (TextView) contentView.findViewById(R.id.tv_male);
        TextView tv_female = (TextView) contentView.findViewById(R.id.tv_female);
        TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);

        tv_male.setOnClickListener(this);
        tv_female.setOnClickListener(this);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderPopupWindow.dismiss();
            }
        });

        genderPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        genderPopupWindow.setTouchable(true);
        genderPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        genderPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.bg_white));
        genderPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantCode.REQ_MODIFY_ALIAS && resultCode == ConstantCode.RES_MODIFY_ALIAS) {
            tv_alias.setText(user.getCust_alias());
        } else if (requestCode == ConstantCode.REQ_MODIFY_PORTRAIT && resultCode == ConstantCode.RES_MODIFY_PORTRAIT) {
            img_portrait.setImageResource(GlobalParams.icons[data.getIntExtra("portraitId", 0)]);
        }
    }


    /** 头像PopupWindow */
    /*private void initPortraitPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_personal_info_portrait, null);

        TextView tv_camera = (TextView) contentView.findViewById(R.id.tv_camera);
        TextView tv_local = (TextView) contentView.findViewById(R.id.tv_local);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_camera.setOnClickListener(this);
        tv_local.setOnClickListener(this);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portraitPopupWindow.dismiss();
            }
        });

        portraitPopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        portraitPopupWindow.setTouchable(true);
        portraitPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        portraitPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.bg_white));
        portraitPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);

    }*/
}
