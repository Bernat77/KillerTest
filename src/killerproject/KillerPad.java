/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class KillerPad implements Runnable {

    Socket sock;
    String ip;
    int port;
    KillerGame killergame;
    BufferedReader in;
    PrintWriter out;

    public KillerPad(Socket sock, String ip, KillerGame killergame, String user, String color) {
        this.sock = sock;
        this.ip = ip;
        this.killergame = killergame;
        port = sock.getPort();
        Controlled player = new Controlled(killergame, Color.yellow, ip, user);
        killergame.getObjects().add(player);
        killergame.getKpads().add(this);
        new Thread(player).start();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            processClient(in, out);
            removeShip("death", killergame, ip, killergame.getIplocal());
            killergame.getKpads().remove(this);
            System.out.println(ip + " connection closed");

        } catch (IOException ex) {

        }

    }

    public void processClient(BufferedReader in, PrintWriter out) {
        boolean done = false;
        String line;
        while (!done) {
            try {
                line = in.readLine();
                if (line != null) {
                    if (line.trim().equals("bye")) {
                        done = true;
                    } else {
                        request(line, killergame, ip, killergame.getIplocal());
                    }
                } else {
                    done = true;
                }

            } catch (Exception ex) {
                done = true;
            }
        }

    }

    public static void request(String msg, KillerGame kg, String ipShip, String ipOrig) {

        System.out.println(msg);

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                if (temporal.getIp().equals(ipShip)) {
                    player = temporal;
                }
            }
        }

        if (msg.equals("replay")) {
            if (player != null) {
                player.setDeath(false);
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        }

        if (msg.equals("death")) {
            if (player != null) {
                player.setDeath(true);
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }

        }

        if (msg.equals("shoot")) {
            if (player != null) {
                if (!player.isDeath()) {
                    player.shoot();
                }
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        } else if (msg.equals("up") || msg.equals("down")
                || msg.equals("left") || msg.equals("right")
                || msg.equals("downright") || msg.equals("downleft")
                || msg.equals("upright") || msg.equals("upleft")
                || msg.equals("idle")) {
            if (player != null) {
                if (!player.isDeath()) {
                    player.setDirections(msg);
                }
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        }
    }

    public static void sendMessageToPad(String msg, KillerGame kg, String ipPad, String ipOrig) {

        KillerPad pad = null;

        for (int i = 0; i < kg.getKpads().size(); i++) {
            if (kg.getKpads().get(i) != null) {
                KillerPad temporal = kg.getKpads().get(i);
                if (temporal.ip.equals(ipPad)) {
                    pad = temporal;
                }
            }
        }

        if (pad != null) {
            pad.out.println(msg);
        } else {
            kg.getNk().sendMessage(kg.getNk().notifyPad(msg, ipPad), "r", ipOrig);
        }

    }

    public static void lifeShip(String msg, KillerGame kg, String ipShip, String ipOrig, boolean life) {

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                if (temporal.getIp().equals(ipShip)) {
                    player = temporal;
                }
            }
        }

        if (player != null) {
            player.setDeath(life);
            player.stop();
        } else {
            kg.getNk().notifyVisual(msg, ipShip);
        }
    }

    public static void removeShip(String msg, KillerGame kg, String ipShip, String ipOrig) {

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i).kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                if (temporal.getIp().equals(ipShip)) {
                    player = temporal;
                }
            }
        }

        if (player != null) {
            player.death();
            kg.getObjects().remove(player);
        } else {
            kg.getNk().notifyVisual(msg, ipShip);

        }
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true);
        } catch (IOException ex) {

        }
    }

}
