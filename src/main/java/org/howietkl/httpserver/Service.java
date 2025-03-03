package org.howietkl.httpserver;

import java.io.IOException;
import java.io.PrintWriter;

public interface Service {
  Service process(Request request, PrintWriter out) throws IOException;
  Service setStatus(Constants.Status status);
}
