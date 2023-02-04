package com.example.kpd_project_20;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udpService {
    public static int udpPort;
    public static InetAddress addr;
    private static DatagramSocket ds = null;

    public static void send(String message){
        byte[] bytes = message.getBytes();
        Log.d("Debug",addr.toString());
        Log.d("Debug", String.valueOf(udpPort));
        Thread thread = new Thread(new Runnable(){
            @Override public void run() {
                try {
                    ds = new DatagramSocket();
                    DatagramPacket packet = new DatagramPacket(bytes,bytes.length,addr,udpPort);
                    ds.send(packet);
                    Log.d("Message send",message);
                }
                catch (IOException e) {
                    Log.d("error",e.toString());
                }
            }
        });
        thread.start();
    }

}
