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

public class MusicPlaying extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SensorManager sensorManager;
    private MusicIntentReceiver myReceiver;
    private NetworkChangedReceiver mConnReceiver;
    private UserProfileObj userProfileObj = new UserProfileObj();
    private final String CONNECTIVITY = "android.net.conn.CONNECTIVITY_CHANGE";
    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;


    // conditional variables
    boolean speed_condition = false;
    boolean connectivity_condition =  false;
    boolean headset_condition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplaying);

        myReceiver = new MusicIntentReceiver();

        registerReceiver();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

        detectActivity();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        System.out.println("Headset is unplugged");
                        headset_condition = false;
                        //Running, wifi connected, headphones connected
                        if (speed_condition && connectivity_condition) {
                            Toast.makeText(MusicPlaying.this,
                                    "Headset is Unplugged", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        System.out.println("Headset is plugged");
                        headset_condition = true;
                        System.out.println("Rohit 1 " + speed_condition);
                        System.out.println("Rohit 2 " + connectivity_condition);
                        System.out.println("Rohit 3 " + headset_condition);

                        //Running, wifi connected, headphones connected
                        if (speed_condition && headset_condition && connectivity_condition) {
                            //TODO: Add Channel Code
                            watchYoutubeVideo("AQ-P5RR7r40");
                        }
                        break;
                    default:
                        System.out.println("I have no idea what the headset state is");
                        headset_condition = false;
                }
            }
        }
    }

    @Override
    public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    private void registerReceiver() {
        mConnReceiver = new NetworkChangedReceiver();
        registerReceiver(mConnReceiver, new IntentFilter(CONNECTIVITY));
    }

    private void internetChanged(boolean networkAvailable) {

        System.out.println("Internet Changed");
        if (networkAvailable) {
            System.out.println("Internet Connected");
            connectivity_condition = true;

        } else {
            System.out.println("Internet Not Connected");
            connectivity_condition = false;
        }
    }


    private class NetworkChangedReceiver extends BroadcastReceiver {
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
        if (!hasLocationPermission()) {
            System.out.println("Does not have location permission granted");
            requestLocationPermission();
            return false;
        }

        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                MusicPlaying.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION_RESULT_CODE);
    }


    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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
                    System.out.println("Location permission denied.");
                }
            }
        }
    }

    private void detectActivity() {
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        ActivityRecognitionResult result = detectedActivityResult.getActivityRecognitionResult();
                        System.out.println("time: " + result.getTime());
                        System.out.println("elapsed time: " + result.getElapsedRealtimeMillis());
                        System.out.println("Most likely activity: " + result.getMostProbableActivity().toString());

                        for (DetectedActivity activity : result.getProbableActivities()) {
                            System.out.println("Activity: " + activity.getType() + " Likelihood: " + activity.getConfidence());
                            if(activity.getType() == 3 && activity.getConfidence() >= 60) {
                                System.out.println("SAMrudhi here: " + activity.getType());
                                speed_condition = true;
                            }
                        }
                    }
                });
    }

}
