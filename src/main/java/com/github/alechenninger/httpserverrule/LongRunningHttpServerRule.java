package com.github.alechenninger.httpserverrule;

import org.junit.rules.ExternalResource;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class LongRunningHttpServerRule extends ExternalResource implements HttpServerRule {
  private final HttpServer server;
  private final AtomicBoolean isRunning;

  public LongRunningHttpServerRule(HttpServer server) {
    this.server = server;
    this.isRunning = new AtomicBoolean(false);
  }

  @Override
  public URL urlForPath(String path) {
    return server.urlForPath(path);
  }

  @Override
  protected void before() throws Throwable {
    if (!isRunning.getAndSet(true)) {
      Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));
      server.start();
    }

    // TODO: await server is actually running?
  }

  private void stopServer() {
    System.out.println("Stopping long running http server...");
    server.stop();
  }
}
