package io.github.eavilaes.tankwargame;

import android.widget.ImageView;

public class Bullet extends Collider {

    public static final float bulletSpeedMultiplier = 50.0f;

    Bullet(boolean restrictsMovement, ImageView imageView){
        super(restrictsMovement,imageView);
    }
}
