/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class ScreenController extends Thread {

    public byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
        encoder.encode(image);
        return baos.toByteArray();
    }

    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }
        return null;
    }

    public static byte[] convertToJPEG(BufferedImage buffi) {
//        ImageWriter writer;
//        ImageWriteParam param;
//        ByteArrayOutputStream os;
//        writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg")
//                .next();
//        param = writer.getDefaultWriteParam();
//        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//        param.setCompressionQuality(.75f);
//        writer.setOutput(ImageIO.createImageOutputStream(os));
//        try {
//            writer.write(null, new IIOImage(image, null, null), param);
//        } catch (IOException ex) {
//            Logger.getLogger(captureScreen.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        writer.dispose();
//        

        try {
//            BufferedImage buffi = ImageIO.read(new File(filename));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(.75f);

            writer.setOutput(ios);
            writer.write(null, new IIOImage(buffi, null, null), param);

            byte[] data = baos.toByteArray();

            writer.dispose();
            System.out.println(data.length);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    static DatagramSocket udpSocket;

    public static void send(byte[] msg) throws SocketException, IOException {
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        if (udpSocket == null) {
            udpSocket = new DatagramSocket();
        }

        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
        DatagramPacket packet = new DatagramPacket(msg, msg.length);
        packet.setAddress(mcIPAddress);
        packet.setPort(mcPort);
        udpSocket.send(packet);

        System.out.println("Sent a  multicast message.");
        System.out.println("Exiting application");
        //udpSocket.close();
    }

    Robot robot;
    Timer timer;
    private volatile boolean running = false;
    BufferedImage capture;
    BufferedImage scaledImage;
    Rectangle screenSize;
    Graphics2D graphics2D;
    Graphics graphicsView;
    JPanel view;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (!running) {
            timer.stop();
        }
    }

    public ScreenController(JPanel view) {
        try {
            this.view = view;
            robot = new Robot();
            screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            graphicsView = view.getGraphics();
        } catch (AWTException ex) {
            Logger.getLogger(ScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        timer = new Timer(1000 / 10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("running...");
                capture = robot.createScreenCapture(screenSize);
                scaledImage = new BufferedImage(
                        capture.getWidth() * 720 / 1080,
                        capture.getHeight() * 720 / 1080,
                        BufferedImage.TYPE_INT_RGB);

                graphics2D = scaledImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.drawImage(capture, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
                graphics2D.dispose();
                graphicsView = view.getGraphics();
//            send(toByteArray(scaledImage));
                //graphicsView.clearRect(0, 0, view.getWidth(), view.getHeight());
                graphicsView.drawImage(scaledImage,
                        0, 0,
                        view.getWidth(), view.getHeight(), null);
                //graphicsView.drawImage(capture, 0, 0, view.getWidth(), view.getHeight(), null);
                graphicsView.dispose();
            }

        });
        timer.start();
    }
}
