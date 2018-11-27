package www.qisu666.sdk.amap.carShare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.xutils.common.util.LogUtil;

import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.model.PositionBean;
import www.qisu666.common.utils.ToastUtil;


/**
 * 717219917@qq.com ${DATA} 10:41.  还车网点列表
 */
public class Adapter_car_share_returnLock extends BaseAdapter{
    boolean isfirst=true;//是否第一次
    private int state[];//记录点击的状态，1为选中状态，0为未选中状态
    private int pre_pos=0;//记录当前点击的位置，供我们在下次onItemClicked的时候还原st

    private Context context;
    private List<PositionBean> list;
    private  OnItemClick onItemClick=null;//定义一个接口 在外部处理点击事件  适配器内部不进行处理

    public interface OnItemClick{ void onItemClick(int position,View  parent);  }

    public void setOnItemClick( OnItemClick onItemClick2){
        this.onItemClick = onItemClick2;
        LogUtil.e("当前点击设置的item："+onItemClick);
    }

    public Adapter_car_share_returnLock(Context con,List<PositionBean> bean){
                   super();
                    isfirst=true;
                   context=con;
                   list=bean;
    }

    public void setList(List<PositionBean> list_tmp){
         list=list_tmp;
         notifyDataSetChanged();
    }

    public void setSelect(int posi){


   notifyDataSetChanged();
    }

    @Override public int getCount() {    if (list==null){return 0;} return list.size();  }
    @Override public PositionBean getItem(int i) { return  list.get(i);  }
    @Override public long getItemId(int i) { return i; }

    @Override public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car_share_returnlock, null);//
            viewHolder.car_share_returnlock_txt = (TextView) convertView.findViewById(R.id.car_share_returnlock_txt);
            viewHolder.car_share_returnlock_img = (ImageView) convertView.findViewById(R.id.car_share_returnlock_img);
            viewHolder.car_share_returnlock_layout = (LinearLayout)convertView.findViewById(R.id.car_share_returnlock_layout);//拿到当前布局的item
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int position = i;//当前点击的postion
        final View   myConverView =convertView;
         PositionBean positionBean = list.get(i);
        viewHolder.car_share_returnlock_txt.setText(positionBean.stationName);//还车网点名字
//        Picasso.with(context).load(R.mipmap.cs_car_place) .fit() .noFade().centerInside() .into( viewHolder.car_share_returnlock_img);

        viewHolder.car_share_returnlock_layout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                LogUtil.e("当前点击的item："+position);
                LogUtil.e("当前点击的item："+onItemClick);
                isfirst=false;
                pre_pos=position;
//                if(onItemClick != null) {
//                    onItemClick.onItemClick(position,myConverView);
//                }
              notifyDataSetChanged();
            }
        });
//          刷新item点击状态  第一个更新状态
        if (isfirst){
            viewHolder.car_share_returnlock_img.setImageResource(R.mipmap.return_maplist_item_0);
        }else {
            if(position==pre_pos){
//            viewHolder.car_share_returnlock_layout.setBackgroundColor(context.getResources().getColor(R.color.bg_navi_topic));
                viewHolder.car_share_returnlock_img.setImageResource(R.mipmap.return_maplist_item_1);
            }else {
//            viewHolder.car_share_returnlock_layout.setBackgroundColor(context.getResources().getColor(R.color.bg_primary));
                viewHolder.car_share_returnlock_img.setImageResource(R.mipmap.return_maplist_item_0);
            }
        }



        return convertView;
    }

    class ViewHolder {
        TextView car_share_returnlock_txt;
        ImageView car_share_returnlock_img;
        LinearLayout car_share_returnlock_layout;//单个的布局
    }


    public void changeState(int position ){
        if(state==null){
            state=new int[getCount()];
            state[position]=1;
            notifyDataSetInvalidated(position);
        }else {
            state[pre_pos]=0;
            state[position]=1;
            pre_pos=position;
            notifyDataSetInvalidated(position);
        }

//        /**
//         * 设置点击效果背景
//         */
//        for(int i=0;i<parent.getCount();i++){
//
//            /**
//             * 因为parent这里是listview，所以parent.getChildAt(i)就是一个一个的item
//             */
//            View item=parent.getChildAt(i);
//
//            /**
//             * 找到item里的每一个元素再进行相关操作
//             */
//            TextView categoryNameTextView = (TextView)(item.findViewById(R.id.category_name_tv));
//
//
//            if (position == i) {
//                item.setBackgroundResource(R.color.menu_item_press);
//                categoryNameTextView.setTextColor(getResources().getColor(R.color.menu_text_press));
//            } else {
//                item.setBackgroundResource(R.color.menu_item_normal);
//                categoryNameTextView.setTextColor(getResources().getColor(R.color.menu_text_normal));
//            }
//        }



    }

    private void notifyDataSetInvalidated(int position) {
        super.notifyDataSetInvalidated();
        state[pre_pos]=0;
        state[position]=1;
        pre_pos=position;
    }

//    private Context context;
//    private List<String> types;
//    public BrandTypeAdapter(Context context, List<String> types) {
//        this.context = context;
//        this.types = types;
//    }

//    @Override public int getCount() {
//        return types.size()-3;
//    }
//    @Override public Object getItem(int position) {
//        return types.get(position);
//    }
//    @Override public long getItemId(int position) {
//        return position;
//    }
//    @Override public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_brand_type, null);
//            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
//            viewHolder.divide = convertView.findViewById(R.id.divide);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.tv_type.setText(types.get(position+3));
//        if(position==getCount()-1){
//            viewHolder.divide.setVisibility(View.GONE);
//        } else {
//            viewHolder.divide.setVisibility(View.VISIBLE);
//        }
//        return convertView;
//    }
//    class ViewHolder { TextView tv_type; View divide;  }




//    private List<Map<String, Object>> list;
//    private OnLoadRefreshCallBack listener;
//    private www.qisu666.com.adapter.CollectionAdapter.OnNaviClickListener onNaviClickListener;
//    private www.qisu666.com.adapter.CollectionAdapter.OnItemClick onItemClick;
//
//    public Adapter_car_share_returnLock(Context context, OnLoadRefreshCallBack listener, www.qisu666.com.adapter.CollectionAdapter.OnItemClick onItemClick, List<Map<String, Object>> list) {
//        this.context = context;
//        this.listener = listener;
//        this.onItemClick = onItemClick;
//        this.list = list;
//    }
//
//    @Override public www.qisu666.com.adapter.CollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        www.qisu666.com.adapter.CollectionAdapter.MyViewHolder holder =  new www.qisu666.com.adapter.CollectionAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_collection, parent, false));
//        return holder;
//    }
//
//    @Override public void onBindViewHolder(www.qisu666.com.adapter.CollectionAdapter.MyViewHolder holder, final int position) {
//        holder.tv_nearby_title.setText(list.get(position).get("station_name").toString());
//        holder.tv_nearby_fee.setText(list.get(position).get("charge_fee_per").toString()+"元/度");
//        holder.tv_nearby_addr.setText(list.get(position).get("charge_address").toString());
////        double b = new BigDecimal(list.get(position).get("charge_distance").toString()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//        holder.tv_nearby_distance.setText(list.get(position).get("charge_distance").toString() + "km");
//        holder.tv_nearby_free_fast.setText("空闲"+list.get(position).get("pile_fast_num_free").toString());
//        holder.tv_nearby_free_slow.setText("空闲"+list.get(position).get("pile_slow_num_free").toString());
//        String method = list.get(position).get("charge_method").toString();
////        holder.tv_nearby_type.setText(method.equals("00") ? "交流" : method.equals("01") ? "直流" : "直流/交流");
//
//        holder.tv_nearby_type.setText("总桩数"+list.get(position).get("total_pile_count").toString());
//
////        holder.tv_charging_duration.setText(list.get(position).get("charge_quantity").toString()+"度");
//        holder.rl_nearby_charge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onNaviClickListener != null) {
//                    onNaviClickListener.onNaviClick(position);
//                }
//            }
//        });
//        holder.station_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onItemClick != null) {
//                    onItemClick.onItemClick(position);
//                }
//            }
//        });
//    }
//    @Override public int getItemCount() {
//        return list.size();
//    }
//    @Override  public void onRefresh(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final RefreshView refreshView) {
//        if (listener != null) { listener.onRefresh();  }
//    }
//    @Override public void onLoadMore(PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView, final LoadMoreView loadMoreView) {
//        if (listener != null) { listener.onLoadMore();  }
//    }
//
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_nearby_title;
//        TextView tv_nearby_fee;
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            tv_nearby_title = (TextView) itemView.findViewById(R.id.tv_nearby_title);
//            tv_nearby_title = (TextView) itemView.findViewById(R.id.tv_nearby_title);
//
//        }
//    }
//    public interface OnNaviClickListener{ void onNaviClick(int position); }
//    public void setOnNaviClickListener(www.qisu666.com.adapter.CollectionAdapter.OnNaviClickListener onNaviClickListener){ this.onNaviClickListener = onNaviClickListener;  }
//
//    public interface OnItemClick{ void onItemClick(int position);  }
//    public void setOnItemClick(www.qisu666.com.adapter.CollectionAdapter.OnItemClick onItemClick){
//        this.onItemClick = onItemClick;
//    }

//    @Override  public Drawable bindEmptyViewDrawable() {
//        return ContextCompat.getDrawable(context, R.mipmap.ic_empty_collection);
//    }
}

