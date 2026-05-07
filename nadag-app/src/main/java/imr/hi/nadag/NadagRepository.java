package imr.hi.nadag;

import imr.hi.mareano.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class NadagRepository {
    private final Database nadagDatabase;

    public NadagRepository(Database nadagDatabase) {
        this.nadagDatabase = nadagDatabase;
    }

    public List<NadagProject> byProsjektnavnOrProsjektnr(String prosjektnavn, String prosjektnr) {
        if (prosjektnavn == null && prosjektnr == null) {
            return List.of();
        }

        List<String> whereClauses = new ArrayList<>();
        List<String> params = new ArrayList<>();

        if (prosjektnavn != null) {
            whereClauses.add("prosjektnavn ILIKE ?");
            params.add("%" + prosjektnavn + "%");
        }

        if (prosjektnr != null) {
            whereClauses.add("prosjektnr ILIKE ?");
            params.add("%" + prosjektnr + "%");
        }

        String sql = """
            SELECT lokalid, prosjektnr, prosjektnavn, ST_AsGeoJSON(omrade) AS omrade_geojson
            FROM grunnundersokelser.geotekniskunders
            WHERE %s
            ORDER BY prosjektnavn
            LIMIT 100
            """.formatted(String.join(" OR ", whereClauses));

        try (Connection connection = nadagDatabase.getDatasource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                List<NadagProject> projects = new ArrayList<>();
                while (rs.next()) {
                    projects.add(new NadagProject(
                        rs.getString("lokalid"),
                        rs.getString("prosjektnr"),
                        rs.getString("prosjektnavn"),
                        rs.getString("omrade_geojson")
                    ));
                }
                return projects;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query Nadag projects", e);
        }
    }
}