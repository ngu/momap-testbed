export const LOCATION_SEARCH_ID = "right-menu-location-search";

import type {
	GeoJsonFeature as MapGeoJsonFeature,
	GeoJsonFeatureCollection as MapGeoJsonFeatureCollection,
} from "@mareano-frontend/extensions/mapNavigateEvent";

export type GeoJsonFeature = MapGeoJsonFeature;

export type GeoJsonFeatureCollection = MapGeoJsonFeatureCollection & {
	name?: string | null;
};
