package io.github.eavilaes.tankwargame;

import android.graphics.Rect;
import android.view.View;

public class Wall extends Collider {

    private View view;

    Wall(View view){
        super(false);
        this.view = view;
    }

    void setHitRect(Rect rect){
        if(this.view!=null)
            this.view.getHitRect(rect);
    }
}
