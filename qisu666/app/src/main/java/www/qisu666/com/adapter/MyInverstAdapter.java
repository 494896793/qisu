package www.qisu666.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.thridparty.G;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import www.qisu666.com.R;
import www.qisu666.com.activity.InverstDetailActivity;
import www.qisu666.com.config.Config;
import www.qisu666.com.entity.MyInverstEntity;
import www.qisu666.com.util.DateUtils;

/**
 * 717219917@qq.com 2018/8/15 15:07.
 */
public class MyInverstAdapter extends BaseAdapter {

    private MyInverstEntity entity;
    private Context mContext;

    public MyInverstAdapter(MyInverstEntity entity,Context mContext){
        this.entity=entity;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        if (entity.getList().size()==0) {
            return 0;
        }else{
            return entity.getList().size()+1;
        }
    }

    public void refreshData(MyInverstEntity entity){
        this.entity=entity;
        notifyDataSetChanged();
    }

    public void loadData(MyInverstEntity entity){
        this.entity.getList().addAll(entity.getList());
        this.entity.setTotalSubscribeCount(entity.getTotalSubscribeCount());
        this.entity.setTotalProfit(entity.getTotalProfit());
        this.entity.setReceiveMonthProfit(entity.getReceiveMonthProfit());
        this.entity.setMonthProfit(entity.getMonthProfit());
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (position==0) {
            return 0;
        }else{
            return entity.getList().get(position-1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyInverstViewHolder holder;
        if(convertView==null){
            if(getItemViewType(position)==1){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.myinverst_item_header_layout,null);
            }else{
                convertView= LayoutInflater.from(mContext).inflate(R.layout.myinverst_item_layout,null);
            }
            holder=new MyInverstViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (MyInverstViewHolder) convertView.getTag();
            if(holder==null||holder.car_img==null||holder.tx_totalProfit==null){
                if(getItemViewType(position)==1){
                    convertView= LayoutInflater.from(mContext).inflate(R.layout.myinverst_item_header_layout,null);
                }else{
                    convertView= LayoutInflater.from(mContext).inflate(R.layout.myinverst_item_layout,null);
                }
                holder=new MyInverstViewHolder(convertView);
                convertView.setTag(holder);
            }
        }
        if(getItemViewType(position)==1){
            holder.tx_totalProfit.setText(entity.getTotalProfit());
            holder.tx_totalSubscribeCount.setText(entity.getTotalSubscribeCount());
            holder.tx_monthProfit.setText(entity.getMonthProfit()+"元");
            holder.tx_receiveMonthProfit.setText(entity.getReceiveMonthProfit()+"元");
            if(entity.getList().get(0).getSubType().equals("1")){
//                holder.tx_producttype.setText("投资型");
                holder.tz_re.setVisibility(View.VISIBLE);
                holder.tz_re2.setVisibility(View.VISIBLE);
                holder.xf_re.setVisibility(View.GONE);
                holder.money_title.setText("投资累计收益（元）");
            }else{
                holder.tz_re.setVisibility(View.GONE);
                holder.tz_re2.setVisibility(View.GONE);
                holder.xf_re.setVisibility(View.VISIBLE);
                holder.tx_tz.setText(entity.getTotalProfit());
                holder.money_title.setText("本月可抵扣额度");
//                holder.tx_producttype.setText("消费型");
            }
        }else{
            Glide.with(mContext).load(Config.IMAGE_HOST+entity.getList().get(position-1).getCarImgPath()).into(holder.car_img);
            holder.tx_brand.setText(entity.getList().get(position-1).getProductTitle());
            if(entity.getList().get(position-1).getSubType().equals("1")){
//                holder.tx_producttype.setText("投资型");
                holder.first_linear.setVisibility(View.VISIBLE);
                holder.tx_firstPhaseTime.setText(entity.getList().get(position-1).getFirstPhaseTime());
            }else{
                holder.first_linear.setVisibility(View.GONE);
//                holder.tx_producttype.setText("消费型");
            }
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String nowDate=simpleDateFormat.format(new Date());
            if(DateUtils.isDateOneBigger(nowDate,entity.getList().get(position-1).getContractExpiresTime())){
                holder.tx_contra_statu.setText("可解除");
            }else{
                holder.tx_contra_statu.setText("不可解除");
            }
            holder.tx_time.setText(entity.getList().get(position-1).getCreatedTime());
            holder.tx_contractExpiresTime.setText(entity.getList().get(position-1).getContractExpiresTime());
            holder.tx_position.setText((position)+"");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, InverstDetailActivity.class);
                    intent.putExtra("productCode",entity.getList().get(position-1).getProductCode());
                    intent.putExtra("subCode",entity.getList().get(position-1).getSubCode());
                    intent.putExtra("subType",entity.getList().get(position-1).getSubType());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else{
            return 2;
        }
    }

    class MyInverstViewHolder {

        TextView money_title;
        TextView tx_tz;
        ImageView car_img;
        TextView tx_brand;
        TextView tx_producttype;
        TextView tx_money;
        TextView tx_contractExpiresTime;
        TextView tx_firstPhaseTime;
        TextView tx_time;
        TextView tx_contra_statu;
        TextView tx_position;
        TextView tx_receiveMonthProfit;
        TextView tx_monthProfit;
        TextView tx_totalSubscribeCount;
        TextView tx_totalProfit;
        LinearLayout first_linear;
        RelativeLayout xf_re;
        RelativeLayout tz_re;
        RelativeLayout tz_re2;

        public MyInverstViewHolder(View itemView){
            money_title=itemView.findViewById(R.id.money_title);
            tx_tz=itemView.findViewById(R.id.tx_tz);
            tz_re2=itemView.findViewById(R.id.tz_re2);
            tz_re=itemView.findViewById(R.id.tz_re);
            xf_re=itemView.findViewById(R.id.xf_re);
            first_linear=itemView.findViewById(R.id.first_linear);
            car_img=itemView.findViewById(R.id.car_img);
            tx_brand=itemView.findViewById(R.id.tx_brand);
            tx_producttype=itemView.findViewById(R.id.tx_producttype);
            tx_money=itemView.findViewById(R.id.tx_money);
            tx_contractExpiresTime=itemView.findViewById(R.id.tx_contractExpiresTime);
            tx_firstPhaseTime=itemView.findViewById(R.id.tx_firstPhaseTime);
            tx_time=itemView.findViewById(R.id.tx_time);
            tx_contra_statu=itemView.findViewById(R.id.tx_contra_statu);
            tx_position=itemView.findViewById(R.id.tx_position);
            tx_receiveMonthProfit=itemView.findViewById(R.id.tx_receiveMonthProfit);
            tx_monthProfit=itemView.findViewById(R.id.tx_monthProfit);
            tx_totalSubscribeCount=itemView.findViewById(R.id.tx_totalSubscribeCount);
            tx_totalProfit=itemView.findViewById(R.id.tx_totalProfit);
        }
    }

}
