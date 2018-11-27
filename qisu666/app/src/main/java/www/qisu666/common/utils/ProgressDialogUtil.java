package www.qisu666.common.utils;

import android.app.ProgressDialog;
import android.content.Context;

import www.qisu666.com.application.IDianNiuApp;


public class ProgressDialogUtil {


	private static Context context = IDianNiuApp.getInstance();


//	public static ProgressDialog ShowDefaultProgressDialog(ProgressDialog progressDialog,boolean cancelable){
//		if(progressDialog == null || !progressDialog.isShowing()){
//			progressDialog = ProgressDialog.show(context,"", context.getResources().getString(R.string.please_wait)
//					, true, cancelable);
//		    progressDialog.show();
//		}
//		return progressDialog;
//	}


	public static ProgressDialog ShowMsg(ProgressDialog progressDialog, String showMeg, boolean cancelable){
		if(progressDialog == null || !progressDialog.isShowing()){
			progressDialog = ProgressDialog.show(context,"",showMeg, true, cancelable);
			progressDialog.show();
		}
		return progressDialog;
	}
	

	public static void cancelProgressDialog(ProgressDialog progressDialog){
		progressDialog.cancel();
	}
	
}
