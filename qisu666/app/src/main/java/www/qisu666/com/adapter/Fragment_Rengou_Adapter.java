package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.qisu666.com.R;
import www.qisu666.com.entity.InverstOrderEntity;

import static www.qisu666.com.config.Config.IMAGE_HOST;

/**
 * 717219917@qq.com 2018/8/15 9:31.
 */
public class Fragment_Rengou_Adapter extends BaseAdapter {

    private Context mContext;
    private List<InverstOrderEntity> list;
    private OnItemClickListnner onItemClickListnner;

    public void setOnItemClickListnner(OnItemClickListnner onItemClickListnner){
        this.onItemClickListnner=onItemClickListnner;
    }

    public   Fragment_Rengou_Adapter(Context mContext,List<InverstOrderEntity> list){
        this.mContext=mContext;
        this.list=list;
    }

    public void refreshData(List<InverstOrderEntity> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public void loadData(List<InverstOrderEntity> list){
        this.list.addAll(list);
        notifyDataSetChanged();
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

        RengouViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.fragment_rengou_item_layout,null);
            holder=new RengouViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (RengouViewHolder) convertView.getTag();
        }
        holder.tx_brand.setText(list.get(position).getProductTitle());
        holder.tx_money.setText(list.get(position).getSubAmount());
        holder.tx_time.setText(list.get(position).getCreatedTime());
        if(list.get(position).getSubType().equals("1")){        //投资型
            holder.img_type.setImageResource(R.mipmap.rg_16);
        }else{
            holder.img_type.setImageResource(R.mipmap.rg_17);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListnner!=null){
                    onItemClickListnner.onClick(position);
                }
            }
        });
        Glide.with(mContext).load(IMAGE_HOST+list.get(position).getCarImgPath()).into(holder.car_img);
        return convertView;
    }

    class RengouViewHolder {

        TextView tx_brand;
        ImageView img_type;
        TextView tx_money;
        TextView tx_time;
        ImageView car_img;

        public  RengouViewHolder(View itemView){
            car_img=itemView.findViewById(R.id.car_img);
            tx_brand=itemView.findViewById(R.id.tx_brand);
            img_type=itemView.findViewById(R.id.img_type);
            tx_money=itemView.findViewById(R.id.tx_money);
            tx_time=itemView.findViewById(R.id.tx_time);
        }
    }

    public interface OnItemClickListnner{
        void onClick(int position);
    }

}
