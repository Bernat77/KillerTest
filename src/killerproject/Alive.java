/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author berna
 */
public abstract class Alive extends VisibleObject implements Runnable {

    double dx;
    double dy;
    double speed;

    Rectangle hitbox;
    Color color;

    boolean alive;
    
    long time;

    public abstract void move();

    public abstract void collision();
    
    public abstract Rectangle nextMove();

    public abstract void death();
}
