package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    Point size = new Point();
    private int W;
    private int H;

    private Boolean coll=false;

    private static final String LOG_TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Get size of the screen to set limits
        getWindowManager().getDefaultDisplay().getSize(size);
        W = size.x;
        H = size.y;

        Tank.getInstance().setImageView((ImageView) findViewById(R.id.tank_player));
        Tank.getInstance().setRotation(90);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Create joystick for tank's movement
        final JoystickView joystick = findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                //Log.d(LOG_TAG, "joystick move. Angle:" + angle);
                Tank.getInstance().setRotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                //Log.d(LOG_TAG, "x: " + x + "   y: " + y);
                float newX = Tank.getInstance().getX()+(float)x * strength * Tank.speedMultiplier;
                float newY = Tank.getInstance().getY()+(float)-y * strength * Tank.speedMultiplier;

                boolean collX = checkCollisionX(newX);
                boolean collY = checkCollisionY(newY);

                if(!collX && !collY){
                    Tank.getInstance().setX(newX);
                    Tank.getInstance().setY(newY);
                }else if(!collX)
                    Tank.getInstance().setX(Tank.getInstance().getX()+(float)x * strength * Tank.speedMultiplier / 2);
                else
                    Tank.getInstance().setY(Tank.getInstance().getY() + (float) -y * strength * Tank.speedMultiplier / 2);
            }
        });

    }

    public void finishGame(View view) {
        finish();
    }

    boolean checkCollisionX(float newX){
        if(newX>20 && newX<W-115)  //TODO: Try to change it for the tank's dimensions.
            return false;
        return true;
    }

    boolean checkCollisionY(float newY){
        if(newY>0 && newY<H-140)
            return false;
        return true;
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