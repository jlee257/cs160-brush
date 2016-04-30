package javis.wearsyncservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.util.Calendar;

/**
 * Created by Me on 4/19/16.
 */
public class DashboardDay extends SlidingMenuActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_day);*/
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
//
//        SharedPreferences data = getPreferences(0); //sSharedPreferences(SCORES)     for storing data
        SharedPreferences data = getSharedPreferences("SCORES", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("SCORES", MODE_PRIVATE).edit();

        String day_score = data.getString("score_day", "0_0_0_0_0__0_0_0_0_0");

//        SharedPreferences.Editor editor = data.edit(); //to edit data
//        //if day of the week is Sunday, reset the data for the week
        Calendar c = Calendar.getInstance();
        int val = c.get(Calendar.DAY_OF_WEEK);
        
        String day = days[val-1];

        if (day.equals("Sunday")) {
            resetWeekData(data);
        }

        float val1 = getDayScore(day_score);
        Log.d("Dashboard", ""+val1);
        editor.putFloat(day, val1);

        if (data.getString("day", null) == null) {
            editor.putString("day", day);
        }
        Log.d("hey", day);
        if (!day.equals(data.getString("day", null))) {
            //if it's not the same day
            editor.putString("day", day);
            //reset the scores
            editor.putString("score_day", "0_0_0_0_0__0_0_0_0_0");
        }
//        //dummy data, get real data
//        editor.putInt("Sun", 90);
//        editor.putInt("Mon", 86);
//        editor.putInt("Tue", 85);
//        editor.putInt("Wed", 80);
//        editor.putInt("Thu", 75);
//        editor.putInt("Fri", 80);
//        editor.putInt("Sat", 85);
//
        editor.apply();

        if (savedInstanceState!=null)
        {
            savedInstanceState.putString("TITLE", "DASHBOARD");
            savedInstanceState.putInt("LAYOUT", R.layout.dashboard_day);
            savedInstanceState.putInt("LAYOUT_ID", R.id.dashboard_rel);
            super.onCreate(savedInstanceState);
        }

        else
        {
            Bundle b = new Bundle();
            b.putString("TITLE", "DASHBOARD");
            b.putInt("LAYOUT", R.layout.dashboard_day);
            b.putInt("LAYOUT_ID", R.id.dashboard_rel); //id of top level Relative/Linear etc Layout
            super.onCreate(b);
        }


        this.setTitle("Dashboard");
//        SharedPreferences accessor = getSharedPreferences("WATCHSCORES", MODE_PRIVATE);
//        SharedPreferences.Editor editor = getSharedPreferences("WATCHSCORES", MODE_PRIVATE).edit();
//
//        //Set the number of notifications
//        String score_data =  accessor.getString("SCOREDATA", null);
////        if (notification_data != null) {
////            new_data = notification_data + "__" + new_data;
////        }
////        Log.d("SharedPref", "Data adding to SP = " + new_data);
////        editor.putString("notification_data", new_data);
//        editor.commit();

//        String[] tempScores = {"1_86_60_40_70", "2_4_0_50_43"};
        Log.d("AEFfeaer", ""+ day_score);
        String[] tempScores = day_score.split("__"); // 2 underscores '__'

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DashboardAdapter(tempScores);
        mRecyclerView.setAdapter(mAdapter);


        Bitmap bmp = getImageBitmap(this, "profile","BMP");;
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

    }

    public void resetWeekData(SharedPreferences data) {
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("Sunday", 0);
        editor.putInt("Monday", 0);
        editor.putInt("Tuesday", 0);
        editor.putInt("Wednesday", 0);
        editor.putInt("Thursday", 0);
        editor.putInt("Friday", 0);
        editor.putInt("Saturday", 0);
        editor.apply();
    }

    public float getDayScore(String score) {
        String[] scores = score.split("__");
       float dayScore = 0;
        for (String indScore: scores) {
            if (indScore != "") {
                dayScore += Float.parseFloat(indScore.split("_")[2])/(float)scores.length;
                Log.d("Dash", "" + dayScore);
            }
        }
        return dayScore;
    }

    public void toDashboardDay(View v) {
        Intent next = new Intent(this, DashboardDay.class);
        startActivity(next);
    }

    public void toDashboardWeek(View v) {
        Intent next = new Intent(this, DashboardWeek.class);
        startActivity(next);
    }
//
    public void sendWatchMessage(View v)
    /**
     * Called onClick of the Send Message button
     */
    {
        /*Intent next = new Intent(this, NotificationView.class);
        startActivity(next); NOT THE CORRECT FLOW INTERACTION*/

        /*final Dialog d = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.custom_dialog);
        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        d.getWindow().setLayout(400,400);
        d.show();*/

        Intent i = new Intent(this, CustomDialog.class);
        startActivity(i);

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
