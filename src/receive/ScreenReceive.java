/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receive;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class ScreenReceive extends Receive implements Runnable {

    private JPanel view;
    private int pts =0;

    public void setPts(int pts) {
        this.pts = pts;
    }

    public void setMB(double MB) {
        this.MB = MB;
    }

    public int getPts() {
        return pts;
    }
    private double MB =0;

    public double getMB() {
        return MB;
    }
    public ScreenReceive(String multicastAddress, int port) throws IOException {
        super(multicastAddress, port);
    }
    public ScreenReceive(int pts,double MB,JPanel view,String multicastAddress, int port) throws IOException{
        super(multicastAddress, port);
        this.view = view;
        this.pts = pts;
        this.MB = MB;
    }

    
    @Override
    public void run() {
        /* Receiving loop */
        while (true) {
            try {
                byte[] imageData = receiveData();
                if(imageData == null) continue;
                ByteArrayInputStream bis = new ByteArrayInputStream(
                        imageData);
                BufferedImage image = ImageIO.read(bis);
                pts++;
                MB += imageData.length;
                Graphics g = view.getGraphics();
                g.clearRect(0, 0, view.getWidth(), view.getHeight());
                g.drawImage(image, 0, 0, view.getWidth(),view.getHeight(),null);
                g.dispose();
                //view.revalidate();
            } catch (IOException ex) {
                Logger.getLogger(ScreenReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
