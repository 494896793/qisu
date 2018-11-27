package www.qisu666.sdk.carshare.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.activity.CarShareConfirmActivity;
import www.qisu666.com.activity.LoginActivity;
import www.qisu666.com.event.CarMapEvent;
import www.qisu666.com.event.CarOrderLationEvent;
import www.qisu666.com.model.CarBean;
import www.qisu666.com.model.CarBeanNew;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.PrefUtil;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.StringUtil;
import www.qisu666.common.utils.ToastUtil;

/**
 * 717219917@qq.com 2018/5/15 19:24.
 */
public class Adapter_PopCar2 extends BaseAdapter {
    private List<CarBeanNew.CarList> mList = new ArrayList<>();
    private Context context;

    public void clear() {
        mList.clear();
    }

    public Adapter_PopCar2(Context context_tmp, List<CarBeanNew.CarList> list) {
        context = context_tmp;
        mList = list;
    }

    public Adapter_PopCar2(Context context_tmp) {
        context = context_tmp;
    }

    public void setList(List<CarBeanNew.CarList> list) {
        mList.clear();
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CarBeanNew.CarList getItem(int index) {
        return mList == null ? null : mList.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item_view2, null);//使用原始布局
            viewHolder.tvItemCarNum = (TextView) convertView.findViewById(R.id.tv_item_car_num);
            viewHolder.tvItemCarName = (TextView) convertView.findViewById(R.id.tv_item_car_name);
            viewHolder.tvItemCarType = (TextView) convertView.findViewById(R.id.tv_item_car_type);
            viewHolder.tvItemCarMile = (TextView) convertView.findViewById(R.id.tv_item_car_mile);
            viewHolder.tvItemCarPower = (TextView) convertView.findViewById(R.id.tv_item_car_power);
            viewHolder.imgItemCar = (ImageView) convertView.findViewById(R.id.img_item_car);
            viewHolder.llCarbyTime = (LinearLayout) convertView.findViewById(R.id.ll_car_by_time);
            viewHolder.tv_map_charging = (TextView) convertView.findViewById(R.id.tv_map_charging);//立即还车
            viewHolder.img_navi = (ImageView) convertView.findViewById(R.id.img_navi);               //导航按钮
            viewHolder.btn_map_charging=convertView.findViewById(R.id.btn_map_charging);
            viewHolder.elect_img=convertView.findViewById(R.id.elect_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int posi = i;
        final CarBeanNew.CarList bean = mList.get(posi);
        Log.e("aaaa", "asd" + bean.getOddMileage());
//        viewHolder.tv_map_charging.setText("立即订车");
        viewHolder.llCarbyTime.setVisibility(View.GONE);
        Picasso.with(context).load(StringUtil.addImageHost(bean.getCarImgPath())).placeholder(R.mipmap.yc_52).into(viewHolder.imgItemCar);
        try {
            viewHolder.tvItemCarName.setText(bean.getBrandName() + " " + bean.getModelNumber());
        } catch (Throwable t) {
            t.printStackTrace();
        }//车名
        try {
            if (bean.getBrandName() == null || bean.getModelNumber() == null) {
                viewHolder.tvItemCarName.setText(bean.getBrandName() + " " + bean.getModelNumber());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }//车名

        viewHolder.tvItemCarType.setText("" + (Double.valueOf(bean.getCarSeatNum())).intValue() + "座");
        viewHolder.tvItemCarMile.setText("可行驶里程：" + bean.getOddMileage() + "公里");
        viewHolder.tvItemCarPower.setText("(" + bean.getOddPowerForNE() + "%)");

        if(bean.getOddPowerForNE()!=null) {
            if (Integer.valueOf(bean.getOddPowerForNE()) > 80) {
                viewHolder.btn_map_charging.getBackground().setAlpha(255);
                viewHolder.tv_map_charging.setEnabled(true);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_46s);
                viewHolder.tv_map_charging.setText("立即订车");
            } else if (Integer.valueOf(bean.getOddPowerForNE()) > 60) {
                viewHolder.btn_map_charging.getBackground().setAlpha(255);
                viewHolder.tv_map_charging.setEnabled(true);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_47);
                viewHolder.tv_map_charging.setText("立即订车");
            } else if (Integer.valueOf(bean.getOddPowerForNE()) > 40) {
                viewHolder.btn_map_charging.getBackground().setAlpha(255);
                viewHolder.tv_map_charging.setEnabled(true);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_48);
                viewHolder.tv_map_charging.setText("立即订车");
            } else if (Integer.valueOf(bean.getOddPowerForNE()) > 20) {
                viewHolder.btn_map_charging.getBackground().setAlpha(255);
                viewHolder.tv_map_charging.setEnabled(true);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_49);
                viewHolder.tv_map_charging.setText("立即订车");
            } else if (Integer.valueOf(bean.getOddPowerForNE()) > 10) {
                viewHolder.btn_map_charging.getBackground().setAlpha(100);
                viewHolder.tv_map_charging.setEnabled(false);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_50);
                viewHolder.tv_map_charging.setText("电量过低，不能租车");
            } else {
                viewHolder.btn_map_charging.getBackground().setAlpha(100);
                viewHolder.tv_map_charging.setEnabled(false);
                viewHolder.elect_img.setImageResource(R.mipmap.yc_51);
                viewHolder.tv_map_charging.setText("电量过低,不能租车");
            }
        }

        viewHolder.tvItemCarNum.setText(bean.getPlateNumber());
//        viewHolder.tvItemCarName.setText(bean.getCarBrandModels().brandName+bean.getCarBrandModels().modelNumber);
        viewHolder.tv_map_charging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SPUtil.put(context, "carCode", bean.getCarCode());
                    PrefUtil.saveString(context, PrefUtil.CAR_CODE, bean.getCarCode());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                Gson g = new Gson();
                String s = g.toJson(bean);
                if (!TextUtils.isEmpty(s)) {
                    try{
                        if (UserParams.INSTANCE.checkLogin((Activity) context)) {
                            ActivityUtil.startActivityWithOne(context, CarShareConfirmActivity.class, s);
                        } else {
                            ToastUtil.showToast(R.string.toast_prompt_login);
                            Intent i = new Intent(context, LoginActivity.class);
                            ((Activity) context).startActivityForResult(i, ConstantCode.REQ_LOGIN);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                EventBus.getDefault().post(new CarOrderLationEvent(mList.get(posi).getLatitude(),mList.get(posi).getLongitude()));
                EventBus.getDefault().post(new CarMapEvent(view.getId(), posi));
                EventBus.getDefault().post("立即订车");
            }
        });

        viewHolder.img_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LogUtil.e("单个item点击时的经纬度：" + bean);
                LogUtil.e("单个item点击时的经纬度：" + bean.toString());
                LogUtil.e("单个item点击时的经纬度：" + bean.getLatitude());
                LogUtil.e("单个item点击时的经纬度：" + bean.getLongitude());

                EventBus.getDefault().post("导航." + bean.getLatitude() + "," + bean.getLongitude());
            }
        });


        return convertView;
    }


    class ViewHolder {
        LinearLayout btn_map_charging;
        TextView tvItemCarNum;
        TextView tvItemCarName;
        TextView tvItemCarType;
        TextView tvItemCarMile;
        TextView tvItemCarPower;
        TextView tv_map_charging;//立即还车
        ImageView img_navi;      //导航按钮
        ImageView elect_img;
        

        ImageView imgItemCar;
        LinearLayout llCarbyTime;//单个的布局
    }


}
