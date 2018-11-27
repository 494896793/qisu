package www.qisu666.sdk.partner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.common.util.LogUtil;

import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.config.Config;
import www.qisu666.sdk.partner.Activity_CarBuy;
import www.qisu666.sdk.partner.bean.ProductList;

/**
 * 717219917@qq.com ${DATA} 10:31.
 */
public class Adapter_Term extends BaseAdapter {

    private Context context;
    private List<ProductList> list;

    public Adapter_Term(Context context , List<ProductList> list) {
        this.context = context;
        this.list = list;
    }

    //更新数据
    public void setList(List<ProductList> list_rec){
        list=list_rec;
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return list==null?0:list.size();
    }

    @Override public Object getItem(int i) {
        return list==null?null:list.get(i);
    }

    @Override  public long getItemId(int i) {
        return i;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder_Term holder;
        if (convertView == null) {
            convertView =   LayoutInflater.from(context).inflate(R.layout.item_term, parent, false);
            holder = new MyViewHolder_Term(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder_Term) convertView.getTag();
        }
         final int posi=position;
        holder.position = position;
        holder.item_term_carModelname.setText(list.get(position).getProductTitle());//车型号
        holder.item_term_carname.setText(list.get(position).getPlateNumber());       //车牌号
        holder.item_term_type.setText("投资类型："+list.get(position).getProductTypeCn());//消费类型描述
        if (list.get(position).getProductStatus().equals("1")){
            holder.item_term_status.setText("投资状态："+"开放认购");
            int percent=100-(int)(list.get(position).getSurplusNumber()*100/list.get(position).getProductNumber());
            holder.item_term_progress.setProgress(percent);
            holder.item_term_percent.setText(percent+"%");
        }else {
            holder.item_term_status.setText("投资状态："+"结束认购");
            holder.item_term_progress.setProgress(100);
            holder.item_term_percent.setText("100%");
        }
        holder.item_term_price.setText("参考价:"+list.get(position).getTotalAmount()/10000+"万");
        Glide.with(context).load(Config.IMAGE_HOST+list.get(position).getCarImgPath()).into(holder.item_term_img);

        holder.item_term_layout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                LogUtil.e("点击获取到的车辆："+list.get(posi).getProductCode());//需要传递的产品编码
                try {
                    LogUtil.e("点击获取到的车辆：" + list.get(posi).getProductCode());//需要传递的产品编码
                    Intent intent = new Intent(context, Activity_CarBuy.class);
                    intent.putExtra("productCode", list.get(posi).getProductCode());
                    context.startActivity(intent);
                }catch (Throwable t){t.printStackTrace();}

            }
        });

        return convertView;
    }



    static class MyViewHolder_Term   {
        int position;
        ImageView item_term_img;
        TextView item_term_carname;
        TextView item_term_carModelname;
        TextView item_term_type;
        TextView item_term_status;
        ProgressBar item_term_progress;
        TextView item_term_percent;
        TextView item_term_price;
        LinearLayout item_term_layout;

        public MyViewHolder_Term(View itemView) {
            item_term_img = (ImageView) itemView.findViewById(R.id.item_term_img);                  //汽车图标
            item_term_carname = (TextView) itemView.findViewById(R.id.item_term_carname);           //汽车名字
            item_term_carModelname = (TextView) itemView.findViewById(R.id.item_term_carModelname); //汽车车型
            item_term_type = (TextView) itemView.findViewById(R.id.item_term_type);                 //投资类型
            item_term_status = (TextView) itemView.findViewById(R.id.item_term_status);              //投资状态

            item_term_price= (TextView) itemView.findViewById(R.id.item_term_price);               //参考价格
            item_term_progress = (ProgressBar) itemView.findViewById(R.id.item_term_progress);     //pregress进度
            item_term_percent = (TextView) itemView.findViewById(R.id.item_term_percent);          //文字进度
            item_term_layout = (LinearLayout) itemView.findViewById(R.id.item_term_layout);        //单个item布局



        }
    }
}
