package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        //Print the stored username in the main screen (if any)
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String username = sharedPreferences.getString("username_local", "Player");
        TextView username_textview = (TextView)findViewById(R.id.username_local);
        username_textview.setText(username);

    }

    public void playButtonPressed(View view) {
        Log.d(LOG_TAG, "Play button pressed");
        Intent game = new Intent(this, GameActivity.class);
        startActivity(game);
        Log.d(LOG_TAG, "Game finished");
    }

    public void settingsButtonPressed(View view) {
        Log.d(LOG_TAG, "Settings button pressed");
        //TODO settings screen
    }
}