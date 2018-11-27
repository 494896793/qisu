package www.qisu666.com.fragment.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import www.qisu666.com.R;
import www.qisu666.sdk.mytrip.Fragment_Base;
import www.qisu666.sdk.mytrip.Fragment_MyTrip_Already;
import www.qisu666.sdk.mytrip.Fragment_MyTrip_NO;
import www.qisu666.sdk.mytrip.Fragment_MyTrip_Underway;

/**
 * 717219917@qq.com 2018/7/5 17:55.
 */
public class Fragment_YongChe extends Fragment_Base {

    ImageView ivCompletedIcon;
    TextView tvCompleted;
    ImageView ivCompletedSanjiao;
    LinearLayout llCompleted;
    ImageView ivIncompleteIcon;
    TextView tvIncomplete;
    ImageView ivIncompleteSanjiao;
    LinearLayout llIncomplete;
    ImageView ivCancelIcon;
    TextView tvCancel;
    ImageView ivCancelSanjiao;
    LinearLayout llCancel;
    private ViewPager mvPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<LinearLayout> llViews;
    private ArrayList<TextView> textViews;
    private ArrayList<ImageView> ivSanjiao;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_yongche, container, false);

        mvPager = view.findViewById(R.id.vp_yongche);

//        if (mvPager != null) {
//            Log.e("asd", "vpYongche");
//        } else {
//            Log.e("asd", "null");
//            mvPager = view.findViewById(R.id.vp_yongche);
//        }

        ivCompletedIcon = view.findViewById(R.id.iv_completed_icon);
        tvCompleted = view.findViewById(R.id.tv_completed);
        ivCompletedSanjiao = view.findViewById(R.id.iv_completed_sanjiao);
        llCompleted = view.findViewById(R.id.ll_completed);
        ivIncompleteIcon = view.findViewById(R.id.iv_incomplete_icon);
        tvIncomplete = view.findViewById(R.id.tv_incomplete);
        ivIncompleteSanjiao = view.findViewById(R.id.iv_incomplete_sanjiao);
        llIncomplete = view.findViewById(R.id.ll_incomplete);
        ivCancelIcon = view.findViewById(R.id.iv_cancel_icon);
        tvCancel = view.findViewById(R.id.tv_cancel);
        ivCancelSanjiao = view.findViewById(R.id.iv_cancel_sanjiao);
        llCancel = view.findViewById(R.id.ll_cancel);

        fragments = new ArrayList<>();
        //已完成
        fragments.add(new Fragment_MyTrip_Already());
        // 进行中
        fragments.add(new Fragment_MyTrip_Underway());
        // 已取消
        fragments.add(new Fragment_MyTrip_NO());

        llViews = new ArrayList<>();
        llViews.add(llCompleted);
        llViews.add(llIncomplete);
        llViews.add(llCancel);

        textViews = new ArrayList<>();
        textViews.add(tvCompleted);
        textViews.add(tvIncomplete);
        textViews.add(tvCancel);

//        if (tvCompleted != null) {
//            Log.e("asd", "111111111");
//        } else {
//            Log.e("asd", "1111111111null");
//            mvPager = view.findViewById(R.id.vp_yongche);
//        }
//        if (tvIncomplete != null) {
//            Log.e("asd", "222222222222");
//        } else {
//            Log.e("asd", "222null");
//            mvPager = view.findViewById(R.id.vp_yongche);
//        }
//        if (tvCancel != null) {
//            Log.e("asd", "333333333");
//        } else {
//            Log.e("asd", "3333333null");
//            mvPager = view.findViewById(R.id.vp_yongche);
//        }

        ivSanjiao = new ArrayList<>();
        ivSanjiao.add(ivCompletedSanjiao);
        ivSanjiao.add(ivIncompleteSanjiao);
        ivSanjiao.add(ivCancelSanjiao);

        mvPager.setOffscreenPageLimit(6);

        mvPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int index) {
                return fragments.get(index);
            }
        });

        mvPager.setOffscreenPageLimit(6);

        mvPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
                switch (position) {
                    case 0:
                        ivCompletedIcon.setImageResource(R.mipmap.yiwanc2);
                        ivIncompleteIcon.setImageResource(R.mipmap.wwc1);
                        ivCancelIcon.setImageResource(R.mipmap.yqx1);
                        break;
                    case 1:
                        ivCompletedIcon.setImageResource(R.mipmap.yiwanc1);
                        ivIncompleteIcon.setImageResource(R.mipmap.wwc2);
                        ivCancelIcon.setImageResource(R.mipmap.yqx1);
                        break;
                    case 2:
                        ivCompletedIcon.setImageResource(R.mipmap.yiwanc1);
                        ivIncompleteIcon.setImageResource(R.mipmap.wwc1);
                        ivCancelIcon.setImageResource(R.mipmap.yqx2);
                        break;
                    default:
                        break;
                }
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
                    mvPager.setCurrentItem(temp);
                }
            });
        }

        return view;
    }

    private void changeState(int position) {
        int size = llViews.size();
        for (int curr = 0; curr < size; curr++) {
            if (curr == position) {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.new_primary));
                ivSanjiao.get(curr).setImageResource(R.mipmap.xiala2);

            } else {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.text_gray));
                ivSanjiao.get(curr).setImageResource(R.mipmap.xiala1);
            }
        }

    }
}
