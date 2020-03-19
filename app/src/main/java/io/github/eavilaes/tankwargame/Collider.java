package io.github.eavilaes.tankwargame;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

class Collider {

    private static int lastId=0;

    private int id;
    private ImageView imageView;
    private boolean restrictsMovement;
    private List<Collider> doesntAffect; //For bullets,

    Collider(boolean restrictsMovement, ImageView imageView){
        this.restrictsMovement=restrictsMovement;
        this.imageView=imageView;
        this.doesntAffect = new LinkedList<Collider>();
        lastId++;
        id=lastId;
        CollisionSystem.getInstance().addCollider(this);
    }

    Collider(boolean restrictsMovement){
        this.restrictsMovement=restrictsMovement;
        lastId++;
        id=lastId;
        CollisionSystem.getInstance().addCollider(this);
    }

    int getId(){
        return this.id;
    }

    float getHeight(){
        return this.imageView.getHeight();
    }

    float getWidth(){
        return this.imageView.getWidth();
    }

    boolean getRestrictsMovement(){
        return restrictsMovement;
    }

    void setImageView(ImageView imageView){
        this.imageView=imageView;
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
