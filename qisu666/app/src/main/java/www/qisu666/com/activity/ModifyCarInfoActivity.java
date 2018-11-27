package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

//修改车辆信息
public class ModifyCarInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageView img_title_left;
    private Button btn_submit;
//    private LinearLayout layout_brand;
    private TextView tv_licence;
    private TextView tv_brand;
    private ClearEditText et_licence;

//    private PopupWindow popupWindow;
//    private CarLicenceAdapter adapter;
//    //当前选择的省份index
//    private int province = 0;
//    //省份车牌简称
//    private String[] provinces = {"京","津","冀","晋","蒙","辽","吉","黑","沪","苏","浙","皖","闽","赣","鲁","豫",
//            "鄂","湘","粤","桂","琼","渝","川","贵","云","藏","陕","甘","青","宁","新","港","澳","台"
//    };

    private String cars_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_add_car_info);
        initViews();
        setListeners();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.add_car_info_title);
        img_title_left = (ImageView) findViewById(R.id.img_title_left);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setText(getResources().getString(R.string.app_delete));
//        btn_title_right = (TextView) findViewById(R.id.btn_title_right);
//        btn_title_right.setText(R.string.app_delete);
//        btn_title_right.setVisibility(View.VISIBLE);
//        findViewById(R.id.img_title_right).setLayoutParams(new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

//        layout_brand = (LinearLayout) findViewById(R.id.layout_brand);
        tv_licence = (TextView) findViewById(R.id.tv_licence);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        et_licence = (ClearEditText) findViewById(R.id.et_licence);
        et_licence.setFocusable(false);
        et_licence.setEnabled(false);
//        btn_submit = (Button) findViewById(R.id.btn_submit);

        Map<String,Object> map = (Map<String, Object>) getIntent().getSerializableExtra("carInfo");
        tv_brand.setText(map.get("cars_brand").toString());
        tv_licence.setText(map.get("cars_license").toString().substring(0,1));
        et_licence.setText(map.get("cars_license").toString().substring(1));
        cars_id = map.get("cars_id").toString();
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        img_title_left.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btn_submit:
                DialogHelper.confirmDialog(this, getString(R.string.dialog_prompt_delete_car_info), new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        connToServer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

    /**
     * 发送 B108 请求，删除车辆信息
     */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "B108");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("cars_id", cars_id);
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String,Object> map, String tag) {
                ToastUtil.showToast(R.string.toast_B108);
                setResult(ConstantCode.RES_ADD_CAR_INFO, new Intent());
                finish();
            }
        });
    }

    /**
     * 选择车牌省份 PopupWindow
     */
/*    private void initPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_car_licence, null);

        GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
        adapter = new CarLicenceAdapter(this, provinces);
        adapter.setItemSelected(province);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_licence.setText(((TextView)view.findViewById(R.id.tv)).getText());
                province = position;
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.bg_white));
        popupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_CHOOSE_BRAND_TYPE && resultCode==ConstantCode.RES_CHOOSE_BRAND_TYPE){
            tv_brand.setText(data.getStringExtra("brand")+" "+data.getStringExtra("type"));
        }
    }*/
}
