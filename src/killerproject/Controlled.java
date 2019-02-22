/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author berna
 */
public class Controlled extends Alive {

    String ip;
    String user;
    boolean up, down, right, left;
    boolean fright;
    Shoot shoot;

    public Controlled(KillerGame kg, Color color, String ip) {
        this.kg = kg;
        this.ip = ip;

        //aspecto visual
        HEIGHT = 30;
        WIDTH = 30;

        this.color = color;

        hitbox = new Rectangle(x, y, WIDTH, HEIGHT);

        //movimiento y posición
        dx = 0;
        dy = 0;
        speed = 5.2;

        x = (int) (kg.getViewer().getWidth() / 2 * Math.random());
        y = (int) (kg.getViewer().getHeight() / 2 * Math.random());

        fright = true;

    }

    public void run() {

        while (true) {
            double ptime= System.nanoTime();
            move();
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {

            }

        }

    }

    @Override
    public void move() {

        checkMove();
        checkShoot();

        x += (int) dx;
        y += (int) dy;

        dx = 0;
        dy = 0;

        updateHitBox();
        kg.checkColision(this);

    }

    @Override
    public void collision() {

    }

    public void death() {

    }

    public Rectangle nextMove() {
        return new Rectangle(x + (int) dx, y + (int) dy, WIDTH, HEIGHT);
    }

    public void shoot() {
        shoot = new Shoot(x, y, (int) (Math.min(HEIGHT, WIDTH) / 2), fright);
    }

    public void checkShoot() {

        if (shoot != null) {
            shoot.move();
            kg.checkColision(shoot);
        }

    }

    public void drawShoot(Graphics g) {
        if (shoot != null) {
            g.setColor(Color.yellow);
            g.fillOval(shoot.x, shoot.y, shoot.radius, shoot.radius);
        }
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
        hitbox.setBounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, HEIGHT, WIDTH);
        drawShoot(g);
        // g.drawImage(,x, y,null);
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

    public class Shoot {

        private int x;
        private int y;
        private int radius;
        private double speed;
        private boolean frightdir;
        Rectangle hitbox;

        private Shoot(int x, int y, int radius, boolean frightdir) {
            this.x = x;
            this.y = y + (radius / 2);
            this.radius = radius;
            hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
            this.frightdir = frightdir;
            this.speed = 12;

        }

        private void move() {
            if (this.frightdir) {
                x += (int) speed;
            } else {
                x -= (int) speed;
            }
            updateHitBox();
        }

        public Controlled getControlled() {
            return Controlled.this;
        }

        public void death() {
            Controlled.this.shoot = null;
        }

        public void updateHitBox() {
            hitbox.setBounds(x, y, WIDTH, HEIGHT);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public boolean isFrightdir() {
            return frightdir;
        }

        public void setFrightdir(boolean frightdir) {
            this.frightdir = frightdir;
        }

        public Rectangle getHitbox() {
            return hitbox;
        }

        public void setHitbox(Rectangle hitbox) {
            this.hitbox = hitbox;
        }

    }

}
