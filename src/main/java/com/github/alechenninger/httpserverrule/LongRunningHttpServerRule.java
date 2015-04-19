package com.github.alechenninger.httpserverrule;

import org.junit.rules.ExternalResource;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LongRunningHttpServerRule extends ExternalResource implements HttpServerRule {
  private final String serverId;

  private final static Map<String, HttpServerAndStatus> servers =
      Collections.synchronizedMap(new HashMap<>());

  /**
   * @param serverId Identifies the server so that subsequent instantiations of the same rule will
   *    reuse a server with the same id if one exists and is already started.
   */
  public LongRunningHttpServerRule(HttpServer server, String serverId) {
    this.serverId = serverId;

    servers.putIfAbsent(serverId, new HttpServerAndStatus(server));
  }

  @Override
  public URL urlForPath(String path) {
    return servers.get(serverId).server.urlForPath(path);
  }

  @Override
  protected void before() throws Throwable {
    servers.get(serverId).start();
  }

  class HttpServerAndStatus {
    private final HttpServer server;
    private boolean isRunning = false;

    HttpServerAndStatus(HttpServer server) {
      this.server = server;
    }

    synchronized void start() {
      isRunning = true;
      server.start();
      Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
  }
}
