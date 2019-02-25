/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author berna
 */
public class KillerGame extends JFrame {

    //Objetos a pintar + Canvas
    private ArrayList<VisibleObject> objects = new ArrayList<>();
    private Viewer viewer;

    //Comunicaciones
    private KillerServer server;
    private KillerClient client;

    private VisualHandler nk;
    private VisualHandler pk;

    //Gamepad (Móvil)
    private KillerPad kpad;

    public KillerGame() {
        frame();
        for (int i = 0; i < 10; i++) {
            objects.add(new Automata(this, Color.red));
        }

        server = new KillerServer(this);

        // objects.add(new Controlled(this, Color.pink, 1));
        startThreads();

    }

    public static void main(String[] args) {
        new KillerGame();
    }

    public void frame() {
        setSize(new Dimension(800, 740));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        getContentPane().add(viewer = new Viewer(this));
        viewer.images();
        //pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);

        new Thread(viewer).start();

    }

    public void startThreads() {
        new Thread(server).start();
        for (int i = 0; i < objects.size(); i++) {
            new Thread((Alive) objects.get(i)).start();

        }

    }

    public void checkColision(Alive obj) {

        //colision con limites
        if ((obj.y + obj.dy) >= viewer.getHeight() - obj.HEIGHT || (obj.y + obj.dy) <= 0) {
            obj.dy *= -1;
        } else if ((obj.x + obj.dx) >= viewer.getWidth() - obj.WIDTH || (obj.x + obj.dx) <= 0) {
            obj.dx *= -1;
        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;

            if (objects.get(i) != obj) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.nextMove().intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collision(obj, objCol);
                }

            }

        }
    }

    public void checkColision(Controlled.Shoot obj) {

        //colision con limites
        if (obj.getY() >= viewer.getHeight() || obj.getY() <= 0
                || obj.getX() >= viewer.getWidth() || obj.getX() <= 0) {
            obj.death();
        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;
            if (objects.get(i) != obj.getControlled()) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.hitbox.intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collisionShoot(obj, (Alive) objCol);
                    objects.remove(i);

                }

            }

        }
    }

    public Viewer getViewer() {
        return viewer;
    }

    public ArrayList<VisibleObject> getObjects() {
        return objects;
    }

}
