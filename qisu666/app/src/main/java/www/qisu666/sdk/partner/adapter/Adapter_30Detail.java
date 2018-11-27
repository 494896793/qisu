package www.qisu666.sdk.partner.adapter;

/**
 * 717219917@qq.com 2018/4/24 17:07.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.common.utils.StringUtil;

/**30天详情*/
public class Adapter_30Detail extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;
    private boolean isFromStationInfoActivity;

    public Adapter_30Detail(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public Adapter_30Detail(Context context, ArrayList<String> list,boolean isFromStationInfoActivity) {
        this.context = context;
        this.list = list;
        this.isFromStationInfoActivity = isFromStationInfoActivity;
    }

    public void setList(ArrayList<String> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override public int getCount() {
        return list==null?0:list.size();
    }

    @Override  public Object getItem(int position) {
        return list==null?0:list.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_carbuy_30detail, null);
            holder = new ViewHolder();
            holder.carbuy_money_txt = (TextView) convertView.findViewById(R.id.carbuy_money_txt);
            holder.carbuy_date_txt = (TextView) convertView.findViewById(R.id.carbuy_date_txt);
            holder.item_30detail_layout=(LinearLayout) convertView.findViewById(R.id.item_30detail_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String str = list.get(position);
           String befor=str.substring(0,str.indexOf(","));
           String after=str.substring(str.indexOf(",")+1,str.length());
        holder.carbuy_money_txt.setTextColor(context.getResources().getColor(R.color.text_green_wx));
        holder.carbuy_money_txt.setText("+"+after);//钱
        holder.carbuy_date_txt.setText(""+befor);  //日期
        holder.item_30detail_layout.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { LogUtil.e("点击事件");   } });
//        holder.tv_start_time.setText(StringUtil.formatHour(list.get(position).get("start_time").toString()));
//        holder.tv_end_time.setText(StringUtil.formatHour(list.get(position).get("end_time").toString()));
//        holder.tv_kilowatt_hour_fee.setText(list.get(position).get("charge_price").toString()+"元/度");
//        holder.tv_kilowatt_hour_fee.setText(Float.valueOf(list.get(position).get("charge_price").toString())/ ConstantUtil.PAY_TO_DIVIDE+"元/度");
//        holder.tv_kilowatt_hour_service_fee.setText(Float.valueOf(list.get(position).get("service_price").toString())/ConstantUtil.PAY_TO_DIVIDE+"元/度");
        return convertView;
    }

    class ViewHolder{
        TextView carbuy_money_txt;
        TextView carbuy_date_txt;
        LinearLayout item_30detail_layout;
    }

}
