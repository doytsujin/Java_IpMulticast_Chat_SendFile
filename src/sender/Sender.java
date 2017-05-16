/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sender;

import static controller.LibImage.bufferedImageToByteArray;
import static sender.ScreenSender.IP_ADDRESS;
import static sender.ScreenSender.PORT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author Admin
 */
public abstract class Sender {

    /* Flags and sizes */
    public static int HEADER_SIZE = 8;
    public static int MAX_PACKETS = 255;
    public static int SESSION_START = 128;
    public static int SESSION_END = 64;
    public static int DATAGRAM_MAX_SIZE = 65507 - HEADER_SIZE;
    public static int MAX_SESSION_NUMBER = 255;
    public static String OUTPUT_FORMAT = "jpg";
    public static String IP_ADDRESS = "224.0.0.251";

    public Sender() {

    }

    int sessionNumber = 0;

    public boolean send1(byte[] imageByteArray, String multicastAddress, int port) {

        int packets = (int) Math.ceil(imageByteArray.length / (float) DATAGRAM_MAX_SIZE);

        /* If image has more than MAX_PACKETS slices -> error */
        if (packets > MAX_PACKETS) {
            System.out.println("Image is too large to be transmitted!");
            return false;
        }

        /* Loop through slices */
        for (int i = 0; i <= packets; i++) {
            int flags = 0;
            flags = i == 0 ? flags | SESSION_START : flags;
            flags = (i + 1) * DATAGRAM_MAX_SIZE > imageByteArray.length ? flags | SESSION_END : flags;

            int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_MAX_SIZE : imageByteArray.length - i * DATAGRAM_MAX_SIZE;

            /* Set additional header */
            byte[] data = new byte[HEADER_SIZE + size];
            data[0] = (byte) flags;
            data[1] = (byte) sessionNumber;
            data[2] = (byte) packets;
            data[3] = (byte) (DATAGRAM_MAX_SIZE >> 8);
            data[4] = (byte) DATAGRAM_MAX_SIZE;
            data[5] = (byte) i;
            data[6] = (byte) (size >> 8);
            data[7] = (byte) size;

            /* Copy current slice to byte array */
            System.arraycopy(imageByteArray, i * DATAGRAM_MAX_SIZE, data, HEADER_SIZE, size);
            /* Send multicast packet */
            sendImage(data, multicastAddress, port);

            /* Leave loop if last slice has been sent */
            if ((flags & SESSION_END) == SESSION_END) {
                break;
            }
        }
        /* Increase session number */
        sessionNumber = sessionNumber < MAX_SESSION_NUMBER ? ++sessionNumber : 0;
        return true;
    }

    protected boolean sendImage(byte[] imageData, String multicastAddress,
            int port) {
        InetAddress ia;

        boolean ret = false;
        int ttl = 2;

        try {
            ia = InetAddress.getByName(multicastAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return ret;
        }

        MulticastSocket ms = null;

        try {
            ms = new MulticastSocket();
            ms.setTimeToLive(ttl);
            DatagramPacket dp = new DatagramPacket(imageData, imageData.length,
                    ia, port);
            System.out.println(imageData.length);
            dp.setLength(imageData.length);
            ms.send(dp);
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            if (ms != null) {
                ms.close();
            }
        }

        return ret;
    }
}
