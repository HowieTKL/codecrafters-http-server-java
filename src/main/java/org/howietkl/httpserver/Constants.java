package org.howietkl.httpserver;

public class Constants {
  public static final String HEADER_ACCEPT = "Accept";
  public static final String HEADER_USER_AGENT = "User-Agent";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";

  public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
  public static final String CONTENT_TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";
  public static final String VERSION = "HTTP/1.1";

  public enum Status {
    STATUS_OK(200, "OK"),
    STATUS_NOT_FOUND(404, "Not Found"),;

    private int code;
    private String message;
    Status(int code, String message) {
      this.code = code;
      this.message = message;
    }
    public int getCode() {
      return code;
    }
    public String getMessage() {
      return message;
    }
    public String toString() {
      return code + " " + message;
    }
  }
}
