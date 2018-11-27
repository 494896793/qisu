package www.qisu666.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.qisu666.com.config.Config;
import www.qisu666.com.R;
import www.qisu666.com.adapter.PayByOthersRecordFinishAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PageLoadResponseCallBack;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayByOthersRecrodFinishFragment extends BaseFragment  implements OnLoadRefreshCallBack {

    private View view;

    private PullRefreshLoadRecyclerView pull_refresh_load_recycler_view;
    private LoadMoreView loadMoreView;
    private PayByOthersRecordFinishAdapter adapter;
    private List<Map<String,Object>> list;

    //当前数据分页
    private int currentPage = 1;
    //每页数据条数
    private static final int PAGE_NUM = 10;

    public PayByOthersRecrodFinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging_record, container, false);
        initViews();
        setAdapter();
        connToServer();
        return view;
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        connToServer();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        pull_refresh_load_recycler_view = (PullRefreshLoadRecyclerView) view.findViewById(R.id.pull_refresh_load_recycler_view);
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        loadMoreView = new DefaultLoadMoreView(getActivity());
        pull_refresh_load_recycler_view.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        list = new ArrayList<Map<String, Object>>();

        //demo代码，添加静态数据
//        for (int i=0;i<10;i++){
//            list.add(new HashMap<String, Object>());
//        }

        adapter = new PayByOthersRecordFinishAdapter(getActivity(), this, list);
        pull_refresh_load_recycler_view.setAdapter(adapter);
    }

    /**
     * 刷新列表
     */
    @Override
    public void onRefresh() {
        currentPage = 1;
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        connToServer();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        currentPage++;
        connToServer();
    }

    /**
     * 发送 C104 请求，获取代付完成记录
     */
    private void connToServer() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "C104");
            jsonObject.put("s_token", UserParams.INSTANCE.getS_token());
            jsonObject.put("user_id", UserParams.INSTANCE.getUser_id());
            jsonObject.put("cur_page_no", String.valueOf(currentPage));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpLogic(getActivity()).sendRequest(Config.REQUEST_URL, jsonObject, new PageLoadResponseCallBack(currentPage, PAGE_NUM, pull_refresh_load_recycler_view, loadMoreView, adapter, list, "data_list"));
    }

}
