package javis.wearsyncservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class TimerActivity extends WearableActivity {

    private TextView mTimerTextView;
    private RelativeLayout mCircularView;
    private RelativeLayout mHorizontalView;
    private RelativeLayout mVerticalView;
    private RelativeLayout mSideView;
    private TextView mSideViewText;
    private RelativeLayout mClean;
    private ImageView mTopTeeth;
    private ImageView mBotTeeth;

    public HashMap<String, Integer> motion_counts = new HashMap<>();

    private TextView mAcce;
    private MyReceiver myReceiver;
    private float speed;
    private RelativeLayout mRelativeLayout;
    final int defaultBrushingTime = 120;
    final int interval = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mTimerTextView = (TextView) findViewById(R.id.timer);
        mCircularView = (RelativeLayout) findViewById(R.id.circular);
        mHorizontalView = (RelativeLayout) findViewById(R.id.horizontal);
        mVerticalView =  (RelativeLayout) findViewById(R.id.vertical);
        mSideView = (RelativeLayout) findViewById(R.id.brushside);
        mSideViewText = (TextView) findViewById(R.id.brushside_text);
        mClean = (RelativeLayout) findViewById(R.id.clean);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.back);

        mTopTeeth = (ImageView) findViewById(R.id.teeth_image_clean_top);
        mBotTeeth = (ImageView) findViewById(R.id.teeth_image_clean_bot);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        // Add initialize HashMap values
        motion_counts.put("cCount", 0); motion_counts.put("cTime", 0);
        motion_counts.put("hCount", 0); motion_counts.put("hTime", 0);
        motion_counts.put("vCount", 0); motion_counts.put("vTime", 0);


        // Set BroadcastReceiver
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACCELEROMETER");
        registerReceiver(myReceiver, intentFilter);



        // Start AccelService
        Intent accIntent = new Intent(this, AccelService.class);
        Log.d("TimerActivity", "timer starting acc");
        startService(accIntent);


        Drawable drawableTop = getResources().getDrawable(R.drawable.teeth_clean_top);
        Drawable drawableBot = getResources().getDrawable(R.drawable.teeth_clean_bot);
        final Bitmap mBitTop = ((BitmapDrawable) drawableTop).getBitmap();
        final Bitmap mBitBot = ((BitmapDrawable) drawableBot).getBitmap();
        final int xwidth = mBitTop.getWidth();
        final int xheight = mBitTop.getHeight();




        // Start the timer
        CountDownTimer timer = new CountDownTimer(defaultBrushingTime * 1000 + 5000, 1000) {

            final int initialTime = defaultBrushingTime * 1000;
            final int t = (initialTime - (16*1000*interval))/12;
            final long intervalLong = interval*1000;
            long last_stop = defaultBrushingTime * 1000;
            boolean doCount = false;

            private int redCount = 0;
            private int redTime = 0;
            private Boolean is_circular;
            private Boolean is_vertical;
            private Boolean is_horizontal;


            public void changeScreen(long millisUntilFinished) {

                if (millisUntilFinished < 5000 && millisUntilFinished <= 6000){
                    mClean.setVisibility(View.VISIBLE);
                    vibrator.vibrate(500);
                    return;
                }



                int quad = (int) ((millisUntilFinished - 5000) * 4 / initialTime);
                long pos = millisUntilFinished - 5000 - (quad * initialTime / 4);
                Log.d("TAG", pos + " " + quad + " " + millisUntilFinished + " " + initialTime);



                if (t <= pos && pos < t + intervalLong) {
                    Log.d("TAG", "pos3");
                    mTimerTextView.setVisibility(View.INVISIBLE);
                    mHorizontalView.setVisibility(View.VISIBLE);
                    is_horizontal = true; is_circular = false; is_vertical=false;

                } else if (t*2 + intervalLong <= pos && pos < t*2 + intervalLong*2) {
                    Log.d("TAG", "pos2");

                    mTimerTextView.setVisibility(View.INVISIBLE);
                    mVerticalView.setVisibility(View.VISIBLE);
                    is_horizontal = false; is_circular = false; is_vertical=true;

                } else if (t*3 + intervalLong*2 <= pos && pos < t*3 + intervalLong*3) {
                    Log.d("TAG", "pos1");

                    mSideView.setVisibility(View.INVISIBLE);
                    mCircularView.setVisibility(View.VISIBLE);
                    is_horizontal = false; is_circular = true; is_vertical=false;

                } else if (t*3 + intervalLong *3 <= pos || pos == 0) {
                    if (quad == 0) {
                        Log.d("TAG", "quad0");
                        mSideViewText.setText("top left");
                    } else if (quad == 1) {
                        Log.d("TAG", "quad1");
                        mSideViewText.setText("bottom left");
                    } else if (quad == 2) {
                        Log.d("TAG", "quad2");
                        mSideViewText.setText("top right");
                    } else {
                        Log.d("TAG", "quad3");
                        mSideViewText.setText("bottom right");
                    }
                    mTimerTextView.setVisibility(View.INVISIBLE);
                    mSideView.setVisibility(View.VISIBLE);
                } else {
                    Log.d("TAG", "timer");
                    mTimerTextView.setText(millisToString(millisUntilFinished, -5));
                    mHorizontalView.setVisibility(View.INVISIBLE);
                    mVerticalView.setVisibility(View.INVISIBLE);
                    mCircularView.setVisibility(View.INVISIBLE);
                    mTimerTextView.setVisibility(View.VISIBLE);
                }
            }


            public void onTick(long millisUntilFinished) {

                // color code
                mTimerTextView.setText(millisToString(millisUntilFinished, initialTime*3));
                if (speed < 25) {
                    if (doCount) {
                        if (last_stop - millisUntilFinished > 5000) {
                            mRelativeLayout.setBackgroundColor(0xffeb5757);

                            redTime += 1;
                            if (is_circular) {
                                motion_counts.put("cTime", motion_counts.get("cTime") + 1);
                                Log.d("***********Timer", "circular Time");
                            } else if (is_horizontal) {
                                motion_counts.put("hTime", motion_counts.get("hTime") + 1);
                                Log.d("*****************Timer", "horizontal Time");
                            } else {
                                motion_counts.put("vTime", motion_counts.get("vTime") + 1);
                                Log.d("************Timer", "vertical Time");
                            }




                            if (last_stop - millisUntilFinished <= 6000) {
                                Log.d("Timer", "It is read so I am adding to ");

                                // Add to Specific Counts
                                if (is_circular) {
                                    motion_counts.put("cCount", motion_counts.get("cCount") + 1);
                                    Log.d("***********Timer", "circular");
                                } else if (is_horizontal) {
                                    motion_counts.put("hCount", motion_counts.get("hCount") + 1);
                                    Log.d("*****************Timer", "horizontal");

                                } else {
                                    motion_counts.put("vCount", motion_counts.get("vCount") + 1);
                                    Log.d("************Timer", "vertical");

                                }

                                redCount += 1;
                            }
                        }
                    } else {
                        doCount = true;
                        last_stop = millisUntilFinished;
                    }
                } else {
                    mRelativeLayout.setBackgroundColor(0xff84e96d);
                    doCount = false;
                }


                // Watch animation
                if (millisUntilFinished-5000 >= initialTime/2) {
                    int x = ((int)(xwidth*(millisUntilFinished-5000-initialTime/2)/(initialTime/2)));
                    if (x >= xwidth) {
                        x = xwidth - 1;
                    }

                    Bitmap topImg = Bitmap.createBitmap(mBitTop, x, 0, xwidth-x, xheight);
                    Bitmap botImg = Bitmap.createBitmap(mBitBot, 0, 0, 1, xheight);

                    mTopTeeth.setImageDrawable(new BitmapDrawable(topImg));
                    mTopTeeth.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    mBotTeeth.setImageDrawable(new BitmapDrawable(botImg));
                    mBotTeeth.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else if (millisUntilFinished > 5000){
                    int x = ((int)(xwidth*(millisUntilFinished-5000)/(initialTime/2)));
                    if (x >= xwidth) {
                        x = xwidth - 1;
                    }

                    Bitmap botImg = Bitmap.createBitmap(mBitBot, 0, 0, xwidth-x, xheight);

                    mBotTeeth.setImageDrawable(new BitmapDrawable(botImg));
                    mBotTeeth.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }


                // screen
                changeScreen(millisUntilFinished);

//                if (millisUntilFinished > 146000) {
//                    mSideViewText.setText("top left");
//                    mTimerTextView.setVisibility(View.INVISIBLE);
//                    mSideView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 143000) {
//                    mSideView.setVisibility(View.INVISIBLE);
//                    mCircularView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 113000) {
//                    mTimerTextView.setText(millisToString(millisUntilFinished, -3*6-5));
//                    mCircularView.setVisibility(View.INVISIBLE);
//                    mTimerTextView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 110000) {
//                    mSideViewText.setText("top right");
//                    mTimerTextView.setVisibility(View.INVISIBLE);
//                    mSideView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 107000) {
//                    mSideView.setVisibility(View.INVISIBLE);
//                    mVerticalView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 77000) {
//                    mTimerTextView.setText(millisToString(millisUntilFinished, -2*6-5));
//                    mVerticalView.setVisibility(View.INVISIBLE);
//                    mTimerTextView.setVisibility(View.VISIBLE);
//
//                } else if (millisUntilFinished > 74000) {
//                    mSideViewText.setText("bottom left");
//                    mTimerTextView.setVisibility(View.INVISIBLE);
//                    mSideView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 71000) {
//                    mSideView.setVisibility(View.INVISIBLE);
//                    mHorizontalView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 41000) {
//                    mTimerTextView.setText(millisToString(millisUntilFinished, -1*6-5));
//                    mHorizontalView.setVisibility(View.INVISIBLE);
//                    mTimerTextView.setVisibility(View.VISIBLE);
//
//                } else if (millisUntilFinished > 38000) {
//                    mSideViewText.setText("bottom right");
//                    mTimerTextView.setVisibility(View.INVISIBLE);
//                    mSideView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 35000) {
//                    mSideView.setVisibility(View.INVISIBLE);
//                    mCircularView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 5000) {
//                    mTimerTextView.setText(millisToString(millisUntilFinished, -5));
//                    mCircularView.setVisibility(View.INVISIBLE);
//                    mTimerTextView.setVisibility(View.VISIBLE);
//                } else if (millisUntilFinished > 4000){
//                    mClean.setVisibility(View.VISIBLE);
//                    vibrator.vibrate(500);
//                }
            }

            private String millisToString(long millisUntilFinished, int adjustment) {
                int seconds = (int) millisUntilFinished / 1000 + adjustment;
                int minutes = seconds / 60;
//                return Integer.toString(seconds) + " raw:" + Integer.toString((int) millisUntilFinished / 1000);

                seconds = seconds - minutes * 60;
                if (seconds <= 0) {
                    return "0:00";
                }
                return Integer.toString(minutes) + ":" + String.format("%02d", seconds);
            }

            public void onFinish() {
                Intent i = new Intent(TimerActivity.this, ResultActivity.class);

                i.putExtra("REDCOUNT", redCount);
                i.putExtra("REDTIME", redTime);

                int cCount = motion_counts.get("cCount"); int cTime = motion_counts.get("cTime");
                int vCount = motion_counts.get("vCount"); int vTime = motion_counts.get("vTime");
                int hCount = motion_counts.get("hCount"); int hTime = motion_counts.get("hTime");

                i.putExtra("CCOUNT", cCount); i.putExtra("CTIME", cTime);
                i.putExtra("VCOUNT", vCount); i.putExtra("VTIME", vTime);
                i.putExtra("HCOUNT", hCount); i.putExtra("HTIME", hTime);


                Log.d("FINISHED", "circular C = " + cCount);
                Log.d("FINISHED", "vertical C = " + vCount);
                Log.d("FINISHED", "horizontal C = " + hCount);

                Log.d("FINISHED", "circular C = " + cTime);
                Log.d("FINISHED", "vertical C = " + vTime);
                Log.d("FINISHED", "horizontal C = " + hTime);

                startActivity(i);
            }
        };
        timer.start();




    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            String acc = arg1.getStringExtra("ACCE");
            speed = Float.parseFloat(acc);
            Log.d("TimerActivity", acc);
        }

    }
}
