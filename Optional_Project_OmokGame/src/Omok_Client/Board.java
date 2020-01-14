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
