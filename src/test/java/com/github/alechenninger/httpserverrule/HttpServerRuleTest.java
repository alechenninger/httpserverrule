package com.github.alechenninger.httpserverrule;

import static org.junit.Assert.assertEquals;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class HttpServerRuleTest {

  @Rule
  public HttpServerRule server = HttpServerRule.builder()
      .stopAfterStatement()
      .build();

  @Test
  public void shouldServeClassPathByDefault() throws IOException {
    HttpTransport client = new NetHttpTransport();

    String footxt = client.createRequestFactory()
        .buildGetRequest(new GenericUrl(server.urlForPath("foo.txt")))
        .execute()
        .parseAsString();

    assertEquals("Hi", footxt.trim());
  }
}
