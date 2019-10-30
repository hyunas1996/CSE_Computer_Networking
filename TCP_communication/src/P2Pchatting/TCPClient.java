package P2Pchatting;


import java.io.*;
import java.net.*;

//class for client to enter his/her message
public class TCPClient extends Thread {

    String clientname;
    Socket clientSocket = new Socket();
    BufferedReader dInStream;
    DataOutputStream dOutStream;
    BufferedReader dInStream2;

    public TCPClient(SocketAddress socket, String name) {

        //if connection fails keep trying
        while(true){
            try{

                this.clientSocket.connect(socket, 100000);
                this.clientname = name;

                //if connection success move to run method (to get message)
                break;

            }catch(Exception e){
                //initialize client's socket
                clientSocket = new Socket();
            }
        }
    }


    public void run(){
        String message= "";

        try{
            dInStream = new BufferedReader(new InputStreamReader(System.in));
            dOutStream = new DataOutputStream(clientSocket.getOutputStream());
            dInStream2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){
                //when client enters message,
                System.out.print(">>> ");

                //read it
                message = dInStream.readLine();

                //and then print it with this form
                dOutStream.writeBytes(clientname + " : " + message + '\n');

                //but if client enters 'exit' string,
                //then end this chat
                if(message.equals("exit")){
                    System.exit(0);
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Error at TCPClient seding message");
        }
    }

}


