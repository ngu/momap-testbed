package no.ngu.geojson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests Geojson class.
 */
public class GeojsonTest {

  /**
   * Serializer/deserializer.
   */
  private static ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testFeatureCollectionDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geojson.FeatureCollection("Norge-grove-grenser-utm33n",
          new Geojson.Crs("name", Map.of("name", "EPSG:25833")),
        List.of(new Geojson.Feature(null, Map.of(),
            new Geometry.Polygon(
                new Geometry.LineString(
                    new Geometry.Point(-71715.796881175716408, 6910621.071005085483193),
                    new Geometry.Point(-28385.975501339649782, 6978441.660990915261209),
                    new Geometry.Point(-71715.796881175716408, 6910621.071005085483193)
                )
            )))
        ),
        objectMapper.readValue("""
        {
          "type": "FeatureCollection",
          "name": "Norge-grove-grenser-utm33n",
          "crs": { "type": "name", "properties": { "name": "EPSG:25833" } },
          "features": [
            {
              "type": "Feature",
              "properties": {},
              "geometry": {
                "type": "Polygon",
                "coordinates": [ [
                  [ -71715.796881175716408, 6910621.071005085483193 ],
                  [ -28385.975501339649782, 6978441.660990915261209 ],
                  [ -71715.796881175716408, 6910621.071005085483193 ]
                ] ]
            } }
          ]
        }
        """, Geojson.class)
    );
  }

  @Test
  public void testFeatureDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geojson.Feature(null, Map.of(),
            new Geometry.Polygon(
              new Geometry.LineString(
                  new Geometry.Point(-71715.796881175716408, 6910621.071005085483193),
                  new Geometry.Point(-28385.975501339649782, 6978441.660990915261209),
                  new Geometry.Point(-71715.796881175716408, 6910621.071005085483193)
              )
            )
        ),
        objectMapper.readValue("""
        {
          "type": "Feature",
          "properties": {},
          "geometry": {
            "type": "Polygon",
            "coordinates": [ [
              [ -71715.796881175716408, 6910621.071005085483193 ],
              [ -28385.975501339649782, 6978441.660990915261209 ],
              [ -71715.796881175716408, 6910621.071005085483193 ]
            ] ]
          }
        }
        """, Geojson.class)
    );
  }
}
