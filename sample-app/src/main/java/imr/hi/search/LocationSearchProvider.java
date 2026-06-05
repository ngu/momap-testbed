package imr.hi.search;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import no.ngu.geojson.Geojson;

public interface LocationSearchProvider {
  Geojson.FeatureCollection search(String q);

  public static String titleOf(Map<String, ? super String> properties, String... titleProperties) {
    String title = List.of(titleProperties).stream()
        .map(prop -> properties.get(prop))
        .filter(Objects::nonNull)
        .map(Object::toString)
        .filter(Predicate.not(String::isBlank))
        .collect(Collectors.joining(", "));
    if (! title.isBlank()) {
      properties.put("title", title);
    }
    return title;
  }
}
