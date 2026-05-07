package no.ngu.kartverket.api.stedsnavn;

import java.io.IOException;

public interface StedsnavnService {

  StedResponse sted(String text, StedsnavnHttpService.Options options) throws IOException, InterruptedException;

  default StedResponse sted(String text) throws IOException, InterruptedException {
    return sted(text, new StedsnavnHttpService.Options());
  }

  NavnResponse navn(String text, StedsnavnHttpService.Options options) throws IOException, InterruptedException;

  default NavnResponse navn(String text) throws IOException, InterruptedException {
    return navn(text, new StedsnavnHttpService.Options());
  }
}
