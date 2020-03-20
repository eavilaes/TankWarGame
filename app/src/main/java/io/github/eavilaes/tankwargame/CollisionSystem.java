package io.github.eavilaes.tankwargame;

import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

class CollisionSystem {

    private static List<Collider> colliders;
    private static CollisionSystem instance = null;

    private CollisionSystem(){
        colliders = new LinkedList<Collider>();
    }

    static CollisionSystem getInstance(){
        if(instance==null)
            instance = new CollisionSystem();
        return instance;
    }

    void addCollider(Collider c){
        if(!colliders.contains(c)){
            colliders.add(c);
        }
    }

    void removeCollider(Collider c){
        try{
            colliders.remove(c);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Returns true if the bullet collides with an enemy tank, false in any other case.
    boolean checkBulletCollisions(Bullet b, float newX, float newY){
        float oldX = b.getPosX();
        float oldY = b.getPosY();
        b.setPosX(newX);
        b.setPosY(newY);

        Rect rcB = new Rect();
        b.setHitRect(rcB);
        for (Collider c : colliders){
            if (c instanceof Tank && !b.cantCollide(c)){
                Rect rcT = new Rect();
                c.setHitRect(rcT);
                if(Rect.intersects(rcB, rcT)){
                    b.setPosX(oldX);
                    b.setPosY(oldY);
                    return true;
                }
            }
        }
        b.setPosX(oldX);
        b.setPosY(oldY);
        return false;
    }

    boolean checkCollisions(Collider c, float newX, float newY){
        if(!c.getRestrictsMovement()) //TODO change the collision system if the object can not stop the movement
            return false;
        float oldX = c.getPosX();
        float oldY = c.getPosY();
        c.setPosX(newX);
        c.setPosY(newY);

        Rect rc1 = new Rect();
        c.setHitRect(rc1);
        for (Collider x :colliders){
            if(c!=x && !c.cantCollide(x)){
                Rect rc2 = new Rect();
                x.setHitRect(rc2);
                if(Rect.intersects(rc1, rc2)) {
                    c.setPosX(oldX);
                    c.setPosY(oldY);
                    return true;
                }
            }
        }
        c.setPosX(oldX);
        c.setPosY(oldY);
        return false;
    }

    void fixTankRotationCollision(Collider c){
        Rect rcT = new Rect();
        c.setHitRect(rcT);
        for (Collider w :colliders){
            if(w instanceof Wall){
                Rect rcW = new Rect();
                w.setHitRect(rcW);
                if(Rect.intersects(rcT, rcW)){ //After the rotation, the tank collides with a wall
                    if((rcW.right-rcW.left)<(rcW.bottom-rcW.top) && rcT.left<rcW.right && rcW.left<rcT.right) //collision on a side of the tank
                        if(rcT.right>rcW.right) c.setPosX(rcW.right+((rcT.right-rcT.left)/2+1));  //collision on the left side of the tank
                        else c.setPosX(rcW.left-((rcT.right- rcT.left)/2+1)); //collision on the right side of the tank
                    if((rcW.right-rcW.left)>(rcW.bottom-rcW.top) && rcT.top<rcW.bottom && rcW.top<rcT.bottom) //collision on the top/bot of the tank
                        if(rcT.bottom>rcW.bottom) c.setPosY(rcW.bottom+((rcT.bottom-rcT.top)/2+1));  //collision on the top side of the tank
                        else c.setPosY(rcW.top-((rcT.bottom-rcT.top)/2+1));
                }
            }
        }
    }
}
