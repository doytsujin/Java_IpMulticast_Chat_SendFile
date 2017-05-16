/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sender;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import org.opencv.core.Core;

/**
 *
 * @author Admin
 */
public class VoiceSender extends Sender implements Runnable {

    /* Default parameters */
    public static double SCALING = 1;
    public static int SLEEP_MILLIS = 30;
    public static int PORT = 5555;
    public static boolean SHOW_MOUSEPOINTER = true;

    private volatile boolean myListening = false;
    public volatile boolean isRunging = true;
    static TargetDataLine microphone;
    static SourceDataLine speakers;
    AudioFormat format;

    public static final int TIMEOUT = 1000;

    public static final AudioFormat FORMAT = new AudioFormat(44100, 8, 1, true, true);
    public static final double BUFFER_TIME = 0.2;

    public static int getBufferSize(AudioFormat format, double bufferTime) {
        return (int) Math.ceil(bufferTime * format.getSampleRate() * format.getSampleSizeInBits() / 8);
    }

    public boolean isMyListening() {
        return myListening;
    }

    public void setMyListening(boolean myListening) {
        this.myListening = myListening;
    }

    public VoiceSender() {
        format = new AudioFormat(44000.0f, 16, 1, true, true);
        try {
            microphone = AudioSystem.getTargetDataLine(format);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (microphone == null) {
                microphone = (TargetDataLine) AudioSystem.getLine(info);
            }
            microphone.open(format);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            microphone.start();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // load native library of opencv     
        VoiceSender tmp = new VoiceSender();
        Thread thread = new Thread(tmp);
        thread.start();
    }

    public void stop() {
        speakers.drain();
        speakers.close();
        microphone.close();
    }

    @Override
    public void run() {
        while (isRunging) {

            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[getBufferSize(format, BUFFER_TIME)];
            numBytesRead = microphone.read(data, 0, data.length);

            send1(data, IP_ADDRESS, PORT);

            if (myListening) {
                speakers.write(data, 0, numBytesRead);
            }
        }
    }

}
