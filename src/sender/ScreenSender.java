/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sender;

import com.sun.image.codec.jpeg.ImageFormatException;
import static controller.LibImage.bufferedImageToByteArray;
import static controller.LibImage.shrink;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class ScreenSender extends Sender implements Runnable {

    /* Default parameters */
    public static double SCALING = 0.75;
    public static int SLEEP_MILLIS = 30;
    public static int PORT = 4444;
    public static boolean SHOW_MOUSEPOINTER = true;
    public volatile boolean isRunning = true;
    long timeStamp = 0;
    int pts = 60;

    public static BufferedImage getScreenshot() throws AWTException,
            ImageFormatException, IOException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);

        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);

        return image;
    }
    private JPanel view;

    public ScreenSender() {
    }

    public ScreenSender(JPanel view) {
        this.view = view;
    }

    public static void main(String[] args) {
        ScreenSender tmp = new ScreenSender();

        Thread thread = new Thread(tmp);
        thread.start();
    }
    int index = 0;
    long time;

    @Override
    public void run() {
        try {
            /* Continuously send images */
            while (isRunning) {
                if (System.currentTimeMillis() - timeStamp > 1000 / pts) {

                    time = System.currentTimeMillis();

                    BufferedImage image;
                    image = getScreenshot();

                    /* Draw mousepointer into image 
                    if (SHOW_MOUSEPOINTER) {
                        PointerInfo p = MouseInfo.getPointerInfo();
                        int mouseX = p.getLocation().x;
                        int mouseY = p.getLocation().y;

                        Graphics2D g2d = image.createGraphics();
                        g2d.setColor(Color.red);
                        Polygon polygon1 = new Polygon(new int[]{mouseX, mouseX + 10, mouseX, mouseX},
                                new int[]{mouseY, mouseY + 10, mouseY + 15, mouseY},
                                4);

                        Polygon polygon2 = new Polygon(new int[]{mouseX + 1, mouseX + 10 + 1, mouseX + 1, mouseX + 1},
                                new int[]{mouseY + 1, mouseY + 10 + 1, mouseY + 15 + 1, mouseY + 1},
                                4);
                        g2d.setColor(Color.black);
                        g2d.fill(polygon1);

                        g2d.setColor(Color.red);
                        g2d.fill(polygon2);
                        g2d.dispose();
                    }
                     */
 /* Scale image */
                    image = shrink(image, SCALING);
                    byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);
                    send1(imageByteArray, IP_ADDRESS, PORT);
                    if (view != null) {
                        Graphics g = view.getGraphics();
                        g.clearRect(0, 0, view.getWidth(), view.getHeight());
                        g.drawImage(image, 0, 0, view.getWidth(), view.getHeight(), null);
                        g.dispose();
                        //view.revalidate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
