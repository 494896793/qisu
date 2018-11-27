package www.qisu666.sdk.partner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.MyMessageUtils_Car;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DisplayUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.adapter.Adapter_Term;
import www.qisu666.sdk.partner.bean.ProductList;
import www.qisu666.sdk.partner.bean.TermBean;

/**  投资认购
 * 717219917@qq.com ${DATA} 10:21.
 */
public class Activity_Term extends BaseActivity   {

    @BindView(R.id.tv_title) TextView tv_title;            //标题
    @BindView(R.id.img_title_left) ImageView img_title_left;//左边返回
     ImageView term_img_logo;
     TextView term_txt;//描述
      PieChart chart;//扇形图


    @BindView(R.id.pull_to_refresh) PullToRefreshListView pull_to_refresh;

    private Adapter_Term adapter;
    private List<ProductList> list=null;   //认购列表
    private int currentPage = 1;           //当前数据分页
    private static final int PAGE_NUM = 10;//每页数据条数

    int percent_yes=100; //已经认购的百分比
    int percent_no=0;   //未认购的百分比
    int num_yes=140;    //已认购数量
    int num_no =60;     //未认购数量
    int num_total=200;  //总数量
    int car_total=20;   //总车辆数


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_term);
        initView();
        initData();
    }

    void initView(){
        tv_title.setText("投资认购");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        //2-2，设置下拉刷新模式BOYH|END|START
        pull_to_refresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);  //同时上拉下拉

        //设置自定义下拉刷新动画文字  getLoadingLayoutProxy(true, false)，参数分别代表要设置上或下的文字修改
        ILoadingLayout headerLayout = pull_to_refresh.getLoadingLayoutProxy(true, false);
        headerLayout.setPullLabel("向下拖动完成刷新...");
        headerLayout.setRefreshingLabel("正在加载新数据...");
        headerLayout.setReleaseLabel("释放完成刷新...");
        //设置底部刷新文字
        ILoadingLayout footLayout = pull_to_refresh.getLoadingLayoutProxy(false, true);
        footLayout.setPullLabel("向上拽动完成刷新...");
        footLayout.setRefreshingLabel("正在刷新数据...");
        footLayout.setReleaseLabel("松开完成刷新...");
//        footLayout.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        View headerView = View.inflate(getApplicationContext(), R.layout.head_term,null);
        ListView listView  = pull_to_refresh.getRefreshableView();
        listView.addHeaderView(headerView);


        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("没有数据,点击重试!");
//        emptyView.setTextSize(DisplayUtil.dipToPx(this,15));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { connectServer(); }//进行加载数据
        });
        ((ViewGroup)listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);

           term_txt=(TextView) headerView.findViewById(R.id.term_txt);//描述
           chart=(PieChart) headerView.findViewById(R.id.chart);//扇形图
        chart.setUsePercentValues(false);//使用百分比显示
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);      //设置pieChart图表背景色
        chart.setExtraOffsets(5, 10, 60, 10);        //设置pieChart图表上下左右的偏移，类似于外边距
        chart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        chart.setRotationAngle(180);                  //设置pieChart图表起始角度
        chart.setRotationEnabled(false);              //设置pieChart图表是否可以手动旋转
        chart.setHighlightPerTapEnabled(false);       //设置piecahrt图表点击Item高亮是否可用
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        chart.setDrawEntryLabels(false);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        chart.setEntryLabelColor(Color.WHITE);        //设置pieChart图表文本字体颜色
        chart.setEntryLabelTextSize(5f);              //设置pieChart图表文本字体大小

        // 设置 pieChart 内部圆环属性
        chart.setDrawHoleEnabled(false);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        chart.setHoleRadius(28f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        chart.setTransparentCircleRadius(31f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        chart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        chart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数//chart.setOnChartValueSelectedListener(this);
    }

    void initData(){
        setData();
        connectServer();
    }

    /** 初始化饼图的数据 */
    private void setData() {
        Legend l = chart.getLegend();
        l.setEnabled(true);                    //是否启用图列（true：下面属性才有意义）
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.DEFAULT); //设置图例的形状
        l.setFormSize(20);                    //设置图例的大小
        l.setFormToTextSpace(1f);            //设置每个图例实体中标签和形状之间的间距
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);           //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setXEntrySpace(10f);                //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(8f);                 //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(10f);                     //设置比例块Y轴偏移量
        l.setTextSize(14f);                   //设置图例标签文本的大小
        l.setTextColor(Color.BLACK);          //设置图例标签文本的颜色


        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#33D6D6"));//已认购d
        colors.add(Color.parseColor("#5CD6FF")); //待认购

        //饼图实体 PieEntry
        PieEntry CashBalance        = new PieEntry(percent_yes, "已认购："+Math.abs(num_yes));
        PieEntry ConsumptionBalance = new PieEntry(percent_no, "待认购："+Math.abs(num_no));
        pieEntryList.add(CashBalance);
        pieEntryList.add(ConsumptionBalance);
        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "    总份数："+Math.abs(num_total));
        pieDataSet.setSliceSpace(0f);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(0f);       //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueTextColor(Color.BLUE);  //设置所有DataSet内数据实体（百分比）的文本颜色
        pieData.setValueTextSize(12f);          //设置所有DataSet内数据实体（百分比）的文本字体大小
//        pieData.setValueTypeface(mTfLight);     //设置所有DataSet内数据实体（百分比）的文本字体样式
        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
        chart.setData(pieData);
        chart.highlightValues(null);
        chart.invalidate();                    //将图表重绘以显示设置的属性和数据
        term_txt.setText("   "+Integer.parseInt(car_total+"")+"辆车开放认购中,共发布"+num_total+"份汽车投资,已认购"+num_yes+"份，待认购"+num_no+"份,快把心仪的车领回家吧！");

//        pull_to_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//
//            }
//        });//下拉刷新

        pull_to_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {  //模拟上拉装载数据
                currentPage++;
                connectServer();
            }

            @Override  public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) { //模拟下拉装载数据
                currentPage++;
                connectServer();
            }
        });

    }





    //刷新  分页
    public void onRefresh() {
        currentPage = 1;
        connectServer();
    }
     public void onLoadMore() {
        currentPage++;
        connectServer();
    }


    //获取数据  认购列表 这里需要修改
    void connectServer(){
        String url = "api/vip/product/list/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", currentPage+"");
        map.put("pageSize", "10");
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(TermBean.class))
//                .compose(RxNetHelper.<TermBean>io_main())
                .compose(RxNetHelper.<TermBean>io_main(mLoadingDialog))            //可以去掉object
                .subscribe(new ProgressSubscriber<TermBean>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    //                .subscribe(new ResultSubscriber<TermBean>() {
                    @Override public void onSuccess(TermBean bean) {
                        pull_to_refresh.onRefreshComplete();
                        mLoadingDialog.dismiss();
                        LogUtil.e("获取认购列表  已认购"+bean.getHasSubscribeNumber());
                        LogUtil.e("获取认购列    剩余 "+bean.getRemainNumber());
                        LogUtil.e("获取认购列总共  "+bean.getAllTotalNumber());
                        LogUtil.e("获取认购列表成功"+bean.getProductList().size());
                        if (bean.getProductList().size()==0){
                            if(currentPage==1){
                                ToastUtil.showToast("暂无数据！");
                            }else {
                                ToastUtil.showToast("已全部加载！");
                            }

                        }
                       try {
                            double tota=Double.parseDouble(bean.getProductNumbers()+"");
                            car_total=Double.valueOf(tota).intValue();
                        }catch (Throwable t){t.printStackTrace();}               //车辆数

                        try {
                            double tota=Double.parseDouble(bean.getHasSubscribeNumber()+"");
                            num_yes=Double.valueOf(tota).intValue();  //已认购数量
                        }catch (Throwable t){t.printStackTrace();}


                        try {
                            double tota=Double.parseDouble(bean.getRemainNumber()+"");
                            num_no=Double.valueOf(tota).intValue();   //未认购数量
                        }catch (Throwable t){t.printStackTrace();}

                        try {
                            double tota=Double.parseDouble(bean.getAllTotalNumber()+"");
                            num_total=Double.valueOf(tota).intValue();   //总数量
                        }catch (Throwable t){t.printStackTrace();}

                        percent_yes=(int) ((float)num_yes*100)/num_total;    //已经认购的百分比
                        percent_no=(int) ((float)num_no*100)/num_total;      //未认购的百分比
                        percent_yes=Math.abs(percent_yes);
                        percent_no =Math.abs(percent_no);
                        //   car_total=bean.getProductList().size();        //列表中总共的车辆数量
                        setData();
                        if (currentPage==1){
                            list=bean.getProductList();
                            adapter = new Adapter_Term(Activity_Term.this,list);
                        }else {
                            List<ProductList> list_tmp = bean.getProductList();
                            list.addAll(list_tmp);
                            adapter.setList(list);
                        }
                        pull_to_refresh.onRefreshComplete();
                        pull_to_refresh.setAdapter(adapter);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    @Override public void onFail(Message<TermBean> bean) {
                        LogUtil.e("获取认购列表失败code:"+bean.code+",msg:"+bean.msg);
                        if(bean.code==-1001||bean.code==-1201){  EventBus.getDefault().post("登陆失效");return;}
                        try{ pull_to_refresh.onRefreshComplete();
                        pull_to_refresh.setAdapter(adapter);
                        adapter.notifyDataSetChanged();}catch (Throwable t){t.printStackTrace();}
                    }
                });
    }

    @Override  protected void onResume() {
        super.onResume();
        connectServer();
    }
    @Override protected void onDestroy() { super.onDestroy();  }


}
