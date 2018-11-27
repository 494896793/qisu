package www.qisu666.sdk.mytrip.Order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.adapter.Bill_two_Adapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.Fragment_Base;

/**
 * 717219917@qq.com 2018/7/4 17:23.
 */
public class Fragment_HuoDong extends Fragment_Base {

    private View view;
    private PullToRefreshListView mypull_to_refresh;
    private Bill_two_Adapter adapter;
    private List<Map<String, Object>> mLists = new ArrayList<>();

    /**
     * 当前数据分页
     */
    private int currentPage = 1;
    /**
     * 每页数据条数
     */
    private static final int PAGE_NUM = 10;
    private ImageView ivGG;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charging_record, container, false);
        initViews();
        connToServer();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置模式
        mypull_to_refresh.setMode(PullToRefreshBase.Mode.BOTH);
        mypull_to_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override   // 下拉
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage = 1;
                connToServer();
            }

            @Override      //上拉
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage++;
                connToServer();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        mypull_to_refresh = view.findViewById(R.id.pull_refresh_load_recycler_view);
        ivGG = view.findViewById(R.id.iv_gg);
    }


    /**
     * 获取活动金
     */
    private void connToServer() {

        String url = "api/user/account/detail";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("accType", "active");
        map.put("pageNo", currentPage + "");
        map.put("rowNum", PAGE_NUM + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main())
                .subscribe(new ResultSubscriber<Object>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void onSuccess(Object bean) {

                        mypull_to_refresh.onRefreshComplete();
                        mLoadingDialog.dismiss();

                        // 对象转json
                        String s = JsonUtils.objectToJson(bean);
                        // json转 list
                        List<String> strings = JsonUtils.jsonToList(s);

                        if (strings != null) {

                            Log.e("aaaa", "rowNum: " + strings.toString());
                            try {
                                JSONArray array = new JSONArray(strings);
                                int count = array.length();
                                if (count == 0) {
                                    ivGG.setVisibility(View.VISIBLE);
                                    mypull_to_refresh.setVisibility(View.GONE);
                                } else {
                                    ivGG.setVisibility(View.GONE);
                                    mypull_to_refresh.setVisibility(View.VISIBLE);
                                    List<Map<String, Object>> listTem = new ArrayList<>();
                                    for (int i = 0; i < count; i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String oString = object.toString();
                                        LogUtils.e("oString: " + oString);
                                        listTem.add(JsonUtils.jsonToMap(oString));
                                    }

                                    if (currentPage == 1) {
                                        mLists = listTem;
                                        adapter = new Bill_two_Adapter(getActivity(), mLists);
                                        mypull_to_refresh.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        mLists.addAll(listTem);
                                        adapter.setList(mLists);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.e("aaaa", "获取失败：" + bean.toString());
                    }

                });
    }

}
