package no.ngu.kartverket.api.stedsnavn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record NavnResponse(Metadata metadata, List<Skrivemate> navn) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Metadata() {
  }

  public record Skrivemate(
    List<StedResponse.Fylke> fylker,
    List<StedResponse.Kommune> kommuner,
    String navneobjekttype,
    String navnestatus,
    StedResponse.Representasjonspunkt representasjonspunkt,
    String skrivemåte,
    String skrivemåtestatus,
    String språk,
    int stedsnummer,
    String stedstatus
  ) {
  }
}
