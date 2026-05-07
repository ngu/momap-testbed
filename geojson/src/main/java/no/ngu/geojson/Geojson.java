package no.ngu.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import java.util.Map;

/**
 * GeoJSON data structures.
 * Specific GeoJSON types are represented as records implementing the Geojson interface.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Geojson.Feature.class, name = "Feature"),
  @JsonSubTypes.Type(value = Geojson.FeatureCollection.class, name = "FeatureCollection"),
  @JsonSubTypes.Type(value = Geojson.Crs.class, name = "Crs"),
  @JsonSubTypes.Type(value = Geojson.Crs.class, name = "name")
})
@JsonIgnoreProperties("type")
public sealed interface Geojson {

  String type();

  /**
   * GeoJSON FeatureCollection.
   */
  public record FeatureCollection(
      String type,
      String name,
      Crs crs,
      List<Feature> features
  ) implements Geojson {
    /**
    * Constructor used by Jackson.
     *
     * @param type the type, will be set to "FeatureCollection"
     * @param name the name
     * @param crs the coordinate reference system
     * @param features the list of features
     */
    @JsonCreator
    public FeatureCollection {
      type = "FeatureCollection";
    }

    /**
     * Constructor to use in code, with type set to "FeatureCollection".
     *
     * @param name the name
     * @param crs the coordinate reference system
     * @param features the list of features
     */
    public FeatureCollection(
        String name,
        Crs crs,
        List<Feature> features) {
      this("FeatureCollection", name, crs, features);
    }
  }

  /**
   * GeoJSON Feature.
   */
  public record Feature(
      String type,
      Crs crs,
      Map<String, String> properties,
      Geometry geometry
  ) implements Geojson {
    /**
    * Constructor used by Jackson.
     *
     * @param type the type, will be set to "Feature"
     * @param crs the coordinate reference system
     * @param properties the properties
     * @param geometry the geometry
     */
    @JsonCreator
    public Feature {
      type = "Feature";
    }

    /**
     * Constructor to use in code, with type set to "Feature".
     *
     * @param crs the coordinate reference system
     * @param properties the properties
     * @param geometry the geometry
     */
    public Feature(
        Crs crs,
        Map<String, String> properties,
        Geometry geometry) {
      this("Feature", crs, properties, geometry);
    }
  }

  /**
   * GeoJSON Coordinate Reference System (CRS).
   */
  public record Crs(
      String type,
      Map<String, String> properties
  ) {
    /**
     * Creates a CRS from an EPSG code.
     * @param epsgCode the EPSG code
     * @return the CRS
     */
    public static Crs from(EpsgCode epsgCode) {
      return new Crs("name", Map.of("name", "EPSG:" + epsgCode.toCrsNumber()));
    }

    /**
     * Creates a CRS from an EPSG code number.
     * @param crsNumber the EPSG code number
     * @return the CRS
     */
    public static Crs from(int crsNumber) {
      return from(EpsgCode.fromCrsNumber(crsNumber));
    }
  }
}
