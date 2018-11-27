package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.config.Config;
import www.qisu666.com.entity.InverstEntity;
import www.qisu666.com.entity.InverstProductEntity;
import www.qisu666.com.widget.CircleProgressBar;

/**
 * 717219917@qq.com 2018/8/14 9:41.
 */
public class InverstAdapter extends BaseAdapter {

    private final static int circleItem=1;
    private final static int listItem=2;
    private Context mContext;
    private InverstEntity entity;
    private OnClickListenner onClickListenner;

    public void setOnClickListenner(OnClickListenner onClickListenner){
        this.onClickListenner=onClickListenner;
    }

    public InverstAdapter(Context mContext,InverstEntity entity){
        this.mContext=mContext;
        this.entity=entity;
    }

    public void refreshData(InverstEntity entity){
        this.entity=entity;
        notifyDataSetChanged();
    }

    public void loadData(InverstEntity entity){
        this.entity.getList().addAll(entity.getList());
        notifyDataSetChanged();
    }

    public InverstEntity getData(){
        return entity;
    }

    @Override
    public int getCount() {
        if(entity.getList().size()==0){
            return 0;
        }else {
            return entity.getList().size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return entity.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InverstViewHolder holder;
        if(convertView==null){
            if(getItemViewType(position)==circleItem){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.inverst_list_head_layout,null);
                
            }else{
                convertView= LayoutInflater.from(mContext).inflate(R.layout.inverst_list_item_layout,null);
            }
            holder=new InverstViewHolder(convertView);
            convertView.setTag(holder);
            final int aa=position-1;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListenner!=null){
                        onClickListenner.onClick(aa);
                    }
                }
            });
        }else{
            holder= (InverstViewHolder) convertView.getTag();
        }
        if(getItemViewType(position)==circleItem){
            holder.tx_already_buy.setText(entity.getHasSubscribeNumber());
            holder.tx_no_buy.setText(entity.getRemainNumber());
            if(Integer.valueOf(entity.getHasSubscribeNumber())<0){
                holder.pro.setProgress(0);
            }else {
                holder.pro.setProgress(Integer.valueOf(entity.getHasSubscribeNumber()));
            }
            holder.pro.setMax(Integer.valueOf(entity.getAllTotalNumber()));
            holder.total_num.setText(entity.getAllTotalNumber()+"");
            holder.tx_title.setText(entity.getList().size()+"辆车开放认购中，共发布"+entity.getAllTotalNumber()+"份汽车投资，已认购"+entity.getHasSubscribeNumber()+"份，待认购"+entity.getRemainNumber()+"份");
        }else{
            if((Integer.valueOf(entity.getList().get(position-1).getProductNumber())-Integer.valueOf(entity.getList().get(position-1).getSurplusNumber()))*100/Integer.valueOf(entity.getList().get(position-1).getProductNumber())<0){
                holder.tx_persentage.setText("0%");
            }else{
                holder.tx_persentage.setText((Integer.valueOf(entity.getList().get(position-1).getProductNumber())-Integer.valueOf(entity.getList().get(position-1).getSurplusNumber()))*100/Integer.valueOf(entity.getList().get(position-1).getProductNumber())+"%");
            }
            holder.brand_name.setText(entity.getList().get(position-1).getProductTitle());
            holder.money_progress.setMax(Integer.valueOf(entity.getList().get(position-1).getProductNumber()));
            holder.money_progress.setProgress(Integer.valueOf(entity.getList().get(position-1).getProductNumber())-Integer.valueOf(entity.getList().get(position-1).getSurplusNumber()));
            holder.tx_money.setText(Integer.valueOf(entity.getList().get(position-1).getTotalAmount())/10000+"万");
            if(entity.getList().get(position-1).getProductType().equals("1")){
                holder.type_img.setImageResource(R.mipmap.rg_16);
            }else{
                holder.type_img.setImageResource(R.mipmap.rg_17);
            }
            holder.car_code.setText(entity.getList().get(position-1).getPlateNumber());
            Glide.with(mContext).load(Config.IMAGE_HOST+entity.getList().get(position-1).getCarImgPath()).into(holder.car_img);
        }
        return convertView;
    }

    public List<InverstProductEntity> getList(){
        return entity.getList();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return circleItem;
        }else{
            return listItem;
        }
    }

    class InverstViewHolder {

        CircleProgressBar pro;
        ImageView type_img;
        TextView brand_name;
        TextView tx_money;
        ProgressBar money_progress;
        TextView tx_persentage;
        ImageView car_img;
        TextView tx_no_buy;
        TextView tx_already_buy;
        TextView car_code;
        TextView total_num;
        TextView tx_title;

        public  InverstViewHolder(View itemView){
            tx_title=itemView.findViewById(R.id.tx_title);
            total_num=itemView.findViewById(R.id.total_num);
            car_code=itemView.findViewById(R.id.car_code);
            car_img=itemView.findViewById(R.id.car_img);
            pro=itemView.findViewById(R.id.pro);
            type_img=itemView.findViewById(R.id.type_img);
            brand_name=itemView.findViewById(R.id.brand_name);
            tx_money=itemView.findViewById(R.id.tx_money);
            money_progress=itemView.findViewById(R.id.money_progress);
            tx_persentage=itemView.findViewById(R.id.tx_persentage);
            tx_no_buy=itemView.findViewById(R.id.tx_no_buy);
            tx_already_buy=itemView.findViewById(R.id.tx_already_buy);
        }
    }

    public interface OnClickListenner{
        void onClick(int position);
    }

}
