package www.qisu666.sdk.partner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;

/**
 * 717219917@qq.com ${DATA} 10:31.
 */
public class Adapter_Partner extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<Adapter_Partner.MyViewHolder_Partner> {

    private Context context;
    private List<Map<String, Object>> list;
    //收入或者支出来源
    private int[] sources = {R.string.bill_ali, R.string.bill_wx, R.string.bill_refund, R.string.bill_consume, R.string.bill_consume_by_other, R.string.bill_refund_by_other};
    //文字颜色
    private int[] colors = {R.color.text_blue_ali, R.color.text_green_wx, R.color.text_red, R.color.text_red, R.color.text_red, R.color.text_green_wx};

    private OnLoadRefreshCallBack listener;

    public Adapter_Partner(Context context, OnLoadRefreshCallBack listener, List<Map<String, Object>> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @Override  public  Adapter_Partner.MyViewHolder_Partner onCreateViewHolder(ViewGroup parent, int viewType) {
        Adapter_Partner.MyViewHolder_Partner holder =  new Adapter_Partner.MyViewHolder_Partner(LayoutInflater.from(context).inflate(R.layout.item_rv_bill, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder_Partner holder, int position) {
        holder.position = position;
//        int source_no = Integer.valueOf(list.get(position).get("source_no").toString());
//        holder.tv_time.setText(StringUtil.formatDate(list.get(position).get("create_time").toString()));
//        holder.tv_balance.setText(list.get(position).get("acct_total").toString());
//        holder.tv_charge.setTextColor(ContextCompat.getColor(context, colors[source_no]));
//        holder.tv_charge_gift.setTextColor(ContextCompat.getColor(context, colors[2]));
//        String giftFee = list.get(position).get("fee_gift").toString();
//        if (!TextUtils.isEmpty(giftFee)
//                && !giftFee.equals("0")){
//            int gitFeeLength = giftFee.length();
//            if (gitFeeLength > 2){
//                holder.tv_charge_gift.setText(" (赠送"+giftFee.substring(0, gitFeeLength - 2)+")");
//            }
//        }else {
//            holder.tv_charge_gift.setText("");
//        }
//        if("0".equals(list.get(position).get("trade_type").toString())){
//            holder.tv_charge.setText("+"+list.get(position).get("charge_money").toString());
//        } else {
//            holder.tv_charge.setText("-"+list.get(position).get("charge_money").toString());
//        }
//        holder.tv_from.setText(sources[source_no]);
    }

    @Override public int getItemCount() {
        return list.size();
    }

    @Override  public void onRefresh(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final RefreshView refreshView) {
        if (listener != null) {
            listener.onRefresh();
        }
    }

    @Override public void onLoadMore(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final LoadMoreView loadMoreView) {
        if (listener != null) {
            listener.onLoadMore();
        }
    }

    //重用view
    public class MyViewHolder_Partner extends RecyclerView.ViewHolder {
        int position;
        TextView tv_charge;
        TextView tv_balance;
        TextView tv_from;
        TextView tv_time;
        TextView tv_charge_gift;
        public MyViewHolder_Partner(View itemView) {
            super(itemView);
            tv_charge = (TextView) itemView.findViewById(R.id.tv_charge);
            tv_balance = (TextView) itemView.findViewById(R.id.tv_balance);
            tv_from = (TextView) itemView.findViewById(R.id.tv_from);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_charge_gift = (TextView) itemView.findViewById(R.id.tv_charge_gift);

        }
    }
}
