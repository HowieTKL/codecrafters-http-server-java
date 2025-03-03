package org.howietkl.httpserver;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Response {
  private Constants.Status status;
  private Map<String, String> headers = new HashMap<>();
  private String body;

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

}
