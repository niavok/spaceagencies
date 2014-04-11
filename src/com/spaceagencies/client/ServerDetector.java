package com.spaceagencies.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.spaceagencies.common.tools.Log;

public class ServerDetector {

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
                InetAddress host;
            try {
//                    host = InetAddress.getByName("192.168.1.255");
//                
//                DatagramSocket socket = new DatagramSocket (null);
//                socket.setSoTimeout(1000);
//                
//                DatagramPacket packet = new DatagramPacket (new byte[100], 0,host, 23536);
//                while(mRunning) {
//                    try {
//                        socket.receive (packet);
//                    } catch (SocketTimeoutException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    
//                    Log.log("Packet received !");
//                    
//                    if(!mRunning) {
//                        break;
//                    }
//                }
//                
//                
//                socket.close ();
//                byte[] data = packet.getData ();
//                String time=new String(data);  // convert byte array data into string
//                System.out.println(time);
                
                     DatagramSocket clientSocket = new DatagramSocket();
                     InetAddress IPAddress = InetAddress.getByName("192.168.1.255");
                     byte[] sendData = new byte[1024];
                     byte[] receiveData = new byte[1024];
                     String sentence = "Is there someone ?";
                     sendData = sentence.getBytes();
                     DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 23536);
                     clientSocket.send(sendPacket);
                     DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                     clientSocket.receive(receivePacket);
                     String modifiedSentence = new String(receivePacket.getData());
                     System.out.println("FROM SERVER:" + modifiedSentence);
                     clientSocket.close();
                
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
