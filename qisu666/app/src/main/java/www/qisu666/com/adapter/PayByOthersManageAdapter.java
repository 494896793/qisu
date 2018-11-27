package www.qisu666.com.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

//他人代付的适配
public class PayByOthersManageAdapter extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<PayByOthersManageAdapter.MyViewHolder> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    private Context context;
    private List<Map<String, Object>> list;

    private OnLoadRefreshCallBack listener;
    private OnDeleteClickListener onDeleteClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public PayByOthersManageAdapter(Context context, OnLoadRefreshCallBack listener, List<Map<String, Object>> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @Override
    public PayByOthersManageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =  new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_pay_by_others_manage, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PayByOthersManageAdapter.MyViewHolder holder, int position) {
        holder.position = position;
        holder.tv_name.setText(list.get(position).get("user_name").toString());
        holder.tv_phone.setText(list.get(position).get("mobile_no").toString());
        if(position==getItemCount()-1){
            holder.divide.setVisibility(View.GONE);
        } else {
            holder.divide.setVisibility(View.VISIBLE);
        }

        mItemManger.bindView(holder.itemView, position);

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

    /**
     * swipeAdapter需实现的方法
     * @param position
     */
    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        int position;

        SwipeLayout swipeLayout;
        TextView tv_name;
        TextView tv_phone;
        View divide;
        TextView tv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottom_wrapper));

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
            divide = itemView.findViewById(R.id.divide);

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                    layout.setSwipeEnabled(true);
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {
                    closeAllExcept(layout);
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    layout.setSwipeEnabled(false);
                    layout.close(true);
                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(onDeleteClickListener!=null){
                    onDeleteClickListener.onDeleteClick(position, swipeLayout);
                }
                }
            });

            swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    swipeLayout.close();
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position, swipeLayout);
                    }
                    return true;
                }
            });

        }

    }

    public interface OnDeleteClickListener{
        void onDeleteClick(int position, SwipeLayout swipeLayout);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position, SwipeLayout swipeLayout);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public Drawable bindEmptyViewDrawable() {
        return context.getResources().getDrawable(R.mipmap.ic_empty_other);
    }
}
