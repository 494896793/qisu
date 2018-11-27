package www.qisu666.com.carshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareConfirmActivity;
import www.qisu666.com.activity.NaviActivity;
import www.qisu666.com.util.UserParams;

import java.io.File;

/**
 * Created by admin on 2018/1/16.
 */

public class PopupHelper {

    private TextView tv_map_address, tv_map_title, tv_charging_fee, tv_service_fee, tv_station_parking_fee, tv_map_distance, tv_free_fast, tv_free_slow, tv_elec_type;
    private ImageView iv_collection;

    private final Activity mContext;

    private View rootView;

    private View p_station_window;

    private PopupWindow pop;

    public static PopupHelper of(final Activity context){
        return new PopupHelper( context);
    }

    private PopupHelper(Activity context) {
//        this.rootView = rootView;
        this.mContext = context;
        init();
    }
    private void init(){
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate( R.layout.popup_station_info, null);

        View btn_map_charging = contentView.findViewById(R.id.btn_map_charging);
        tv_map_address = (TextView) contentView.findViewById(R.id.tv_map_address);
        tv_map_title = (TextView) contentView.findViewById(R.id.tv_map_title);
        tv_charging_fee = (TextView) contentView.findViewById(R.id.tv_charging_fee);
        tv_service_fee = (TextView) contentView.findViewById(R.id.tv_service_fee);
        tv_station_parking_fee = (TextView) contentView.findViewById(R.id.tv_station_parking_fee);
        tv_map_distance = (TextView) contentView.findViewById(R.id.tv_map_distance);
        tv_free_fast = (TextView) contentView.findViewById(R.id.tv_free_fast);
        tv_free_slow = (TextView) contentView.findViewById(R.id.tv_free_slow);
        tv_elec_type = (TextView) contentView.findViewById(R.id.tv_elec_type);
        iv_collection = (ImageView) contentView.findViewById(R.id.iv_collection);
        if (TextUtils.isEmpty(UserParams.INSTANCE.getUser_id())) {
            iv_collection.setVisibility(View.GONE);
        } else {
            iv_collection.setVisibility(View.VISIBLE);
        }

        tv_map_address.setText("定位中...");

        p_station_window = contentView.findViewById(R.id.p_station_window);
        p_station_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intoDetail();
//                pop.dismiss();
            }
        });

        btn_map_charging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopViewClicked(v);
            }
        });

        pop = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        pop.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.bg_white));
        pop.setAnimationStyle(R.style.Popup_Anim_Bottom);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                closeMarker(curMarker);
            }
        });
    }

    public void show(){
        pop.showAtLocation(mContext.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
    }

    private void onPopViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_amap:
//                mapPopupWindow.dismiss();
//                openAMapNavi();
//                break;
//            case R.id.tv_start_navi:
//                Intent intent = new Intent(this, NaviActivity.class);
//                intent.putExtra("current", new NaviLatLng(curLat, curLon));
//                intent.putExtra("target", new NaviLatLng(markerLat, markerLon));
//                mstartActivity(intent);
//                mapPopupWindow.dismiss();
//                break;
//            case R.id.tv_baidu:
//                mapPopupWindow.dismiss();
//                openBaiduMapNavi();
//                break;
//            case R.id.btn_map_charging:
////                pop.dismiss();
////                mapPopupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
//                Intent intent1 = new Intent(this, CarShareConfirmActivity.class);
//                startActivity(intent1);
//                break;
//            case R.id.iv_collection:
//                favorAction();
//                break;
//        }
    }

    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btnPickByTake:
//                if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//                    takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
//                }else {
//                    takePhoto.onPickFromCapture(imageUri);
//                }
//                break;
//            default:
//                break;
//        }
    }
}
