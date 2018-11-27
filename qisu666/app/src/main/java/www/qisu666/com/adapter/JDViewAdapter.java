package www.qisu666.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.activity.LoginActivity;
import www.qisu666.com.activity.NotificationDetailActivity;
import www.qisu666.com.entity.AdverNotice;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.JDAdverView;

/**
 * 717219917@qq.com 2018/8/21 16:53.
 */
public class JDViewAdapter {

    private List<AdverNotice> mDatas;
    private Context mContext;
    private List<Map<String,Object>> list;

    public JDViewAdapter(List<AdverNotice> mDatas,Context mContext,List<Map<String,Object>> list) {
        this.mDatas = mDatas;
        this.mContext=mContext;
        this.list=list;
        if (mDatas == null || mDatas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }
    /**
     * 获取数据的条数
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     * @param position
     * @return
     */
    public AdverNotice getItem(int position) {
        return mDatas.get(position);
    }
    /**
     * 获取条目布局
     * @param parent
     * @return
     */
    public View getView(JDAdverView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.jdview_item, null);
    }

    /**
     * 条目数据适配
     * @param view
     * @param data
     */
    public void setItem(final View view, final AdverNotice data) {
        TextView tv = (TextView) view.findViewById(R.id.ll_index_message_content);
        tv.setText(data.title);
        TextView tag = (TextView) view.findViewById(R.id.ll_index_message_time);
        tag.getBackground().setAlpha(50);
        tag.setText(data.url);
        view.setTag(data);
//你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserParams.INSTANCE.getUser_id()==null){
                    mContext.startActivity(new Intent(mContext,LoginActivity.class));
                }else{
                    if(v.getTag()!=null&&list!=null) {
                        Intent messageIntent = new Intent(mContext, NotificationDetailActivity.class);
                        messageIntent.putExtra("notification", list.get(Integer.valueOf(((AdverNotice) v.getTag()).position)).toString());
                        messageIntent.putExtra("position", ((AdverNotice) v.getTag()).position);
                        mContext.startActivity(messageIntent);
                    }
                }
            }
        });
    }

}
