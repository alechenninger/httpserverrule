package com.github.alechenninger.httpserverrule;

public interface ServerConfig {
  void configure(ConfigurableServer configurable);

  class FileSystemServer implements ServerConfig {
    private final String path;

    public FileSystemServer(String path) {
      this.path = path;
    }

    @Override
    public void configure(ConfigurableServer configurable) {
      configurable.serveFileSystem(path);
    }
  }

  class ClasspathServer implements ServerConfig {
    private final ClassLoader cl;
    private final String prefix;

    public ClasspathServer(ClassLoader cl, String prefix) {
      this.cl = cl;
      this.prefix = prefix;
    }

    @Override
    public void configure(ConfigurableServer configurable) {
      configurable.serveClassPath(cl, prefix);
    }
  }
}
