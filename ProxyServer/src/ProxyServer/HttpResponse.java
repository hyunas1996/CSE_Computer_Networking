package ProxyServer;

import java.io.DataInputStream;
import java.io.IOException;

public class HttpResponse {
    final static String CRLF = "\r\n";
    final static int BUF_SIZE = 8192;
    final static int MAX_OBJECT_SIZE = 100000;

    String version;
    int status;

    String statusLine = "";
    String headers = "";

    byte[] body = new byte[MAX_OBJECT_SIZE];

    public HttpResponse(DataInputStream fromServer) {
        int length = -1;
        boolean gotStatusLine = false;

        try{
            String line = fromServer.readLine();
            while((line != null) && (line.length() != 0)) {
                if(!gotStatusLine) {
                    statusLine = line;
                    gotStatusLine = true;
                }
                else{
                    headers += line + CRLF;
                }

                if (line.startsWith("Content-Length") || line.startsWith("Content-length")) {
                    String[] tmp = line.split(" ");
                    length = Integer.parseInt(tmp[1]);
                }
                line = fromServer.readLine();
            }
        }catch (IOException e){
            System.out.println("Error reading headers from server : " + e);
            return;
        }

        try {
            int bytesRead = 0;
            byte buf[] = new byte[BUF_SIZE];
            boolean loop = false;

            if (length == -1) {
                loop = true;
                length = MAX_OBJECT_SIZE;
            }

            int index = 0;

            while (bytesRead < length || loop){
                int tam = BUF_SIZE;
                if(tam>length) tam = length;

                int res = fromServer.read(buf, 0, tam);
                index = index + res;

                if(res == -1){
                    break;
                }

                for (int i = 0; i< res && (i+bytesRead) < MAX_OBJECT_SIZE; i++){
                    body[i+bytesRead] = buf[i];
                }
                bytesRead += res;
            }
        } catch (IOException e) {
            System.out.println("Error reading response body : " + e);
            return;
        }
    }
}
