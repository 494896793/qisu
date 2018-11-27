package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.qisu666.com.R;
import www.qisu666.com.config.Config;
import www.qisu666.com.entity.NearbyEntity;
import www.qisu666.com.widget.CustomImageView;

/**
 * 717219917@qq.com 2018/8/13 9:35.
 */
public class NearbyActivityAdapter extends BaseAdapter{

    private Context mContext;
    private List<NearbyEntity> list;
    private OnClickListener onClickListener;

    public  NearbyActivityAdapter(Context mContext,List<NearbyEntity> list){
        this.list=list;
        this.mContext=mContext;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NearbyViewHolder holder;
        if (convertView==null) {
            convertView= LayoutInflater.from(mContext).inflate(R.layout.nearby_activity_item_layout,null);
            holder=new NearbyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (NearbyViewHolder) convertView.getTag();
        }
        holder.new_text_gray.setText("距您约"+doubleToString(Double.valueOf(list.get(position).getDistance()))+"km");
        holder.candriver_tx.setText("可行驶距离："+list.get(position).getOddMileage()+"km");
        holder.car_name.setText(list.get(position).getPlateNumber());
        holder.car_people_num.setText(list.get(position).getCarSeatNum()+"座");
        Glide.with(mContext).load(Config.IMAGE_HOST+list.get(position).getCarImgPath()).into(holder.car_img);
        holder.elect_tx.setText(list.get(position).getOddPower());
        holder.car_brand.setText(list.get(position).getBrandName()+list.get(position).getModelNumber());
        holder.addresss_tx.setText(list.get(position).getLastLocation());
        if(list.get(position).getOddPower()!=null&&!list.get(position).getOddPower().trim().equals("")){
            if(Integer.valueOf(list.get(position).getOddPower())==0){
                holder.elect_img.setImageResource(R.mipmap.yc_51);
            }else if(Integer.valueOf(list.get(position).getOddPower())<=20){
                holder.elect_img.setImageResource(R.mipmap.yc_50);
            }else if(Integer.valueOf(list.get(position).getOddPower())<=40){
                holder.elect_img.setImageResource(R.mipmap.yc_49);
            }else if(Integer.valueOf(list.get(position).getOddPower())<=60){
                holder.elect_img.setImageResource(R.mipmap.yc_48);
            }else if(Integer.valueOf(list.get(position).getOddPower())<=80){
                holder.elect_img.setImageResource(R.mipmap.yc_47);
            }else{
                holder.elect_img.setImageResource(R.mipmap.yc_46);
            }
        }
        holder.submit_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(position);
                }
            }
        });
        return convertView;
    }

    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public class NearbyViewHolder{
        RelativeLayout submit_re;
        TextView candriver_tx;
        TextView car_name;
        TextView car_people_num;
        ImageView car_img;
        ImageView elect_img;
        TextView elect_tx;
        TextView car_brand;
        TextView new_text_gray;
        TextView addresss_tx;

        public NearbyViewHolder(View itemView){
            addresss_tx=itemView.findViewById(R.id.addresss_tx);
            new_text_gray=itemView.findViewById(R.id.distance_tx);
            car_brand=itemView.findViewById(R.id.car_brand);
            submit_re=itemView.findViewById(R.id.submit_re);
            candriver_tx=itemView.findViewById(R.id.candriver_tx);
            car_name=itemView.findViewById(R.id.car_name);
            car_people_num=itemView.findViewById(R.id.car_people_num);
            car_img=itemView.findViewById(R.id.car_img);
            elect_img=itemView.findViewById(R.id.elect_img);
            elect_tx=itemView.findViewById(R.id.elect_tx);
        }
    }

    public interface OnClickListener{
           void onClick(int position);
    }


}
