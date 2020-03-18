package io.github.eavilaes.tankwargame;

import android.widget.ImageView;

class Tank extends Collider {

    static final float speedMultiplier = 0.1f; //Default: 0.1f
    private int player;
    private float firingCD;
    private long lastShot;


    Tank(ImageView imageView, int nPlayer){
        super(true, imageView);
        this.player=nPlayer;
        this.firingCD =0.5f;
        this.lastShot=-1;
    }

    int getNPlayer(){
        return this.player;
    }

    float getFiringCD(){
        return this.firingCD;
    }

    long getLastShot(){
        return this.lastShot;
    }

    void newShot(){
        this.lastShot = System.nanoTime();
    }
}
