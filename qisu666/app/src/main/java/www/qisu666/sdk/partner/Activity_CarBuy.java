package www.qisu666.sdk.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import www.qisu666.com.R;
import www.qisu666.com.activity.CommonWebViewActivity;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.bean.Event_PayRequest;
import www.qisu666.sdk.partner.bean.Product;
import www.qisu666.sdk.partner.bean.TermBean;
import www.qisu666.sdk.partner.widget.AmountView;
import www.qisu666.sdk.partner.widget.UnderLineLinearLayout;

/**  汽车认购
 * 717219917@qq.com ${DATA} 16:36.
 */
public class Activity_CarBuy extends BaseActivity {

    @BindView(R.id.carbuy_banner) Banner carbuy_banner ;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.img_title_left)ImageView img_title_left;

    @BindView(R.id.amount_view)   AmountView mAmountView;//购物车 + - 数量的view
    @BindView(R.id.carbuy_btn_login) Button carbuy_btn_login;//立即认购

    @BindView(R.id.carbuy_model)TextView carbuy_model;//汽车标题
    @BindView(R.id.carbuy_yanse)TextView carbuy_yanse;//汽车颜色  颜色：白
    @BindView(R.id.carbuy_chengshi)TextView carbuy_chengshi;//城市编码   所属城市：深圳
    @BindView(R.id.carbuy_chepaihao)TextView carbuy_chepaihao;//车牌号
    @BindView(R.id.carbuy_xingshizhenghao)TextView carbuy_xingshizhenghao;//行驶证号
    @BindView(R.id.carbuy_chejiahao)TextView carbuy_chejiahao;            //车架号
    @BindView(R.id.car_buy_status)TextView car_buy_status;                //认购类型
    @BindView(R.id.carbuy_money)TextView carbuy_money;                    //参考价格
    @BindView(R.id.carbuy_Surplus)TextView carbuy_Surplus;                //剩余份数
    @BindView(R.id.carbuy_hetongyangben)TextView carbuy_hetongyangben;    //投资型合同样本
    @BindView(R.id.carbuy_hetongyangben_img)ImageView carbuy_hetongyangben_img;//合同样本的img
    @BindView(R.id.carbuy_shangxian)TextView carbuy_shangxian;                 //达到投资上限txt
    @BindView(R.id.carbuy_shangxian_img)ImageView carbuy_shangxian_img;         //投资上限img


    @BindView(R.id.carbuy_timeline)ImageView carbuy_timeline;                         //时间轴图片 公用
    @BindView(R.id.carbuy_layout_3)LinearLayout carbuy_layout_3;                      //时间轴3个的
    @BindView(R.id.carbuy_layout3_left)TextView carbuy_layout3_left;                  //3的左边  认购
    @BindView(R.id.carbuy_layout3_center1_time)TextView carbuy_layout3_center1_time;  //3的中间 -时间
    @BindView(R.id.carbuy_layout3_center1_txt)TextView carbuy_layout3_center1_txt;    //3的中间 =文字
    @BindView(R.id.carbuy_layout3_right_time)TextView carbuy_layout3_right_time;      //3的右边 -时间
    @BindView(R.id.carbuy_layout3_right_txt)TextView carbuy_layout3_right_txt;        //3的右边 -文字

    @BindView(R.id.carbuy_layout_4)LinearLayout carbuy_layout_4;                       //时间轴4个的
    @BindView(R.id.carbuy_layout4_left)TextView carbuy_layout4_left;                   //4的左边  认购
    @BindView(R.id.carbuy_layout4_center1_time)TextView carbuy_layout4_center1_time;   //4的中间  时间
    @BindView(R.id.carbuy_layout4_center1_txt)TextView carbuy_layout4_center1_txt;     //4的中间  文字
    @BindView(R.id.carbuy_layout4_center2_time)TextView carbuy_layout4_center2_time;   //4的中间 -时间
    @BindView(R.id.carbuy_layout4_center2_txt)TextView carbuy_layout4_center2_txt;     //4的中间 -文字
    @BindView(R.id.carbuy_layout4_right_time)TextView carbuy_layout4_right_time;       //4的右边 -时间
    @BindView(R.id.carbuy_layout4_right_txt)TextView carbuy_layout4_right_txt;         //4的右边 -文字


    ArrayList<String> images= new ArrayList<>();

    private static final int[] LINE_GRAVITY={UnderLineLinearLayout.GRAVITY_TOP,UnderLineLinearLayout.GRAVITY_MIDDLE, UnderLineLinearLayout.GRAVITY_BOTTOM};
    private static final String[] LINE_GRAVITY_STR={"TOP","MIDDLE","BOTTOM"};
    int t=1;
    int i = 0;

    String productCode="";

    Product bean=null;

    /**认购份数量*/
    String subCount="";

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setView(R.layout.activity_carbuy);
        initView();
        LogUtil.e("获取到的prductCode："+productCode);
    }


   void initView(){
       tv_title.setText("汽车认购");
//       carbuy_shangxian.setVisibility(View.INVISIBLE);    //投资上限  隐藏 占用布局位置
//       carbuy_shangxian_img.setVisibility(View.INVISIBLE);//投资上限
       img_title_left.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View view) {
                    finish();
           }});
       carbuy_hetongyangben_img.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View view) {
               if (bean==null){return;}
               if (bean.getProductType().contains("2")){//消费型
                   Intent i = new Intent(Activity_CarBuy.this, CommonWebViewActivity.class);
                   i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                   i.putExtra("act_title", "消费型合同样本");
                   startActivity(i);
               }else {//投资型
                   Intent i = new Intent(Activity_CarBuy.this, CommonWebViewActivity.class);
                   i.putExtra("act_url", "file:///android_asset/argu/argu.html");
                   i.putExtra("act_title", "投资型合同样本");
                   startActivity(i);
               }
           }
       });

       mAmountView = (AmountView) findViewById(R.id.amount_view);
//       if (bean.getCanSubscribeCount()<=0){
//           mAmountView.setGoods_storage(bean.getCanSubscribeCount());
//       }else {
//           mAmountView.setGoods_storage(10);
//       }
       carbuy_btn_login.setClickable(false);                                   //认购按钮点击状态
       carbuy_btn_login.setBackgroundResource(R.color.not_login);//认购按钮ui  初始化

       mAmountView.setGoods_storage(0);
       mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
           @Override public void onAmountChange(View view, int amount) {
                     subCount=amount+"";
                   carbuy_btn_login.setClickable(amount>0);                                                 //认购按钮点击状态
                   carbuy_btn_login.setBackgroundResource(amount>0 ? R.color.can_login : R.color.not_login);//认购按钮ui
//                  if (amount>10){ToastUtil.showToast("最多认购十份！");}
           }
       });

       RxView.clicks(carbuy_btn_login)  //前往认购按钮
               .throttleFirst(3, TimeUnit.SECONDS)//3秒内不允许多次点击
               .subscribe(new Action1<Void>() {
                   @Override public void call(Void aVoid) {
                       if (carbuy_btn_login.isClickable()){//是否可以点击状态
//                           ToastUtil.showToast("点击立即认购");
//                         startActivity(new Intent(Activity_CarBuy.this, Activity_CarbuyComplete.class));//认购成功
                           if (bean==null){return;}
                           Event_PayRequest payRequest = new Event_PayRequest();
                           payRequest.userId=UserParams.INSTANCE.getUser_id();
                           payRequest.productCode=productCode;
                           payRequest.subType=bean.getProductType();
                           payRequest.subCount=subCount;
                           payRequest.subAmount=bean.getSubAmount()+"";
                           payRequest.carbuy_model=bean.getProductTitle();
                          try{ payRequest.totalFee=(Integer.parseInt(payRequest.subCount)*Integer.parseInt(payRequest.subAmount))+"";}catch (Throwable t){t.printStackTrace();}
                           EventBus.getDefault().postSticky(payRequest);
                           Intent intent = new Intent(Activity_CarBuy.this, Activity_CarBuy_Payway.class);
                           intent.putExtra("num",subCount);
                           intent.putExtra("type",bean.getProductTypeCn());
                           startActivity(intent);  //认购付款
                           finish();
                       }
                   } });

       productCode=getIntent().getStringExtra("productCode");//获取到prductCode
       LogUtil.e("接收到的产品编码："+productCode);
       initData();
   }


   void initData(){
       carbuy_btn_login.setClickable(false);                                   //认购按钮点击状态
       carbuy_btn_login.setBackgroundResource(R.color.not_login);
//       images.add(Config.IMAGE_HOST+bean.getCarImgPath());
       carbuy_banner.setImageLoader(new GlideImageLoader());//设置图片加载器
       carbuy_banner.setImages(images);  //设置图片集合
//       carbuy_banner.start();            //banner设置方法全部调用完毕时最后调用
       connectServer();
   }


   /**消费型*/
   void consume(Product bean){
       carbuy_layout_3.setVisibility(View.VISIBLE);
       carbuy_layout_4.setVisibility(View.GONE);
      Glide.with(this).load(R.mipmap.carbuy_timeline3).into(carbuy_timeline);
       SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");

       carbuy_layout3_center1_time.setText(myFmt.format(bean.getCanCancleTime()));
       carbuy_layout3_right_time.setText(myFmt.format(bean.getContractExpiresTime()));
       carbuy_hetongyangben.setText("消费型合同样本");


       if (bean.getSubscribeType().equals("")){
           mAmountView.setGoods_storage(bean.getSubscribeMax());
           carbuy_Surplus.setText("剩余份数："+bean.getSurplusNumber());
           carbuy_shangxian.setText("温馨提示：您还可以认购"+bean.getSubscribeMax()+"份消费型投资！");
//           carbuy_shangxian_img.setVisibility(View.GONE);
//           carbuy_shangxian.setVisibility(View.GONE);
       }else {
           double subcritype=Double.parseDouble(bean.getSubscribeType());
           if (Double.valueOf(subcritype).intValue()==1){//投资型
               carbuy_shangxian.setText("温馨提示：您有投资型投资未到期!");
               carbuy_Surplus.setText("剩余份数："+bean.getSurplusNumber());
           }else{
           carbuy_Surplus.setText("剩余份数："+bean.getCanSubscribeCount());
         if (bean.getCanSubscribeCount()<=0){//已经达到消费型上限
           mAmountView.setGoods_storage(0);
           carbuy_shangxian.setText("温馨提示：您已达到消费型投资数量上限！");  //还要限制按钮不可点击状体
       }else {
           carbuy_Surplus.setText("剩余份数："+bean.getCanSubscribeCount());
           mAmountView.setGoods_storage(bean.getCanSubscribeCount());
           carbuy_shangxian.setText("温馨提示：您还可以认购"+bean.getCanSubscribeCount()+"份消费型投资！");
       }
      }

       }

       carbuy_btn_login.setClickable(false);                                   //认购按钮点击状态
       carbuy_btn_login.setBackgroundResource(R.color.not_login);


       LogUtil.e("获取认购详情解除时间"+bean.getCanCancleTime());
       LogUtil.e("获取认购详情分红到账时间"+bean.getContractExpiresTime());
       LogUtil.e("获取认购详情当前时间"+bean.getCurrentDate());
   }


   /**投资型*/
   void invest(Product bean){
       carbuy_layout_4.setVisibility(View.VISIBLE);
       carbuy_layout_3.setVisibility(View.GONE);
       Glide.with(this).load(R.mipmap.carbuy_timeline).into(carbuy_timeline);
       SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");
       carbuy_layout4_center1_time.setText(myFmt.format(bean.getFirstPhaseTime()));
       carbuy_layout4_center2_time.setText(myFmt.format(bean.getCanCancleTime()));
       carbuy_layout4_right_time.setText(myFmt.format(bean.getContractExpiresTime()));
       carbuy_hetongyangben.setText("投资型合同样本");
//       mAmountView.setGoods_storage(99999);

       if (bean.getSubscribeType().equals("")){
           carbuy_Surplus.setText("剩余份数："+bean.getSurplusNumber());
           mAmountView.setGoods_storage(bean.getSurplusNumber());
           carbuy_shangxian.setVisibility(View.GONE);
           carbuy_shangxian_img.setVisibility(View.GONE);
           }else {
                   double subcribe=Double.parseDouble(bean.getSubscribeType());
                   if (new Double(subcribe).intValue()==2){//
                       carbuy_shangxian.setText("温馨提示：您有消费型投资未到期!");
                       carbuy_Surplus.setText("剩余份数："+bean.getSurplusNumber());
                       mAmountView.setGoods_storage(0);

                   }else {
                       carbuy_Surplus.setText("剩余份数："+bean.getCanSubscribeCount());
                       carbuy_shangxian.setVisibility(View.GONE);
                       carbuy_shangxian_img.setVisibility(View.GONE);
                       mAmountView.setGoods_storage(bean.getSurplusNumber());
        }

       }
       carbuy_btn_login.setClickable(false);                                   //认购按钮点击状态
       carbuy_btn_login.setBackgroundResource(R.color.not_login);
   }

    public class GlideImageLoader extends ImageLoader {
        @Override  public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(path).into(imageView);
        } //Glide 加载图片简单用法
    }

    //网络请求成功之后 更新ui
   void  update(Product bean){
       carbuy_model.setText(bean.getProductTitle());                        //标题 迈凯伦
       carbuy_chepaihao.setText("车牌号  ："+bean.getPlateNumber());
       carbuy_chengshi.setText( "城   市 ："+bean.getCityName());                //城市编码   所属城市：深圳

       String  tmp ="";
      try {
          int count  =0;
          tmp="";
          if (bean.getCarWpmi().length()>8){count=bean.getCarWpmi().length()-8;}else {count=4;}
          for (int a=0;a<count;a++) {
            tmp+="*";
          }
      }catch (Throwable t){t.printStackTrace();tmp="****";}

       try {
          String carWpmi_after = bean.getCarWpmi().substring(0, 4) + tmp+ bean.getCarWpmi().substring(bean.getCarWpmi().length()-4, bean.getCarWpmi().length());
          carbuy_xingshizhenghao.setText("行驶证号：" + carWpmi_after);         //行驶证号

      }catch (Throwable t){t.printStackTrace();}


       try {
           int count  =0;
           tmp="";
           if (bean.getVinNo().length()>8){count=bean.getVinNo().length()-8;} else {count=4;}
           for (int a=0;a<count;a++) {
               tmp+="*";
           }
       }catch (Throwable t){t.printStackTrace();tmp="****";}

      try {
          String vinNo_after = bean.getVinNo().substring(0, 4) + tmp + bean.getVinNo().substring(bean.getVinNo().length()-4, bean.getVinNo().length());
          carbuy_chejiahao.setText("车驾号  ：" + vinNo_after);                    //车架号

      }catch (Throwable t){t.printStackTrace();}
       car_buy_status.setText("消费类型："+bean.getProductTypeCn());         //消费类型
       carbuy_money.setText(  "参考价格："+(bean.getTotalAmount()/10000)+"万");
       carbuy_Surplus.setText("剩余份数："+bean.getSurplusNumber());
//       if (bean.getCanSubscribeCount()<=0){

//         mAmountView.setGoods_storage(0);
//       }else {
//           mAmountView.setGoods_storage(10);
//       }
       carbuy_yanse.setText("颜   色 ："+bean.getColor());
       if (images.size()==0){
           images.add(Config.IMAGE_HOST+bean.getCarImgPath());
           images.add(Config.IMAGE_HOST+bean.getCarImgPath());
           images.add(Config.IMAGE_HOST+bean.getCarImgPath());
           carbuy_banner.setImages(images);  //设置图片集合
           carbuy_banner.start();            //banner设置方法全部调用完毕时最后调用
       }

       if (bean.getProductType().contains("2")){
                consume(bean);//消费型
       }else {
               invest(bean);  //投资型
       }

   }

     //查询汽车产品详情
    void connectServer(){
        String url = "api/vip/product/detail/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("productCode", productCode);
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Product.class))
                .compose(RxNetHelper.<Product>io_main(mLoadingDialog))            //可以去掉object
                .subscribe(new ProgressSubscriber<Product>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    //                .compose(RxNetHelper.<Product>io_main())
//                .subscribe(new ResultSubscriber<Product>() {
                    @Override public void onSuccess(Product bean_resu) {
                        mLoadingDialog.dismiss();
                        bean=bean_resu;
                        LogUtil.e("获取认购详情成功"+bean.getCarCode());
                        LogUtil.e("获取认购详情成功"+bean.getPlateNumber());
                        LogUtil.e("获取认购详情成功"+bean.getProductCode());
                        LogUtil.e("获取认购详情成功"+bean.getCarCode());
                        LogUtil.e("获取认购详情成功"+bean.getCarImgPath());
                        LogUtil.e("获取认购详情成功"+bean.getProductStatus());
                        LogUtil.e("获取认购详情成功"+bean.getColor());
                        LogUtil.e("获取认购详情成功"+bean.getProductTitle());
                        LogUtil.e("获取认购详情成功"+bean.getProductType());
                        LogUtil.e("获取认购详情成功"+bean.getProductTypeCn());
                        LogUtil.e("获取认购详情成功"+bean.getBalance());
                        LogUtil.e("获取认购详情解除时间"+bean.getCanCancleTime());
                        LogUtil.e("获取认购详情分红到账时间"+bean.getContractExpiresTime());
                        LogUtil.e("获取认购详情当前时间"+bean.getCurrentDate());
                        LogUtil.e("获取认购详情成功"+bean.getPeriod());
                        LogUtil.e("获取认购详情成功"+bean.getProductNumber());

                        LogUtil.e("获取认购详情成功"+bean.getSubAmount());
                        LogUtil.e("获取认购详情成功"+bean.getSubRebate());
                        LogUtil.e("获取认购详情成功"+bean.getSurplusNumber());
                        LogUtil.e("获取认购详情成功"+bean.getWithdrawPeriods());
                        LogUtil.e("获取认购详情成功"+bean.getTotalAmount());

                        update(bean);//进行更新数据
                    }
                    @Override public void onFail(Message<Product> bean) {
                        LogUtil.e("获取认购详情成功"+bean.msg);
                    }
                });
    }


    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

}
