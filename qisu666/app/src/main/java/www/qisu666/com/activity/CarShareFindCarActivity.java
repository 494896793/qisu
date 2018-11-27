package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CustomFragmentPagerAdapter;
import www.qisu666.com.fragment.CarShareNearCarFragment;
import www.qisu666.com.fragment.CarShareNearStationFragment;
import www.qisu666.com.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//列表找车
public class CarShareFindCarActivity extends BaseActivity {

    @BindView(R.id.img_title_left) ImageView img_title_left;
    @BindView(R.id.view_pager) CustomViewPager view_pager;
    @BindView(R.id.tab_car_share) SlidingTabLayout tabCarShare;
    @BindView(R.id.tv_title) TextView tvTitle;

    private CustomFragmentPagerAdapter adapter;

    private String[] mTitles = {"网点列表", "车辆信息"};

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_find_car);
        initViews();
        setAdapter();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        view_pager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tvTitle.setText("列表找车");
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CarShareNearStationFragment());
        fragments.add(new CarShareNearCarFragment());
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        tabCarShare.setViewPager(view_pager, mTitles);
    }


    @OnClick({R.id.bg_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bg_view:
                onBackPressed();
                break;
        }
    }
}
