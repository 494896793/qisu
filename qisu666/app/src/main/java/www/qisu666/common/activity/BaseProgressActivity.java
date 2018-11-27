package www.qisu666.common.activity;

import android.os.Bundle;

import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Created by admin on 2018/2/8.
 */

public class BaseProgressActivity extends BaseActivity{

    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = DialogHelper.loadingAletDialog(mContext, "正在加载中...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog!=null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
