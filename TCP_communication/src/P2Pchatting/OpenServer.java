package P2Pchatting;

import java.io.*;
import java.net.*;

//class to start server thread
public class OpenServer extends Thread{
    int port;

    //constructor
    public OpenServer(int port) {
        this.port = port;
    }

    public void run(){
        try{
            ServerSocket serversocket = new ServerSocket(this.port);

            while(true){
                Socket socket = serversocket.accept();
                TCPServer server = new TCPServer(socket);
                server.start();
            }

        }catch (Exception e){
            System.out.println("Error in OpneServer");
            System.out.println(e);

        }
    }
}
