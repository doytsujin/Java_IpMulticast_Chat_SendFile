/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Admin
 */
public class Send {
    public static void main(String[] args) throws Exception {
    int mcPort = 12345;
    String mcIPStr = "230.1.1.1";
    DatagramSocket udpSocket = new DatagramSocket();

    InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
    byte[] msg = "Hello".getBytes();
    DatagramPacket packet = new DatagramPacket(msg, msg.length);
    packet.setAddress(mcIPAddress);
    packet.setPort(mcPort);
    udpSocket.send(packet);

    System.out.println("Sent a  multicast message.");
    System.out.println("Exiting application");
    udpSocket.close();
  }
}
