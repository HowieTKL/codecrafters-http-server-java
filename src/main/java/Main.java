import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);
       System.out.println("accepted new connection");
       Socket socket = serverSocket.accept(); // Wait for connection from client.
       PrintStream out = new PrintStream(socket.getOutputStream());
       BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       String requestLine = in.readLine();
       String[] requestLineParts = requestLine.split(" ");

       if ("/".equals(requestLineParts[1])) {
         out.print("HTTP/1.1 200 OK\r\n\r\n");
       } else if (requestLineParts[1].startsWith("/echo/")) {
         String echo = requestLineParts[1].substring(6);
         out.print("HTTP/1.1 200 OK\r\n\r\n");
         out.print("Content-Type: text/plain\r\n");
         out.print("Content-Length: ");
         out.print(echo.length());
         out.print("\r\n\r\n");
         out.print(echo);
         out.print("\r\n");
       } else {
         out.print("HTTP/1.1 404 Not Found\r\n\r\n");
       }
       out.flush();

     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
