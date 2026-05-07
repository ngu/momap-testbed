package no.ngu.geojson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests Geometry class.
 */
public class GeometryTest {

  /**
   * Serializer/deserializer.
   */
  private static ObjectMapper objectMapper = new ObjectMapper();

  private void assertJsonEquals(String expected, String actual) {
    assertEquals(expected.replaceAll("\\s+", ""), actual);
  }

  @Test
  public void testPointGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.Point(30.0, 10.0),
        objectMapper.readValue("""
            {
              "type": "Point", 
              "coordinates": [30.0, 10.0]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public void testPointWithZGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.Point(30.0, 10.0, 15.0),
        objectMapper.readValue("""
            {
              "type": "Point", 
              "coordinates": [30.0, 10.0, 15.0]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testPointGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "Point", 
          "coordinates": [30.0, 10.0]
        }
        """,
        objectMapper.writeValueAsString(new Geometry.Point(30.0, 10.0))
    );
  }

  @Test
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public void testPointWithZGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "Point", 
          "coordinates": [30.0, 10.0, 15.0]
        }
        """,
        objectMapper.writeValueAsString(new Geometry.Point(30.0, 10.0, 15.0))
    );
  }

  @Test
  public void testMultiPointGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.MultiPoint(
            new Geometry.Point(10.0, 40.0),
            new Geometry.Point(40.0, 30.0),
            new Geometry.Point(20.0, 20.0),
            new Geometry.Point(30.0, 10.0)
        ),
        objectMapper.readValue("""
            {
              "type": "MultiPoint", 
              "coordinates": [
                  [10.0, 40.0],
                  [40.0, 30.0],
                  [20.0, 20.0],
                  [30.0, 10.0]
              ]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testMultiPointGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "MultiPoint", 
          "coordinates": [
              [10.0, 40.0],
              [40.0, 30.0],
              [20.0, 20.0],
              [30.0, 10.0]
          ]
        }
        """,
        objectMapper.writeValueAsString(
          new Geometry.MultiPoint(
              new Geometry.Point(10.0, 40.0),
              new Geometry.Point(40.0, 30.0),
              new Geometry.Point(20.0, 20.0),
              new Geometry.Point(30.0, 10.0)
          )
        )
    );
  }

  @Test
  public void testLineStringGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.LineString(
            new Geometry.Point(10.0, 40.0),
            new Geometry.Point(40.0, 30.0),
            new Geometry.Point(20.0, 20.0),
            new Geometry.Point(30.0, 10.0)
        ),
        objectMapper.readValue("""
            {
              "type": "LineString", 
              "coordinates": [
                  [10.0, 40.0],
                  [40.0, 30.0],
                  [20.0, 20.0],
                  [30.0, 10.0]
              ]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testLineStringGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "LineString", 
          "coordinates": [
              [10.0, 40.0],
              [40.0, 30.0],
              [20.0, 20.0],
              [30.0, 10.0]
          ]
        }
        """,
        objectMapper.writeValueAsString(
            new Geometry.LineString(
                new Geometry.Point(10.0, 40.0),
                new Geometry.Point(40.0, 30.0),
                new Geometry.Point(20.0, 20.0),
                new Geometry.Point(30.0, 10.0)
            )
        )
    );
  }

  @Test
  public void testMultiLineStringGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.MultiLineString(
            new Geometry.LineString(
                new Geometry.Point(10.0, 10.0),
                new Geometry.Point(20.0, 20.0),
                new Geometry.Point(10.0, 40.0)
            ),
            new Geometry.LineString(
                new Geometry.Point(40.0, 40.0),
                new Geometry.Point(30.0, 30.0),
                new Geometry.Point(40.0, 20.0),
                new Geometry.Point(30.0, 10.0)
            )
        ),
        objectMapper.readValue("""
            {
              "type": "MultiLineString", 
              "coordinates": [
                  [
                      [10.0, 10.0],
                      [20.0, 20.0],
                      [10.0, 40.0]
                  ],
                  [
                      [40.0, 40.0],
                      [30.0, 30.0],
                      [40.0, 20.0],
                      [30.0, 10.0]
                  ]
              ]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testMultiLineStringGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "MultiLineString", 
          "coordinates": [
              [
                  [10.0, 10.0],
                  [20.0, 20.0],
                  [10.0, 40.0]
              ],
              [
                  [40.0, 40.0],
                  [30.0, 30.0],
                  [40.0, 20.0],
                  [30.0, 10.0]
              ]
          ]
        }
        """,
        objectMapper.writeValueAsString(
            new Geometry.MultiLineString(
                new Geometry.LineString(
                    new Geometry.Point(10.0, 10.0),
                    new Geometry.Point(20.0, 20.0),
                    new Geometry.Point(10.0, 40.0)
                ),
                new Geometry.LineString(
                    new Geometry.Point(40.0, 40.0),
                    new Geometry.Point(30.0, 30.0),
                    new Geometry.Point(40.0, 20.0),
                    new Geometry.Point(30.0, 10.0)
                )
            )
        )
    );
  }

  @Test
  public void testPolygonGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.Polygon(
            new Geometry.LineString(
                new Geometry.Point(10.0, 10.0),
                new Geometry.Point(20.0, 20.0),
                new Geometry.Point(10.0, 40.0)
            ),
            new Geometry.LineString(
                new Geometry.Point(40.0, 40.0),
                new Geometry.Point(30.0, 30.0),
                new Geometry.Point(40.0, 20.0),
                new Geometry.Point(30.0, 10.0)
            )
        ),
        objectMapper.readValue("""
            {
              "type": "Polygon", 
              "coordinates": [
                  [
                      [10.0, 10.0],
                      [20.0, 20.0],
                      [10.0, 40.0]
                  ],
                  [
                      [40.0, 40.0],
                      [30.0, 30.0],
                      [40.0, 20.0],
                      [30.0, 10.0]
                  ]
              ]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testPolygonGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "Polygon", 
          "coordinates": [
              [
                  [10.0, 10.0],
                  [20.0, 20.0],
                  [10.0, 40.0]
              ],
              [
                  [40.0, 40.0],
                  [30.0, 30.0],
                  [40.0, 20.0],
                  [30.0, 10.0]
              ]
          ]
        }
        """,
        objectMapper.writeValueAsString(
            new Geometry.Polygon(
                new Geometry.LineString(
                    new Geometry.Point(10.0, 10.0),
                    new Geometry.Point(20.0, 20.0),
                    new Geometry.Point(10.0, 40.0)
                ),
                new Geometry.LineString(
                    new Geometry.Point(40.0, 40.0),
                    new Geometry.Point(30.0, 30.0),
                    new Geometry.Point(40.0, 20.0),
                    new Geometry.Point(30.0, 10.0)
                )
            )
        )
    );
  }

  @Test
  public void testMultiPolygonGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.MultiPolygon(
            new Geometry.Polygon(
                new Geometry.LineString(
                    new Geometry.Point(40.0, 40.0),
                    new Geometry.Point(20.0, 45.0),
                    new Geometry.Point(45.0, 30.0),
                    new Geometry.Point(40.0, 40.0)
                )
            ),
            new Geometry.Polygon(
                new Geometry.LineString(
                    new Geometry.Point(10.0, 10.0),
                    new Geometry.Point(20.0, 20.0),
                    new Geometry.Point(10.0, 40.0)
                ),
                new Geometry.LineString(
                    new Geometry.Point(40.0, 40.0),
                    new Geometry.Point(30.0, 30.0),
                    new Geometry.Point(40.0, 20.0),
                    new Geometry.Point(30.0, 10.0)
                )
            )
        ),
        objectMapper.readValue("""
            {
              "type": "MultiPolygon", 
              "coordinates": [
                  [
                      [
                          [40.0, 40.0],
                          [20.0, 45.0],
                          [45.0, 30.0],
                          [40.0, 40.0]
                      ]
                  ], 
                  [
                      [
                          [10.0, 10.0],
                          [20.0, 20.0],
                          [10.0, 40.0]
                      ],
                      [
                          [40.0, 40.0],
                          [30.0, 30.0],
                          [40.0, 20.0],
                          [30.0, 10.0]
                      ]
                  ]
              ]
            }
            """, Geometry.class
        )
    );
  }

  @Test
  public void testMultiPolygonGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "MultiPolygon", 
          "coordinates": [
              [
                  [
                      [40.0, 40.0],
                      [20.0, 45.0],
                      [45.0, 30.0],
                      [40.0, 40.0]
                  ]
              ], 
              [
                  [
                      [10.0, 10.0],
                      [20.0, 20.0],
                      [10.0, 40.0]
                  ],
                  [
                      [40.0, 40.0],
                      [30.0, 30.0],
                      [40.0, 20.0],
                      [30.0, 10.0]
                  ]
              ]
          ]
        }
        """,
        objectMapper.writeValueAsString(
            new Geometry.MultiPolygon(
                new Geometry.Polygon(
                    new Geometry.LineString(
                        new Geometry.Point(40.0, 40.0),
                        new Geometry.Point(20.0, 45.0),
                        new Geometry.Point(45.0, 30.0),
                        new Geometry.Point(40.0, 40.0)
                    )
                ),
                new Geometry.Polygon(
                    new Geometry.LineString(
                        new Geometry.Point(10.0, 10.0),
                        new Geometry.Point(20.0, 20.0),
                        new Geometry.Point(10.0, 40.0)
                    ),
                    new Geometry.LineString(
                        new Geometry.Point(40.0, 40.0),
                        new Geometry.Point(30.0, 30.0),
                        new Geometry.Point(40.0, 20.0),
                        new Geometry.Point(30.0, 10.0)
                    )
                )
          )
      )
    );
  }

  @Test
  public void testGeometryCollectionGeojsonDeserialization() throws JsonProcessingException {
    assertEquals(
        new Geometry.GeometryCollection(List.of(
            new Geometry.Point(40.0, 10.0),
            new Geometry.LineString(
                new Geometry.Point(10.0, 10.0),
                new Geometry.Point(20.0, 20.0),
                new Geometry.Point(10.0, 40.0)
            ),
            new Geometry.Polygon(
                new Geometry.LineString(
                    new Geometry.Point(40.0, 40.0),
                    new Geometry.Point(20.0, 45.0),
                    new Geometry.Point(45.0, 30.0),
                    new Geometry.Point(40.0, 40.0)
                )
            )
        )),
        objectMapper.readValue("""
            {
              "type": "GeometryCollection", 
              "geometries": [
                  {
                      "type": "Point",
                      "coordinates": [40.0, 10.0]
                  },
                  {
                      "type": "LineString",
                      "coordinates": [
                          [10.0, 10.0],
                          [20.0, 20.0],
                          [10.0, 40.0]
                      ]
                  },
                  {
                      "type": "Polygon",
                      "coordinates": [
                          [
                              [40.0, 40.0],
                              [20.0, 45.0],
                              [45.0, 30.0],
                              [40.0, 40.0]
                          ]
                      ]
                  }
              ]
            }
            """, Geometry.class
      )
    );
  }

  @Test
  public void testGeometryCollectionGeojsonSerialization() throws JsonProcessingException {
    assertJsonEquals("""
        {
          "type": "GeometryCollection",
          "geometries": [
              {
                  "type": "Point",
                  "coordinates": [40.0, 10.0]
              },
              {
                  "type": "LineString",
                  "coordinates": [
                      [10.0, 10.0],
                      [20.0, 20.0],
                      [10.0, 40.0]
                  ]
              },
              {
                  "type": "Polygon",
                  "coordinates": [
                      [
                          [40.0, 40.0],
                          [20.0, 45.0],
                          [45.0, 30.0],
                          [40.0, 40.0]
                      ]
                  ]
              }
          ]
        }
        """,
        objectMapper.writeValueAsString(
            new Geometry.GeometryCollection(List.of(
                new Geometry.Point(40.0, 10.0),
                new Geometry.LineString(
                    new Geometry.Point(10.0, 10.0),
                    new Geometry.Point(20.0, 20.0),
                    new Geometry.Point(10.0, 40.0)
                ),
                new Geometry.Polygon(
                    new Geometry.LineString(
                        new Geometry.Point(40.0, 40.0),
                        new Geometry.Point(20.0, 45.0),
                        new Geometry.Point(45.0, 30.0),
                        new Geometry.Point(40.0, 40.0)
                    )
                )
            ))
        )
    );
  }
}
