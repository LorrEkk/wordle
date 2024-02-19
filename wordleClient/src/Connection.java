import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Connection {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

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
