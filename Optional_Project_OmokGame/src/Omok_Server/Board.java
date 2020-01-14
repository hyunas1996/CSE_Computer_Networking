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
