/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class Viewer extends Canvas implements Runnable {

    private KillerGame killer;

    static final int WIDTH = 1830;
    static final int HEIGHT = 1030;

    private int fps = 60;
    private double averageFPS;
    private double target = 1000 / fps;

    private BufferedImage fondo;
    private BufferedImage frame;
    private Image offImg;
    public Graphics graph;

    public Viewer(KillerGame k) {
        killer = k;
        setSize(new Dimension(killer.getWidth(), killer.getHeight()));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();
      

    }

    public void run() {
        images();
        while (true) {
            update();
            try {
                Thread.sleep((long) target);
            } catch (InterruptedException ex) {

            }
        }

    }

    public void images() {
        //creamos una nueva imagen del tamaño del canvas
        setSize(new Dimension(killer.getWidth(), killer.getHeight()));
        offImg = createImage(getWidth(), getHeight());
        fondo = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    }

    public void update() {

        //cogemos los graficos de la imagen
        Graphics g2d = offImg.getGraphics();
        //asi pintamos el fondo
        g2d.drawImage(fondo, 0, 0, null);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(g2d);

        //pintamos la imagen en el canvas
        this.getGraphics().drawImage(offImg, 0, 0, null);
    }

    public void drawComponents(Graphics g) {

        for (int i = 0; i < killer.getObjects().size(); i++) {
            killer.getObjects().get(i).draw(g);
        }

    }

}
