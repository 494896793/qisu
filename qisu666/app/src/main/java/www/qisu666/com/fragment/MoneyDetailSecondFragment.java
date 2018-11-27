package www.qisu666.com.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.qisu666.com.R;
import www.qisu666.com.adapter.MoneyListViewActiviAdapter;
import www.qisu666.com.adapter.MoneyListViewAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.model.ActiviMoneyBean;
import www.qisu666.com.model.MoneyDetailBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.JsonUtils;

/**
 * 717219917@qq.com 2018/8/8 9:49.
 */
public class MoneyDetailSecondFragment extends Fragment {


    private PullToRefreshListView pull_refresh_load_recycler_view;
    private List<ActiviMoneyBean> list;
    private MoneyListViewActiviAdapter adapter;
    private ImageView iv_gg;
    private int pageNo=1;
    private int rowNum=10;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.moneydetailfragment_layout,null);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(adapter==null){
            request();
        }

        pull_refresh_load_recycler_view.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_load_recycler_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新时会回调的方法
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNo = 1;
                request();
            }

            //上啦加载时执行的方法
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNo++;
                request();
            }
        });

        request();
    }

    private void request(){
        String url="api/user/account/detail";
        HashMap<String,Object> map=new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("accType","active");
        map.put("pageNo",pageNo+"");
        map.put("rowNum",rowNum+"");
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {

                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        try{
                            pull_refresh_load_recycler_view.onRefreshComplete();
                            String jsonString= JsonUtils.objectToJson(bean);
                            if(!TextUtils.isEmpty(bean.toString())){
                                JSONArray jsonArray=new JSONArray(jsonString);
                                if(jsonArray!=null&&jsonArray.length()>0){
                                    list=new ArrayList<>();
                                    for(int i=0;i<jsonArray.length();i++){
                                        ActiviMoneyBean activiMoneyBean=new ActiviMoneyBean();
                                        activiMoneyBean.setActiveTitle(jsonArray.optJSONObject(i).optString("activeTitle"));
                                        activiMoneyBean.setConsumeAmount(jsonArray.optJSONObject(i).optString("consumeAmount"));
                                        activiMoneyBean.setMonthAbleaMount(jsonArray.optJSONObject(i).optString("monthAbleaMount"));
                                        activiMoneyBean.setOperationDate(jsonArray.optJSONObject(i).optString("operationDate"));
                                        activiMoneyBean.setUserCode(jsonArray.optJSONObject(i).optString("userCode"));
                                        list.add(activiMoneyBean);
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(list!=null){
                            if (adapter==null) {
                                adapter=new MoneyListViewActiviAdapter(list,getActivity());
                                pull_refresh_load_recycler_view.setAdapter(adapter);
                            }else{
                                if(pageNo==1){
                                    adapter.refreshData(list);
                                }else{
                                    adapter.loadData(list);
                                }
                            }
                        }
                        if(adapter!=null){
                            if(adapter.getData()==null||adapter.getData().size()==0){
                                iv_gg.setVisibility(View.VISIBLE);
                            }else{
                                iv_gg.setVisibility(View.GONE);
                            }
                        } else{
                            iv_gg.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        pull_refresh_load_recycler_view.onRefreshComplete();
                        Log.i("=","=");
                    }
                });
    }

    private void initView(View view){
        iv_gg=view.findViewById(R.id.iv_gg);
        this.pull_refresh_load_recycler_view=view.findViewById(R.id.pull_refresh_load_recycler_view);
    }

}
