package www.qisu666.sdk.partner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.util.UserParams;
import www.qisu666.sdk.partner.Activity_CarBuy;
import www.qisu666.sdk.partner.Activity_CarBuyDetail;
import www.qisu666.sdk.partner.Activity_ContractDetail;
import www.qisu666.sdk.partner.bean.Bean_MyInvest;
import www.qisu666.sdk.partner.bean.ProductList;

/**
 * 717219917@qq.com ${DATA} 10:31.
 */
public class Adapter_MyInvest extends BaseAdapter {

   private Context context;
    private List<Bean_MyInvest.UserSubscribeList> list;

    public Adapter_MyInvest(Context context , List<Bean_MyInvest.UserSubscribeList> list) {
        this.context = context;
        this.list = list;
    }

    //更新数据
    public void setList(List<Bean_MyInvest.UserSubscribeList> list_rec){
        list=list_rec;
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return list==null?0:list.size();
    }

    @Override public Object getItem(int i) {
        return list==null?null:list.get(i);
    }

    @Override  public long getItemId(int i) {
        return i;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder_Term holder;
        if (convertView == null) {
            convertView =   LayoutInflater.from(context).inflate(R.layout.item_myinvest, parent, false);
            holder = new MyViewHolder_Term(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder_Term) convertView.getTag();
        }
         final int posi=position;
        holder.position = position;
        holder.myinvest_carname.setText(list.get(position).getProductTitle());       //车牌号
        holder.myinvest_id.setText(++position+"");
       try{ holder.myinvest_statues_hetong.setText(list.get(position).getSubStatus().equals("1")?"合同状态:不可解除":"合同状态:可以解除");}catch (Throwable t){t.printStackTrace();}
       try{ holder.myinvest_buy_time.setText("认购时间:"+new SimpleDateFormat("yyyy-MM-dd").format(list.get(position).getCreatedTime()) );}catch (Throwable t){t.printStackTrace();}

       try {
        if (list.get(posi).getSubType() == 2) {
             try {  holder.myinvest_return_time.setVisibility(View.GONE);  } catch (Throwable t) { t.printStackTrace();  }
        } else {
              try { holder.myinvest_return_time.setText("分红到账时间:" + new SimpleDateFormat("yyyy-MM-dd").format(list.get(position).getFirstPhaseTime())); } catch (Throwable t) { t.printStackTrace();  }
            }
       }catch (Throwable t){t.printStackTrace();  }

       try{ holder.myinvest_remove_time.setText("解除合同时间:"+new SimpleDateFormat("yyyy-MM-dd").format(list.get(position).getContractExpiresTime()));}catch (Throwable t){t.printStackTrace();}


        holder.myinvest_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                LogUtil.e("点击获取到的车辆："+list.get(posi).getProductCode());//需要传递的产品编码
                LogUtil.e("点击获取到的车辆："+list.get(posi).getSubType());//需要传递的产品编码
                try {
                    LogUtil.e("点击获取到的车辆：" + list.get(posi).getProductCode());//需要传递的产品编码
                    Intent intent = new Intent(context, Activity_CarBuyDetail.class);          //认购详情
                    intent.putExtra("productCode", list.get(posi).getProductCode());
                    intent.putExtra("subCode",list.get(posi).getSubCode());
                    intent.putExtra("subType",list.get(posi).getSubType()+"");
                    intent.putExtra("userCode", UserParams.INSTANCE.getUser_id());
                    context.startActivity(intent);
                }catch (Throwable t){t.printStackTrace();}
            }
        });

        return convertView;
    }



    static class MyViewHolder_Term   {
        int position;
        ImageView myinvest_carimg;
        TextView myinvest_carname;
        TextView myinvest_id;
        TextView myinvest_statues_hetong;
        TextView myinvest_statues;
        TextView myinvest_buy_time;
        TextView myinvest_return_time;
        TextView myinvest_remove_time;
        LinearLayout myinvest_item_layout;

        public MyViewHolder_Term(View itemView) {
            myinvest_carimg = (ImageView) itemView.findViewById(R.id.myinvest_carimg);   //汽车图标
            myinvest_carname = (TextView) itemView.findViewById(R.id.myinvest_carname);  //汽车名字
            myinvest_id = (TextView) itemView.findViewById(R.id.myinvest_id);            //list的顺序 1 2 3
            myinvest_statues_hetong = (TextView) itemView.findViewById(R.id.myinvest_statues_hetong);              //合同状态
            myinvest_statues = (TextView) itemView.findViewById(R.id.myinvest_statues);                            //投资状态
            myinvest_buy_time = (TextView) itemView.findViewById(R.id.myinvest_buy_time);                          //认购时间
            myinvest_return_time = (TextView) itemView.findViewById(R.id.myinvest_return_time);                    //分红到账时间
            myinvest_remove_time = (TextView) itemView.findViewById(R.id.myinvest_remove_time);                    //合同解除时间
            myinvest_item_layout = (LinearLayout) itemView.findViewById(R.id.myinvest_item_layout);               //单个item布局
        }
    }
}
