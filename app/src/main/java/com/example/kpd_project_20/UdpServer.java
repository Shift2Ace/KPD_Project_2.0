package com.example.kpd_project_20;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;



public class UdpServer {
    public static int UDP_SERVER_PORT = 11111;
    public static int MAX_UDP_DATAGRAM_LEN = 2048;

    public static void runUdpServer() {
        new Thread(new Runnable() {
            public void run() {
                String lText;
                byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
                DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket(UDP_SERVER_PORT);
                    //disable timeout for testing
                    //ds.setSoTimeout(100000);
                    while (true) {
                        ds.receive(dp);
                        lText = new String(lMsg,0, dp.getLength());
                        Log.i("UDP packet received", lText);
                        Log.d("running", "ok");
                        switch (lText) {
                            case ("connect_successfully"):
                                Log.d("ok", "ok");
                                return;
                        }
                        dp.setLength(lMsg.length);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
                Log.d("running","ok2");
            }
        }).start();
    }
}