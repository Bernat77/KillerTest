/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author berna
 */
public class Controlled extends Alive {

    String ip;
    String user;
    boolean up, down, right, left;
    boolean fright;
    private ArrayList<Shoot> shoots = new ArrayList();

    public Controlled(KillerGame kg, Color color, String ip, String user) {
        this.kg = kg;
        this.ip = ip;

        //aspecto visual
        HEIGHT = 30;
        WIDTH = 30;
        this.user = user;

        this.color = color;

        hitbox = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);

        //movimiento y posición
        dx = 0;
        dy = 0;
        speed = 3.1;

        x = (int) (kg.getViewer().getWidth() / 2 * Math.random());
        y = (int) (kg.getViewer().getHeight() / 2 * Math.random());

        fright = true;

        time = System.nanoTime();

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

    @Override
    public void move() {
        double timedif = (System.nanoTime() - time) / 10000000d;

        kg.checkColision(this);
        checkMove();

        x += dx * timedif;
        y += dy * timedif;

        dx = 0;
        dy = 0;

        updateHitBox();

        time = System.nanoTime();

    }

    @Override
    public void collision() {

    }

    public void death() {
        alive = false;

    }

    public Rectangle nextMove() {
        return new Rectangle((int) (x + dx), (int) (y + dy), WIDTH, HEIGHT);
    }

    public void shoot() {
        Shoot fire = new Shoot(kg, color, this);
        shoots.add(fire);
        new Thread(fire).start();
    }

    public void checkMove() {
        if (up) {
            dy = -speed;
        }
        if (down) {
            dy = speed;
        }
        if (right) {
            dx = speed;
            fright = true;
        }
        if (left) {
            dx = -speed;
            fright = false;
        }
    }

    public void setDirections(String direction) {

        String dir = direction.trim().toLowerCase();

//        if (direction.trim().equalsIgnoreCase("up")) {
//            up = true;
//            down = false;
//            right = false;
//            left = false;
//
//        } else if (direction.trim().equalsIgnoreCase("upright")) {
//            up = true;
//            down = false;
//            right = true;
//            left = false;
//
//        } else if (direction.trim().equalsIgnoreCase("upleft")) {
//            up = true;
//            down = false;
//            right = false;
//            left = true;
//
//        } else if (direction.trim().equalsIgnoreCase("left")) {
//            up = false;
//            down = false;
//            right = false;
//            left = true;
//
//        } else if (direction.trim().equalsIgnoreCase("right")) {
//
//            up = false;
//            down = false;
//            right = true;
//            left = false;
//
//        } else if (direction.trim().equalsIgnoreCase("down")) {
//
//            up = false;
//            down = true;
//            right = false;
//            left = false;
//
//        } else if (direction.trim().equalsIgnoreCase("downleft")) {
//
//            up = false;
//            down = true;
//            right = false;
//            left = true;
//
//        } else if (direction.trim().equalsIgnoreCase("downright")) {
//
//            up = false;
//            down = true;
//            right = true;
//            left = false;
//
//        } else {
//            up = false;
//            down = false;
//            right = false;
//            left = false;
//        }
        switch (dir) {
            case "up":
                up = true;
                down = false;
                right = false;
                left = false;
                break;
            case "upright":
                up = true;
                down = false;
                right = true;
                left = false;
                break;
            case "upleft":
                up = true;
                down = false;
                right = false;
                left = true;
                break;
            case "left":
                up = false;
                down = false;
                right = false;
                left = true;
                break;
            case "right":
                up = false;
                down = false;
                right = true;
                left = false;
                break;
            case "down":
                up = false;
                down = true;
                right = false;
                left = false;
                break;
            case "downleft":
                up = false;
                down = true;
                right = false;
                left = true;
                break;
            case "downright":
                up = false;
                down = true;
                right = true;
                left = false;
                break;
            case "idle":
                up = false;
                down = false;
                right = false;
                left = false;
                break;

        }

    }

    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawString(user, (int) x, (int) y - (HEIGHT / 2));
        g.fillOval((int) x, (int) y, HEIGHT, WIDTH);
        drawShoots(g);
        // g.drawImage(,x, y,null);
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

}
