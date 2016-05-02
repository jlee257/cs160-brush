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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

public class RegistrationActivity extends Activity {

    private Boolean is_male = true;
    final String SETTINGS_FILE = "BRUSH_SETTINGS";
    private SharedPreferences.Editor settingsEditor;

    private TextView maleView;
    private TextView femaleView;
    private EditText firstName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bitmap bmp = getImageBitmap(this, "profile","BMP");
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        maleView = (TextView) findViewById(R.id.textViewMale);
        femaleView = (TextView) findViewById(R.id.textViewFemale);

        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);

    }

    public void genderMale(View v) {
        maleView.setTextColor(getResources().getColor(R.color.orange)); // set color to orange
        femaleView.setTextColor(getResources().getColor(R.color.darkGrey));
        is_male = true;
    }
    public void genderFemale(View v) {
        maleView.setTextColor(getResources().getColor(R.color.darkGrey)); // set color to orange
        femaleView.setTextColor(getResources().getColor(R.color.orange));
        is_male = false;
    }

    public void onSave(View v) {

        String first_name = firstName.getText().toString();
        String last_name = lastName.getText().toString();

        //Saved to sharedPref
        settingsEditor = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE).edit();
        settingsEditor.putString("FIRST_NAME", first_name);
        settingsEditor.putString("LAST_NAME", last_name);
        settingsEditor.putBoolean("IS_MALE", is_male);

        settingsEditor.commit();
        Log.d("RegAct", "put into SP = " + first_name + last_name + is_male);
//        first_name = settings.getString
//        last_name = settings.getString
//        age = settings.getInt("AGE", 5);
//        is_male = settings.getBoolean(
//        is_left_hand = settings.getBoolean("IS_LEFT_HAND", true);
    }


    public void do_photo_selction(View w)
    /**
     * Called onClick of the purple photo button. Brings up the
     * photo selection dialog
     */
    {
        Intent intent = new Intent(this, CircCropActivity.class);
        startActivityForResult(intent, CircCropImplementation.BITMAP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmp = getImageBitmap(this, "profile","BMP");
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));
        /*switch(requestCode) {
            case (MY_CHILD_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                }
                break;
            }
        }*/
    }

    public void go_to_2_registration(View w)
    /**
     * Called onClick of the next button. Goes to the second registration screen.
     */
    {
        onSave(w);
        Intent next = new Intent(this, Registration2Activity.class);
        startActivity(next);
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
