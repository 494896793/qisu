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
import android.widget.TextView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.amap.stationMap.JsonUtil;
import www.qisu666.sdk.mytrip.bean.Bean_TripNo;
import www.qisu666.sdk.mytrip.bean.EventMsg;
import www.qisu666.sdk.mytrip.bean.RxBus;

/**
 * 717219917@qq.com  2016/12/14 0:18
 */
@ContentView(R.layout.fragment_mytrip)//我的行程 未完成   分页已做
public class Fragment_MyTrip_NO extends Fragment_Base {
    @ViewInject(R.id.pull_to_refresh)
    PullToRefreshListView mypull_to_refresh; //整个list
    @ViewInject(R.id.iv_gg)
    ImageView ivGG;
    Adapter_Fragment_MyTrip_No mMain_adapter;                            //适配器
    List<Bean_TripNo.MyOrderList> mList = new ArrayList<>();
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
                        connectServer();
                    } else if (eventMsg.getType() == 2) {
                        mypull_to_refresh.setVisibility(View.GONE);
                        ivGG.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mypull_to_refresh.setMode(PullToRefreshBase.Mode.BOTH);//设置模式只上拉刷新
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
//        TextView headView = new TextView(getActivity());
//        headView.setText("暂无数据,点击重试！");
//        headView.setOnClickListener(new View.OnClickListener() {  @Override public void onClick(View view) { currentPage=1; connectServer();  }  });
//        mypull_to_refresh.getRefreshableView().addHeaderView(headView);
//        connectServer();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            connectServer();
//        }
//
//    }

    /**
     * 获取行程列表   已取消
     */
    private void connectServer() {

        String url = "api/my/order/type/query";
        HashMap<String, Object> map = new HashMap<>();
        //未完成
        map.put("orderType", "2");
        map.put("optType", "4,5");
        map.put("pageNum", currentPage + "");
        map.put("pageSize", "10");
        map.put("userCode", UserParams.INSTANCE.getUser_id() + "");

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TripNo.class))
                .compose(RxNetHelper.<Bean_TripNo>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Bean_TripNo>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Bean_TripNo bean) {

                        if(currentPage==1){
                            if(bean==null||bean.getMyOrderList()==null||bean.getMyOrderList().size()==0){
                                ivGG.setVisibility(View.VISIBLE);
                            }else{
                                ivGG.setVisibility(View.GONE);
                            }
                        }
                        mypull_to_refresh.onRefreshComplete();
                        Log.e("fffffff", "取消" + bean.toString());
                        String strJosn = JsonUtils.objectToJson(bean);
//                        Log.e("fffffff", "strJosn" + strJosn);
                        try {
                            if (bean.getMyOrderList().size() == 0) {
//                                ivGG.setVisibility(View.VISIBLE);
//                                mypull_to_refresh.setVisibility(View.GONE);
                                return;
//                                ToastUtil.showToast("已全部加载！");
                            } else {
                                ivGG.setVisibility(View.GONE);
                                mypull_to_refresh.setVisibility(View.VISIBLE);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            ToastUtil.showToast("已全部加载！");
                            return;
                        }
                        if (currentPage == 1) {
                            mList = bean.getMyOrderList();
                            mMain_adapter = new Adapter_Fragment_MyTrip_No(getActivity(), mList);
                            mypull_to_refresh.setAdapter(mMain_adapter);
                            mMain_adapter.notifyDataSetChanged();
                        } else {
                            mList.addAll(bean.getMyOrderList());
                            mMain_adapter.setList(mList);
                        }

//                        for (int a = 0; a < bean_resu.getOrderList().size(); a++) {
//                            LogUtil.e(a + "获取我的行程成功  创建时间    " + bean_resu.getOrderList().get(a).getCreatedTime());
//                            LogUtil.e(a + "获取我的行程成功  开始位置    " + bean_resu.getOrderList().get(a).getBeginLocationTxt());
//                            LogUtil.e(a + "获取我的行程成功  借车时间    " + bean_resu.getOrderList().get(a).getBorrowTime());
//                            LogUtil.e(a + "获取我的行程成功  订单号      " + bean_resu.getOrderList().get(a).getOrderCode());
//                            LogUtil.e(a + "获取我的行程成功  还车时间    " + bean_resu.getOrderList().get(a).getReturnTime());
//                            LogUtil.e(a + "获取我的行程成功  更新时间    " + bean_resu.getOrderList().get(a).getUpdatedTime());
//                            LogUtil.e(a + "获取我的行程成功  费用        " + bean_resu.getOrderList().get(a).getPayAmount());
//                            LogUtil.e(a + "获取我的行程成功  rowno       " + bean_resu.getOrderList().get(a).getROWNO());
//                            LogUtil.e(a + "获取我的行程成功  状态        " + bean_resu.getOrderList().get(a).getStatus());
//
//                        }
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onFail(Message<Bean_TripNo> bean) {
                        try {
                            mypull_to_refresh.onRefreshComplete();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        LogUtil.e("获取我的行程详情失败" + bean.msg);
//                        if (bean.code == -1001) {
//                            EventBus.getDefault().post("登陆失效");
//                        }

                    }
                });

    }


}
