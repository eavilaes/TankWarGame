package io.github.eavilaes.tankwargame;

import android.util.Log;
import android.widget.ImageView;

class Tank extends Collider {

    private static final String LOG_TAG = "Tank";

    static final float speedMultiplier = 0.1f; //Default: 0.1f

    Tank(ImageView imageView){
        super(true, imageView);
    }

}
