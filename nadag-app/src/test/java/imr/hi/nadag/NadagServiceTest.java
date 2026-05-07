package imr.hi.nadag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import imr.hi.mareano.database.Database;
import java.util.List;
import no.ngu.geojson.Geojson.FeatureCollection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NadagServiceTest {

  private static Database database;
  private static NadagService service;

  @BeforeAll
  static void setup() {
    database = NadagRepositoryTest.createNadagDatabase();
    service = new NadagService(new NadagRepository(database));
  }

  @AfterAll
  static void teardown() {
    database.close();
  }

  @Test
  void testByProsjektnavnOrProsjektnr() {
    List<NadagProject> result = service.byProsjektnavnOrProsjektnr(
        NadagRepositoryTest.sampleNadagProject.prosjektnavn(), null
    );
    assertTrue(
        NadagRepositoryTest.findNadagProject(result, NadagRepositoryTest.sampleNadagProject) != null,
        "Expected project row was not found"
    );
  }

  public static void checkSampleSearchResult(FeatureCollection fc) {
    assertFalse(
        fc.features().isEmpty(),
        "Expected feature collection was not found"
    );
    for (var feature : fc.features()) {
      var crs = feature.geometry().crs();
      assertEquals("name", crs.type());
      assertEquals("EPSG:25833", crs.properties().get("name"));
      var properties = feature.properties();
      assertNotNull(properties.get("prosjektnavn"));
    }
  }

  @Test
  void testSearch() {
    FeatureCollection fc = service.search(NadagRepositoryTest.sampleNadagProject.prosjektnavn());
    checkSampleSearchResult(fc);
  }
}
