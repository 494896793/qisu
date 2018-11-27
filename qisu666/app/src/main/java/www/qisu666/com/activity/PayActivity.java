package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CustomFragmentPagerAdapter;
import www.qisu666.com.event.FinishActivityEvent;
import www.qisu666.com.event.PayFailEvent;
import www.qisu666.com.event.PaySuccessEvent;
import www.qisu666.com.fragment.AccountPayFragment;
import www.qisu666.com.fragment.DirectPayFragment;
import www.qisu666.com.fragment.OthersPayFragment;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

//支付选择 账户支付与他人代付
public class PayActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
    private RadioButton radio_account;
    private RadioButton radio_direct;
    private RadioButton radio_others;
    private CustomViewPager viewPager;
    private CustomFragmentPagerAdapter adapter;

    @Subscribe
    public void onEventMainThread(PaySuccessEvent event) {
        ToastUtil.showToast(R.string.toast_pay_success);
        Intent i = new Intent(PayActivity.this, ConnectionActivity.class);
        startActivity(i);
        finish();
    }

    @Subscribe
    public void onEventMainThread(PayFailEvent event) {
        DialogHelper.alertDialog(this, getString(R.string.dialog_pay_failed));
    }

    @Subscribe
    public void onEventMainThread(FinishActivityEvent event) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pay);
        initView();
        setListeners();
        setAdapter();
        EventBus.getDefault().register(this);
//        registerReceiver(finishReceiver, new IntentFilter(ReceiverAction.ACTION_PAY_SUCCESS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finishReceiver);
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initTitleBar();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio_account = (RadioButton) findViewById(R.id.radio_account);
        radio_direct = (RadioButton) findViewById(R.id.radio_direct);
        radio_others = (RadioButton) findViewById(R.id.radio_others);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.pay_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        radio_account.setOnClickListener(this);
        radio_direct.setOnClickListener(this);
        radio_others.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("out position:" + position);
//                switch (position){
//                    case 0: radioGroup.check(R.id.radio_account); break;//                        LogUtils.d("in position:"+0);
//                    case 1: radioGroup.check(R.id.radio_direct); break;//                        LogUtils.d("in position:"+1);
//                    case 2: radioGroup.check(R.id.radio_others); break;//                        LogUtils.d("in position:"+2);
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_account:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radio_direct:
                viewPager.setCurrentItem(1);
                break;
            case R.id.radio_others:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        String charge_pile_seri = getIntent().getStringExtra("charge_pile_seri");
        String charge_pile_num = getIntent().getStringExtra("charge_pile_num");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(AccountPayFragment.newInstance(charge_pile_seri, charge_pile_num));
//        fragments.add(DirectPayFragment.newInstance(charge_pile_seri, charge_pile_num));
//        fragments.add(OthersPayFragment.newInstance(charge_pile_seri, charge_pile_num));
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

}
