package io.github.eavilaes.tankwargame;

import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

public class CollisionSystem {

    private static List<Collider> colliders;
    private static CollisionSystem instance = null;

    private CollisionSystem(){
        colliders = new LinkedList<Collider>();
    }

    public static CollisionSystem getInstance(){
        if(instance==null)
            instance = new CollisionSystem();
        return instance;
    }

    boolean addCollider(Collider c){
        if(!colliders.contains(c)){
            colliders.add(c);
            return true;
        }
        return false;
    }

    void removeCollider(Collider c){
        try{
            colliders.remove(c);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Collider getCollider(int id){
        for (Collider c : colliders){
            if(c.getId()==id)
                return c;
        }
        return null;
    }

    boolean checkCollisions(int id){
        Collider c = getCollider(id);
        return checkCollisions(c);
    }

    boolean checkCollisions(Collider c){
        if(!c.getRestrictsMovement()) //TODO change the collision system if the object is not a wall
            return false;
        Rect rc1 = new Rect();
        c.setHitRect(rc1);
        for (Collider x :colliders){
            if(c!=x && !c.cantCollide(x)){
                Rect rc2 = new Rect();
                x.setHitRect(rc2);
                if(Rect.intersects(rc1, rc2))
                    return true;
            }
        }
        return false;
    }
}
