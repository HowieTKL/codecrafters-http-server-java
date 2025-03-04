package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

public class BasicService implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

  private Constants.Status status = Constants.Status.STATUS_OK;
  private final Map<String, String> headers = new HashMap<>();
  private String body;
  private byte[] bodyBytes;

  @Override
  public Service process(Request request, PrintStream out) throws IOException {
    LOG.info("BasicService");

    List<String> supportedEncodings = getSupportedEncodings(request.getHeaders().get(Constants.HEADER_ACCEPT_ENCODING));
    if (!supportedEncodings.isEmpty()) {
      headers.put(Constants.HEADER_CONTENT_ENCODING, String.join(",", supportedEncodings));
      if (getBody() != null) {
        ByteArrayOutputStream gzippedOut = new ByteArrayOutputStream();
        GZIPOutputStream gzippingOut = new GZIPOutputStream(gzippedOut);
        gzippingOut.write(getBody().getBytes(StandardCharsets.UTF_8));
        gzippingOut.close();
        // setBody(bytesToHex(gzippedOut.toByteArray()));
        // setBody(new String(gzippedOut.toByteArray(), StandardCharsets.UTF_8));
        setBody(gzippedOut.toByteArray());
      }
      setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
    }

    generate(out);
    return this;
  }

  static List<String> getSupportedEncodings(String encodings) {
    if (encodings == null) {
      return Collections.emptyList();
    }
    return Stream.of(encodings.split(","))
        .map(String::trim)
        .filter(e -> "gzip".equals(e))
        .distinct()
        .collect(Collectors.toList());
  }

  static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      String hex = String.format("%02x", b);
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public void setBody(String body) {
    this.body = body;
    bodyBytes = null;
  }

  public void setBody(byte[] bytes) {
    body = null;
    bodyBytes = bytes;
  }

  public void setContentType(String contentType) {
    headers.put(Constants.HEADER_CONTENT_TYPE, contentType);
  }

  public Service setStatus(Constants.Status  status) {
    this.status = status;
    return this;
  }

  public void generate(PrintStream out) throws IOException {
    // e.g. HTTP/1.1 200 OK
    out.print(Constants.VERSION);
    out.print(" ");
    out.print(status);
    out.print("\r\n");

    if (body != null && !body.isEmpty()) {
      headers.put(Constants.HEADER_CONTENT_LENGTH, String.valueOf(body.length()));
    } else if (bodyBytes != null) {
      headers.put(Constants.HEADER_CONTENT_LENGTH, String.valueOf(bodyBytes.length));
    }
    headers.forEach((k, v) -> out.print(k + ": " + v + "\r\n"));
    out.print("\r\n");
    if (body != null && !body.isEmpty()) {
      out.print(body);
    } else if (bodyBytes != null) {
      out.write(bodyBytes);
    }
    out.flush();
  }

  public String getBody() {
    return body;
  }

}
