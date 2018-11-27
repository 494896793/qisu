package www.qisu666.com.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import www.qisu666.com.activity.LoginActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareConfirmActivity;
import www.qisu666.com.event.CarMapEvent;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.UserParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import www.qisu666.common.utils.ToastUtil;


//车辆列表 适配器   立即订车界面的  (小)
public class CarItemPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private Stack<View> mRecycledViewsList;
//    private int mCount = 5;
    private List<CarBean> mList = new ArrayList<>();
    public CarItemPagerAdapter(Context context) {
        this.context = context;
        mRecycledViewsList = new Stack<>();
    }
    @Override public int getCount() {
        return mList.size();
    } // 显示多少个页面
    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    // 初始化显示的条目对象
    @SuppressLint("SetTextI18n")
    @Override public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflateOrRecycleView(context);
        ImageView imgItemCar = (ImageView) view.findViewById(R.id.img_item_car);
        TextView tvItemCarNum = (TextView) view.findViewById(R.id.tv_item_car_num);//车牌号
        TextView tvItemCarName = (TextView) view.findViewById(R.id.tv_item_car_name);
        TextView tvItemCarType = (TextView) view.findViewById(R.id.tv_item_car_type);
        TextView tvItemCarMile = (TextView) view.findViewById(R.id.tv_item_car_mile);
        TextView tvItemCarPower = (TextView) view.findViewById(R.id.tv_item_car_power);
        LinearLayout llCarbyTime = (LinearLayout) view.findViewById(R.id.ll_car_by_time);
        TextView tvMapCharging = (TextView) view.findViewById(R.id.tv_map_charging);//立即还车
        tvMapCharging.setText("立即订车");
        llCarbyTime.setVisibility(View.GONE);
        final CarBean bean = mList.get(position);
        Picasso.with(context).load(StringUtil.addImageHost(bean.carImgPath)).placeholder(R.mipmap.yc_52).into(imgItemCar);

        try{tvItemCarName.setText(bean.getCarBrandModels().brandName + " " + bean.getCarBrandModels().modelNumber);}catch (Throwable t){t.printStackTrace();}//车名

        tvItemCarType.setText("(" + (Double.valueOf(bean.carSeatNum)).intValue() + "座)");//转 double
//        tvItemCarType.setText("("+bean.carSeatNum+"座)");
        tvItemCarMile.setText("可行驶里程："+bean.oddMileage + "公里(");
        tvItemCarPower.setText(bean.oddPowerForNE+"%)");
        tvItemCarNum.setText(bean.plateNumber);
        view.findViewById(R.id.btn_map_charging).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();
                String s = g.toJson(bean);
                if (!TextUtils.isEmpty(s)) {
                    if (UserParams.INSTANCE.checkLogin((Activity) context)) {
                        ActivityUtil.startActivityWithOne(context, CarShareConfirmActivity.class, s);
                    }    else{
                        ToastUtil.showToast(R.string.toast_prompt_login);
                        Intent i = new Intent(context, LoginActivity.class);
                        ((Activity) context).getParent().startActivityForResult(i, ConstantCode.REQ_LOGIN);
                    }
                }
                EventBus.getDefault().post(new CarMapEvent(v.getId(), position));
            }
        });
        container.addView(view);// 添加到ViewPager容器
        return view;            // 返回填充的View对象
    }

    // 销毁条目对象
    @Override public void destroyItem(ViewGroup container, int position, Object view) {
        ViewPager pager = (ViewPager) container;
        View recycledView = (View) view;
        pager.removeView(recycledView);
        mRecycledViewsList.push(recycledView);
        Log.i("guanglog", "Stored view in cache " + recycledView.hashCode());
    }

    private View inflateOrRecycleView(Context context) {
        View viewToReturn;
        mInflater = LayoutInflater.from(context);
        if (mRecycledViewsList.isEmpty()) {
            viewToReturn = mInflater.inflate(R.layout.car_item_view, null, false);
        } else {
            viewToReturn = mRecycledViewsList.pop();
            Log.i("guanglog", "Restored recycled view from cache " + viewToReturn.hashCode());
        }
        return viewToReturn;
    }

    public void setNewData(List<CarBean> bean) {
        mList.clear();
        mList = bean;
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

}
