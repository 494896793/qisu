package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.com.R;

//汽车拍照适配器
public class CarLicenceAdapter extends BaseAdapter {
    private Context context;
    private String[] provinces;
    private int selected;
    public CarLicenceAdapter(Context context, String[] provinces) {
        this.context = context;
        this.provinces = provinces;
    }
    @Override public int getCount() {
        return provinces.length;
    }
    @Override public Object getItem(int position) {
        return provinces[position];
    }
    @Override  public long getItemId(int position) {
        return position;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_car_licence, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv);
        textView.setText(provinces[position]);
        if(position == selected){
            textView.setSelected(true);
        }
        return convertView;
    }

    public void setItemSelected(int selected){
        this.selected = selected;
    }

}
