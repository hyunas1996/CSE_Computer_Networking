package Web_Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class WebClient {
    public static void main(String[] args) throws IOException {

        HttpRequest req = new HttpRequest();
        Image image = null;
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.println("Method Input:");
            String method = sc.next();

            if(method.equals("GET")) {
                System.out.println("URL INPUT:");
                String url=sc.next();
                System.out.println("url "+url);
                String test=req.getWebContentByGet(url, "UTF-8", 10000);
                System.out.println(test);
            }
            if (method.equals("POST")) {
                System.out.println("URL INPUT:");
                String url=sc.next();
                String answer = sc.next();
                String test = req.getWebContentByPost(url, answer,"UTF-8", 10000);
                System.out.println(test);
            }
            if (method.equals("GUI")){
                System.out.println("URL INPUT: ");
                String url = sc.next();
                URL url2 = new URL(url);
                image = ImageIO.read(url2);

                //GUI???
                JFrame frame = new JFrame();
                frame.setSize(300, 300);
                JLabel label = new JLabel(new ImageIcon(image));
                frame.add(label);
                frame.setVisible(true);
            }


        }
    }
}
