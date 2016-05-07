package javis.wearsyncservice;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Aspire on 4/18/2016.
 */
public class AccelService extends WearableListenerService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Context context;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AccelService", "onStartCommand called");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        Log.d("AccelService", "onCreate called");
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        context = AccelService.this.getApplicationContext();
    }


    @Override
    public void onDestroy() {
        sensorManager.unregisterListener((SensorEventListener) this, sensor);
        Log.d("AccelService", "sensor onDestroy");
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("AccelService", "onSensorChanged called");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 1000ms.
            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];




                float speed = (Math.abs(x- last_x) + Math.abs(y - last_y) + Math.abs(z - last_z))
                        / diffTime * 10000;

                //send Speed data to TimerActivity
                MyThread myThread = new MyThread(speed);
                myThread.start();


//                if (speed > SHAKE_THRESHOLD) {
//                    Log.d("AccelService", "Speed exceeds threshold: " + Float.toString(speed));
//                } else {
//                    Log.d("AccelService", "Speed below threshold: " + Float.toString(speed));
//                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //sends speed data to activity
    public class MyThread extends Thread{

        private float speed0;

        public MyThread(float speed) {
            super();
            speed0 = speed;
        }

        @Override
        public void run() {
            Log.d("AccelService", "sending data to TimerActivity");
            Intent intent = new Intent();
            intent.setAction("ACCELEROMETER");
            intent.putExtra("ACCE", Float.toString(speed0));
            sendBroadcast(intent);
        }

    }
}
