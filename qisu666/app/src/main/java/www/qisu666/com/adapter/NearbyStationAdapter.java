package www.qisu666.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

import www.qisu666.com.R;
import www.qisu666.com.activity.StationInfoActivity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//附近的电站
public class NearbyStationAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private OnGuideClickListener onGuideClickListener;

    public NearbyStationAdapter(Context context, OnGuideClickListener onGuideClickListener, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.onGuideClickListener = onGuideClickListener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list == null ? null : list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NearbyStationAdapter.MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rv_collection, parent, false);
            holder = new NearbyStationAdapter.MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NearbyStationAdapter.MyViewHolder) convertView.getTag();
        }
        final int posi = position;

        try {
            final Map m = list.get(position);
            Log.e("aaaa", "m:" + m);
            holder.tv_nearby_title.setText(m.get("stationName").toString());

            BigDecimal money_str = new BigDecimal(m.get("chargeFeePer").toString());
            double money = money_str.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            holder.tv_nearby_fee.setText(String.valueOf(money) + "元/度");
            holder.tv_nearby_addr.setText(m.get("chargeAddress").toString());
            BigDecimal b = new BigDecimal(m.get("chargeDistance").toString());
            double dis = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            holder.tv_nearby_distance.setText(String.valueOf(dis) + "km");
            holder.tv_nearby_free_fast.setText("空闲" + m.get("pileFastNumFree").toString());
            holder.tv_nearby_free_slow.setText("空闲" + m.get("pileSlowNumFree").toString());
//        viewHolder.tv_nearby_type.setText(m.get("charge_method").toString().equals("00") ? "交流" : m.get("charge_method").toString().equals("01") ? "直流" : "直流/交流");
            holder.tv_nearby_type.setText("总桩数" + m.get("totalPileCount").toString());
            holder.rl_nearby_charge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGuideClickListener.onGuideClick(posi);
                }
            });
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StationInfoActivity.class);
                    intent.putExtra("station_id", list.get(posi).get("stationId").toString());
                    intent.putExtra("station_name", list.get(posi).get("stationName").toString());
                    context.startActivity(intent);
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return convertView;
    }

    public void loadList(List<Map<String,Object>> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        Log.e("aaaa", "setList" + list.size());
        notifyDataSetChanged();
    }

    public class MyViewHolder {
        int position;
        TextView tv_nearby_title;
        TextView tv_nearby_fee;
        TextView tv_nearby_addr;
        TextView tv_nearby_distance;
        TextView tv_nearby_free_fast;
        TextView tv_nearby_free_slow;
        TextView tv_nearby_type;
        View rl_nearby_charge;
        LinearLayout linear;

        public MyViewHolder(View itemView) {
            linear=itemView.findViewById(R.id.linear);
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

    public interface OnGuideClickListener {
        void onGuideClick(int position);
    }

}
