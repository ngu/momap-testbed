package no.ngu.kartverket.api.stedsnavn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import no.ngu.geojson.Geometry;

public record StedResponse(Metadata metadata, List<Sok> navn) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Metadata(int utkoordsys) {
  }

  public record Sok(
    List<Fylke> fylker,
    List<Kommune> kommuner,
    String navneobjekttype,
    String oppdateringsdato,
    Geojson geojson,
    Representasjonspunkt representasjonspunkt,
    List<SkrivemateJson> stedsnavn,
    int stedsnummer,
    String stedstatus
  ) {
  }

  public record Fylke(String fylkesnavn, String fylkesnummer) {
  }

  public record Kommune(String kommunenavn, String kommunenummer) {
  }

  public record Geojson(Geometry geometry) {
  }

  @JsonIgnoreProperties("koordsys")
  public record Representasjonspunkt(int koordsys, double øst, double nord) {
  }

  public record SkrivemateJson(String navnestatus, String skrivemåte, String skrivemåtestatus, String språk, int stedsnavnnummer) {
  }
}
