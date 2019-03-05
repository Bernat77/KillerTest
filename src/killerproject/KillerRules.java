/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

/**
 *
 * @author berna
 */
public class KillerRules {

    public static void collision(VisibleObject obj1, VisibleObject obj2) {
        if (obj1 instanceof Alive && obj2 instanceof Alive) {
            collisionAlive((Alive) obj1, (Alive) obj2);
        }
    }

    public static void collisionAlive(Alive obj1, Alive obj2) {

//        obj1.collision();
//        obj2.collision();

        if (obj1 instanceof Controlled) {
            ((Controlled) obj1).kill();
        } else {
            obj1.death();
        }

        if (obj2 instanceof Controlled) {
            ((Controlled) obj2).kill();
        } else {
            obj2.death();
        }

    }

    public static void collisionShoot(Shoot obj1, Alive obj2) {
        obj1.death();
        obj2.death();
    }

}
