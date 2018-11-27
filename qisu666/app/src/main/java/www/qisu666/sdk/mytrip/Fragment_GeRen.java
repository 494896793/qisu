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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

/**
 * 717219917@qq.com  2016/12/14 0:18
 *
 * @author lp
 */
@ContentView(R.layout.fragment_mytrip)//个人消息
public class Fragment_GeRen extends Fragment_Base {
    /**
     * 整个list
     */
    @ViewInject(R.id.pull_to_refresh)
    PullToRefreshListView mypull_to_refresh;
    @ViewInject(R.id.iv_gg)
    ImageView ivGG;
    //适配器

    private List<Map<String, Object>> mLists = new ArrayList<>();
    int currentPage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (UserParams.INSTANCE.getUser_id() != null) {
            connectServer();
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取行程列表     已完成
     */
    private void connectServer() {

        String url = "api/my/order/type/query";
        final HashMap<String, Object> map = new HashMap<>();
        //已完成
        map.put("orderType", "2");
        map.put("optType", "3");
        map.put("pageNum", currentPage + "");
        map.put("pageSize", "10");
        map.put("userCode", UserParams.INSTANCE.getUser_id() + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        mypull_to_refresh.onRefreshComplete();
                        mLoadingDialog.dismiss();
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