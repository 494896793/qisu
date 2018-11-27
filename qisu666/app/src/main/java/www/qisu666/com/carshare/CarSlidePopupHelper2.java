package www.qisu666.com.carshare;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.event.CarMapEvent;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.model.CarBeanNew;
import www.qisu666.com.widget.WrapContentViewPager;
import www.qisu666.sdk.carshare.adapter.Adapter_PopCar;
import www.qisu666.sdk.carshare.adapter.Adapter_PopCar2;

//弹出立即订车的dialog
public class CarSlidePopupHelper2 {

    private final Activity mContext;
    private PopupWindow pop;
    //    private CarItemPagerAdapter mAdapter;
    Adapter_PopCar2 mAdapter;
    private WrapContentViewPager viewPager;
    //   private TextView tv_map_address;
    private ListView pop_car_listview;
    private TextView tv_map_address;
    private LinearLayout dongli_map_old;
    private TextView distance_tx;


    public static CarSlidePopupHelper2 of(final Activity context, int count) {
        return new CarSlidePopupHelper2(context, count);
    }

    private CarSlidePopupHelper2(Activity context) {
//        this.rootView = rootView;
        this.mContext = context;
        init();
    }

    private CarSlidePopupHelper2(Activity context, int count) {
//        this.rootView = rootView;
        this.mContext = context;
        init(count);
    }

    private void init() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_car_station_info2, null);
        pop_car_listview = (ListView) contentView.findViewById(R.id.pop_car_listview);
        distance_tx=contentView.findViewById(R.id.distance_tx);
        tv_map_address=contentView.findViewById(R.id.tv_map_address);
        dongli_map_old=contentView.findViewById(R.id.dongli_map_old);
//        viewPager = (WrapContentViewPager) contentView.findViewById(R.id.vp_test);
       tv_map_address = (TextView) contentView.findViewById(R.id.tv_map_address);
       contentView.findViewById(R.id.img_navi).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventBus.getDefault().post(new CarMapEvent(v.getId(), 0));//导航
           }
       });
//        mAdapter = new CarItemPagerAdapter(mContext);
//        viewPager.setAdapter(mAdapter);
        mAdapter = new Adapter_PopCar2(mContext);
        pop_car_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        pop = new PopupWindow(contentView, mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_340dp), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false; // 这里如果返回true的话，touch事件将被拦截 截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框 我觉得这里是API的一个bug
//        pop.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        pop.setAnimationStyle(R.style.Popup_Anim_Bottom);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new CarMapEvent(true));//取消
            }
        });
    }

    //==0 不变，如果大于0  用单个布局
    private void init(int count) {  // 一个自定义的布局，作为显示的内容
        View contentView;
        if (count == 1) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_car_station_info2_new, null);
        } else {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_car_station_info_new, null);
        }
        pop_car_listview = (ListView) contentView.findViewById(R.id.pop_car_listview);
        distance_tx=contentView.findViewById(R.id.distance_tx);
//        viewPager = (WrapContentViewPager) contentView.findViewById(R.id.vp_test);
        dongli_map_old=contentView.findViewById(R.id.dongli_map_old);
        tv_map_address = (TextView) contentView.findViewById(R.id.tv_map_address);
        contentView.findViewById(R.id.img_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CarMapEvent(v.getId(), 0));//导航
            }
        });

//        mAdapter = new CarItemPagerAdapter(mContext);
//        viewPager.setAdapter(mAdapter);
        mAdapter = new Adapter_PopCar2(mContext);
        pop_car_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        pop = new PopupWindow(contentView, mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_340dp), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false; // 这里如果返回true的话，touch事件将被拦截 截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框 我觉得这里是API的一个bug
//        pop.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        pop.setAnimationStyle(R.style.Popup_Anim_Bottom);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new CarMapEvent(true));//取消
            }
        });
    }


    public void show() {
        pop.showAtLocation(mContext.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        pop.dismiss();
    }

    public void setDistance(String distance){
        try{
            if(distance==null||distance.equals("")){
                distance_tx.setText("距您0公里");
            }else{
                distance_tx.setText("距您"+doubleToString(Double.valueOf(distance))+"公里");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public void setStationName(String name) {
        if(name==null||name.trim().equals("")){
            if(dongli_map_old!=null){
                dongli_map_old.setVisibility(View.GONE);
            }
        }else{
            if(dongli_map_old!=null) {
                dongli_map_old.setVisibility(View.VISIBLE);
            }
        }
        LogUtil.e("获取到的station name 桩名字：" + name);
       try{
           if (tv_map_address != null){
               LogUtil.e("获取到的station name 桩名字：不为空");
               tv_map_address.setText(name);
           }else {
               LogUtil.e("获取到的station name 桩名字：为空");
               // 一个自定义的布局，作为显示的内容
               tv_map_address.setText("未获取到地址！");
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

//   public void setNewData(List<CarBean> bean){
//       int count = bean.size();
//       if (count == 0){
////            viewPager.setVisibility(View.GONE);
//           pop_car_listview.setVisibility(View.GONE);
//       }else {
////            viewPager.setVisibility(View.VISIBLE);
//           pop_car_listview.setVisibility(View.GONE);
//           mAdapter.setList(bean);
//       }
//       show();
//   }

    public void setNewData2(List<CarBeanNew.CarList> bean) {
        Log.e("aaaa", "asd" + bean.toString());

//        mAdapter = new CarItemPagerAdapter(mContext);
//        viewPager.setAdapter(mAdapter);
        mAdapter = new Adapter_PopCar2(mContext);
        pop_car_listview.setAdapter(mAdapter);

        int count = bean.size();
        if (count == 0) {
//            viewPager.setVisibility(View.GONE);
            pop_car_listview.setVisibility(View.GONE);
        } else {
//            viewPager.setVisibility(View.VISIBLE);
            pop_car_listview.setVisibility(View.VISIBLE);
            mAdapter.setList(bean);
        }
        show();
    }


    public void clear() {
        mAdapter.clear();
    }

    

}
