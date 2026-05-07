package no.ngu.kartverket.api.adresser;

import java.io.IOException;

public interface AdresserService {

  SokResponse sok(String text, AdresserHttpService.Options options) throws IOException, InterruptedException;

  default SokResponse sok(String text) throws IOException, InterruptedException {
    return sok(text, new AdresserHttpService.Options());
  }
}
