package www.qisu666.com.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import www.qisu666.com.adapter.ChargingRecordAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.R;
import www.qisu666.com.adapter.ChargingStatisticsAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PageLoadResponseCallBack;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.Fragment_Base;
import www.qisu666.sdk.mytrip.bean.ChongDianJiLu;
import www.qisu666.sdk.mytrip.bean.ChongdianTongji;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * 充电统计
 */
public class ChargingStatisticsFragment extends Fragment_Base {

    private View view;

    private PullToRefreshListView pull_refresh_load_recycler_view;
    private ChargingStatisticsAdapter adapter;
    private List<ChongdianTongji.MyOrderList> mLists = new ArrayList<>();

    //当前数据分页
    private int currentPage = 1;
    //每页数据条数
    private static final int PAGE_NUM = 10;

    private boolean requested = false;

    private ImageView ivGG;

    public ChargingStatisticsFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {

        connToServer();
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging_statistics, container, false);
        pull_refresh_load_recycler_view = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_load_recycler_view);
        ivGG = view.findViewById(R.id.iv_gg);
        if (UserParams.INSTANCE.getUser_id() != null) {
            connToServer();
        }

        RxBus.getInstance().toObservable().map(new Function<Object, EventMsg>() {
            @Override
            public EventMsg apply(Object o) throws Exception {
                return (EventMsg) o;
            }
        }).subscribe(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if (eventMsg != null) {
                    if (eventMsg.getType() == 1) {
                        connToServer();
                    } else if (eventMsg.getType() == 2) {
                        pull_refresh_load_recycler_view.setVisibility(View.GONE);
                        ivGG.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置模式
        pull_refresh_load_recycler_view.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_load_recycler_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉刷新时会回调的方法
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage = 1;
                connToServer();
            }

            //上啦加载时执行的方法
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage++;
                connToServer();
            }
        });
    }

    /**
     * 发送 C102 请求，获取充电统计
     */
    private void connToServer() {

        String url = "api/my/order/type/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("orderType", "1");
        map.put("optType", "2");
        map.put("pageSize", String.valueOf(PAGE_NUM));
        map.put("pageNum", String.valueOf(currentPage));

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(ChongdianTongji.class))
                .compose(RxNetHelper.<ChongdianTongji>io_main())
                .subscribe(new ResultSubscriber<ChongdianTongji>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(ChongdianTongji bean) {


                        pull_refresh_load_recycler_view.onRefreshComplete();
                        try {
                            if (bean.getMyOrderList().size() == 0) {
//                                ivGG.setVisibility(View.VISIBLE);
//                                pull_refresh_load_recycler_view.setVisibility(View.GONE);
                                return;
                            } else {
                                ivGG.setVisibility(View.GONE);
                                pull_refresh_load_recycler_view.setVisibility(View.VISIBLE);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            ToastUtil.showToast("已全部加载！");
                            return;
                        }
                        if (currentPage == 1) {
                            mLists = bean.getMyOrderList();
                            adapter = new ChargingStatisticsAdapter(getActivity(), mLists);
                            pull_refresh_load_recycler_view.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            mLists.addAll(bean.getMyOrderList());
                            adapter.setList(mLists);
                        }
                    }

                    @Override
                    public void onFail(Message<ChongdianTongji> bean) {
                        try {
                            pull_refresh_load_recycler_view.onRefreshComplete();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !requested) {
            connToServer();
            requested = true;
        }
    }
}
