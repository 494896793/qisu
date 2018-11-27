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

//操作流程
public class OperationFlowActivity extends BaseWebViewActivity{

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView(getString(R.string.user_operation_flow), "http://www.qisu666.com/app-flow/guide-jsc.html");
    }

}
