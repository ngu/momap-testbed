package imr.hi.nadag;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import imr.hi.mareano.database.Database;
import imr.hi.mareano.database.DatabaseConfig;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NadagRepositoryTest {

  public static NadagProject sampleNadagProject = new NadagProject("5b66b711-5e3d-4182-970f-29998f951a17", null, "Gk00517", null);

  static NadagProject findNadagProject(List<NadagProject> projects, NadagProject target) {
    return projects.stream().filter(p -> {
      return target.lokalid() == null || p.lokalid().equals(target.lokalid())
          || target.prosjektnavn() == null || p.prosjektnavn().equals(target.prosjektnavn())
          || target.prosjektnr() == null || p.prosjektnr().equals(target.prosjektnr())
          || target.omradeGeoJson() == null || p.omradeGeoJson().equals(target.omradeGeoJson());
    }).findFirst().orElse(null);
  }

  public static Database createNadagDatabase() {
    String jdbcUrl = System.getenv("NADAG_DB_URL");
    assumeTrue(jdbcUrl != null && !jdbcUrl.isBlank(), "NADAG_DB_URL is not set");

    return new Database(new DatabaseConfig(jdbcUrl, null, 2));
  }

  private static Database database;
  private static NadagRepository repository;

  @BeforeAll
  static void setup() {
    database = createNadagDatabase();
    repository = new NadagRepository(database);
  }

  @AfterAll
  static void teardown() {
    database.close();
  }

  @Test
  void searchProsjektReturnsExpectedProjectRow() {
    List<NadagProject> result = repository.byProsjektnavnOrProsjektnr(
        sampleNadagProject.prosjektnavn(), null
    );
    assertTrue(
        findNadagProject(result, sampleNadagProject) != null,
        "Expected project row was not found"
    );
  }
}
