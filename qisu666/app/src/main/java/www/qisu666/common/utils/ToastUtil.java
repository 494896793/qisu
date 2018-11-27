package www.qisu666.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import www.qisu666.com.application.IDianNiuApp;


public class ToastUtil {
	
	private static Context context = IDianNiuApp.getInstance();

	/**
	 * 显示消息
	 * 
	 * @param msg
	 *
	 */
	public static void showToast(String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		TextView textView=toast.getView().findViewById(android.R.id.message);
		textView.setTextSize(18);
		toast.show();
	}

	public static void longToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showToast(int resId) {
		Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
	}
	
}
