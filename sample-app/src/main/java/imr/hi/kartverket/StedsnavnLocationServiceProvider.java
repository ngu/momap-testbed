package imr.hi.kartverket;

import com.fasterxml.jackson.databind.ObjectMapper;
import imr.hi.search.LocationSearchProvider;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ngu.geojson.Geojson.Crs;
import no.ngu.geojson.Geojson.Feature;
import no.ngu.geojson.Geojson.FeatureCollection;
import no.ngu.geojson.Geometry;
import no.ngu.kartverket.api.AbstractHttpService;
import no.ngu.kartverket.api.stedsnavn.StedResponse;
import no.ngu.kartverket.api.stedsnavn.StedsnavnHttpService;
import no.ngu.kartverket.api.stedsnavn.StedsnavnService;

public final class StedsnavnLocationServiceProvider implements LocationSearchProvider {

  private final StedsnavnService stedsnavnService = new StedsnavnHttpService(
      HttpClient.newHttpClient(),
      URI.create("https://api.kartverket.no/stedsnavn/v1/"),
      new ObjectMapper().findAndRegisterModules()
  );

  @Override
  public FeatureCollection search(String q) {
    try {
      var response = stedsnavnService.sted(q);
      var features = response.navn().stream()
          .map(this::toFeature)
          .filter(feature -> feature != null)
          .toList();
      return new FeatureCollection("provider.kartverket.stedsnavn", null, features);
    } catch (Exception exception) {
      System.err.println("Kartverket location search failed: " + exception.getMessage());
      return new FeatureCollection("provider.kartverket.stedsnavn", null, List.of());
    }
  }

  private Feature toFeature(StedResponse.Sok sok) {
    int koordsys = sok.representasjonspunkt().koordsys();
    if (koordsys <= 0) {
      koordsys = AbstractHttpService.DEFAULT_KOORDSYS;
    }
    Crs crs = new Crs("name", Map.of("name", "EPSG:" + koordsys));
    Geometry geometry = sok.geojson().geometry();
    var properties = new HashMap<String, String>();
    if (! sok.stedsnavn().isEmpty()) {
      var entry = sok.stedsnavn().get(0);
      properties.put("stedsnavn", entry.skrivemåte());
    }
    if (! sok.kommuner().isEmpty()) {
      var entry = sok.kommuner().get(0);
      properties.put("kommunenavn", entry.kommunenavn());
    }
    if (! sok.fylker().isEmpty()) {
      var entry = sok.fylker().get(0);
      properties.put("fylkesnavn", entry.fylkesnavn());
    }
    LocationSearchProvider.titleOf(properties, "stedsnavn", "kommunenavn", "fylkesnavn");
    return new Feature(crs, properties, geometry);
  }
}
