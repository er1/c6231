package io.github.er1.c6231.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Replicator {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        while (true) {
            int port = in.nextInt();
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress addr = InetAddress.getByName("localhost");

                DatagramPacket packet = new DatagramPacket(new byte[]{0}, 1, addr, port);
                socket.send(packet);

                byte[] buffer = new byte[1500];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

            } catch (UnknownHostException ex) {
            } catch (IOException ex) {
            }
        }
    }
}