package com.github.alechenninger.httpserverrule;

public interface ConfigurableServer {
  void serveFileSystem(String path);

  void serveClassPath(ClassLoader cl, String prefix);
}
