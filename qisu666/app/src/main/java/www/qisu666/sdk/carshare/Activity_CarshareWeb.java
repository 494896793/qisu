package www.qisu666.sdk.carshare;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
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
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.sdk.partner.widget.WebViews;

/**
 * 717219917@qq.com 2018/5/15 9:42.
 */
public class Activity_CarshareWeb   extends BaseActivity  implements View.OnClickListener {
      WebViews webView;
     TextView carshareweb_title;
     TextView carshareweb_no;   //不同意
    TextView carshareweb_yes;  //同意
    ImageView carshareweb_top; //返回顶部
    LinearLayout carshareweb_bottom;   //底部布局      //左边退出
    public LoadingDialog loadingDialog;

    TextView tv_title;
    ImageView img_title_left;
    RelativeLayout layout_title;

    String title ="奇速共享汽车使用协议";
    String webUrl="file:///android_asset/user/user.html";     //加载的本地url

    String  fromeSetting ="false";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_carshareweb);
//        try{title=getIntent().getStringExtra("title");}catch (Throwable t){t.printStackTrace();title ="奇速共享汽车使用协议";}
//        try{webUrl=getIntent().getStringExtra("url"); }catch (Throwable t){t.printStackTrace();webUrl="file:///android_asset/user/user.html";}
        try{fromeSetting=getIntent().getStringExtra("fromeSetting"); }catch (Throwable t){t.printStackTrace();fromeSetting="false";}
        initView();
    }


    @Override protected void onResume() {
        super.onResume();
        try {
            if (fromeSetting.equals("true")) {
                carshareweb_bottom.setVisibility(View.GONE);
                carshareweb_yes.setVisibility(View.GONE);
                carshareweb_no.setVisibility(View.GONE);
            }else {  layout_title.setVisibility(View.GONE);   }
        }catch (Throwable t){t.printStackTrace();}
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initView(){
        webView=(WebViews)findViewById(R.id.carshareweb_webview);
        carshareweb_title=(TextView)findViewById(R.id.carshareweb_title);
        carshareweb_no=(TextView)findViewById(R.id.carshareweb_no);
        carshareweb_yes=(TextView)findViewById(R.id.carshareweb_yes);
        carshareweb_top=(ImageView)findViewById(R.id.carshareweb_top);
        carshareweb_bottom=(LinearLayout) findViewById(R.id.carshareweb_bottom);

        tv_title=(TextView)findViewById(R.id.tv_title);
       img_title_left=(ImageView)findViewById(R.id.img_title_left);   //
       layout_title=(RelativeLayout) findViewById(R.id.layout_title);  //标题栏布局


        carshareweb_no.setOnClickListener(this);
        carshareweb_yes.setOnClickListener(this);
        carshareweb_top.setOnClickListener(this);
        carshareweb_title.setText(title);


        tv_title.setText("奇速共享汽车使用协议");
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override  public void onClick(View view) {
                finish();
            }
        });



        if(NetworkUtils.isConnected(this)) {
            webView.loadUrl(webUrl);
            loadingDialog = DialogHelper.loadingDialog( this, "正在加载中...");
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
                    if (newProgress >=100) {
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
                    if (webcontent - webcontent*3/2 <= 20) {  carshareweb_top.setVisibility(View.VISIBLE);  } else {  }//已经处于底端
                    if (webnow == webView.getHeight()) {       carshareweb_top.setVisibility(View.GONE);  } else {  }//已经处于顶端
                }  });
        }

        try {
            if (fromeSetting.equals("true")) {
                carshareweb_bottom.setVisibility(View.GONE);
                carshareweb_yes.setVisibility(View.GONE);
                carshareweb_no.setVisibility(View.GONE);
            }else {
                layout_title.setVisibility(View.GONE);//隐藏标题栏
            }
        }catch (Throwable t){t.printStackTrace();
            layout_title.setVisibility(View.GONE);//隐藏标题栏
        }


    }



    /**取消合同*/
    void cancel_hetong(){
//        String url = "api/vip/subscribe/suspend";
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("productCode", productCode);
//        map.put("userCode", UserParams.INSTANCE.getUser_id());
//
//        MyNetwork.getMyApi()
//                .carRequest(url, MyMessageUtils.addBody(map))
//                .map(new FlatFunction<>(Object.class))
//                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))            //可以去掉object
//                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
//                    @Override public void onSuccess(Object bean_resu) {
//                        mLoadingDialog.dismiss();
//                        LogUtil.e("获取认购详情成功"+bean_resu);
//                    }
//                    @Override public void onFail(Message<Object> bean) {
//                        LogUtil.e("获取认购详情成功"+bean.msg);
//                    }
//                });
    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {//屏蔽back按键
            try {
                if (fromeSetting.equals("true")) { return super.onKeyDown(keyCode, event);  } else { return true; }
            }catch (Throwable t){t.printStackTrace();  return  true;}
//            if (webView.canGoBack()) { webView.goBack(); return true;  }else{ finish(); return true; }
        }  return super.onKeyDown(keyCode,event);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog!=null){  loadingDialog.dismiss(); loadingDialog = null;  }
    }

    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.carshareweb_no:   EventBus.getDefault().post("返回主界面"); finish();   break; //不同意
            case R.id.carshareweb_yes:
                try {
                    SPUtil.put(this, title , "true");
                }catch (Throwable t){t.printStackTrace();}
                finish();
                break; //同意
            case R.id.carshareweb_top: webView.scrollTo(0,0);    break;   //

        }
    }
}


