package no.ngu.geojson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the EpsgCode class.
 */
public class EpsgCodeTest {
  
  @Test
  public void testFromString() {
    assertEquals(EpsgCode.epsg_4326, EpsgCode.fromString("EPSG:4326"));
    assertEquals(EpsgCode.epsg_5972, EpsgCode.fromString("EPSG_5972"));
    assertEquals(EpsgCode.epsg_25832, EpsgCode.fromString("epsg:25832"));
    assertEquals(EpsgCode.epsg_25836, EpsgCode.fromString("epsg_25836"));
  }

  @Test
  public void testToCrsNumber() {
    assertEquals(4326, EpsgCode.epsg_4326.toCrsNumber());
    assertEquals(5972, EpsgCode.epsg_5972.toCrsNumber());
    assertEquals(25836, EpsgCode.epsg_25836.toCrsNumber());
  }

  @Test
  public void testToCrsCode() {
    assertEquals("EPSG:4326", EpsgCode.epsg_4326.toCrsCode());
    assertEquals("EPSG:5972", EpsgCode.epsg_5972.toCrsCode());
    assertEquals("EPSG:25836", EpsgCode.epsg_25836.toCrsCode());
  }
}
