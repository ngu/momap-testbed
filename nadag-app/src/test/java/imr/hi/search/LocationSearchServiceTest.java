package imr.hi.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import imr.hi.kartverket.StedsnavnLocationServiceProvider;
import java.util.List;
import no.ngu.geojson.Geojson.FeatureCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LocationSearchServiceTest {

  private static LocationSearchService service;

  @BeforeAll
  static void setup() {
    service = new LocationSearchService(new StedsnavnLocationServiceProvider());
  }

  @Test
  void testSearch() {
    List<FeatureCollection> fcs = service.search("Stangvik");
    assertEquals(
        1,
        fcs.size(),
        "Expected one feature collection"
    );
  }
}
