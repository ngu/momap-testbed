package no.ngu.kartverket.api.adresser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import no.ngu.kartverket.api.AbstractHttpService;

public final class AdresserHttpService extends AbstractHttpService implements AdresserService {

  public AdresserHttpService(HttpClient client, URI baseUri, ObjectMapper objectMapper) {
    super(client, baseUri, objectMapper);
  }

  public AdresserHttpService(HttpClient client, URI baseUri) {
    this(client, baseUri, new ObjectMapper().findAndRegisterModules());
  }

  @Override
  public SokResponse sok(String text, Options options) throws IOException, InterruptedException {
    return sendRequest("sok", text, options, SokResponse.class);
  }
}
