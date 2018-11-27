package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
/**
 * 广告页
 */
public class AdvertisementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_advertisement, TYPE_NULL);
        final ImageView img_ad = (ImageView) findViewById(R.id.img_ad);
        img_ad.setImageResource(R.mipmap.img_ad);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.advertisement_scale_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_ad.setVisibility(View.GONE);
                startActivity(new Intent(AdvertisementActivity.this, GuideActivity.class));
                finish();
                overridePendingTransition(0, R.anim.advertisement_alpha_anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_ad.startAnimation(animation);
    }
}
