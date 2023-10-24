import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

class ConnectionHandler implements Runnable {
    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private final Database database;
    private boolean isRunning;

    public ConnectionHandler(Socket clientSocket, Database database) {
        client = clientSocket;
        this.database = database;
        isRunning = true;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (isRunning) {
                String line = in.readLine(); //wait for client request

                if (line.equals("!draw")) {
                    out.println(database.drawWord());
                } else if (line.startsWith("!check ")) {
                    String word = line.split(" ", 2)[1];

                    if (database.checkWord(word)) {
                        out.println("!true");
                    } else {
                        out.println("!false");
                    }
                } else if (line.equals("!close")) {
                    isRunning = false;
                } else {
                    out.println("!error");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error!");
        } catch (Exception e) {
            //ignore
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            System.out.println("Client disconnected " + client.getInetAddress().getHostAddress());

            in.close();
            out.close();

            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }

}
