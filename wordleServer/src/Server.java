import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable  {
    ServerSocket server;

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            Database database = new Database();

            ExecutorService threads = Executors.newCachedThreadPool();

            while (true) { //capture new clients
                Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());

                ConnectionHandler clientConnection = new ConnectionHandler(client, database);

                threads.execute(clientConnection);
            }

        } catch (SQLException e) {
            System.err.println("Database connection error!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeServer();
        }
    }

    private void closeServer() {
        try {
            System.out.println("Server closed");

            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }

}