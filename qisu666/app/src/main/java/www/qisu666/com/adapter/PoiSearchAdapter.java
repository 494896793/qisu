package www.qisu666.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;

 //信息点 搜索
public class PoiSearchAdapter extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<PoiSearchAdapter.MyViewHolder> {

    private Context context;
    private List<PoiItem> list;
    private OnLoadRefreshCallBack listener;
    private OnItemClickListener onItemClickListener;

    public PoiSearchAdapter(Context context, OnLoadRefreshCallBack listener, OnItemClickListener onItemClickListener, List<PoiItem> list) {
        this.context = context;
        this.listener = listener;
        this.onItemClickListener = onItemClickListener;
        this.list = list;
    }

    @Override public PoiSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =  new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_poi_search, parent, false));
        return holder;
    }

    @Override public void onBindViewHolder(PoiSearchAdapter.MyViewHolder holder, int position) {
        holder.position = position;
        holder.tv_address.setText(list.get(position).getTitle());
        holder.tv_area.setText(list.get(position).getAdName());
    }

    @Override  public int getItemCount() {
        return list.size();
    }

    @Override public void onRefresh(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final RefreshView refreshView) {
        if (listener != null) { listener.onRefresh();  }
    }

    @Override public void onLoadMore(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final LoadMoreView loadMoreView) {
        if (listener != null) { listener.onLoadMore();  }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int position;
        TextView tv_address;
        TextView tv_area;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_area = (TextView) itemView.findViewById(R.id.tv_area);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{ void onItemClick(int position);  }
}
