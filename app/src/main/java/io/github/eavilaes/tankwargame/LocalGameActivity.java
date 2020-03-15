package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class LocalGameActivity extends AppCompatActivity {

    private static final float bulletSpeedMultiplier = 50.0f;

    Point size = new Point();
    private int W;
    private int H;

    Tank player1, player2;

    private static final String LOG_TAG = "LocalGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);

        //Enable fullscreen to hide status bar. Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get size of the screen to set limits
        getWindowManager().getDefaultDisplay().getSize(size);
        W = size.x;
        H = size.y;

        player1 = new Tank((ImageView)findViewById(R.id.tank_player));
        player2 = new Tank((ImageView)findViewById(R.id.tank_player2));
        player1.setRotation(90);
        player2.setRotation(270);

        //Create joystick for tank's movement
        final JoystickView joystickP1 = findViewById(R.id.joystick);
        joystickP1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                player1.setRotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                float newX = player1.getPosX()+(float)x * strength * Tank.speedMultiplier;
                float newY = player1.getPosY()+(float)-y * strength * Tank.speedMultiplier;

                boolean collX = checkCollisionX(newX);
                boolean collY = checkCollisionY(newY);

                if(!collX && !collY){ //If the tank doesn't collide, normal speed
                    player1.setPosX(newX);
                    player1.setPosY(newY);
                }else if(!collX) //If the tank collides with Y (sides), X speed /2
                    player1.setPosX(player1.getPosX() + (float)x * strength * Tank.speedMultiplier / 2);
                else //If the tank collides with X (top/bot), Y speed /2
                    player1.setPosY(player1.getPosY() + (float)-y * strength * Tank.speedMultiplier / 2);
            }
        });

        //Create joystick for player 2 tank's movement
        final JoystickView joystickP2 = findViewById(R.id.joystick2);
        joystickP2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                player2.setRotation(90-angle);
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                float newX = player2.getPosX()+(float)x * strength * Tank.speedMultiplier;
                float newY = player2.getPosY()+(float)-y * strength * Tank.speedMultiplier;

                boolean collX = checkCollisionX(newX);
                boolean collY = checkCollisionY(newY);

                if(!collX && !collY){
                    player2.setPosX(newX);
                    player2.setPosY(newY);
                }else if(!collX)
                    player2.setPosX(player2.getPosX() + (float)x * strength * Tank.speedMultiplier /2);
                else
                    player2.setPosY(player2.getPosY() + (float)y * strength * Tank.speedMultiplier /2);
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
        findViewById(R.id.joystick2).setEnabled(false);
        findViewById(R.id.fireButton).setEnabled(false);
        findViewById(R.id.fireButton2).setEnabled(false);
        findViewById(R.id.pauseButton).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.quitButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.resumeButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.joystick).setEnabled(true);
        findViewById(R.id.joystick2).setEnabled(true);
        findViewById(R.id.fireButton).setEnabled(true);
        findViewById(R.id.fireButton2).setEnabled(true);
        findViewById(R.id.pauseButton).setVisibility(View.VISIBLE);
    }


    public void fireBullet(View view){
        fireBullet(view, player1);
    }

    public void fireBullet2(View view){
        fireBullet(view, player2);
    }

    public void fireBullet(View view, Tank player) {
        final ImageView bullet = new ImageView(this);
        bullet.setImageResource(R.drawable.bullet1);
        final ConstraintLayout layout = findViewById(R.id.localGameLayout);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        float angle = player.getRotation()-90;
        final double x = Math.cos(Math.toRadians(angle));
        final double y = Math.sin(Math.toRadians(angle));
        bullet.setScaleX(0.15f);
        bullet.setScaleY(0.15f);

        bullet.setX(player.getPosX());
        bullet.setY(player.getPosY()+20);
        bullet.setRotation(angle-90);
        layout.addView(bullet, layoutParams);

        final Handler handler = new Handler();
        final int delay = 50; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                bullet.setX((float) (bullet.getX() + x * bulletSpeedMultiplier));
                bullet.setY((float) (bullet.getY() + y * bulletSpeedMultiplier));

                if(!checkCollisionX(bullet.getX()) && !checkCollisionY(bullet.getY()))
                    handler.postDelayed(this, delay);
                else {
                    Log.d(LOG_TAG, "Bullet collision");
                    layout.removeView(bullet);

                }
            }
        }, delay);

    }
}