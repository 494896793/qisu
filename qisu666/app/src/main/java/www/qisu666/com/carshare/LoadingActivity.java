package www.qisu666.com.carshare;

import android.os.Bundle;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Created by admin on 2018/1/16.
 */

public class LoadingActivity extends BaseActivity{

    protected LoadingDialog mLoadingDialog;

    protected void initLoadingDialog(){
        mLoadingDialog = new LoadingDialog(this);
    }

    protected void initLoadingDialog(String type){
        mLoadingDialog = new LoadingDialog(this, type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }
}
