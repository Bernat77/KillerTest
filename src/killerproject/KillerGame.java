/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author berna
 */
public class KillerGame extends JFrame {

    //Objetos a pintar + Canvas
    private ArrayList<VisibleObject> objects = new ArrayList<>();
    private Viewer viewer;

    //Comunicaciones
    private KillerServer server;
    private int SERVERPORT;
    //
    private VisualHandler nk = new VisualHandler(this, true);
    private VisualHandler pk = new VisualHandler(this, false);

    //Gamepad (Móvil)
    private ArrayList<KillerPad> kpads = new ArrayList();

    public KillerGame() {
        portFrame();
    }

    public static void main(String[] args) {
        new KillerGame();
    }

    public void portFrame() {
        JFrame portframe = new JFrame("Killer Game: Set port");
        portframe.setSize(240, 150);
        Container cp = portframe.getContentPane();
        portframe.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(3, 3, 3, 3);
        JLabel jl = new JLabel("Introduce puerto:");
        cp.add(jl, c);
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        JTextField text = new JTextField();
        cp.add(text, c);
        JButton butt = new JButton("OK");
        butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!text.getText().isEmpty()) {
                    try {
                        SERVERPORT = Integer.parseInt(text.getText());
                        startServer();
                        ipframe();
                        portframe.dispose();
                    } catch (Exception ex) {
                        text.setText("Nada de letras");
                    }
                }
            }

        });

        c.gridy = 2;
        cp.add(butt, c);

        portframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
        portframe.setResizable(false);
        portframe.setLocationRelativeTo(null);
        portframe.setVisible(true);
    }

    public void ipframe() {
        JFrame frame = new JFrame("Killer Game: Set IP's");
        frame.setSize(500, 100);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container cp = frame.getContentPane();
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 4, 4, 3);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        JLabel ip1 = new JLabel("IP Previous:");
        frame.add(ip1, c);
        c.gridx = 1;
        JLabel ip2 = new JLabel("PORT:");
        frame.add(ip2, c);
        c.gridx = 3;
        JLabel ip3 = new JLabel("IP Next:");
        frame.add(ip3, c);
        c.gridx = 4;
        JLabel ip4 = new JLabel("PORT:");
        frame.add(ip4, c);
        frame.setVisible(true);

        c.gridx = 0;
        c.gridy = 1;
        JTextField ipnext = new JTextField(10);
        frame.add(ipnext, c);
        c.gridx = 1;
        JTextField portnext = new JTextField(5);
        frame.add(portnext, c);

        JTextField ipprev = new JTextField(10);
        JTextField portprev = new JTextField(5);

        c.gridx = 2;
        c.insets = new Insets(3, 6, 6, 3);
        JButton start = new JButton("START");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nk.setIp(ipnext.getText());
                    nk.setOriginport(Integer.parseInt(portnext.getText()));
                    //
                    pk.setIp(ipprev.getText());
                    pk.setOriginport(Integer.parseInt(portprev.getText()));

                    // frame.dispose();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Nada de letras.");
                }
            }
        });

        start.setSize(100, 100);
        frame.add(start, c);

        c.insets = new Insets(3, 4, 4, 3);
        c.gridx = 3;
        frame.add(ipprev, c);
        c.gridx = 4;
        frame.add(portprev, c);

    }

    public void startServer() {
        server = new KillerServer(this, SERVERPORT);
        new Thread(server).start();
        nk.startClient();
        pk.startClient();

        frame();
        for (int i = 0; i < 1; i++) {
            objects.add(new Automata(this, Color.red));
        }

        // objects.add(new Controlled(this, Color.pink, 1));
        startThreads();
    }

    public void frame() {
        setSize(new Dimension(800, 740));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        getContentPane().add(viewer = new Viewer(this));
        viewer.images();
        //pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);

        new Thread(viewer).start();

    }

    public void startThreads() {
        for (int i = 0; i < objects.size(); i++) {
            new Thread((Alive) objects.get(i)).start();

        }

    }

    public void checkColision(Alive obj) {

        //colision con limites
        if ((obj.y + obj.dy) >= viewer.getHeight() - obj.HEIGHT || (obj.y + obj.dy) <= 0) {
            obj.dy *= -1;
        } else if ((obj.x + obj.dx) >= viewer.getWidth() - obj.WIDTH || (obj.x + obj.dx) <= 0) {
            obj.dx *= -1;
        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;

            if (objects.get(i) != obj) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.nextMove().intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collision(obj, objCol);
                }

            }

        }
    }

    public void checkColision(Shoot obj) {

        //colision con limites
        if (obj.getY() >= viewer.getHeight() || obj.getY() <= 0
                || obj.getX() >= viewer.getWidth() || obj.getX() <= 0) {
            obj.death();
        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;
            if (objects.get(i) != obj.getControlled()) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.hitbox.intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collisionShoot(obj, (Alive) objCol);
                    objects.remove(i);

                }

            }

        }
    }

    public Viewer getViewer() {
        return viewer;
    }

    public ArrayList<VisibleObject> getObjects() {
        return objects;
    }

    public VisualHandler getNk() {
        return nk;
    }

    public void setNk(VisualHandler nk) {
        this.nk = nk;
    }

    public VisualHandler getPk() {
        return pk;
    }

    public void setPk(VisualHandler pk) {
        this.pk = pk;
    }

    public KillerServer getServer() {
        return server;
    }

    public void setServer(KillerServer server) {
        this.server = server;
    }

    public int getSERVERPORT() {
        return SERVERPORT;
    }

    public void setSERVERPORT(int SERVERPORT) {
        this.SERVERPORT = SERVERPORT;
    }

    public ArrayList<KillerPad> getKpads() {
        return kpads;
    }

    public void setKpads(ArrayList<KillerPad> kpads) {
        this.kpads = kpads;
    }

}
