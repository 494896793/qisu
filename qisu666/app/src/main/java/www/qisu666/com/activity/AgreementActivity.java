package www.qisu666.com.activity;

import android.os.Bundle;

import www.qisu666.common.activity.BaseWebViewActivity;

/**
 * 用户协议
 */
public class AgreementActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView("用户协议", "http://www.qisu666.com/agreement-qsgx.html");
    }

}
