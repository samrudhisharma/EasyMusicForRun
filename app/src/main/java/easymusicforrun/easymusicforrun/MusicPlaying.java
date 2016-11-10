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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.ConnectionResult;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.snapshot.BeaconStateResult;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;



public class MusicPlaying extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SensorManager sensorManager;
    boolean speed_condition = true;
    private MusicIntentReceiver myReceiver;
    private  NetworkChangedReceiver mConnReceiver;
    private UserProfileObj userProfileObj = new UserProfileObj();
    private final String CONNECTIVITY = "android.net.conn.CONNECTIVITY_CHANGE";
    private GoogleApiClient mGoogleApiClient;

    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;

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

        //myReceiver = new MusicIntentReceiver();

        //registerReceiver();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

        detectHeadphones();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}



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


    //Awareness API related code
    private boolean checkLocationPermission() {
        if( !hasLocationPermission() ) {
            Log.e("Tuts+", "Does not have location permission granted");
            requestLocationPermission();
            return false;
        }

        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                MusicPlaying.this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_RESULT_CODE );
    }


    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RESULT_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Log.e("Tuts+", "Location permission denied.");
                }
            }
        }
    }


    private void detectHeadphones() {
        System.out.println("Rohit");
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                        if (headphoneState.getState() == HeadphoneState.PLUGGED_IN) {
                            Log.e("Tuts+", "Headphones are plugged in.");
                            System.out.println("Headphones Plugged In!!!!!!");
                        } else {
                            Log.e("Tuts+", "Headphones are NOT plugged in.");
                        }
                    }
                });
    }

    private void detectLocation() {
        if( !checkLocationPermission() ) {
            return;
        }
        System.out.println("Rohit");
        Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        Location location = locationResult.getLocation();

                        Log.e("Tuts+", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());

                        Log.e("Tuts+", "Provider: " + location.getProvider() + " time: " + location.getTime());

                        if( location.hasAccuracy() ) {
                            Log.e("Tuts+", "Accuracy: " + location.getAccuracy());
                        }
                        if( location.hasAltitude() ) {
                            Log.e("Tuts+", "Altitude: " + location.getAltitude());
                        }
                        if( location.hasBearing() ) {
                            Log.e("Tuts+", "Bearing: " + location.getBearing());
                        }
                        if( location.hasSpeed() ) {
                            Log.e("Tuts+", "Speed: " + location.getSpeed());
                        }
                    }
                });
    }

    private void detectWeather() {
        if( !checkLocationPermission() ) {
            return;
        }
        System.out.println("Rohit");
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        Weather weather = weatherResult.getWeather();
                        Log.e("Tuts+", "Temp: " + weather.getTemperature(Weather.FAHRENHEIT));
                        Log.e("Tuts+", "Feels like: " + weather.getFeelsLikeTemperature(Weather.FAHRENHEIT));
                        Log.e("Tuts+", "Dew point: " + weather.getDewPoint(Weather.FAHRENHEIT));
                        Log.e("Tuts+", "Humidity: " + weather.getHumidity() );

                        if( weather.getConditions()[0] == Weather.CONDITION_CLOUDY ) {
                            Log.e("Tuts+", "Looks like there's some clouds out there");
                        }
                    }
                });
    }

    private void detectActivity() {
        System.out.println("Rohit");
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        ActivityRecognitionResult result = detectedActivityResult.getActivityRecognitionResult();
                        Log.e("Tuts+", "time: " + result.getTime());
                        Log.e("Tuts+", "elapsed time: " + result.getElapsedRealtimeMillis());
                        Log.e("Tuts+", "Most likely activity: " + result.getMostProbableActivity().toString());

                        for( DetectedActivity activity : result.getProbableActivities() ) {
                            Log.e("Tuts+", "Activity: " + activity.getType() + " Likelihood: " + activity.getConfidence() );
                        }
                    }
                });
    }

}
