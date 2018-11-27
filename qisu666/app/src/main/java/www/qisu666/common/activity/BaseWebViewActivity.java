package www.qisu666.common.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.amap.api.maps.MapView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.util.UserParams;
import www.qisu666.common.utils.JsonUtils;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.com.R;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.sdk.amap.stationMap.JsonUtil;

/**
 * Created by admin on 2016/9/2.
 */
public class BaseWebViewActivity extends BaseActivity{
    public WebView webView;
    public LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_operation_flow);

        View img_title_left = findViewById(R.id.img_title_left);
        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.webView);
    }

    protected void initWebView(String title, String webUrl){

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        if(NetworkUtils.isConnected(this)) {
            webView.loadUrl(webUrl);
            loadingDialog = DialogHelper.loadingDialog(BaseWebViewActivity.this, "正在加载中...");
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    return true;
                }
            });

            //启用支持javascript
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        if(loadingDialog!=null)
                            loadingDialog.dismiss();

                    } else {
                        // 加载中

                    }

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }else{
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
