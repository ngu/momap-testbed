package imr.hi.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import imr.hi.mareano.database.Database;
import imr.hi.nadag.NadagRepository;
import imr.hi.nadag.NadagRepositoryTest;
import imr.hi.nadag.NadagService;
import imr.hi.nadag.NadagServiceTest;
import java.util.List;
import no.ngu.geojson.Geojson.FeatureCollection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LocationSearchServiceTest {

  private static Database database;
  private static LocationSearchService service;

  @BeforeAll
  static void setup() {
    database = NadagRepositoryTest.createNadagDatabase();
    service = new LocationSearchService(new NadagService(new NadagRepository(database)));
  }

  @AfterAll
  static void teardown() {
    database.close();
  }

  @Test
  void testSearch() {
    List<FeatureCollection> fcs = service.search(NadagRepositoryTest.sampleNadagProject.prosjektnavn());
    assertEquals(
        1,
        fcs.size(),
        "Expected one feature collection"
    );
    NadagServiceTest.checkSampleSearchResult(fcs.get(0));
  }
}
