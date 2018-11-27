package www.qisu666.sdk.partner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.MyMessageUtils_Car;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DisplayUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.adapter.Adapter_MyInvest;
import www.qisu666.sdk.partner.adapter.Adapter_Term;
import www.qisu666.sdk.partner.bean.Bean_MyInvest;
import www.qisu666.sdk.partner.bean.Bean_RecMoney;
import www.qisu666.sdk.partner.bean.ProductList;
import www.qisu666.sdk.partner.bean.TermBean;
import www.qisu666.sdk.partner.widget.calendar.CalendarView;

/** 我的投资
 * 717219917@qq.com ${DATA} 11:32.
 */
public class Activity_MyInvest extends BaseActivity{

//    @BindView(R.id.tv_pre) TextView tv_pre;
//    @BindView(R.id.tv_next) TextView tv_next;
//    @BindView(R.id.tv_month) TextView tv_month;
//    @BindView(R.id.calendar) CalendarView calendar;//**日历控件*/
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.img_title_left)ImageView img_title_left;
    /**日历对象*/
    private Calendar cal;
    /**格式化工具*/
    private SimpleDateFormat formatter;
    /**日期*/
    private Date curDate;

      private int currentPage = 1;           //当前数据分页
    @BindView(R.id.myinvest_pull_to_refresh) PullToRefreshListView myinvest_pull_to_refresh;//整个外部布局
          TextView myinvest_total,myinvest_total_fen,myinvest_month,myinvest_month_no,myinvest_total_txt,myinvest_mingxi;//累计收益.总计份数.月份(已到账//未到账).  累计收益描述txt
    int scrolledX=0,scrolledY=0;                                    //listview滚动位置记录
     List<Bean_MyInvest.MonthProfitList> monthProfitList;              //当月收益清单list
     List<Bean_MyInvest.ReceiveMonthProfitList>receiveMonthProfitLists;

    private Adapter_MyInvest adapter;
    private List<Bean_MyInvest.UserSubscribeList> list=new ArrayList<>() ;        //认购列表
    Bean_MyInvest bean;                                                           //返回的bean
    ImageView myinvest_month_img_no,myinvest_month_img ;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_myinvest);
        initView();
    }

    private void initView() {
//        calendar = (CalendarView) findViewById(R.id.calendar);     cal = Calendar.getInstance();
        tv_title.setText("我的认购");
        img_title_left.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { finish();  }});
        myinvest_pull_to_refresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);  //同时上拉下拉
        //设置自定义下拉刷新动画文字  getLoadingLayoutProxy(true, false)，参数分别代表要设置上或下的文字修改
        ILoadingLayout headerLayout = myinvest_pull_to_refresh.getLoadingLayoutProxy(true, false);
        headerLayout.setPullLabel("向下拖动完成刷新...");
        headerLayout.setRefreshingLabel("正在加载新数据...");
        headerLayout.setReleaseLabel("释放完成刷新...");
        //设置底部刷新文字
        ILoadingLayout footLayout = myinvest_pull_to_refresh.getLoadingLayoutProxy(false, true);
        footLayout.setPullLabel("向上拽动完成刷新...");
        footLayout.setRefreshingLabel("正在刷新数据...");
        footLayout.setReleaseLabel("松开完成刷新...");
//        footLayout.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        View headerView = View.inflate(getApplicationContext(), R.layout.head_activity_myinvest,null);
          myinvest_month_img = (ImageView) headerView.findViewById(R.id.myinvest_month_img);
        myinvest_month_img.setOnClickListener(new View.OnClickListener() {//投资已到账
            @Override public void onClick(View view) {
                if (bean.getMonthProfit()<=0){  ToastUtil.showToast("暂无数据！");  return;  }
                Bean_RecMoney  bean_recMoney = new Bean_RecMoney();
                bean_recMoney.isCustomer=true;
                bean_recMoney.monthProfitLists=monthProfitList;
                bean_recMoney.monthProfit=Integer.parseInt(bean.getMonthProfit()+"");
                EventBus.getDefault().postSticky(bean_recMoney);
                Intent intent = new Intent(Activity_MyInvest.this,Activity_InvestMonth.class);
                startActivity(intent);

            }
        });
          myinvest_month_img_no = (ImageView) headerView.findViewById(R.id.myinvest_month_img_no);//投资未到账
        myinvest_month_img_no.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (bean.getReceiveMonthProfit()<=0){  ToastUtil.showToast("暂无数据！"); return;  }

                Bean_RecMoney  bean_recMoney = new Bean_RecMoney();
                bean_recMoney.isCustomer=false;
                bean_recMoney.receiveMonthProfitLists=receiveMonthProfitLists;
                bean_recMoney.monthProfit=Integer.parseInt(bean.getMonthProfit()+"");
                EventBus.getDefault().postSticky(bean_recMoney);
                Intent intent = new Intent(Activity_MyInvest.this,Activity_InvestMonth.class);
                startActivity(intent);
            }
        });
        ListView listView  = myinvest_pull_to_refresh.getRefreshableView();
        listView.addHeaderView(headerView);

//        listView.setEmptyView(headerView);


//        TextView emptyView = new TextView(this);
//        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        emptyView.setText("没有数据,点击重试!");
//        emptyView.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View view) { connectServer();  }
//        });
//        emptyView.setTextSize(DisplayUtil.dipToPx(this,15));
//        emptyView.setGravity(Gravity.CENTER);
//        emptyView.setVisibility(View.GONE);
//        ((ViewGroup)listView.getParent()).addView(emptyView);
//        listView.setEmptyView(emptyView);


           myinvest_total=(TextView) headerView.findViewById(R.id.myinvest_total);        //累计收益
           myinvest_total_fen=(TextView) headerView.findViewById(R.id.myinvest_total_fen);//投资份数
           myinvest_month=(TextView) headerView.findViewById(R.id.myinvest_month);        //月已到账
           myinvest_month_no=(TextView) headerView.findViewById(R.id.myinvest_month_no);  //月未到账
            myinvest_total_txt=(TextView) headerView.findViewById(R.id.myinvest_total_txt);
            myinvest_mingxi=(TextView) headerView.findViewById(R.id.myinvest_mingxi);

        myinvest_pull_to_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {   currentPage++; connectServer();  }//模拟上拉装载数据
            @Override  public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {    currentPage++; connectServer();  }//模拟下拉装载数据
        });

        myinvest_pull_to_refresh.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {

            /**  滚动状态改变时调用  */
            @Override  public void onScrollStateChanged(AbsListView view, int scrollState) { // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        scrolledX = myinvest_pull_to_refresh.getRefreshableView().getScrollX();
                        scrolledY = myinvest_pull_to_refresh.getRefreshableView().getScrollY();
                }
            }
            /** 滚动时调用 */
            @Override  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  }
        });
        connectServer();
    }

    /** 初始化界面(日历)  */
    @SuppressLint("SimpleDateFormat")
    private void init() {
        formatter = new SimpleDateFormat("yyyy年MM月");
        //获取当前时间
        curDate = cal.getTime();
        String str = formatter.format(curDate);
//        tv_month.setText(str);
        String strPre=(cal.get(Calendar.MONTH))+"月";
        if (strPre.equals("0月")){
            strPre="12月";
        }
//        tv_pre.setText(strPre);
        String strNext=(cal.get(Calendar.MONTH)+2)+"月";
        if(strNext.equals("13月")){
            strNext="1月";
        }
//        tv_next.setText(strNext);
        connectServer();
    }


    @SuppressLint("SetTextI18n")
    void  uodate(Bean_MyInvest bean_tmp){//totalBonusAmount
        bean=bean_tmp;
        try{
            LogUtil.e("获取到的totalprofit:"+bean.getTotalProfit());
//            myinvest_total.setText(bean.getTotalProfit()+"");
            myinvest_total.setText(new BigDecimal(bean.getTotalProfit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+""); //两位小数
        }catch (Throwable t){t.printStackTrace();}            //总共累计收益
        try{
            LogUtil.e("获取到的SubscribeCount:"+bean.getTotalSubscribeCount());
            myinvest_total_fen.setText(Double.valueOf(bean.getTotalSubscribeCount()).intValue()+""); }catch (Throwable t){t.printStackTrace();}  //总投资份数

        if (bean_tmp.getSubType().contains("2")){  //消费型
            myinvest_total_txt.setText("本月可抵扣额度 ");
            myinvest_month.setClickable(false);    //不可点击
            myinvest_month_img.setClickable(false);//不可点击
            try{   myinvest_month.setText("当月可抵扣金额 "+ bean.getMonthProfit() +"元。");}catch (Throwable t){t.printStackTrace();}
            myinvest_month_img_no.setVisibility(View.GONE);
            myinvest_month_no.setVisibility(View.GONE);
        }else {
            LogUtil.e("获取到的已到getMonthProfit:"+bean.getMonthProfit());
            LogUtil.e("获取到的未到getReceiveMonthProfit:"+bean.getReceiveMonthProfit());
            try{   myinvest_month.setText("当月分红已到账"+bean.getMonthProfit()+"元。");}catch (Throwable t){t.printStackTrace();}
            try{   myinvest_month_no.setText("当月分红待到账"+bean.getReceiveMonthProfit()+"元。");}catch (Throwable t){t.printStackTrace();}
        }


    }

    //获取我的认购
    void connectServer(){
        String url = "api/vip/user/subscribe/record";
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", currentPage+"");
        map.put("pageSize", "10");
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_MyInvest.class))
                .compose(RxNetHelper.<Bean_MyInvest>io_main())
                .subscribe(new ResultSubscriber<Bean_MyInvest>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(Bean_MyInvest bean) {
                        myinvest_pull_to_refresh.onRefreshComplete();
                        try{ monthProfitList=bean.getMonthProfitList();}catch (Throwable t){t.printStackTrace();}
                        try{ receiveMonthProfitLists=bean.getReceiveMonthProfitList();}catch (Throwable t){t.printStackTrace();}

                        LogUtil.e("获取认购列表  已认购"+bean.toString());
                        LogUtil.e("获取认购列表  已认购"+bean.getTotalProfit());                          //总收益
                        LogUtil.e("获取认购列表  已认购"+bean.getTotalSubscribeCount());                 //总认购份数
                        LogUtil.e("获取认购列表  已认购"+bean.getUserSubscribeList().size());             //数量
//                        LogUtil.e("获取认购列表  已认购"+bean.getUserSubscribeList().get(0).getSubType()); //数量
                        if (bean.getUserSubscribeList().size()==0){
                            ToastUtil.showToast("已全部加载！");
                        }
                        if (currentPage==1){
                            list=bean.getUserSubscribeList();
                            adapter = new Adapter_MyInvest(Activity_MyInvest.this,list);
                            myinvest_pull_to_refresh.setAdapter(adapter);
                        }else {
                            List<Bean_MyInvest.UserSubscribeList> list_tmp = bean.getUserSubscribeList();
                            list.addAll(list_tmp);
                            adapter.setList(list);
                        }
                        myinvest_pull_to_refresh.onRefreshComplete();
//                        myinvest_pull_to_refresh.setAdapter(adapter);
//                        adapter.setList(list);
                        try{
//                            myinvest_pull_to_refresh.getRefreshableView().scrollTo(scrolledX, scrolledY);
                        }catch (Throwable t){t.printStackTrace();}
                        adapter.notifyDataSetChanged();
                        try{if (list.size()>0){myinvest_mingxi.setVisibility(View.GONE);}}catch (Throwable t){t.printStackTrace();}
                      uodate(bean);

                    }
                    @Override public void onFail(Message<Bean_MyInvest> bean) {
                        LogUtil.e("获取认购列表失败code:"+bean.code+",msg:"+bean.msg);
                        if(bean.code==-1001){  EventBus.getDefault().post("登陆失效");return;}
                    }
                });
    }


    @Override  protected void onResume() {
        super.onResume();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
