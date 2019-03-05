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
    KillerGame killergame;
    BufferedReader in;
    PrintWriter out;
    Controlled player;

    boolean death;

    public KillerPad(Socket sock, String ip, KillerGame killergame, String user) {
        this.sock = sock;
        this.ip = ip;
        this.killergame = killergame;
        player = new Controlled(killergame, Color.yellow, ip, user);
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
            sock.close();
            player.death();
            killergame.getObjects().remove(player);
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
                    if (line.trim().equals("pad:bye")) {
                        done = true;
                    } else {
                        request(line, killergame, ip, killergame.getIplocal());
                    }
                } else {
                    done = true;
                }

            } catch (IOException ex) {
                done = true;
            }
        }

    }

    public static void request(String msg, KillerGame kg, String ipShip, String ipOrig) {

        if (msg.substring(0, 3).equals("pad")) {

            Controlled player = null;

            for (int i = 0; i < kg.getObjects().size(); i++) {
                if (kg.getObjects().get(i) instanceof Controlled) {
                    Controlled temporal = (Controlled) kg.getObjects().get(i);
                    if (temporal.getIp().equals(ipShip)) {
                        player = temporal;
                        System.out.println("la tengo!");
                    }
                }
            }

            String line = msg.trim().split(":")[1];
            if (line.equals("shoot")) {
                if (player != null) {
                    player.shoot();
                } else {
                    kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
                }
            } else {
                if (player != null) {
                    player.setDirections(line);
                } else {
                    kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
                }
            }
        }
    }

    public void sendMessageToPad(String msg) {
        out.println(msg);
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
