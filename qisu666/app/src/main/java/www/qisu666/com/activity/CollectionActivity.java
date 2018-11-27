package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.navi.model.NaviLatLng;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.MapUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.CollectionAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PageLoadResponseCallBack;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//我的收藏
public class CollectionActivity extends BaseActivity implements OnLoadRefreshCallBack ,AMapLocationListener, View.OnClickListener, CollectionAdapter.OnItemClick {

    private List<Map<String,Object>> list;
    private CollectionAdapter adapter;
    private PullRefreshLoadRecyclerView pull_refresh_load_recycler_view;
    private LoadMoreView loadMoreView;
    private int currentPage = 1;//当前数据分页
    private static final int PAGE_NUM = 10;//每页数据条数

    private String locationLng, locationLat;
    private String clickLng, clickLat;
    private PopupWindow mapPopupWindow;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private LoadingDialog loadingDialog;

    /*private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(currentPage==1){
                adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
                list.clear();
            }

            for(int i=0;i<10;i++){
                Map<String, Object> map = new HashMap<>();
                map.put("nearby_title", "海心沙广场");
                map.put("nearby_fee", "2.4");
                map.put("nearby_addr", "广东省广州市天河区临江大道");
                map.put("nearby_distance", "1.6");
                map.put("nearby_free_fast", "3");
                map.put("nearby_free_slow", "5");
                map.put("nearby_type", "直流/交流");
                list.add(map);
            }

            adapter.notifyItemRangeInserted((currentPage - 1) * PAGE_NUM, list.size() - (currentPage - 1) * PAGE_NUM);

            if(currentPage==1){
                pull_refresh_load_recycler_view.getRefreshView().setState(RefreshView.STATE_NORMAL);
                pull_refresh_load_recycler_view.setLoadMoreView(new DefaultLoadMoreView(CollectionActivity.this));
            } else {
                if(pull_refresh_load_recycler_view.getLoadMoreView()!=null){
                    pull_refresh_load_recycler_view.getLoadMoreView().setState(LoadMoreView.STATE_NORMAL);
                }
            }
        }
    };*/

    @Subscribe public void onEventMainThread(LoginEvent event) {
        startLocation();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_collection);
        initViews();
        setAdapter();
        startLocation();
        EventBus.getDefault().register(this);
    }

    private void startLocation() {
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this); //设置定位监听
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置为高精度定位模式
        locationOption.setOnceLocation(true);//设置是否单次定位
        locationClient.setLocationOption(locationOption);//设置定位参数
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        locationClient.startLocation();
        loadingDialog = new LoadingDialog(this, LoadingDialog.TYPE_GIF);
        loadingDialog.show();
    }

    private void connToServer(boolean flag) {
//        handler.sendEmptyMessageDelayed(0, 2000);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "D105");
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("longitude", locationLng);
            jsonObject.put("latitude", locationLat);
            jsonObject.put("page_index", currentPage);
            jsonObject.put("page_num", PAGE_NUM);
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//            LogUtils.d("距离："+AMapUtils.calculateLineDistance(new LatLng(locationLat, locationLng), new LatLng(locationLat-0.1,locationLng-0.1)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, flag, new PageLoadResponseCallBack(currentPage, PAGE_NUM, pull_refresh_load_recycler_view, loadMoreView, adapter, list, "record_list"));
    }

    private void setAdapter() {
        list = new ArrayList<Map<String, Object>>();
        adapter = new CollectionAdapter(this, this, this, list);
        adapter.setOnNaviClickListener(new CollectionAdapter.OnNaviClickListener() {
            @Override
            public void onNaviClick(int position) {
                clickLng = list.get(position).get("longitude").toString();
                clickLat = list.get(position).get("latitude").toString();
                mapPopupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
            }
        });
        pull_refresh_load_recycler_view.setAdapter(adapter);
    }

    private void initViews() {
        pull_refresh_load_recycler_view = (PullRefreshLoadRecyclerView) findViewById(R.id.pull_refresh_load_recycler_view);
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        loadMoreView = new DefaultLoadMoreView(this);
        pull_refresh_load_recycler_view.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        initMapPopupWindow();
        initTitleBar();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("我的收藏");
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        connToServer(false);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        connToServer(false);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(loadingDialog!=null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            LogUtils.d("当前位置："+amapLocation.getLatitude()+","+amapLocation.getLongitude()+","+amapLocation.getAddress());
            locationLat = String.valueOf(amapLocation.getLatitude());
            locationLng = String.valueOf(amapLocation.getLongitude());
            connToServer(true);
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            LogUtils.e("AmapErr:"+errText);

            if(NetworkUtils.isConnected(this)){
                if(amapLocation.getErrorCode() == 12){
                    ToastUtil.showToast(R.string.toast_permission_location);
                }else{
                    ToastUtil.showToast(getString(R.string.toast_map_location_failed) + amapLocation.getErrorInfo());
                }
            }
        }
    }

    /**
     * 选择地图PopupWindow
     */
    private void initMapPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_choice_map, null);

        TextView tv_start_navi = (TextView) contentView.findViewById(R.id.tv_start_navi);
        TextView tv_amap = (TextView) contentView.findViewById(R.id.tv_amap);
        TextView tv_baidu = (TextView) contentView.findViewById(R.id.tv_baidu);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_start_navi.setOnClickListener(this);
        tv_amap.setOnClickListener(this);
        tv_baidu.setOnClickListener(this);
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
        mapPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.bg_white));
        mapPopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_amap:
                mapPopupWindow.dismiss();
                openAMapNavi();
                break;
            case R.id.tv_start_navi:
                Intent intent = new Intent(this, NaviActivity.class);

                intent.putExtra("current_lat",locationLat+"");
                intent.putExtra("current_lon",locationLng+"");
                intent.putExtra("target_lat",clickLat+"");
                intent.putExtra("target_lon",clickLng+"");

//                intent.putExtra("current", new NaviLatLng(Double.valueOf(locationLat), Double.valueOf(locationLng)));
//                intent.putExtra("target", new NaviLatLng(Double.valueOf(clickLat), Double.valueOf(clickLng)));
                startActivity(intent);
                mapPopupWindow.dismiss();
                break;
            case R.id.tv_baidu:
                mapPopupWindow.dismiss();
                openBaiduMapNavi();
                break;
        }
    }

    public void openAMapNavi(){
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        LatLng latLng = new LatLng(Double.valueOf(clickLat), Double.valueOf(clickLng));
        naviPara.setTargetPoint(latLng);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }
    }

    /**
     * 打开百度地图
     */
    public void openBaiduMapNavi(){
        try {
            Intent intent = Intent.parseUri("intent://map/direction?destination=" + MapUtils.bd_encrypt(Double.valueOf(clickLat), Double.valueOf(clickLng)) + "&mode=driving&src=iDianNiu|iDianNiu#Intent;scheme=b" +
                    "dapp;package=com.baidu.BaiduMap;end",0);
            if(isInstallByread("com.baidu.BaiduMap")){
                startActivity(intent); //启动调用
                LogUtils.e("百度地图客户端已经安装") ;
            }else{
                ToastUtil.showToast(R.string.toast_app_uninstall_baidu);
                LogUtils.e("没有安装百度地图客户端") ;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, StationInfoActivity.class);
        i.putExtra("station_id", list.get(position).get("station_id").toString());
        i.putExtra("station_name", list.get(position).get("station_name").toString());
        startActivityForResult(i, ConstantCode.REQ_OPEN_STATION_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_OPEN_STATION_INFO && resultCode==ConstantCode.RES_OPEN_STATION_INFO){
            onRefresh();
        }
    }

}
