package www.qisu666.com.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.widget.AlertPhotoDialog;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Created by zsd on 2016/6/2.zsd
 */
public class PhotoDialogHelper {

    public static boolean isShowLoginDialog = false;

    //demo示例

//       PhotoDialogHelper.alertPhotoDialog(getActivity(),"确认","取消", "开车前","取车后",R.mipmap.bg_banner );
//                PhotoDialogHelper.alertTwoConfirmDialog(getActivity(), "确认1", "确认2", "环绕车身", R.mipmap.bg_banner, new AlertPhotoDialog.OnDialogButtonClickListener() {
//        @Override
//        public void onConfirm() {
//            ToastUtil.showToast("确认1");
//        }
//
//        @Override
//        public void onCancel() {
//            ToastUtil.showToast("确认2");
//        }
//    });

    //两个确认对话框
    public static AlertPhotoDialog alertTwoConfirmDialog(Context context, String confirm, String confirm2, String title, @DrawableRes int resId, AlertPhotoDialog.OnDialogButtonClickListener listener){
        AlertPhotoDialog dialog = new AlertPhotoDialog(context, confirm, confirm2, title, resId);
        dialog.show();
        dialog.setSampleDialogListener(listener);
        return dialog;
    }

    //含确认取消的对话框
    public static AlertPhotoDialog alertPhotoDialog(Context context, String confirm, String cancel, String title, String message,@DrawableRes int resId, AlertPhotoDialog.OnDialogButtonClickListener listener){
        AlertPhotoDialog dialog = new AlertPhotoDialog(context, confirm, cancel, title, message, resId);
        dialog.show();
        dialog.setSampleDialogListener(listener);
        return dialog;
    }

}
