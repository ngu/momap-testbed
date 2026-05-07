package no.ngu.kartverket.api.adresser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

class AdresserHttpServiceTest {

  private URI baseUri = URI.create("https://ws.geonorge.no/adresser/v1/");

  @Test
  void search_callsSokEndpointWithSokAndParsesResponse() throws Exception {
    var service = new AdresserHttpService(
        HttpClient.newHttpClient(),
        baseUri
    );
    var response = service.sok("Dr. Sands veg 4", new AdresserHttpService.Options());
    assertEquals(1, response.adresser().size());
    assertEquals("Dr. Sands veg 4", response.adresser().get(0).adressetekst());
  }
}
