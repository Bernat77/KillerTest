/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernat2
 */
public class ConnectionHandler implements Runnable {

    private Socket socket;
    private String ip;
    private KillerGame kg;

    public ConnectionHandler(Socket socket, String ip, KillerGame kg) {
        this.socket = socket;
        this.ip = ip;
        this.kg = kg;

    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            String[] data = line.split("/");
            if (data[0].split(":")[1].equals("P")) {

//                KillerPad kp = null;
//                for (int i = 0; i < this.kg.getKpads().size(); i++) {
//                    if (this.kg.getKpads().get(i).ip.equals(ip)) {
//                        kp = this.kg.getKpads().get(i);
//                    }
//                }
//                if (kp != null) {
//                    kp.setSock(socket);
//                } else {
                new Thread(new KillerPad(socket, ip, kg, data[1].split(":")[1])).start();

            } else if (data[0].split(":")[1].equals("V")) {
                VisualHandler visual = null;
                if (data[1].equals("L")) {
                    visual = kg.getNk();
                } else if (data[1].equals("R")) {
                    visual = kg.getPk();
                }
                visual.setSock(socket);
                visual.setOriginport(Integer.parseInt(data[2]));
                System.out.println("setteado por server");
            }
        } catch (IOException ex) {

        }

    }

}
