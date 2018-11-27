package www.qisu666.com.cardid;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.MapUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.event.CarMapEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by admin on 2018/1/23.
 */

public class PhotoPopupHelper {

    private final Activity mContext;

    private PopupWindow photoPopupWindow;

    private OnPhotoPopListener onPhotoPopListener;

    public interface OnPhotoPopListener{
        void onTakePhoto();
        void onGetPhoto();
    }

    public void setOnPhotoPopListener(OnPhotoPopListener onPhotoPopListener){
        this.onPhotoPopListener = onPhotoPopListener;
    }


    public static PhotoPopupHelper of(final Activity context){
        return new PhotoPopupHelper( context);
    }

    private PhotoPopupHelper(Activity context) {
//        this.rootView = rootView;
        this.mContext = context;
        init();
    }
    private void init(){
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate( R.layout.popup_choice_photo, null);

        TextView tv_take_photo = (TextView) contentView.findViewById(R.id.tv_take_photo);
        TextView tv_take_from_gallery = (TextView) contentView.findViewById(R.id.tv_take_from_gallery);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPhotoPopListener != null){
                    onPhotoPopListener.onTakePhoto();
                }
            }
        });
        tv_take_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPhotoPopListener != null){
                    onPhotoPopListener.onGetPhoto();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPopupWindow.dismiss();
            }
        });

        photoPopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        photoPopupWindow.setTouchable(true);
        photoPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        photoPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.bg_white));
        photoPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    public void show(){
        if (photoPopupWindow != null) {
            photoPopupWindow.showAtLocation(mContext.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
        }
    }

    public void dismiss(){
        if (photoPopupWindow != null) {
            photoPopupWindow.dismiss();
        }
    }


}
