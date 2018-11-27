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
import www.qisu666.com.adapter.InverstAdapter;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.entity.InverstEntity;
import www.qisu666.com.entity.InverstProductEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.CircleProgressBar;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.sdk.partner.bean.TermBean;

/**
 * 717219917@qq.com 2018/8/13 17:54.
 */
public class InverstActivity extends BaseActivity {

    @BindView(R.id.pull_refresh_load_recycler_view)
    PullToRefreshListView pull_refresh_load_recycler_view;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;

    private int pageNum=1;
    private int pageSize=10;

    private InverstAdapter adapter;
    private InverstEntity entity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_inverst_layout);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNum=1;
//        if (UserParams.INSTANCE.checkLogin(this)) {
            initData();
//        }else{
//            startActivity(new Intent(this,LoginActivity.class));
//        }
    }

    private void initView(){
        tv_title.setText("投资认购");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pull_refresh_load_recycler_view.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_load_recycler_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新时会回调的方法
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum = 1;
//                if (UserParams.INSTANCE.checkLogin(InverstActivity.this)) {
                    initData();
//                }else{
//                    startActivity(new Intent(InverstActivity.this,LoginActivity.class));
//                }
            }

            //上啦加载时执行的方法
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageNum++;
//                if (UserParams.INSTANCE.checkLogin(InverstActivity.this)) {
                    initData();
//                }else{
//                    startActivity(new Intent(InverstActivity.this,LoginActivity.class));
//                }
            }
        });

    }

    private void initData(){
        String url = "api/vip/product/list/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum+"");
        map.put("pageSize", pageSize+"");
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
//                .compose(RxNetHelper.<TermBean>io_main())
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))            //可以去掉object
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        pull_refresh_load_recycler_view.onRefreshComplete();
                        if(bean!=null){
                            String jsonString=JsonUtils.objectToJson(bean);
                            try {
                                JSONObject jsonObject=new JSONObject(jsonString);
                                entity=new InverstEntity();
                                entity.setAllTotalNumber(jsonObject.optString("allTotalNumber"));
                                entity.setHasSubscribeNumber(jsonObject.optString("hasSubscribeNumber"));
                                entity.setProductNumbers(jsonObject.optString("productNumbers"));
                                entity.setRemainNumber(jsonObject.optString("remainNumber"));
                                JSONArray jsonArray=jsonObject.optJSONArray("productList");
                                if(jsonArray!=null&&jsonArray.length()!=0){
                                    List<InverstProductEntity> list=new ArrayList<>();
                                    for(int i=0;i<jsonArray.length();i++){
                                        InverstProductEntity productEntity=new InverstProductEntity();
                                        productEntity.setBalance(jsonArray.optJSONObject(i).optString("balance"));
                                        productEntity.setCarCode(jsonArray.optJSONObject(i).optString("carCode"));
                                        productEntity.setCarImgPath(jsonArray.optJSONObject(i).optString("carImgPath"));
                                        productEntity.setCarSeatNum(jsonArray.optJSONObject(i).optString("carSeatNum"));
                                        productEntity.setPeriod(jsonArray.optJSONObject(i).optString("period"));
                                        productEntity.setPlateNumber(jsonArray.optJSONObject(i).optString("plateNumber"));
                                        productEntity.setProductCode(jsonArray.optJSONObject(i).optString("productCode"));
                                        productEntity.setProductStatus(jsonArray.optJSONObject(i).optString("productStatus"));
                                        productEntity.setProductTitle(jsonArray.optJSONObject(i).optString("productTitle"));
                                        productEntity.setProductType(jsonArray.optJSONObject(i).optString("productType"));
                                        productEntity.setTotalAmount(jsonArray.optJSONObject(i).optString("totalAmount"));
                                        productEntity.setSurplusNumber(jsonArray.optJSONObject(i).optString("surplusNumber"));
                                        productEntity.setSubRebate(jsonArray.optJSONObject(i).optString("subRebate"));
                                        productEntity.setROWNO(jsonArray.optJSONObject(i).optString("ROWNO"));
                                        productEntity.setProductTypeCn(jsonArray.optJSONObject(i).optString("productTypeCn"));
                                        productEntity.setSubAmount(jsonArray.optJSONObject(i).optString("subAmount"));
                                        productEntity.setProductNumber(jsonArray.optJSONObject(i).optString("productNumber"));
                                        list.add(productEntity);
                                    }
                                    entity.setList(list);
                                    if(adapter==null){
                                        adapter=new InverstAdapter(InverstActivity.this,entity);
                                        adapter.setOnClickListenner(new InverstAdapter.OnClickListenner() {
                                            @Override
                                            public void onClick(int position) {
                                                if(position>=0){
                                                    if(entity!=null&&entity.getList()!=null){
                                                        if (UserParams.INSTANCE.checkLogin(InverstActivity.this)) {
                                                            Intent carInverstIntent=new Intent(InverstActivity.this,CarInverstActivity.class);
                                                            carInverstIntent.putExtra("productCode",adapter.getList().get(position).getProductCode());
                                                            carInverstIntent.putExtra("productType",adapter.getList().get(position).getProductType());
                                                            startActivity(carInverstIntent);
                                                        }else{
                                                            startActivity(new Intent(InverstActivity.this,LoginActivity.class));
                                                        }

                                                    }
                                                }
                                            }
                                        });
                                        pull_refresh_load_recycler_view.setAdapter(adapter);
                                    }else{
                                        if(pageNum==1){
                                            adapter.refreshData(entity);
                                        }else{
                                            adapter.loadData(entity);
                                        }
                                    }
                                }{
                                    if(adapter==null){
                                        entity.setList(new ArrayList<InverstProductEntity>());
                                        adapter=new InverstAdapter(InverstActivity.this,entity);
                                        adapter.setOnClickListenner(new InverstAdapter.OnClickListenner() {
                                            @Override
                                            public void onClick(int position) {
                                                if(position>=0){
                                                    if(entity!=null&&entity.getList()!=null){
                                                        if (UserParams.INSTANCE.checkLogin(InverstActivity.this)) {
                                                            Intent carInverstIntent=new Intent(InverstActivity.this,CarInverstActivity.class);
                                                            carInverstIntent.putExtra("productCode",entity.getList().get(position).getProductCode());
                                                            carInverstIntent.putExtra("productType",entity.getList().get(position).getProductType());
                                                            startActivity(carInverstIntent);
                                                        }else{
                                                            startActivity(new Intent(InverstActivity.this,LoginActivity.class));
                                                        }

                                                    }
                                                }
                                            }
                                        });
                                        pull_refresh_load_recycler_view.setAdapter(adapter);
                                    } else{
                                        if(pageNum==1){
                                            entity.setList(adapter.getList());
                                            adapter.refreshData(entity);
                                        }else{
                                            entity.setList(new ArrayList<InverstProductEntity>());
                                            adapter.loadData(entity);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        pull_refresh_load_recycler_view.onRefreshComplete();
                        Log.i("","");
                    }
                });
    }

}
