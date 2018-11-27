package www.qisu666.com.activity;

import android.os.Bundle;

import www.qisu666.common.activity.BaseWebViewActivity;

//注册协议
public class RegisterAgreementActivity extends BaseWebViewActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView("注册协议", "http://www.qisu666.com/agreement-qsgx.html");
    }

}
