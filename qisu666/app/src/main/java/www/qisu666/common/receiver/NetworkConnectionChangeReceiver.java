package www.qisu666.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;

/**
 * 网络连接监听Receiver
 *
 */
public class NetworkConnectionChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (!gprs.isConnected() && !wifi.isConnected()) {
			ToastUtil.showToast(R.string.toast_network_interrupt);
			
		}else{
			//Toast.makeText(context, "����������!", Toast.LENGTH_LONG).show();
		}

	}

}
