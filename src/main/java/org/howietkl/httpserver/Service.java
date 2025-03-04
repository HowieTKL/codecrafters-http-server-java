package org.howietkl.httpserver;

import java.io.IOException;
import java.io.PrintStream;

public interface Service {
  Service process(Request request, PrintStream out) throws IOException;
  Service setStatus(Constants.Status status);
}
