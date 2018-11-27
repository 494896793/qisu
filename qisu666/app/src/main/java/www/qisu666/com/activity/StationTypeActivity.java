package www.qisu666.com.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.activity.BaseWebViewActivity;
import www.qisu666.common.utils.NetworkUtils;
import www.qisu666.com.R;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;

//电站分类
public class StationTypeActivity extends BaseWebViewActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView("电站分类", "http://wx.idianniu.com/idn/chat/round/station/type.html");
    }

}
