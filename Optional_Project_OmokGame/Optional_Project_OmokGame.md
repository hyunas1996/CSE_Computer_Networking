### Otional Project Omok Game by 2016025514 서현아

#### 1. Expalnation

By using TCP socket programming, which is included in the first project, this project serves omok game.

Client and Server are connected through a TCP socket.

When Server and Client are connected successfully, the omok game is played.

Client can choose which color he will play. Then automatically server's color is choosen.

In turn, each palyer puts baduk stone, and each palyer's baduk stone appears on each other's screen through socket communication.

If five stones of the same color is placed in a row, like the rules of the game we are familiar with, the game ends, and the winner and loser are determined.

The loser can choose the color of the stone, after which the game will resume.

<br/>

#### 2. Source Code

1. Client의 오목판 class : omok boad screen for client

```java
package Omok_Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

public class Board extends Canvas {
    public static final int BLACK = 1, WHITE = -1;
    public static final int size = 30; //size of one room
    public static final int num = 20; //maximum number of stones can be put
    public static final int x = 30; //start position of x
    public static final int y = 30; //start position of y
    public static final int width = 30; //size of a stone - width
    public static final int height = 30; //size of a stone - height

    private int color; //stone color of client
    private int server_color; //stone color of server
    private String info = "choose color of stone : "; //print string
    private String str_color; //string to print stone color of client
    private boolean enable = false; //activation info of board
    private PrintWriter writer; //stream to deliver message to server
    private int Stone[][] = new int [num][num]; // store stone positions

    public Board(){
        this.setVisible(true);
        this.setBackground(new Color(200,200,100));
        //when client clicks
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //when the board is activated
               if(!enable)
                   return;
               //out of board boundary : right & left
               else if(e.getX() > x+size*(num-1) || e.getY() > y+size*(num-1))
                   return;
               //out of board boundary : up & down
               else if(e.getX() < (x-size/2) || e.getY() < (y-size/2))
                   return;
               //already a stone exists
               else if(Stone[(e.getX()-x+size/2)/size][(e.getY()-y+size/2)/size] != 0)
                   return;
               //put stone
               else
                   Stone[(e.getX()-x+size/2)/size][(e.getY()-y+size/2)/size] = color;

               //send position of client's stone to server
               writer.println("[STONE]" + (e.getX()-x+size/2)/size + "," + (e.getY() - y + size/2)/size);
               info = "client is putting stone.";
               repaint();

               enable = false;
            }
        });
    }

    //initialize board when the game ends
    void reset(){
        for(int i = 0; i<num; i++){
            for(int j = 0; j<num; j++)
                Stone[i][j] = 0;
        }

        //repaint = call paint method
        repaint();
        setEnable(false);
    }

    public void paint(Graphics g){
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        //print Info
        g.drawString(info, 30, 20);

        //draw line
        for(int i = 0; i<num; i++){
            //choose color of line
            g.setColor(Color.BLACK);
            //horizontal line
            g.drawLine(x, y + size*i, x + size*(num-1), y + size*i);
            //vertical line
            g.drawLine(x + size*i, y, x + size*i, y + size*(num-1));
        }

        //put stone
        for(int i = 0; i <num; i++){
            for(int j = 0; j<num; j++){
                //black stone
                if(Stone[i][j] == BLACK){
                    g.setColor(Color.BLACK);
                    g.fillOval((x-size/2) + i*size, (y-size/2) + j*size, width, height);
                }
                //white stone
                else if(Stone[i][j] == WHITE){
                    g.setColor(Color.WHITE);
                    g.fillOval((x-size/2) + i*size, (y-size/2) + j*size, width, height);
                }
            }
        }
    }

    //message for choosing stone color
    public void stoneSelect(){
        //choose black
        if(JOptionPane.showOptionDialog(this, "Black Or White?", "Choose color of stone", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Black", "White"}, "Black") ==0){
            setColor(BLACK, WHITE);
            writer.println("[COLOR]" + WHITE + "," + BLACK);
        }
        //choose white
        else{
            setColor(WHITE, BLACK);
            writer.println("[COLOR]" + BLACK + "," + WHITE);
        }
    }

    //change board's activation info
    public void setEnable(boolean enable){
        this.enable = enable;
    }

    //set stone's color
    public void setColor(int color, int server_color){
        //set client's stone color
        this.color= color;
        //set server's stone color
        this.server_color = server_color;

        //black stone : first attack
        if(color == BLACK){
            //activate board
            setEnable(true);
            info = "Stone Color : Black - first attack";
            str_color = "Black";
        }
        //white stone : second attack
        else{
            //don't activate board
            setEnable(false);
            info = "Stone Color : White - second attack";
            str_color = "White";
        }

        repaint();
    }

    //change Info to print out
    public void setInfo(String info){
        this.info = info;
    }

    //put server's stone
    public void putServer(int x, int y){
        Stone[x][y] = server_color;
        info = "My turn (" + str_color + ") - Put you stone";
        repaint();
    }

    //manage connection with server
    public void setWriter(PrintWriter writer){
        this.writer = writer;
    }

}
```

2. Client class : creates object of board. Marks client's playing and server's stone color and playing. Accepts server's game conclusion message and do proper actions by the message. 

```java
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
```

3. Server의 오목판 class : omok board screen for server

```java
package Omok_Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

public class Board extends Canvas {
    public static final int BLACK = 1, WHITE = -1;
    public static final int size = 30; //size of one room
    public static final int num = 20; //maximum number of stones can be put
    public static final int x = 30; //start position of x
    public static final int y = 30; //start position of y
    public static final int width = 30; //size of a stone - width
    public static final int height = 30; //size of a stone - height

    private int color; //stone color of server
    private int client_color; //stone color of client
    private String info = "waiting for connection"; //print string for stuatus
    private String str_color; //string to print server's stone color
    private boolean enable = false; //activation info of board
    private PrintWriter writer; //stream to deliver message to client
    private int Stone[][] = new int [num][num]; // store stones' positions
    
    public Board(){
        this.setVisible(true);
        this.setBackground(new Color(200,200,100));
        //click mouse
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //if board is inactive
                if(!enable)
                    return;

                //out of board boundary : right & left
                else if(e.getX() > x+size*(num-1) || e.getY() > y+size*(num-1))
                    return;

                //out of board boundary : up & down
                else if(e.getX() < (x-size/2) || e.getY() < (y-size/2))
                    return;

                //already a stone exists
                else if(Stone[(e.getX()-x+size/2)/size][(e.getY()-y+size/2)/size] != 0)
                    return;

                //put stone
                else
                    Stone[(e.getX()-x+size/2)/size][(e.getY()-y+size/2)/size] = color;

                //send stone's position to client
                writer.println("[STONE]" + (e.getX() - x + size/2)/size + "," + (e.getY() - y + size/2)/size);

                //check server's status >> game ends & server win
                if(check(color) == true){
                    repaint();
                    //print server's winning message
                    JOptionPane.showMessageDialog(null, "Server Win");
                    //send losing message to client
                    writer.println("[LOSE] You Lose");
                    //it's server's turn to choose stone
                    info = "Server Is Choosing Stone";
                    reset();
                    return;
                }
                
                info = "Server is putting stone";
                repaint();
                enable = false;
            }
        });
    }

    //checking win/lose method
    boolean check(int color){
        for(int i = 0; i<num-4; i++){
            for(int j = 0; j<num; j++){
                if(Stone[i][j] == color && Stone[i+1][j] == color && Stone[i+2][j] == color && Stone[i+3][j] == color && Stone[i+4][j] == color)
                    return true;
            }
        }

        for(int i = 0; i<num; i++){
            for(int j = 0; j<num-4; j++){
                if(Stone[i][j] == color && Stone[i][j+1]==color && Stone[i][j+2]==color && Stone[i][j+3] == color && Stone[i][j+4] == color)
                    return true;
            }
        }

        for(int i = 19; i>3; i--){
            for(int j = 0; j<num-4; j++){
                if(Stone[i][j] == color && Stone[i-1][j+1] == color && Stone[i-2][j+2] == color && Stone[i-3][j+3] == color && Stone[i-4][j+4] == color)
                    return true;
            }
        }

        for(int i = 0; i <num-4; i++){
            for(int j = 0; j<num-4; j++){
                if(Stone[i][j] == color && Stone[i+1][j+1] == color && Stone[i+2][j+2] == color && Stone[i+3][j+3] == color && Stone[i+4][j+4] == color)
                    return true;
            }
        }
        return false;
    }
    
    //when the game ends, reset the game
    void reset(){
        for(int i = 0; i<num; i++){
            for(int j = 0; j<num; j++){
                Stone[i][j] = 0;
            }
        }
        repaint();
        //make the board unactivated.
        setEnable(false);
    }

    public void paint(Graphics g){
        g.clearRect(0, 0, getWidth(), getHeight());

        //print info 
        g.setColor(Color.RED);
        g.drawString(info, 30, 20);

        //draw line
        for(int i = 0; i<num; i++){
            //choose color of line
            g.setColor(Color.BLACK);
            //horizontal line
            g.drawLine(x, y + size*i, x + size*(num-1), y + size*i);
            //vertical line
            g.drawLine(x + size*i, y, x + size*i, y + size*(num-1));
        }

        //put stone
        for(int i = 0; i <num; i++){
            for(int j = 0; j<num; j++){
                //black stone
                if(Stone[i][j] == BLACK){
                    g.setColor(Color.BLACK);
                    g.fillOval((x-size/2) + i*size, (y-size/2) + j*size, width, height);
                }
                //white stone
                else if(Stone[i][j] == WHITE){
                    g.setColor(Color.WHITE);
                    g.fillOval((x-size/2) + i*size, (y-size/2) + j*size, width, height);
                }
            }
        }
    }

    //message for choosing stone color
    public void stoneSelect(){
        if(JOptionPane.showOptionDialog(this, "Black Or White?", "Choose color of stone", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Black", "White"}, "Black") ==0){
            setColor(BLACK, WHITE);
            writer.println("[COLOR]" + WHITE + "," + BLACK);
        }
        else{
            setColor(WHITE, BLACK);
            writer.println("[COLOR]" + BLACK + "," + WHITE);
        }
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }

    public void setWriter(PrintWriter writer){
        this.writer = writer;
    }

    public void setColor(int color, int client_color){
        this.color= color;
        this.client_color = client_color;

        if(color == BLACK){
            setEnable(true);
            info = "Stone Color : Black - first attack";
            str_color = "Black";
        }
        else{
            setEnable(false);
            info = "Stone Color : White - second attack";
            str_color = "White";
        }

        repaint();
    }


    public void setInfo(String info){
        this.info = info;
    }

    //put client's stone
    public void putClient(int x, int y){
        Stone[x][y] = client_color;
        info = "My turn (" + str_color + ") - Put you stone";
        repaint();
        //check client's win/lose
        if(check(client_color) == true){
            repaint();
            JOptionPane.showMessageDialog(null, "Server Lose");
            writer.println("[WIN] Client Win");
            info = "Choose Stone";
            reset();
            stoneSelect();
        }

    }

}
```

4. Server class : creates object of board. Marks server's playing and clinet's stone color and playing. Accepts client's game conclusion message and do proper actions by the message. 

```java
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
```

#### 3. Result (playing game)

<img width="1257" alt="스크린샷 2019-12-04 오후 10 17 10" src="https://user-images.githubusercontent.com/45492242/70145604-dbd86280-16e3-11ea-8f2a-d3248491dbfb.png">

<br/>

<img width="1268" alt="스크린샷 2019-12-04 오후 9 53 49" src="https://user-images.githubusercontent.com/45492242/70145522-9caa1180-16e3-11ea-9535-7ac5b43bf827.png">

<br/>

<img width="1258" alt="스크린샷 2019-12-04 오후 9 53 59" src="https://user-images.githubusercontent.com/45492242/70145632-e98de800-16e3-11ea-987f-5d1d9065e061.png">