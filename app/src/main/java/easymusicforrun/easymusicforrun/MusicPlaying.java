package easymusicforrun.easymusicforrun;

import android.content.Context;
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
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.net.ConnectivityManager;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.util.Log;


public class MusicPlaying extends AppCompatActivity {

    private SensorManager sensorManager;
    boolean speed_condition = true;
    private MusicIntentReceiver myReceiver;
    private  NetworkChangedReceiver mConnReceiver;
    private UserProfileObj userProfileObj = new UserProfileObj();
    private final String CONNECTIVITY = "android.net.conn.CONNECTIVITY_CHANGE";

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
            //watchYoutubeVideo("AQ-P5RR7r40");
        }

        if(isOnline()){
            System.out.println("Works");
        }

        myReceiver = new MusicIntentReceiver();

        registerReceiver();

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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
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

    @Override public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        System.out.println("Headset is unplugged");
                        break;
                    case 1:
                        System.out.println("Headset is plugged");
                        break;
                    default:
                        System.out.println("I have no idea what the headset state is");
                }
            }
        }
    }

    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    private void registerReceiver() {
        mConnReceiver = new NetworkChangedReceiver();
        registerReceiver(mConnReceiver, new IntentFilter(CONNECTIVITY));
    }


    private void internetChanged(boolean networkAvailable) {

        System.out.println("Internet Changed");
        if(networkAvailable){

            System.out.println("Yo1");

        }else{
            System.out.println("Yo2");
        }
    }


    private class NetworkChangedReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            internetChanged(isNetworkAvailable(ctx));
        }

        // Checks if Internet is Online via Wifi or Mobile
        public boolean isNetworkAvailable(Context context) {
            boolean isMobile = false, isWifi = false;

            NetworkInfo[] infoAvailableNetworks = getConnectivityManagerInstance(context).getAllNetworkInfo();

            if (infoAvailableNetworks != null) {
                for (NetworkInfo network : infoAvailableNetworks) {

                    if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (network.isConnected() && network.isAvailable())
                            isWifi = true;
                    }
                    if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                        if (network.isConnected() && network.isAvailable())
                            isMobile = true;
                    }
                }
            }

            return isMobile || isWifi;
        }

        private ConnectivityManager getConnectivityManagerInstance(Context context) {
            return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

    }


}
