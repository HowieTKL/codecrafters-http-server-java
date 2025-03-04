package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;

public class EchoService extends BasicService implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(EchoService.class);

  public Service process(Request request, PrintStream out) throws IOException {
    LOG.info("EchoService");
    setBody(request.getPath().substring("/echo/".length()));
    setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
    setStatus(Constants.Status.STATUS_OK);
    super.process(request, out);
    return this;
  }
}
