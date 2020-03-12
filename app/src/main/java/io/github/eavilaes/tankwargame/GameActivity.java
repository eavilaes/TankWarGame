package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    private static final float speedMultiplier = 0.075f;

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
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                Log.d(LOG_TAG, "x: " + x + "   y: " + y);
                tank_player.setX(tank_player.getX()+(float)x * strength * speedMultiplier);
                tank_player.setY(tank_player.getY()+(float)-y * strength * speedMultiplier);
            }
        });

    }

    public void finishGame(View view) {
        finish();
    }
}