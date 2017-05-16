/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public abstract class Receive {

    boolean debug = true;
    /* Flags and sizes */
    public static int HEADER_SIZE = 8;
    public static int SESSION_START = 128;
    public static int SESSION_END = 64;

    /*
	 * The absolute maximum datagram packet size is 65507, The maximum IP packet
	 * size of 65535 minus 20 bytes for the IP header and 8 bytes for the UDP
	 * header.
     */
    private static int DATAGRAM_MAX_SIZE = 65507;

    
    InetAddress ia = null;
    MulticastSocket ms = null;

    int currentSession = -1;
    int slicesStored = 0;
    int[] slicesCol = null;
    byte[] imageData = null;
    boolean sessionAvailable = false;

    /* Setup byte array to store data received */
    byte[] buffer = new byte[DATAGRAM_MAX_SIZE];

    public Receive(String multicastAddress, int port) throws IOException {
        /* Get address */
        ia = InetAddress.getByName(multicastAddress);

        /* Setup socket and join group */
        ms = new MulticastSocket(port);
        ms.joinGroup(ia);
    }

    public byte[] receiveData() {
        try {
            /* Receive a UDP packet */
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ms.receive(dp);
            byte[] data = dp.getData();

            /* Read header infomation */
            short session = (short) (data[1] & 0xff);
            short slices = (short) (data[2] & 0xff);
            int maxPacketSize = (int) ((data[3] & 0xff) << 8 | (data[4] & 0xff)); // mask
            // the
            // sign
            // bit
            short slice = (short) (data[5] & 0xff);
            int size = (int) ((data[6] & 0xff) << 8 | (data[7] & 0xff)); // mask
            // the
            // sign
            // bit

            if (debug) {
                System.out.println("------------- PACKET -------------");
                System.out.println("SESSION_START = "
                        + ((data[0] & SESSION_START) == SESSION_START));
                System.out.println("SSESSION_END = "
                        + ((data[0] & SESSION_END) == SESSION_END));
                System.out.println("SESSION NR = " + session);
                System.out.println("SLICES = " + slices);
                System.out.println("MAX PACKET SIZE = " + maxPacketSize);
                System.out.println("SLICE NR = " + slice);
                System.out.println("SIZE = " + size);
                System.out.println("------------- PACKET -------------\n");
            }

            /* If SESSION_START falg is set, setup start values */
            if ((data[0] & SESSION_START) == SESSION_START) {
                if (session != currentSession) {
                    currentSession = session;
                    slicesStored = 0;
                    /* Consturct a appropreately sized byte array */
                    imageData = new byte[slices * maxPacketSize];
                    slicesCol = new int[slices];
                    sessionAvailable = true;
                }
            }

            /* If package belogs to current session */
            if (sessionAvailable && session == currentSession) {
                if (slicesCol != null && slicesCol[slice] == 0) {
                    slicesCol[slice] = 1;
                    System.arraycopy(data, HEADER_SIZE, imageData, slice
                            * maxPacketSize, size);
                    slicesStored++;
                }
            }

            /* If image is complete dispay it */
            if (slicesStored == slices) {
                
                return imageData;

            }
        } catch (IOException ex) {
            Logger.getLogger(ScreenReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
