package javis.wearsyncservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the watch
 */
public class PhoneWearableListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(PhoneWatchClass.WATCH_TO_PHONE_MESSAGE_PATH)) {
            String receivedText = new String(messageEvent.getData());
            String data = "";
            Log.d("FRom Watch", "text = " + receivedText);
            if (receivedText.startsWith("UPDATED_GRAPH"))
            {
                Intent next = new Intent(this, updatedDashboard.class);
                next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(next);
            } else {
                Calendar calendar = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("a");
                String time_of_day = df.format(calendar.getTime());
                Log.d("Phone", "TOD" + time_of_day);

                data = time_of_day + "_" + receivedText;
                Log.d("PHONE Ddfa", data);
                saveToSharedPreferences(data);

                Intent startDashboardDay = new Intent(this, DashboardDay.class);
                startDashboardDay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startDashboardDay);
            }
            broadcastIntent(data);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    public void saveToSharedPreferences(String data) {
        SharedPreferences accessor = getSharedPreferences("SCORES", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("SCORES", MODE_PRIVATE).edit();
        int first_attempt = accessor.getInt("first_attempt", 0);
        if (first_attempt == 0) { // No one has brushed today
            editor.putString("score_day", data);
            editor.putInt("first_attempt", 1);
        } else {
            String previousData = accessor.getString("score_day", "");
            editor.putString("score_day", previousData + "__" + data); // compund the scores
        }

        editor.commit();
    }

    // broadcast a custom intent.
    public void broadcastIntent(String text) {
        Intent intent = new Intent();
        intent.setAction(Constant.MY_INTENT_FILTER);
        intent.putExtra(Constant.PHONE_TO_WATCH_TEXT, text);
        sendBroadcast(intent);
    }
}