package www.qisu666.com.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.de.hdodenhof.circleimageview.CircleImageView;
import www.qisu666.com.R;
import www.qisu666.com.util.GlobalParams;
import www.qisu666.com.util.MatchUtils;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.widget.StarBar;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;

import java.util.List;
import java.util.Map;

 //电站评论适配器
public class StationCommentAdapter extends BaseAdapter  {
    private Context context;
    private List<Map<String, Object>> list;
    public StationCommentAdapter(Context context,  List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

     @Override
     public int getCount() {
         return list.size();
     }

     @Override
     public Object getItem(int position) {
         return list.get(position);
     }

     public void refreshData(List<Map<String, Object>> list){
         this.list=list;
         notifyDataSetChanged();
     }

     public void loadData(List<Map<String, Object>> list){
         this.list.addAll(list);
         notifyDataSetChanged();
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         CommentViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_lv_comment, null);
            holder=new CommentViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (CommentViewHolder) convertView.getTag();
        }
         holder.starbar_comment_detail.setIsTouchAble(false);
//        holder.starbar_comment_detail.setStarMark(4.0f);
         String user_name = list.get(position).get("userName").toString();
         if(MatchUtils.matchMobileNo(user_name)){
             user_name = user_name.substring(0,3) + "****" + user_name.substring(7,11);
         }
         holder.tv_user_name.setText(user_name);
         holder.civ_comment_user_icon.setImageResource(GlobalParams.icons[Integer.parseInt(list.get(position).get("picture").toString())]);
         holder.tv_comment_detail.setText(list.get(position).get("content").toString());
         holder.tv_comment_score.setText(list.get(position).get("grade").toString() + "分");
         holder.starbar_comment_detail.setStarMark(Float.parseFloat(list.get(position).get("grade").toString()));
         holder.tv_comment_time.setText(list.get(position).get("createTime").toString());
         return convertView;
     }

    class CommentViewHolder  {
        CircleImageView civ_comment_user_icon;
        TextView tv_user_name;
        TextView tv_comment_score;
        TextView tv_comment_detail;
        TextView tv_comment_time;
        StarBar starbar_comment_detail;
        public CommentViewHolder(View itemView) {
            civ_comment_user_icon = (CircleImageView) itemView.findViewById(R.id.civ_comment_user_icon);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_comment_score = (TextView) itemView.findViewById(R.id.tv_comment_score);
            tv_comment_detail = (TextView) itemView.findViewById(R.id.tv_comment_detail);
            tv_comment_time = (TextView) itemView.findViewById(R.id.tv_comment_time);
            starbar_comment_detail = (StarBar) itemView.findViewById(R.id.starbar_comment_detail);
        }
    }
}
