package www.qisu666.com.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

//代付款人 适配器
public class CollectionAdapter extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<CollectionAdapter.MyViewHolder> {
    private Context context;
    private List<Map<String, Object>> list;
    private OnLoadRefreshCallBack listener;
    private OnNaviClickListener onNaviClickListener;
    private OnItemClick onItemClick;

    public CollectionAdapter(Context context, OnLoadRefreshCallBack listener, OnItemClick onItemClick, List<Map<String, Object>> list) {
        this.context = context;
        this.listener = listener;
        this.onItemClick = onItemClick;
        this.list = list;
    }

    @Override public CollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =  new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_collection, parent, false));
        return holder;
    }

    @Override public void onBindViewHolder(CollectionAdapter.MyViewHolder holder, final int position) {
        holder.tv_nearby_title.setText(list.get(position).get("station_name").toString());
        holder.tv_nearby_fee.setText(list.get(position).get("charge_fee_per").toString()+"元/度");
        holder.tv_nearby_addr.setText(list.get(position).get("charge_address").toString());
//        double b = new BigDecimal(list.get(position).get("charge_distance").toString()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        holder.tv_nearby_distance.setText(list.get(position).get("charge_distance").toString() + "km");
        holder.tv_nearby_free_fast.setText("空闲"+list.get(position).get("pile_fast_num_free").toString());
        holder.tv_nearby_free_slow.setText("空闲"+list.get(position).get("pile_slow_num_free").toString());
        String method = list.get(position).get("charge_method").toString();
//        holder.tv_nearby_type.setText(method.equals("00") ? "交流" : method.equals("01") ? "直流" : "直流/交流");

        holder.tv_nearby_type.setText("总桩数"+list.get(position).get("total_pile_count").toString());

//        holder.tv_charging_duration.setText(list.get(position).get("charge_quantity").toString()+"度");
        holder.rl_nearby_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNaviClickListener != null) {
                    onNaviClickListener.onNaviClick(position);
                }
            }
        });
        holder.station_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null) {
                    onItemClick.onItemClick(position);
                }
            }
        });
    }
    @Override public int getItemCount() {
        return list.size();
    }
    @Override  public void onRefresh(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final RefreshView refreshView) {
        if (listener != null) { listener.onRefresh();  }
    }
    @Override public void onLoadMore(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final LoadMoreView loadMoreView) {
        if (listener != null) { listener.onLoadMore();  }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nearby_title;
        TextView tv_nearby_fee;
        TextView tv_nearby_addr;
        TextView tv_nearby_distance;
        TextView tv_nearby_free_fast;
        TextView tv_nearby_free_slow;
        TextView tv_nearby_type;
        View rl_nearby_charge;
        View station_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            station_item = itemView.findViewById(R.id.station_item);
            tv_nearby_title = (TextView) itemView.findViewById(R.id.tv_nearby_title);
            tv_nearby_fee = (TextView) itemView.findViewById(R.id.tv_nearby_fee);
            tv_nearby_addr = (TextView) itemView.findViewById(R.id.tv_nearby_addr);
            tv_nearby_distance = (TextView) itemView.findViewById(R.id.tv_nearby_distance);
            tv_nearby_free_fast = (TextView) itemView.findViewById(R.id.tv_nearby_free_fast);
            tv_nearby_free_slow = (TextView) itemView.findViewById(R.id.tv_nearby_free_slow);
            tv_nearby_type = (TextView) itemView.findViewById(R.id.tv_nearby_type);
            rl_nearby_charge = itemView.findViewById(R.id.rl_nearby_charge);
        }
    }
    public interface OnNaviClickListener{ void onNaviClick(int position); }
    public void setOnNaviClickListener(OnNaviClickListener onNaviClickListener){ this.onNaviClickListener = onNaviClickListener;  }
    public interface OnItemClick{ void onItemClick(int position);  }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    @Override  public Drawable bindEmptyViewDrawable() {
        return ContextCompat.getDrawable(context, R.mipmap.ic_empty_collection);
    }
}
