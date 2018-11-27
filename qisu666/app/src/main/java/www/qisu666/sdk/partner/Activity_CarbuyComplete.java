package www.qisu666.sdk.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;

/** 认购成功
 *  717219917@qq.com ${DATA} 10:14.
 */
public class Activity_CarbuyComplete extends BaseActivity{

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.compile_txt)TextView compile_txt;
    @BindView(R.id.img_title_left)ImageView img_title_left;
    @BindView(R.id.tv_car_complete_sure)TextView tv_car_complete_sure;//确认按钮

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_carbuycomplete);
        tv_title.setText("认购成功");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }});
      String  str = "您已成功认购"+getIntent().getStringExtra("carbuy_model")+"汽车认购,认购合同已生成。认购合同及分红日程可在“个人中心-我的认购“处查看。”";
        compile_txt.setText(str);
//        您已成功认购xxx型汽车认购,认购合同已生成。认购合同及分红日程可在“个人中心-我的认购“处查看。”

    }


    @OnClick({R.id.tv_car_complete_sure})
    public void onViewClicked(View view){
          switch(view.getId()){
              case  R.id.tv_car_complete_sure:  finish();break;  // startActivity(new Intent(Activity_CarbuyComplete.this,Activity_CarBuyDetail.class)); break;
          }

    }

    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
