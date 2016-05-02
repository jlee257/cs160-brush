package javis.wearsyncservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

public class Registration2Activity extends Activity {

    final String SETTINGS_FILE = "BRUSH_SETTINGS";
    private SharedPreferences.Editor settingsEditor;

    private TextView righHandView;
    private  TextView leftHandView;
    private EditText editAge;
    private Boolean is_left_handed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        Bitmap bmp = getImageBitmap(this, "profile","BMP");
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        righHandView = (TextView) findViewById(R.id.textViewRight);
        leftHandView = (TextView) findViewById(R.id.textViewLeft);
        editAge = (EditText) findViewById(R.id.editTextAge);
    }

    public void do_photo_selection_2(View w)
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

    public void go_to_pink_q(View w)
    /**
     * Called onClick of the finish button. Moves to the Pink Questions screen.
     */
    {
        onSave(w);
        Intent next = new Intent(this, childQuestionsPink.class);
        startActivity(next);
    }

    public void onSave(View v) {
        settingsEditor = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE).edit();
        String ageInput = editAge.getText().toString();
        int age;
        if (ageInput=="" || ageInput==null || ageInput.equals(""))
        {
            age = 0;
        }
        else {
            age = Integer.parseInt(ageInput);
        }

        settingsEditor.putInt("AGE", age);
        settingsEditor.putBoolean("IS_LEFT_HAND", is_left_handed);
        settingsEditor.commit();
    }

    public void selectLeftHand(View v) {
        leftHandView.setTextColor(getResources().getColor(R.color.orange));
        righHandView.setTextColor(getResources().getColor(R.color.darkGrey));
        is_left_handed = true;
    }

    public void selectRightHand(View v) {
        leftHandView.setTextColor(getResources().getColor(R.color.darkGrey));
        righHandView.setTextColor(getResources().getColor(R.color.orange));
        is_left_handed = false;
    }

}
