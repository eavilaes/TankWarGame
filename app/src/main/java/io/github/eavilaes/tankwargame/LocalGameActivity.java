package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private static SharedPreferences sharedPreferences;
    private static final float NANOS_TO_SECONDS = 1000000000;
    private static final int MAX_BULLET_TIME = 3; //max bullet time in seconds
    private static int GAME_TIME_MINUTES = 5; //max game time in minutes
    private static int GAME_MAX_SCORE = 10; //max game score (-1 means infinite)
    private static long GAME_INITIAL_COUNTDOWN_MILLIS = 6000; //6 seconds initial countdown will create a 5-to-0 countdown
    private static long GAME_TIME_MILLIS = GAME_TIME_MINUTES * 60000 + GAME_INITIAL_COUNTDOWN_MILLIS;
    Tank player1, player2;

    TextView timerTextView, countdownTextView;
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = GAME_TIME_MILLIS - (System.currentTimeMillis() - startTime);
            int seconds = (int) (millis/1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    Handler countdownHandler = new Handler();
    Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = GAME_INITIAL_COUNTDOWN_MILLIS - (System.currentTimeMillis() - startTime);
            if(millis<0) {
                countdownTextView.setVisibility(View.GONE);
                resumeControls();
            }
            else{
                pauseControls();
                int seconds = (int) (millis/1000);
                countdownTextView.setText(String.format("%d", seconds));
                countdownHandler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);
        layout = findViewById(R.id.localGameLayout);

        //Enable fullscreen to hide status bar. Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

                final int tankFixRotation = 0; //to move the tank a certain angle to search for better results.

                boolean coll=false;

                if(CollisionSystem.getInstance().xMovementLocked(player1, newX)){
                    if(angle>90 && angle <270) { //moving towards left wall
                        if (angle < 180) //a bit up
                            angle -= tankFixRotation;
                        else if (angle > 180) //a bit down
                            angle += tankFixRotation;
                        //check for overpassed vertical angle
                        if  (angle<90)
                            angle=90;
                        else if (angle>270)
                            angle=270;
                    }else { //moving towards right wall
                        if (angle > 0 && angle<90) //a bit up
                            angle += tankFixRotation;
                        else if (angle > 270)
                            angle -= tankFixRotation;
                        //check for overpassed vertical angle
                        if (angle>90 && angle<180)
                            angle=90;
                        else if(angle>180 && angle<270)
                            angle=270;
                    }
                    player1.setRotation(90-angle);
                    player1.setPosY(newY);
                    coll=true;
                }
                if(!coll){
                    player1.setRotation(90-angle);
                    player1.setPosX(newX);
                    player1.setPosY(newY);
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

                final int tankFixRotation = 0; //to move the tank a certain angle to search for better results.

                boolean coll=false;

                if(CollisionSystem.getInstance().xMovementLocked(player2, newX)){
                    if(angle>90 && angle <270) { //moving towards left wall
                        if (angle < 180) //a bit up
                            angle -= tankFixRotation;
                        else if (angle > 180) //a bit down
                            angle += tankFixRotation;
                        //check for overpassed vertical angle
                        if  (angle<90)
                            angle=90;
                        else if (angle>270)
                            angle=270;
                    }else { //moving towards right wall
                        if (angle > 0 && angle<90) //a bit up
                            angle += tankFixRotation;
                        else if (angle > 270)
                            angle -= tankFixRotation;
                        //check for overpassed vertical angle
                        if (angle>90 && angle<180)
                            angle=90;
                        else if(angle>180 && angle<270)
                            angle=270;
                    }
                    player2.setRotation(90-angle);
                    player2.setPosY(newY);
                    coll=true;
                }
                if(!coll){
                    player2.setRotation(90-angle);
                    player2.setPosX(newX);
                    player2.setPosY(newY);
                }
            }
        });

        //Load max time and score from shared preferences
        sharedPreferences = getSharedPreferences("SETTINGS_FILE", MODE_PRIVATE);
        GAME_TIME_MINUTES = sharedPreferences.getInt("game_time", GAME_TIME_MINUTES);
        GAME_TIME_MILLIS = GAME_TIME_MINUTES * 60000 + GAME_INITIAL_COUNTDOWN_MILLIS;
        GAME_MAX_SCORE = sharedPreferences.getInt("game_score", GAME_MAX_SCORE);


        //Create timer handler
        timerTextView = (TextView) findViewById(R.id.timer);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        //Create countdown
        countdownTextView = (TextView) findViewById(R.id.countdown);
        countdownHandler.postDelayed(countdownRunnable, 0);

    } // -- End onCreate() --

    @Override
    public void onBackPressed() {
        onPause();
    }

    //Pause the game and open a pause menu
    public void pauseGame(View view) {
        onPause();
    }

    //Resume the game hiding the pause menu
    public void resumeGame(View view) {
        onResume();
    }

    //Finish the game and return to the previous activity.
    public void finishGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    boolean checkBulletCollision(Bullet b, float newX, float newY){
        return CollisionSystem.getInstance().checkBulletCollisions(b, newX, newY);
    }

    void pauseControls(){
        findViewById(R.id.joystick).setEnabled(false);
        findViewById(R.id.joystick2).setEnabled(false);
        findViewById(R.id.fireButton).setEnabled(false);
        findViewById(R.id.fireButton2).setEnabled(false);
        findViewById(R.id.pauseButton).setVisibility(View.INVISIBLE);
    }

    void resumeControls(){
        findViewById(R.id.joystick).setEnabled(true);
        findViewById(R.id.joystick2).setEnabled(true);
        findViewById(R.id.fireButton).setEnabled(true);
        findViewById(R.id.fireButton2).setEnabled(true);
        findViewById(R.id.pauseButton).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Pause timer
        timerHandler.removeCallbacks(timerRunnable);

        //Freeze controls
        findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
        findViewById(R.id.resumeButton).setVisibility(View.VISIBLE);
        pauseControls();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Resume timer
        timerHandler.postDelayed(timerRunnable, 0);

        //Unfreeze controls
        findViewById(R.id.quitButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.resumeButton).setVisibility(View.INVISIBLE);
        resumeControls();
    }

    //Player 1 calls this method when firing
    public void fireBullet(View view){
        fireBullet(player1);
    }

    //Player 2 calls this method when firing
    public void fireBullet2(View view){
        fireBullet(player2);
    }

    //Manage the bullets fired
    public void fireBullet(final Tank player) {
        if(player.getLastShot()==-1 || (System.nanoTime() - player.getLastShot())/NANOS_TO_SECONDS > player.getFiringCD()) {
            player.newShot(); //sets the timer for the player (shooting cooldown)
            final ImageView bulletImg = new ImageView(this);
            bulletImg.setImageResource(R.drawable.bullet1);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            final Bullet bulletC = new Bullet(true, bulletImg);
            bulletC.addDoesntAffect(player); //The bullet won't hit the player shooting it
            CollisionSystem.getInstance().addCollider(bulletC);

            //Get the angle of the tank and calculate X and Y strengths
            float angle = player.getRotation() - 90;
            final double x = Math.cos(Math.toRadians(angle));
            final double y = Math.sin(Math.toRadians(angle));
            bulletImg.setScaleX(0.15f);
            bulletImg.setScaleY(0.15f);

            //Draw the bullet in its origin position
            bulletImg.setX(player.getPosX());
            bulletImg.setY(player.getPosY() + 20);
            bulletImg.setRotation(angle - 90);
            layout.addView(bulletImg, layoutParams);

            //Handler to create a thread to control the bullet
            final Handler handler = new Handler();
            final int delay = 50; //Update delay in milliseconds

            final long startTime = System.nanoTime();

            handler.postDelayed(new Runnable() {
                public void run() {
                    bulletImg.setX((float) (bulletImg.getX() + x * Bullet.bulletSpeedMultiplier));
                    bulletImg.setY((float) (bulletImg.getY() + y * Bullet.bulletSpeedMultiplier));

                    if (!checkBulletCollision(bulletC, bulletImg.getX(), bulletImg.getY())){
                        if ((System.nanoTime() - startTime) / NANOS_TO_SECONDS < MAX_BULLET_TIME) //Converting nanoseconds to seconds
                            handler.postDelayed(this, delay);
                    } else {
                        Log.d(LOG_TAG, "Bullet collision");
                        score(player);
                        layout.removeView(bulletImg);
                        CollisionSystem.getInstance().removeCollider(bulletC);
                    }
                }
            }, delay);
        }
    }

    void score(Tank player){
        TextView scoreT;
        if(player.getNPlayer()==1)
            scoreT = findViewById(R.id.scoreP1);
        else
            scoreT = findViewById(R.id.scoreP2);
        int score = Integer.parseInt(scoreT.getText().toString());
        score++;
        scoreT.setText(String.valueOf(score));
        if(score == GAME_MAX_SCORE){
            if(player.getNPlayer()==1)
                maxScoreReached(1);
            else
                maxScoreReached(2);
        }
    }

    void maxScoreReached(int player){
        pauseControls();
        if(player==1){
            findViewById(R.id.winnerP1).setVisibility(View.VISIBLE);
            findViewById(R.id.loserP2).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.winnerP2).setVisibility(View.VISIBLE);
            findViewById(R.id.loserP1).setVisibility(View.VISIBLE);
        }

        countdownTextView.setTextSize(100);
        countdownTextView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.GONE);
        startTime = System.currentTimeMillis();
        final Handler finishGameHandler = new Handler();
        Runnable finishGameRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = GAME_INITIAL_COUNTDOWN_MILLIS - (System.currentTimeMillis() - startTime);
                if(millis<0) {
                    Intent intent = new Intent(LocalGameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    pauseControls();
                    int seconds = (int) (millis/1000);
                    countdownTextView.setText(String.format("%d", seconds));
                    finishGameHandler.postDelayed(this, 500);
                }
            }
        };
        countdownHandler.postDelayed(finishGameRunnable, 0);
    }
}