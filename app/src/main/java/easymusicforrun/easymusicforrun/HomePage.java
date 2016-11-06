package easymusicforrun.easymusicforrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.content.Intent;

/**
 * Created by samrudhi on 11/6/16.
 */
public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void callListActivity(View view)
    {
        Intent intent = new Intent(HomePage.this, ListActivity.class);
        startActivity(intent);
    }

    public void callChooseNotification(View view)
    {
        Intent intent = new Intent(HomePage.this, ChooseNotification.class);
        startActivity(intent);
    }

    public void callMusicPlaying(View view)
    {
        Intent intent = new Intent(HomePage.this, MusicPlaying.class);
        startActivity(intent);
    }

}
