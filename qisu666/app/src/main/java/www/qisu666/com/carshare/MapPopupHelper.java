package www.qisu666.com.carshare;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.navi.model.NaviLatLng;

import www.qisu666.com.config.Config;
import www.qisu666.com.entity.LngLat;
import www.qisu666.com.util.CoodinateCovertor;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.MapUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareConfirmActivity;
import www.qisu666.com.activity.NaviActivity;
import www.qisu666.com.event.CarMapEvent;
import www.qisu666.com.util.UserParams;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 选择地图PopupWindow
 */

public class MapPopupHelper {

    private final Activity mContext;

    private PopupWindow mapPopupWindow;


    public static MapPopupHelper of(final Activity context) {
        return new MapPopupHelper(context);
    }

    private MapPopupHelper(Activity context) {
//        this.rootView = rootView;
        this.mContext = context;
        init();
    }

    private void init() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_choice_map, null);

        TextView tv_start_navi = (TextView) contentView.findViewById(R.id.tv_start_navi);
        TextView tv_amap = (TextView) contentView.findViewById(R.id.tv_amap);
        TextView tv_baidu = (TextView) contentView.findViewById(R.id.tv_baidu);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_start_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopViewClicked(v);
            }
        });
        tv_amap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopViewClicked(v);
            }
        });
        tv_baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopViewClicked(v);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPopupWindow.dismiss();
            }
        });

        mapPopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mapPopupWindow.setTouchable(true);
        mapPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mapPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.bg_white));
        mapPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    public void show() {
        mapPopupWindow.showAtLocation(mContext.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mapPopupWindow.dismiss();
    }

    private void onPopViewClicked(View view) {
        EventBus.getDefault().post(new CarMapEvent(view.getId()));
//        switch (view.getId()) {
//            case R.id.tv_amap:
//                break;
//            case R.id.tv_start_navi:
//                break;
//            case R.id.tv_baidu:
//                break;
//        }
    }


    /**
     * 打开高德地图
     */
    public void openAMapNavi(double markerLat, double markerLon) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        LatLng latLng = new LatLng(markerLat, markerLon);
        CoordinateConverter converter = new CoordinateConverter(mContext);
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// 转换
        converter.coord(latLng);
// 获取转换之后的高德坐标
        LatLng result = converter.convert();
        naviPara.setTargetPoint(result);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, mContext.getApplicationContext());
        } catch (AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(mContext.getApplicationContext());

        }
    }

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private LatLng pianyi(double lon, double lat) {
        double x = lon;
        double y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double temp = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);

        double bdLon = z * Math.cos(temp) + 0.0065;
        double bdLat = z * Math.sin(temp) + 0.006;
        LatLng newcenpt = new LatLng(bdLat, bdLon);
        return newcenpt;
    }

    public static double[] gcj02tobd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lng, bd_lat};
    }

    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    private LatLng toBd(double lat,double lon){
        LatLng sourceLatLng=new LatLng(lat,lon);
        CoordinateConverter converter = new CoordinateConverter(mContext);
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    /**
     * 打开百度地图
     */
    public void openBaiduMapNavi(double markerLat, double markerLon) {
        try {
            double lat1=markerLat + Config.OFF_LAT;
            double lng1= markerLon + Config.OFF_LNG;
            LatLng aa=toBd(lat1,lng1);
//            markerLat = markerLat + Config.OFF_LAT_BAIDU;
//            markerLon = markerLon + Config.OFF_LNG_BAIDU;
//            double[] dd=gcj02tobd09(markerLon,markerLat);
//            markerLat=dd[1];
//            markerLon=dd[0];
//            LatLng latLng=pianyi(markerLon,markerLat);
//            markerLat=latLng.latitude;
//            markerLon=latLng.longitude;
            LogUtil.e("markerLat:" + markerLat + "||markerLon:" + markerLon);
            Intent intent = Intent.parseUri("intent://map/direction?destination=" + MapUtils.bd_encrypt(lat1, lng1) + "&mode=driving&src=iDianNiu|iDianNiu#Intent;scheme=b" +
                    "dapp;package=com.baidu.BaiduMap;end", 0);
            if (isInstallByread("com.baidu.BaiduMap")) {
                mContext.startActivity(intent); //启动调用
                LogUtil.e("百度地图客户端已经安装");
            } else {
                ToastUtil.showToast(R.string.toast_app_uninstall_baidu);
                LogUtil.e("没有安装百度地图客户端");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
