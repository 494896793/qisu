package www.qisu666.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import www.qisu666.common.utils.StringUtil;
import www.qisu666.com.R;
import www.qisu666.com.activity.NotificationDetailActivity;
import www.qisu666.com.util.OnLoadRefreshCallBack;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

//通知适配器
public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;

    public NotificationAdapter(Context context, List<Map<String, Object>> mLists) {
        this.context = context;
        this.list = mLists;
    }

    public List<Map<String, Object>> getData(){
             return list;
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

        NotificationAdapter.MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rv_notification, parent, false);
            holder = new NotificationAdapter.MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NotificationAdapter.MyViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(list.get(position).get("title").toString());
        holder.tv_time.setText(list.get(position).get("jpushTime").toString().substring(0,10));
            holder.tv_content.setText(list.get(position).get("content").toString().replace(" ", ""));
        final int posi = position;
        holder.tv_title.setTag(position+"");

        return convertView;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder  {
        ImageView img_tag;
        TextView tv_title;
        TextView tv_time;
        TextView tv_content;
        int position;

        public MyViewHolder(View itemView) {

//            img_tag = (ImageView) itemView.findViewWithTag(R.id.img_tag);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NotificationDetailActivity.class);
                    intent.putExtra("notification", list.get(Integer.valueOf(tv_title.getTag().toString())).toString());
                    intent.putExtra("position", tv_title.getTag().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}
