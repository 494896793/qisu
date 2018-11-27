package www.qisu666.com.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.qisu666.com.config.Config;
import www.qisu666.com.R;
import www.qisu666.com.adapter.ChargingStatisticsAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.fragment.BaseFragment;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PageLoadResponseCallBack;
import www.qisu666.com.model.CarOrderBean;
import www.qisu666.com.model.PositionBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.sdk.mytrip.Fragment_Base;

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
 * Created by admin on 2018/2/8.
 */

//共享汽车 骑行未结束
public class CarShareTravelUnfinishFragment extends Fragment_Base {

    private View view;

    private PullToRefreshListView pull_refresh_load_recycler_view;
    private ChargingStatisticsAdapter adapter;
    private List<Map<String, Object>> list;

    private int currentPage = 1;//当前数据分页
    private static final int PAGE_NUM = 50;//每页数据条数
    private boolean requested = false;

    public CarShareTravelUnfinishFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        requestData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging_statistics, container, false);
        pull_refresh_load_recycler_view = view.findViewById(R.id.pull_refresh_load_recycler_view);
        requestData();
        return view;
    }



    private void requestData() {

        String url = "api/tss/order/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        requestMap.put("status", "0,1,2,4");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .map(new FlatFunction<>(CarOrderBean.class))
                .compose(RxNetHelper.<CarOrderBean>io_main())
                .subscribe(new ResultSubscriber<CarOrderBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(CarOrderBean bean) {

                    }

                    @Override
                    public void onFail(Message<CarOrderBean> bean) {

                    }
                });
    }
}
