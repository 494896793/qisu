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
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;

/**
 * 717219917@qq.com ${DATA} 10:31.
 */
public class Adapter_Fragment_MyTrip_Already extends BaseAdapter {

    private Context context;
    private List<Bean_TripAlready.MyOrderList> list;

    public Adapter_Fragment_MyTrip_Already(Context context, List<Bean_TripAlready.MyOrderList> list) {
        this.context = context;
        this.list = list;
    }

    //更新数据
    public void setList(List<Bean_TripAlready.MyOrderList> list_rec) {
        list = list_rec;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Bean_TripAlready.MyOrderList getItem(int i) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mytrip_already, parent, false);
            holder = new MyViewHolder_Term(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder_Term) convertView.getTag();
        }

        final int posi = position;
        holder.mytrip_item_time.setText(" " + list.get(position).getCreatedTime());//创建时间
        holder.mytrip_item_wangdian.setText("" + list.get(position).getBeginLocationTxt());//网点
        double paymoney = 0.00;
        try {
            paymoney = new BigDecimal(list.get(position).getPayAmount() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            holder.mytrip_item_payAmount.setText("￥" + paymoney );
        } catch (Throwable t) {
            t.printStackTrace();
            holder.mytrip_item_payAmount.setText("￥" + paymoney );
        }

        if (list.get(posi).getStatus().equals("3")) {
            holder.mytrip_item_status.setText("已完成");
        }


        holder.mytrip_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LogUtil.e("点击获取到的行程code：" + list.get(posi).getOrderCode());//需要传递的产品编码
                    Intent intent;
                    // 已完成
                    if (list.get(posi).getStatus().equals("3")) {
                        intent = new Intent(context, Activity_MyTrip_AlreadyDetail.class);
                        intent.putExtra("status", "3");
                        intent.putExtra("orderCode", list.get(posi).getOrderCode());
                        context.startActivity(intent);
                    }

                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        });

        try {
            if (list.get(posi).getEndLocationTxt().equals("")) {
                holder.mytrip_item_return.setText(list.get(posi).getBeginLocationTxt());
            } else {
                holder.mytrip_item_return.setText(list.get(posi).getEndLocationTxt());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            holder.mytrip_item_return.setText(list.get(posi).getBeginLocationTxt());
        }

        return convertView;
    }


    static class MyViewHolder_Term {
        ImageView mytrip_item_img;
        TextView mytrip_item_time;
        TextView mytrip_item_wangdian;
        TextView mytrip_item_payAmount;
        TextView mytrip_item_status;//兼容旧版  已经取消  status=5 时候
        LinearLayout mytrip_item_layout;//item 整个布局mytrip_item_return
        TextView mytrip_item_return;    //还车网点


        public MyViewHolder_Term(View itemView) {
//            mytrip_item_img = (ImageView) itemView.findViewById(R.id.mytrip_item_img);               //图标
            mytrip_item_time = (TextView) itemView.findViewById(R.id.mytrip_item_time);              //时间
            mytrip_item_wangdian = (TextView) itemView.findViewById(R.id.mytrip_item_wangdian);      //用车网点
            mytrip_item_return = (TextView) itemView.findViewById(R.id.mytrip_item_return);//还车网点
            mytrip_item_payAmount = (TextView) itemView.findViewById(R.id.mytrip_item_payAmount);    //实付款
            mytrip_item_layout = (LinearLayout) itemView.findViewById(R.id.mytrip_item_layout);     //布局
            mytrip_item_status = (TextView) itemView.findViewById(R.id.mytrip_item_status);


        }
    }
}
