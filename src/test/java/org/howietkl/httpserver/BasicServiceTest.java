package org.howietkl.httpserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class BasicServiceTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getSupportedEncodings() {
    List<String> gzipEncoding = new ArrayList<>();
    gzipEncoding.add("gzip");
    assertEquals(gzipEncoding, BasicService.getSupportedEncodings("gzip"));
    assertEquals(gzipEncoding, BasicService.getSupportedEncodings("unsupported-1, gzip, unsupported-2"));
    assertEquals(Collections.EMPTY_LIST, BasicService.getSupportedEncodings("unsupported"));
    assertEquals(Collections.EMPTY_LIST, BasicService.getSupportedEncodings(""));
  }
}