package imr.hi.kartverket;

import imr.hi.search.LocationSearchProvider;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ngu.geojson.Geojson.Crs;
import no.ngu.geojson.Geojson.Feature;
import no.ngu.geojson.Geojson.FeatureCollection;
import no.ngu.geojson.Geometry;
import no.ngu.kartverket.api.adresser.AdresserHttpService;
import no.ngu.kartverket.api.adresser.AdresserService;
import no.ngu.kartverket.api.adresser.SokResponse;

public final class AdresserLocationServiceProvider implements LocationSearchProvider {

  private final AdresserService adresserService = new AdresserHttpService(
      HttpClient.newHttpClient(),
      URI.create("https://ws.geonorge.no/adresser/v1/")
  );
  
  @Override
  public FeatureCollection search(String q) {
    try {
      var response = adresserService.sok(q, new AdresserHttpService.Options());
      var features = response.adresser().stream()
          .map(this::toFeature)
          .filter(feature -> feature != null)
          .toList();
      return new FeatureCollection("Kartverket adresser", null, features);
    } catch (Exception exception) {
      System.err.println("Kartverket location search failed: " + exception.getMessage());
      return new FeatureCollection("Kartverket adresser", null, List.of());
    }
  }

  private Feature toFeature(SokResponse.OutputAdresse sok) {
    String epsg = sok.representasjonspunkt().epsg();
    Crs crs = new Crs("name", Map.of("name", epsg));
    Geometry geometry = new Geometry.Point(
      sok.representasjonspunkt().lon(),
      sok.representasjonspunkt().lat()
    );
    var properties = new HashMap<String, String>();
    properties.put("adressetekst", sok.adressetekst());
    properties.put("postnummer", sok.postnummer());
    properties.put("poststed", sok.poststed());
    properties.put("kommunenavn", sok.kommunenavn());
    LocationSearchProvider.titleOf(properties, "adressetekst", "postnummer", "kommunenavn");
    return new Feature(crs, properties, geometry);
  }
}
