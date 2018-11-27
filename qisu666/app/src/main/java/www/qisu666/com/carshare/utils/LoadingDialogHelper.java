package www.qisu666.com.carshare.utils;

import android.content.Context;

import org.xutils.common.util.LogUtil;

import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Created by admin on 2018/1/16.
 */

public class LoadingDialogHelper {

    public static LoadingDialog showDialog( final LoadingDialog loadingDialog){
        try {
            loadingDialog.show();
        }catch (Exception e){
            LogUtil.e("Unable to add window");
            return loadingDialog;
        }
        return loadingDialog;
    }

//    public static LoadingDialog showDialog(Context context, final LoadingDialog loadingDialog, String type){
//        loadingDialog = new LoadingDialog(context, type);
//        try {
//            loadingDialog.show();
//        }catch (Exception e){
//            LogUtils.e("Unable to add window");
//            return loadingDialog;
//        }
//        return loadingDialog;
//    }

    public static void cancelDialog(final LoadingDialog loadingDialog){
        if(loadingDialog == null) return;
        loadingDialog.cancel();
    }
}
