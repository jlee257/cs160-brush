package javis.wearsyncservice;

/**
 * Created by AshleyTheMagnificant on 4/27/16.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;


/* This class pretty much creates a notification once it is started.
* Can replace this with Notification builder stuff maybe? */
public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        sendNotification("Wake Up! Wake Up!");

    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        //TODO send watch notification

        Intent msgIntent = new Intent(this, SendWatchMessageIntentService.class);
        msgIntent.putExtra(SendWatchMessageIntentService.INPUT_EXTRA, "MSG;" + "Its time to brush");
        startService(msgIntent);


//        alarmNotificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, NotificationsSet.class), 0);
//
//        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
//                this).setContentTitle("Alarm").setSmallIcon(R.drawable.bottomroundbutton)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                .setContentText(msg);
//
//
//        alamNotificationBuilder.setContentIntent(contentIntent);
//        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
