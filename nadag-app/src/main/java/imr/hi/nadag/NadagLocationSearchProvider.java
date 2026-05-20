package imr.hi.nadag;

import com.fasterxml.jackson.databind.ObjectMapper;
import imr.hi.search.LocationSearchProvider;
import no.ngu.geojson.Geojson.FeatureCollection;
import no.ngu.nadag.NadagService;

public final class NadagLocationSearchProvider implements LocationSearchProvider {

  private final NadagService nadagService;

  public NadagLocationSearchProvider(NadagService nadagService) {
    this.nadagService = nadagService;
  }

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public FeatureCollection search(String q) {
    var nadagProjects = nadagService.byProsjektnavnOrProsjektnr(q, q);
    var features = nadagProjects.stream()
        .map(p -> nadagService.toGeoJsonFeature(p, objectMapper))
        .filter(f -> f != null)
        .toList();
    return new FeatureCollection("GeotekniskUnders", null, features);
  }
}
