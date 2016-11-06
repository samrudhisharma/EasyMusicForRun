package easymusicforrun.easymusicforrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by samrudhi on 11/6/16.
 */
public class UserPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpreferences);
    }

    public void callMusicPlaying(View view)
    {
        Intent intent = new Intent(UserPreferences.this, MusicPlaying.class);
        startActivity(intent);
    }

    /*
    public void callMusicPlaying(View view)
    {
        Intent intent = new Intent(UserPreferences.this, MusicPlaying.class);
        startActivity(intent);
    }*/
}
