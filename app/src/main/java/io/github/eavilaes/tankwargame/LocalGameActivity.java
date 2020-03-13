package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class LocalGameActivity extends AppCompatActivity {

    Point size = new Point();
    private int W;
    private int H;

    private static final String LOG_TAG = "LocalGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);

        //Get size of the screen to set limits
        getWindowManager().getDefaultDisplay().getSize(size);
        W = size.x;
        H = size.y;

        Tank.getPlayer().setPlayerImageView((ImageView) findViewById(R.id.tank_player));
        Tank.getPlayer().setPlayerRotation(90);

        Tank.getPlayer2().setPlayer2ImageView((ImageView) findViewById(R.id.tank_player2));
        Tank.getPlayer2().setPlayer2Rotation(270);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Create joystick for tank's movement
        final JoystickView joystick = findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                //Log.d(LOG_TAG, "joystick move. Angle:" + angle);
                Tank.getPlayer().setPlayerRotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                //Log.d(LOG_TAG, "x: " + x + "   y: " + y);
                float newX = Tank.getPlayer().getPlayerX()+(float)x * strength * Tank.speedMultiplier;
                float newY = Tank.getPlayer().getPlayerY()+(float)-y * strength * Tank.speedMultiplier;

                boolean collX = checkCollisionX(newX);
                boolean collY = checkCollisionY(newY);

                if(!collX && !collY){
                    Tank.getPlayer().setPlayerX(newX);
                    Tank.getPlayer().setPlayerY(newY);
                }else if(!collX)
                    Tank.getPlayer().setPlayerX(Tank.getPlayer().getPlayerX()+(float)x * strength * Tank.speedMultiplier / 2);
                else
                    Tank.getPlayer().setPlayerY(Tank.getPlayer().getPlayerY() + (float) -y * strength * Tank.speedMultiplier / 2);
            }
        });

        //Create joystick for player 2 tank's movement
        final JoystickView joystick2 = findViewById(R.id.joystick2);
        joystick2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                //Log.d(LOG_TAG, "joystick move. Angle:" + angle);
                Tank.getPlayer2().setPlayer2Rotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                //Log.d(LOG_TAG, "x: " + x + "   y: " + y);
                float newX = Tank.getPlayer2().getPlayer2X()+(float)x * strength * Tank.speedMultiplier;
                float newY = Tank.getPlayer2().getPlayer2Y()+(float)-y * strength * Tank.speedMultiplier;

                boolean collX = checkCollisionX(newX);
                boolean collY = checkCollisionY(newY);

                if(!collX && !collY){
                    Tank.getPlayer2().setPlayer2X(newX);
                    Tank.getPlayer2().setPlayer2Y(newY);
                }else if(!collX)
                    Tank.getPlayer2().setPlayer2X(Tank.getPlayer2().getPlayer2X()+(float)x * strength * Tank.speedMultiplier / 2);
                else
                    Tank.getPlayer2().setPlayer2Y(Tank.getPlayer2().getPlayer2Y() + (float) -y * strength * Tank.speedMultiplier / 2);
            }
        });

    }

    public void finishGame(View view) {
        finish();
    }

    boolean checkCollisionX(float newX){
        return !(newX > 20) || !(newX < W - 115);   //TODO: Try to change it for the tank's dimensions.
    }

    boolean checkCollisionY(float newY){
        return !(newY > 0) || !(newY < H - 140);
    }

    public void pauseGame(View view) {
        onPause();
    }

    public void resumeGame(View view) {
        onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
        findViewById(R.id.resumeButton).setVisibility(View.VISIBLE);
        findViewById(R.id.joystick).setEnabled(false);
        findViewById(R.id.pauseButton).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.quitButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.resumeButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.joystick).setEnabled(true);
        findViewById(R.id.pauseButton).setVisibility(View.VISIBLE);
    }

}