package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.sdk.mytrip.Fragment_GeRen;
import www.qisu666.sdk.mytrip.Fragment_XiTong;

//消息中心(通知列表)
public class NotificationActivity extends BaseActivity {


    @BindView(R.id.tv_xitong)
    TextView tvXitong;
    @BindView(R.id.tv_geren)
    TextView tvGeren;
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
        setView(R.layout.activity_notification);
        initTitleBar();

        ViewPropertyAnimator.animate(tvXitong).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<>();
        // 系统消息
        fragments.add(new Fragment_XiTong());
        // 个人消息
        fragments.add(new Fragment_GeRen());

        textViews = new ArrayList<>();
        textViews.add(tvXitong);
        textViews.add(tvGeren);

        line_width = this.getWindowManager().getDefaultDisplay().getWidth() / 2;
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
                }
            });
        }

    }


    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.notification_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeState(int index) {
        int size = textViews.size();
        for (int curr = 0; curr < size; curr++) {
            if (curr == index) {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.new_primary));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            } else {
                textViews.get(curr).setTextColor(getResources().getColor(R.color.text_gray));
                textViews.get(curr).animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
            }
        }

    }


//    /** 消息查看状态改变时，收到消息 */
//    @Subscribe public void onEventMainThread(NotificationUpdateEvent event) {
//        int position = event.getPosition();
//        if(position!=-1){
//            list.get(position).put("state", "1");
//            adapter.notifyItemChanged(position);
//        }
//    }


}
