package com.github.alechenninger.httpserverrule;

import org.junit.rules.ExternalResource;

import java.net.URL;

public class ShortRunningHttpServerRule extends ExternalResource implements HttpServerRule {
  private final HttpServer server;

  public ShortRunningHttpServerRule(HttpServer server) {
    this.server = server;
  }

  public URL urlForPath(String path) {
    return server.urlForPath(path);
  }

  @Override
  protected void before() throws Throwable {
    server.start();
  }

  @Override
  protected void after() {
    server.stop();
  }
}
