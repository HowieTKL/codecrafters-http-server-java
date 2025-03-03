package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class BasicService implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

  private Constants.Status status = Constants.Status.STATUS_OK;
  private final Map<String, String> headers = new HashMap<>();
  private String body = "";

  @Override
  public Service process(Request request, PrintWriter out) throws IOException {
    LOG.info("BasicService");

    // gzip support
    String encoding = request.getHeaders().get(Constants.HEADER_ACCEPT_ENCODING);
    if ("gzip".equals(encoding)) {
      headers.put(Constants.HEADER_CONTENT_ENCODING, encoding);
    }

    generate(out);
    return this;
  }

  public void setBody(String body) {
    if (body == null) {
      body = "";
    }
    this.body = body;
  }

  public void setContentType(String contentType) {
    headers.put(Constants.HEADER_CONTENT_TYPE, contentType);
  }

  public Service setStatus(Constants.Status  status) {
    this.status = status;
    return this;
  }

  public void generate(PrintWriter out) {
    // e.g. HTTP/1.1 200 OK
    out.print(Constants.VERSION);
    out.print(" ");
    out.print(status);
    out.print("\r\n");

    if (!body.isEmpty()) {
      headers.put(Constants.HEADER_CONTENT_LENGTH, String.valueOf(body.length()));
    }
    headers.forEach((k, v) -> out.print(k + ": " + v + "\r\n"));
    out.print("\r\n");
    if (!body.isEmpty()) {
      out.print(body);
    }
    out.flush();
  }

  public String getBody() {
    return body;
  }

  public void addAcceptEncoding(String encoding) {
    headers.put(Constants.HEADER_ACCEPT_ENCODING, encoding);
  }

}
