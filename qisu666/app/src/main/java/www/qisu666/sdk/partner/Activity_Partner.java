package www.qisu666.sdk.partner;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.adapter.BillAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.sdk.partner.adapter.Adapter_Partner;

/**  合伙人页面
 * 717219917@qq.com ${DATA} 20:31.
 */
public class Activity_Partner extends BaseActivity implements OnLoadRefreshCallBack {

    @BindView(R.id.tv_title) TextView tv_title;               //标题
    @BindView(R.id.img_title_left) ImageView img_title_left;  //左返回
    @BindView(R.id.partner_pull_refresh_load_recycler_view)   PullRefreshLoadRecyclerView partner_pull_refresh_load_recycler_view;//车辆列表
    private LoadMoreView loadMoreView;
    private Adapter_Partner adapter;
    private List<Map<String,Object>> list;

    private int currentPage = 1;           //当前数据分页
    private static final int PAGE_NUM = 10;//每页数据条数


    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_partner);//合作者
        initView();
        initData();
    }


    void initView(){
        tv_title.setText("投资认购");
        partner_pull_refresh_load_recycler_view.setLoadMoreView(null);
        loadMoreView = new DefaultLoadMoreView(this);
        partner_pull_refresh_load_recycler_view.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
    }

    void initData(){
        list = new ArrayList<Map<String, Object>>();
//        for(int i=0;i<10;i++){
//            list.add(new HashMap<String, Object>());
//        }
        adapter = new Adapter_Partner(this, this, list);
        partner_pull_refresh_load_recycler_view.setAdapter(adapter);
        connectServer();
    }

    @OnClick({ R.id.img_title_left})
    public void InitListener(View view){
        switch (view.getId()){
            case R.id.img_title_left:  finish(); break;//返回
        }
    }


    @Override public void onRefresh() {
        currentPage = 1;
        partner_pull_refresh_load_recycler_view.setLoadMoreView(null);
        connectServer();
    }//刷新list,进行网络请求

    @Override  public void onLoadMore() {
        currentPage++;
        connectServer();
    }//加载更多list  进行网络请求


     //获取认购列表
    void connectServer(){
            String url = "api/vip/product/list/query";
            HashMap<String, Object> map = new HashMap<>();
            map.put("pageNum", currentPage+"");
            map.put("pageSize", "10");
            map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                    .carRequest(url, MyMessageUtils.addBody(map))
                    .map(new FlatFunction<>(Object.class))
                    .compose(RxNetHelper.<Object>io_main())
                    .subscribe(new ResultSubscriber<Object>() {
                        @Override
                        public void onSuccessCode(Message object) {

                        }

                        @Override public void onSuccess(Object bean) {
                            LogUtil.e("获取认购列表失败"+bean.toString());

                        }
                        @Override public void onFail(Message<Object> bean) {
                            LogUtil.e("获取认购列表失败"+bean.msg);
                        }
                    });
    }



    @Override protected void onResume() {
        super.onResume();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
    }


}
