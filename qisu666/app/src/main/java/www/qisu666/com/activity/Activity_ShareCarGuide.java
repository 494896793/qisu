package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.InputStreamBitmapDecoderFactory;

import www.qisu666.com.R;
import www.qisu666.common.activity.BaseActivity;

/**
 * 用车指南
 * 717219917@qq.com ${DATA} 11:23.
 */
public class Activity_ShareCarGuide extends BaseActivity{
    private TextView tv_title;
    private ImageView img_title_left;
    //加载大图
    private LargeImageView largeImageView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_sharecarguide);
        iniView();
    }

    private void iniView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        //租车指南
        tv_title.setText(R.string.car_share_guide);
        img_title_left = (ImageView) findViewById(R.id.img_title_left);
        largeImageView = (LargeImageView) findViewById(R.id.sharecarguide_img);
        largeImageView.setEnabled(true);

        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                  finish();
            }
        });


        try {
            //通过流的方式加载assets文件夹里面的大图
            largeImageView.setImage(new InputStreamBitmapDecoderFactory(getAssets().open("zuche")));
        } catch (Exception e) { e.printStackTrace();  }
//        Picasso.with(Activity_ShareCarGuide.this).load(R.mipmap.zuchezhinan).into(sharecarguide_img);
    }


}


