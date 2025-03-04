package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;

public class UserAgentService extends BasicService implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(UserAgentService.class);

  public Service process(Request request, PrintStream out) throws IOException {
    LOG.info("UserAgentService");
    String userAgent = request.getHeaders().get(Constants.HEADER_USER_AGENT);
    setStatus(Constants.Status.STATUS_OK);
    setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
    setBody(userAgent);
    super.process(request, out);
    return this;
  }
}
