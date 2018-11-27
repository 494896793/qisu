package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import www.qisu666.com.R;
import www.qisu666.com.adapter.ChargingRecordAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyDisposableSubscriber;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.fragment.BaseFragment;
import www.qisu666.com.model.CarOrderBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.Fragment_Base;
import www.qisu666.sdk.mytrip.bean.ChongDianJiLu;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by admin on 2018/2/8. */

//共享汽车  骑行结束  获取充电记录
public class CarShareTravelFinishFragment extends Fragment_Base {

    private View view;

    private PullToRefreshListView pull_refresh_load_recycler_view;
    private ChargingRecordAdapter adapter;
    private List<ChongDianJiLu.MyOrderList> mLists = new ArrayList<>();

    //当前数据分页
    private int currentPage = 1;
    //每页数据条数
    private static final int PAGE_NUM = 50;

    public CarShareTravelFinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging_record, container, false);
        pull_refresh_load_recycler_view = view.findViewById(R.id.pull_refresh_load_recycler_view);
        connToServer();
        return view;
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        connToServer();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置模式
        pull_refresh_load_recycler_view.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_load_recycler_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage++;
                connToServer();
            }//模拟上拉装载数据

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage++;
                connToServer();
            }//模拟下拉装载数据
        });
    }


    /**
     * 发送 C101 请求，获取充电记录
     */
    private void connToServer() {
        String url = "api/my/order/type/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("orderType", "2");
        map.put("optType", "3,5");
        map.put("pageSize", String.valueOf(PAGE_NUM));
        map.put("pageNum", String.valueOf(currentPage));

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(ChongDianJiLu.class))
                .compose(RxNetHelper.<ChongDianJiLu>io_main())
                .subscribe(new ResultSubscriber<ChongDianJiLu>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(ChongDianJiLu bean) {

                        pull_refresh_load_recycler_view.onRefreshComplete();
                        mLoadingDialog.dismiss();

                        try {
                            if (bean.getMyOrderList().size() == 0) {
                                ToastUtil.showToast("已全部加载！");
                                return;
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            ToastUtil.showToast("已全部加载！");
                            return;
                        }
                        if (currentPage == 1) {
                            mLists = bean.getMyOrderList();
                            adapter = new ChargingRecordAdapter(getActivity(),mLists);
                            pull_refresh_load_recycler_view.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            mLists.addAll(bean.getMyOrderList());
                            adapter.setList(mLists);
                        }
                    }

                    @Override
                    public void onFail(Message<ChongDianJiLu> bean) {
                        try {
                            pull_refresh_load_recycler_view.onRefreshComplete();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                });


//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("req_code", "C101");
//            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
//            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
//            jsonObject.put("cur_page_no", String.valueOf(currentPage));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new HttpLogic(getActivity()).sendRequest(Config.REQUEST_URL, jsonObject,
//                new PageLoadResponseCallBack(currentPage, PAGE_NUM, pull_refresh_load_recycler_view, loadMoreView, adapter, list, "data_list"));
    }
}
