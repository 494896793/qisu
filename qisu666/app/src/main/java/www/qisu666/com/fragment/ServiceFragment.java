package www.qisu666.com.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import www.qisu666.com.fragment.order.Fragment_ChongDian;
import www.qisu666.com.fragment.order.Fragment_RenGou;
import www.qisu666.com.fragment.order.Fragment_YongChe;
import www.qisu666.com.fragment.order.NonSwipeableViewPager;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.DensityUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.activity.BuildPileActivity;
import www.qisu666.com.activity.ShareChargingTypeActivity;
import www.qisu666.com.activity.StationInfoActivity;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.PermissionUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.sdk.mytrip.Order.Fragment_HuoDong;
import www.qisu666.sdk.mytrip.Order.Fragment_TiXian;
import www.qisu666.sdk.mytrip.Order.Fragment_Yu_e;
import www.qisu666.sdk.mytrip.bean.MyFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment {

    private RelativeLayout mLin;
    private NonSwipeableViewPager mvPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<TextView> textViews;
    private int lineWidth;
    private View viewlines;

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return initView(inflater, container);
    }

    @NonNull
    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = getImmersionView(inflater.inflate(R.layout.fragment_service, container, false));

        View viewlines=view.findViewById(R.id.viewlines);
        TextView tvChongdian = view.findViewById(R.id.tab_chongdian);
        TextView tvYongche = view.findViewById(R.id.tab_yongche);
        TextView tvRengou = view.findViewById(R.id.tab_rengou);

        mLin = view.findViewById(R.id.line);
        mvPager = view.findViewById(R.id.viewPager);

        fragments = new ArrayList<>();
        // 充电订单
        fragments.add(new Fragment_ChongDian());
        // 用车订单
        fragments.add(new Fragment_YongChe());
        //认购订单
        fragments.add(new Fragment_RenGou());

        textViews = new ArrayList<>();
        textViews.add(tvChongdian);
        textViews.add(tvYongche);
        textViews.add(tvRengou);

        viewlines.setBackgroundColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            lineWidth = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getWidth() / 3;
        }
        mLin.getLayoutParams().width = lineWidth;
        mLin.requestLayout();

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
            public void onPageSelected(int index) {
                changeState(index);
            }

            @Override
            public void onPageScrolled(int index, float arg1, int offset) {
                int length = (int) (index * lineWidth + arg1 * lineWidth);
                mLin.animate().translationX(length).setDuration(0);
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

    @SuppressLint("NewApi")
    private void changeState(int index) {
//        L.i("当前index:"+index);
        int size = textViews.size();
        for (int curr = 0; curr < size; curr++) {
            if (curr == index) {
//                L.i("当前index:"+index+"---------------color");
                textViews.get(curr).setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.text_white));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            } else {
//                L.i("当前index:"+index+",-----------else");
                textViews.get(curr).setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.text_gray));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            }
        }

    }

    public static ServiceFragment newInstance(String content) {
        Bundle args = new Bundle();
        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
