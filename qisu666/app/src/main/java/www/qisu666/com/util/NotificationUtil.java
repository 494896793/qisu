package www.qisu666.com.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import www.qisu666.com.R;


public class NotificationUtil {

    @SuppressWarnings("deprecation")
    public static void sendNotification(Context context, Intent intent,
                                        String edtTitle, String edtContent, int id) {
        Log.d("YYQ", "NotificationUtil sendNotification");
        try {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification();
            notification.icon = R.mipmap.ic_launcher;
            notification.tickerText = edtTitle;
            notification.when = System.currentTimeMillis();


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            int requestCode = 0;
            if (id >= 10000)
                requestCode = id;
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentText(edtContent);
            builder.setContentTitle(edtTitle);

            builder.setContentIntent(pendingIntent);

            notification = builder.getNotification();

            notification.defaults |= Notification.DEFAULT_VIBRATE;
            long[] vibrate = {0, 100, 200, 300};
            notification.vibrate = vibrate;

            notification.defaults |= Notification.DEFAULT_SOUND;
            Uri uri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.sound = uri;

            notification.defaults |= Notification.DEFAULT_LIGHTS;

            notification.ledARGB = 0xff00ff00;
            notification.ledOnMS = 300;
            notification.ledOffMS = 1000;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            manager.notify(id, notification);
            Log.d("YYQ", "NotificationUtil id = " + id);
        } catch (Exception e) {
            Log.e("YYQ", "Exception: " + e.getClass().getName());
        }
    }
}
