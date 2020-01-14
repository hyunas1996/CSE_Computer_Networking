package Omok_Server;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Frame {

    //object of omok game board
    private Board board = new Board();

    ServerSocket serverSocket = null;
    Socket socket = null;

    private BufferedReader reader;
    private PrintWriter writer;

    //contructor
    public Server(String name){
        super(name);
        add(board);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent w){
                System.exit(0);
            }
        });
    }

    //connection
    public void connect(){
        try{
            //put client's port num
            serverSocket = new ServerSocket(0516);

            socket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            board.setWriter(writer);
            board.setInfo("상대 Is Choosing Stone");

            String msg;

            while((msg = reader.readLine()) != null){
                //when server puts a stone
                if(msg.startsWith("[STONE")){
                    msg = msg.substring(7);
                    //it is server's turn
                    board.setEnable(true);
                    board.putClient(Integer.parseInt(msg.substring(0, msg.indexOf(","))), Integer.parseInt(msg.substring(msg.indexOf(",") +1)));
                }

                //when client chooses stone's color
                else if(msg.startsWith("[COLOR]")){
                    msg = msg.substring(7);
                    board.setColor(Integer.parseInt(msg.substring(0, msg.indexOf(","))), Integer.parseInt(msg.substring(msg.indexOf(",") +1)));
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                serverSocket.close();
                socket.close();
            }
            catch (Exception e) {}
        }
    }

    //main method
    public static void main(String[] args) {
        Server server = new Server("Omok Game : Server");
        server.setBounds(0,0,650,670);
        server.setVisible(true);
        server.connect();
    }
}
