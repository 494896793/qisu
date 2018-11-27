package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MessageUtil;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.entity.CarInverstEntity;
import www.qisu666.com.entity.InverstDetailEntity;
import www.qisu666.com.entity.SubscribeinfoEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DateUtils;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.sdk.partner.Activity_CarBuy;

/**
 * 717219917@qq.com 2018/8/15 10:33.
 */
public class InverstDetailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tx_dl)
    TextView tx_dl;
    @BindView(R.id.tx_color)
    TextView tx_color;
    @BindView(R.id.tx_city)
    TextView tx_city;
    @BindView(R.id.tx_carnum)
    TextView tx_carnum;
    @BindView(R.id.tx_driver)
    TextView tx_driver;
    @BindView(R.id.tx_info)
    TextView tx_info;
    @BindView(R.id.tx_statu)
    TextView tx_statu;
    @BindView(R.id.tx_inverst_type)
    TextView tx_inverst_type;
    @BindView(R.id.tx_inverst_time)
    TextView tx_inverst_time;
    @BindView(R.id.tx_inverst_endtime)
    TextView tx_inverst_endtime;
    @BindView(R.id.tx_inverst_receive_time)
    TextView tx_inverst_receive_time;
    @BindView(R.id.tx_inverst_money_time)
    TextView tx_inverst_money_time;
    @BindView(R.id.tx_contract_statu)
    TextView tx_contract_statu;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.car_brand)
    TextView car_brand;
    @BindView(R.id.relieve_time)
    TextView relieve_time;
    @BindView(R.id.tx_contract_title)
    TextView tx_contract_title;
    @BindView(R.id.re_contract)
    RelativeLayout re_contract;
    @BindView(R.id.first_re2)
    RelativeLayout first_re2;
    @BindView(R.id.first_re)
    RelativeLayout first_re;

    private String productCode;
    private String subCode;
    private String subType;
    private InverstDetailEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_inverstdetail_layout);
        initIntent();
        initLayout();
        if(productCode!=null&&subCode!=null&&subType!=null){
            initData();
        }
    }

    private void initLayout(){
        img_title_left.setOnClickListener(this);
        re_contract.setOnClickListener(this);
        tv_title.setText("认购详情");
    }

    private void initIntent(){
        productCode=getIntent().getStringExtra("productCode");
        subCode=getIntent().getStringExtra("subCode");
        subType=getIntent().getStringExtra("subType");

        if(subType.equals("1")){    //投资型
            first_re2.setVisibility(View.VISIBLE);
            first_re.setVisibility(View.VISIBLE);
        }else{
            first_re2.setVisibility(View.GONE);
            first_re.setVisibility(View.GONE);
        }
    }

    private void initData(){
        String url="api/vip/user/subscribe/sigle";
        HashMap<String,Object> map=new HashMap<>();
        map.put("productCode",productCode);
        map.put("subCode",subCode);
        map.put("subType",subType);
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        MyNetwork.getMyApi().carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        if(bean!=null){
                            String jsonString= JsonUtils.objectToJson(bean);
                            if(jsonString!=null&&!jsonString.equals("")){
                                try{
                                    JSONObject jsonObject=new JSONObject(jsonString);
                                    JSONObject jsonObject1=jsonObject.optJSONObject("subscribeInfo");
                                    entity=new InverstDetailEntity();
                                    if(jsonObject1!=null){
                                        SubscribeinfoEntity subscribeinfoEntity=new SubscribeinfoEntity();
                                        subscribeinfoEntity.setCanCancleTime(jsonObject1.optString("canCancleTime"));
                                        subscribeinfoEntity.setContractExpiresTime(jsonObject1.optString("contractExpiresTime"));
                                        subscribeinfoEntity.setContractStatus(jsonObject1.optString("contractStatus"));
                                        subscribeinfoEntity.setFirstPhaseTime(jsonObject1.optString("firstPhaseTime"));
                                        subscribeinfoEntity.setSubStatus(jsonObject1.optString("subStatus"));
                                        subscribeinfoEntity.setSubTime(jsonObject1.optString("subTime"));
                                        subscribeinfoEntity.setSubType(jsonObject1.optString("subType"));
                                        subscribeinfoEntity.setUseBonusAmount(jsonObject1.optString("useBonusAmount"));
                                        entity.setSubscribeinfoEntity(subscribeinfoEntity);
                                    }
                                    JSONObject jsonObject2=jsonObject.optJSONObject("carInfoMap");
                                    if(jsonObject2!=null){
                                        CarInverstEntity carInverstEntity=new CarInverstEntity();
                                        carInverstEntity.setProductTypeCn(jsonObject2.optString("productTypeCn"));
                                        carInverstEntity.setProductType(jsonObject2.optString("productType"));
                                        carInverstEntity.setProductCode(jsonObject2.optString("productCode"));
                                        carInverstEntity.setProductTitle(jsonObject2.optString("productTitle"));
                                        carInverstEntity.setPlateNumber(jsonObject2.optString("plateNumber"));
                                        carInverstEntity.setProductNumber(jsonObject2.optString("productNumber"));
                                        carInverstEntity.setProductStatus(jsonObject2.optString("productStatus"));
                                        carInverstEntity.setSubRebate(jsonObject2.optString("subRebate"));
                                        carInverstEntity.setSubAmount(jsonObject2.optString("subAmount"));
                                        carInverstEntity.setSurplusNumber(jsonObject2.optString("surplusNumber"));
                                        carInverstEntity.setVinNo(jsonObject2.optString("vinNo"));
                                        carInverstEntity.setBalance(jsonObject2.optString("balance"));
                                        carInverstEntity.setCarCode(jsonObject2.optString("carCode"));
                                        carInverstEntity.setColor(jsonObject2.optString("color"));
                                        carInverstEntity.setWithdrawPeriods(jsonObject2.optString("withdrawPeriods"));
                                        carInverstEntity.setPeriod(jsonObject2.optString("period"));
                                        carInverstEntity.setCityCode(jsonObject2.optString("cityCode"));
                                        carInverstEntity.setCarImgPath(jsonObject2.optString("carImgPath"));
                                        carInverstEntity.setTotalAmount(jsonObject2.optString("totalAmount"));
                                        carInverstEntity.setCityName(jsonObject2.optString("cityName"));
                                        carInverstEntity.setCarWpmi(jsonObject2.optString("carWpmi"));
                                        entity.setCarInverstEntity(carInverstEntity);
                                    }
                                    initResourse();
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {

                    }
                });
    }

    private void initResourse(){
        car_brand.setText(entity.getCarInverstEntity().getProductTitle());
        tx_color.setText(entity.getCarInverstEntity().getColor());
        tx_city.setText(entity.getCarInverstEntity().getCityName());
        tx_carnum.setText(entity.getCarInverstEntity().getPlateNumber());
        tx_driver.setText(entity.getCarInverstEntity().getCarWpmi());
        tx_info.setText(entity.getCarInverstEntity().getVinNo());
        if(entity.getSubscribeinfoEntity().getSubStatus().equals("1")){
            tx_statu.setText("投资中");
        }else{
            tx_statu.setText("投资结束");
        }
        if(entity.getSubscribeinfoEntity().getSubType().equals("1")){       //投资型
            tx_inverst_type.setText("投资型");
            tx_contract_title.setText("投资型合同");
        }else{
            tx_inverst_type.setText("消费型");
            tx_contract_title.setText("消费型合同");
        }
        tx_inverst_time.setText(entity.getSubscribeinfoEntity().getSubTime());
        tx_inverst_endtime.setText(entity.getSubscribeinfoEntity().getContractExpiresTime());
        tx_inverst_receive_time.setText(entity.getSubscribeinfoEntity().getFirstPhaseTime());
        tx_inverst_money_time.setText(entity.getSubscribeinfoEntity().getUseBonusAmount());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String time=simpleDateFormat.format(new Date());
        if(DateUtils.isDateOneBigger(time,entity.getSubscribeinfoEntity().getCanCancleTime())){
            relieve_time.setText("");
            tx_contract_statu.setText("可解除");
        }else{
            relieve_time.setText("("+entity.getSubscribeinfoEntity().getCanCancleTime()+"后可解除)");
            tx_contract_statu.setText("不可解除");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_left:
                finish();
                break;
            case R.id.re_contract:
                if(entity.getSubscribeinfoEntity().getSubType().equals("1")){       //投资型
                    Intent i = new Intent(InverstDetailActivity.this, CommonWebViewActivity.class);
                    i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                    i.putExtra("act_title", "投资型合同样本");
                    startActivity(i);
                }else{
                    Intent i = new Intent(InverstDetailActivity.this, CommonWebViewActivity.class);
                    i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                    i.putExtra("act_title", "消费型合同样本");
                    startActivity(i);
                }
                break;
        }
    }
}
