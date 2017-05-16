/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Admin
 */
public class senderMulticast {

    public static void main(String[] args) throws Exception {
        int mcPort = 1235;
        String mcIPStr = "224.0.0.1";
        DatagramSocket udpSocket = new DatagramSocket();

        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
        byte[] msg = "224".getBytes();
        while (true) {
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setAddress(mcIPAddress);
            packet.setPort(mcPort);
            udpSocket.send(packet);
            Thread.sleep(1000);
        }

//        System.out.println("Sent a  multicast message.");
//        System.out.println("Exiting application");
//        udpSocket.close();
    }
}
