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

 //车辆信息适配器
public class CarInfoAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> carInfos;

    public CarInfoAdapter(Context context, List<Map<String,Object>> carInfos) {
        this.context = context;
        this.carInfos = carInfos;
    }

    @Override public int getCount() {
        return carInfos.size();
    }
    @Override public Object getItem(int position) {
        return carInfos.get(position);
    }
    @Override public long getItemId(int position) {
        return position;
    }

    @Override  public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_brand_type, null);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.divide = convertView.findViewById(R.id.divide);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_type.setText(carInfos.get(position).get("cars_brand").toString());
        if(position==getCount()-1){
            viewHolder.divide.setVisibility(View.GONE);
        } else {
            viewHolder.divide.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_type;
        View divide;
    }
}
