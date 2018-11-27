package www.qisu666.com.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;

 //为下拉刷新写的基类
public abstract class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder> extends PullRefreshLoadRecyclerView.LoadRefreshAdapter<T>{
}
