package www.qisu666.com.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.R;


//扫码页面 两个选项 的适配器
public class OperationFlowPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] subtitles =  {R.string.operation_flow_subtitle_1, R.string.operation_flow_subtitle_2};
    private int[] contents =  {R.string.operation_flow_content_1, R.string.operation_flow_content_2};
    private int[] images =  {R.mipmap.img_operation_flow_1, R.mipmap.img_operation_flow_2};

    private OnArrowClickListener listener;

    public OperationFlowPagerAdapter(Context context, OnArrowClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LogUtil.e("viewpager init:"+position);
        View view = LayoutInflater.from(context).inflate(R.layout.item_vp_operation_flow, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        ImageView img_left_arrow = (ImageView) view.findViewById(R.id.img_left_arrow);
        ImageView img_right_arrow = (ImageView) view.findViewById(R.id.img_right_arrow);
        if(position==0) {
            img_left_arrow.setVisibility(View.GONE);
            img_right_arrow.setVisibility(View.VISIBLE);
        } else if (position==1){
            img_left_arrow.setVisibility(View.VISIBLE);
            img_right_arrow.setVisibility(View.GONE);
        }

        img_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) listener.onLeftArrowClick();
            }
        });
        img_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) listener.onRightArrowClick();
            }
        });
        tv_title.setText(subtitles[position]);
        tv_content.setText(contents[position]);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogUtil.e("viewpager destroy:"+position);
        container.removeView((View) object);
    }

    public interface OnArrowClickListener{
        void onLeftArrowClick();
        void onRightArrowClick();
    }

}
