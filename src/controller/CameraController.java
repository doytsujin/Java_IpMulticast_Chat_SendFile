/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author Admin
 */
public class CameraController extends Thread {

    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    private volatile boolean running = true;

    public void setRunning(boolean running) {
        this.running = running;
    }

    JPanel view;

    public CameraController(JPanel view) {
        this.view = view;
        webSource = new VideoCapture(0);
        running = true;
    }
    int count = 0;

    @Override
    public void run() {
        // synchronized (this) {
        while (running) {
            if (webSource.grab()) {
                try {
                    webSource.retrieve(frame);
                    Highgui.imencode(".bmp", frame, mem);
                    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                    BufferedImage buff = (BufferedImage) im;
                    Graphics g = view.getGraphics();
                    g.drawImage(buff, 0, 0, view.getWidth(), view.getWidth(),
                            0, 0, buff.getWidth(), buff.getHeight(), null);

                    //view.revalidate();
                    // System.out.println("im here" + count ++);
                } catch (Exception ex) {
                    System.out.println("Error");
                }
            }
        }
        webSource.release();
        //  }
    }
}
