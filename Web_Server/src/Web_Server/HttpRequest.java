package Web_Server;

import java.util.*;
import java.net.*;
import java.io.*;

final class HttpRequest implements Runnable{
	//\r : carriage return
	//\n : feed line
	final static String CRLF = "\r\n";
	
	Socket socket;
	
	//Constructor
	public HttpRequest(Socket socket) throws Exception{
		this.socket = socket;
	}
	
	//implement run() method of the Runnable Interface
	public void run(){
		try{
			processRequest();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	private void processRequest() throws Exception{
		//Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		//Set up input stream filters
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		//Get the request line of the HTTP request message
		String requestLine = br.readLine();
		
		if (requestLine != null){
			//Extract the filename from the request line
			StringTokenizer tokens= new StringTokenizer(requestLine);
			System.out.println("request line: " + requestLine.toString());
			
			tokens.nextToken();
			
			//Skip over the method, which should be "GET"
			String fileName = tokens.nextToken();
			
			// Prepend a "." so that file request if within the current directory
			fileName = "."+fileName;
			
			//Open the requested file
			FileInputStream fis = null;
			boolean fileExists = true;
			
			try{
				fis = new FileInputStream(fileName);
			}catch(FileNotFoundException e){
				fileExists = false;
			}
			
			System.out.println("Incoming!!!");
			
			//make response message : status line (status code + phrase) + headers + body
			String statusLine = null;
			String contentTypeLine = null;
			String entityBody = null;
			
			if(fileExists){
				statusLine = "HTTP/1.0 200 OK" + CRLF;
				contentTypeLine = "Content-type: "+ contentType(fileName) + CRLF;
			}
			
			else{
				statusLine = "HTTP/1.0 404 Not found" + CRLF;
				contentTypeLine = "Content-type : text/html" + CRLF;
				entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
			}
			
			//send the status line first, 
			os.writeBytes(statusLine);
			
			//send the content type line ( kind of header)
			os.writeBytes(contentTypeLine);
			
			//sent a blank line (carriage return + feed line) 
			//to indicate the end of the header lines
			os.writeBytes(CRLF);
			
			//send the entity body if exists
			if(fileExists){
				sendBytes(fis, os);
			}
			else{
				os.writeBytes(entityBody);
			}
			
			
			//Close streams and socket
			if(fis != null){
				fis.close();
			}
		}
		
		os.close();
		br.close();
		socket.close();
	}
	
	
	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception{
		//construct a 1K buffer to hold bytes on their way to the socket
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		//copy requested file into the socket's output stream
		while((bytes = fis.read(buffer)) != -1){
			os.write(buffer, 0, bytes);
		}
	}
	
	
	//figure out which kind of content is from the fileName
	private static String contentType(String fileName){
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")){
			return "text/html";
		}
		
		if(fileName.endsWith(".gif") || fileName.endsWith(".GIF")){
			return "image/gif";
		}
		
		if(fileName.endsWith(".jpeg")){
			return "image/jpeg";
		}
		
		return "application/octet-stream";
	}

	
	
}











