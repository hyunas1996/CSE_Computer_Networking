package P2Pchatting;

import java.io.*;
import java.net.*;

//chat window
public class ChatServer {

    public static void main(String[] args) throws Exception{
        //data input stream to read lines from chatter's keyboard
        BufferedReader dInStream = new BufferedReader(new InputStreamReader(System.in));

        //get name to use in chatting
        System.out.print("enter name : ");
        String name = dInStream.readLine();

        //my port number
        System.out.print("enter port : ");
        int port = Integer.parseInt(dInStream.readLine());

        //the other party's port number
        System.out.print("enter other port : ");
        int otherport = Integer.parseInt(dInStream.readLine());
        //if the port number matched each other
        //then the chat starts successfully

        //open server with my port number
        //then the other party will be connected by matching port number
        OpenServer openserver = new OpenServer(port);
        openserver.start(); //start function is allowed as OpenServer class extends Thread

        System.out.println("SERVER started successfully");

        //my local computer ip
        String ipAddr = "127.0.0.1";

        //open client's chat thread
        TCPClient client = new TCPClient(new InetSocketAddress(ipAddr, otherport), name);
        client.start();

        System.out.println("CLIENT started successfully");


    }

}
