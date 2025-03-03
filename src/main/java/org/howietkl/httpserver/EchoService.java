package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class EchoService extends Response implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(EchoService.class);

  public void process(Request request, PrintWriter out) {
    LOG.info("EchoService");
    setBody(request.getPath().substring("/echo/".length()));
    setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
    setStatus(Constants.Status.STATUS_OK);
    generate(out);
  }
}
