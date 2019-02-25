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
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        } catch (IOException ex) {
            
        }

    }

}
