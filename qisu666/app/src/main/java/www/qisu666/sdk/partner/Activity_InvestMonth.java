package www.qisu666.sdk.partner;

/**
 * 717219917@qq.com 2018/4/23 16:18.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.widget.CustomListView;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.adapter.Adapter_30Detail;
import www.qisu666.sdk.partner.bean.Bean_MyInvest;
import www.qisu666.sdk.partner.bean.Bean_RecMoney;

/**30天投资详情 list*/
public class Activity_InvestMonth  extends BaseActivity{
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.img_title_left)ImageView img_title_left;
    @BindView(R.id.investmonty_txt)TextView investmonty_txt;//整个列表的txt
    List<Bean_MyInvest.MonthProfitList> monthProfitList;    //当月清单list
    Bean_RecMoney bean_recMoney=new Bean_RecMoney();                             //封装已经分红和未分红的数据
    CustomListView listView;
    ArrayList<String> list = new ArrayList<>();
    Adapter_30Detail adapter_30Detail;


    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setView(R.layout.activity_investmonth);//30天投资list布局
        initView();

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onInvestMonth(Bean_RecMoney bean_recMoney_tmp) {
        LogUtil.e("接收到当月详情数据----"+bean_recMoney.isCustomer);
        bean_recMoney=bean_recMoney_tmp;//接收数据

        if (bean_recMoney.isCustomer) {//已分红
            int count = bean_recMoney.monthProfitLists.size();
            for (int a = 0; a < count; a++) {
                list.add(simpleDateFormat.format(bean_recMoney.monthProfitLists.get(a).getProfitTime()) + ","+bean_recMoney.monthProfitLists.get(a).getProfit());
            }
        }else {
            int count = bean_recMoney.receiveMonthProfitLists.size();
            for (int a = 0; a < count; a++) {
                list.add(simpleDateFormat.format(bean_recMoney.receiveMonthProfitLists.get(a).getProfitTime()) + ","+bean_recMoney.receiveMonthProfitLists.get(a).getProfit());
            }
        }
//        adapter_30Detail= new Adapter_30Detail(this,list);
//        listView.setAdapter(adapter_30Detail);
//        adapter_30Detail.notifyDataSetChanged();


//        monthProfitList=bean_recMoney.monthProfitLists;
//        LogUtil.e("接收到当月详情数据"+monthProfitList.size());
//        initData(bean_recMoney);
    }


    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    void initView(){
        LogUtil.e("接收到未分红收益：initview----");
        tv_title.setText("当月收益详情");
        img_title_left.setOnClickListener(new View.OnClickListener() {  @Override  public void onClick(View view) { finish(); } });//左边返回按钮
        listView = (CustomListView) findViewById(R.id.listView);
        listView.setDispatch(true);
        View headerView = View.inflate(getApplicationContext(), R.layout.item_30detail_head,null);
        TextView item_30detail_head_txt = (TextView) headerView.findViewById(R.id.item_30detail_head_txt);

        try{
           if (bean_recMoney.isCustomer){
               item_30detail_head_txt.setText("当月已到账收益(汇总)："+bean_recMoney.monthProfit+"元");
           }else {
               item_30detail_head_txt.setText("当月未到账收益(汇总)："+bean_recMoney.receiveMonthProfit+"元");
           }
        }catch (Throwable t){t.printStackTrace(); item_30detail_head_txt.setText("当月已到账收益(汇总)：0元"); }


        listView.addHeaderView(headerView);
        initData();
   }

    @SuppressLint("SetTextI18n")
    void initData(){//true 已到账   false为未到账
        LogUtil.e("接收到未分红收益：initdata----");
//        investmonty_txt.setText("当月收益："+monthProfitList.size());
//        investmonty_txt.setText("当月收益：test");

        if (bean_recMoney==null){
            LogUtil.e("接收到未分红收益：initdata----bean为null-----");
        }
       try {
           if (bean_recMoney.isCustomer) {//已到账
               tv_title.setText("当月收益详情");
               String str = "分红已到账：\n";
               for (int a = 0; a < bean_recMoney.monthProfitLists.size(); a++) {
                   str += simpleDateFormat.format(bean_recMoney.monthProfitLists.get(a).getProfitTime()) + "," + bean_recMoney.monthProfitLists.get(a).getProfit() + "元\n";
               }
               investmonty_txt.setText(str);
           } else {
               LogUtil.e("接收到未分红收益：----");
               tv_title.setText("当月收益详情");
               String str = "分红未到账：\n";
               for (int a = 0; a < bean_recMoney.receiveMonthProfitLists.size(); a++) {
                   str += simpleDateFormat.format(bean_recMoney.receiveMonthProfitLists.get(a).getProfitTime()) + "," + bean_recMoney.receiveMonthProfitLists.get(a).getProfit() + "元\n";
               }
               investmonty_txt.setText(str);
           }

           adapter_30Detail = new Adapter_30Detail(this, list);
           listView.setAdapter(adapter_30Detail);
           adapter_30Detail.notifyDataSetChanged();
           if (list.isEmpty()) {
               ToastUtil.showToast("暂无数据!");
               finish();
           }
       }catch (Throwable t){t.printStackTrace(); ToastUtil.showToast("暂无数据!");finish();}

    }


    @Override protected void onResume() {
        super.onResume();
        LogUtil.e("接收到未分红收益：onresume----");
//        x.task().postDelayed(new Runnable() { @Override public void run() {  }  },2000);//
    }

    @Override  protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
