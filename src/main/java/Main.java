import org.howietkl.httpserver.EchoService;
import org.howietkl.httpserver.Request;
import org.howietkl.httpserver.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    try (ServerSocket serverSocket = new ServerSocket(4221);
         ExecutorService executorService = Executors.newFixedThreadPool(4);) {
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        executorService.submit(() -> handleRequest(socket));
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private static void handleRequest(Socket socket) {
    try {
      LOG.info("accepted new connection");
      try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
        Request request = new Request();
        request.parseRequest(in);

        String path = request.getPath();
        if ("/".equals(path)) {
          out.print("HTTP/1.1 200 OK\r\n\r\n");
        } else if (path.startsWith("/echo/")) {
          new EchoService().process(request, out);
        } else if (path.equals("/user-agent")) {
          new UserAgentService().process(request, out);
        } else {
          out.print("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        out.flush();
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

}
