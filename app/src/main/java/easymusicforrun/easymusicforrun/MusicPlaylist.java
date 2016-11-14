package easymusicforrun.easymusicforrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MusicPlaylist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplaylist);

        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener(new OnClickListener() {

            final EditText runningId   = (EditText)findViewById(R.id.editText);
            final EditText walkingId   = (EditText)findViewById(R.id.editText2);

            public void onClick(View v)
            {
                final String _runningId = runningId.getText().toString();
                final String _walkingId = walkingId.getText().toString();
            }
        });


    }
}
