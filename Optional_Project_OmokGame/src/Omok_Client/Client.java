package Omok_Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Frame {

    //object of omok game board
    private Board board = new Board();

    Socket socket = null;
    //input stream
    private BufferedReader reader;
    //output stream
    private PrintWriter writer;

    //constructor
    public Client(String name){
        super(name);
        add(board);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent w){
                System.exit(0);
            }
        });
    }

    //connection
    private void connect() {
        try {
            socket = new Socket("127.0.0.1", 0516);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            board.setWriter(writer);
            board.stoneSelect();

            String msg;

            while ((msg = reader.readLine()) != null) {
                //when server puts a stone
                if (msg.startsWith("[STONE]")) {
                    msg = msg.substring(7);
                    //now it's client's turn
                    board.setEnable(true);
                    board.putServer(Integer.parseInt(msg.substring(0, msg.indexOf(","))), Integer.parseInt(msg.substring(msg.indexOf(",") + 1)));
                }

                //when server chooses stone's color
                else if (msg.startsWith("[COLOR]")) {
                    msg = msg.substring(7);
                    //set server's stone's color
                    board.setColor(Integer.parseInt(msg.substring(0, msg.indexOf(","))), Integer.parseInt(msg.substring(msg.indexOf(",") + 1)));
                }

                //server notices client's lose
                else if (msg.startsWith("[LOSE]")) {
                    msg = msg.substring(6);
                    JOptionPane.showMessageDialog(null, msg);
                    //clinet will choose stone's color
                    board.setInfo("Choose Stone");
                    board.reset();

                    //reprint screen of choosing stone's color
                    board.stoneSelect();
                }

                //server notices client's win
                else if (msg.startsWith("[WIN]")) {
                    msg = msg.substring(5);
                    JOptionPane.showMessageDialog(null, msg);
                    //server will choose stone's color
                    board.setInfo("Server Is Choosing Stone");
                    board.reset();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }

    //main method
    public static void main(String[] args) {
        Client client = new Client("Omok Game : Client");
        client.setBounds(500,50,650,670);
        client.setVisible(true);
        client.connect();
    }
}
