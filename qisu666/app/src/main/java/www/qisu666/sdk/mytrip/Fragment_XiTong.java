package www.qisu666.sdk.mytrip;


import android.annotation.SuppressLint;
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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import www.qisu666.com.R;
import www.qisu666.com.adapter.BillAdapter;
import www.qisu666.com.adapter.NotificationAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

/**
 * 717219917@qq.com  2016/12/14 0:18
 *
 * @author lp
 */
@ContentView(R.layout.fragment_mytrip)//系统消息
public class Fragment_XiTong extends Fragment_Base {
    /**
     * 整个list
     */
    @ViewInject(R.id.pull_to_refresh)
    PullToRefreshListView mypull_to_refresh;
    @ViewInject(R.id.iv_gg)
    ImageView ivGG;
    //适配器
    NotificationAdapter adapter;
    private List<Map<String, Object>> mLists = new ArrayList<>();
    int currentPage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            connectServer();

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置模式
        mypull_to_refresh.setMode(PullToRefreshBase.Mode.BOTH);
        mypull_to_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉刷新时会回调的方法
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage = 1;
                connectServer();
            }

            //上啦加载时执行的方法
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                currentPage++;
                connectServer();
            }
        });
    }
    
    /**
     * 获取系统消息
     */
    private void connectServer() {

        String url = "api/jpush/page/list/query";
        final HashMap<String, Object> map = new HashMap<>();
       
        map.put("platform", "0");
        map.put("pageIndex", currentPage + "");
        map.put("pageNum", "10");
        map.put("userId", UserParams.INSTANCE.getUser_id() + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
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
//                                    DialogHelper.alertDialog(getActivity(), "暂无数据");
                                } else {
                                    List<Map<String, Object>> listTem = new ArrayList<>();
                                    for (int i = 0; i < count; i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String oString = object.toString();
                                        LogUtils.e("oString: " + oString);
                                        listTem.add(JsonUtils.jsonToMap(oString));
                                    }

                                    if (currentPage == 1) {
                                        mLists = listTem;
                                        adapter = new NotificationAdapter(getActivity(), mLists);
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
                        if(adapter!=null&&adapter.getData()!=null&&adapter.getData().size()!=0){
                            ivGG.setVisibility(View.GONE);
                        }else{
                            ivGG.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        try {
                            mypull_to_refresh.onRefreshComplete();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        Log.e("asd", "获取我的行程详情失败" + bean.msg);
                    }
                });

    }
}