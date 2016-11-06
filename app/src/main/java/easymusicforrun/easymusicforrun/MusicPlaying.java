package easymusicforrun.easymusicforrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;

/**
 * Created by samrudhi on 11/6/16.
 */
public class MusicPlaying extends AppCompatActivity {

    private UserProfileObj userProfileObj = new UserProfileObj();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplaying);

        watchYoutubeVideo("AQ-P5RR7r40");
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
}
