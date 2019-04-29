/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.transform.Affine;

/**
 *
 * @author berna
 */
public class Controlled extends Alive {

    String ip;
    String user;
    boolean up, down, right, left;
    boolean fright;
    boolean wup, wdown, wright, wleft;
    private ArrayList<Shoot> shoots = new ArrayList();
    private boolean death;
    double vx, vy, a, m, maxspeedX, maxspeedY, perX, perY, morroX, morroY, radians;

    private BufferedImage sprite;

    public Controlled(KillerGame kg, Color color, String ip, String user) {
        this.kg = kg;
        this.ip = ip;

        //aspecto visual
        HEIGHT = 100;
        WIDTH = 100;
        this.user = user;
        this.color = color;
        colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        //tamaño hitbox
        hitbox = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);

        //movimiento y posición
        dx = 0;
        dy = 0;
        maxspeed = 7;
        vx = 0;
        vy = 0;
        a = 0.1;
        m = 15;
        perX = 0;
        perY = 0;

        //posicion aleatoria en el espacio del canvas
        x = (int) (kg.getViewer().getWidth() / 2 * Math.random());
        y = (int) (kg.getViewer().getHeight() / 2 * Math.random());

        //estados
        fright = true;
        alive = true;
        death = false;

        wup = false;
        wdown = false;
        wleft = false;
        wright = false;
        //tiempo
        time = System.nanoTime();

        try {
            sprite = ImageIO.read(new File("src\\sprites\\ship.png"));
        } catch (IOException ex) {
            System.out.println(new File("src\\sprites\\ship.png").getAbsoluteFile());
        }

        sprite = getScaledImage(sprite, WIDTH, WIDTH);
        radians = 0;

    }

    private BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public void run() {

        while (alive) {
            move();
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {

            }

        }

    }

    public void checkMove() {
        //        if (up && !wup) {
        //            dy = -maxspeed;
        //            if (wdown) {
        //                wdown = false;
        //            }
        //        }
        //        if (down && !wdown) {
        //            dy = maxspeed;
        //
        //            if (wup) {
        //                wup = false;
        //            }
        //        }
        //        if (right && !wright) {
        //            dx = maxspeed;
        //            fright = true;
        //
        //            if (wleft) {
        //                wleft = false;
        //            }
        //        }
        //        if (left && !wleft) {
        //            dx = -maxspeed;
        //            fright = false;
        //
        //            if (wright) {
        //                wright = false;
        //            }
        //        }

    }

    @Override
    public void collision() {

    }

    public void death() {
        alive = false;
    }

    @Override
    public void draw(Graphics g) {
        if (!death) {

            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.WHITE);
            g.drawString(user, (int) x, (int) y - (HEIGHT / 3));
            g.setColor(color);
            g.fillOval((int) x, (int) y, HEIGHT, WIDTH);
            g2d.drawImage(sprite, alterateObject(), null);
            drawShoots(g);
        }
        // g.drawImage(,x, y,null);
    }

    public AffineTransform alterateObject() {

        AffineTransform rot = new AffineTransform();

        rot.translate(x, y);

        if (perX != 0 || perY != 0) {

            if (perX <= 0 && perY * -1 > 0) {
                radians = Math.atan(perX / perY);
                System.out.println(radians);
            } else if (perX <= 0 && perY * -1 <= 0) {
                radians = Math.atan(Math.abs(perY / perX)) + (Math.PI / 2);
                System.out.println(radians);
            } else if (perX > 0 && perY * -1 <= 0) {
                radians = Math.atan(Math.abs(perX / perY)) + Math.PI;;
            } else if (perX > 0 && perY * -1 > 0) {
                radians = Math.atan(Math.abs(perY / perX)) + (Math.PI * 1.5);
            }
        }
        rot.rotate(-radians, WIDTH / 2, HEIGHT / 2);
        return rot;
    }

    public void drawShoots(Graphics g) {
        if (!shoots.isEmpty()) {
            for (int i = 0; i < shoots.size(); i++) {
                Shoot current = shoots.get(i);
                g.setColor(current.getColor());
                g.fillOval((int) current.getX(), (int) current.getY(),
                        current.getRadius(), current.getRadius());
            }

        }
    }

    public void kill() {
        System.out.println(ip + " ha muerto.");
        KillerPad.lifeShip("death", kg, ip, kg.getIplocal());
        KillerPad.sendMessageToPad("ded", kg, ip, kg.getIplocal());
    }

    @Override
    public void move() {
        double timedif = (System.nanoTime() - time) / 10000000d;

        kg.checkColision(this);
        checkMove();
//        x += dx * timedif;
//        y += dy * timedif;

        maxspeedX = Math.abs(maxspeed * perX);
        maxspeedY = Math.abs(maxspeed * perY);

        if (vx < maxspeedX) {
            if (vx + a <= maxspeedX) {
                vx += a;
            }
        } else if (vx > maxspeedX && vx != 0) {
            vx -= a;
        }

        if (vy < maxspeedY) {
            if (vy + a <= maxspeedY) {
                vy += a;
            }
        } else if (vy > maxspeedY && vy != 0) {
            vy -= a;
        }

        if (radians >= Math.PI) {
            x += vx;
        } else {
            x -= vx;
        }

        if (radians <= Math.PI / 2 || radians >= Math.PI * 1.5) {
            y -= vy;
        } else {
            y += vy;
        }

        morroX = (x + WIDTH / 2) - (Math.sin(radians) * (HEIGHT / 2));
        morroY = (y + HEIGHT / 2) - (Math.cos(radians) * (HEIGHT / 2));

        dx = 0;
        dy = 0;

        updateHitBox();

        time = System.nanoTime();

    }

    public Rectangle nextMove() {
        return new Rectangle((int) (x + dx), (int) (y + dy), WIDTH, HEIGHT);
    }

    public void restart() {
        System.out.println(ip + "ha revivido.");
        KillerPad.lifeShip("replay", kg, ip, kg.getIplocal());
    }

    public void setDirections(String direction) {

        String[] dir = direction.trim().toLowerCase().split(",");

        perX = Double.parseDouble(dir[0]);
        perY = Double.parseDouble(dir[1]) * -1;
    }

    public void shoot() {
        Shoot fire = new Shoot(kg, color, radians, this);
        shoots.add(fire);
        new Thread(fire).start();
    }

    public void stop() {
        up = false;
        down = false;
        right = false;
        left = false;
    }

    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, WIDTH, HEIGHT);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Shoot> getShoots() {
        return shoots;
    }

    public void setShoots(ArrayList<Shoot> shoots) {
        this.shoots = shoots;
    }

    public boolean isWup() {
        return wup;
    }

    public void setWup(boolean wup) {
        this.wup = wup;
    }

    public boolean isWdown() {
        return wdown;
    }

    public void setWdown(boolean wdown) {
        this.wdown = wdown;
    }

    public boolean isWright() {
        return wright;
    }

    public void setWright(boolean wright) {
        this.wright = wright;
    }

    public boolean isWleft() {
        return wleft;
    }

    public void setWleft(boolean wleft) {
        this.wleft = wleft;
    }

    public boolean isDeath() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

}
