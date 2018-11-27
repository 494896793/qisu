package www.qisu666.sdk.mytrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.sdk.mytrip.bean.Bean_TripNo;

/**
 * 717219917@qq.com ${DATA} 10:31.
 */
public class Adapter_Fragment_MyTrip_No extends BaseAdapter {

    private Context context;
    private List<Bean_TripNo.MyOrderList> list;

    public Adapter_Fragment_MyTrip_No(Context context, List<Bean_TripNo.MyOrderList> list) {
        this.context = context;
        this.list = list;
    }

    //更新数据
    public void setList(List<Bean_TripNo.MyOrderList> list_rec) {
        list = list_rec;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Bean_TripNo.MyOrderList getItem(int i) {
        return list == null ? null : list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder_Term holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mytrip_no, parent, false);
            holder = new MyViewHolder_Term(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder_Term) convertView.getTag();
        }
        final int posi = position;
        holder.mytrip_item_time.setText(" " + list.get(position).getCreatedTime());//创建时间
        holder.mytrip_item_wangdian.setText("" + list.get(position).getBeginLocationTxt());//网点
        double paymoney = 0.0;
//              try {
//                  paymoney=new BigDecimal(list.get(position).getPayAmount()/100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                  holder.mytrip_item_payAmount.setText("实付款：￥"+paymoney+"元");
//              }catch (Throwable t){t.printStackTrace();
//                  holder.mytrip_item_payAmount.setText("实付款：￥"+paymoney+"元");
//              }
        holder.mytrip_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LogUtil.e("点击获取到的行程code：" + list.get(posi).getOrderCode());//需要传递的产品编码
                    Intent intent = new Intent(context, Activity_MyTripDetail.class);
                    intent.putExtra("status", list.get(posi).getStatus());//订单状态
                    intent.putExtra("orderCode", list.get(posi).getOrderCode());
                    context.startActivity(intent);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        });


        return convertView;
    }


    static class MyViewHolder_Term {
        ImageView mytrip_item_img;
        TextView mytrip_item_time;
        TextView mytrip_item_wangdian;
        TextView mytrip_item_payAmount;
        LinearLayout mytrip_item_layout;//item 整个布局
        TextView mytrip_item_cancel;


        ImageView mytrip_item_img_return;//还车网点img
        TextView mytrip_item_return;     //还车网点txt

        public MyViewHolder_Term(View itemView) {
            mytrip_item_img = (ImageView) itemView.findViewById(R.id.mytrip_item_img);               //图标
            mytrip_item_time = (TextView) itemView.findViewById(R.id.mytrip_item_time);              //时间
            mytrip_item_wangdian = (TextView) itemView.findViewById(R.id.mytrip_item_wangdian);      //还车网点
//            mytrip_item_payAmount = (TextView) itemView.findViewById(R.id.mytrip_item_payAmount);    //实付款
            mytrip_item_layout = (LinearLayout) itemView.findViewById(R.id.mytrip_item_layout);     //布局
//            mytrip_item_img_return = (ImageView) itemView.findViewById(R.id.mytrip_item_img_return); //网点2
            mytrip_item_return = (TextView) itemView.findViewById(R.id.mytrip_item_return);           //网点2描述
            mytrip_item_cancel = (TextView) itemView.findViewById(R.id.mytrip_item_cancel);           //是否取消
        }
    }
}
