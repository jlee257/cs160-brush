package javis.wearsyncservice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by AshleyTheMagnificant on 4/28/16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static String[] mDataset;
    private static Activity parentClass;
    private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<>();
    static AlarmManager alarmManager;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTimeView;
        public TextView mLabelView;
        public Switch mSwitch;
        public NotificationView parentActivity;


        public ViewHolder(final View v){
            super(v);
            mTimeView = (TextView) v.findViewById(R.id.alarm_time);
            mLabelView = (TextView) v.findViewById(R.id.alarm_label);
            mSwitch = (Switch) v.findViewById(R.id.alarm_switch);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    int i = getAdapterPosition();
                    Log.d("Adapter", "pos = " + i);
                    if (isChecked) {
                        Log.d("Adapter Notif", "This alarm is on!");
                        String[] vals = mDataset[i].split("_");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vals[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(vals[1]));

                        Intent myIntent = new Intent(mSwitch.getContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mSwitch.getContext(), 0, myIntent, 0);

                        pendingIntents.put(i, pendingIntent);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                1000 * 60 * 1440, pendingIntent);

                    } else {
                        Log.d("Adapter Notif", "Canceling Alarm");
                        alarmManager.cancel(pendingIntents.get(i));
                    }
                }
            });
        }
    }

    public NotificationAdapter(String[] myDataset, Activity parent) {
        mDataset = myDataset;
        parentClass = parent;
        alarmManager = (AlarmManager) parent.getSystemService(parent.ALARM_SERVICE);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTimeView = (TextView) v.findViewById(R.id.alarm_time);
        vh.mLabelView = (TextView) v.findViewById(R.id.alarm_label);
        vh.mSwitch = (Switch) v.findViewById(R.id.alarm_switch);
        return vh;
    }

    //     Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String[] values = mDataset[position].split("_");

        // time
        int hour = Integer.parseInt(values[0]);
        int min = Integer.parseInt(values[1]);

        Log.d("Adapter ", "time = " + hour + "" + min);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        DateFormat df = new SimpleDateFormat("h:mm a");
        String time = df.format(calendar.getTime());

        // label
        String label = values[3];

        Log.d("Adapter", "Time = " + time);
        holder.mTimeView.setText(time);
        holder.mLabelView.setText(label);
        if (holder.mSwitch.isChecked()) {
            Log.d("Notif Alarm", "Alarm is on");
            // Do alarm stuff
            if (!pendingIntents.containsKey(position)) { // so as to not make duplicate intents
                Log.d("Adapter no", "This posistion");
                Intent myIntent = new Intent(holder.mSwitch.getContext(), AlarmReceiver.class); // Alarm managers notifies AlarmReciever
                PendingIntent pendingIntent = PendingIntent.getBroadcast(holder.mSwitch.getContext(), position, myIntent, 0);
                pendingIntents.put(position, pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 1440, pendingIntent); //every 1440 - 24 hrs

//                Calendar calendar1 = Calendar.getInstance();
//                DateFormat df1 = new SimpleDateFormat("h:mm a");
//                String time1 = df1.format(calendar1.getTime());
//                Log.d("Alarm service", "Time on the watch = " + time1);
            }
        } else {
            Log.d("MyActivity", "Alarm Off");// Alarm not previously set so dont need to delete it
//            alarmManager.cancel(pendingIntent);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}


