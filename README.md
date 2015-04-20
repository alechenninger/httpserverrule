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

By default, the server is a long running process which shuts down on JVM exit, though to take advantage of this you will need to reuse a rule instance, like:

```java
public abstract class HttpServers {
  public static HttpServerRule longRunning() {
    return HttpServerRule.builder()
        .servingClasspathResources("/pages/")
        .build();
  }
}

public class MyHtmlTest {
  @ClassRule
  public static HttpServerRule server = HttpServers.longRunning();
  
  @Test
  public void shouldBeCool() {
    // use server...
  }
```
