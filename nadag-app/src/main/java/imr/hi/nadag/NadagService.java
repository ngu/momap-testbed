package imr.hi.nadag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import imr.hi.search.LocationSearchProvider;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import no.ngu.geojson.Geojson.Feature;
import no.ngu.geojson.Geojson.FeatureCollection;
import no.ngu.geojson.Geometry;

public final class NadagService implements LocationSearchProvider {

  private final NadagRepository repository;

  public NadagService(NadagRepository repository) {
    this.repository = repository;
  }

  public List<NadagProject> byProsjektnavnOrProsjektnr(String prosjektnavn, String prosjektnr) {
    return repository.byProsjektnavnOrProsjektnr(prosjektnavn, prosjektnr);
  }

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public FeatureCollection search(String q) {
    var nadagProjects = repository.byProsjektnavnOrProsjektnr(q, q);
    var features = nadagProjects.stream()
        .map(p -> toGeoJsonFeature(p))
        .filter(f -> f != null)
        .toList();
    return new FeatureCollection("GeotekniskUnders", null, features);
  }

  private Feature toGeoJsonFeature(NadagProject project) {
    try {
      if (project.omradeGeoJson() != null) {
        var geometry = objectMapper.readValue(project.omradeGeoJson(), Geometry.class);
        var properties = new HashMap<String, String>();
        if (project.prosjektnr() != null) {
          properties.put("prosjektnr", project.prosjektnr());
        }
        if (project.prosjektnavn() != null) {
          properties.put("prosjektnavn", project.prosjektnavn());
        }
        LocationSearchProvider.titleOf(properties, "prosjektnr", "prosjektnavn");
        return new Feature(geometry.crs(), properties, geometry);
      }
    } catch (JsonProcessingException e) {
      // fall through
      System.err.println(e);
    }
    return null;
  }
}
