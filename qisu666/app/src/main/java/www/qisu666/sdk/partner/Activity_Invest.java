package www.qisu666.sdk.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import org.xutils.common.util.LogUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import www.qisu666.com.R;
import www.qisu666.com.activity.CommonWebViewActivity;
import www.qisu666.com.activity.SettingActivity;
import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.DataCleanManager;
import www.qisu666.sdk.amap.stationMap.StationLocation;
import www.qisu666.sdk.utils.Update;

/** 投资合伙人  第一次进入
 * 717219917@qq.com ${DATA} 17:50.
 */
public class Activity_Invest extends BaseActivity {



    @BindView(R.id.invest_shuoming_layout) LinearLayout invest_shuoming_layout;//需要隐藏的布局
    @BindView(R.id.cb_2)
    CheckBox checkBox2;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.invest_tiaokuan_txt)
    TextView invest_tiaokuan_txt;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;           //左边退出
    @BindView(R.id.invest_img_touzi)
    ImageView invest_img_touzi;       //投资
    @BindView(R.id.invest_img_xiaofei)
    ImageView invest_img_xiaofei;   //消费
    TextView invest_xiaofei;
    TextView invest_touzi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_invest);
        initView();
    }

    private void initView() {
        tv_title.setText("合伙人计划");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RxView.clicks(btn_login)  //前往认购按钮
                .throttleFirst(3, TimeUnit.SECONDS)//3秒内不允许多次点击
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (btn_login.isClickable()) {//是否可以点击状态
                            SPUtil.put(Activity_Invest.this, UserParams.INSTANCE.getUser_id(), "true");
                            startActivity(new Intent(Activity_Invest.this, Activity_Term.class));//跳转到我的认购页面
                            finish();
                        }
                    }
                });
        RxCompoundButton.checkedChanges(checkBox2)//检查点击状态
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        btn_login.setClickable(aBoolean);//状态
                        btn_login.setBackgroundResource(aBoolean ? R.color.can_login : R.color.not_login);//更新ui
                    }
                });

        String rengou = (String) SPUtil.get(this, UserParams.INSTANCE.getUser_id(), "false");
        if (rengou.equals("true")) {  //如果  没有同意条款 跳转条款页  other 投资认购
            boolean aBoolean = true;
            checkBox2.setVisibility(View.GONE);
            invest_shuoming_layout.setVisibility(View.GONE);
            btn_login.setClickable(aBoolean);//状态
            btn_login.setBackgroundResource(aBoolean ? R.color.can_login : R.color.not_login);//更新ui
//            cb_2
        } else {

        }
        invest_img_touzi = (ImageView) findViewById(R.id.invest_img_touzi);
        invest_img_xiaofei = (ImageView) findViewById(R.id.invest_img_xiaofei);
        invest_touzi = (TextView) findViewById(R.id.invest_touzi);
        invest_xiaofei = (TextView) findViewById(R.id.invest_xiaofei);

        invest_touzi.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick(View view) {
                  touzi();
            }
        });
        invest_xiaofei.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                xiaofei();
            }
        });

        invest_img_touzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e("点击投资");
                 touzi();
            }
        });
        invest_img_xiaofei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e("点击消费");
               xiaofei();
        }});
    }
        @OnClick(R.id.invest_tiaokuan_txt)
        void toWeb () {//跳转到条款web页
        Intent i = new Intent(this, CommonWebViewActivity.class);
        i.putExtra("act_title","投资计划说明");
        i.putExtra("act_url", "file:///android_asset/argu/argu.html");
//        i.putExtra("act_url", "file:///android_asset/user/user.html");
        startActivity(i);
        }

        private void touzi(){
            String str = "投资型：投资10000元，分60个月每月返还200元分红，投资到期后返还10000元本金，投资期间租车享受7折优惠（服务费用除外）。投资满12个月后，可申请合同解除。认购多份分红收益累加，认购投资型后将无法认购消费型。";
//            DialogHelper.confirmTitleDialog2(Activity_Invest.this,null, str, new AlertDialog.OnDialogButtonClickListener() {
//                @Override  public void onConfirm() {  }
//                @Override public void onCancel() {  }
//            });

            AlertDialog alertDialog = DialogHelper.alertDialog(this, "投资型说明", str);
//                alertDialog.getMessage_tv().setAlign(AlignTextView.Align.ALIGN_LEFT);
            alertDialog.getMessage_tv().setGravity(Gravity.LEFT);


        }

        private void xiaofei (){
            String str = "消费型：投资10000元，分60个月每月赠送当月消费额度500元，可用于用车费用抵扣。超过额度的用车费用享受7折优惠。投资满12个月后可申请合同解除。消费型认购最多可认购3份，赠送消费额度累加，认购消费型后将无法认购投资型。";
//                new AlertDialog(Activity_Invest.this,str,true);
//                str="这是测试 埃及的发昂书昂书阿斯顿发射点发大水";
//            DialogHelper.confirmTitleDialog2(Activity_Invest.this,null, str, new AlertDialog.OnDialogButtonClickListener() {
//                @Override  public void onConfirm() {  }
//                @Override public void onCancel() {  }
//            });
            AlertDialog alertDialog = DialogHelper.alertDialog(this, "消费型说明", str);
//                alertDialog.getMessage_tv().setAlign(AlignTextView.Align.ALIGN_LEFT);
            alertDialog.getMessage_tv().setGravity(Gravity.LEFT);

        }

        @Override protected void onResume () {
            super.onResume();
        }
        @Override protected void onDestroy () {
            super.onDestroy();
        }
    }
