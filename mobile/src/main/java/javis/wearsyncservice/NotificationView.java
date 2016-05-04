package javis.wearsyncservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.HashMap;

public class NotificationView extends SlidingMenuActivity {

    String ALARM_TAG = "ALARM";
    String NOTIFICATIONS = "notifications";
    AlarmManager alarmManager;
    private static NotificationView inst;
    private RecyclerView mRecyclerView;
    private android.support.v7.widget.RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private HashMap<Integer, PendingIntent> pendingAlarms = new HashMap<>();
    final String SETTINGS_FILE = "BRUSH_SETTINGS";


    public static NotificationView instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notification_view);

        if (savedInstanceState!=null)
        {
            savedInstanceState.putString("TITLE", "NOTIFICATIONS");
            savedInstanceState.putInt("LAYOUT", R.layout.content_notification_view);
            savedInstanceState.putInt("LAYOUT_ID", 345345);
            super.onCreate(savedInstanceState);
        }
        else
        {
            Bundle b = new Bundle();
            b.putString("TITLE", "NOTIFICATIONS");
            b.putInt("LAYOUT", R.layout.content_notification_view);
            b.putInt("LAYOUT_ID", 34534); //id of top level Relative/Linear etc Layout
            super.onCreate(b);
        }

        SharedPreferences getSettings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        String name = getSettings.getString("FIRST_NAME", "No Name");
        if (name.equals(""))
        {
            name = "Your child";
        }
        TextView mainNameView = (TextView) findViewById(R.id.Name);
        mainNameView.setText(name);

        Bitmap bmp = getImageBitmap(this, "profile","BMP");;
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mRecyclerView = (RecyclerView) findViewById(R.id.notification_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNotification();
            }
        });*/

        SharedPreferences accessor = getSharedPreferences(NOTIFICATIONS, MODE_PRIVATE);
        String notification_data =  accessor.getString("notification_data", null);
//        String notification_data = DataHolder.getInstance().getData();


        if (notification_data != null) {
            inflateViews(notification_data);
        }
        Log.d("NotifView", "data from SP = " + notification_data);
    }

    public void onClickPseudoFAB(View view) {
        Log.d("BAZOOKA", "clicked the pseudo fab");
        newNotification();
    }

    public void newNotification() {
        Intent newAlarm = new Intent(this, notificationsSet.class);
        startActivity(newAlarm);
    }

    public void inflateViews(String data) {
        String[] notifs = data.split("__"); // 2 underscores "_" separate notification information
        /*Log.d("BAZOOKA", "# of notifications = "+ notifs.length);
        if (notifs.length==0)
        {
            Log.d("BAZOOKA", "There are no notifications");
            TextView empty = (TextView)findViewById(R.id.empty_string);
            empty.setVisibility(View.VISIBLE);
        }
        else {*/
        TextView empty = (TextView)findViewById(R.id.empty_string);
        empty.setVisibility(View.INVISIBLE);
        mAdapter = new NotificationAdapter(notifs, this);
            mRecyclerView.setAdapter(mAdapter);
        //}
    }

    public Bitmap getImageBitmap(Context context,String name,String extension){
        name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        //return null;
        //If there is no selected image displays the fox icon
        Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.drawable.foxicon);
        return Icon;
    }


}