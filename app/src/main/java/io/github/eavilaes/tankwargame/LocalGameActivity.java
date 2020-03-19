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
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class LocalGameActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LocalGameActivity";
    ConstraintLayout layout;

    private static final float bulletSpeedMultiplier = 50.0f;
    private static final int NANOS_TO_SECONDS = 1000000000;
    private static final int MAX_BULLET_TIME = 3; //seconds
    Tank player1, player2;
    Point size = new Point();
    private int W;
    private int H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);
        layout = findViewById(R.id.localGameLayout);

        //Enable fullscreen to hide status bar. Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get size of the screen to set limits
        getWindowManager().getDefaultDisplay().getSize(size);
        W = size.x;
        H = size.y;

        //Create tanks and set rotations
        player1 = new Tank((ImageView)findViewById(R.id.tank_player),1);
        player2 = new Tank((ImageView)findViewById(R.id.tank_player2),2);
        Log.d(LOG_TAG, player1.toString());
        player1.setRotation(90);
        player2.setRotation(270);

        //Add outer walls to the collision system
        CollisionSystem collSys = CollisionSystem.getInstance();
        collSys.addCollider(new Wall(findViewById(R.id.wallN)));
        collSys.addCollider(new Wall(findViewById(R.id.wallS)));
        collSys.addCollider(new Wall(findViewById(R.id.wallE)));
        collSys.addCollider(new Wall(findViewById(R.id.wallW)));


        //Create joystick 1 for tank's movement
        final JoystickView joystickP1 = findViewById(R.id.joystick);
        joystickP1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                float newX = player1.getPosX()+(float)x * strength * Tank.speedMultiplier;
                float newY = player1.getPosY()+(float)-y * strength * Tank.speedMultiplier;

                if(!CollisionSystem.getInstance().checkCollisions(player1, newX, newY)){
                    player1.setRotation(90-angle);
                    player1.setPosX(newX);
                    player1.setPosY(newY);
                    CollisionSystem.getInstance().fixTankRotationCollision(player1);
                }
            }
        });

        //Create joystick for player 2 tank's movement
        final JoystickView joystickP2 = findViewById(R.id.joystick2);
        joystickP2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double x = Math.cos(Math.toRadians(angle));
                double y = Math.sin(Math.toRadians(angle));
                float newX = player2.getPosX()+(float)x * strength * Tank.speedMultiplier;
                float newY = player2.getPosY()+(float)-y * strength * Tank.speedMultiplier;

                if(!CollisionSystem.getInstance().checkCollisions(player2, newX, newY)){
                    player2.setRotation(90-angle);
                    player2.setPosX(newX);
                    player2.setPosY(newY);
                    CollisionSystem.getInstance().fixTankRotationCollision(player2);
                }
            }
        });

    } // -- End onCreate() --

    //Finish the game and return to the previous activity.
    public void finishGame(View view) {
        finish();
    }

    boolean checkCollisions(int id, float newX, float newY){
        return CollisionSystem.getInstance().checkCollisions(id, newX, newY);
    }

    //Pause the game and open a pause menu
    public void pauseGame(View view) {
        onPause();
    }

    //Resume the game hiding the pause menu
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

    //Player 1 calls this method when firing
    public void fireBullet(View view){
        fireBullet(view, player1);
    }

    //Player 2 calls this method when firing
    public void fireBullet2(View view){
        fireBullet(view, player2);
    }

    void score(Tank player){
        TextView scoreT;
        if(player.getNPlayer()==1)
            scoreT = findViewById(R.id.scoreP1);
        else
            scoreT = findViewById(R.id.scoreP2);
        int score = Integer.parseInt(scoreT.getText().toString());
        score++;
        scoreT.setText(Integer.toString(score));
    }

    //Manage the bullets fired
    public void fireBullet(View view, final Tank player) {
        if(player.getLastShot()==-1 || (System.nanoTime() - player.getLastShot())/NANOS_TO_SECONDS > player.getFiringCD()) {
            player.newShot();
            final ImageView bullet = new ImageView(this);
            bullet.setImageResource(R.drawable.bullet1);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            final Collider bulletC = new Collider(true, bullet);
            bulletC.addDoesntAffect(player);
            CollisionSystem.getInstance().addCollider(bulletC);

            //Get the angle of the tank and calculate X and Y strengths
            float angle = player.getRotation() - 90;
            final double x = Math.cos(Math.toRadians(angle));
            final double y = Math.sin(Math.toRadians(angle));
            bullet.setScaleX(0.15f);
            bullet.setScaleY(0.15f);

            bullet.setX(player.getPosX());
            bullet.setY(player.getPosY() + 20);
            bullet.setRotation(angle - 90);
            layout.addView(bullet, layoutParams);

            //Handler to create a thread to control the bullet
            final Handler handler = new Handler();
            final int delay = 50; //Update delay in milliseconds

            final long startTime = System.nanoTime();

            handler.postDelayed(new Runnable() {
                public void run() {
                    bullet.setX((float) (bullet.getX() + x * bulletSpeedMultiplier));
                    bullet.setY((float) (bullet.getY() + y * bulletSpeedMultiplier));

                    //if(!checkCollisionX(bullet.getX()) && !checkCollisionY(bullet.getY()))
                    if (!checkCollisions(bulletC.getId(), bullet.getX(), bullet.getY())) {
                        if ((System.nanoTime() - startTime) / NANOS_TO_SECONDS < MAX_BULLET_TIME) //Converting nanoseconds to seconds
                            handler.postDelayed(this, delay);
                    } else {
                        Log.d(LOG_TAG, "Bullet collision");
                        score(player);
                        layout.removeView(bullet);
                        CollisionSystem.getInstance().removeCollider(bulletC);
                    }
                }
            }, delay);
        }
    }
}