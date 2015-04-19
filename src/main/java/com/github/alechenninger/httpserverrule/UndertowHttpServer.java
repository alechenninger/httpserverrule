package com.github.alechenninger.httpserverrule;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Paths;

public class UndertowHttpServer implements HttpServer {
  private final Undertow undertow;
  private final int port;
  private final String hostname;

  public UndertowHttpServer(ServerConfig config) throws IOException {
    this.port = getFreePort();
    this.hostname = "localhost";

    final Undertow.Builder builder = Undertow.builder()
        .addHttpListener(port, hostname);

    config.configure(new ConfigurableServer() {
      @Override
      public void serveFileSystem(String path) {
        ResourceManager rm = new FileResourceManager(Paths.get(path).toFile(), 0);
        addResourceHandler(rm);
      }

      @Override
      public void serveClassPath(ClassLoader cl, String prefix) {
        ResourceManager rm = new ClassPathResourceManager(cl, prefix);
        addResourceHandler(rm);
      }

      private void addResourceHandler(ResourceManager rm) {
        builder.setHandler(new ResourceHandler(rm));
      }
    });

    undertow = builder.build();
  }

  @Override
  public void start() {
    undertow.start();
  }

  @Override
  public void stop() {
    undertow.stop();
  }

  public URL urlForPath(String path) {
    try {
      return new URL("http://" + hostname + ":" + port + "/" + path.replaceAll("^/", ""));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static int getFreePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    }
  }
}
