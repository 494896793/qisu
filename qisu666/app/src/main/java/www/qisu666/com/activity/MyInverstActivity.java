package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.adapter.Fragment_Rengou_Adapter;
import www.qisu666.com.adapter.MyInverstAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.entity.InverstOrderEntity;
import www.qisu666.com.entity.MyInverstEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;

/**
 * 717219917@qq.com 2018/8/15 15:05.
 */
public class MyInverstActivity extends BaseActivity implements View.OnClickListener{

    private int pageNum=1;
    private int pageSize=10;
    private MyInverstEntity entity;
    private MyInverstAdapter adapter;
    @BindView(R.id.pull)
    PullToRefreshListView pull;
    @BindView(R.id.img_nodata)
    ImageView img_nodata;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_myinverst_layout);
        initlisetnner();
        initData();
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
                        if(bean!=null&&!bean.toString().equals("")){
                            String jsonString= JsonUtils.objectToJson(bean);
                            try{
                                entity=new  MyInverstEntity();
                                JSONObject jsonObject=new JSONObject(jsonString);
                                entity.setMonthProfit(jsonObject.optString("monthProfit"));
                                entity.setReceiveMonthProfit(jsonObject.optString("receiveMonthProfit"));
                                entity.setTotalProfit(jsonObject.optString("totalProfit"));
                                entity.setTotalSubscribeCount(jsonObject.optString("totalSubscribeCount"));
                                JSONArray jsonArray=jsonObject.optJSONArray("userSubscribeList");
                                if(jsonArray!=null&&jsonArray.length()>0){
                                    img_nodata.setVisibility(View.GONE);
                                    List<InverstOrderEntity>list=new ArrayList<>();
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
                                    entity.setList(list);
                                    if(adapter==null){
                                        adapter=new MyInverstAdapter(entity,MyInverstActivity.this);
                                        pull.setAdapter(adapter);
                                    }else{
                                        if(pageNum==1){
                                            adapter.refreshData(entity);
                                        }else{
                                            adapter.loadData(entity);
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

    private void initlisetnner(){
        tv_title.setText("我的认购");

        img_title_left.setOnClickListener(this);
        pull.setMode(PullToRefreshBase.Mode.BOTH);
        pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum++;
                initData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
        }
    }
}
