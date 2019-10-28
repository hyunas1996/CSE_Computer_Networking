package Web_Server;

import java.util.*;
import java.net.*;
import java.io.*;

public class WebServer {

	public static void main(String[] args) throws Exception{
		//setting my port number : 8888 
		ServerSocket serverSocket = new ServerSocket(8888);
	
		while (true){
		
			Socket connectSocket = serverSocket.accept();
			
			//contruct object to process the HTTP request message
			HttpRequest request = new HttpRequest(connectSocket);
			
			//create a new thread to process the request
			Thread thread = new Thread(request);
			thread.start();
		}
	}

}
