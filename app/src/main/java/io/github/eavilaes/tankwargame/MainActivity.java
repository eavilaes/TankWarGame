package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_REQUEST = 10;
    private TextView username_textview;
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        /*decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Print the stored username in the main screen (if any)
        SharedPreferences sharedPreferences = getSharedPreferences("SETTINGS_FILE", MODE_PRIVATE);
        String username = sharedPreferences.getString("username_local", "Player");
        username_textview= findViewById(R.id.username_local);
        username_textview.setText(username);

    }

    public void playButtonPressed(View view) {
        Log.d(LOG_TAG, "Play button pressed");
        Intent gameSelection = new Intent(this, GameSelectionActivity.class);
        startActivity(gameSelection);
    }

    public void settingsButtonPressed(View view) {
        Log.d(LOG_TAG, "Settings button pressed");
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivityForResult(settings, SETTINGS_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK){
            username_textview.setText(data.getStringExtra("newname"));
            Log.d(LOG_TAG, "Settings finished");
            //decorView.setSystemUiVisibility(uiOptions); //Set fullscreen again
        }
    }
}