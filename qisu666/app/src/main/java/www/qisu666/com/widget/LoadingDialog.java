package www.qisu666.com.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import www.qisu666.common.utils.DensityUtil;
import www.qisu666.com.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * Created by Coang on 2015/8/19.
 */
public class LoadingDialog extends Dialog {

    private Context context;

    private String type = "gif";

    private TextView tv_msg;

    private String message;

    public static final String TYPE_GIF = "gif";

    public static final String TYPE_ROTATE = "rotate";

    public LoadingDialog(Context context) {
        this(context, R.style.dialog);
    }

    public LoadingDialog(Context context, String type){
        this(context, type.equals("gif") ? R.style.dialog : R.style.dialog1);
        this.type = type;
    }

    public LoadingDialog(Context context, String type, String message){
        this(context, type.equals("gif") ? R.style.dialog : R.style.dialog1);
        this.type = type;
        this.message = message;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(type.equals("gif")){
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
            GifImageView gifImageView = (GifImageView) layout.findViewById(R.id.gifImageView);
            gifImageView.setImageResource(R.mipmap.anim_refresh);
            GifDrawable drawable = (GifDrawable) gifImageView.getDrawable();
            drawable.start();
            this.setContentView(layout, new RelativeLayout.LayoutParams(DensityUtil.getScreenWidth(context), DensityUtil.getScreenHeight(context)));
        }else{
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_loading_rotate, null);
            ImageView iv_loading = (ImageView) layout.findViewById(R.id.iv_loading);
            iv_loading.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation));
            tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
            if(!TextUtils.isEmpty(message)){
                tv_msg.setText(message);
            }
            this.setContentView(layout, new RelativeLayout.LayoutParams(DensityUtil.getScreenWidth(context), DensityUtil.getScreenHeight(context)));
        }
    }
}
