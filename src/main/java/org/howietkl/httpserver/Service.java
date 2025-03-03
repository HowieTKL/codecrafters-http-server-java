package org.howietkl.httpserver;

import java.io.PrintWriter;

public interface Service {
  void process(Request request, PrintWriter out);
}
