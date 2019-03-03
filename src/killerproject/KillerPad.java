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

    public KillerPad(Socket sock, String ip, KillerGame killergame, String user) {
        this.sock = sock;
        this.ip = ip;
        this.killergame = killergame;
        player = new Controlled(killergame, Color.yellow, ip, user);
        killergame.getObjects().add(player);
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
                System.out.println(line);
                if (line != null) {
                    if (line.trim().equals("pad:bye")) {
                        done = true;
                    } else {
                        request(line);
                    }
                } else {
                    done = true;
                }

            } catch (IOException ex) {

            }
        }

    }

    public void request(String msg) {
        if (msg.substring(0, 3).equals("pad")) {
            if (msg.trim().split(":")[1].equals("shoot")) {
                player.shoot();
            } else {
                player.setDirections(msg.split(":")[1]);
            }
        }
    }

}
