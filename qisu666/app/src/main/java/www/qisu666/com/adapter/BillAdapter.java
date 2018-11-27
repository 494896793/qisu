package www.qisu666.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;
import www.qisu666.sdk.mytrip.bean.ChongDianJiLu;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/1.
 */
public class BillAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;

    public BillAdapter(Context context, List<Map<String, Object>> mLists) {
        this.context = context;
        this.list = mLists;
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


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rv_bill, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        final int posi = position;

        holder.tv_time.setText(StringUtil.formatDate(list.get(position).get("operationDate").toString()));
        holder.tv_balance.setText(list.get(position).get("acctTotal").toString());
        holder.tv_from.setText(list.get(position).get("sourceNo").toString());
        holder.tv_charge.setText(list.get(position).get("chargeMoney").toString());
        holder.tv_charge_gift.setText(" (赠送" + list.get(position).get("feeGift").toString() + "元)");
//        String giftFee = list.get(position).get("feeGift").toString();
//        if (!TextUtils.isEmpty(giftFee) && !giftFee.equals("0")) {
//            int gitFeeLength = giftFee.length();
//            if (gitFeeLength > 2) {
//                holder.tv_charge_gift.setText(" (赠送" + giftFee.substring(0, gitFeeLength - 2) + ")");
//            }
//        } else {
//            holder.tv_charge_gift.setText("");
//        }

        return convertView;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //重用view
    public class MyViewHolder {
        int position;
        TextView tv_charge;
        TextView tv_balance;
        TextView tv_from;
        TextView tv_time;
        TextView tv_charge_gift;

        public MyViewHolder(View itemView) {
            tv_charge = (TextView) itemView.findViewById(R.id.tv_charge);
            tv_balance = (TextView) itemView.findViewById(R.id.tv_balance);
            tv_from = (TextView) itemView.findViewById(R.id.tv_from);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_charge_gift = (TextView) itemView.findViewById(R.id.tv_charge_gift);

        }
    }
}
