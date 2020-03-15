package io.github.eavilaes.tankwargame;

import android.util.Log;
import android.widget.ImageView;

class Tank {

    private static final String LOG_TAG = "Tank";

    static final float speedMultiplier = 0.1f; //Default: 0.1f
    private ImageView tank_player;

    Tank(ImageView imageView){
        this.tank_player = imageView;
    }

    void setImageView(ImageView imageView){
        this.tank_player = imageView;
    }

    void setRotation(int angle){
        this.tank_player.setRotation(angle);
    }

    int getRotation(){
        return (int)this.tank_player.getRotation();
    }

    void setPosX(float x){
        this.tank_player.setX(x);
    }

    float getPosX(){
        return this.tank_player.getX();
    }

    void setPosY(float y){
        this.tank_player.setY(y);
    }

    float getPosY(){
        return this.tank_player.getY();
    }
}
