package com.github.alechenninger.httpserverrule;

import org.junit.rules.TestRule;

import java.io.IOException;
import java.net.URL;

public interface HttpServerRule extends TestRule {
  URL urlForPath(String path);

  static Builder builder() {
    return new Builder();
  }

  class Builder {
    private ServerConfig config;
    private boolean keepRunning = true;

    public Builder() {
      servingClasspathResources();
      keepRunning();
    }

    public Builder servingClasspathResources() {
      return servingClasspathResources("");
    }

    public Builder servingClasspathResources(String prefix) {
      return servingClasspathResources(Thread.currentThread().getContextClassLoader(), prefix);
    }

    public Builder servingClasspathResources(ClassLoader cl, String prefix) {
      config = new ServerConfig.ClasspathServer(cl, prefix);
      return this;
    }

    public Builder servingFileSystemAt(String path) {
      config = new ServerConfig.FileSystemServer(path);
      return this;
    }

    public Builder keepRunning() {
      keepRunning = true;
      return this;
    }

    public Builder stopAfterStatement() {
      keepRunning = false;
      return this;
    }

    public HttpServerRule build() {
      try {
        HttpServer server = new UndertowHttpServer(config);

        return keepRunning
            ? new LongRunningHttpServerRule(server)
            : new ShortRunningHttpServerRule(server);
      } catch (IOException e) {
        throw new HttpServerRuleException(e);
      }
    }
  }
}
