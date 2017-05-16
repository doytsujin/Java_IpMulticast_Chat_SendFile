package controller;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class AudioController extends Thread{

    private volatile boolean myListening = false;

    public boolean isMyListening() {
        return myListening;
    }

    public void setMyListening(boolean myListening) {
        this.myListening = myListening;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    private volatile boolean running = true;

    TargetDataLine microphone;
    SourceDataLine speakers;

    public AudioController(boolean myListening) {
        this.myListening = myListening;
    }

    @Override
    public void run() {
        AudioFormat format = new AudioFormat(44000.0f, 16, 1, true, true);
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            while (running) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                // bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                //out.write(data, 0, numBytesRead);
                // write mic data to stream for immediate playback
                if(myListening)speakers.write(data, 0, numBytesRead);
            }
            speakers.drain();
            speakers.close();
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
