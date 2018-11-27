package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.com.R;

import java.util.List;

 //品牌类型适配器
public class BrandTypeAdapter extends BaseAdapter {

    private Context context;
    private List<String> types;
    public BrandTypeAdapter(Context context, List<String> types) {
        this.context = context;
        this.types = types;
    }

    @Override public int getCount() {
        return types.size()-3;
    }
    @Override public Object getItem(int position) {
        return types.get(position);
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
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
        viewHolder.tv_type.setText(types.get(position+3));
        if(position==getCount()-1){
            viewHolder.divide.setVisibility(View.GONE);
        } else {
            viewHolder.divide.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class ViewHolder { TextView tv_type; View divide;  }

}
