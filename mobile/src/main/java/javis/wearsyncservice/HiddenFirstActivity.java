package javis.wearsyncservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class HiddenFirstActivity extends Activity {
    /**
     * Not displayed. Just decides which screen to start from
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_first);

        Class activityClass;

        SharedPreferences settings = getSharedPreferences(signUpBrush.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);


        if(hasLoggedIn)
            activityClass = DashboardDay.class;
        else
            activityClass = signUpBrush.class;

        Intent newActivity = new Intent(this, activityClass);
        startActivity(newActivity);


    }
}
