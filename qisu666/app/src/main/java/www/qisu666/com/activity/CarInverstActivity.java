package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import butterknife.BindView;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.entity.CarInverstEntity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.bean.Bean_TotalAmount;

/**
 * 717219917@qq.com 2018/8/14 13:44.
 */
public class CarInverstActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.car_brand)
    TextView car_brand;
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
    @BindView(R.id.tx_money)
    TextView tx_money;
    @BindView(R.id.type_img)
    ImageView type_img;
    @BindView(R.id.tx_canCancleTime)
    TextView tx_canCancleTime;
    @BindView(R.id.tx_firstPhaseTime)
    TextView tx_firstPhaseTime;
    @BindView(R.id.tx_contractExpiresTime)
    TextView tx_contractExpiresTime;
    @BindView(R.id.tx_surplusNumber)
    TextView tx_surplusNumber;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.tx_number)
    TextView tx_number;
    @BindView(R.id.img_reduce)
    ImageView img_reduce;
    @BindView(R.id.img_add)
    ImageView img_add;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_title_left)
    ImageView img_title_left;
    @BindView(R.id.re_contract)
    RelativeLayout re_contract;
    @BindView(R.id.tz_re)
    RelativeLayout tz_re;
    @BindView(R.id.xf_re)
    RelativeLayout xf_re;
    @BindView(R.id.xf_canCancleTime)
    TextView xf_canCancleTime;
    @BindView(R.id.xf_contractExpiresTime)
    TextView xf_contractExpiresTime;

    private String productCode;
    private CarInverstEntity entity;
    private String productType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_carinverst_layout);
        productCode=getIntent().getStringExtra("productCode");
        productType=getIntent().getStringExtra("productType");
        initLayout();
        initListenner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserParams.INSTANCE.checkLogin(CarInverstActivity.this)) {
            initData();
        }else{
            startActivity(new Intent(CarInverstActivity.this,LoginActivity.class));
        }
    }

    private void initLayout(){
        tv_title.setText("汽车认购");
    }

    private void initData(){
        String url="api/vip/product/detail/query";
        HashMap<String,Object> map=new HashMap<>();
        map.put("productCode",productCode);
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
                            try {
                                JSONObject jsonObject=new JSONObject(jsonString);
                                entity=new CarInverstEntity();
                                entity.setBalance(jsonObject.optString("balance"));
                                entity.setCanSubscribeCount(jsonObject.optString("canSubscribeCount"));
                                entity.setCarCode(jsonObject.optString("carCode"));
                                entity.setCarImgPath(jsonObject.optString("carImgPath"));
                                entity.setCarWpmi(jsonObject.optString("carWpmi"));
                                entity.setCityCode(jsonObject.optString("cityCode"));
                                entity.setCityName(jsonObject.optString("cityName"));
                                entity.setColor(jsonObject.optString("color"));
                                entity.setContractExpiresTime(jsonObject.optString("contractExpiresTime"));
                                entity.setCurrentDate(jsonObject.optString("currentDate"));
                                entity.setFirstPhaseTime(jsonObject.optString("firstPhaseTime"));
                                entity.setPeriod(jsonObject.optString("period"));
                                entity.setWithdrawPeriods(jsonObject.optString("withdrawPeriods"));
                                entity.setVinNo(jsonObject.optString("vinNo"));
                                entity.setTotalAmount(jsonObject.optString("totalAmount"));
                                entity.setSurplusNumber(jsonObject.optString("surplusNumber"));
                                entity.setSubscribeMax(jsonObject.optString("subscribeMax"));
                                entity.setSubscribeCount(jsonObject.optString("subscribeCount"));
                                entity.setSubAmount(jsonObject.optString("subAmount"));
                                entity.setSubRebate(jsonObject.optString("subRebate"));
                                entity.setSubscribeType(jsonObject.optString("subscribeType"));
                                entity.setCanCancleTime(jsonObject.optString("canCancleTime"));
                                entity.setProductType(jsonObject.optString("productType"));
                                entity.setProductStatus(jsonObject.optString("productStatus"));
                                entity.setProductNumber(jsonObject.optString("productNumber"));
                                entity.setPlateNumber(jsonObject.optString("plateNumber"));
                                entity.setProductTitle(jsonObject.optString("productTitle"));
                                entity.setProductCode(jsonObject.optString("productCode"));
                                entity.setProductTypeCn(jsonObject.optString("productTypeCn"));
                                entity.setSubscribeMax(jsonObject.optString("subscribeMax"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            tx_color.setText(entity.getColor());
                            tx_city.setText(entity.getCityName());
                            tx_carnum.setText(entity.getPlateNumber());
                            if(entity.getCarWpmi()!=null&&!entity.getCarWpmi().toString().equals("")){
                                tx_driver.setText(entity.getCarWpmi().substring(0,5)+"****"+entity.getCarWpmi().substring(9,entity.getCarWpmi().length()));
                            }else{
                                tx_driver.setText("");
                            }
                            if(entity.getVinNo()!=null&&!entity.getVinNo().toString().equals("")){
                                tx_info.setText(entity.getVinNo().substring(0,3)+"***********"+entity.getVinNo().substring(14,entity.getVinNo().length()));
                            }else{
                                tx_info.setText("");
                            }
                            if(entity.getTotalAmount()!=null){
                                tx_money.setText(Integer.valueOf(entity.getTotalAmount())/10000+"万");
                            }else{
                                tx_money.setText("");
                            }
                            if(entity.getProductType().equals("1")){       //投资型
                                type_img.setImageResource(R.mipmap.rg_16);
                                tz_re.setVisibility(View.VISIBLE);
                                xf_re.setVisibility(View.GONE);
                            }else{
                                tz_re.setVisibility(View.GONE);
                                xf_re.setVisibility(View.VISIBLE);
                                type_img.setImageResource(R.mipmap.rg_17);
                            }
                            if(entity.getProductTitle()!=null){
                                car_brand.setText(entity.getProductTitle());
                            }else{
                                car_brand.setText("");
                            }
                            if(entity.getFirstPhaseTime()!=null){
                                tx_firstPhaseTime.setText(entity.getFirstPhaseTime());
                            }else{
                                tx_firstPhaseTime.setText("");
                            }
                            if(entity.getCanCancleTime()!=null){
                                tx_canCancleTime.setText(entity.getCanCancleTime());
                                xf_canCancleTime.setText(entity.getCanCancleTime());
                            }else{
                                tx_canCancleTime.setText("");
                            }
                            if(entity.getContractExpiresTime()!=null){
                                tx_contractExpiresTime.setText(entity.getContractExpiresTime());
                                xf_contractExpiresTime.setText(entity.getContractExpiresTime());
                            }else{
                                tx_contractExpiresTime.setText("");
                            }
                            if(entity.getSurplusNumber()!=null){
                                tx_surplusNumber.setText(entity.getSurplusNumber()+"份");
                            }else{
                                tx_surplusNumber.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        Log.i("=","");
                    }
                });
    }

    private void initListenner(){
        img_title_left.setOnClickListener(this);
        submit.setOnClickListener(this);
        img_reduce.setOnClickListener(this);
        img_add.setOnClickListener(this);
        re_contract.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if(Integer.valueOf(tx_number.getText().toString())>0){
                    if (UserParams.INSTANCE.checkLogin(CarInverstActivity.this)) {
                        requestPay();
                    }else{
                        startActivity(new Intent(CarInverstActivity.this,LoginActivity.class));
                    }
                }else{
                    ToastUtil.showToast("请先选择认购份数");
                }
                break;
            case R.id.img_add:
                if(entity.getProductType().equals("1")){        //投资型
                    if(Integer.valueOf(tx_number.getText().toString())<Integer.valueOf(entity.getSurplusNumber())){
                        tx_number.setText((Integer.valueOf(tx_number.getText().toString())+1)+"");
                    }
                }else{
                     if(Integer.valueOf(tx_number.getText().toString())<Integer.valueOf(entity.getSubscribeMax())){
                             tx_number.setText((Integer.valueOf(tx_number.getText().toString())+1)+"");
                     }
                }
                break;
            case R.id.img_reduce:
                if(Integer.valueOf(tx_number.getText().toString())>0){
                    tx_number.setText((Integer.valueOf(tx_number.getText().toString())-1)+"");
                }
                break;
            case R.id.img_title_left:
                finish();
                break;
            case R.id.re_contract:
                if(entity.getProductType().equals("1")){
                    Intent i = new Intent(CarInverstActivity.this, CommonWebViewActivity.class);
                    i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                    i.putExtra("act_title", "投资型合同样本");
                    startActivity(i);
                }else{
                    Intent i = new Intent(CarInverstActivity.this, CommonWebViewActivity.class);
                    i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                    i.putExtra("act_title", "消费型合同样本");
                    startActivity(i);
                }
                break;
        }
    }

    //获取总金额
    private void requestPay() {
        String url = "api/vip/product/subscribe";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());
        map.put("productCode",entity.getProductCode());
        map.put("subAmount", entity.getSubAmount());
        map.put("subCount",tx_number.getText().toString());
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_TotalAmount.class))
                .compose(RxNetHelper.<Bean_TotalAmount>io_main())
                .subscribe(new ResultSubscriber<Bean_TotalAmount>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(Bean_TotalAmount bean) {
                        LogUtil.e(" 认购 获取到的 总计金额"+bean.totalSubAmount);
                        LogUtil.e(" 认购 获取到的 用户编码"+bean.userCode);
                        if (UserParams.INSTANCE.checkLogin(CarInverstActivity.this)) {
                            Intent intent = new Intent(CarInverstActivity.this, PayforActivity.class);
                            intent.putExtra("totalFee",bean.totalSubAmount);
                            intent.putExtra("productCode",entity.getProductCode());
                            intent.putExtra("subAmount",entity.getSubAmount());
                            intent.putExtra("subCount",tx_number.getText().toString());
                            intent.putExtra("subType",productType);
                            startActivity(intent);  //认购付款
                            finish();
                        }else{
                            startActivity(new Intent(CarInverstActivity.this,LoginActivity.class));
                        }
                    }
                    @Override public void onFail(www.qisu666.com.carshare.Message<Bean_TotalAmount> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }


}
