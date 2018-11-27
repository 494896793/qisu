package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.sdk.mytrip.Fragment_MyTrip_Already;
import www.qisu666.sdk.mytrip.Fragment_MyTrip_NO;
import www.qisu666.sdk.mytrip.Order.Fragment_HuoDong;
import www.qisu666.sdk.mytrip.Order.Fragment_TiXian;
import www.qisu666.sdk.mytrip.Order.Fragment_Yu_e;

/**
 * 明细页面
 */
public class BillActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;
    @BindView(R.id.tab_yu_e)
    TextView tabYuE;
    @BindView(R.id.tab_huodong)
    TextView tabHuodong;
    @BindView(R.id.tab_tixian)
    TextView tabTixian;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private ArrayList<TextView> textViews;
    private int line_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_bill);
        initView();

        ViewPropertyAnimator.animate(tabYuE).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<>();
        // 可用余额
        fragments.add(new Fragment_Yu_e());
        // 活动金
        fragments.add(new Fragment_HuoDong());
        //可提现金额
        fragments.add(new Fragment_TiXian());

        textViews = new ArrayList<>();
        textViews.add(tabYuE);
        textViews.add(tabHuodong);
        textViews.add(tabTixian);

        line_width = this.getWindowManager().getDefaultDisplay().getWidth() / 3;
        line.getLayoutParams().width = line_width;
        line.requestLayout();


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

        viewPager.setOffscreenPageLimit(6);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                changeState(index);
//                Log.e("asd", "点击了" + index);
            }

            @Override
            public void onPageScrolled(int index, float arg1, int offset) {
                int length = (int) (index * line_width + arg1 * line_width);
                line.animate().translationX(length).setDuration(0);
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
                    Log.e("asd", "fuckd" + temp);
                }
            });
        }
    }

    private void initView() {
        tvTitle.setText("账户明细");
        imgTitleLeft.setOnClickListener(new View.OnClickListener() {
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
                textViews.get(curr).setTextColor(getResources().getColor(R.color.new_primary));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            } else {
//                L.i("当前index:"+index+",-----------else");
                textViews.get(curr).setTextColor(getResources().getColor(R.color.text_gray));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            }
        }

    }

}
