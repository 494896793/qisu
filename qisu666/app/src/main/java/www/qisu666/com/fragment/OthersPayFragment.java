package www.qisu666.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import www.qisu666.common.utils.DensityUtil;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.OthersPayAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.util.ChargeStatus;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersPayFragment extends Fragment implements View.OnClickListener {

    private View view;

    private RadioGroup radio_group;
    private RadioButton radio_amount_1;
    private RadioButton radio_amount_2;
    private RadioButton radio_amount_3;
    private int currentRadioButtonId = -1;

    private EditText et_amount;
    private ImageView img_help;
    private LinearLayout layout_select_others;
    private TextView tv_select_others;
    private ListView lv_others;
    private TextView btn_submit;

    private List<Map<String,Object>> othersInfos;
    private OthersPayAdapter adapter;

    //支付金额
    private String payAmount = "";
    //桩号
    private String charge_pile_seri;
    //枪号
    private String charge_pile_num;
    //代付人Id
    private String user_id;

    private UserParams user = UserParams.INSTANCE;

    public OthersPayFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String charge_pile_seri, String charge_pile_num){
        OthersPayFragment fragment = new OthersPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("charge_pile_seri", charge_pile_seri);
        bundle.putString("charge_pile_num", charge_pile_num);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        connToServerForPayingAgent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_others_pay, container, false);

        if(getArguments()!=null){
            charge_pile_seri = getArguments().getString("charge_pile_seri");
            charge_pile_num = getArguments().getString("charge_pile_num");
        }
        initView();
        setListeners();
        connToServerForPayingAgent();
        return view;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        radio_group = (RadioGroup) view.findViewById(R.id.radio_group);
        radio_amount_1 = (RadioButton) view.findViewById(R.id.radio_amount_1);
        radio_amount_2 = (RadioButton) view.findViewById(R.id.radio_amount_2);
        radio_amount_3 = (RadioButton) view.findViewById(R.id.radio_amount_3);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        img_help = (ImageView) view.findViewById(R.id.img_help);
        layout_select_others = (LinearLayout) view.findViewById(R.id.layout_select_others);
        tv_select_others = (TextView) view.findViewById(R.id.tv_select_others);
        lv_others = (ListView) view.findViewById(R.id.lv_others);
        btn_submit = (TextView) view.findViewById(R.id.btn_submit);

        othersInfos = new ArrayList<Map<String, Object>>();
        //demo代码，添加静态数据
//        for (int i=0;i<10;i++){
//            HashMap map = new HashMap<String, Object>();
//            map.put("test","张三"+i);
//            othersInfos.add(map);
//        }
        adapter = new OthersPayAdapter(getActivity(), othersInfos);
        lv_others.setAdapter(adapter);

        layout_select_others.post(new Runnable() {
            @Override
            public void run() {
                int[] coordinate = new int[2];
                layout_select_others.getLocationOnScreen(coordinate);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.getScreenHeight(getActivity()) - coordinate[1] - layout_select_others.getHeight());
                layoutParams.addRule(RelativeLayout.BELOW, R.id.layout_select_others);
                layoutParams.setMargins(0,DensityUtil.dip2px(getActivity(),2),0,0);
                lv_others.setLayoutParams(layoutParams);
                LogUtils.d("y:"+coordinate[1]+",getScreenHeight:"+DensityUtil.getScreenHeight(getActivity())+",total:"+(DensityUtil.getScreenHeight(getActivity()) - coordinate[1] - layout_select_others.getHeight()));
            }
        });

    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        layout_select_others.setOnClickListener(this);
        img_help.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        radio_amount_1.setOnClickListener(this);
        radio_amount_2.setOnClickListener(this);
        radio_amount_3.setOnClickListener(this);

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("") && currentRadioButtonId != -1) {
                    radio_group.check(currentRadioButtonId);
                    LogUtils.d(((RadioButton) view.findViewById(R.id.radio_amount_3)).isChecked() + "");
                } else if (!s.toString().equals("")) {
                    if (currentRadioButtonId != -1) {
                        radio_group.clearCheck();
                        currentRadioButtonId = -1;
                    }

                    payAmount = s.toString();
                }
//                LogUtils.d("currentRadioButtonId="+currentRadioButtonId);
//                LogUtils.d("RadioGroup:"+((RadioButton)findViewById(R.id.radio_amount_1)).isChecked()+","+((RadioButton)findViewById(R.id.radio_amount_2)).isChecked()
//                        +","+((RadioButton)findViewById(R.id.radio_amount_3)).isChecked());
            }
        });

        lv_others.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_name = (TextView) ((LinearLayout)lv_others.getChildAt(position)).findViewById(R.id.tv_name);
                tv_select_others.setText(tv_name.getText().toString());
                user_id = othersInfos.get(position).get("user_id").toString();
                lv_others.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select_others:
                if(lv_others.getVisibility() == View.VISIBLE){
                    lv_others.setVisibility(View.GONE);
                } else {
                    lv_others.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_submit:
                if (checkInput()) {
                    ChargeStatus.INSTANCE.setStatus(ChargeStatus.STATUS_UNKNOWN);
                    connToServer();
                }
                break;
            case R.id.radio_amount_1:
            case R.id.radio_amount_2:
            case R.id.radio_amount_3:
                onRadioButonClick(v);
                break;
            case R.id.img_help:
                DialogHelper.alertDialog(getActivity(), getString(R.string.dialog_pay_by_others_help_2_title), getString(R.string.dialog_pay_by_others_help_2));
                break;
        }
    }

    /**
     * 检查输入内容是否合法
     *
     * @return
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(payAmount)) {
            ToastUtil.showToast(R.string.toast_pay_amount_is_null);
        } else if (Integer.valueOf(payAmount) <= 0 || Integer.valueOf(payAmount) > 10000) {
            ToastUtil.showToast(R.string.toast_pay_amount_illegal);
        } else if (TextUtils.isEmpty(user_id)) {
            ToastUtil.showToast(R.string.toast_pay_paying_agent_is_null);
        } else {
            return true;
        }
        return false;
    }

    private void onRadioButonClick(View v) {
        currentRadioButtonId = v.getId();
        et_amount.setText("");
//        ((RadioButton)v).setChecked(true);
        String text = ((RadioButton) v).getText().toString();
        payAmount = text.substring(0, text.length() - 1);
    }


    /**
     * 发送 E109 请求，发送代付请求
     */
    private void connToServer() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "E109");
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("charge_pile_seri", charge_pile_seri);
            jsonObject.put("charge_pile_num", charge_pile_num);
            jsonObject.put("total_fee", payAmount);
            jsonObject.put("assist_voltage", "1");
            jsonObject.put("user_name", user.getCust_alias());
            jsonObject.put("spBillCreateIP", NetworkUtils.getPhoneIp());
            jsonObject.put("pay_agent_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(getActivity()).sendRequest(Config.REQUEST_URL, jsonObject, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                UserParams.INSTANCE.setOut_trade_no(map.get("out_trade_no").toString());
                EventBus.getDefault().post(new PaySuccessEvent());
            }
        });
    }

    /**
     * 发送 I106 请求，获取代付人列表
     */
    private void connToServerForPayingAgent() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "I106");
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("cur_page_no", "1");
            jsonObject.put("page_size", "500");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(getActivity()).sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
                if(map.get("data_list")!=null && !"".equals(map.get("data_list").toString())){
                    try {
                        JSONArray array = new JSONArray(map.get("data_list").toString());
                        int count = array.length();
                        if(count == 0){
                            tv_select_others.setText("您暂时没有代付人");
                            btn_submit.setEnabled(false);
                        } else {
                            btn_submit.setEnabled(true);
                            for (int i = 0; i < count; i++) {
                                JSONObject object = array.getJSONObject(i);
                                othersInfos.add(JsonUtils.jsonToMap(object.toString()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
