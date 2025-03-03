package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileService extends Response implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
  private static File dir;

  public void process(Request request, PrintWriter out) throws IOException {
    LOG.info("FileService");
    String fileName = request.getPath().substring("/files/".length());
    File file = new File(dir, fileName);
    if (!file.exists()) {
      generate404(out);
    } else {
      byte[] data = Files.readAllBytes(file.toPath());
      setBody(new String(data, StandardCharsets.UTF_8));
      setContentType(Constants.CONTENT_TYPE_APPLICATION_OCTET_STREAM);
      setStatus(Constants.Status.STATUS_OK);
      generate(out);
    }
  }

  public static final void setDir(String directory) {
    FileService.dir = new File(directory);
    LOG.info("dir={} exists={} isDir={}", dir.getPath(), dir.exists(), dir.isDirectory());
  }
}