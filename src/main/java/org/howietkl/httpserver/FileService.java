package org.howietkl.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileService extends BasicService implements Service {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
  private static File dir;

  public Service process(Request request, PrintWriter out) throws IOException {
    String fileName = request.getPath().substring("/files/".length());
    File file = new File(dir, fileName);
    if (request.getMethod().equalsIgnoreCase("GET")) {
      LOG.info("FileService GET");
      if (!file.exists()) {
        setStatus(Constants.Status.STATUS_NOT_FOUND);
      } else {
        byte[] data = Files.readAllBytes(file.toPath());
        setBody(new String(data, StandardCharsets.UTF_8));
        setContentType(Constants.CONTENT_TYPE_APPLICATION_OCTET_STREAM);
        setStatus(Constants.Status.STATUS_OK);
      }
    } else if (request.getMethod().equalsIgnoreCase("POST")) {
      LOG.info("FileService POST");
      try (FileOutputStream fos = new FileOutputStream(file)) {
        String data = request.getBody();
        if (data == null) {
          data = "";
        }
        fos.write(data.getBytes(StandardCharsets.UTF_8));
      }
      setStatus(Constants.Status.STATUS_CREATED);
    }
    super.process(request, out);
    return this;
  }

  public static final void setDir(String directory) {
    FileService.dir = new File(directory);
    LOG.info("dir={} exists={} isDir={}", dir.getPath(), dir.exists(), dir.isDirectory());
  }
}