package no.ngu.kartverket.api.stedsnavn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

class StedsnavnHttpServiceTest {

  private URI baseUri = URI.create("https://api.kartverket.no/stedsnavn/v1/");

  @Test
  void search_callsStedEndpointWithSokAndParsesResponse() throws Exception {
    var service = new StedsnavnHttpService(
        HttpClient.newHttpClient(),
        baseUri
    );
    var response = service.sted("Stangvik", new StedsnavnHttpService.Options());
    assertEquals(5, response.navn().size());
    assertEquals(1, response.navn().get(0).stedsnavn().size());
    assertEquals("Stangvik", response.navn().get(0).stedsnavn().get(0).skrivemåte());
  }

  @Test
  void search_callsNavnEndpointWithSokAndParsesResponse() throws Exception {
    var service = new StedsnavnHttpService(
        HttpClient.newHttpClient(),
        baseUri
    );
    var response = service.navn("Stangvik", new StedsnavnHttpService.Options());
    assertEquals(5, response.navn().size());
    assertEquals("Stangvik", response.navn().get(0).skrivemåte());
  }
}
