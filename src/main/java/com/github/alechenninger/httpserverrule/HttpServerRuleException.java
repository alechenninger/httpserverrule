package com.github.alechenninger.httpserverrule;

public class HttpServerRuleException extends RuntimeException {
  public HttpServerRuleException() {
  }

  public HttpServerRuleException(String message) {
    super(message);
  }

  public HttpServerRuleException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpServerRuleException(Throwable cause) {
    super(cause);
  }

  public HttpServerRuleException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
