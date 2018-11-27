package www.qisu666.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;

/**
 * 717219917@qq.com 2018/8/13 16:29.
 */
public class BePartnerActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.agree_linear)
    LinearLayout agree_linear;
    @BindView(R.id.agree_check)
    CheckBox agree_check;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.view_tz)
    View view_tz;
    @BindView(R.id.view_hh)
    View view_hh;
    @BindView(R.id.read_tx)
    TextView read_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.main_background);
        setContentView(R.layout.activity_bepartner_layout);
        ButterKnife.bind(this);
        tv_title.setText("合伙人计划");
        agree_check.setChecked(true);
        agree_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    submit.setEnabled(true);
                    submit.getBackground().setAlpha(255);
                }else{
                    submit.setEnabled(false);
                    submit.getBackground().setAlpha(100);
                }
            }
        });
        SharedPreferences sharedPreferences=getSharedPreferences("read_agree",MODE_PRIVATE);
        if(sharedPreferences!=null&&sharedPreferences.getString("agree","").equals("1")&&sharedPreferences.getString("userId","").equals(UserParams.INSTANCE.getUser_id())){
            agree_linear.setVisibility(View.GONE);
        }

        read_tx.setText(Html.fromHtml("<font color='#ffffff'>我已阅读并同意</font><font color='#51E7D3'>《奇速合伙人协议》</font>"));
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_hh.setOnClickListener(this);
        view_tz.setOnClickListener(this);
        submit.setOnClickListener(this);
        read_tx.setOnClickListener(this);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                    SharedPreferences sharedPreferences=getSharedPreferences("read_agree",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("agree","1");
                    editor.putString("userId",UserParams.INSTANCE.getUser_id());
                    editor.commit();
                    Intent inverstIntent=new Intent(this,InverstActivity.class);
                    startActivity(inverstIntent);
                break;
            case R.id.view_hh:
                xiaofei();
                break;
            case R.id.view_tz:
                touzi();
                break;
            case R.id.read_tx:
                Intent i = new Intent(this, CommonWebViewActivity.class);
                i.putExtra("act_title","投资计划说明");
                i.putExtra("act_url", "file:///android_asset/argu/argu.html");
//        i.putExtra("act_url", "file:///android_asset/user/user.html");
                startActivity(i);
                break;
        }
    }
}
