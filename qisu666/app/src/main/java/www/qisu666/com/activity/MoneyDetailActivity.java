package www.qisu666.com.activity;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.adapter.MoneyDetailFragmentAdapter;
import www.qisu666.com.fragment.MoneyDetailFragment;
import www.qisu666.com.fragment.MoneyDetailSecondFragment;
import www.qisu666.common.activity.BaseActivity;

/**
 * 717219917@qq.com 2018/8/8 9:34.
 */
public class MoneyDetailActivity extends BaseActivity implements View.OnClickListener{

    private MoneyDetailFragmentAdapter adapter;
    private List<Fragment> list;
    private MoneyDetailFragment useFragment;
    private MoneyDetailSecondFragment activiFragment;
    private ViewPager viewpager;
    private TextView tx_use;
    private TextView tx_activi;
    private TextView tv_title;
    private ImageView img_title_left;
    private View mLin;
    private int lineWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this,R.color.main_background);
        setContentView(R.layout.moneudetail_layout);
        initView();
        initData();
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

    private void initView(){
        mLin=findViewById(R.id.line);
        img_title_left=findViewById(R.id.img_title_left);
        tv_title=findViewById(R.id.tv_title);
        viewpager=findViewById(R.id.viewpager);
        tx_use=findViewById(R.id.tx_use);
        tx_activi=findViewById(R.id.tx_activi);

        img_title_left.setOnClickListener(this);
        tx_use.setOnClickListener(this);
        tx_activi.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            lineWidth = Objects.requireNonNull(this).getWindowManager().getDefaultDisplay().getWidth() / 2;
        }
        mLin.getLayoutParams().width = lineWidth;
        mLin.requestLayout();

        tv_title.setText("账户明细");
    }

    private void initData(){
        if(adapter==null){
            list=new ArrayList<>();
            useFragment=new MoneyDetailFragment();
            activiFragment=new MoneyDetailSecondFragment();
            list.add(useFragment);
            list.add(activiFragment) ;
            adapter=new MoneyDetailFragmentAdapter(getSupportFragmentManager(),list);
            viewpager.setAdapter(adapter);
            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int length = (int) (position * lineWidth + positionOffset * lineWidth);
                    mLin.animate().translationX(length).setDuration(0);
                }

                @Override
                public void onPageSelected(int position) {
                    if(position==0){
                        tx_use.setTextColor(getResources().getColor(R.color.new_primary));
                        tx_activi.setTextColor(getResources().getColor(R.color.new_text_gray));
                    }else{
                        tx_use.setTextColor(getResources().getColor(R.color.new_text_gray));
                        tx_activi.setTextColor(getResources().getColor(R.color.new_primary));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.tx_use:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tx_activi:
                viewpager.setCurrentItem(1);
                break;
        }
    }

}
