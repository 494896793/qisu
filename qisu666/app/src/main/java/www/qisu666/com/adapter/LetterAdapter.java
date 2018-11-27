package www.qisu666.com.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import www.qisu666.common.utils.DensityUtil;
import www.qisu666.com.R;

//银行卡(通信证)适配器
public class LetterAdapter extends BaseAdapter {
    private Context context;
    private char[] letters = new char[27];
    private int lvHeight;

    public LetterAdapter(Context context) {
        this.context = context;
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        lvHeight = DensityUtil.getScreenHeight(context) - context.getResources().getDimensionPixelOffset(R.dimen.title_height) - result;
        initData();
    }

    private void initData() {
        for(char i='A';i<='Z';i++){
            letters[i-65] = i;
        }
        letters[26] = '#';
    }

    @Override public int getCount() { return letters.length;  }
    @Override public Object getItem(int position) {
        return letters[position];
    }
    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new AbsListView.LayoutParams(lvHeight/27, lvHeight/27));
        tv.setText(String.valueOf(letters[position]));
        tv.setBackgroundColor(context.getResources().getColor(R.color.bg_white));
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
