package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.StatusBarUtils;
import www.qisu666.com.R;
import www.qisu666.com.adapter.GuidePagerAdapter;
import com.pers.medusa.circleindicator.widget.CircleIndicator;

//引导页面
public class GuideActivity extends BaseActivity {

    private ViewPager view_pager;
    private GuidePagerAdapter adapter;
//    private CircleIndicator circleIndicator;

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_guide, TYPE_NULL);

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new GuidePagerAdapter(this);
        view_pager.setAdapter(adapter);

//        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
//        circleIndicator.setViewPager(view_pager);

//        final ImageView img_ad = (ImageView) findViewById(R.id.img_ad);
//        img_ad.setImageResource(R.mipmap.img_ad);
////        img_ad.setVisibility(View.GONE);
////        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.advertisement_scale_anim);
//        AnimatorSet set = new AnimatorSet();
//        ObjectAnimator oaX = ObjectAnimator.ofFloat(img_ad, "scaleX", 1.0f, 1.1f);
//        ObjectAnimator oaY = ObjectAnimator.ofFloat(img_ad, "scaleY", 1.0f, 1.1f);
////        scaleAnimation.setFillAfter(true);
//        set.playTogether(oaX, oaY);
//        set.setDuration(1800);
//        final Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.advertisement_alpha_anim);
//        set.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                img_ad.startAnimation(alphaAnimation);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//
//        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                img_ad.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        set.start();

        StatusBarUtils.changeStatusBarDarkMode(getWindow());
    }


}
