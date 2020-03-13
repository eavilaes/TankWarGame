package io.github.eavilaes.tankwargame;

import android.util.Log;
import android.widget.ImageView;

public class Tank {

    private static final String LOG_TAG = "Tank";
    private static Tank singleton = null;

    static final float speedMultiplier = 0.075f; //Default: 0.075f
    private static ImageView tank_player;


    private Tank(){
        Log.d(LOG_TAG, "private Tank()");
    }

    public static Tank getInstance(){
        if (singleton==null)
            singleton = new Tank();
        return singleton;
    }

    static void setImageView(ImageView tank_player){
        Tank.tank_player = tank_player;
    }

    static ImageView getImageView(){
        return Tank.tank_player;
    }

    static void setRotation(int angle){
        Tank.tank_player.setRotation(angle);
    }

    int getRotation(){
        return (int)Tank.tank_player.getRotation();
    }

    static void setX(float x){
        Tank.tank_player.setX(x);
    }

    static float getX(){
        return Tank.tank_player.getX();
    }

    static void setY(float y){
        Tank.tank_player.setY(y);
    }

    static float getY(){
        return Tank.tank_player.getY();
    }
}
