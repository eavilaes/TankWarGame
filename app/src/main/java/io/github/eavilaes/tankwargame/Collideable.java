package io.github.eavilaes.tankwargame;

import android.widget.ImageView;

public class Collideable extends Collider {

    Collideable(boolean restrictsMovement, ImageView imageView){
        super(restrictsMovement,imageView);
    }
}
