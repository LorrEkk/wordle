import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Connection {
    private HttpClient client = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    public String drawAnswer() throws RuntimeException {
        HttpRequest drawRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/wordle/draw"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(drawRequest, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), String.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkWord(String word) throws RuntimeException {
        HttpRequest checkRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/wordle/check?word=" + word))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(checkRequest, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

//public class Connection implements Runnable { //connection with server
//    private Socket client;
//    private BufferedReader in;
//    private PrintWriter out;
//
//
//    @Override
//    public void run() throws RuntimeException{
//        try {
//            client = new Socket("localhost", 9999);
//            out = new PrintWriter(client.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String drawAnswer() throws RuntimeException {
//        try {
//            out.println("!draw");
//            return in.readLine();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void closeConnection() {
//        try {
//            out.println("!close");
//
//            in.close();
//            out.close();
//
//            if (!client.isClosed()) {
//                client.close();
//            }
//        }
//        catch (Exception e) {
//            //ignore
//        }
//    }
//
//    public Boolean checkWord(String word) throws RuntimeException{
//        try {
//            out.println("!check " + word);
//            String line = in.readLine();
//
//            if (line.equals("!true")) {
//                return true;
//            } else if (line.equals("!false")) {
//                return false;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return null;
//    }
//
//}
