package www.qisu666.com.fragment.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import www.qisu666.com.R;
import www.qisu666.com.fragment.ChargingRecordFragment;
import www.qisu666.com.fragment.ChargingStatisticsFragment;
import www.qisu666.sdk.mytrip.Fragment_Base;
import www.qisu666.sdk.mytrip.bean.MyFragmentPagerAdapter;

/**
 * 717219917@qq.com 2018/7/5 17:55.
 * 充电页面
 */
public class Fragment_ChongDian extends Fragment_Base {

    private TextView tvJilu, tvTongji;
    private ImageView ivTongjiIcon, ivJiluIcon;
    private ImageView ivTongjiSanjiao, ivJiluSanjiao;
    private LinearLayout llTongji, llJilu;
    private ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private ArrayList<LinearLayout> llViews;
    private ArrayList<TextView> textViews;
    private ArrayList<ImageView> ivSanjiao;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_chongdian, container, false);

        llJilu = view.findViewById(R.id.ll_jilu);
        llTongji = view.findViewById(R.id.ll_tongji);

        tvJilu = view.findViewById(R.id.tv_jilu);
        tvTongji = view.findViewById(R.id.tv_tongji);

        ivJiluIcon = view.findViewById(R.id.iv_jilu_icon);
        ivTongjiIcon = view.findViewById(R.id.iv_tongji_icon);

        ivJiluSanjiao = view.findViewById(R.id.iv_jilu_sanjiao);
        ivTongjiSanjiao = view.findViewById(R.id.iv_tongji_sanjiao);

        viewPager = view.findViewById(R.id.viewPager_server);


        fragments = new ArrayList<>();
        // 充电记录
        fragments.add(new ChargingRecordFragment());
        // 充电统计
        fragments.add(new ChargingStatisticsFragment());

        llViews = new ArrayList<>();
        llViews.add(llJilu);
        llViews.add(llTongji);

        textViews = new ArrayList<>();
        textViews.add(tvJilu);
        textViews.add(tvTongji);

        ivSanjiao = new ArrayList<>();
        ivSanjiao.add(ivJiluSanjiao);
        ivSanjiao.add(ivTongjiSanjiao);


        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
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
                switch (index) {
                    case 0:
                        ivJiluIcon.setImageResource(R.mipmap.cdjl2);
                        ivTongjiIcon.setImageResource(R.mipmap.cdtj1);
                        break;
                    case 1:
                        ivTongjiIcon.setImageResource(R.mipmap.cdtj2);
                        ivJiluIcon.setImageResource(R.mipmap.cdjl1);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrolled(int index, float arg1, int offset) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        int size = llViews.size();
        for (int curr = 0; curr < size; curr++) {
            final int temp = curr;
            llViews.get(temp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(temp);
                }
            });
        }

        return view;
    }

    private void changeState(int index) {
        int size = llViews.size();
        for (int curr = 0; curr < size; curr++) {
            if (curr == index) {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.new_primary));
                ivSanjiao.get(curr).setImageResource(R.mipmap.xiala2);

            } else {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.text_gray));
                ivSanjiao.get(curr).setImageResource(R.mipmap.xiala1);
            }
        }
    }
}
