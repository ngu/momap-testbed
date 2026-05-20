package no.ngu.nadag;

import static org.junit.jupiter.api.Assertions.assertTrue;

import imr.hi.mareano.database.Database;
import java.util.List;
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
    if (database != null) {
      database.close();
    }
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
}
