package P2Pchatting;

import java.io.*;
import java.net.*;

//class to print the other party's message
public class TCPServer extends Thread {
    Socket socket;
    DataOutputStream dOutStream;

    public TCPServer(Socket socket) throws IOException{
        this.socket = socket;
    }

    public void run(){
        try{
            String message = "";

            BufferedReader dInStream3 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                message = dInStream3.readLine();
                System.out.println(message);
            }
        }catch(IOException e){
            System.out.println("Exception error in TCPServer");
        }
    }
}
