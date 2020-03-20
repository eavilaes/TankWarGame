package io.github.eavilaes.tankwargame;

import android.widget.ImageView;

class Bullet extends Collider {

    static final float bulletSpeedMultiplier = 50.0f;

    Bullet(boolean restrictsMovement, ImageView imageView){
        super(restrictsMovement,imageView);
    }
}
