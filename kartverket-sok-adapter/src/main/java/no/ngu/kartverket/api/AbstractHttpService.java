package no.ngu.kartverket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpService {

  private final HttpClient client;
  private final URI baseUri;
  private final ObjectMapper objectMapper;

  public AbstractHttpService(HttpClient client, URI baseUri, ObjectMapper objectMapper) {
    this.client = client;
    this.baseUri = baseUri;
    this.objectMapper = objectMapper;
  }

  public AbstractHttpService(HttpClient client, URI baseUri) {
    this(client, baseUri, new ObjectMapper().findAndRegisterModules());
  }

  public final static int DEFAULT_KOORDSYS = 25833;

  public record Options(Integer utkoordsys, Boolean fuzzy) {
    public Options() {
      this(DEFAULT_KOORDSYS, false);
    }
  }

  protected <T> T sendRequest(String requestName, String sok, Options options, Class<T> responseType)
      throws IOException, InterruptedException {
    var query = "sok=" + URLEncoder.encode(sok.trim(), StandardCharsets.UTF_8);
    if (options != null) {
      if (options.utkoordsys() != null) {
        query += "&utkoordsys=" + options.utkoordsys();
      }
      if (options.fuzzy() != null) {
        query += "&fuzzy=" + options.fuzzy();
      }
    }
    var uri = baseUri.resolve(requestName + "?" + query);
    var request = HttpRequest.newBuilder(uri)
        .header("Accept", "application/json")
        .GET()
        .build();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() >= 400) {
      throw new IOException("Kartverket " + requestName + " request failed: " + response.statusCode());
    }
    return objectMapper.readValue(response.body(), responseType);
  }
}
