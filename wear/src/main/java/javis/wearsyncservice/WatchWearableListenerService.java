package javis.wearsyncservice;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Calendar;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the phone
 */
public class WatchWearableListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(PhoneWatchClass.PHONE_TO_WATCH_MESSAGE_PATH)) {
            String receivedText = new String(messageEvent.getData());
            Context ct = getApplication();
            if (receivedText.startsWith("MSG;"))
            {
                showNotification(ct, receivedText.substring(receivedText.indexOf(';')+1)); // Show encouragement message
            }
            else
            {
                //set the alarm
                set_reminder();
            }
            broadcastIntent(receivedText);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    // broadcast a custom intent.
    public void broadcastIntent(String text) {
        Intent intent = new Intent();
        intent.setAction(Constant.MY_INTENT_FILTER);
        intent.putExtra(Constant.PHONE_TO_WATCH_TEXT, text);
        sendBroadcast(intent);
    }

    public Notification buildNotification(Context mContext, String real_msg) {


        Bitmap bitmap = Bitmap.createBitmap(320,320, Bitmap.Config.ARGB_8888);
        int dull_purple = Color.parseColor("#895A99");
        bitmap.eraseColor(dull_purple);

        Notification.WearableExtender extender = new Notification.WearableExtender();
        extender.setBackground(bitmap);

        Intent playbackIntent = new Intent(mContext, HelloWorldActivity.class);

        PendingIntent playbackPendingIntent = PendingIntent.getActivity(
                mContext, 0, playbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(mContext)
                .setContentTitle("Brush!")
                .setContentText(real_msg)
                .setSmallIcon(R.drawable.notif_icon_fox)
                .setDefaults(Notification.DEFAULT_ALL)
                .extend(extender);
                //.setContentIntent(playbackPendingIntent);

        Notification notif = builder.build();
        notif.defaults = Notification.DEFAULT_ALL;

        return notif;
    }

    @TargetApi(23)
    public void showNotification(Context mContext, String real_msg) {
        Notification notification = buildNotification(mContext, real_msg);

        NotificationManager manager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.visibility=Notification.VISIBILITY_PUBLIC;
        Log.d("BAZOOKA", notification.toString());
        manager.notify(1, notification);
    }

    public void set_reminder()
    {
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //Intent intent = new Intent(getApplicationContext(), HelloWorldActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 3333,
        //        intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intent = new Intent(this, MsgReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 15);
        Log.d("BAZOOKA","Setting the reminder");
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }
}