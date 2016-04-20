package javis.wearsyncservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Me on 4/16/16.
 */
public class signUpBrush extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_brush);
    }

    public void toSignUpWithInfoName(View v) {
        SharedPreferences settings = getSharedPreferences(signUpBrush.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            //this.getSharedPreferences(signUpBrush.PREFS_NAME, 0).edit().clear().commit();
            //to delete the shared prefernce of the phone
            //Go directly to main activity.
            Intent next = new Intent(this, unupdatedDashboard.class);
            startActivity(next);
        }
        else {

            Intent next = new Intent(this, signUpWithInfoName.class);
            startActivity(next);
        }
    }

    public void toTrack(View v) {
        Intent next = new Intent(this, signUpTrack.class);
        startActivity(next);
    }

    public void toCare(View v) {
        Intent next = new Intent(this, signUpCare.class);
        startActivity(next);
    }

}
