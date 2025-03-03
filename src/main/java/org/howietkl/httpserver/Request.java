package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
  private static final Logger LOG = LoggerFactory.getLogger(Request.class);

  private String requestLine;
  private String method;
  private String path;
  private String version;

  private final Map<String, String> headers = new HashMap<>();
  private String body;

  public void parseRequest(BufferedReader in) throws IOException {
    LOG.info("parseRequest");

    requestLine = in.readLine();
    LOG.debug("{}", requestLine);
    String[] requestLineParts = requestLine.split(" ");
    method = requestLineParts[0];
    path = requestLineParts[1];
    version = requestLineParts[2];

    String header;
    while(!(header = in.readLine()).isEmpty()) { // read until empty line
      int separatorPosition = header.indexOf(':');
      if (separatorPosition > 0) {
        String key = header.substring(0, separatorPosition);
        String value = header.substring(separatorPosition + 1).trim();
        headers.put(key, value);
        LOG.debug("{}: {}", key, value);
      }
    }

    String contentLengthStr = headers.get(Constants.HEADER_CONTENT_LENGTH);
    if (contentLengthStr != null) {
      int contentLength = Integer.parseInt(contentLengthStr);
      char[] bodyArray = new char[contentLength];
      int c;
      for (int i = 0; i < contentLength && (c = in.read()) != -1; ++i) {
        bodyArray[i] = (char) c;
      }
      body = new String(bodyArray);
      LOG.debug("{}", body);
    }
  }

  public String getRequestLine() {
    return requestLine;
  }

  public Map<String, String> getHeaders() {
    return new HashMap<>(headers);
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getVersion() {
    return version;
  }

  public String getBody() {
    return body;
  }

}