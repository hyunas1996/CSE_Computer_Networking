## Computer Network Project2 (2016025514 서현아)

### 1. Source Code

#### 1) HttpRequest Class 

Get method and Post method for Http communication are in this class. 

<br/>

* Get method : Request objects to the following URL in request line. 

  ```java
  				//URL 객체로 만들어줌
          URL url = new URL(urlString);
  
          //위의 url에 연결되는 class 객체를 만들어줌 : conn
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  
          //request method를 GET으로 설정 : GET - url에 해당하는 파일을 얻고 싶은 메소드
          conn.setRequestMethod("GET");
  
          //헤더라인 중 user-agent : client의 browser 종류
          conn.setRequestProperty("User-Agent", "2016025514/HYUNASEO/WebClient/COMPUTERNETWORK");
  
          //헤더라인 중 accept : 받아들일 data의 형식(?)
          conn.setRequestProperty("Accept", "text/html");
          conn.setConnectTimeout(timeout);
  ```

  Make URL object, HttpURLConnection object. 

  <img width="761" alt="스크린샷 2019-11-12 오후 3 09 11" src="https://user-images.githubusercontent.com/45492242/68646418-79fe6000-055e-11ea-848b-d24876655c56.png">

  As above picture explains, HTTP request message's first line notices which method is this. In line 8, setRequestMethod do this job with "GET" parameter.

  And line 11 means  ***<u>mission 1 : handle user-agent</u>*** by using setRequestProperty. This method is used for setting headerlines if request message. 

  ```java
  				//http 연결 통해서 들어오는 data를 읽어올 수 있음
          InputStream input = conn.getInputStream();
  
          BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
          String line = null;
          StringBuffer sb = new StringBuffer();
  
          while ((line = reader.readLine()) != null) {
              //carriage return과 feeding line 추가
              sb.append(line).append("\r\n");
          }
          if (reader != null) {
              reader.close();
          }
          if (conn != null) {
              conn.disconnect();
          }
          return sb.toString();
      }			
  ```

  By using getInputStream method, data can be read through http connection. 

  While there is data to be read, keep reading through while loop and readLine method. We should put "\r\n" at the end of the line. This means carriage return and feeding line as we learned in the class.

  <br/>

* Post Method : Request objects to the server, but in this case we can apply input which is included in the entity body of request message. 

  ```java
   				//URL Connection의 출력 스트림을 사용할지 여부를 결정 
          connection.setDoOutput(true);
          //URL Connection의 입력 스트림을 사용할지 여부를 결정 
          connection.setDoInput(true);
          
          
          connection.setRequestMethod("POST");
  
          //cache는 사용하지 않음 (질문 : 이부분이 proxy와 관련이 있을까요??) 
          connection.setUseCaches(false);
          
          
          connection.setInstanceFollowRedirects(true);
  
          //setRequestProperty method를 사용해서, Content-type을 text/xml으로 지정해준다 
          connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
          
          //헤더라인 중 user-agent : client의 browser 종류
          connection.setRequestProperty("User-Agent", "2016025514/HYUNASEO/WebClient/ComputerNetwork");
          connection.setRequestProperty("Accept", "text/xml");
          connection.setConnectTimeout(timeout);
  ```

  setDoOutput method and setDoInput method decide whether we use output/input stream or not. 

  Other things are almost same as the get method above.

  In here, I hava a question : setUseCaches method is relative with proxy server?

  <br/>

#### 2) WebClient Class : main class

```java
HttpRequest req = new HttpRequest();
        Image image = null;
        Scanner sc = new Scanner(System.in);
ㄴ
        while (true) {
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
```

We must use while loop to use Get method and Post method without ending the connection. We are using Http version 1.1, so this is persistent protocol. So we have to stay in the same connection, if want to apply POST method right after GET method to the same connection. (while 문 없이 GET, POST, GUI 메소드를 적용시키면, 연결이 한 번 끊기고 다시 연결되어서 작동할 수 있기 때문에, while문을 사용해서 연결이 끊기는 상황을 제어해준다.)

Get input which defines what method we are going to use. 

-GET : input url to request

-POST : input url to request and input data in the second line. For first grading site, only student ID is the data. For the MIR Lab grading site, studentID/number of jpg files is the data. 

-GUI : I couldn't implement GUI method perfectly, so I used JFrame and accept url address from the input. 

<br/>

### 2. Result First

1. GET Method

<img width="579" alt="test1_mission2" src="https://user-images.githubusercontent.com/45492242/68647728-ee86ce00-0561-11ea-8e44-555973fb11ec.png">

2. POST Method

<img width="568" alt="test1_mission3" src="https://user-images.githubusercontent.com/45492242/68647736-f5addc00-0561-11ea-9773-7b49671383e2.png">

3. Final Score Result

<img width="556" alt="test1_결과" src="https://user-images.githubusercontent.com/45492242/68647760-01010780-0562-11ea-993f-9be1350f9d53.png">



#### 3. Result Second : MIR LAB site

1. GET Method 

<img width="350" alt="test2_get1" src="https://user-images.githubusercontent.com/45492242/68652686-5bec2c00-056d-11ea-97ea-70382c00f68e.png">

​	Output after Get Method : count number of jpg files - 8 jpg files

<img width="605" alt="test2_get2" src="https://user-images.githubusercontent.com/45492242/68652699-63133a00-056d-11ea-8f63-6280bed48cd2.png">

2. POST Method 1) for mission2  : Enter URL address and data whose format is StudentID / number of jpg files (in my case, 2016025514/8). Then the output shows whether my count is true or not. 

   <img width="535" alt="test2_post" src="https://user-images.githubusercontent.com/45492242/68652834-aec5e380-056d-11ea-8c4f-d1864dc6911b.png">

3. POST Method 2) for mission3 : In mission 3, when I enter URL address and data, a line comes out as an output, and I could choose this in the site. (mission3의 경우, 데이터 입력 이후, 결과가 출력되고, 해당 결과를 MIR LAB test site에서 체크하여 제출합니다. )

   <img width="348" alt="test2_post2" src="https://user-images.githubusercontent.com/45492242/68652918-e9c81700-056d-11ea-8073-7d9945cc72fe.png">

4. GUI Method(?) : Enter URL address

   <img width="310" alt="test2_gui" src="https://user-images.githubusercontent.com/45492242/68653055-30b60c80-056e-11ea-9d6d-a7bdf6d2f27b.png">

   

   Then, one java script page pops up. 

   <img width="746" alt="스크린샷 2019-11-12 오후 12 48 50" src="https://user-images.githubusercontent.com/45492242/68653081-3b70a180-056e-11ea-8257-d2124f36410c.png">

   

### 4. Realization

During the test, it was fun that the results of the code were reflected on the web.

The real http communication may be more complex than this, but it's good to go through the process of understanding the basic framework one more time through practice.

Sorry that I closed the project at this point because I don't have enough time to implement GUI.

Although the project was implemented only by the CLI, GUI will be implemented before long, even for personal projects.

