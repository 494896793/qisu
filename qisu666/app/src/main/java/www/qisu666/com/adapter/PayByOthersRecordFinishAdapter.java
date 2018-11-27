package www.qisu666.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

 //他人代付订单适配器
public class PayByOthersRecordFinishAdapter extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<PayByOthersRecordFinishAdapter.MyViewHolder> {

    private Context context;
    private List<Map<String, Object>> list;

    private OnLoadRefreshCallBack listener;

    public PayByOthersRecordFinishAdapter(Context context, OnLoadRefreshCallBack listener, List<Map<String, Object>> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @Override
    public PayByOthersRecordFinishAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =  new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_pay_by_others_record_finish, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PayByOthersRecordFinishAdapter.MyViewHolder holder, int position) {
        holder.position = position;

        holder.tv_station_name.setText(list.get(position).get("charge_station_name").toString());
        holder.tv_time.setText(StringUtil.formatDate(list.get(position).get("charge_end_time").toString()));
        holder.tv_phone.setText(list.get(position).get("mobile_no").toString());
        holder.tv_charging_amount.setText(list.get(position).get("charge_electricity").toString()+"度");
        holder.tv_person.setText(list.get(position).get("user_name").toString());
        holder.tv_charging_duration.setText(list.get(position).get("charge_time").toString().length() > 5 ? StringUtil.formatTime(list.get(position).get("charge_time").toString()) : "00:00:00");
        holder.tv_payment_amount.setText("￥"+list.get(position).get("pay_money").toString());
        holder.tv_actual_amount.setText("￥"+list.get(position).get("real_money").toString());
        holder.tv_surplus_amount.setText("￥"+list.get(position).get("reback_money").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onRefresh(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final RefreshView refreshView) {
        if (listener != null) {
            listener.onRefresh();
        }
    }

    @Override
    public void onLoadMore(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final LoadMoreView loadMoreView) {
        if (listener != null) {
            listener.onLoadMore();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        int position;

        TextView tv_station_name;
        TextView tv_time;
        TextView tv_phone;
        TextView tv_charging_amount;
        TextView tv_person;
        TextView tv_charging_duration;
        TextView tv_payment_amount;
        TextView tv_actual_amount;
        TextView tv_surplus_amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_station_name = (TextView) itemView.findViewById(R.id.tv_station_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_charging_amount = (TextView) itemView.findViewById(R.id.tv_charging_amount);
            tv_person = (TextView) itemView.findViewById(R.id.tv_person);
            tv_charging_duration = (TextView) itemView.findViewById(R.id.tv_charging_duration);
            tv_payment_amount = (TextView) itemView.findViewById(R.id.tv_payment_amount);
            tv_actual_amount = (TextView) itemView.findViewById(R.id.tv_actual_amount);
            tv_surplus_amount = (TextView) itemView.findViewById(R.id.tv_surplus_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
