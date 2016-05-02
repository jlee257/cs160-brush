package javis.wearsyncservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;


public class settings extends SlidingMenuActivity {

    final String SETTINGS_FILE = "BRUSH_SETTINGS";
    private SharedPreferences settings;

    private String first_name = "";
    private String last_name = "";
    private int age = -1;
    private boolean is_male = true;
    private boolean is_left_hand = true;

    private boolean local_is_male;
    private boolean local_is_left_hand;

    private RelativeLayout cancel_button;
    private RelativeLayout edit_button;
    private EditText view_first_name;
    private EditText view_last_name;
    private EditText view_age;
    private TextView view_male;
    private TextView view_female;
    private TextView view_left_hand;
    private TextView view_right_hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //setContentView(R.layout.settings);

        if (savedInstanceState!=null)
        {
            savedInstanceState.putString("TITLE", "SETTINGS");
            savedInstanceState.putInt("LAYOUT", R.layout.settings);
            savedInstanceState.putInt("LAYOUT_ID", 34534);
            super.onCreate(savedInstanceState);
        }

        else
        {
            Bundle b = new Bundle();
            b.putString("TITLE", "SETTINGS");
            b.putInt("LAYOUT", R.layout.settings);
            b.putInt("LAYOUT_ID", 34534); //id of top level Relative/Linear etc Layout
            super.onCreate(b);
        }

        SharedPreferences getSettings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        String name = getSettings.getString("FIRST_NAME", "No Name :(");
        if (name.equals(""))
        {
            name = "Your child";
        }
        TextView mainNameView = (TextView) findViewById(R.id.Name);
        mainNameView.setText(name);

        cancel_button = (RelativeLayout) findViewById(R.id.cancel_button);
        edit_button = (RelativeLayout) findViewById(R.id.edit_button);
        view_first_name = (EditText) findViewById(R.id.editTextFirstName);
        view_last_name = (EditText) findViewById(R.id.editTextLastName);
        view_age = (EditText) findViewById(R.id.editTextAge);
        view_male = (TextView) findViewById(R.id.textViewMale);
        view_female = (TextView) findViewById(R.id.textViewFemale);
        view_left_hand = (TextView) findViewById(R.id.textViewLeft);
        view_right_hand = (TextView) findViewById(R.id.textViewRight);

        Bitmap bmp = getImageBitmap(this, "profile","BMP");;
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        // get shared preferences
        settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        first_name = settings.getString("FIRST_NAME", "Andrew");
        if (first_name.equals(""))
        {
            first_name = "     ";
        }
        last_name = settings.getString("LAST_NAME", "Smith");
        if (last_name.equals(""))
        {
            last_name = "     ";
        }
        age = settings.getInt("AGE", 5);
        is_male = settings.getBoolean("IS_MALE", true);
        is_left_hand = settings.getBoolean("IS_LEFT_HAND", true);



        // update xml
        view_first_name.setText(first_name);
        Log.d("TAG", "set text: " + first_name);
        view_last_name.setText(last_name);
        view_age.setText(String.valueOf(age));
        switch_gender(is_male);
        switch_hand(is_left_hand);



        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_first_name.setText(first_name);
                view_last_name.setText(last_name);
                view_age.setText(String.valueOf(age));
                switch_gender(is_male);
                switch_hand(is_left_hand);

                View view = getCurrentFocus();
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    view.clearFocus();
                } catch (Exception e) {
                    Log.d("Settings", "error " + e.toString());
                }
            }
        });


        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                String fn = view_first_name.getText().toString();
                String ln = view_last_name.getText().toString();
                int ag = Integer.valueOf(view_age.getText().toString());

                editor.putString("FIRST_NAME", fn);
                editor.putString("LAST_NAME", ln);
                editor.putInt("AGE", ag);
                editor.putBoolean("IS_MALE", local_is_male);
                editor.putBoolean("IS_LEFT_HAND", local_is_left_hand);
                editor.commit();
                Log.d("TAG", "put first_name: " + fn);

                first_name = fn;
                last_name = ln;
                age = ag;
                is_male = local_is_male;
                is_left_hand = local_is_left_hand;

                View view = getCurrentFocus();
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    view.clearFocus();
                } catch (Exception e) {
                    Log.d("Settings", "error " + e.toString());
                }

                Toast.makeText(getBaseContext(), "SAVED", Toast.LENGTH_SHORT).show();
            }
        });


        view_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_gender(true);
            }
        });

        view_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_gender(false);
            }
        });

        view_left_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_hand(true);
            }
        });

        view_right_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_hand(false);
            }
        });
    }



    private void switch_gender(boolean to_male) {
        if (to_male) {
            view_male.setTextColor(getResources().getColor(R.color.orange));
            view_female.setTextColor(getResources().getColor(R.color.darkGrey));
            local_is_male = true;
        } else {
            view_male.setTextColor(getResources().getColor(R.color.darkGrey));
            view_female.setTextColor(getResources().getColor(R.color.orange));
            local_is_male = false;
        }
    }



    private void switch_hand(boolean to_left) {
        if (to_left) {
            view_left_hand.setTextColor(getResources().getColor(R.color.orange));
            view_right_hand.setTextColor(getResources().getColor(R.color.darkGrey));
            local_is_left_hand = true;
        } else {
            view_left_hand.setTextColor(getResources().getColor(R.color.darkGrey));
            view_right_hand.setTextColor(getResources().getColor(R.color.orange));
            local_is_left_hand = false;
        }
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

    public void settings_photo_selection(View w)
    /**
     * Called onClick of the purple photo button. Brings up the
     * photo selection dialog
     */
    {
        Log.d("BAZOOKA", "DETECTED THE PHOTO BUTTON CLICK");
        Intent intent = new Intent(this, CircCropActivity.class);
        startActivityForResult(intent, CircCropImplementation.BITMAP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BAZOOKA", "back from selecting the photo");
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



}