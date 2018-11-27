package www.qisu666.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.com.R;

import java.util.ArrayList;
import java.util.List;


 //品牌适配器  标准写法
public class BrandAdapter extends BaseAdapter {
    private Context context;
    private List<ArrayList<String>> brands;
    public BrandAdapter(Context context, List<ArrayList<String>> brands) {
        this.context = context;
        this.brands = brands;
    }

    @Override public int getCount() {
        return brands.size();
    }
    @Override public Object getItem(int position) {
        return brands.get(position);
    }
    @Override public long getItemId(int position) {
        return position;
    }
    @Override  public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_brand, null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String section = getSectionForPosition(position); //根据position获取分类的首字母
        if(position == getPositionForSection(section)){ //如果当前位置等于该分类首字母位置 ，则认为是第一次出现
            viewHolder.tv_letter.setVisibility(View.VISIBLE);
            viewHolder.tv_letter.setText(brands.get(position).get(0));
        }else{
            viewHolder.tv_letter.setVisibility(View.GONE);
        }
        viewHolder.tv_title.setText(brands.get(position).get(2));
        return convertView;
    }

    class ViewHolder {
        TextView tv_letter;
        TextView tv_title;
    }


    /** 根据ListView的当前位置获取分类的首字母 */
    public String getSectionForPosition(int position) {
        return brands.get(position).get(0);
    }

    /**  根据分类的首字母值获取其第一次出现该首字母的位置 */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String firstChar = brands.get(i).get(0);
            if (firstChar.equals(section)) {
                return i;
            }
        }
        return -1;
    }

}
