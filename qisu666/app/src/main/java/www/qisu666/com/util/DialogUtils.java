package www.qisu666.com.util;

import android.content.Context;

import www.qisu666.com.widget.AlertDialog;

/**
 * Created by zsd on 2016/6/2.zsd
 */
public class DialogUtils {

    //含确认取消的对话框
    public static void confirmDialog(Context context, String message, AlertDialog.OnDialogButtonClickListener listener){
        AlertDialog dialog = new AlertDialog(context, message, true);
        dialog.show();
        dialog.setSampleDialogListener(listener);
    }

    //含确认取消的对话框
    public static void confirmDialog2No(Context context, String message, AlertDialog.OnDialogButtonClickListener listener){
        AlertDialog dialog = new AlertDialog(context, message, false);
        dialog.show();
        dialog.setSampleDialogListener(listener);
    }


}
