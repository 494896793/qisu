package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import www.qisu666.com.R;
import www.qisu666.com.util.GlobalParams;

 //头像适配器
public class PortraitAdapter extends BaseAdapter {
    private Context context;
    private int checked;
    public PortraitAdapter(Context context, int checked) {
        this.context = context;
        this.checked = checked;
    }
    @Override public int getCount() {
        return GlobalParams.icons.length;
    }
    @Override public Object getItem(int position) {
        return GlobalParams.icons[position];
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_portrait, null);
        ImageView img_portrait = (ImageView) convertView.findViewById(R.id.img_portrait);
        img_portrait.setImageResource(GlobalParams.icons[position]);
        ImageView img_checked = (ImageView) convertView.findViewById(R.id.img_checked);
        if(position == checked){
            img_checked.setVisibility(View.VISIBLE);
        } else {
            img_checked.setVisibility(View.GONE);
        }
        return convertView;
    }

}

