package com.spaceagencies.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.spaceagencies.common.tools.Log;

public class ServerBroadcaster {

    private OnServerDetectedListener mListener;
    private boolean mRunning;


    public void setOnServerDetectedListener(OnServerDetectedListener listener) {
        mListener = listener;
    }

    public void start() {
        mRunning = true;
         new ServerDetectorThread().start();
    }
    
    public void stop() {
        mRunning = false;
    }
    
    
    private class ServerDetectorThread extends Thread {
        @Override
        public void run() {
            try {
                
                DatagramSocket serverSocket = new DatagramSocket (23536);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
                while(mRunning) {
//                    try {
//                        String ping = "I'm here !";
//                        
//                        byte[] bytes = ping.getBytes();
//                        DatagramPacket packet = new DatagramPacket (bytes, bytes.length);
//                        socket.send (packet);
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    
//                    Log.log("Packet Send !");
//                    
//                    if(!mRunning) {
//                        break;
//                    }
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                    
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String sentence = new String( receivePacket.getData());
                    System.out.println("RECEIVED: " + sentence);
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    String capitalizedSentence = sentence.toUpperCase();
                    sendData = capitalizedSentence.getBytes();
                    DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    
                }
                
                
                serverSocket.close ();
//                byte[] data = packet.getData ();
//                String time=new String(data);  // convert byte array data into string
//                System.out.println(time);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public interface OnServerDetectedListener {
        void onServerDetected();
    }
}
