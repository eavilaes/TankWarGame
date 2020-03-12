package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    private static final String LOG_TAG = "GameActivity";
    private static ImageView tank_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tank_player = (ImageView) findViewById(R.id.tank_player);
        tank_player.setRotation(90);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Enable joystick for tank's movement
        final JoystickView joystick = (JoystickView) findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d(LOG_TAG, "joystick move. Angle:" + angle);
                tank_player.setRotation(90-angle);
            }
        });

    }

    public void finishGame(View view) {
        finish();
    }
}