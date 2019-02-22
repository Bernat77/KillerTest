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

        obj1.collision();
        obj2.collision();

    }

    public static void collisionShoot(Controlled.Shoot obj1, Alive obj2) {
        obj1.death();
        obj2.death();
    }

}
