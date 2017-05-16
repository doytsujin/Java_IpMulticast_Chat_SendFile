
package sender;

import com.sun.image.codec.jpeg.ImageFormatException;
import static controller.LibImage.bufferedImageToByteArray;
import static controller.LibImage.shrink;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


public class CameraSender extends Sender implements  Runnable {

    /* Default parameters */
    public static double SCALING = 0.75;
    public static int PORT = 4444;
    public static boolean SHOW_MOUSEPOINTER = true;
    public volatile boolean isRunning = true;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();

    public BufferedImage getFrameCamera() throws AWTException,
            ImageFormatException, IOException {
        if (webSource.grab()) {
            try {
                webSource.retrieve(frame);
                Highgui.imencode(".bmp", frame, mem);
                Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                return (BufferedImage) im;
            } catch (Exception ex) {
                // System.out.println("Error");
                //ex.printStackTrace();
            }
        }
        return null;
    }
    private JPanel view;


    public CameraSender() {
        webSource = new VideoCapture(0);
    }

    public CameraSender(JPanel view) {
        this.view = view;
        webSource = new VideoCapture(0);
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // load native library of opencv     
        CameraSender tmp = new CameraSender();
        Thread thread = new Thread(tmp);
        thread.start();
    }

    @Override
    public void run() {
        try {
            /* Continuously send images */
            while (isRunning) {
                
                    BufferedImage image;
                    image = getFrameCamera();
                    if (image == null) {
                        continue;
                    }
                    /* Scale image */
                    image = shrink(image, SCALING);
                    byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);
                    send1(imageByteArray, IP_ADDRESS, PORT);
                    if (view != null) {
                        Graphics g = view.getGraphics();
                        g.clearRect(0, 0, view.getWidth(), view.getHeight());
                        g.drawImage(image, 0, 0, view.getWidth(), view.getHeight(), null);
                        g.dispose();

                    }
                           
            }
            webSource.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

