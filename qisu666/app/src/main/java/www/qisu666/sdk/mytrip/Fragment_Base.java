package www.qisu666.sdk.mytrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;

/**
 * 717219917@qq.com
 * 2016/12/13  23:29
 */
public class Fragment_Base extends Fragment {
    private boolean injected = false;
    String tag = "Fragment_Base";
    protected LoadingDialog mLoadingDialog;//加载中弹框

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
        mLoadingDialog = DialogHelper.loadingAletDialog(getActivity(), "正在加载中...");
    }

    @Override  public void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog!=null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}