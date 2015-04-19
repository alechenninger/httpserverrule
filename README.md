# httpserverrule
JUnit4 rule for serving files from classpath or filesystem over HTTP

## Using it

```java
public class HttpServerRuleTest {

  @Rule
  public HttpServerRule server = HttpServerRule.builder()
      .stopAfterStatement()
      .build();

  @Test
  public void shouldServeClassPathByDefault() throws IOException {
    HttpTransport client = new NetHttpTransport();

    String footxt = client.createRequestFactory()
        .buildGetRequest(new GenericUrl(server.urlForPath("foo.txt")))
        .execute()
        .parseAsString();

    assertEquals("Hi", footxt.trim());
  }
}
```

By default, the server is a long running process which shuts down on JVM exit, though to take advantage of this you will need to give it a unique id, or reuse a rule instance.
