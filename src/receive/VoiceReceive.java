/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import sender.VoiceSender;

/**
 *
 * @author Admin
 */
public class VoiceReceive extends Receive implements Runnable {

    InetAddress ia = null;
    MulticastSocket ms = null;
    SourceDataLine speakers;
    /* Setup byte array to store data received */
    byte[] buffer;// = new byte[8900];
    AudioFormat format;
    private double MB;

    public VoiceReceive(double MB, String multicastAddress, int port) throws IOException {
        super(multicastAddress, port);
        this.MB = MB;
        try {
            format = new AudioFormat(44000.0f, 16, 1, true, true);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            buffer = new byte[VoiceSender.getBufferSize(format, VoiceSender.BUFFER_TIME)];
            speakers.open(format, buffer.length);
            speakers.start();
            /* Get address */
            ia = InetAddress.getByName(multicastAddress);

            /* Setup socket and join group */
            ms = new MulticastSocket(port);
            ms.joinGroup(ia);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(VoiceReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        /* Receiving loop */
        while (true) {

            try {
                /* Receive a UDP packet */
                byte[] data = receiveData();
                if (data == null) {
                    continue;
                }
                this.MB += data.length;
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                AudioInputStream ais = new AudioInputStream(bais, format, buffer.length);
                int bytesRead = 0;
                if ((bytesRead = ais.read(data)) != -1) {
                    System.out.println("Writing to audio output.");
                    speakers.write(data, 0, bytesRead);

                    //                 bais.reset();
                }
                ais.close();
                bais.close();
                System.out.println("Có data");
                //speakers.write(data, 0, data.length);
            } catch (IOException ex) {
                Logger.getLogger(VoiceReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop() {
        speakers.drain();
        speakers.close();
    }

    public static void main(String[] args) throws IOException, LineUnavailableException {
        /* Default values */
        String IP_ADDRESS = "225.4.5.6";
        int PORT = 5555;
        VoiceReceive tmp = new VoiceReceive(0, IP_ADDRESS, PORT);
        Thread thread = new Thread(tmp);
        thread.start();
    }
}
