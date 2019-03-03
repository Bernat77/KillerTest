/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernat2
 */
public class VisualHandler implements Runnable {

    private Socket sock;
    private String ip;
    private int originport;
    private KillerGame killergame;
    private BufferedReader in;
    private PrintWriter out;
    private KillerClient kc;

    private boolean right;

    public VisualHandler(KillerGame kg, boolean right) {
        kc = new KillerClient(this);
        this.right = right;
        killergame = kg;

    }

    @Override
    public void run() {

    }

    public void startClient() {
        new Thread(this.kc).start();
    }

    public synchronized Socket getSock() {
        return sock;
    }

    public synchronized void setSock(Socket sock) {
        this.sock = sock;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException ex) {

        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public KillerGame getKillergame() {
        return killergame;
    }

    public void setKillergame(KillerGame killergame) {
        this.killergame = killergame;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public KillerClient getKc() {
        return kc;
    }

    public void setKc(KillerClient kc) {
        this.kc = kc;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public int getOriginport() {
        return originport;
    }

    public void setOriginport(int originport) {
        this.originport = originport;
    }

}
