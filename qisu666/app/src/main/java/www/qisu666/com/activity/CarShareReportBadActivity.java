package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CarLicenceAdapter;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.model.CarNowOrderBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//车辆信息
public class CarShareReportBadActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.img_title_right) ImageView img_title_left;
    @BindView(R.id.tv_brand) TextView tv_brand;
    @BindView(R.id.tv_licence) TextView tv_licence;
    @BindView(R.id.et_licence) ClearEditText et_licence;
    @BindView(R.id.btn_submit) Button btn_submit;

    private PopupWindow popupWindow;
    private CarLicenceAdapter adapter;
    private int province = 0; //当前选择的省份index

    //省份车牌简称
    private String[] provinces = {"京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫",
            "鄂", "湘", "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕", "甘", "青", "宁", "新", "港", "澳", "台"
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_report_bad);
        initViews();
        initPopupWindow();
        requestBadList();
    }

    private void requestBadList() {
        String url = "api/user/report/type/query";
        HashMap<String, Object> map = new HashMap<>();
        //1.用户反馈 2.故障报备
        map.put("reportType", "2");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarNowOrderBean.class))
                .compose(RxNetHelper.<CarNowOrderBean>io_main())
                .subscribe();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tv_title.setText(R.string.add_car_info_title);
    }



    @OnClick({R.id.img_title_left, R.id.layout_brand, R.id.tv_licence, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.layout_brand:
                startActivityForResult(new Intent(this, ChoosePowerActivity.class), ConstantCode.REQ_CHOOSE_BRAND_TYPE);
                break;
            case R.id.tv_licence:
                adapter.setItemSelected(province);
                adapter.notifyDataSetChanged();
                popupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_submit:
                if (checkInput()) {
//                    connToServer();
                }
                break;
        }
    }

    private boolean checkInput() {
        if (!tv_brand.getText().toString().trim().equals(getResources().getString(R.string.add_car_info_brand_hint)) && !TextUtils.isEmpty(et_licence.getText().toString().trim())) {
            return true;
        } else if (tv_brand.getText().toString().trim().equals(getResources().getString(R.string.add_car_info_brand_hint))) {
            ToastUtil.showToast(R.string.toast_add_car_info_no_brand);
        } else if (TextUtils.isEmpty(et_licence.getText().toString().trim())) {
            ToastUtil.showToast(R.string.toast_add_car_info_no_licence);
        }
        return false;
    }

    /**
     * 发送 B104 请求，添加车辆信息
     */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "B104");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("cars_brand", tv_brand.getText().toString());
            jsonObject.put("cars_license", tv_licence.getText().toString().trim() + et_licence.getText().toString().trim().toUpperCase());
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                /**
                 * C101 response:{"count_sum":20, "return_msg": "成功", "addit_num": "", "return_code": "0000", "addit_index": "",
                 *              "tran_list":[{"rn":1,"remark":"","electronics_certificate":"","product_name":"20151216第二期车贷",
                 *              "product_desc":"<p><strong style=\"color: rgb(69, 69, 69); font-family: arial, 宋体, sans-serif, tahoma, &#39;Microsoft YaHei&#39;; font-size: 14px; line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);\">个人汽车贷款的对象：年龄在18周岁以上，具有完全民事行为能力的公民；具有合法身份证件，有当地常住户口或有效居住证件。<br/><br/>　　申请个人汽车贷款必备的条件：贷款的个人要具有稳定的职业和经济收入或易于变现的资产，足以按期偿还贷款本息；贷款人自有资金足以支付建设银行规定的购车首付款；贷款人必须提供银行认可的担保；贷款人愿意接受银行认为必要的其他条件。<br/><br/>　　个人汽车贷款期限及利率：根据客户资信情况和所购车辆的用途，个人汽车贷款期限长短不同。其中，所购车辆用于出租营运、汽车租赁、交通运输经营的，最长期限为5年，用于货运的最长为3年。贷款利率按照人民银行规定的同期贷款利率执行，并允许按照人民银行规定实行上浮或下浮。<br/><br/>　　个人汽车贷款限额：按银行的个人信用评定办法达到A级以上的客户，可以用所购车辆作抵押申请汽车贷款，贷款额度最高为所购车辆销售款项的80％。借款人以银行认可的国债、金融债券、国家重点建设债券、银行出具的个人存单进行质押的，贷款额度最高为质押凭证价值的90％。借款人以房屋、其他地上定着物或依法取得的国有土地使用权作抵押的，贷款额度最高为抵押物评估价值的70％。保险公司提供分期还款保证保险的，贷款额最高为汽车销售款项的80％；购买再交易车辆的贷款额度最高为其评估价值的70％。提供第三方连带责任保证方式（银行、保险公司除外）的，按照银行的个人信用评定办法为借款人（或保证人）设定贷款额度，且贷款额度最高为汽车销售款项的80％；购买再交易车辆的，贷款额度最高为其评估价值的70％。</strong></p>",
                 *              "annual_rate":0.21,"avail_invest_money":121900,"borrow_money":8100,"product_status":"B01","end_date":"20151218","payment_money":0,
                 *              "repayment_type":"B","full_check_time":"","show_days":30,"if_full":"1","cust_no":"","expire":90,"publish_date":"20151216","product_type":"0",
                 *              "product_no":"2015121600000294","invest_schedule":0.062308,"check_time":"","risk_mng_remark":"none","create_time":"20151216174521",
                 *              "full_time":"20151218153345","security_type":"1","product_money":130000},
                 *
                 */
                ToastUtil.showToast(R.string.toast_B104);
                Intent intent = new Intent();
                intent.putExtra("carInfo", tv_brand.getText().toString());
                setResult(ConstantCode.RES_ADD_CAR_INFO, intent);
                finish();
            }
        });
    }

    /**
     * 选择车牌省份 PopupWindow
     */
    private void initPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_car_licence, null);

        GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
        adapter = new CarLicenceAdapter(this, provinces);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_licence.setText(((TextView) view.findViewById(R.id.tv)).getText());
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
        if (requestCode == ConstantCode.REQ_CHOOSE_BRAND_TYPE && resultCode == ConstantCode.RES_CHOOSE_BRAND_TYPE) {
            tv_brand.setText(data.getStringExtra("brand") + " " + data.getStringExtra("type"));
        }
    }


}
