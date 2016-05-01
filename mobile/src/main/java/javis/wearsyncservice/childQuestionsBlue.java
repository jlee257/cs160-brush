package javis.wearsyncservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

/**
 * Created by Me on 4/19/16.
 */
public class childQuestionsBlue extends Activity {

    final String SETTINGS_FILE = "BRUSH_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_questions_blue);

        Bitmap bmp = getImageBitmap(this, "profile","BMP");;
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        SharedPreferences getSettings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        String name = getSettings.getString("FIRST_NAME", "your child");
        Log.d("Settings Saved", name);
        if (name.equals(""))
        {
            name = "your child";
        }
        TextView mainNameView = (TextView) findViewById(R.id.question);
        mainNameView.setText("How many cavities has "+name+" had?");
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

    public void toDashboard(View v) {
        Intent next = new Intent(this, DashboardDay.class);
        startActivity(next);
    }
}
