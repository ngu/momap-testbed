package no.ngu.kartverket.api.stedsnavn;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import no.ngu.kartverket.api.AbstractHttpService;

public final class StedsnavnHttpService extends AbstractHttpService implements StedsnavnService {

  public StedsnavnHttpService(HttpClient client, URI baseUri, ObjectMapper objectMapper) {
    super(client, baseUri, objectMapper);
  }

  public StedsnavnHttpService(HttpClient client, URI baseUri) {
    this(client, baseUri, new ObjectMapper().findAndRegisterModules());
  }

  @Override
  public StedResponse sted(String text, Options options) throws IOException, InterruptedException {
    return sendRequest("sted", text, options, StedResponse.class);
  }

  @Override
  public NavnResponse navn(String text, Options options) throws IOException, InterruptedException {
    return sendRequest("navn", text, options, NavnResponse.class);
  }
}
