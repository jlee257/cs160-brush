package javis.wearsyncservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends Activity {

    private TextView mTextView;
    private float circular_score;
    private float horizontal_score;
    private float vertical_score;
    private float overall_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();


        Bundle extras = getIntent().getExtras();
        int cCount = 0; int cTime = 0;
        int vCount = 0; int vTime = 0;
        int hCount = 0; int hTime = 0;
        if (extras != null) {
            cCount = extras.getInt("CCOUNT"); cTime = extras.getInt("CTIME");
            vCount = extras.getInt("VCOUNT"); vTime = extras.getInt("VTIME");
            hCount = extras.getInt("HCOUNT"); hTime = extras.getInt("HTIME");

        }

        circular_score = 100 - (cCount*0.3f + cTime*0.7f)*2f;
        vertical_score = 100 - (vCount*0.3f + vTime*0.7f)*2f;
        horizontal_score = 100 - (hCount*0.3f + hTime*0.7f)*2f;

        overall_score = (circular_score + vertical_score + horizontal_score)/3f;
        String score = overall_score + "_" + circular_score + "_" + vertical_score + "_" + horizontal_score;
        fireMessage(score);
        Log.d("Result", score);

        // Stop AccelService
        stopService(new Intent(this, AccelService.class));
    }


    private void fireMessage(String text) {
        Intent msgIntent = new Intent(this, SendPhoneMessageIntentService.class);
        msgIntent.putExtra(SendPhoneMessageIntentService.INPUT_EXTRA, text);
        startService(msgIntent);
    }

}
