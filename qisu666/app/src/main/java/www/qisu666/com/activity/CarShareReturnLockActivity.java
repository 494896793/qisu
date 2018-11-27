package www.qisu666.com.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import www.qisu666.com.config.Config;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.DialogUtils;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.model.PositionBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.PrefUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.carShare.adapter.Adapter_car_share_returnLock;

/**
 * 确认锁门还车Activity  还车网点列表
 */
public class CarShareReturnLockActivity extends BaseActivity implements AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.tv_title) TextView tvTitle;//标题
    @BindView(R.id.img_title_left)ImageView img_title_left;//左边返回
    @BindView(R.id.carmap_list_loadmore) LinearLayout carmap_list_loadmore;//点击加载更多的布局
    @BindView(R.id.btn_submit) TextView btn_submit;//确认还车按钮   屏蔽

//    @BindView(R.id.tv_return_address) TextView tvReturnAddress;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private String endStation;
    private String endLocationTxt;
    private String endLocationTxt_tmp;


    //新增接口以及信息
    String distance ="30";  //接口获取到的距离  默认30
    String lat="",lon=""; //接口获取到的经纬度
    @BindView(R.id.listView_carshare_returnlock)  ListView listView_carshare_returnlock;//网点展示的list
    Adapter_car_share_returnLock adapter_car_share_returnLock;//网点展示的适配器
    List<PositionBean> mylist;//当前网点信息的 bean 列表


    protected LoadingDialog mLoadingDialog;
     boolean onClicked =false;//是否选中还车点


    String   adcode="";  //根据车的经纬度获取 区域
    String   adadress="";//车的详细地址
    GeocodeSearch geocodeSearch;//反地理编码

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = DialogHelper.loadingAletDialog(mContext, "正在加载中...");
        setView(R.layout.activity_car_share_return_lock);
//        img_title_left=(ImageView)findViewById(R.id.img_title_left);//左边返回
//        tvTitle =(ImageView)findViewById(R.id.img_title_left);
        initAmap();
        initView();
    }

    private void initAmap() {
        locationClient = new AMapLocationClient(getApplicationContext());//初始化定位
        locationClient.setLocationListener(this); //设置定位回调监听
        locationOption = new AMapLocationClientOption(); //初始化AMapLocationClientOption对象
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置定位模式为高精度模式
        locationOption.setOnceLocation(true); //获取一次定位结果： 该方法默认为false。
        locationClient.setLocationOption(locationOption);//给定位客户端对象设置定位参数

        //地理搜索类
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    private void initView() {
        tvTitle.setText("还车网点");
        btn_submit.setVisibility(View.GONE);//
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                  finish();
            }
        });
        if (null != locationClient) {
            locationClient.startLocation(); //启动定位
        }

        adapter_car_share_returnLock=new Adapter_car_share_returnLock(this,null);
        listView_carshare_returnlock.setAdapter(adapter_car_share_returnLock);//绑定适配器

        adapter_car_share_returnLock.setOnItemClick(new Adapter_car_share_returnLock.OnItemClick() {
            @Override  public void onItemClick(int position, View parent) {
                LogUtil.e("进入点击事件：");
                onClicked=true;
                adapter_car_share_returnLock.setSelect(position);
                try {//最终还车时候的网点和网点描述
                    endStation = mylist.get(position).stationId;
                    endLocationTxt = mylist.get(position).stationName;
                }catch (Throwable t){t.printStackTrace();}
                try {
                    double latt = Double.parseDouble(mylist.get(position).lat) -  Config.OFF_LAT;//lat
                    double lngg = Double.parseDouble(mylist.get(position).lng) - Config.OFF_LNG;//lng
//                    outOfDistance((String) SPUtil.get(mContext, "carCode", ""), latt + "", lngg + "");//点击之后 判断距离
                    outOfDistance((String) SPUtil.get(mContext, "carCode", ""), mylist.get(position).lat + "", mylist.get(position).lng + "");

                }catch (Throwable t){t.printStackTrace();}
            }
        });


//        listView_carshare_returnlock.setOnItemClickListener(new AdapterView.OnItemClickListener() {//这个是系统api
//            @Override public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
//                LogUtil.e("当前点击的item：" + p);
//                onClicked=true;
//                /** 设置点击效果背景  */
//
//                adapter_car_share_returnLock.setSelect(p);
//
//                for (int i = 0; i < adapterView.getCount(); i++) {
//                    try {
//                        /**  因为parent这里是listview，所以parent.getChildAt(i)就是一个一个的item */
//                        View item = adapterView.getChildAt(i);
//                        ImageView imageView = (ImageView) item.findViewById(R.id.car_share_returnlock_img);
//                        /** 找到item里的每一个元素再进行相关操作 */
////                    LinearLayout car_share_returnlock_layout = (LinearLayout)(item.findViewById(R.id.car_share_returnlock_layout));
//                        if (p == i) {
//                            imageView.setImageResource(R.mipmap.return_maplist_item_1);
////                    item.setBackgroundResource(R.color.bg_navi_topic);
//                        } else {
//                            imageView.setImageResource(R.mipmap.return_maplist_item_0);
////                        item.setBackgroundResource(R.color.bg_primary);
//                        }
//                    }catch (Throwable t){t.printStackTrace(); }
//                }
//               try {//最终还车时候的网点和网点描述
//                   endStation = mylist.get(p).stationId;
//                   endLocationTxt = mylist.get(p).stationName;
//               }catch (Throwable t){t.printStackTrace();}
//                try {
//                    double latt = Double.parseDouble(mylist.get(p).lat) -  Config.OFF_LAT;//lat
//                    double lngg = Double.parseDouble(mylist.get(p).lng) - Config.OFF_LNG;//lng
////                    outOfDistance((String) SPUtil.get(mContext, "carCode", ""), latt + "", lngg + "");//点击之后 判断距离
//                    outOfDistance((String) SPUtil.get(mContext, "carCode", ""), mylist.get(p).lat + "", mylist.get(p).lng + "");
//
//                }catch (Throwable t){t.printStackTrace();}
//            }
//        });

        getDistance();//初始化拿到附近的距离
    }

    @OnClick({R.id.img_title_left, R.id.btn_submit,R.id.carmap_list_loadmore,R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                LogUtil.e("点击返回按钮");
//                finish();break;
                onBackPressed(); break;
            case R.id.img_title_left:
                LogUtil.e("点击返回按钮");
//                finish();break;//
             onBackPressed(); break;
            case R.id.btn_submit:
//                ActivityUtil.startActivity(mContext, CarShareCompleteActivity.class);
//                requestBackCar(); break; //这里要去掉注释
//                  getDistance();break;   //测试获取距离
                if (onClicked){
                    requestBackCar(isfive); break;
                }else{
                    ToastUtil.showToast("请选择还车网点！");
                    getLatLot();break;    //测试  获取汽车经纬度
                }
            case R.id.carmap_list_loadmore ://无网络 点击加载更多
                getDistance();//重新走流程
                break;


        }
    }



    @Override public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
//            requestStation(loc.getLongitude() + "", loc.getLatitude() + "");
        }
    }



    private void testGeo(){
          String testLng = "114.1144306149";
          String testLat = "22.5640882213";


    }



     /**1.获取当前距离--测试ok正常拿到返回数据*/
    private void getDistance(){
        if(!distance.equals("")){getLatLot();}//进行第二步骤
        String url = "api/dic/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("dicKey", "KEY_OF_NEARSTATION_RANGE");//nearStationRange

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))//可以去掉object
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override  public void onSuccess(Object bean) {
                        try {
                            LogUtil.e("距离  请求成功:" + bean.toString());
                            String str = bean.toString().substring(bean.toString().indexOf("dicValue"),bean.toString().indexOf("dicValue")+20);
                            LogUtil.e("距离  请求成功2:" + str);
                            String str_tmp = str.substring(str.indexOf("=")+1,str.indexOf(","));//不包含
                            LogUtil.e("距离  请求成功3:" + str_tmp);
                            distance =str_tmp;
                            LogUtil.e("距离  请求成功:" + distance);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            distance="";
                        }
                        //第一个接口请求成功之后
                        LogUtil.e("获取到的距离 "+distance);
                        getLatLot();//获取经纬度

                    }
                    @Override public void onFail(Message<Object> bean) {
                        LogUtil.e("距离  请求失败3:" + bean.msg);
//                            ToastUtil.showToast("距离 请求失败:"+bean.msg);
                    }  });

    }

    /**2.获取车辆经纬度*/
    private void getLatLot(){
        String url = "api/car/info/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("carCode", SPUtil.get(mContext,"carCode",""));//动态修改carCode
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))//可以去掉object
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override  public void onSuccess(Object bean) {
                        try {
                            LogUtil.e("经纬度  请求成功:" + bean.toString());//longitude=114.044952, latitude=22.601326,
                            String lat_tmp = bean.toString().substring(bean.toString().indexOf("longitude")+10,bean.toString().indexOf("longitude")+25);
                            lon=lat_tmp.substring(0,lat_tmp.indexOf(","));
                            LogUtil.e("lon  请求成功:" + lon);
                            String lon_tmp = bean.toString().substring(bean.toString().indexOf("latitude")+9,bean.toString().indexOf("latitude")+25);
                            lat=lon_tmp.substring(0,lon_tmp.indexOf(","));
                            LogUtil.e("经纬度lat  请求成功:" + lat);
//                            requestStation(lat+"",lon+"");
                            requestStation(lon+"",lat+"");
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        //第一个接口请求成功之后  进行逆地理编码
                        String lattt="113.751043";
                        String lonnn="23.053412";
                        LogUtil.e("经纬度 par："+Double.parseDouble(lattt));
//                        getAddressByLatlng( Double.parseDouble(lattt),Double.parseDouble(lonnn));//测试用
                          getAddressByLatlng( Double.parseDouble(lat),Double.parseDouble(lon) );   //正式传后台
                    }
                    @Override public void onFail(Message<Object> bean) {
                        ToastUtil.showToast("经纬度 请求失败:"+bean.msg);
                    }  });
    }


    private void getAddressByLatlng(double longitude,double latitude) {
        LogUtil.e("经纬度  请求 反地理编码 lat:" +latitude+",lon:"+longitude );
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latitude+Config.OFF_LAT,longitude+Config.OFF_LNG);//谷歌转高德 直接加
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);//异步查询
    }

    //得到逆地理编码异步查询结果
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
      String  simpleAddress = formatAddress.substring(9);
        LogUtil.e("查询经纬度对应详细地址："+simpleAddress);
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getAdCode());
        adcode=regeocodeAddress.getAdCode();
        adadress=regeocodeAddress.getFormatAddress();
        endLocationTxt_tmp=regeocodeAddress.getFormatAddress();
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getCityCode());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getCity());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getFormatAddress());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getCity());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getBuilding());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getDistrict());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getProvince());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getNeighborhood());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getTownship());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getStreetNumber().getDirection());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getStreetNumber().getStreet());
        LogUtil.e("查询经纬度对应详细地址："+regeocodeAddress.getStreetNumber().getNumber());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        GeocodeAddress  geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
        String formatAddress = geocodeAddress.getFormatAddress();
        String  simpleAddress = formatAddress.substring(9);
        LogUtil.e("222查询经纬度对应详细地址："+simpleAddress);
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getAdcode());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getCity());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getBuilding());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getDistrict());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getFormatAddress());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getProvince());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getLevel());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getNeighborhood());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getTownship());
        LogUtil.e("222查询经纬度对应详细地址："+geocodeAddress.getLatLonPoint());
    }


    /**3.附近还车网点 列表*/
    private void requestStation(String lng, String lat) {
        String url = "api/station/nearly/query";
        HashMap<String, Object> requestMap = new HashMap<>();
//        requestMap.put("lng", String.valueOf(curLon));
//        requestMap.put("lat", String.valueOf(curLat));
        requestMap.put("lng", lng);//要改为车的经纬度
        requestMap.put("lat", lat);
        requestMap.put("distance", distance);

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatListFunction<>(PositionBean.class))
                .compose(RxNetHelper.<List<PositionBean>>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<List<PositionBean>>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(List<PositionBean> bean) {
                        int count = bean.size();
                        LogUtil.e("请求成功之后 bean的大小"+count);
//                        mylist.clear();
                        mylist=bean;
                        try {
                            adapter_car_share_returnLock.setList(mylist);
                            carmap_list_loadmore.setVisibility(View.GONE);
                            listView_carshare_returnlock.setVisibility(View.VISIBLE);
                        }catch (Throwable t){t.printStackTrace();
                            carmap_list_loadmore.setVisibility(View.VISIBLE);
                            listView_carshare_returnlock.setVisibility(View.GONE);
                        }
//                        if (count > 0) {
//                            PositionBean positionBean = bean.get(0);
//                            endStation = positionBean.stationCode;
//                            endLocationTxt = positionBean.stationName;
//                            if (!TextUtils.isEmpty(positionBean.stationName)){
////                                tvReturnAddress.setText(positionBean.stationName);
//                            }
//                        }
                    }

                    @Override public void onFail(Message<List<PositionBean>> bean) {
                        LogUtil.e("还车网点请求失败"+bean.msg);
                        carmap_list_loadmore.setVisibility(View.VISIBLE);
                        listView_carshare_returnlock.setVisibility(View.GONE);
                    }
                });
    }


    /**4.判断车是否在
     * */
    private void outOfDistance(String carCode,String latt,String lonn){
        String url = "api/tss/distance/return/car"; //返回outOfDistance
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("stationLatitude", latt);     //停车位的坐标
        requestMap.put("stationLongitude", lonn);
        requestMap.put("carCode", carCode);//CA000001
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))//可以去掉object
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override  public void onSuccess(Object bean) {
                        try {
                            LogUtil.e("是否5米内   请求成功:" + bean.toString());//{outOfDistance=1, distance=7666852.1}
                            String lat_tmp = bean.toString().substring(bean.toString().indexOf("out")+10,bean.toString().indexOf("out")+18);
                            lat_tmp=lat_tmp.substring(lat_tmp.indexOf("=")+1,lat_tmp.indexOf(","));
                            LogUtil.e("是否5米内  请求成功:" + lat_tmp);
                               Transferfee(lat_tmp);
                        } catch (Throwable t) { t.printStackTrace();   }
                        //第一个接口请求成功之后


                    }
                    @Override public void onFail(Message<Object> bean) {
                        String str ="";
                        str =bean.data.toString();
                        LogUtil.e("还车失败"+str);
                        ToastUtil.showToast("还车 请求失败:"+bean.msg);
                    }  });

    }

    String isfive ="0";
    /***5.判断是否需要挪车费用,并继续后续流程*/
    private void Transferfee(String isfee){
        if (isfee.equals("1")){//需要挪车费
                 isfive="1";
            DialogUtils.confirmDialog2No(this, "未在网点还车,需加收挪车费。", new AlertDialog.OnDialogButtonClickListener() {
                @Override public void onConfirm() {
                  LogUtil.e("弹窗点击 确认");
                }

                @Override public void onCancel() {
                    LogUtil.e("弹窗点击 取消");
                }
            });
        }else if (isfee.equals("2")){//为2  不需要挪车费
            isfive="2";
//            requestBackCar(isfee);//传入参数,是否5米内
        }

    }



    /**6.最后锁门还车*/
    private void requestBackCar(String isParkingOut) {
        String url = "api/tss/car/return";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("orderCode", PrefUtil.getString(mContext, PrefUtil.ORDER_CODE));

        if (isParkingOut.equals("1")){
            if (!TextUtils.isEmpty(endStation)){ requestMap.put("endStation", endStation); }              //结束位置 网点名
            endLocationTxt = endLocationTxt_tmp;
        }

        if (!TextUtils.isEmpty(endLocationTxt)){ requestMap.put("endLocationTxt", endLocationTxt); }  //结束位置描述

        requestMap.put("adcode", adcode);                                                             //车 所在区域编码
        requestMap.put("isParkingOut", isParkingOut);                                                 //5米之内

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override  public void onSuccess(Object bean) {
                        LogUtil.e("还车成功 结果发送到 展示页面"+bean.toString());
                        Gson g = new Gson();
                        ActivityUtil.startActivityWithOne(mContext, CarShareCompleteActivity.class, g.toJson(bean));
                        finish();
                    }
                    @Override public void onFail(Message<Object> bean) {
                        LogUtil.e("还车失败:"+bean.msg);
                        ToastUtil.showToast(bean.msg+"");
                     if (bean.code==-1004){   //提示余额不足 并继续跳转
                         Gson g = new Gson();
                         fee2NoNeed(g.toJson(bean.data));
                     }

                    }
                });
       }

           private void fee2NoNeed(final String str){//提示余额不足 然后跳转
             DialogHelper.alertDialog(CarShareReturnLockActivity.this, "余额不足！", new AlertDialog.OnDialogButtonClickListener() {
            @Override public void onConfirm() {
                ActivityUtil.startActivityWithOne(mContext, CarShareCompleteActivity.class, str);
                finish();
            }
            @Override public void onCancel() {
                ActivityUtil.startActivityWithOne(mContext, CarShareCompleteActivity.class, str);
                finish();
            }  });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog!=null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
