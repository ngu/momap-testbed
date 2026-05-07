package no.ngu.kartverket.api.adresser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

public record SokResponse(Metadata metadata, List<OutputAdresse> adresser) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Metadata() {
  }

  public enum Objtype {
    Vegadresse, Matrikkeladresse
  }

  public record OutputAdresse(
      String adressenavn,
      String adressetekst,
      String adressetilleggsnavn,
      int adressekode,
      int nummer,
      String bokstav,
      String kommunenummer,
      String kommunenavn,
      int gardsnummer,
      int bruksnummer,
      int festenummer,
      int undernummer,
      List<String> bruksenhetsnummer,
      Objtype objtype,
      String poststed,
      String postnummer,
      String adressetekstutenadressetilleggsnavn,
      boolean stedfestingverifisert,
      GeomPoint representasjonspunkt,
      String oppdateringsdato,
      double meterDistanseTilPunkt
  ) {
  }

  public record GeomPoint(String epsg, double lat, double lon) {
  }
}
