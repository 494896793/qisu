package www.qisu666.sdk.mytrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.nineoldandroids.view.ViewPropertyAnimator;


import java.util.ArrayList;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;

/**
 * 717219917@qq.com  2016/12/14 0:26
 *///我的行程的外层viewpager
public class Activity_MyTrip extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    //    @ViewInject(R.id.tab_game)   TextView tab_game;
    @BindView(R.id.tab_app)
    TextView tab_app;
    @BindView(R.id.tab_ceshi)
    TextView tab_ceshi;
    @BindView(R.id.line)
    View line;
    private ArrayList<Fragment> fragments;
    private ArrayList<TextView> textViews;
    private int line_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_mytrip);

        initView();
        ViewPropertyAnimator.animate(tab_app).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<>();
        // 已完成
        fragments.add(new Fragment_MyTrip_Already());
        fragments.add(new Fragment_MyTrip_NO());//未完成

        textViews = new ArrayList<>();
        textViews.add(tab_app);
//        textViews.add(tab_game);
        textViews.add(tab_ceshi);

        line_width = getWindowManager().getDefaultDisplay().getWidth() / fragments.size();
        line.getLayoutParams().width = line_width;
        line.requestLayout();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int index) {
                return fragments.get(index);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                changeState(index);

            }

            @Override
            public void onPageScrolled(int index, float arg1, int offset) {
                float tagerX = index * line_width + offset / fragments.size();
                ViewPropertyAnimator.animate(line).translationX(tagerX).setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        int size = textViews.size();
        for (int curr = 0; curr < size; curr++) {
            final int temp = curr;
            textViews.get(temp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(temp);

                }
            });
        }

    }

    private void initView() {
        tv_title.setText("我的行程");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void changeState(int index) {
//        L.i("当前index:"+index);
        int size = textViews.size();
        for (int curr = 0; curr < size; curr++) {
            if (curr == index) {
//                L.i("当前index:"+index+"---------------color");
                textViews.get(curr).setTextColor(getResources().getColor(R.color.primary));
                ViewPropertyAnimator.animate(textViews.get(curr)).scaleX(1.0f).scaleY(1.0f).setDuration(200);
            } else {
//                L.i("当前index:"+index+",-----------else");
                textViews.get(curr).setTextColor(getResources().getColor(R.color.primary));
                ViewPropertyAnimator.animate(textViews.get(curr)).scaleX(1.0f).scaleY(1.0f).setDuration(200);
            }
        }

    }

}

