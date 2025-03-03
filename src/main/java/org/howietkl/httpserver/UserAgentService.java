package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class UserAgentService extends Response implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(UserAgentService.class);

  public void process(Request request, PrintWriter out) {
    LOG.info("UserAgentService");
    String userAgent = request.getHeaders().get(Constants.HEADER_USER_AGENT);
    setStatus(Constants.Status.STATUS_OK);
    setContentType(Constants.CONTENT_TYPE_TEXT_PLAIN);
    setBody(userAgent);
    generate(out);
  }
}
