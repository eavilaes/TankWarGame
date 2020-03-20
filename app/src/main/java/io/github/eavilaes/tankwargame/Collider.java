package io.github.eavilaes.tankwargame;

import android.graphics.Rect;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

class Collider {

    private ImageView imageView;
    private boolean restrictsMovement;
    private List<Collider> doesntAffect; //For bullets,

    Collider(boolean restrictsMovement, ImageView imageView){
        this.restrictsMovement=restrictsMovement;
        this.imageView=imageView;
        this.doesntAffect = new LinkedList<Collider>();
        CollisionSystem.getInstance().addCollider(this);
    }

    Collider(boolean restrictsMovement){
        this.restrictsMovement=restrictsMovement;
        CollisionSystem.getInstance().addCollider(this);
    }

    boolean getRestrictsMovement(){
        return restrictsMovement;
    }

    void setRotation(int angle){
        this.imageView.setRotation(angle);
    }

    int getRotation(){
        return (int)this.imageView.getRotation();
    }

    void setPosX(float x){
        this.imageView.setX(x);
    }

    float getPosX(){
        return this.imageView.getX();
    }

    void setPosY(float y){
        this.imageView.setY(y);
    }

    float getPosY(){
        return this.imageView.getY();
    }

    void setHitRect(Rect rect){
        if(this.imageView!=null)
            this.imageView.getHitRect(rect);
    }

    void addDoesntAffect(Collider c){
        this.doesntAffect.add(c);
    }

    boolean cantCollide(Collider c){
        return this.doesntAffect.contains(c);
    }

}
