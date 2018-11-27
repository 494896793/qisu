package www.qisu666.com.fragment.order;

import android.content.Intent;
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
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.qisu666.com.R;
import www.qisu666.com.activity.InverstDetailActivity;
import www.qisu666.com.adapter.Fragment_Rengou_Adapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MessageUtil;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.entity.InverstOrderEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.sdk.mytrip.Fragment_Base;
import www.qisu666.sdk.mytrip.bean.Bean_TripAlready;

/**
 * 我的认购
 */
public class Fragment_RenGou extends Fragment_Base {

    private int pageNum=1;
    private int pageSize=10;
    private List<InverstOrderEntity> list;

    PullToRefreshListView pull;
    ImageView img_nodata;

    Fragment_Rengou_Adapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rengou,null);
        initView(view);
        Log.i("====","=====onCreateView");
        return view;
    }

    private void initView(View view){
        pull=view.findViewById(R.id.pull);
        img_nodata=view.findViewById(R.id.img_nodata);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("====","=====onViewCreated");
        initListenner();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initListenner(){
        pull.setMode(PullToRefreshBase.Mode.BOTH);
        pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新时会回调的方法
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum = 1;
                initData();
            }

            //上啦加载时执行的方法
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum++;
                initData();
            }
        });
    }

    private void initData(){
        String url="api/my/order/type/query";
        HashMap<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("orderType","3");
        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        pull.onRefreshComplete();
                        Log.i("","");
                        if(bean!=null&&!bean.toString().equals("")){
                            String jsonString= JsonUtils.objectToJson(bean);
                            try{
                                JSONObject jsonObject=new JSONObject(jsonString);
                                JSONArray jsonArray=jsonObject.optJSONArray("userSubscribeList");
                                if(jsonArray!=null&&jsonArray.length()>0){
                                    img_nodata.setVisibility(View.GONE);
                                    list=new ArrayList<>();
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                        InverstOrderEntity orderEntity=new InverstOrderEntity();
                                        orderEntity.setContractExpiresTime(jsonObject1.optString("contractExpiresTime"));
                                        orderEntity.setContractStatus(jsonObject1.optString("contractStatus"));
                                        orderEntity.setCountPeriods(jsonObject1.optString("countPeriods"));
                                        orderEntity.setCreatedTime(jsonObject1.optString("createdTime"));
                                        orderEntity.setIdcardNum(jsonObject1.optString("idcardNum"));
                                        orderEntity.setModelCode(jsonObject1.optString("modelCode"));
                                        orderEntity.setProductCode(jsonObject1.optString("productCode"));
                                        orderEntity.setProductTitle(jsonObject1.optString("productTitle"));
                                        orderEntity.setRechSubAmount(jsonObject1.optString("rechSubAmount"));
                                        orderEntity.setRelName(jsonObject1.optString("relName"));
                                        orderEntity.setSubAmount(jsonObject1.optString("subAmount"));
                                        orderEntity.setSubCode(jsonObject1.optString("subCode"));
                                        orderEntity.setSubId(jsonObject1.optString("subId"));
                                        orderEntity.setSubOrderNo(jsonObject1.optString("subOrderNo"));
                                        orderEntity.setSubStatus(jsonObject1.optString("subStatus"));
                                        orderEntity.setSubType(jsonObject1.optString("subType"));
                                        orderEntity.setSurplusAmount(jsonObject1.optString("surplusAmount"));
                                        orderEntity.setTotalBonusAmount(jsonObject1.optString("totalBonusAmount"));
                                        orderEntity.setUserCode(jsonObject1.optString("userCode"));
                                        orderEntity.setUseBonusAmount(jsonObject1.optString("useBonusAmount"));
                                        orderEntity.setFirstPhaseTime(jsonObject1.optString("firstPhaseTime"));
                                        orderEntity.setCarImgPath(jsonObject1.optString("carImgPath"));
                                        list.add(orderEntity);
                                    }
                                    if(adapter==null){
                                        adapter=new Fragment_Rengou_Adapter(getActivity(),list);
                                        adapter.setOnItemClickListnner(new Fragment_Rengou_Adapter.OnItemClickListnner() {
                                            @Override
                                            public void onClick(int position) {
                                                Intent intent=new Intent(getActivity(), InverstDetailActivity.class);
                                                intent.putExtra("productCode",list.get(position).getProductCode());
                                                intent.putExtra("subCode",list.get(position).getSubCode());
                                                intent.putExtra("subType",list.get(position).getSubType());
                                                startActivity(intent);
                                            }
                                        });
                                        pull.setAdapter(adapter);
                                    }else{
                                        if(pageNum==1){
                                            adapter.refreshData(list);
                                        }else{
                                            adapter.loadData(list);
                                        }
                                    }
                                }else{
                                    if(adapter==null){
                                        img_nodata.setVisibility(View.VISIBLE);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else{
                            if(adapter==null){
                                img_nodata.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        pull.onRefreshComplete();
                        Log.i("","");
                    }
                });
    }

}
