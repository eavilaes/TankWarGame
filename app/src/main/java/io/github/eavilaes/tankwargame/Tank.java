package io.github.eavilaes.tankwargame;

import android.util.Log;
import android.widget.ImageView;

class Tank {

    private static final String LOG_TAG = "Tank";
    private static Tank player = null;
    private static Tank player2 = null;

    static final float speedMultiplier = 0.1f; //Default: 0.1f
    private static ImageView tank_player;
    private static ImageView tank_player2;

    private Tank(){
        Log.d(LOG_TAG, "private Tank()");
    }

    static Tank getPlayer(){
        if (player ==null)
            player = new Tank();
        return player;
    }

    static Tank getPlayer2(){
        if (player2 ==null)
            player2 = new Tank();
        return player2;
    }

    void setPlayerImageView(ImageView tank_player){
        Tank.tank_player = tank_player;
    }

    void setPlayerRotation(int angle){
        Tank.tank_player.setRotation(angle);
    }

    int getPlayerRotation(){
        return (int)Tank.tank_player.getRotation();
    }

    void setPlayerX(float x){
        Tank.tank_player.setX(x);
    }

    float getPlayerX(){
        return Tank.tank_player.getX();
    }

    void setPlayerY(float y){
        Tank.tank_player.setY(y);
    }

    float getPlayerY(){
        return Tank.tank_player.getY();
    }

    void setPlayer2ImageView(ImageView tank_player2){
        Tank.tank_player2 = tank_player2;
    }

    void setPlayer2Rotation(int angle){
        Tank.tank_player2.setRotation(angle);
    }

    int getPlayer2Rotation(){
        return (int)Tank.tank_player2.getRotation();
    }

    void setPlayer2X(float x){
        Tank.tank_player2.setX(x);
    }

    float getPlayer2X(){
        return Tank.tank_player2.getX();
    }

    void setPlayer2Y(float y){
        Tank.tank_player2.setY(y);
    }

    float getPlayer2Y(){
        return Tank.tank_player2.getY();
    }
}
