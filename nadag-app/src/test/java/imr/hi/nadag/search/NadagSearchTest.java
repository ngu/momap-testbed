package imr.hi.nadag.search;

import imr.hi.mareano.database.Database;
import imr.hi.mareano.database.DatabaseConfig;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class NadagSearchTest {

    public static NadagProject sampleNadagProject = new NadagProject("5b66b711-5e3d-4182-970f-29998f951a17", null, "Gk00517");

    @Test
    void searchProsjektReturnsExpectedProjectRow() {
        String jdbcUrl = System.getenv("NADAG_DB_URL");
        assumeTrue(jdbcUrl != null && !jdbcUrl.isBlank(), "NADAG_DB_URL is not set");

        var nadagDatabase = new Database(new DatabaseConfig(jdbcUrl, null, 2));
        try {
            NadagRepository repository = new NadagRepository(nadagDatabase);
            List<NadagProject> result = repository.byProsjektnavnOrProsjektnr(
                sampleNadagProject.prosjektnavn(), null
            );
            assertTrue(
                result.contains(sampleNadagProject),
                "Expected project row was not found"
            );
        } finally {
            nadagDatabase.close();
        }
    }
}