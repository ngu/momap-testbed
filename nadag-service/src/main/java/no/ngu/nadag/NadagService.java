package no.ngu.nadag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import no.ngu.geojson.Geojson.Feature;
import no.ngu.geojson.Geometry;

public final class NadagService {

  private final NadagRepository repository;

  public NadagService(NadagRepository repository) {
    this.repository = repository;
  }

  public List<NadagProject> byProsjektnavnOrProsjektnr(String prosjektnavn, String prosjektnr) {
    return repository.byProsjektnavnOrProsjektnr(prosjektnavn, prosjektnr);
  }

  public Feature toGeoJsonFeature(NadagProject project, ObjectMapper objectMapper) {
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
        String title = null;
        if (properties.containsKey("prosjektnr")) {
          title = properties.get("prosjektnr");
        }
        if (properties.containsKey("prosjektnavn")) {
          title = (title != null) ? title + ", " : "";
          title += properties.get("prosjektnavn");
        }
        if (title != null) {
          properties.put("title", title);
        }
        return new Feature(geometry.crs(), properties, geometry);
      }
    } catch (JsonProcessingException e) {
      // fall through
      System.err.println(e);
    }
    return null;
  }
}
