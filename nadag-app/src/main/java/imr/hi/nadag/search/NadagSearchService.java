package imr.hi.nadag.search;

import java.util.List;

public final class NadagSearchService {
    private final NadagRepository repository;

    public NadagSearchService(NadagRepository repository) {
        this.repository = repository;
    }

    public List<NadagProject> byProsjektnavnOrProsjektnr(String prosjektnavn, String prosjektnr) {
        return repository.byProsjektnavnOrProsjektnr(prosjektnavn, prosjektnr);
    }
}