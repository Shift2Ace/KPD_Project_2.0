package com.example.kpd_project_20;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;


import android.util.Log;



public class UdpServer {
    public static int UDP_SERVER_PORT = 11111;
    public static int MAX_UDP_DATAGRAM_LEN = 2048;
    public static boolean connect_state = false;
    private static String TAG = "UDP Server";
    public static byte[] byte_vector;

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

                        switch (lText) {
                            case ("UDP_OK"):
                                Log.i(TAG, "data receive : UDP_OK");
                                Log.d(TAG,"Connect Successfully");
                                connect_state = true;
                                ATL.ipAddress = udpService.addr.toString();
                                return;
                            default:
                                if (dp.getLength() == 1460 &&
                                        lMsg[0] == -1 &&
                                        lMsg[1] == -40 &&
                                        lMsg[2] == -1){
                                    byte_vector = null;
                                }
                                byte[] temp = Arrays.copyOf(lMsg,dp.getLength());
                                if (byte_vector != null) {
                                    byte_vector = combine(byte_vector, temp);
                                }else{
                                        byte_vector = temp;
                                }
                                if (dp.getLength() != 1460 &&
                                        lMsg[dp.getLength() - 2] == -1 &&
                                        lMsg[dp.getLength() - 1] == -39){
                                    MainActivity.imageWrite(byte_vector);
                                }
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
            }
        }).start();
    }

    private static byte[] combine(byte[] a, byte[] b){
        byte[] concatenatedArray = new byte[a.length + b.length];
        System.arraycopy(a, 0, concatenatedArray, 0, a.length);
        System.arraycopy(b, 0, concatenatedArray, a.length, b.length);
        return concatenatedArray;
    }

}