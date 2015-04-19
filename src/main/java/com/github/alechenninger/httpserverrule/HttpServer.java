package com.github.alechenninger.httpserverrule;

import java.net.URL;

public interface HttpServer {
  void start();

  void stop();;

  URL urlForPath(String path);
}
