package imr.hi.search;

import java.util.List;
import no.ngu.geojson.Geojson;

public final class LocationSearchService {

    private final List<LocationSearchProvider> searchProviders;

    public LocationSearchService(List<LocationSearchProvider> searchProviders) {
        this.searchProviders = searchProviders;
    }

    public LocationSearchService(LocationSearchProvider... searchProviders) {
        this.searchProviders = List.of(searchProviders);
    }

    public List<Geojson.FeatureCollection> search(String q) {
        return searchProviders.stream()
                .map(provider -> provider.search(q))
                .toList();
    }
}
