package org.howietkl.httpserver;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Response {
  private Constants.Status status;
  private Map<String, String> headers = new HashMap<>();
  private String body;

  public Response() {
    setStatus(Constants.Status.STATUS_OK);
    setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setContentType(String contentType) {
    headers.put(Constants.HEADER_CONTENT_TYPE, contentType);
  }

  public void setStatus(Constants.Status  status) {
    this.status = status;
  }

  public void generate(PrintWriter out) {
    headers.put(Constants.HEADER_CONTENT_LENGTH, String.valueOf(body.length()));

    out.print(Constants.VERSION);
    out.print(" ");
    out.print(status);
    out.print("\r\n");

    headers.forEach((k, v) -> out.print(k + ": " + v + "\r\n"));
    out.print("\r\n");

    out.print(body);
    out.flush();
  }

  public void generate200(PrintWriter out) {
    out.print("HTTP/1.1 200 OK\r\n\r\n");
    out.flush();
  }

  public void generate404(PrintWriter out) {
    out.print("HTTP/1.1 404 Not Found\r\n\r\n");
    out.flush();
  }

}
