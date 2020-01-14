package ProxyServer;

import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProxyCache {

    //추가 : proxy port#
    public static int port;
    //추가 : client socket
    public static ServerSocket socket;


    public static void main(String[] args) {
        int myPort = 0;

        try{
            myPort = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need port number as argument");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

        init(myPort);

        Socket client = null;

        while(true){
            try{
                client = socket.accept();
                handle(client);
            } catch (IOException e) {
                System.out.println("Error reading request from client : " + e);
                continue;
            }
        }

    }

    public static void init(int p) {
        //static 변수로 위에 선언
        port = p;

        try{
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating socket : " + e);
            System.exit(-1);
        }
    }

    public static void handle(Socket client){
        Socket server = null;
        HttpRequest request = null;
        HttpResponse response = null;

        try{
            BufferedReader fromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));
            request = new HttpRequest(fromClient);
        } catch (IOException e) {
            System.out.println("Error reading request from client : " + e);
            return;
        }

        try{
            //open socket and write request to socket
            server = new Socket(request.getHost(), request.getPort());
            DataOutputStream toServer = new DataOutputStream(server.getOutputStream());

            //이거 뭔지 잘 이해안감
            toServer.writeBytes(request.toString());
            toServer.flush();

        } catch (UnknownHostException e) {
            System.out.println("Unknow host : " + request.getHost());
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println("Error writing request to server : " + e);
            return;
        }

        try{
            DataInputStream fromServer = new DataInputStream(server.getInputStream());
            response = new HttpReponse(fromServer);
            DataOutputStream toCient = new DataOutputStream(client.getOutputStream());
            toCient.writeBytes(response.toString());
            toCient.writeBytes(response.body);
            toCient.flush();
            //write response to client first header, then body*/

            client.close();
            server.close();
        } catch (IOException e){
            System.out.println("Error writing response to client : " + e);
        }
    }
}
