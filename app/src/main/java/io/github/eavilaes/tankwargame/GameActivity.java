package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

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

        Tank.setImageView((ImageView) findViewById(R.id.tank_player));
        Tank.setRotation(90);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Create joystick for tank's movement
        final JoystickView joystick = (JoystickView) findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d(LOG_TAG, "joystick move. Angle:" + angle);
                Tank.setRotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                Log.d(LOG_TAG, "x: " + x + "   y: " + y);
                float newX = Tank.getX()+(float)x * strength * Tank.speedMultiplier;
                float newY = Tank.getY()+(float)-y * strength * Tank.speedMultiplier;

                if(newX>20 && newX<W-115) {
                    if(!coll)
                        Tank.setX(newX);
                    else
                        Tank.setX(Tank.getX()+(float)x * strength * Tank.speedMultiplier/2);
                    coll=false;
                }
                else
                    coll=true;
                if(newY>0 && newY<H-140) {
                    if (!coll)
                        Tank.setY(newY);
                    else
                        Tank.setY(Tank.getY() + (float) -y * strength * Tank.speedMultiplier / 2);
                    coll=false;
                }
                else
                    coll=true;
            }
        });

    }

    public void finishGame(View view) {
        finish();
    }
}