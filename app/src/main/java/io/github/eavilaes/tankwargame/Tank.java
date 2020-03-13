package io.github.eavilaes.tankwargame;

import android.util.Log;
import android.widget.ImageView;

class Tank {

    private static final String LOG_TAG = "Tank";
    private static Tank singleton = null;

    static final float speedMultiplier = 0.1f; //Default: 0.1f
    private static ImageView tank_player;


    private Tank(){
        Log.d(LOG_TAG, "private Tank()");
    }

    static Tank getInstance(){
        if (singleton==null)
            singleton = new Tank();
        return singleton;
    }

    void setImageView(ImageView tank_player){
        Tank.tank_player = tank_player;
    }

    void setRotation(int angle){
        Tank.tank_player.setRotation(angle);
    }

    int ygetRotation(){
        return (int)Tank.tank_player.getRotation();
    }

    void setX(float x){
        Tank.tank_player.setX(x);
    }

    float getX(){
        return Tank.tank_player.getX();
    }

    void setY(float y){
        Tank.tank_player.setY(y);
    }

    float getY(){
        return Tank.tank_player.getY();
    }
}
