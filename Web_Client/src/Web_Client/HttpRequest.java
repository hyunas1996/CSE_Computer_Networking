package Web_Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    //getmethod
    public String getWebContentByGet(String urlString, final String charset, int timeout) throws IOException {

        //Debug line
        //System.out.print(urlString);

        if (urlString == null || urlString.length() == 0) {
            return null;
        }

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

        //성공적으로 연결 안되면 종료
        try {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

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

    //post method - 사용자의 input이 entity body에 담겨서 web에게 전송!!!
    //해당 input을 반영하여 response해줄 것을 요청함
    public String getWebContentByPost(String urlString, String data, final String charset, int timeout) throws IOException{
        if(urlString == null || urlString.length() == 0){
            return null;
        }

        //get method와 아래 세 줄 동일
        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


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
        connection.connect();

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        byte[] content = data.getBytes("UTF-8");

        out.write(content);
        out.flush();
        out.close();


        InputStream input = connection.getInputStream();

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

        return sb.toString();
        //질문 : return 이렇게 해주면 되나요?? >> NOPE :get method와 동일한 방식으로 재현
        //return out.toString();
    }
}
