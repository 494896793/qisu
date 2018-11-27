package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.model.MoneyDetailBean;

/**
 * 717219917@qq.com 2018/8/8 10:16.
 */
public class MoneyListViewAdapter extends BaseAdapter {

    private List<MoneyDetailBean> list;
    private Context mContext;

    public     MoneyListViewAdapter(List<MoneyDetailBean> list,Context mContext){
        this.list=list;
        this.mContext=mContext;
    }

    public void refreshData(List<MoneyDetailBean> list){
                  this.list=list;
                  notifyDataSetChanged();
    }

    public List<MoneyDetailBean> getData(){
        return list;
    }

    public void loadData(List<MoneyDetailBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoneyListViewViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.money_detail_item_layout,null);
            holder=new MoneyListViewViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MoneyListViewViewHolder) convertView.getTag();
        }
        holder.tx_money.setText(list.get(position).getChargeMoney());
        holder.tx_date.setText(list.get(position).getOperationDate().substring(0,10));
        holder.tx_now_money.setText(list.get(position).getAcctTotal());
        holder.tx_pay.setText(list.get(position).getSourceNo());
        return convertView;
    }

    static class MoneyListViewViewHolder {
        TextView tx_money;
        TextView tx_pay;
        TextView tx_now_money;
        TextView tx_date;

        public  MoneyListViewViewHolder(View itemView){
            tx_money=itemView.findViewById(R.id.tx_money);
            tx_pay=itemView.findViewById(R.id.tx_pay);
            tx_date=itemView.findViewById(R.id.tx_date);
            tx_now_money=itemView.findViewById(R.id.tx_now_money);
        }

    }

}
