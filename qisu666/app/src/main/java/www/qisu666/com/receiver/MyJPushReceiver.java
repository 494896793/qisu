package www.qisu666.com.receiver;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import www.qisu666.com.util.PushChatUtils;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.activity.NotificationDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import www.qisu666.common.utils.ToastUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {

	private NotificationManager manager;

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		String alert2 = bundle.getString(JPushInterface.EXTRA_ALERT);
//		PushChatUtils.pushChat(context,"奇速共享",alert2);
		LogUtils.d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtils.d("[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			LogUtils.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	bundle.putString(JPushInterface.EXTRA_MESSAGE, "change "+bundle.getString(JPushInterface.EXTRA_MESSAGE));

//			if(manager == null){
//				manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//			}

//			Map map = JsonUtils.jsonToMap(JPushInterface.EXTRA_MESSAGE);
//
//			Intent mIntent = new Intent(context, MainActivity.class);
//			PendingIntent pendingIntent = PendingIntent.getActivity(context,0,mIntent,0);
//
//			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//			builder.setTicker(JPushInterface.EXTRA_EXTRA)
//				.setContentTitle(map.get("title").toString())
//				.setContentText(map.get("content").toString())
//				.setSmallIcon(R.mipmap.ic_launcher)
//				.setContentIntent(pendingIntent);
//
//			manager.notify(Integer.valueOf(map.get("id").toString()), builder.build());

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			LogUtils.d("[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			LogUtils.d("[MyReceiver] 接收到推送下来的通知的内容: " + alert);
            bundle.putString(JPushInterface.EXTRA_ALERT, "change "+alert);
			LogUtils.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			wakeAndUnlock(context, true);
//			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//			manager.;

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			LogUtils.d("[MyReceiver] 用户点击打开了通知");
            
        	//打开自定义的Activity
//        	Intent i = new Intent(context, NotificationDetailActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			LogUtils.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			LogUtils.w("[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
			LogUtils.d("[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					LogUtils.i("This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					LogUtils.e("Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//锁屏、唤醒相关
	private KeyguardManager km;
//	private KeyguardManager.KeyguardLock kl;
	private PowerManager pm;
	private PowerManager.WakeLock wl;



	private void wakeAndUnlock(final Context context, boolean b)
	{
		if(b)
		{
			//获取电源管理器对象
			pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);

			//获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
			wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

			//点亮屏幕
			wl.acquire();

			//得到键盘锁管理器对象
//			km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//			kl = km.newKeyguardLock("unLock");
//
//			//解锁
//			kl.disableKeyguard();

			Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					wakeAndUnlock(context, false);
				}
			};
			handler.sendEmptyMessageDelayed(0, 5000);

		}
		else
		{
			//锁屏
//			kl.reenableKeyguard();

			//释放wakeLock，关灯
			wl.release();
		}

	}


}
