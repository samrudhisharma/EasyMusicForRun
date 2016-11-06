package easymusicforrun.easymusicforrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;

import android.app.PendingIntent;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Notification;
import android.location.Location;

/**
 * Created by samrudhi on 11/6/16.
 */
public class MusicPlaying extends AppCompatActivity {

    private SensorManager sensorManager;
    boolean speed_condition = true;
    private UserProfileObj userProfileObj = new UserProfileObj();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplaying);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        Sensor AccelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (AccelometerSensor != null) {
            sensorManager.registerListener(
                    AccelometerSensorListener,
                    AccelometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(speed_condition) {
            //TODO: Add Channel Code
            watchYoutubeVideo("AQ-P5RR7r40");
        }
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private final SensorEventListener AccelometerSensorListener
            = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                //Use POJO
                if (event.values[1] <= userProfileObj.getMinRunningSpeed()) {
                    speed_condition = true;
                }
            }
        }
    };


}
