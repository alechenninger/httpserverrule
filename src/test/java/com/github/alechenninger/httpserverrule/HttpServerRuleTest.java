package com.github.alechenninger.httpserverrule;

import static org.junit.Assert.assertEquals;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class HttpServerRuleTest {

  @ClassRule
  public static HttpServerRule server = HttpServerRule.builder()
      .stopAfterStatement()
      .build();

  @ClassRule
  public static HttpServerRule longRunningServer = HttpServerRule.builder().build();

  @Test
  public void shouldServeClassPathByDefault() throws IOException {
    HttpTransport client = new NetHttpTransport();

    String footxt = client.createRequestFactory()
        .buildGetRequest(new GenericUrl(server.urlForPath("foo.txt")))
        .execute()
        .parseAsString();

    assertEquals("Hi", footxt.trim());
  }

  @Test
  public void shouldWorkWithLongRunningServer() throws IOException {
    HttpTransport client = new NetHttpTransport();

    String footxt = client.createRequestFactory()
        .buildGetRequest(new GenericUrl(longRunningServer.urlForPath("foo.txt")))
        .execute()
        .parseAsString();

    assertEquals("Hi", footxt.trim());
  }
}
