package com.github.alechenninger.httpserverrule;

import org.junit.rules.TestRule;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public interface HttpServerRule extends TestRule {
  URL urlForPath(String path);

  static Builder builder() {
    return new Builder();
  }

  class Builder {
    private String id;
    private ServerConfig config;
    private boolean keepRunning = true;

    public Builder() {
      this(UUID.randomUUID().toString());
    }

    public Builder(String id) {
      withId(id);
      servingClasspathResources();
      keepRunning();
    }

    public Builder withId(String id) {
      this.id = id;
      return this;
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
            ? new LongRunningHttpServerRule(server, id)
            : new ShortRunningHttpServerRule(server);
      } catch (IOException e) {
        throw new HttpServerRuleException(e);
      }
    }
  }
}
