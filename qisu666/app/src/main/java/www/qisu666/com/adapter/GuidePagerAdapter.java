package www.qisu666.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.xutils.common.util.LogUtil;

import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.R;
import www.qisu666.com.activity.MainActivity;

//操作指南适配器
public class GuidePagerAdapter extends PagerAdapter {
    private Context context;
    private int[] images = {R.mipmap.img_guide_1, R.mipmap.img_guide_2, R.mipmap.img_guide_3};

    public GuidePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        LogUtils.d("viewpager init:"+position);
        View view = LayoutInflater.from(context).inflate(R.layout.item_vp_guide, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Button button = (Button) view.findViewById(R.id.btn);
        imageView.setImageResource(images[position]);
        if (position == 2) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MainActivity.class));
                    ((Activity) context).finish();
                }
            });
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogUtil.e("viewpager destroy:" + position);
        container.removeView((View) object);
    }

}
