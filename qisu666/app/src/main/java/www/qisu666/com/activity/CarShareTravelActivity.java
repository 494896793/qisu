package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CustomFragmentPagerAdapter;
import www.qisu666.com.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//我的行程
public class CarShareTravelActivity extends BaseActivity {

    @BindView(R.id.tab_car_share) SlidingTabLayout tabCarShare;
    @BindView(R.id.view_pager_travel) CustomViewPager viewPagerTravel;
    @BindView(R.id.tv_title) TextView tvTitle;

    private CustomFragmentPagerAdapter adapter;
    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_travel);
        initViews();
        setAdapter();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        viewPagerTravel.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tvTitle.setText("我的行程");
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CarShareTravelUnfinishFragment());
        fragments.add(new CarShareTravelFinishFragment());
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPagerTravel.setAdapter(adapter);
        tabCarShare.setViewPager(viewPagerTravel, new String[]{"未完成", "已完成"});
    }

    @OnClick({R.id.img_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                onBackPressed(); break;
        }
    }
}
