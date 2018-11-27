package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.com.R;

import java.util.List;
import java.util.Map;

//其他支付方式的适配器
public class OthersPayAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> othersInfos;
    public OthersPayAdapter(Context context, List<Map<String,Object>> othersInfos) {
        this.context = context;
        this.othersInfos = othersInfos;
    }

    @Override  public int getCount() {
        return othersInfos.size();
    }
    @Override public Object getItem(int position) {
        return othersInfos.get(position);
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override  public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_others_pay, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.divide = convertView.findViewById(R.id.divide);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(othersInfos.get(position).get("angent_name").toString());
        if(position==getCount()-1){
            viewHolder.divide.setVisibility(View.GONE);
        } else {
            viewHolder.divide.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        View divide;
    }
}
