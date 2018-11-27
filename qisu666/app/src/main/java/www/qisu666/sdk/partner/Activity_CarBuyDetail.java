package www.qisu666.sdk.partner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iflytek.thridparty.G;
import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Single;
import rx.functions.Action1;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.partner.bean.Bean_Single;
import www.qisu666.sdk.partner.widget.AmountView;
import www.qisu666.sdk.partner.widget.UnderLineLinearLayout;
import www.qisu666.sdk.utils.Update;

/**  汽车认购 详情页
 * 717219917@qq.com ${DATA} 16:36.
 */
public class Activity_CarBuyDetail extends BaseActivity {

    @BindView(R.id.carbuy_banner)            Banner carbuy_banner ;
    @BindView(R.id.tv_title)                TextView tv_title;
    @BindView(R.id.img_title_left)         ImageView img_title_left;

    @BindView(R.id.carbuy_detail_xiangqing) TextView carbuy_detail_xiangqing;//合同详情
    @BindView(R.id.carbuy_model)                TextView carbuy_model;       //车型号  标题
    @BindView(R.id.carbuy_auto)                TextView carbuy_auto;         //是否自动档
    @BindView(R.id.carbuy_money)                TextView carbuy_money;        //参考价15万
    @BindView(R.id.carbuy_dongliyuan)           TextView carbuy_dongliyuan;   //动力源 纯电动
    @BindView(R.id.carbuy_yanse)                TextView carbuy_yanse;        //颜色 白
    @BindView(R.id.carbuy_chengshi)             TextView carbuy_chengshi;     //所属城市  深圳
    @BindView(R.id.carbuy_chepaihao)            TextView carbuy_chepaihao;    //车牌号
    @BindView(R.id.carbuy_xingshizhenghao)     TextView carbuy_xingshizhenghao;//行驶证号

    @BindView(R.id.carbuy_chejiahao)                TextView carbuy_chejiahao;               //车架号
    @BindView(R.id.carbuy_detail_carimg)         ImageView carbuy_detail_carimg;             //车图像
    @BindView(R.id.carbuy_detail_carimg_txt)        TextView carbuy_detail_carimg_txt;       //车牌描述
    @BindView(R.id.carbuy_detail_statue)            TextView carbuy_detail_statue;           //投资状态  投资中
    @BindView(R.id.carbuy_detail_type)              TextView carbuy_detail_type;             //投资类型  投资型
    @BindView(R.id.carbuy_detail_buytime)                 TextView car_buy_buytime;                //认购时间

    @BindView(R.id.carbuy_detail_finishtime)              TextView car_buy_finishtime;             //到期时间
    @BindView(R.id.carbuy_detail_moneytime)               TextView car_buy_moneytime;              //分红时间
    @BindView(R.id.carbuy_detail_hetongstatus)            TextView carbuy_detail_hetongstatus;     //合同状态
    @BindView(R.id.carbuy_detail_totalmoney)            TextView carbuy_detail_totalmoney;         //累计收益
    @BindView(R.id.carbuy_detail_hetongtype) TextView carbuy_detail_hetongtype;                    //投资型合同类型 /消费型合同类型



    ArrayList<String> images= new ArrayList<>();
    String productCode ="";
    String subCode ="";
    String subType ="";
    String userCode ="";
    boolean cancel=false;
    Bean_Single bean;

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_carbuydetail);
        initView();
    }


   void initView(){
       tv_title.setText("认购详情");
       img_title_left.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View view) {
                    finish();
           }});
       carbuy_detail_xiangqing.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View view) {
//               Date dt=new Date();
//               boolean cancel = bean.getSubscribeInfo().getCanCancleTime().after(dt);
//               LogUtil.e("时间比较："+cancel);//true 不可解除
//               DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置显示格式
//               Intent intent =new Intent(Activity_CarBuyDetail.this,Activity_ContractDetail.class);
//               intent.putExtra("productCode",productCode);
//               intent.putExtra("cancel",cancel);
//               intent.putExtra("cancelTime",df.format(bean.getSubscribeInfo().getCanCancleTime()));//取消时间
//               intent.putExtra("contractStatus",bean.getSubscribeInfo().getContractStatus());//当前合同状态
//               startActivity(intent);
           }
       });
       productCode=getIntent().getStringExtra("productCode");
       subCode=getIntent().getStringExtra("subCode");
       subType=getIntent().getStringExtra("subType");
       userCode=getIntent().getStringExtra("userCode");

       initData();
   }



   void initData(){
//       images.add("http://mananger.qisu666.com/upload/station_img/201804022004580488_1.png");
       images.add(R.mipmap.cs_car+"");
       images.add(R.mipmap.cs_car+"");
       images.add(R.mipmap.cs_car+"");

       carbuy_banner.setImageLoader(new GlideImageLoader());//设置图片加载器
       carbuy_banner.setImages(images);                     //设置图片集合
       carbuy_banner.start();                               //banner设置方法全部调用完毕时最后调用
       connectServer();
   }

    public class GlideImageLoader extends ImageLoader {
        @Override  public void displayImage(Context context, Object path, ImageView imageView) { imageView.setScaleType(ImageView.ScaleType.FIT_XY);Glide.with(context).load(path).into(imageView);    } //Glide 加载图片简单用法
    }


    @SuppressLint({"SetTextI18n", "SimpleDateFormat" })
    void update(Bean_Single bean){
        Glide.with(this).load(Config.IMAGE_HOST+bean.getCarInfoMap().getCarImgPath()).into(carbuy_detail_carimg);
        images.clear();
        images.add(Config.IMAGE_HOST+bean.getCarInfoMap().getCarImgPath());
        images.add(Config.IMAGE_HOST+bean.getCarInfoMap().getCarImgPath());
        images.add(Config.IMAGE_HOST+bean.getCarInfoMap().getCarImgPath());
        carbuy_banner.setImageLoader(new GlideImageLoader());//设置图片加载器
        carbuy_banner.setImages(images);                     //设置图片集合
        carbuy_banner.start();                               //banner设置方法全部调用完毕时最后调用

          carbuy_model.setText(bean.getCarInfoMap().getProductTitle());                           //车型号  标题
//        @BindView(R.id.carbuy_auto)                TextView carbuy_auto;                        //是否自动档
         carbuy_money.setText("参考价："+(bean.getCarInfoMap().getTotalAmount()/10000)+"万");        //参考价15万
         carbuy_dongliyuan.setText("动力源:纯电动");                                                 //动力源 纯电动
         carbuy_yanse.setText("颜色："+bean.getCarInfoMap().getColor());                              //颜色 白
         carbuy_chengshi.setText("所属城市:"+bean.getCarInfoMap().getCityName());                   //所属城市  深圳
         carbuy_chepaihao.setText("车牌号:"+bean.getCarInfoMap().getPlateNumber());                //车牌号

//         String  wpmi=bean.getCarInfoMap().getCarWpmi().substring(0,3)+"***"+bean.getCarInfoMap().getCarWpmi().substring(6,bean.getCarInfoMap().getCarWpmi().length());
         carbuy_xingshizhenghao.setText("行驶证号:"+bean.getCarInfoMap().getCarWpmi());            //行驶证号
//         String vin=bean.getCarInfoMap().getVinNo().substring(0,3)+"***"+bean.getCarInfoMap().getVinNo().substring(6,bean.getCarInfoMap().getVinNo().length());
         carbuy_chejiahao.setText("车驾号:"+bean.getCarInfoMap().getVinNo());                                                    //车架号
         carbuy_detail_carimg_txt.setText(bean.getCarInfoMap().getProductTitle());                   //车牌描述
         carbuy_detail_statue.setText(bean.getSubscribeInfo().getSubStatus().equals("1")?"投资状态:投资中":"投资状态:投资结束");                     //投资状态  投资中
   try {
       if (bean.getSubscribeInfo().getSubStatus().equals("1")) {//投资中
           carbuy_detail_statue.setText("投资状态:投资中");                     //投资状态  投资中
       } else if (bean.getSubscribeInfo().getSubStatus().equals("2")) {//投资结束
           carbuy_detail_statue.setText("投资状态:投资结束");                     //投资状态  投资中
       } else {
           carbuy_detail_statue.setText("投资状态:状态异常");                     //投资状态  投资中
       }
   }catch (Throwable t){t.printStackTrace(); carbuy_detail_statue.setText("投资状态:状态异常");    }

         carbuy_detail_type.setText("投资类型:"+bean.getCarInfoMap().getProductTypeCn());             //投资类型  投资型
         car_buy_buytime.setText("认购时间:"+new SimpleDateFormat("yyyy-MM-dd").format(bean.getSubscribeInfo().getSubTime()));                //认购时间
         car_buy_finishtime.setText("到期时间:"+new SimpleDateFormat("yyyy-MM-dd").format(bean.getSubscribeInfo().getContractExpiresTime()));  //到期时间
          if(bean.getCarInfoMap().getProductTypeCn().contains("消费")){
              carbuy_detail_totalmoney.setVisibility(View.GONE);
              carbuy_detail_totalmoney.setText("累计收益:"+bean.getSubscribeInfo().getUseBonusAmount()+"元");
          }else {
              carbuy_detail_totalmoney.setText("累计收益:"+bean.getSubscribeInfo().getUseBonusAmount()+"元");
          }


         if (bean.getCarInfoMap().getProductType().equals("2")){  //消费型
             car_buy_moneytime.setVisibility(View.GONE);          //消费型 没有分红
         }else{
             car_buy_moneytime.setText("分红时间:"+new SimpleDateFormat("yyyy-MM-dd").format(bean.getSubscribeInfo().getFirstPhaseTime()));        //分红时间
         }
        carbuy_detail_hetongtype.setText(bean.getCarInfoMap().getProductTypeCn()+"合同");

//        1.不可解除 2 可解除 3 已解除  4 解除申请中
        switch(bean.getSubscribeInfo().getContractStatus()){
            case "1":   carbuy_detail_hetongstatus.setText("合同状态:不可解除（"+new SimpleDateFormat("yyyy-MM-dd").format(bean.getSubscribeInfo().getContractExpiresTime())+"后可解除)");            break;
            case "2":   carbuy_detail_hetongstatus.setText("合同状态:可解除");              break;
            case "3":   carbuy_detail_hetongstatus.setText("合同状态:已解除");              break;
            case "4":   carbuy_detail_hetongstatus.setText("合同状态:解除申请中");          break;
        }





    }

    //获取单个认证详情
    void connectServer(){
        String url = "api/vip/user/subscribe/sigle";
        HashMap<String, Object> map = new HashMap<>();
        map.put("productCode", productCode);
        map.put("subCode", subCode);
        try{double d=Double.parseDouble(subType);map.put("subType", (new Double(d)).intValue());}catch (Throwable t){t.printStackTrace();map.put("subType", subType);}
         map.put("userCode", UserParams.INSTANCE.getUser_id());
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(Bean_Single.class))
                .compose(RxNetHelper.<Bean_Single>io_main())
                .subscribe(new ResultSubscriber<Bean_Single>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(Bean_Single bean_tmp) {
                        bean=bean_tmp;
                        LogUtil.e("获取认购列表        已认购"+bean.toString());
                        LogUtil.e("获取认购列表   颜色 已认购"+bean.getCarInfoMap().getColor());
                        LogUtil.e("获取认购列表        已认购"+bean.getSubscribeInfo().getUseBonusAmount());
                        LogUtil.e("获取认购列表  已认购"+bean.getCarInfoMap().getCarImgPath());
                        update(bean);
                    }
                    @Override public void onFail(Message<Bean_Single> bean) {
                        LogUtil.e("获取认购列表失败code:"+bean.code+",msg:"+bean.msg);
                        if(bean.code==-1001){  EventBus.getDefault().post("登陆失效");return;}
                        if(bean.code==-1201){  EventBus.getDefault().post("登陆失效");return;}
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
