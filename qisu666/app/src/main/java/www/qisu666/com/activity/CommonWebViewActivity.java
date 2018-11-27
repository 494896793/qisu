package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import www.qisu666.com.R;
import www.qisu666.com.adapter.ShareAdapter;
import www.qisu666.com.application.IDianNiuApp;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ResultSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.model.CarConfirmBean;
import www.qisu666.com.model.MyT;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.activity.BaseWebViewActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;

//评论webview页面  轮播图重用了
public class CommonWebViewActivity extends BaseWebViewActivity {

    private PopupWindow sharePopupWindow;
    private LinearLayout webView_layout;
    private String shareUrl;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharePopupWindow();
        webView_layout=findViewById(R.id.webView_layout);
        String act_title = getIntent().getStringExtra("act_title");
        String act_url = getIntent().getStringExtra("act_url");//   act_url="file:///android_asset/web.html";  //测试url
////        String act_url  ="file:///android_asset/recharge/recharge.html";  //测试url
//        String act_url  ="file:///android_asset/zengsong/activity.html";  //测试url
        initWebView(act_title, act_url);
        //支持JS
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);

        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setLoadWithOverviewMode(true);
       try{
           webView.addJavascriptInterface(CommonWebViewActivity.this,"android");
       }catch (Throwable t){
           t.printStackTrace();
       }
        //支持屏幕缩放



    }


    @JavascriptInterface
    public void hello(final String string){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if (!UserParams.INSTANCE.checkLogin(CommonWebViewActivity.this)){
                    startActivity(new Intent(CommonWebViewActivity.this,LoginActivity.class));
                    return;
                }
                //检查登陆情况
                    if (string.equals("chongzhi")){
                        startActivity(new Intent(CommonWebViewActivity.this, RechargeActivity.class));//跳转到充值页面
                    }else if(string.equals("zengsong")){
                        if (!UserParams.INSTANCE.checkLogin(CommonWebViewActivity.this)){
                            ToastUtil.showToast(R.string.toast_prompt_login);
                            Intent i = new Intent(CommonWebViewActivity.this, LoginActivity.class);
                            startActivityForResult(i, ConstantCode.REQ_LOGIN);
                            return;
                        }         //检查登陆情况
                        requestUserConfirm();
//                  startActivity(new Intent(CommonWebViewActivity.this, RechargeActivity.class));                 //跳转到充值页面
                    } else if(string.equals("tuijian")) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("userId",UserParams.INSTANCE.getUser_id());
                        String desString=MyMessageUtils.writeMessage(map,"HL1HBF6lLND721");
                        String newUrl="http://www.qisu666.com/app-activity/qrcode.html?parameters="+desString;
                        webView.loadUrl(newUrl);
                    }else if(string.equals("fuzhi")){
                        ToastUtil.showToast("复制成功");
                    }else if(string.equals("fanhui")){
                        finish();
                    } else{
//                        try{
//                            if(JsonUtils.isBadJson(string)) {
                                Map<String, Object> map = JsonUtils.jsonToMap(string);
                                if (map != null) {
                                    shareUrl = map.get("shareUrl").toString();
                                    sharePopupWindow.showAtLocation(webView_layout, Gravity.BOTTOM, 0, 0);
                                } else {
                                    ToastUtil.showToast("敬请期待");
                                }
//                            }else if(string.contains("http")){
//                                Map<String, Object> map = JsonUtils.jsonToMap(string);
//                                shareUrl = map.get("shareUrl").toString();
//                                webView.loadUrl(shareUrl);
////                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                    }
            }
        });
    }

    /**
     * 分享PopupWindow
     */
    private void initSharePopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(CommonWebViewActivity.this).inflate(
                R.layout.pop_shares, null);
        ImageView closeImg=contentView.findViewById(R.id.closeImg);
        LinearLayout wxFriend=contentView.findViewById(R.id.wxFriend);
        LinearLayout friendCicle=contentView.findViewById(R.id.friendCicle);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharePopupWindow!=null){
                    sharePopupWindow.dismiss();
                }
            }
        });
        final UMImage image=new UMImage(CommonWebViewActivity.this,R.mipmap.share_icon);
        wxFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aa=shareUrl;
                new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText("跟我一起来奇速共享体验共享汽车吧~现在加入还有红包领取哦>>")
                        .withMedia(image)
                        .withTargetUrl(shareUrl)
                        .withTitle("绿色出行选奇速共享，注册即送1800元")
                        //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                        .share();
            }
        });
        friendCicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withText("跟我一起来奇速共享体验共享汽车吧~现在加入还有红包领取哦>>")
                        .withMedia(image)
                        .withTargetUrl(shareUrl)
                        .withTitle("绿色出行选奇速共享，注册即送1800元")
                        .share();
            }
        });

        sharePopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        sharePopupWindow.setTouchable(true);
        sharePopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        sharePopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(CommonWebViewActivity.this, R.drawable.white_background));
        sharePopupWindow.setAnimationStyle(R.style.Popup_Anim_Bottom);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showToast(platform + "分享成功");
            if(sharePopupWindow!=null){
                sharePopupWindow.dismiss();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showToast(platform + "分享失败");
            if(sharePopupWindow!=null){
                sharePopupWindow.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            ToastUtil.showToast(platform + " 分享取消了");
        }
    };


    //身份认证判断
    private void requestUserConfirm() {
        //type 0:分时,1:按日
        //网点预约租车
        String url = "api/auth/info/query";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", UserParams.INSTANCE.getUser_id());

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(map))
                .map(new FlatFunction<>(CarConfirmBean.class))
                .compose(RxNetHelper.<CarConfirmBean>io_main())
                .subscribe(new ResultSubscriber<CarConfirmBean>() {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(CarConfirmBean bean) {
                        LogUtil.e("当前身份认证 身份证" + bean.idcardIsAuth);
                        LogUtil.e("当前身份认证 驾驶证" + bean.licenseIsAuth);
                        if (bean.idcardIsAuth == 1) {        //身份证已认证
                            if (bean.licenseIsAuth == 1) {   //驾驶证已认证
                                ToastUtil.showToast("您已是实名认证用户，赠送金额已自动发放到您的账户。");
                                return;// ActivityUtil.startActivity(mContext, LiveResultActivity.class);//进行人脸识别
                            } else {
                                ActivityUtil.startActivity(mContext, CarShareConfirmDriverActivity.class);//驾驶证认证  暂时去掉 调试人脸
                                return;
                            }
                        } else {
                            ActivityUtil.startActivity(mContext, CarShareConfirmIdentityActivity.class);//进行身份证认证
                            finish();
                            return;
                        }

//                                            try {
//                                                int driverType = tabCarShare.getCurrentTab();//用车逻辑
//                                                requestBook(driverType);
//                                            }catch (Throwable t){t.printStackTrace();}
                    }

                    @Override public void onFail(Message<CarConfirmBean> bean) {
                        LogUtil.e("bean"+bean.msg);
                        ToastUtil.showToast(bean.msg);
                        LogUtil.e("进入登陆超时000");
                        if (bean.code==-1001){//重新登陆
                            LogUtil.e("进入登陆超时");
                            UserParams.INSTANCE.clear();
                            /*UserParams.INSTANCE.checkLogin(CommonWebViewActivity.this);*/
                            ToastUtil.showToast(R.string.toast_prompt_login);
                            Intent i = new Intent(CommonWebViewActivity.this, LoginActivity.class);
                            startActivityForResult(i, ConstantCode.REQ_LOGIN);
                        }
                        if (bean.code == -1145) { //身份证未认证
                            ActivityUtil.startActivity(mContext, CarShareConfirmIdentityActivity.class);
                        }
                    }

                });
    }



}
