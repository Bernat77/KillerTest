/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class KillerServer implements Runnable {

    private KillerGame killergame;

    private static final int PORT = 8000;
    private Socket clientSock;
    private ServerSocket serverSocket;

    private BufferedReader in;
    private PrintWriter out;

    public KillerServer(KillerGame kg) {

        killergame = kg;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {

        }

    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("Waiting for a client......");
                contact();
            } catch (IOException ex) {

            }
        }

    }

    public void contact() throws IOException {

        clientSock = serverSocket.accept();
        System.out.println("Connection from " + clientSock.getInetAddress().getHostAddress());
        new Thread(new KillerPad(clientSock, clientSock.getInetAddress().getHostAddress(), killergame)).start();
        

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
        Controlled contr = null;

        if (msg.trim().equals("act")) {
             ArrayList<?> arr = this.killergame.getObjects();

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i) instanceof Controlled) {
                    contr = (Controlled) arr.get(i);
                }

            }
            contr.shoot();

        } else {
            ArrayList<?> arr = this.killergame.getObjects();

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i) instanceof Controlled) {
                    contr = (Controlled) arr.get(i);
                }

            }
            contr.setDirections(msg);
        }
    }

}
