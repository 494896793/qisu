package www.qisu666.sdk.partner;

/**
 * 717219917@qq.com 2018/4/18 16:44.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.sdk.partner.widget.WebViews;

/**合同详情(解除等)*/
public class Activity_ContractDetail extends BaseActivity{
       WebViews webView;
    public LoadingDialog loadingDialog;
    @BindView(R.id.img_title_left)ImageView img_title_left;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.contractdetail_top)ImageView contractdetail_top;//返回顶部
    @BindView(R.id.carbuy_btn_login)Button carbuy_btn_login;



    String subCode ="";
    String subType ="";
    String userCode ="";
    String productCode ="";
    boolean cancel =false;    //是否可以取消
    String cancelTime ="";    //取消时间
    String contractStatus=""; //当前合同状态  1.不可解除 2 可解除 3 已解除  4 解除申请中
    String webUrl="file:///android_asset/argu/argu.html";     //加载的本地url

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_contractdetail);
        productCode=getIntent().getStringExtra("productCode");
        subCode=getIntent().getStringExtra("subCode");
        subType=getIntent().getStringExtra("subType");
        userCode=getIntent().getStringExtra("userCode");
        cancelTime=getIntent().getStringExtra("cancelTime");
        cancel=getIntent().getBooleanExtra("cancel",false);
        contractStatus  =getIntent().getStringExtra("contractStatus");
        LogUtil.e("获取到的 productCode ："+  productCode );
        LogUtil.e("获取到的 subType ："+  subType );
        LogUtil.e("获取到的 subCode ："+  subCode );
        LogUtil.e("获取到的 userCode ："+  userCode );
        LogUtil.e("获取到的 cancelTime ："+  cancelTime );
        LogUtil.e("获取到的 cancel ："+  cancel );
        LogUtil.e("获取到的 contractStatus ："+  contractStatus );
         switch(contractStatus){
             case "1" :  carbuy_btn_login.setClickable(false);carbuy_btn_login.setText(cancelTime+"前不可解除");                               break;
             case "2" :  carbuy_btn_login.setClickable(true); carbuy_btn_login.setText("解除合同"); carbuy_btn_login.setBackgroundColor(getResources().getColor(R.color.text_red));  break;
             case "3" :  carbuy_btn_login.setClickable(false);carbuy_btn_login.setText("合同已解除");                                          break;
             case "4" :  carbuy_btn_login.setClickable(false);carbuy_btn_login.setText("解除申请中");                                          break;
             default:    carbuy_btn_login.setClickable(false); carbuy_btn_login.setText("不可解除");                                           break;

         }

        initView();
    }


    @Override protected void onResume() {
        super.onResume();
    }

   @SuppressLint("SetJavaScriptEnabled")
   void initView(){
       tv_title.setText("合同详情");
       contractdetail_top.setVisibility(View.GONE);
       webView=(WebViews)findViewById(R.id.webView);
       img_title_left.setOnClickListener(new View.OnClickListener() { @Override  public void onClick(View v) {  finish();  }  });

       if(NetworkUtils.isConnected(this)) {
           webView.loadUrl(webUrl);
           loadingDialog = DialogHelper.loadingDialog(Activity_ContractDetail.this, "正在加载中...");
           webView.setWebViewClient(new WebViewClient(){//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
               @Override  public boolean shouldOverrideUrlLoading(WebView view, String url) {
                   view.loadUrl(url);
                   return true;  //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
               }
           });

           WebSettings settings = webView.getSettings();
           settings.setSupportZoom(true);
           settings.setUseWideViewPort(true);//设定支持viewport
           settings.setLoadWithOverviewMode(true);

           settings.setBuiltInZoomControls(true);
           settings.setDisplayZoomControls(false);
           webView.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式

           webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
           webView.getSettings().setLoadWithOverviewMode(true);


           webView.setWebChromeClient(new WebChromeClient() {
               @Override public void onProgressChanged(WebView view, int newProgress) {
                   if (newProgress == 100) {
                       if(loadingDialog!=null) loadingDialog.dismiss(); } else {  }// 加载中
               }  });
           //得到mWebview的内容高度，由于mWebview有缩放因此需要乘以他的缩放值mWebView.getScale()
           //mWebview.getContentHeight()
           //当前mWebView显示的高度
           //mWebView.getHeight()
           //mWebView竖直方向上滚动的高度，若为0说明滚动条在顶部了
           //mWebView.getScrollY()

           webView.setOnCustomScroolChangeListener(new WebViews.ScrollInterface() {
               @Override public void onSChanged(int l, int t, int oldl, int oldt) {
                   float webcontent = webView.getContentHeight() * webView.getScale(); //webview的高度
                   float webnow     = webView.getHeight() + webView.getScrollY();      //当前webview的高度
                   if (webcontent - webcontent*3/2 <= 20) { contractdetail_top.setVisibility(View.VISIBLE);  } else {  }//已经处于底端
                   if (webnow == webView.getHeight()) {    contractdetail_top.setVisibility(View.GONE); } else {  }//已经处于顶端

               }  });
       }
   }



   /**取消合同*/
   void cancel_hetong(){
       String url = "api/vip/subscribe/suspend";
       HashMap<String, Object> map = new HashMap<>();
       map.put("productCode", productCode);
       map.put("userCode", UserParams.INSTANCE.getUser_id());

       MyNetwork.getMyApi()
               .carRequest(url, MyMessageUtils.addBody(map))
               .map(new FlatFunction<>(Object.class))
               .compose(RxNetHelper.<Object>io_main(mLoadingDialog))            //可以去掉object
               .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                   @Override
                   public void onSuccessCode(Message object) {

                   }

                   @Override public void onSuccess(Object bean_resu) {
                       mLoadingDialog.dismiss();
                       LogUtil.e("获取认购详情成功"+bean_resu);
                   }
                   @Override public void onFail(Message<Object> bean) {
                       LogUtil.e("获取认购详情成功"+bean.msg);
                   }
               });



   }

   @OnClick({R.id.contractdetail_top,R.id.carbuy_btn_login})
    public void onViewClicked(View view){
       switch (view.getId()){
           case R.id.contractdetail_top:  webView.scrollTo(0,0);     break; //返回顶部
           case R.id.carbuy_btn_login:    //中止合同等
               String str ="合同解除后将无法继续享受相应收益,合同解除申请发起后无法撤回。";
               DialogHelper.confirmTitleDialog_hetong(Activity_ContractDetail.this, null, str, new AlertDialog.OnDialogButtonClickListener() {
                   @Override public void onConfirm() {  cancel_hetong(); }
                   @Override public void onCancel() {  }
               });break;


       }

    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) { webView.goBack(); return true;  }else{ finish(); return true; }
        }  return super.onKeyDown(keyCode,event);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog!=null){  loadingDialog.dismiss(); loadingDialog = null;  }
    }
}


