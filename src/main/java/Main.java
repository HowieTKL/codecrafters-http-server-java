import org.howietkl.httpserver.BasicService;
import org.howietkl.httpserver.Constants;
import org.howietkl.httpserver.EchoService;
import org.howietkl.httpserver.FileService;
import org.howietkl.httpserver.Request;
import org.howietkl.httpserver.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    try (ServerSocket serverSocket = new ServerSocket(4221);
         ExecutorService executorService = Executors.newFixedThreadPool(4)) {
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      if (args.length > 1 && "--directory".equals(args[0].trim())) {
        LOG.info("args: {} {}", args[0], args[1]);
        FileService.setDir(args[1]);
      }
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
    LOG.info("accepted new connection");
    try (PrintStream out = new PrintStream(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      for (;;) {
        Request request = new Request();
        request.parseRequest(in);

        String path = request.getPath();
        Map<String, String> headers = request.getHeaders();

        if (!headers.isEmpty()) {
          String c = headers.get("Connection");
          if ("close".equals(c)) {
            LOG.info("Connection close");
            break;
          }
        }
        if ("/".equals(path)) {
          new BasicService()
              .setStatus(Constants.Status.STATUS_OK)
              .process(request, out);
        } else if (path.startsWith("/echo/")) {
          new EchoService().process(request, out);
        } else if (path.equals("/user-agent")) {
          new UserAgentService().process(request, out);
        } else if (path.startsWith("/files/")) {
          new FileService().process(request, out);
        } else {
          new BasicService()
              .setStatus(Constants.Status.STATUS_NOT_FOUND)
              .process(request, out);
        }
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

}
